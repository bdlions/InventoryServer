package org.bdlions.inventory.request.handler;

import com.bdlions.dto.response.ClientListResponse;
import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bdlions.inventory.dto.DTOAdjustStockOrder;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.entity.EntityAdjustStockOrder;
import org.bdlions.inventory.entity.EntityAdjustStockOrderProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerAdjustStockOrder;
import org.bdlions.inventory.entity.manager.EntityManagerAdjustStockOrderProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrder;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.manager.Stock;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.util.Constants;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author nazmul hasan
 */
public class AdjustStockHandler {
    private final Logger logger = LoggerFactory.getLogger(AdjustStockHandler.class);
    private final ISessionManager sessionManager;

    public AdjustStockHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.ADD_ADJUST_STOCK_ORDER_INFO)
    public ClientResponse addAdjustStockOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        DTOAdjustStockOrder responseDTOAdjustStockOrder = new DTOAdjustStockOrder();
        Gson gson = new Gson();
        DTOAdjustStockOrder dtoAdjustStockOrder = gson.fromJson(packet.getPacketBody(), DTOAdjustStockOrder.class);     
        if(dtoAdjustStockOrder == null || dtoAdjustStockOrder.getEntityAdjustStockOrder() == null)
        {
            responseDTOAdjustStockOrder.setSuccess(false);
            responseDTOAdjustStockOrder.setMessage("Invalid Adjust Stock Order Info. Please try again later.");
            return responseDTOAdjustStockOrder;
        }
        else if(dtoAdjustStockOrder.getProducts() == null || dtoAdjustStockOrder.getProducts().isEmpty())
        {
            responseDTOAdjustStockOrder.setSuccess(false);
            responseDTOAdjustStockOrder.setMessage("Please select product to adjust stock.");
            return responseDTOAdjustStockOrder;
        }
        EntityManagerAdjustStockOrder entityManagerAdjustStockOrder = new EntityManagerAdjustStockOrder(packet.getPacketHeader().getAppId());
        int autoOrderNo = 1;
        EntityAdjustStockOrder tempEntityAdjustStockOrder = entityManagerAdjustStockOrder.getLastAdjustStockOrder();
        if(tempEntityAdjustStockOrder != null)
        {
            autoOrderNo = tempEntityAdjustStockOrder.getNextOrderNo();
            if(autoOrderNo < 2)
            {
                autoOrderNo = 1;
            }
        }        
        if(!StringUtils.isNullOrEmpty(dtoAdjustStockOrder.getEntityAdjustStockOrder().getOrderNo()))
        {
            //check whether order no exists or not
            EntityAdjustStockOrder resultEntityAdjustStockOrder = entityManagerAdjustStockOrder.getAdjustStockOrderByOrderNo(dtoAdjustStockOrder.getEntityAdjustStockOrder().getOrderNo());
            if(resultEntityAdjustStockOrder != null)
            {
                resultEntityAdjustStockOrder.setSuccess(false);
                resultEntityAdjustStockOrder.setMessage("Order No already exists or invalid.");
                return resultEntityAdjustStockOrder;
            }
            if(dtoAdjustStockOrder.getEntityAdjustStockOrder().getOrderNo().equals(StringUtils.generateAdjustStockOrderNo(autoOrderNo)))
            {
                autoOrderNo = autoOrderNo + 1;
            }
        }
        else
        {            
            String orderNo = StringUtils.generateAdjustStockOrderNo(autoOrderNo);
            //check whether this order number already exists or not
            //this checking is required bacause someone may update order no manually which will match auto generated order no
            int maxCounter = 50;
            int counter = 0;
            boolean isValid = false;
            while(++counter <= maxCounter)
            {
                EntityAdjustStockOrder resultEntityAdjustStockOrder = entityManagerAdjustStockOrder.getAdjustStockOrderByOrderNo(orderNo);
                if(resultEntityAdjustStockOrder != null)
                {
                    autoOrderNo = autoOrderNo + 1;
                    orderNo = StringUtils.generateAdjustStockOrderNo(autoOrderNo);
                }
                else
                {
                    isValid = true;
                }
            }
            if(!isValid)
            {
                responseDTOAdjustStockOrder.setSuccess(false);
                responseDTOAdjustStockOrder.setMessage("Unable to generate order no. Please contact with system admin.");
                return responseDTOAdjustStockOrder;
            }
            dtoAdjustStockOrder.getEntityAdjustStockOrder().setOrderNo(orderNo); 
            autoOrderNo = autoOrderNo + 1;
        }
        dtoAdjustStockOrder.getEntityAdjustStockOrder().setNextOrderNo(autoOrderNo);       
        
        List<Integer> productIds = new ArrayList<>();
        HashMap<Integer, Double> productIdQuantityMap = new HashMap<>();
        List<EntityAdjustStockOrderProduct> entityAdjustStockOrderProducts = new ArrayList<>();
        List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
        
        List<DTOProduct> products = dtoAdjustStockOrder.getProducts();
        if(products != null && !products.isEmpty())
        {
            int totalProducts = products.size();        
            for(int counter = 0; counter < totalProducts; counter++)
            {
                DTOProduct dtoProduct = products.get(counter);
                if(dtoProduct != null && dtoProduct.getEntityProduct() != null && dtoProduct.getEntityProduct().getId() > 0)
                {
                    productIds.add(dtoProduct.getEntityProduct().getId());
                    if(!productIdQuantityMap.containsKey(dtoProduct.getEntityProduct().getId()))
                    {
                        productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), dtoProduct.getNewQuantity());
                    }
                    double difference = dtoProduct.getNewQuantity() - dtoProduct.getOldQuantity();
                    EntityAdjustStockOrderProduct entityAdjustStockOrderProduct = new EntityAdjustStockOrderProduct();
                    entityAdjustStockOrderProduct.setOrderNo(dtoAdjustStockOrder.getEntityAdjustStockOrder().getOrderNo());
                    entityAdjustStockOrderProduct.setProductId(dtoProduct.getEntityProduct().getId());
                    entityAdjustStockOrderProduct.setDescription(dtoProduct.getDescription());
                    entityAdjustStockOrderProduct.setNewQuantity(dtoProduct.getNewQuantity());
                    entityAdjustStockOrderProduct.setOldQuantity(dtoProduct.getOldQuantity());
                    entityAdjustStockOrderProduct.setDifference(difference);
                    entityAdjustStockOrderProducts.add(entityAdjustStockOrderProduct);

                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                    entityShowRoomStock.setProductName(dtoProduct.getEntityProduct().getName());
                    if(difference > 0)
                    {
                        entityShowRoomStock.setStockIn(difference);
                        entityShowRoomStock.setStockOut(0);
                    }
                    else
                    {
                        entityShowRoomStock.setStockIn(0);
                        entityShowRoomStock.setStockOut(-difference);
                    }                    
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_STOCK_ADJUSTMENT);
                    entityShowRoomStock.setTransactionCategoryTitle(Constants.SS_TRANSACTION_CATEGORY_TITLE_STOCK_ADJUSTMENT);
                    entityShowRoomStocks.add(entityShowRoomStock);
                }

            }
        }         
        
        //checking whether stock is available or not before reducing
        Stock stock = new Stock(packet.getPacketHeader().getAppId());
        List<DTOProduct> stockProducts = stock.getCurrentStockByProductIds(productIds);
        List<Integer> excludedProductIds = new ArrayList<>();
        List<Integer> tempProductIds = new ArrayList<>();
        if(stockProducts != null && !stockProducts.isEmpty())
        {
            for(int counter = 0; counter < stockProducts.size(); counter++)
            {
                if(!tempProductIds.contains(stockProducts.get(counter).getEntityProduct().getId()))
                {
                    tempProductIds.add(stockProducts.get(counter).getEntityProduct().getId());
                }
            }
        }
        for(int productId: productIds)
        {
            if(!tempProductIds.contains(productId))
            {
                excludedProductIds.add(productId);
            }
        }
        if(stockProducts == null)
        {
            stockProducts = new ArrayList<>();
        }
        for(DTOProduct tempDTOProduct: products)
        {
            if(excludedProductIds.contains(tempDTOProduct.getEntityProduct().getId()))
            {
                EntityProduct tempEntityProduct = tempDTOProduct.getEntityProduct();
                DTOProduct excludedDTOProduct = new DTOProduct();
                excludedDTOProduct.setQuantity(0);
                excludedDTOProduct.setEntityProduct(tempEntityProduct);                
                stockProducts.add(excludedDTOProduct);
            }
        }
        for(int productCounter = 0; productCounter < stockProducts.size(); productCounter++)
        {
            DTOProduct stockProduct = stockProducts.get(productCounter);
            if(productIdQuantityMap.containsKey(stockProduct.getEntityProduct().getId()) && productIdQuantityMap.get(stockProduct.getEntityProduct().getId()) < 0)
            {
                //insufficient stock for the product if reduced vai stock adjust
                responseDTOAdjustStockOrder.setSuccess(false);
                responseDTOAdjustStockOrder.setMessage("Right now you are not allowed to set negative stock during stock adjustment for the product " + stockProduct.getEntityProduct().getName() + ". Available stock quantity is " + stockProduct.getQuantity() );
                return responseDTOAdjustStockOrder;
            }
        }
        
        //setting created by and modified by user
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
        int userId = (int)session.getUserId();
        dtoAdjustStockOrder.getEntityAdjustStockOrder().setCreatedByUserId(userId);
        dtoAdjustStockOrder.getEntityAdjustStockOrder().setModifiedByUserId(userId);
        EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
        if(tempEntityUser != null && tempEntityUser.getId() > 0)
        {
            dtoAdjustStockOrder.getEntityAdjustStockOrder().setCreatedByUserName(tempEntityUser.getUserName());
            dtoAdjustStockOrder.getEntityAdjustStockOrder().setModifiedByUserName(tempEntityUser.getUserName());
        }  
        
        if(!StringUtils.isNullOrEmpty(dtoAdjustStockOrder.getAdjustDate()))
        {
            dtoAdjustStockOrder.getEntityAdjustStockOrder().setAdjustOn(TimeUtils.convertHumanToUnix(dtoAdjustStockOrder.getAdjustDate(), "", ""));
        }
        else
        {
            dtoAdjustStockOrder.getEntityAdjustStockOrder().setAdjustOn(TimeUtils.getCurrentTime());
        }
        
        EntityAdjustStockOrder entityAdjustStockOrder = entityManagerAdjustStockOrder.createAdjustStockOrder(dtoAdjustStockOrder.getEntityAdjustStockOrder(), entityAdjustStockOrderProducts, entityShowRoomStocks);
        responseDTOAdjustStockOrder.setEntityAdjustStockOrder(entityAdjustStockOrder);
        if(responseDTOAdjustStockOrder.getEntityAdjustStockOrder() != null && responseDTOAdjustStockOrder.getEntityAdjustStockOrder().getId() > 0)
        {
            responseDTOAdjustStockOrder.setSuccess(true);
            responseDTOAdjustStockOrder.setMessage("Adjust Stock Order is added successfully.");
        }
        else
        {
            responseDTOAdjustStockOrder = new DTOAdjustStockOrder();
            responseDTOAdjustStockOrder.setSuccess(false);
            responseDTOAdjustStockOrder.setMessage("Unable to add Adjust Stock Order. Please try again later..");
        }
        return responseDTOAdjustStockOrder;
    }
    
    @ClientRequest(action = ACTION.UPDATE_ADJUST_STOCK_ORDER_INFO)
    public ClientResponse updateAdjustStockOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOAdjustStockOrder dtoAdjustStockOrder = gson.fromJson(packet.getPacketBody(), DTOAdjustStockOrder.class);     
        if(dtoAdjustStockOrder == null || dtoAdjustStockOrder.getEntityAdjustStockOrder() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Adjust Stock Order Info. Please try again later.");
            return response;
        }
        else if(dtoAdjustStockOrder.getEntityAdjustStockOrder().getId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Adjust Stock Order Info. Please try again later..");
            return response;
        }
        else if(dtoAdjustStockOrder.getEntityAdjustStockOrder().getOrderNo() == null || dtoAdjustStockOrder.getEntityAdjustStockOrder().getOrderNo().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Order no is required.");
            return response;
        }
        else if(dtoAdjustStockOrder.getProducts() == null || dtoAdjustStockOrder.getProducts().isEmpty())
        {
            response.setSuccess(false);
            response.setMessage("Please select product for the adjust stock.");
            return response;
        }
        
        EntityManagerAdjustStockOrder entityManagerAdjustStockOrder = new EntityManagerAdjustStockOrder(packet.getPacketHeader().getAppId());
        EntityAdjustStockOrder resultEntityAdjustStockOrder = entityManagerAdjustStockOrder.getAdjustStockOrderByOrderNo(dtoAdjustStockOrder.getEntityAdjustStockOrder().getOrderNo());
        if(resultEntityAdjustStockOrder != null && resultEntityAdjustStockOrder.getId() != dtoAdjustStockOrder.getEntityAdjustStockOrder().getId())
        {
            response.setSuccess(false);
            response.setMessage("Order No already exists or invalid.");
            return response;
        }
        EntityAdjustStockOrder currentEntityAdjustStockOrder = entityManagerAdjustStockOrder.getAdjustStockOrderById(dtoAdjustStockOrder.getEntityAdjustStockOrder().getId());
        if(currentEntityAdjustStockOrder != null && dtoAdjustStockOrder.getEntityAdjustStockOrder().getId() > 0)
        {
            List<Integer> productIds = new ArrayList<>();
            HashMap<Integer, Double> productIdQuantityMap = new HashMap<>();
            List<EntityAdjustStockOrderProduct> entityAdjustStockOrderProducts = new ArrayList<>();
            List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
            
            List<DTOProduct> products = dtoAdjustStockOrder.getProducts();
            if(products != null && !products.isEmpty())
            {
                int totalProducts = products.size();            
                for(int counter = 0; counter < totalProducts; counter++)
                {
                    DTOProduct dtoProduct = products.get(counter);

                    if(dtoProduct != null && dtoProduct.getEntityProduct() != null && dtoProduct.getEntityProduct().getId() > 0)
                    {
                        productIds.add(dtoProduct.getEntityProduct().getId());
                        if(!productIdQuantityMap.containsKey(dtoProduct.getEntityProduct().getId()))
                        {
                            productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), dtoProduct.getNewQuantity());
                        }

                        double difference = dtoProduct.getNewQuantity() - dtoProduct.getOldQuantity();
                        EntityAdjustStockOrderProduct entityAdjustStockOrderProduct = new EntityAdjustStockOrderProduct();
                        entityAdjustStockOrderProduct.setOrderNo(dtoAdjustStockOrder.getEntityAdjustStockOrder().getOrderNo());
                        entityAdjustStockOrderProduct.setProductId(dtoProduct.getEntityProduct().getId());
                        entityAdjustStockOrderProduct.setDescription(dtoProduct.getDescription());
                        entityAdjustStockOrderProduct.setNewQuantity(dtoProduct.getNewQuantity());
                        entityAdjustStockOrderProduct.setOldQuantity(dtoProduct.getOldQuantity());
                        entityAdjustStockOrderProduct.setDifference(difference);
                        entityAdjustStockOrderProducts.add(entityAdjustStockOrderProduct);

                        EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                        entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                        entityShowRoomStock.setProductName(dtoProduct.getEntityProduct().getName());
                        if(difference > 0)
                        {
                            entityShowRoomStock.setStockIn(difference);
                            entityShowRoomStock.setStockOut(0);
                        }
                        else
                        {
                            entityShowRoomStock.setStockIn(0);
                            entityShowRoomStock.setStockOut(-difference);
                        }                    
                        entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_STOCK_ADJUSTMENT);
                        entityShowRoomStock.setTransactionCategoryTitle(Constants.SS_TRANSACTION_CATEGORY_TITLE_STOCK_ADJUSTMENT);
                        entityShowRoomStocks.add(entityShowRoomStock);
                    }
                }
            }
            
            //checking whether stock is available or not before reducing
            Stock stock = new Stock(packet.getPacketHeader().getAppId());
            List<DTOProduct> stockProducts = stock.getCurrentStockByProductIds(productIds);
            List<Integer> excludedProductIds = new ArrayList<>();
            List<Integer> tempProductIds = new ArrayList<>();
            if(stockProducts != null && !stockProducts.isEmpty())
            {
                for(int counter = 0; counter < stockProducts.size(); counter++)
                {
                    if(!tempProductIds.contains(stockProducts.get(counter).getEntityProduct().getId()))
                    {
                        tempProductIds.add(stockProducts.get(counter).getEntityProduct().getId());
                    }
                }
            }
            for(int productId: productIds)
            {
                if(!tempProductIds.contains(productId))
                {
                    excludedProductIds.add(productId);
                }
            }
            if(stockProducts == null)
            {
                stockProducts = new ArrayList<>();
            }
            for(DTOProduct tempDTOProduct: products)
            {
                if(excludedProductIds.contains(tempDTOProduct.getEntityProduct().getId()))
                {
                    EntityProduct tempEntityProduct = tempDTOProduct.getEntityProduct();
                    DTOProduct excludedDTOProduct = new DTOProduct();
                    excludedDTOProduct.setQuantity(0);
                    excludedDTOProduct.setEntityProduct(tempEntityProduct);                
                    stockProducts.add(excludedDTOProduct);
                }
            }
            for(int productCounter = 0; productCounter < stockProducts.size(); productCounter++)
            {
                DTOProduct stockProduct = stockProducts.get(productCounter);
                if(productIdQuantityMap.containsKey(stockProduct.getEntityProduct().getId()) && productIdQuantityMap.get(stockProduct.getEntityProduct().getId()) < 0)
                {
                    //insufficient stock for the product if reduced vai stock adjust
                    response.setSuccess(false);
                    response.setMessage("Right now you are not allowed to set negative stock during stock adjustment for the product " + stockProduct.getEntityProduct().getName() + ". Available stock quantity is " + stockProduct.getQuantity() );
                    return response;
                }
            }
            //setting created by and modified by user
            EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
            int userId = (int)session.getUserId();
            dtoAdjustStockOrder.getEntityAdjustStockOrder().setCreatedByUserId(userId);
            dtoAdjustStockOrder.getEntityAdjustStockOrder().setModifiedByUserId(userId);
            EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
            if(tempEntityUser != null && tempEntityUser.getId() > 0)
            {
                dtoAdjustStockOrder.getEntityAdjustStockOrder().setCreatedByUserName(tempEntityUser.getUserName());
                dtoAdjustStockOrder.getEntityAdjustStockOrder().setModifiedByUserName(tempEntityUser.getUserName());
            }  

            if(!StringUtils.isNullOrEmpty(dtoAdjustStockOrder.getAdjustDate()))
            {
                dtoAdjustStockOrder.getEntityAdjustStockOrder().setAdjustOn(TimeUtils.convertHumanToUnix(dtoAdjustStockOrder.getAdjustDate(), "", ""));
            }
            else
            {
                dtoAdjustStockOrder.getEntityAdjustStockOrder().setAdjustOn(TimeUtils.getCurrentTime());
            }
            
            if(entityManagerAdjustStockOrder.updateAdjustStockOrder(currentEntityAdjustStockOrder, dtoAdjustStockOrder.getEntityAdjustStockOrder(), entityAdjustStockOrderProducts, entityShowRoomStocks))
            {
                response.setSuccess(true);
                response.setMessage("Adjust Stock order is updated successfully.");
            }
            else 
            {
                response.setSuccess(false);
                response.setMessage("Invalid Adjust Stock Order Info. Please try again later...");
            }
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Invalid Adjust Stock Order Info. Please try again later....");
        }
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_ADJUST_STOCK_ORDER_INFO)
    public ClientResponse getAdjustStockOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOAdjustStockOrder dtoAdjustStockOrder = gson.fromJson(packet.getPacketBody(), DTOAdjustStockOrder.class); 
        
        if(dtoAdjustStockOrder == null || dtoAdjustStockOrder.getEntityAdjustStockOrder() == null || StringUtils.isNullOrEmpty(dtoAdjustStockOrder.getEntityAdjustStockOrder().getOrderNo()) )
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid adjust stock order.");
            return response;
        }
        
        EntityManagerAdjustStockOrder entityManagerAdjustStockOrder = new EntityManagerAdjustStockOrder(packet.getPacketHeader().getAppId());
        EntityAdjustStockOrder entityAdjustStockOrder = entityManagerAdjustStockOrder.getAdjustStockOrderByOrderNo(dtoAdjustStockOrder.getEntityAdjustStockOrder().getOrderNo());
        if(entityAdjustStockOrder != null)
        {
            dtoAdjustStockOrder.setEntityAdjustStockOrder(entityAdjustStockOrder);
            dtoAdjustStockOrder.setAdjustDate(TimeUtils.convertUnixToHuman(entityAdjustStockOrder.getAdjustOn(), "yyyy-MM-dd", ""));
            
            //getting products for the adjust stock order
            EntityManagerAdjustStockOrderProduct entityManagerAdjustStockOrderProduct = new EntityManagerAdjustStockOrderProduct(packet.getPacketHeader().getAppId());
            List<EntityAdjustStockOrderProduct> entityAdjustStockOrderProducts = entityManagerAdjustStockOrderProduct.getAdjustStockOrderProductsByOrderNo(dtoAdjustStockOrder.getEntityAdjustStockOrder().getOrderNo());
            if(entityAdjustStockOrderProducts != null && !entityAdjustStockOrderProducts.isEmpty())
            {
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
                for(int counter = 0; counter < entityAdjustStockOrderProducts.size(); counter++)
                {
                    EntityAdjustStockOrderProduct entityAdjustStockOrderProduct = entityAdjustStockOrderProducts.get(counter);
                    EntityProduct entityProduct = entityManagerProduct.getProductByProductId(entityAdjustStockOrderProduct.getProductId());

                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.setNewQuantity(entityAdjustStockOrderProduct.getNewQuantity());
                    dtoProduct.setOldQuantity(entityAdjustStockOrderProduct.getOldQuantity());
                    dtoProduct.setDescription(entityAdjustStockOrderProduct.getDescription());
                    dtoAdjustStockOrder.getProducts().add(dtoProduct);
                }
            }            
            dtoAdjustStockOrder.setSuccess(true);
            return dtoAdjustStockOrder;
        }
        else
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid adjust stock order..");
            return response;
        }
    }
    
    @ClientRequest(action = ACTION.FETCH_ADJUST_STOCK_ORDERS)
    public ClientResponse getAdjustStockOrders(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOAdjustStockOrder dtoAdjustStockOrder = gson.fromJson(packet.getPacketBody(), DTOAdjustStockOrder.class);   
        if(dtoAdjustStockOrder == null)
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid request to get adjust stock orders.");
            return response;
        }
        
        List<DTOAdjustStockOrder> adjustStockOrders = new ArrayList<>();
        EntityManagerAdjustStockOrder entityManagerAdjustStockOrder = new EntityManagerAdjustStockOrder(packet.getPacketHeader().getAppId());
        List<EntityAdjustStockOrder> entityAdjustStockOrders =  entityManagerAdjustStockOrder.getAdjustStockOrders(dtoAdjustStockOrder.getOffset(), dtoAdjustStockOrder.getLimit());
        if(entityAdjustStockOrders != null)
        {            
            for (int counter = 0; counter < entityAdjustStockOrders.size(); counter++) 
            {
                EntityAdjustStockOrder entityAdjustStockOrder = entityAdjustStockOrders.get(counter);
                DTOAdjustStockOrder dtoASO = new DTOAdjustStockOrder();
                dtoASO.setEntityAdjustStockOrder(entityAdjustStockOrder);
                adjustStockOrders.add(dtoASO);
            }
        }
        ClientListResponse response = new ClientListResponse();
        response.setList(adjustStockOrders);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.SEARCH_ADJUST_STOCK_ORDERS)
    public ClientResponse searchAdjustStockOrders(ISession session, IPacket packet) throws Exception 
    {
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);    
        String offsetString = jsonObject.get("offset").getAsString();
        String limitString = jsonObject.get("limit").getAsString();
        String orderNo = jsonObject.get("orderNo").getAsString();
        int offset = 0;
        int limit = 0;
        try
        {
            offset = Integer.parseInt(offsetString);
            limit = Integer.parseInt(limitString);
        }
        catch(Exception ex)
        {
            offset = 0;
            limit = 0;
            logger.debug(ex.toString());
        }
        List<DTOAdjustStockOrder> adjustStockOrders = new ArrayList<>();
        EntityManagerAdjustStockOrder entityManagerAdjustStockOrder = new EntityManagerAdjustStockOrder(packet.getPacketHeader().getAppId());
        List<EntityAdjustStockOrder> entityAdjustStockOrders = entityManagerAdjustStockOrder.searchAdjustStockOrdersDQ(orderNo, 0, 0, offset, limit);
        if(entityAdjustStockOrders != null)
        {            
            for (int counter = 0; counter < entityAdjustStockOrders.size(); counter++) 
            {
                EntityAdjustStockOrder entityAdjustStockOrder = entityAdjustStockOrders.get(counter);
                DTOAdjustStockOrder dtoASO = new DTOAdjustStockOrder();
                dtoASO.setEntityAdjustStockOrder(entityAdjustStockOrder);
                adjustStockOrders.add(dtoASO);
            }
        }
        ClientListResponse response = new ClientListResponse();
        response.setList(adjustStockOrders);
        response.setSuccess(true);
        return response;        
    } 
}
