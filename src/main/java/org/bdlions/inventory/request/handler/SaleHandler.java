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
import org.bdlions.inventory.entity.EntitySaleOrderReturnProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrder;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderProduct;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderReturnProduct;
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
        else if(dtoSaleOrder.getEntitySaleOrder().getCustomerUserId() == 0 && dtoSaleOrder.getEntitySaleOrder().getPaid() != dtoSaleOrder.getEntitySaleOrder().getTotal())
        {
            responseDTOSaleOrder.setSuccess(false);
            responseDTOSaleOrder.setMessage("Paid amount should be equal to total amount.");
            return responseDTOSaleOrder;
        }
        else if(dtoSaleOrder.getProducts() == null || dtoSaleOrder.getProducts().isEmpty())
        {
            responseDTOSaleOrder.setSuccess(false);
            responseDTOSaleOrder.setMessage("Please select product for the sale.");
            return responseDTOSaleOrder;
        }        
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder(packet.getPacketHeader().getAppId());
        int autoOrderNo = 1;
        EntitySaleOrder tempEntitySaleOrder = entityManagerSaleOrder.getLastSaleOrder();        
        if(tempEntitySaleOrder != null)
        {
            autoOrderNo = tempEntitySaleOrder.getNextOrderNo();
            if(autoOrderNo < 2)
            {
                autoOrderNo = 1;
            }
        }        
        if(!StringUtils.isNullOrEmpty(dtoSaleOrder.getEntitySaleOrder().getOrderNo()))
        {
            EntitySaleOrder resultEntitySaleOrder = entityManagerSaleOrder.getSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            if(resultEntitySaleOrder != null)
            {
                responseDTOSaleOrder.setSuccess(false);
                responseDTOSaleOrder.setMessage("Order No already exists or invalid.");
                return responseDTOSaleOrder;
            }
            if(dtoSaleOrder.getEntitySaleOrder().getOrderNo().equals(StringUtils.generateSaleOrderNo(autoOrderNo)))
            {
                autoOrderNo = autoOrderNo + 1;
            }
        }
        else
        {            
            String orderNo = StringUtils.generateSaleOrderNo(autoOrderNo);
            //check whether this order number already exists or not
            //this checking is required bacause someone may update order no manually which will match auto generated order no
            int maxCounter = 50;
            int counter = 0;
            boolean isValid = false;
            while(++counter <= maxCounter)
            {
                EntitySaleOrder resultEntitySaleOrder = entityManagerSaleOrder.getSaleOrderByOrderNo(orderNo);
                if(resultEntitySaleOrder != null)
                {
                    autoOrderNo = autoOrderNo + 1;
                    orderNo = StringUtils.generateSaleOrderNo(autoOrderNo);
                }
                else
                {
                    isValid = true;
                }
            }
            if(!isValid)
            {
                responseDTOSaleOrder.setSuccess(false);
                responseDTOSaleOrder.setMessage("Unable to generate order no. Please contact with system admin.");
                return responseDTOSaleOrder;
            }
            dtoSaleOrder.getEntitySaleOrder().setOrderNo(orderNo); 
            autoOrderNo = autoOrderNo + 1;
        }
        dtoSaleOrder.getEntitySaleOrder().setNextOrderNo(autoOrderNo); 
        
        //selected product id list of this sale order
        List<Integer> productIds = new ArrayList<>();
        //quantity of selected products of this sale order
        HashMap<Integer, Double> productIdQuantityMap = new HashMap<>();
        List<EntitySaleOrderProduct> entitySaleOrderProducts = new ArrayList<>();
        List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProducts = new ArrayList<>();
        List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
        
        List<DTOProduct> products = dtoSaleOrder.getProducts();
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
                        productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), dtoProduct.getQuantity());
                    }

                    EntitySaleOrderProduct entitySaleOrderProduct = new EntitySaleOrderProduct();
                    entitySaleOrderProduct.setProductId(dtoProduct.getEntityProduct().getId());
                    entitySaleOrderProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());   
                    entitySaleOrderProduct.setCostPrice(dtoProduct.getEntityProduct().getCostPrice());
                    entitySaleOrderProduct.setDiscount(dtoProduct.getDiscount());
                    entitySaleOrderProduct.setVat(dtoProduct.getEntityProduct().getVat());
                    entitySaleOrderProduct.setQuantity(dtoProduct.getQuantity());
                    //double subtotal = (dtoProduct.getEntityProduct().getUnitPrice() * dtoProduct.getQuantity() - (dtoProduct.getEntityProduct().getUnitPrice() * dtoProduct.getQuantity() * dtoProduct.getDiscount() / 100));
                    entitySaleOrderProduct.setSubtotal(dtoProduct.getTotal());
                    entitySaleOrderProduct.setCreatedOn(TimeUtils.getCurrentTime());
                    entitySaleOrderProduct.setModifiedOn(TimeUtils.getCurrentTime());
                    entitySaleOrderProducts.add(entitySaleOrderProduct);

                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                    entityShowRoomStock.setProductName(dtoProduct.getEntityProduct().getName());
                    entityShowRoomStock.setStockIn(0);
                    entityShowRoomStock.setStockOut(dtoProduct.getQuantity());
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_FULFILLED);
                    entityShowRoomStock.setTransactionCategoryTitle(Constants.SS_TRANSACTION_CATEGORY_TITLE_SALE_ORDER_FULFILLED);
                    entityShowRoomStock.setCreatedOn(TimeUtils.getCurrentTime());
                    entityShowRoomStock.setModifiedOn(TimeUtils.getCurrentTime());
                    entityShowRoomStocks.add(entityShowRoomStock);
                }

            }
        }         
        
        //setting returned products during sale
        List<DTOProduct> returnProducts = dtoSaleOrder.getReturnProducts();
        if(returnProducts != null && !returnProducts.isEmpty())
        {
            int totalReturnedProducts = returnProducts.size();
            for(int counter = 0; counter < totalReturnedProducts; counter++)
            {
                DTOProduct dtoProduct = returnProducts.get(counter);
                if(dtoProduct != null && dtoProduct.getEntityProduct() != null && dtoProduct.getEntityProduct().getId() > 0)
                {
                    EntitySaleOrderReturnProduct entitySaleOrderReturnProduct = new EntitySaleOrderReturnProduct();
                    entitySaleOrderReturnProduct.setProductId(dtoProduct.getEntityProduct().getId());
                    entitySaleOrderReturnProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());   
                    entitySaleOrderReturnProduct.setDiscount(dtoProduct.getDiscount());
                    entitySaleOrderReturnProduct.setQuantity(dtoProduct.getQuantity());
                    //double subtotal = (dtoProduct.getEntityProduct().getUnitPrice() * dtoProduct.getQuantity() - (dtoProduct.getEntityProduct().getUnitPrice() * dtoProduct.getQuantity() * dtoProduct.getDiscount() / 100));
                    entitySaleOrderReturnProduct.setSubtotal(dtoProduct.getTotal());
                    entitySaleOrderReturnProduct.setCreatedOn(TimeUtils.getCurrentTime());
                    entitySaleOrderReturnProduct.setModifiedOn(TimeUtils.getCurrentTime());
                    entitySaleOrderReturnProducts.add(entitySaleOrderReturnProduct);

                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                    entityShowRoomStock.setProductName(dtoProduct.getEntityProduct().getName());
                    entityShowRoomStock.setStockIn(dtoProduct.getQuantity());
                    entityShowRoomStock.setStockOut(0);
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_RESTOCK);
                    entityShowRoomStock.setTransactionCategoryTitle(Constants.SS_TRANSACTION_CATEGORY_TITLE_SALE_ORDER_RESTOCK);
                    entityShowRoomStock.setCreatedOn(TimeUtils.getCurrentTime());
                    entityShowRoomStock.setModifiedOn(TimeUtils.getCurrentTime());
                    entityShowRoomStocks.add(entityShowRoomStock);

                    if(productIdQuantityMap.containsKey(dtoProduct.getEntityProduct().getId()))
                    {
                        if(productIdQuantityMap.get(dtoProduct.getEntityProduct().getId()) < dtoProduct.getQuantity())
                        {
                            responseDTOSaleOrder.setSuccess(false);
                            responseDTOSaleOrder.setMessage("You can't return higher quantity than sale quantity for the product " + dtoProduct.getEntityProduct().getName());
                            return responseDTOSaleOrder;
                        }
                        //reducing quantity of a product based on selected product quantity and return product quantity 
                        //which will be used while validating available stock
                        productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), productIdQuantityMap.get(dtoProduct.getEntityProduct().getId()) - dtoProduct.getQuantity());
                    }
                }                
            } 
        }
        
        //checking whether stock is available or not
        Stock stock = new Stock(packet.getPacketHeader().getAppId());
        List<DTOProduct> stockProducts = stock.getCurrentStockByProductIds(productIds);
        //this product ids has no entry in stock table
        List<Integer> excludedProductIds = new ArrayList<>();
        //this product ids contain entry in stock table
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
            if(productIdQuantityMap.containsKey(stockProduct.getEntityProduct().getId()) && productIdQuantityMap.get(stockProduct.getEntityProduct().getId()) > stockProduct.getQuantity())
            {
                //insufficient stock for the product
                responseDTOSaleOrder.setSuccess(false);
                responseDTOSaleOrder.setMessage("Insufficient stock for the product " + stockProduct.getEntityProduct().getName() + ". Available stock quantity is " + stockProduct.getQuantity() );
                return responseDTOSaleOrder;
            }
        }
        
        //setting user profile info into sale order info
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
           
        //setting created by and modified by user
        int userId = (int)session.getUserId();
        dtoSaleOrder.getEntitySaleOrder().setCreatedByUserId(userId);
        dtoSaleOrder.getEntitySaleOrder().setModifiedByUserId(userId);
        EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
        if(tempEntityUser != null && tempEntityUser.getId() > 0)
        {
            dtoSaleOrder.getEntitySaleOrder().setCreatedByUserName(tempEntityUser.getUserName());
            dtoSaleOrder.getEntitySaleOrder().setModifiedByUserName(tempEntityUser.getUserName());
        }  
        
        EntitySaleOrder entitySaleOrder = entityManagerSaleOrder.createSaleOrder(dtoSaleOrder.getEntitySaleOrder(), entitySaleOrderProducts, entitySaleOrderReturnProducts, entityShowRoomStocks);
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
        else if(dtoSaleOrder.getEntitySaleOrder().getCustomerUserId() == 0 && dtoSaleOrder.getEntitySaleOrder().getPaid() != dtoSaleOrder.getEntitySaleOrder().getTotal())
        {
            response.setSuccess(false);
            response.setMessage("Paid amount should be equal to total amount.");
            return response;
        }
        else if(dtoSaleOrder.getProducts() == null || dtoSaleOrder.getProducts().isEmpty())
        {
            response.setSuccess(false);
            response.setMessage("Please select product for the sale.");
            return response;
        }
        
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder(packet.getPacketHeader().getAppId());
        EntitySaleOrder resultEntitySaleOrder = entityManagerSaleOrder.getSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        if(resultEntitySaleOrder != null && resultEntitySaleOrder.getId() != dtoSaleOrder.getEntitySaleOrder().getId())
        {
            response.setSuccess(false);
            response.setMessage("Order No already exists or invalid.");
            return response;
        }
        EntitySaleOrder currentEntitySaleOrder = entityManagerSaleOrder.getSaleOrderById(dtoSaleOrder.getEntitySaleOrder().getId());
        if(currentEntitySaleOrder != null && dtoSaleOrder.getEntitySaleOrder().getId() > 0)
        {
            List<Integer> productIds = new ArrayList<>();
            HashMap<Integer, Double> productIdQuantityMap = new HashMap<>();
            List<EntitySaleOrderProduct> entitySaleOrderProducts = new ArrayList<>();
            List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProducts = new ArrayList<>();
            List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
            
            List<DTOProduct> products = dtoSaleOrder.getProducts();
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
                            productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), dtoProduct.getQuantity());
                        }

                        EntitySaleOrderProduct entitySaleOrderProduct = new EntitySaleOrderProduct();
                        entitySaleOrderProduct.setId(dtoProduct.getId());
                        entitySaleOrderProduct.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                        entitySaleOrderProduct.setProductId(dtoProduct.getEntityProduct().getId());
                        entitySaleOrderProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());   
                        entitySaleOrderProduct.setCostPrice(dtoProduct.getEntityProduct().getCostPrice());
                        entitySaleOrderProduct.setDiscount(dtoProduct.getDiscount());
                        entitySaleOrderProduct.setVat(dtoProduct.getEntityProduct().getVat());
                        entitySaleOrderProduct.setQuantity(dtoProduct.getQuantity());
                        //double subtotal = (dtoProduct.getEntityProduct().getUnitPrice() * dtoProduct.getQuantity() - (dtoProduct.getEntityProduct().getUnitPrice() * dtoProduct.getQuantity() * dtoProduct.getDiscount() / 100));
                        entitySaleOrderProduct.setSubtotal(dtoProduct.getTotal());
                        if(dtoProduct.getEntityProduct().getCreatedOn() == 0)
                        {                    
                            entitySaleOrderProduct.setCreatedOn(TimeUtils.getCurrentTime());                    
                        }    
                        else
                        {
                            entitySaleOrderProduct.setCreatedOn(dtoProduct.getEntityProduct().getCreatedOn());
                        }
                        entitySaleOrderProduct.setModifiedOn(TimeUtils.getCurrentTime());
                        entitySaleOrderProducts.add(entitySaleOrderProduct);

                        EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                        entityShowRoomStock.setId(dtoProduct.getStockId());
                        //entityShowRoomStock.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                        entityShowRoomStock.setOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                        entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                        entityShowRoomStock.setProductName(dtoProduct.getEntityProduct().getName());
                        entityShowRoomStock.setStockIn(0);
                        entityShowRoomStock.setStockOut(dtoProduct.getQuantity());
                        entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_FULFILLED);
                        entityShowRoomStock.setTransactionCategoryTitle(Constants.SS_TRANSACTION_CATEGORY_TITLE_SALE_ORDER_FULFILLED);
                        if(dtoProduct.getEntityProduct().getCreatedOn() == 0)
                        {                    
                            entityShowRoomStock.setCreatedOn(TimeUtils.getCurrentTime());                    
                        }    
                        else
                        {
                            entityShowRoomStock.setCreatedOn(dtoProduct.getEntityProduct().getCreatedOn());
                        }
                        entityShowRoomStock.setModifiedOn(TimeUtils.getCurrentTime());
                        entityShowRoomStocks.add(entityShowRoomStock);
                    }
                }
            }
            
            
            //setting returned products during sale
            List<DTOProduct> returnProducts = dtoSaleOrder.getReturnProducts();
            int totalReturnedProducts = 0;
            if(returnProducts != null && !returnProducts.isEmpty())
            {
                totalReturnedProducts = returnProducts.size();
                for(int counter = 0; counter < totalReturnedProducts; counter++)
                {
                    DTOProduct dtoProduct = returnProducts.get(counter);
                    if(dtoProduct != null && dtoProduct.getEntityProduct() != null && dtoProduct.getEntityProduct().getId() > 0)
                    {
                        EntitySaleOrderReturnProduct entitySaleOrderReturnProduct = new EntitySaleOrderReturnProduct();
                        entitySaleOrderReturnProduct.setId(dtoProduct.getId());
                        entitySaleOrderReturnProduct.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                        entitySaleOrderReturnProduct.setProductId(dtoProduct.getEntityProduct().getId());
                        entitySaleOrderReturnProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());   
                        entitySaleOrderReturnProduct.setDiscount(dtoProduct.getDiscount());
                        entitySaleOrderReturnProduct.setQuantity(dtoProduct.getQuantity());
                        //double subtotal = (dtoProduct.getEntityProduct().getUnitPrice() * dtoProduct.getQuantity() - (dtoProduct.getEntityProduct().getUnitPrice() * dtoProduct.getQuantity() * dtoProduct.getDiscount() / 100));
                        entitySaleOrderReturnProduct.setSubtotal(dtoProduct.getTotal());
                        if(dtoProduct.getEntityProduct().getCreatedOn() == 0)
                        {                    
                            entitySaleOrderReturnProduct.setCreatedOn(TimeUtils.getCurrentTime());                    
                        }    
                        else
                        {
                            entitySaleOrderReturnProduct.setCreatedOn(dtoProduct.getEntityProduct().getCreatedOn());
                        }
                        entitySaleOrderReturnProduct.setModifiedOn(TimeUtils.getCurrentTime());
                        entitySaleOrderReturnProducts.add(entitySaleOrderReturnProduct);

                        EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                        entityShowRoomStock.setId(dtoProduct.getStockId());
                        //entityShowRoomStock.setSaleOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                        entityShowRoomStock.setOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
                        entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                        entityShowRoomStock.setProductName(dtoProduct.getEntityProduct().getName());
                        entityShowRoomStock.setStockIn(dtoProduct.getQuantity());
                        entityShowRoomStock.setStockOut(0);
                        entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_RESTOCK);
                        entityShowRoomStock.setTransactionCategoryTitle(Constants.SS_TRANSACTION_CATEGORY_TITLE_SALE_ORDER_RESTOCK);
                        if(dtoProduct.getEntityProduct().getCreatedOn() == 0)
                        {                    
                            entityShowRoomStock.setCreatedOn(TimeUtils.getCurrentTime());                    
                        }    
                        else
                        {
                            entityShowRoomStock.setCreatedOn(dtoProduct.getEntityProduct().getCreatedOn());
                        }
                        entityShowRoomStock.setModifiedOn(TimeUtils.getCurrentTime());
                        entityShowRoomStocks.add(entityShowRoomStock);

                        if(productIdQuantityMap.containsKey(dtoProduct.getEntityProduct().getId()))
                        {
                            if(productIdQuantityMap.get(dtoProduct.getEntityProduct().getId()) < dtoProduct.getQuantity())
                            {
                                response.setSuccess(false);
                                response.setMessage("You can't return higher quantity than sale quantity for the product " + dtoProduct.getEntityProduct().getName());
                                return response;
                            }
                            productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), productIdQuantityMap.get(dtoProduct.getEntityProduct().getId()) - dtoProduct.getQuantity());
                        }
                    }                    
                } 
            }
            
            
            //----------------------------check available stock logic in details with more simulation
            //checking whether stock is available or not
            Stock stock = new Stock(packet.getPacketHeader().getAppId());
            List<DTOProduct> stockProducts = stock.getCurrentStockByProductIds(productIds);
            for(int productCounter = 0; productCounter < stockProducts.size(); productCounter++)
            {
                DTOProduct stockProduct = stockProducts.get(productCounter);
                
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(packet.getPacketHeader().getAppId());
                EntityShowRoomStock showRoomStockProduct = entityManagerShowRoomStock.getShowRoomProductBySaleOrderNoAndTransactionCategoryId(stockProduct.getEntityProduct().getId(), currentEntitySaleOrder.getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_FULFILLED);
                //we don't have any previous sale for this product
                if(showRoomStockProduct == null)
                {
                    showRoomStockProduct = new EntityShowRoomStock();
                    showRoomStockProduct.setStockIn(0);
                    showRoomStockProduct.setStockOut(0);
                    //response.setSuccess(false);
                    //response.setMessage("Unable to update purchase order. Please contact with system admin.");
                    //return response;
                }
                if(productIdQuantityMap.containsKey(stockProduct.getEntityProduct().getId()) && productIdQuantityMap.get(stockProduct.getEntityProduct().getId()) > (stockProduct.getQuantity() + showRoomStockProduct.getStockOut()) )
                {
                    //insufficient stock for the product
                    response.setSuccess(false);
                    response.setMessage("Insufficient stock for the product " + stockProduct.getEntityProduct().getName() + ". Available stock quantity is " + (stockProduct.getQuantity()+showRoomStockProduct.getStockOut()) );
                    return response;
                }
            }
            //setting user profile info into sale order info
            EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
            
            //setting created by and modified by user
            int userId = (int)session.getUserId();
            dtoSaleOrder.getEntitySaleOrder().setModifiedByUserId(userId);
            EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
            if(tempEntityUser != null && tempEntityUser.getId() > 0)
            {
                dtoSaleOrder.getEntitySaleOrder().setModifiedByUserName(tempEntityUser.getUserName());
            }  
            
            if(entityManagerSaleOrder.updateSaleOrder(currentEntitySaleOrder, dtoSaleOrder.getEntitySaleOrder(), entitySaleOrderProducts, entitySaleOrderReturnProducts, entityShowRoomStocks))
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
        
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder(packet.getPacketHeader().getAppId());
        EntitySaleOrder entitySaleOrder = entityManagerSaleOrder.getSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        if(entitySaleOrder != null)
        {
            dtoSaleOrder.setEntitySaleOrder(entitySaleOrder);
            
            //getting products for the sale order
            EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct(packet.getPacketHeader().getAppId());
            List<EntitySaleOrderProduct> entitySaleOrderProducts = entityManagerSaleOrderProduct.getSaleOrderProductsByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            if(entitySaleOrderProducts != null && !entitySaleOrderProducts.isEmpty())
            {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(packet.getPacketHeader().getAppId());
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
                for(int counter = 0; counter < entitySaleOrderProducts.size(); counter++)
                {
                    EntitySaleOrderProduct entitySaleOrderProduct = entitySaleOrderProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductBySaleOrderNoAndTransactionCategoryId(entitySaleOrderProduct.getProductId(), dtoSaleOrder.getEntitySaleOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_FULFILLED);
                    if(stockProduct != null)
                    {
                        EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());
                        if(entityProduct != null)
                        {
                            DTOProduct dtoProduct = new DTOProduct();
                            dtoProduct.setId(entitySaleOrderProduct.getId());
                            dtoProduct.setStockId(stockProduct.getId());
                            dtoProduct.setQuantity(stockProduct.getStockOut());
                            dtoProduct.setTotal(entitySaleOrderProduct.getSubtotal());
                            dtoProduct.setEntityProduct(entityProduct);
                            dtoProduct.getEntityProduct().setUnitPrice(entitySaleOrderProduct.getUnitPrice());
                            dtoProduct.getEntityProduct().setCostPrice(entitySaleOrderProduct.getCostPrice());
                            dtoProduct.getEntityProduct().setVat(entitySaleOrderProduct.getVat());
                            dtoProduct.setDiscount(entitySaleOrderProduct.getDiscount());
                            dtoProduct.getEntityProduct().setCreatedOn(entitySaleOrderProduct.getCreatedOn());
                            dtoSaleOrder.getProducts().add(dtoProduct);
                        }                        
                    }                    
                }
            }
            
            //getting returned products for this purchase order
            EntityManagerSaleOrderReturnProduct entityManagerSaleOrderReturnProduct = new EntityManagerSaleOrderReturnProduct(packet.getPacketHeader().getAppId());
            List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProducts = entityManagerSaleOrderReturnProduct.getSaleOrderReturnProductsByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            if(entitySaleOrderReturnProducts != null && !entitySaleOrderReturnProducts.isEmpty())
            {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(packet.getPacketHeader().getAppId());
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
                for(int counter = 0; counter < entitySaleOrderReturnProducts.size(); counter++)
                {
                    EntitySaleOrderReturnProduct entitySaleOrderReturnProduct = entitySaleOrderReturnProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductBySaleOrderNoAndTransactionCategoryId(entitySaleOrderReturnProduct.getProductId(), dtoSaleOrder.getEntitySaleOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_RESTOCK);
                    if(stockProduct != null)
                    {
                        EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());
                        if(entityProduct != null)
                        {
                            entityProduct.setCreatedOn(entitySaleOrderReturnProduct.getCreatedOn());
                            entityProduct.setModifiedOn(entitySaleOrderReturnProduct.getModifiedOn());
                            DTOProduct dtoProduct = new DTOProduct();
                            dtoProduct.setId(entitySaleOrderReturnProduct.getId());
                            dtoProduct.setStockId(stockProduct.getId());
                            dtoProduct.setCreatedOn(TimeUtils.convertUnixToHuman(entitySaleOrderReturnProduct.getCreatedOn(), "", ""));
                            dtoProduct.setModifiedOn(TimeUtils.convertUnixToHuman(entitySaleOrderReturnProduct.getModifiedOn(), "", ""));
                            dtoProduct.setQuantity(stockProduct.getStockIn());
                            dtoProduct.setTotal(entitySaleOrderReturnProduct.getSubtotal());
                            dtoProduct.setEntityProduct(entityProduct);
                            dtoProduct.getEntityProduct().setUnitPrice(entitySaleOrderReturnProduct.getUnitPrice());
                            dtoProduct.setDiscount(entitySaleOrderReturnProduct.getDiscount());
                            dtoProduct.getEntityProduct().setCreatedOn(entitySaleOrderReturnProduct.getCreatedOn());
                            dtoSaleOrder.getReturnProducts().add(dtoProduct);
                        }                        
                    }                    
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
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder(packet.getPacketHeader().getAppId());
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
        listSaleOrder.setTotalSaleAmount(entityManagerSaleOrder.getTotalSaleAmount());
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
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder(packet.getPacketHeader().getAppId());
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
        listSaleOrder.setTotalSaleAmount(entityManagerSaleOrder.searchTotalSaleAmountByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo()));
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
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder(packet.getPacketHeader().getAppId());
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
        listSaleOrder.setTotalSaleAmount(entityManagerSaleOrder.searchTotalSaleAmountByCell(dtoSaleOrder.getEntitySaleOrder().getCell()));
        listSaleOrder.setSaleOrders(saleOrders);
        listSaleOrder.setSuccess(true);
        return listSaleOrder;
    }
}
