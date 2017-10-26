package org.bdlions.inventory.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.dto.DTOPurchaseOrder;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.dto.ListPurchaseOrder;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.manager.Purchase;

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
        if(dtoPurchaseOrder == null || dtoPurchaseOrder.getEntityPurchaseOrder() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Purchase Order Info. Please try again later.");
            return response;
        }
        else if(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo() == null || dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Order no is required.");
            return response;
        }
        else if(dtoPurchaseOrder.getEntityPurchaseOrder().getSupplierUserId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Supplier. Please select a supplier.");
            return response;
        }
        else if(dtoPurchaseOrder.getProducts() == null || dtoPurchaseOrder.getProducts().isEmpty())
        {
            response.setSuccess(false);
            response.setMessage("Please select product for the purchase.");
            return response;
        }
        
        //check whether order no exists or not
        EntityPurchaseOrder tempEntityPurchaseOrder = new EntityPurchaseOrder();
        tempEntityPurchaseOrder.setOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
        EntityPurchaseOrder resultEntityPurchaseOrder = purchase.getEntityPurchaseOrderByOrderNo(tempEntityPurchaseOrder);
        if(resultEntityPurchaseOrder != null)
        {
            response.setSuccess(false);
            response.setMessage("Order No already exists or invalid.");
            return response;
        }
        
        if(purchase.addPurchaseOrderInfo(dtoPurchaseOrder))
        {
            response.setSuccess(true);
            response.setMessage("Purchase Order is added successfully.");
        }   
        else
        {
            response.setSuccess(false);
            response.setMessage("Unable to add Purchase Order. Please try again later..");
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
    
    @ClientRequest(action = ACTION.UPDATE_PURCHASE_ORDER_INFO)
    public ClientResponse updatePurchaseOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOPurchaseOrder dtoPurchaseOrder = gson.fromJson(packet.getPacketBody(), DTOPurchaseOrder.class);     
        Purchase purchase = new Purchase();
        if(dtoPurchaseOrder == null || dtoPurchaseOrder.getEntityPurchaseOrder() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Purchase Order Info. Please try again later.");
            return response;
        }
        else if(dtoPurchaseOrder.getEntityPurchaseOrder().getId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Purchase Order Info. Please try again later..");
            return response;
        }
        else if(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo() == null || dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Order no is required.");
            return response;
        }
        else if(dtoPurchaseOrder.getEntityPurchaseOrder().getSupplierUserId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Supplier. Please select a supplier.");
            return response;
        }
        else if(dtoPurchaseOrder.getProducts() == null || dtoPurchaseOrder.getProducts().size() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Please select product for the purchase.");
            return response;
        }
        
        //check whether order no exists or not
        EntityPurchaseOrder tempEntityPurchaseOrder = new EntityPurchaseOrder();
        tempEntityPurchaseOrder.setOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
        EntityPurchaseOrder resultEntityPurchaseOrder = purchase.getEntityPurchaseOrderByOrderNo(tempEntityPurchaseOrder);
        if(resultEntityPurchaseOrder != null && resultEntityPurchaseOrder.getId() != dtoPurchaseOrder.getEntityPurchaseOrder().getId())
        {
            response.setSuccess(false);
            response.setMessage("Order No already exists or invalid.");
            return response;
        }
        
        if(dtoPurchaseOrder.getEntityPurchaseOrder().getId() > 0)
        {
            if(purchase.updatePurchaseOrderInfo(dtoPurchaseOrder))
            {
                response.setSuccess(true);
                response.setMessage("Purchase order is updated successfully.");
            }
            else
            {
                response.setSuccess(false);
                response.setMessage("Invalid Purchase Order Info. Please try again later...");
            }
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Invalid Purchase Order Info. Please try again later....");
        }        
        return response;
    }
}
