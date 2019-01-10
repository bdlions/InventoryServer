package org.bdlions.inventory.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.dto.DTOPurchaseOrder;
import org.bdlions.inventory.dto.DTOPurchaseOrderPayment;
import org.bdlions.inventory.dto.ListPurchaseOrder;
import org.bdlions.inventory.dto.ListPurchaseOrderPayment;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntityPurchaseOrderPayment;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrder;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrderPayment;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class PurchaseReportHandler {
    private final Logger logger = LoggerFactory.getLogger(PurchaseReportHandler.class);
    private final ISessionManager sessionManager;

    public PurchaseReportHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.FETCH_PURCHASE_ORDER_SUMMARY)
    public ClientResponse getPurchaseOrderSummary(ISession session, IPacket packet) throws Exception 
    {
        ListPurchaseOrder listPurchaseOrder = new ListPurchaseOrder();
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);        
        String startDate = jsonObject.get("startDate").getAsString();
        String endDate = jsonObject.get("endDate").getAsString();
        if(StringUtils.isNullOrEmpty(startDate))
        {
            startDate = TimeUtils.getCurrentDate("", "");
        }
        if(StringUtils.isNullOrEmpty(endDate))
        {
            endDate = TimeUtils.getCurrentDate("", "");
        }
        long startTime = 0;
        long endTime = 0;
        startTime = TimeUtils.convertHumanToUnix(startDate, "", "");
        endTime = TimeUtils.convertHumanToUnix(endDate, "", "") + 86400;
        
        String offsetString = jsonObject.get("offset").getAsString();
        String limitString = jsonObject.get("limit").getAsString();
        String userIdString = jsonObject.get("userId").getAsString();
        int offset = 0;
        int limit = 0;
        int userId = -1;
        try
        {
            offset = Integer.parseInt(offsetString);
            limit = Integer.parseInt(limitString);    
            userId = Integer.parseInt(userIdString); 
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid request to get sale list.");
            return response;
        }
        
        List<DTOPurchaseOrder> purchaseOrders = new ArrayList<>();
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder(packet.getPacketHeader().getAppId());
        List<EntityPurchaseOrder> entityPurchaseOrders =  entityManagerPurchaseOrder.getPurchaseOrdersDQ(startTime, endTime, userId, offset, limit);
        if(entityPurchaseOrders != null)
        {
            
            for (int counter = 0; counter < entityPurchaseOrders.size(); counter++) 
            {
                EntityPurchaseOrder entityPurchaseOrder = entityPurchaseOrders.get(counter);
                DTOPurchaseOrder dtoPO = new DTOPurchaseOrder();
                dtoPO.setEntityPurchaseOrder(entityPurchaseOrder);
                dtoPO.setOrderDate(TimeUtils.convertUnixToHuman(entityPurchaseOrder.getCreatedOn(), "", ""));
                dtoPO.setInvoiceDate(TimeUtils.convertUnixToHuman(entityPurchaseOrder.getInvoiceOn(), "", ""));
                purchaseOrders.add(dtoPO);
            }
        }        
        listPurchaseOrder.setPurchaseOrders(purchaseOrders);
        listPurchaseOrder.setTotalPurchaseOrders(entityManagerPurchaseOrder.getTotalPurchaseOrdersDQ(startTime, endTime, userId));
        listPurchaseOrder.setTotalPurchaseAmount(entityManagerPurchaseOrder.getTotalPurchaseAmountDQ(startTime, endTime, userId));
        listPurchaseOrder.setSuccess(true);
        return listPurchaseOrder;
    }
    
    @ClientRequest(action = ACTION.FETCH_PURCHASE_ORDER_PAYMENT_SUMMARY)
    public ClientResponse getPurchaseOrderPaymentSummary(ISession session, IPacket packet) throws Exception 
    {
        ListPurchaseOrderPayment response = new ListPurchaseOrderPayment();
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);  
        int supplierUserId = jsonObject.get("supplierUserId").getAsInt();
        String strPaymentTypeIds = jsonObject.get("paymentTypeIds").getAsString();
        List<Integer> paymentTypeIds = new ArrayList<>();
        if(!StringUtils.isNullOrEmpty(strPaymentTypeIds))
        {
            String[] paymentTypeIdList = strPaymentTypeIds.split(",");
            for(String paymentTypeId: paymentTypeIdList)
            {
                paymentTypeId = paymentTypeId.trim();
                try
                {
                    paymentTypeIds.add(Integer.parseInt(paymentTypeId));
                }
                catch(Exception ex)
                {
                    logger.debug("Invalid paymentTypeIds : " + strPaymentTypeIds);
                    logger.debug("Exception : " + ex.toString());
                    paymentTypeIds = new ArrayList<>();
                    break;
                }
            }
        }
        //handle start time, end time, unix payment start time and unix payment end time later
        int offset = jsonObject.get("offset").getAsInt();
        int limit = jsonObject.get("limit").getAsInt();

        List<DTOPurchaseOrderPayment> purchaseOrderPayments = new ArrayList<>();
        EntityManagerPurchaseOrderPayment entityManagerPurchaseOrderPayment = new EntityManagerPurchaseOrderPayment(packet.getPacketHeader().getAppId());
        List<EntityPurchaseOrderPayment> entityPurchaseOrderPayments =  entityManagerPurchaseOrderPayment.getPurchaseOrderPaymentsDQ(paymentTypeIds, supplierUserId, 0, 0, 0, 0, offset, limit);
        if(entityPurchaseOrderPayments != null)
        {
            
            for (int counter = 0; counter < entityPurchaseOrderPayments.size(); counter++) 
            {
                EntityPurchaseOrderPayment entityPurchaseOrderPayment = entityPurchaseOrderPayments.get(counter);
                DTOPurchaseOrderPayment dtoPOP = new DTOPurchaseOrderPayment();
                dtoPOP.setEntityPurchaseOrderPayment(entityPurchaseOrderPayment);
                dtoPOP.setCreatedDate(TimeUtils.convertUnixToHuman(entityPurchaseOrderPayment.getCreatedOn(), "", ""));
                purchaseOrderPayments.add(dtoPOP);
            }
        }        
        response.setPurchaseOrderPayments(purchaseOrderPayments);
        response.setTotalPurchaseOrderPayments(entityManagerPurchaseOrderPayment.getTotalPurchaseOrderPaymentsDQ(paymentTypeIds, supplierUserId, 0, 0, 0, 0));
        response.setTotalPaymentAmount(entityManagerPurchaseOrderPayment.getTotalPaymentAmountDQ(paymentTypeIds, supplierUserId, 0, 0, 0, 0));
        response.setSuccess(true);
        
        return response;
    }
}
