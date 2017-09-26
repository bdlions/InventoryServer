package org.bdlions.util;

import org.bdlions.util.StringUtils;
import com.bdlions.dto.response.ClientResponse;
import org.bdlions.exceptions.InvalidRequestException;
import org.bdlions.transport.packet.IPacket;
import org.bdlions.transport.packet.IPacketHeader;
import org.bdlions.request.handler.AuthHandler;
import org.bdlions.request.handler.RequestExecutorInfo;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import org.bdlions.util.annotation.ClientRequest;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bdlions.request.handler.CustomerHandler;
import org.bdlions.request.handler.ProductHandler;
import org.bdlions.request.handler.UserHandler;
import org.bdlions.session.UserSessionManagerImpl;
import org.bdlions.util.handler.request.IClientRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nazmul hasan
 */
public class ClientRequestHandler implements IClientRequestHandler{

    private final Logger logger = LoggerFactory.getLogger(ClientRequestHandler.class);
    private final Map<REQUEST_TYPE, Map<Integer, RequestExecutorInfo>> actionMap = new HashMap<>();
    private final UserSessionManagerImpl sessionManager;

    private static ClientRequestHandler instance;

    private ClientRequestHandler() {
        sessionManager = new UserSessionManagerImpl(new DBUserProvider());
        buildActionMap();
    }

    public static ClientRequestHandler getInstance() {
        if (instance == null) {
            instance = new ClientRequestHandler();
        }
        return instance;
    }
    public UserSessionManagerImpl getSessionManager(){
        return sessionManager;
    }

    private void buildActionMap() {
        try {
            //process to get functions
            Class[] delegators = getDelegators();

            for (Class delegator : delegators) {
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                /**
                 * get all methods from the class
                 */
                for (Method method : delegator.getDeclaredMethods()) {

                    if (method.isAnnotationPresent(ClientRequest.class)) {

                        /**
                         * *
                         * only add into action map if method is declared as
                         * client request
                         */
                        ClientRequest clientRequest = (ClientRequest) method.getAnnotation(ClientRequest.class);

                        /**
                         * get the client action
                         */
                        ACTION action = clientRequest.action();

                        RequestExecutorInfo requestExecutor = new RequestExecutorInfo();
                        /**
                         * Define method signature
                         */
                        MethodType constructorMethodType = MethodType.methodType(void.class, ISessionManager.class);

                        MethodHandle constructor = lookup.findConstructor(delegator, constructorMethodType);
                        requestExecutor.setInstance(constructor.invoke(sessionManager));

                        MethodType methodType = MethodType.methodType(ClientResponse.class, ISession.class, IPacket.class);
                        /**
                         * creating method for runtime invoke
                         */
                        MethodHandle methodHandle = lookup.findVirtual(delegator, method.getName(), methodType);

                        requestExecutor.setMethod(methodHandle);
                        /**
                         * adding to the actionMap
                         */
                        putRequest(action, requestExecutor);
                    }
                }
            }

        } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException e) {
            logger.error("exception in building action map " + e.getMessage(), e);
        } catch (Throwable ex) {
            java.util.logging.Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void putRequest(ACTION action, RequestExecutorInfo method) {
        REQUEST_TYPE socketType = action.getRequestType();
        int actionId = action.getId();

        if (!actionMap.containsKey(socketType)) {
            actionMap.put(socketType, new HashMap<>());
        }
        actionMap.get(socketType).put(actionId, method);
    }

    private Class[] getDelegators() {
        Class[] delegators = new Class[]{AuthHandler.class, UserHandler.class, ProductHandler.class, CustomerHandler.class};
        return delegators;
    }

    private RequestExecutorInfo getRequest(REQUEST_TYPE requestType, ACTION action) {
//        SOCKET_TYPE socketType = action.getSocketType();
        int actionId = action.getId();

        if (actionMap.containsKey(requestType) && actionMap.get(requestType).containsKey(actionId)) {
            return actionMap.get(requestType).get(actionId);
        }
        return null;
    }

    @Override
    public Object executeRequest(IPacket packet) throws InvalidRequestException, Throwable {
        logger.debug("Request is going to be processed. starts..");
        if (packet != null) {
            IPacketHeader packetHeader = packet.getPacketHeader();
            Gson gson = new GsonBuilder().create();
            logger.debug(gson.toJson(packetHeader));
            if (packetHeader.getAction() == null || packetHeader.getAction().getId() <= 0) {                
                logger.debug("invalid action found.");
                throw new InvalidRequestException();
            }

            RequestExecutorInfo clientRequestExecutorInfo = getRequest(packetHeader.getRequestType(), packetHeader.getAction());

            ISession session = null;
            String sessionId = packetHeader.getSessionId();
            logger.debug("session id : " + sessionId);
            if(!StringUtils.isNullOrEmpty(sessionId)){
                session = sessionManager.getSessionBySessionId(sessionId);
            }
            if (clientRequestExecutorInfo != null) {
                //execute request
                logger.debug("method for action : " + packetHeader.getAction() + " is " + clientRequestExecutorInfo.toString());
                return clientRequestExecutorInfo.getMethod().invoke(clientRequestExecutorInfo.getInstance(), session, packet);
            } else {
                logger.debug("No method is found for action : " + packetHeader.getAction() + " , socket : " + packetHeader.getRequestType());
                throw new InvalidRequestException();
            }
        } 
        else 
        {
            logger.debug("packet is null.");
            throw new InvalidRequestException();
        }
    }

}
