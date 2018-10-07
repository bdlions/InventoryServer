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
import org.bdlions.inventory.dto.DTOPurchaseOrderPayment;
import org.bdlions.inventory.dto.DTOSaleOrder;
import org.bdlions.inventory.dto.DTOSaleOrderPayment;
import org.bdlions.inventory.entity.EntityPurchaseOrderPayment;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderPayment;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrderPayment;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrder;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderPayment;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class SaleReportHandler {

    private final ISessionManager sessionManager;

    public SaleReportHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.FETCH_SALE_ORDER_SUMMARY)
    public ClientResponse getSaleOrderSummary(ISession session, IPacket packet) throws Exception 
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
        startTime = TimeUtils.convertHumanToUnix(startDate, "", "");
        endTime = TimeUtils.convertHumanToUnix(endDate, "", "") + 86400;
        
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
        
        List<DTOSaleOrder> saleOrders = new ArrayList<>();
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder(packet.getPacketHeader().getAppId());
        List<EntitySaleOrder> entitySaleOrders =  entityManagerSaleOrder.getSaleOrdersDQ(startTime, endTime, offset, limit);
        if(entitySaleOrders != null)
        {
            
            for (int counter = 0; counter < entitySaleOrders.size(); counter++) 
            {
                EntitySaleOrder entitySaleOrder = entitySaleOrders.get(counter);
                DTOSaleOrder dtoSO = new DTOSaleOrder();
                dtoSO.setEntitySaleOrder(entitySaleOrder);
                dtoSO.setOrderDate(TimeUtils.convertUnixToHuman(entitySaleOrder.getCreatedOn(), "", ""));
                saleOrders.add(dtoSO);
            }
        }        
        response.setList(saleOrders);
        response.setCounter(entityManagerSaleOrder.getTotalSaleOrdersDQ(startTime, endTime));
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_SALE_ORDER_PAYMENT_SUMMARY)
    public ClientResponse getSaleOrderPaymentSummary(ISession session, IPacket packet) throws Exception 
    {
        ClientListResponse response = new ClientListResponse();
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);  
        int customerUserId = jsonObject.get("customerUserId").getAsInt();
        int paymentTypeId = jsonObject.get("paymentTypeId").getAsInt();
        //handle start time, end time, unix payment start time and unix payment end time later
        int offset = jsonObject.get("offset").getAsInt();
        int limit = jsonObject.get("limit").getAsInt();

        List<DTOSaleOrderPayment> saleOrderPayments = new ArrayList<>();
        EntityManagerSaleOrderPayment entityManagerSaleOrderPayment = new EntityManagerSaleOrderPayment(packet.getPacketHeader().getAppId());
        List<EntitySaleOrderPayment> entitySaleOrderPayments =  entityManagerSaleOrderPayment.getSaleOrderPaymentsDQ(customerUserId, paymentTypeId, 0, 0, 0, 0, offset, limit);
        if(entitySaleOrderPayments != null)
        {
            
            for (int counter = 0; counter < entitySaleOrderPayments.size(); counter++) 
            {
                EntitySaleOrderPayment entitySaleOrderPayment = entitySaleOrderPayments.get(counter);
                DTOSaleOrderPayment dtoSOP = new DTOSaleOrderPayment();
                dtoSOP.setEntitySaleOrderPayment(entitySaleOrderPayment);
                dtoSOP.setCreatedDate(TimeUtils.convertUnixToHuman(entitySaleOrderPayment.getCreatedOn(), "", ""));
                saleOrderPayments.add(dtoSOP);
            }
        }        
        response.setList(saleOrderPayments);
        response.setCounter(entityManagerSaleOrderPayment.getTotalSaleOrderPaymentsDQ(customerUserId, paymentTypeId, 0, 0, 0, 0));
        response.setSuccess(true);
        
        return response;
    }
}
