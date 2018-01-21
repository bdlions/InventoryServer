package org.bdlions.inventory.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.dto.DTOSaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.dto.ListSaleOrder;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntitySaleOrderProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrder;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderProduct;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.manager.Stock;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.util.Constants;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;

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
        DTOSaleOrder responseDTOSaleOrder = new DTOSaleOrder();
        //GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOSaleOrder dtoSaleOrder = gson.fromJson(packet.getPacketBody(), DTOSaleOrder.class);     
        if(dtoSaleOrder == null || dtoSaleOrder.getEntitySaleOrder() == null)
        {
            responseDTOSaleOrder.setSuccess(false);
            responseDTOSaleOrder.setMessage("Invalid Sale Order Info. Please try again later.");
            return responseDTOSaleOrder;
        }
        else if(dtoSaleOrder.getEntitySaleOrder().getOrderNo() == null || dtoSaleOrder.getEntitySaleOrder().getOrderNo().equals(""))
        {
            responseDTOSaleOrder.setSuccess(false);
            responseDTOSaleOrder.setMessage("Order no is required.");
            return responseDTOSaleOrder;
        }
        else if(dtoSaleOrder.getEntitySaleOrder().getCustomerUserId() <= 0)
        {
            responseDTOSaleOrder.setSuccess(false);
            responseDTOSaleOrder.setMessage("Invalid Customer. Please select a customer.");
            return responseDTOSaleOrder;
        }
        else if(dtoSaleOrder.getProducts() == null || dtoSaleOrder.getProducts().isEmpty())
        {
            responseDTOSaleOrder.setSuccess(false);
            responseDTOSaleOrder.setMessage("Please select product for the sale.");
            return responseDTOSaleOrder;
        }        
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        EntitySaleOrder resultEntitySaleOrder = entityManagerSaleOrder.getSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        if(resultEntitySaleOrder != null)
        {
            responseDTOSaleOrder.setSuccess(false);
            responseDTOSaleOrder.setMessage("Order No already exists or invalid.");
            return responseDTOSaleOrder;
        }
        
        List<DTOProduct> products = dtoSaleOrder.getProducts();
        int totalProducts = products.size();
        List<EntitySaleOrderProduct> entitySaleOrderProducts = new ArrayList<>();
        List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
        List<Integer> productIds = new ArrayList<>();
        HashMap<Integer, Double> productIdQuantityMap = new HashMap<>();
        for(int counter = 0; counter < totalProducts; counter++)
        {
            DTOProduct dtoProduct = products.get(counter);
            if(dtoProduct != null && dtoProduct.getEntityProduct() != null && dtoProduct.getEntityProduct().getId() > 0)
            {
                productIds.add(dtoProduct.getEntityProduct().getId());
                if(!productIdQuantityMap.containsKey(dtoProduct.getEntityProduct().getId()))
                {
                    productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), dtoProduct.getQuantity());
                }
                
                EntitySaleOrderProduct entitySaleOrderProduct = new EntitySaleOrderProduct();
                entitySaleOrderProduct.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                entitySaleOrderProduct.setProductId(dtoProduct.getEntityProduct().getId());
                entitySaleOrderProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());   
                entitySaleOrderProduct.setDiscount(dtoProduct.getDiscount());
                entitySaleOrderProducts.add(entitySaleOrderProduct);

                EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                entityShowRoomStock.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                entityShowRoomStock.setStockIn(0);
                entityShowRoomStock.setStockOut(dtoProduct.getQuantity());
                entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
                entityShowRoomStocks.add(entityShowRoomStock);
            }
            
        }      
        //checking whether stock is available or not
        Stock stock = new Stock();
        List<DTOProduct> stockProducts = stock.getCurrentStockByProductIds(productIds);
        for(int productCounter = 0; productCounter < stockProducts.size(); productCounter++)
        {
            DTOProduct stockProduct = stockProducts.get(productCounter);
            if(productIdQuantityMap.containsKey(stockProduct.getEntityProduct().getId()) && productIdQuantityMap.get(stockProduct.getEntityProduct().getId()) > stockProduct.getQuantity())
            {
                //insufficient stock for the product
                responseDTOSaleOrder.setSuccess(false);
                responseDTOSaleOrder.setMessage("Insufficient stock for the product " + stockProduct.getEntityProduct().getName() + ". Available stock quantity is " + stockProduct.getQuantity() );
                return responseDTOSaleOrder;
            }
        }
        
        //setting user profile info into sale order info
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        EntityUser entityUser = entityManagerUser.getUserByUserId(dtoSaleOrder.getEntitySaleOrder().getCustomerUserId());
        if(entityUser != null)
        {
            if(!StringUtils.isNullOrEmpty(entityUser.getUserName()))
            {
                dtoSaleOrder.getEntitySaleOrder().setCustomerName(entityUser.getUserName());
            }
            dtoSaleOrder.getEntitySaleOrder().setEmail(entityUser.getEmail());
            dtoSaleOrder.getEntitySaleOrder().setCell(entityUser.getCell());
        }
            
        
        EntitySaleOrder entitySaleOrder = entityManagerSaleOrder.createSaleOrder(dtoSaleOrder.getEntitySaleOrder(), entitySaleOrderProducts, entityShowRoomStocks);
        responseDTOSaleOrder.setEntitySaleOrder(entitySaleOrder);
        if(responseDTOSaleOrder.getEntitySaleOrder() != null && responseDTOSaleOrder.getEntitySaleOrder().getId() > 0)
        {
            responseDTOSaleOrder.setSuccess(true);
            responseDTOSaleOrder.setMessage("Sale Order is added successfully.");
        }
        else
        {
            responseDTOSaleOrder = new DTOSaleOrder();
            responseDTOSaleOrder.setSuccess(false);
            responseDTOSaleOrder.setMessage("Unable to add Sale Order. Please try again later..");
        }
        return responseDTOSaleOrder;
    }
    
    @ClientRequest(action = ACTION.UPDATE_SALE_ORDER_INFO)
    public ClientResponse updateSaleOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOSaleOrder dtoSaleOrder = gson.fromJson(packet.getPacketBody(), DTOSaleOrder.class);     
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
        
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        EntitySaleOrder resultEntitySaleOrder = entityManagerSaleOrder.getSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        if(resultEntitySaleOrder != null && resultEntitySaleOrder.getId() != dtoSaleOrder.getEntitySaleOrder().getId())
        {
            response.setSuccess(false);
            response.setMessage("Order No already exists or invalid.");
            return response;
        }
        
        if(dtoSaleOrder.getEntitySaleOrder().getId() > 0)
        {
            List<Integer> productIds = new ArrayList<>();
            HashMap<Integer, Double> productIdQuantityMap = new HashMap<>();
            
            List<DTOProduct> products = dtoSaleOrder.getProducts();
            int totalProducts = products.size();
            List<EntitySaleOrderProduct> entitySaleOrderProducts = new ArrayList<>();
            List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
            for(int counter = 0; counter < totalProducts; counter++)
            {
                DTOProduct dtoProduct = products.get(counter);
                
                if(dtoProduct != null && dtoProduct.getEntityProduct() != null && dtoProduct.getEntityProduct().getId() > 0)
                {
                    productIds.add(dtoProduct.getEntityProduct().getId());
                    if(!productIdQuantityMap.containsKey(dtoProduct.getEntityProduct().getId()))
                    {
                        productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), dtoProduct.getQuantity());
                    }
                    
                    EntitySaleOrderProduct entitySaleOrderProduct = new EntitySaleOrderProduct();
                    entitySaleOrderProduct.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                    entitySaleOrderProduct.setProductId(dtoProduct.getEntityProduct().getId());
                    entitySaleOrderProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());   
                    entitySaleOrderProduct.setDiscount(dtoProduct.getDiscount());
                    entitySaleOrderProducts.add(entitySaleOrderProduct);

                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                    entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                    entityShowRoomStock.setStockIn(0);
                    entityShowRoomStock.setStockOut(dtoProduct.getQuantity());
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
                    entityShowRoomStocks.add(entityShowRoomStock);
                }
            }
            
            //checking whether stock is available or not
            Stock stock = new Stock();
            List<DTOProduct> stockProducts = stock.getCurrentStockByProductIds(productIds);
            for(int productCounter = 0; productCounter < stockProducts.size(); productCounter++)
            {
                DTOProduct stockProduct = stockProducts.get(productCounter);
                
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                EntityShowRoomStock showRoomStockProduct = entityManagerShowRoomStock.getShowRoomProductBySaleOrderNoAndTransactionCategoryId(stockProduct.getEntityProduct().getId(), dtoSaleOrder.getEntitySaleOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
                if(productIdQuantityMap.containsKey(stockProduct.getEntityProduct().getId()) && productIdQuantityMap.get(stockProduct.getEntityProduct().getId()) > (stockProduct.getQuantity() + showRoomStockProduct.getStockOut()) )
                {
                    //insufficient stock for the product
                    response.setSuccess(false);
                    response.setMessage("Insufficient stock for the product " + stockProduct.getEntityProduct().getName() + ". Available stock quantity is " + (stockProduct.getQuantity()+showRoomStockProduct.getStockOut()) );
                    return response;
                }
            }
            //setting user profile info into sale order info
            EntityManagerUser entityManagerUser = new EntityManagerUser();
            EntityUser entityUser = entityManagerUser.getUserByUserId(dtoSaleOrder.getEntitySaleOrder().getCustomerUserId());
            if(entityUser != null)
            {
                if(!StringUtils.isNullOrEmpty(entityUser.getUserName()))
                {
                    dtoSaleOrder.getEntitySaleOrder().setCustomerName(entityUser.getUserName());
                }
                dtoSaleOrder.getEntitySaleOrder().setEmail(entityUser.getEmail());
                dtoSaleOrder.getEntitySaleOrder().setCell(entityUser.getCell());
            }
            
            if(entityManagerSaleOrder.updateSaleOrder(dtoSaleOrder.getEntitySaleOrder(), entitySaleOrderProducts, entityShowRoomStocks))
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
    
    @ClientRequest(action = ACTION.FETCH_SALE_ORDER_INFO)
    public ClientResponse getSaleOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSaleOrder dtoSaleOrder = gson.fromJson(packet.getPacketBody(), DTOSaleOrder.class); 
        
        if(dtoSaleOrder == null || dtoSaleOrder.getEntitySaleOrder() == null || StringUtils.isNullOrEmpty(dtoSaleOrder.getEntitySaleOrder().getOrderNo()) )
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid sale order.");
            return response;
        }
        
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        EntitySaleOrder entitySaleOrder = entityManagerSaleOrder.getSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        if(entitySaleOrder != null)
        {
            dtoSaleOrder.setEntitySaleOrder(entitySaleOrder);
            EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct();
            List<EntitySaleOrderProduct> entitySaleOrderProducts = entityManagerSaleOrderProduct.getSaleOrderProductsByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            if(entitySaleOrderProducts != null && !entitySaleOrderProducts.isEmpty())
            {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
                for(int counter = 0; counter < entitySaleOrderProducts.size(); counter++)
                {
                    EntitySaleOrderProduct entitySaleOrderProduct = entitySaleOrderProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductBySaleOrderNoAndTransactionCategoryId(entitySaleOrderProduct.getProductId(), dtoSaleOrder.getEntitySaleOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
                    EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());

                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setQuantity(stockProduct.getStockOut());
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.getEntityProduct().setUnitPrice(entitySaleOrderProduct.getUnitPrice());
                    dtoProduct.setDiscount(entitySaleOrderProduct.getDiscount());
                    dtoSaleOrder.getProducts().add(dtoProduct);
                }
            }
            dtoSaleOrder.setSuccess(true);
            return dtoSaleOrder;
        }
        else
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid sale order..");
            return response;
        }
    }
    
    @ClientRequest(action = ACTION.FETCH_SALE_ORDERS)
    public ClientResponse getSaleOrders(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSaleOrder dtoSaleOrder = gson.fromJson(packet.getPacketBody(), DTOSaleOrder.class);   
        if(dtoSaleOrder == null)
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid request to get sale orders.");
            return response;
        }
        
        List<DTOSaleOrder> saleOrders = new ArrayList<>();
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        List<EntitySaleOrder> entitySaleOrders =  entityManagerSaleOrder.getSaleOrders(dtoSaleOrder.getOffset(), dtoSaleOrder.getLimit());
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
        ListSaleOrder listSaleOrder = new ListSaleOrder();
        listSaleOrder.setTotalSaleOrders(entityManagerSaleOrder.getTotalSaleOrders());
        listSaleOrder.setSaleOrders(saleOrders);
        listSaleOrder.setSuccess(true);
        return listSaleOrder;
    }
    
    @ClientRequest(action = ACTION.FETCH_SALE_ORDERS_BY_ORDER_NO)
    public ClientResponse getSaleOrdersByOrderNo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSaleOrder dtoSaleOrder = gson.fromJson(packet.getPacketBody(), DTOSaleOrder.class);   
        if(dtoSaleOrder == null)
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid request to get sale orders.");
            return response;
        }
        
        List<DTOSaleOrder> saleOrders = new ArrayList<>();
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        List<EntitySaleOrder> entitySaleOrders =  entityManagerSaleOrder.searchSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo(), dtoSaleOrder.getOffset(), dtoSaleOrder.getLimit());
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
        ListSaleOrder listSaleOrder = new ListSaleOrder();
        listSaleOrder.setTotalSaleOrders(entityManagerSaleOrder.searchTotalSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo()));
        listSaleOrder.setSaleOrders(saleOrders);
        listSaleOrder.setSuccess(true);
        return listSaleOrder;
    }
    
    @ClientRequest(action = ACTION.FETCH_SALE_ORDERS_BY_CELL)
    public ClientResponse getSaleOrdersByCell(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSaleOrder dtoSaleOrder = gson.fromJson(packet.getPacketBody(), DTOSaleOrder.class);   
        if(dtoSaleOrder == null)
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid request to get sale orders.");
            return response;
        }
        
        List<DTOSaleOrder> saleOrders = new ArrayList<>();
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        List<EntitySaleOrder> entitySaleOrders =  entityManagerSaleOrder.searchSaleOrderByCell(dtoSaleOrder.getEntitySaleOrder().getCell(), dtoSaleOrder.getOffset(), dtoSaleOrder.getLimit());
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
        ListSaleOrder listSaleOrder = new ListSaleOrder();
        listSaleOrder.setTotalSaleOrders(entityManagerSaleOrder.searchTotalSaleOrderByCell(dtoSaleOrder.getEntitySaleOrder().getCell()));
        listSaleOrder.setSaleOrders(saleOrders);
        listSaleOrder.setSuccess(true);
        return listSaleOrder;
    }
}
