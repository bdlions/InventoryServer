package org.bdlions.inventory.request.handler;

import com.bdlions.dto.response.ClientListResponse;
import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.dto.DTOPurchaseOrder;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrder;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class PurchaseReportHandler {

    private final ISessionManager sessionManager;

    public PurchaseReportHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.FETCH_PURCHASE_ORDER_SUMMARY)
    public ClientResponse getPurchaseOrderSummary(ISession session, IPacket packet) throws Exception 
    {
        ClientListResponse response = new ClientListResponse();
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
        startTime = TimeUtils.convertHumanToUnix(startDate);
        endTime = TimeUtils.convertHumanToUnix(endDate) + 86400;
        
        String offsetString = jsonObject.get("offset").getAsString();
        String limitString = jsonObject.get("limit").getAsString();
        int offset = 0;
        int limit = 0;
        try
        {
            offset = Integer.parseInt(offsetString);
            limit = Integer.parseInt(limitString);            
        }
        catch(Exception ex)
        {
            response.setSuccess(false);
            response.setMessage("Invalid request to get sale list.");
            return response;
        }
        
        List<DTOPurchaseOrder> purchaseOrders = new ArrayList<>();
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
        List<EntityPurchaseOrder> entityPurchaseOrders =  entityManagerPurchaseOrder.getPurchaseOrdersDQ(startTime, endTime, offset, limit);
        if(entityPurchaseOrders != null)
        {
            
            for (int counter = 0; counter < entityPurchaseOrders.size(); counter++) 
            {
                EntityPurchaseOrder entityPurchaseOrder = entityPurchaseOrders.get(counter);
                DTOPurchaseOrder dtoPO = new DTOPurchaseOrder();
                dtoPO.setEntityPurchaseOrder(entityPurchaseOrder);
                dtoPO.setOrderDate(TimeUtils.convertUnixToHuman(entityPurchaseOrder.getCreatedOn(), "", ""));
                purchaseOrders.add(dtoPO);
            }
        }        
        response.setList(purchaseOrders);
        response.setCounter(entityManagerPurchaseOrder.getTotalPurchaseOrdersDQ(startTime, endTime));
        response.setSuccess(true);
        return response;
    }
    
    
}
