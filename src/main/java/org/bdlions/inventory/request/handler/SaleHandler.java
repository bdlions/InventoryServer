package org.bdlions.inventory.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.List;
import org.bdlions.inventory.dto.DTOSaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.dto.ListSaleOrder;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.manager.Sale;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class SaleHandler {

    private final ISessionManager sessionManager;

    public SaleHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.ADD_SALE_ORDER_INFO)
    public ClientResponse addSaleOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOSaleOrder dtoSaleOrder = gson.fromJson(packet.getPacketBody(), DTOSaleOrder.class);     
        Sale sale = new Sale();
        if(dtoSaleOrder == null || dtoSaleOrder.getEntitySaleOrder() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Sale Order Info. Please try again later.");
            return response;
        }
        else if(dtoSaleOrder.getEntitySaleOrder().getOrderNo() == null || dtoSaleOrder.getEntitySaleOrder().getOrderNo().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Order no is required.");
            return response;
        }
        else if(dtoSaleOrder.getEntitySaleOrder().getCustomerUserId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Customer. Please select a customer.");
            return response;
        }
        else if(dtoSaleOrder.getProducts() == null || dtoSaleOrder.getProducts().isEmpty())
        {
            response.setSuccess(false);
            response.setMessage("Please select product for the sale.");
            return response;
        }
        
        EntitySaleOrder tempEntitySaleOrder = new EntitySaleOrder();
        tempEntitySaleOrder.setOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        EntitySaleOrder resultEntitySaleOrder = sale.getEntitySaleOrderByOrderNo(tempEntitySaleOrder);
        if(resultEntitySaleOrder != null)
        {
            response.setSuccess(false);
            response.setMessage("Order No already exists or invalid.");
            return response;
        }
        
        if(sale.addSaleOrderInfo(dtoSaleOrder))
        {
            response.setSuccess(true);
            response.setMessage("Sale Order is added successfully.");
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Unable to add Sale Order. Please try again later..");
        }
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_SALE_ORDER_INFO)
    public ClientResponse getSaleOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSaleOrder dtoSaleOrder = gson.fromJson(packet.getPacketBody(), DTOSaleOrder.class);     
        Sale sale = new Sale();
        DTOSaleOrder saleOrder = sale.getSaleOrderInfo(dtoSaleOrder);
        if(saleOrder != null)
        {
            saleOrder.setSuccess(true);
            return saleOrder;
        }
        else
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid sale order.");
            return response;
        }
    }
    
    @ClientRequest(action = ACTION.FETCH_SALE_ORDERS)
    public ClientResponse getSaleOrders(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSaleOrder dtoSaleOrder = gson.fromJson(packet.getPacketBody(), DTOSaleOrder.class);     
        Sale sale = new Sale();
        List<DTOSaleOrder> saleOrders = sale.getSaleOrders(dtoSaleOrder);
        ListSaleOrder listSaleOrder = new ListSaleOrder();
        listSaleOrder.setSaleOrders(saleOrders);
        listSaleOrder.setSuccess(true);
        return listSaleOrder;
    }
    
    @ClientRequest(action = ACTION.UPDATE_SALE_ORDER_INFO)
    public ClientResponse updateSaleOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOSaleOrder dtoSaleOrder = gson.fromJson(packet.getPacketBody(), DTOSaleOrder.class);     
        Sale sale = new Sale();
        if(dtoSaleOrder == null || dtoSaleOrder.getEntitySaleOrder() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Sale Order Info. Please try again later.");
            return response;
        }
        else if(dtoSaleOrder.getEntitySaleOrder().getId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Sale Order Info. Please try again later..");
            return response;
        }
        else if(dtoSaleOrder.getEntitySaleOrder().getOrderNo() == null || dtoSaleOrder.getEntitySaleOrder().getOrderNo().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Order no is required.");
            return response;
        }
        else if(dtoSaleOrder.getEntitySaleOrder().getCustomerUserId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Customer. Please select a customer.");
            return response;
        }
        else if(dtoSaleOrder.getProducts() == null || dtoSaleOrder.getProducts().isEmpty())
        {
            response.setSuccess(false);
            response.setMessage("Please select product for the sale.");
            return response;
        }
        
        EntitySaleOrder tempEntitySaleOrder = new EntitySaleOrder();
        tempEntitySaleOrder.setOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        EntitySaleOrder resultEntitySaleOrder = sale.getEntitySaleOrderByOrderNo(tempEntitySaleOrder);
        if(resultEntitySaleOrder != null && resultEntitySaleOrder.getId() != dtoSaleOrder.getEntitySaleOrder().getId())
        {
            response.setSuccess(false);
            response.setMessage("Order No already exists or invalid.");
            return response;
        }
        
        if(dtoSaleOrder.getEntitySaleOrder().getId() > 0)
        {
            if(sale.updateSaleOrderInfo(dtoSaleOrder))
            {
                response.setSuccess(true);
                response.setMessage("Sale order is updated successfully.");
            }
            else 
            {
                response.setSuccess(false);
                response.setMessage("Invalid Sale Order Info. Please try again later...");
            }
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Invalid Sale Order Info. Please try again later....");
        }
        return response;
    }
}
