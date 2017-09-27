package org.bdlions.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.bdlions.dto.DTOPurchaseOrder;
import org.bdlions.dto.ListPurchaseOrder;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.manager.Purchase;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class PurchaseHandler {

    private final ISessionManager sessionManager;

    public PurchaseHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.ADD_PURCHASE_ORDER_INFO)
    public ClientResponse addPurchaseOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOPurchaseOrder dtoPurchaseOrder = gson.fromJson(packet.getPacketBody(), DTOPurchaseOrder.class);     
        Purchase purchase = new Purchase();
        if(purchase.addPurchaseOrderInfo(dtoPurchaseOrder))
        {
            response.setSuccess(true);
            response.setMessage("Purchase order is added successfully.");
        }        
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_PURCHASE_ORDER_INFO)
    public ClientResponse getPurchaseOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        
        Gson gson = new Gson();
        DTOPurchaseOrder dtoPurchaseOrder = gson.fromJson(packet.getPacketBody(), DTOPurchaseOrder.class);     
        Purchase purchase = new Purchase();
        DTOPurchaseOrder purchaseOrder = purchase.getPurchaseOrderInfo(dtoPurchaseOrder);
        if(purchaseOrder != null)
        {
            purchaseOrder.setSuccess(true);
            return purchaseOrder;
        } 
        else
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid purchase order.");
            return response;
        }
    }
    
    @ClientRequest(action = ACTION.FETCH_PURCHASE_ORDERS)
    public ClientResponse getPurchaseOrders(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOPurchaseOrder dtoPurchaseOrder = gson.fromJson(packet.getPacketBody(), DTOPurchaseOrder.class);     
        Purchase purchase = new Purchase();
        List<DTOPurchaseOrder> purchaseOrders = purchase.getPurchaseOrders(dtoPurchaseOrder);
        ListPurchaseOrder listPurchaseOrder = new ListPurchaseOrder();
        listPurchaseOrder.setSuccess(true);
        listPurchaseOrder.setPurchaseOrders(purchaseOrders);
        return listPurchaseOrder;
    }
}
