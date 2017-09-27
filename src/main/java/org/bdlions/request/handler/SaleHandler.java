package org.bdlions.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.List;
import org.bdlions.dto.DTOSaleOrder;
import org.bdlions.dto.ListSaleOrder;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.manager.Sale;

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
        if(sale.addSaleOrderInfo(dtoSaleOrder))
        {
            response.setSuccess(true);
            response.setMessage("Sale order is added successfully.");
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
}
