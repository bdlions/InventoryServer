package org.bdlions.inventory.request.handler;

import com.bdlions.dto.Credential;
import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.commons.ClientMessages;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.SignInResponse;
import org.bdlions.util.StringUtils;
import org.bdlions.util.annotation.ClientRequest;
import com.google.gson.Gson;
import org.bdlions.inventory.packet.PacketHeaderImpl;
import org.bdlions.transport.packet.IPacketHeader;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class AuthHandler {

    private final ISessionManager sessionManager;

    public AuthHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.SIGN_UP)
    public ClientResponse signUp(ISession session, IPacket packet) throws Exception 
    {
        SignInResponse response = new SignInResponse();
        return response;
    }
    
    @ClientRequest(action = ACTION.SIGN_IN)
    public ClientResponse signIn(ISession session, IPacket packet) throws Exception 
    {

        SignInResponse response = new SignInResponse();
        if(session != null){
            response.setMessage(ClientMessages.ALREADY_LOGGED_IN);
            response.setSessionId(session.getSessionId());
            response.setUserName(session.getUserName());
            response.setSuccess(true);
            return response;
        }
        if (StringUtils.isNullOrEmpty(packet.getPacketBody())) {
            response.setMessage(ClientMessages.INVALID_SIGNIN_REQUEST_FORMAT);
            response.setSuccess(false);
            return response;
        }

        Gson gson = new Gson();
        Credential credential = gson.fromJson(packet.getPacketBody(), Credential.class);

        if (StringUtils.isNullOrEmpty(credential.getUserName())) {
            response.setMessage(ClientMessages.USER_NAME_IS_MANDATORY);
            response.setSuccess(false);
            return response;
        }
        if (StringUtils.isNullOrEmpty(credential.getPassword())) {
            response.setMessage(ClientMessages.PASSWORD_IS_MANDATORY);
            response.setSuccess(false);
            return response;
        }

//        try{
//            session = sessionManager.createSession(credential);
//        }catch(UnknownAccountException uae){
//            response.setMessage(ClientMessages.INVALID_CREDENTIAL);
//            response.setSuccess(false);
//            return response;
//        }
        try{
            credential.setAppId(packet.getPacketHeader().getAppId());
            session = sessionManager.createSession(credential);
        }catch(Exception ex){
            response.setMessage(ClientMessages.INVALID_CREDENTIAL);
            response.setSuccess(false);
            return response;
        }
        
        
        if(session == null){
            response.setMessage(ClientMessages.INVALID_CREDENTIAL);
            response.setSuccess(false);
            return response;
        }
        
        session.setRemotePort(packet.getRemotePort());
        session.setRemoteIP(packet.getRemoteIP());
        response.setSessionId((String) session.getSessionId());
        response.setUserName(credential.getUserName());
        response.setFullName(credential.getFirstName() + " " + credential.getLastName());
        response.setSuccess(true);
        

        return response;
    }
    
    @ClientRequest(action = ACTION.SIGN_OUT)
    public ClientResponse signOut(ISession session, IPacket packet) throws Exception 
    {
        if(session != null)
        {
            String sessionId = session.getSessionId();
            String userName = session.getUserName();
            try
            {
                sessionManager.removeDeviceSession(userName, 0, sessionId);
                sessionManager.destroySession(sessionId);
            }
            catch(Exception ex)
            {
                //add exception in log file
            }
        }
        SignInResponse response = new SignInResponse();
        response.setMessage("Sign out successful");
        response.setSuccess(true);
        return response;
    }
    
    /*@ClientRequest(action = ACTION.UPDATE_PROFILE_INFO)
    public ClientResponse updateProfile(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        EntityProfile profile = gson.fromJson(packet.getPacketBody(), EntityProfile.class);
        Profile profileManager = new Profile();
        SignInResponse response = new SignInResponse();
        if(profile != null && profile.getUser() != null)
        {
            if(profileManager.updateProfile(profile))
            {
                response.setMessage("Profile is updated successfully.");
                response.setSuccess(true);
            }
            else
            {
                response.setMessage("Unable to update profile info. Please try again later.");
                response.setSuccess(false);
            }
            
        }
        else
        {
            response.setMessage("Invalid params to update user profile. Please try again later.");
            response.setSuccess(false);
        } 
        return response;
    }*/
}
