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
import org.bdlions.inventory.dto.DTOPurchaseOrder;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.dto.ListPurchaseOrder;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.entity.EntityPOShowRoomReturnProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerPOShowRoomProduct;
import org.bdlions.inventory.entity.manager.EntityManagerPOShowRoomReturnProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrder;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.util.Constants;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;

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
        DTOPurchaseOrder responseDTOPurchaseOrder = new DTOPurchaseOrder();
        //GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOPurchaseOrder dtoPurchaseOrder = gson.fromJson(packet.getPacketBody(), DTOPurchaseOrder.class);     
        if(dtoPurchaseOrder == null || dtoPurchaseOrder.getEntityPurchaseOrder() == null)
        {
            responseDTOPurchaseOrder.setSuccess(false);
            responseDTOPurchaseOrder.setMessage("Invalid Purchase Order Info. Please try again later.");
            return responseDTOPurchaseOrder;
        }
        else if(dtoPurchaseOrder.getEntityPurchaseOrder().getSupplierUserId() <= 0)
        {
            responseDTOPurchaseOrder.setSuccess(false);
            responseDTOPurchaseOrder.setMessage("Invalid Supplier. Please select a supplier.");
            return responseDTOPurchaseOrder;
        }
        else if(dtoPurchaseOrder.getProducts() == null || dtoPurchaseOrder.getProducts().isEmpty())
        {
            responseDTOPurchaseOrder.setSuccess(false);
            responseDTOPurchaseOrder.setMessage("Please select product for the purchase.");
            return responseDTOPurchaseOrder;
        }
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder(packet.getPacketHeader().getAppId());
        int autoOrderNo = 1;
        EntityPurchaseOrder tempEntityPurchaseOrder = entityManagerPurchaseOrder.getLastPurchaseOrder();        
        if(tempEntityPurchaseOrder != null)
        {
            autoOrderNo = tempEntityPurchaseOrder.getNextOrderNo();
            if(autoOrderNo < 2)
            {
                autoOrderNo = 1;
            }
        }        
        if(!StringUtils.isNullOrEmpty(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo()))
        {
            //check whether order no exists or not
            EntityPurchaseOrder resultEntityPurchaseOrder = entityManagerPurchaseOrder.getPurchaseOrderByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            if(resultEntityPurchaseOrder != null)
            {
                responseDTOPurchaseOrder.setSuccess(false);
                responseDTOPurchaseOrder.setMessage("Order No already exists or invalid.");
                return responseDTOPurchaseOrder;
            }
            if(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo().equals(StringUtils.generatePurchaseOrderNo(autoOrderNo)))
            {
                autoOrderNo = autoOrderNo + 1;
            }
        }
        else
        {            
            String orderNo = StringUtils.generatePurchaseOrderNo(autoOrderNo);
            //check whether this order number already exists or not
            //this checking is required bacause someone may update order no manually which will match auto generated order no
            int maxCounter = 50;
            int counter = 0;
            boolean isValid = false;
            while(++counter <= maxCounter)
            {
                EntityPurchaseOrder resultEntityPurchaseOrder = entityManagerPurchaseOrder.getPurchaseOrderByOrderNo(orderNo);
                if(resultEntityPurchaseOrder != null)
                {
                    autoOrderNo = autoOrderNo + 1;
                    orderNo = StringUtils.generatePurchaseOrderNo(autoOrderNo);
                }
                else
                {
                    isValid = true;
                }
            }
            if(!isValid)
            {
                responseDTOPurchaseOrder.setSuccess(false);
                responseDTOPurchaseOrder.setMessage("Unable to generate order no. Please contact with system admin.");
                return responseDTOPurchaseOrder;
            }
            dtoPurchaseOrder.getEntityPurchaseOrder().setOrderNo(orderNo); 
            autoOrderNo = autoOrderNo + 1;
        }
        dtoPurchaseOrder.getEntityPurchaseOrder().setNextOrderNo(autoOrderNo);       
        
        HashMap<Integer, Double> productIdQuantityMap = new HashMap<>();
        List<EntityPOShowRoomProduct> entityPOShowRoomProducts = new ArrayList<>();
        List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProducts = new ArrayList<>();
        List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
        
        List<DTOProduct> products = dtoPurchaseOrder.getProducts();
        if(products != null && !products.isEmpty())
        {
            int totalProducts = products.size();
            for(int counter = 0; counter < totalProducts; counter++)
            {
                DTOProduct dtoProduct = products.get(counter);            
                if(dtoProduct != null && dtoProduct.getEntityProduct() != null && dtoProduct.getEntityProduct().getId() > 0)
                {
                    if(!productIdQuantityMap.containsKey(dtoProduct.getEntityProduct().getId()))
                    {
                        productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), dtoProduct.getQuantity());
                    }
                    EntityPOShowRoomProduct entityPOShowRoomProduct = new EntityPOShowRoomProduct();
                    entityPOShowRoomProduct.setProductId(dtoProduct.getEntityProduct().getId());
                    entityPOShowRoomProduct.setUnitPrice(dtoProduct.getEntityProduct().getCostPrice());  
                    entityPOShowRoomProduct.setDiscount(dtoProduct.getDiscount());
                    entityPOShowRoomProducts.add(entityPOShowRoomProduct);

                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                    entityShowRoomStock.setProductName(dtoProduct.getEntityProduct().getName());
                    entityShowRoomStock.setStockIn(dtoProduct.getQuantity());
                    entityShowRoomStock.setStockOut(0);
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    entityShowRoomStocks.add(entityShowRoomStock);
                }
            } 
        }  
        
        //setting returned products during purchase
        List<DTOProduct> returnProducts = dtoPurchaseOrder.getReturnProducts();
        if(returnProducts != null && !returnProducts.isEmpty())
        {
            int totalReturnedProducts = returnProducts.size();
            for(int counter = 0; counter < totalReturnedProducts; counter++)
            {
                DTOProduct dtoProduct = returnProducts.get(counter);

                EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct = new EntityPOShowRoomReturnProduct();
                entityPOShowRoomReturnProduct.setProductId(dtoProduct.getEntityProduct().getId());
                entityPOShowRoomReturnProduct.setUnitPrice(dtoProduct.getEntityProduct().getCostPrice());  
                entityPOShowRoomReturnProduct.setDiscount(dtoProduct.getDiscount());
                entityPOShowRoomReturnProduct.setCreatedOn(TimeUtils.getCurrentTime());
                entityPOShowRoomReturnProduct.setModifiedOn(TimeUtils.getCurrentTime());
                entityPOShowRoomReturnProducts.add(entityPOShowRoomReturnProduct);

                EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                entityShowRoomStock.setProductName(dtoProduct.getEntityProduct().getName());
                entityShowRoomStock.setStockIn(0);
                entityShowRoomStock.setStockOut(dtoProduct.getQuantity());
                entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_RETURN);
                entityShowRoomStocks.add(entityShowRoomStock);

                if(productIdQuantityMap.containsKey(dtoProduct.getEntityProduct().getId()))
                {
                    if(productIdQuantityMap.get(dtoProduct.getEntityProduct().getId()) < dtoProduct.getQuantity())
                    {
                        responseDTOPurchaseOrder.setSuccess(false);
                        responseDTOPurchaseOrder.setMessage("You can't return higher quantity than purchased quantity for the product " + dtoProduct.getEntityProduct().getName());
                        return responseDTOPurchaseOrder;
                    }
                    productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), productIdQuantityMap.get(dtoProduct.getEntityProduct().getId()) - dtoProduct.getQuantity());
                }
            } 
        }
          
        
        //setting user profile info into purchase order info
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
        EntityUser entityUser = entityManagerUser.getUserByUserId(dtoPurchaseOrder.getEntityPurchaseOrder().getSupplierUserId());
        if(entityUser != null)
        {
            if(!StringUtils.isNullOrEmpty(entityUser.getUserName()))
            {
                dtoPurchaseOrder.getEntityPurchaseOrder().setSupplierName(entityUser.getUserName());
            }
            dtoPurchaseOrder.getEntityPurchaseOrder().setEmail(entityUser.getEmail());
            dtoPurchaseOrder.getEntityPurchaseOrder().setCell(entityUser.getCell());
        }
        
        //setting created by and modified by user
        int userId = (int)session.getUserId();
        dtoPurchaseOrder.getEntityPurchaseOrder().setCreatedByUserId(userId);
        dtoPurchaseOrder.getEntityPurchaseOrder().setModifiedByUserId(userId);
        EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
        if(tempEntityUser != null && tempEntityUser.getId() > 0)
        {
            dtoPurchaseOrder.getEntityPurchaseOrder().setCreatedByUserName(tempEntityUser.getUserName());
            dtoPurchaseOrder.getEntityPurchaseOrder().setModifiedByUserName(tempEntityUser.getUserName());
        } 
        
        if(!StringUtils.isNullOrEmpty(dtoPurchaseOrder.getInvoiceDate()))
        {
            dtoPurchaseOrder.getEntityPurchaseOrder().setInvoiceOn(TimeUtils.convertHumanToUnix(dtoPurchaseOrder.getInvoiceDate(), "", ""));
        }
        else
        {
            dtoPurchaseOrder.getEntityPurchaseOrder().setInvoiceOn(TimeUtils.getCurrentTime());
        }
        
        EntityPurchaseOrder entityPurchaseOrder = entityManagerPurchaseOrder.createPurchaseOrder(dtoPurchaseOrder.getEntityPurchaseOrder(), entityPOShowRoomProducts, entityPOShowRoomReturnProducts, entityShowRoomStocks);
        responseDTOPurchaseOrder.setEntityPurchaseOrder(entityPurchaseOrder);
        
        if(responseDTOPurchaseOrder.getEntityPurchaseOrder() != null && responseDTOPurchaseOrder.getEntityPurchaseOrder().getId() > 0)
        {
            responseDTOPurchaseOrder.setSuccess(true);
            responseDTOPurchaseOrder.setMessage("Purchase Order is added successfully.");
        }   
        else
        {
            responseDTOPurchaseOrder = new DTOPurchaseOrder();
            responseDTOPurchaseOrder.setSuccess(false);
            responseDTOPurchaseOrder.setMessage("Unable to add Purchase Order. Please try again later..");
        }
        return responseDTOPurchaseOrder;
    }
    
    @ClientRequest(action = ACTION.UPDATE_PURCHASE_ORDER_INFO)
    public ClientResponse updatePurchaseOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOPurchaseOrder dtoPurchaseOrder = gson.fromJson(packet.getPacketBody(), DTOPurchaseOrder.class);     
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
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder(packet.getPacketHeader().getAppId());
        //check whether order no exists or not
        EntityPurchaseOrder resultEntityPurchaseOrder = entityManagerPurchaseOrder.getPurchaseOrderByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
        if(resultEntityPurchaseOrder != null && resultEntityPurchaseOrder.getId() != dtoPurchaseOrder.getEntityPurchaseOrder().getId())
        {
            response.setSuccess(false);
            response.setMessage("Order No already exists or invalid.");
            return response;
        }
        EntityPurchaseOrder currentEntityPurchaseOrder = entityManagerPurchaseOrder.getPurchaseOrderById(dtoPurchaseOrder.getEntityPurchaseOrder().getId());
        if(currentEntityPurchaseOrder != null && dtoPurchaseOrder.getEntityPurchaseOrder().getId() > 0)
        {            
            List<Integer> productIds = new ArrayList<>();
            HashMap<Integer, Double> productIdQuantityMap = new HashMap<>();
            List<EntityPOShowRoomProduct> entityPOShowRoomProducts = new ArrayList<>();
            List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProducts = new ArrayList<>();
            List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
            
            List<DTOProduct> products = dtoPurchaseOrder.getProducts();
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
                        EntityPOShowRoomProduct entityPOShowRoomProduct = new EntityPOShowRoomProduct();
                        entityPOShowRoomProduct.setOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                        entityPOShowRoomProduct.setProductId(dtoProduct.getEntityProduct().getId());
                        entityPOShowRoomProduct.setUnitPrice(dtoProduct.getEntityProduct().getCostPrice());
                        entityPOShowRoomProduct.setDiscount(dtoProduct.getDiscount());
                        entityPOShowRoomProducts.add(entityPOShowRoomProduct);

                        EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                        entityShowRoomStock.setPurchaseOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                        entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                        entityShowRoomStock.setProductName(dtoProduct.getEntityProduct().getName());
                        entityShowRoomStock.setStockIn(dtoProduct.getQuantity());
                        entityShowRoomStock.setStockOut(0);
                        entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                        entityShowRoomStocks.add(entityShowRoomStock);
                    }
                }
            } 
            
            //setting returned products during purchase
            List<DTOProduct> returnProducts = dtoPurchaseOrder.getReturnProducts();
            if(returnProducts != null && !returnProducts.isEmpty())
            {
                int totalReturnedProducts = returnProducts.size();
                for(int counter = 0; counter < totalReturnedProducts; counter++)
                {
                    DTOProduct dtoProduct = returnProducts.get(counter);

                    EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct = new EntityPOShowRoomReturnProduct();
                    entityPOShowRoomReturnProduct.setOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                    entityPOShowRoomReturnProduct.setProductId(dtoProduct.getEntityProduct().getId());
                    entityPOShowRoomReturnProduct.setUnitPrice(dtoProduct.getEntityProduct().getCostPrice());  
                    entityPOShowRoomReturnProduct.setDiscount(dtoProduct.getDiscount());
                    if(dtoProduct.getEntityProduct().getCreatedOn() == 0)
                    {                    
                        entityPOShowRoomReturnProduct.setCreatedOn(TimeUtils.getCurrentTime());                    
                    }    
                    else
                    {
                        entityPOShowRoomReturnProduct.setCreatedOn(dtoProduct.getEntityProduct().getCreatedOn());
                    }
                    entityPOShowRoomReturnProduct.setModifiedOn(TimeUtils.getCurrentTime());
                    entityPOShowRoomReturnProducts.add(entityPOShowRoomReturnProduct);

                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setPurchaseOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                    entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                    entityShowRoomStock.setProductName(dtoProduct.getEntityProduct().getName());
                    entityShowRoomStock.setStockIn(0);
                    entityShowRoomStock.setStockOut(dtoProduct.getQuantity());
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_RETURN);
                    entityShowRoomStocks.add(entityShowRoomStock);

                    if(productIdQuantityMap.containsKey(dtoProduct.getEntityProduct().getId()))
                    {
                        if(productIdQuantityMap.get(dtoProduct.getEntityProduct().getId()) < dtoProduct.getQuantity())
                        {
                            response.setSuccess(false);
                            response.setMessage("You can't return higher quantity than purchased quantity for the product " + dtoProduct.getEntityProduct().getName());
                            return response;
                        }
                        productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), productIdQuantityMap.get(dtoProduct.getEntityProduct().getId()) - dtoProduct.getQuantity());
                    }
                } 
            }
                       
            
            //--------------------------------------we need to revise stock availability checking later
            //checking whether stock is available or not
            /*Stock stock = new Stock(packet.getPacketHeader().getAppId());
            List<DTOProduct> stockProducts = stock.getCurrentStockByProductIds(productIds);
            for(int productCounter = 0; productCounter < stockProducts.size(); productCounter++)
            {
                DTOProduct stockProduct = stockProducts.get(productCounter);
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(packet.getPacketHeader().getAppId());
                EntityShowRoomStock showRoomStockProduct = entityManagerShowRoomStock.getShowRoomProductByPurchaseOrderNoAndTransactionCategoryId(stockProduct.getEntityProduct().getId(), currentEntityPurchaseOrder.getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                if(showRoomStockProduct == null)
                {
                    response.setSuccess(false);
                    response.setMessage("Unable to update purchase order. Please contact with system admin.");
                    return response;
                }
                if(productIdQuantityMap.containsKey(stockProduct.getEntityProduct().getId()) && (productIdQuantityMap.get(stockProduct.getEntityProduct().getId()) < showRoomStockProduct.getStockIn()) && (showRoomStockProduct.getStockIn() - productIdQuantityMap.get(stockProduct.getEntityProduct().getId()) > stockProduct.getQuantity()) )
                {
                    //insufficient stock for the product
                    response.setSuccess(false);
                    response.setMessage("You cannot reduce quantity which is already sold. For the product " + stockProduct.getEntityProduct().getName() + " you can reduce maximum of " + (showRoomStockProduct.getStockIn() - stockProduct.getQuantity()) );
                    return response;
                }
            }*/
            
            //setting user profile info into purchase order info
            EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
            EntityUser entityUser = entityManagerUser.getUserByUserId(dtoPurchaseOrder.getEntityPurchaseOrder().getSupplierUserId());
            if(entityUser != null)
            {
                if(!StringUtils.isNullOrEmpty(entityUser.getUserName()))
                {
                    dtoPurchaseOrder.getEntityPurchaseOrder().setSupplierName(entityUser.getUserName());
                }
                dtoPurchaseOrder.getEntityPurchaseOrder().setEmail(entityUser.getEmail());
                dtoPurchaseOrder.getEntityPurchaseOrder().setCell(entityUser.getCell());
            }
            
            //setting modified by user
            int userId = (int)session.getUserId();
            dtoPurchaseOrder.getEntityPurchaseOrder().setModifiedByUserId(userId);
            EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
            if(tempEntityUser != null && tempEntityUser.getId() > 0)
            {
                dtoPurchaseOrder.getEntityPurchaseOrder().setModifiedByUserName(tempEntityUser.getUserName());
            } 
            
            if(!StringUtils.isNullOrEmpty(dtoPurchaseOrder.getInvoiceDate()))
            {
                dtoPurchaseOrder.getEntityPurchaseOrder().setInvoiceOn(TimeUtils.convertHumanToUnix(dtoPurchaseOrder.getInvoiceDate(), "", ""));
            }
            else
            {
                dtoPurchaseOrder.getEntityPurchaseOrder().setInvoiceOn(TimeUtils.getCurrentTime());
            }
            
            if(entityManagerPurchaseOrder.updatePurchaseOrder(currentEntityPurchaseOrder, dtoPurchaseOrder.getEntityPurchaseOrder(), entityPOShowRoomProducts, entityPOShowRoomReturnProducts, entityShowRoomStocks))
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
    
    @ClientRequest(action = ACTION.FETCH_PURCHASE_ORDER_INFO)
    public ClientResponse getPurchaseOrderInfo(ISession session, IPacket packet) throws Exception 
    {
        
        Gson gson = new Gson();
        DTOPurchaseOrder dtoPurchaseOrder = gson.fromJson(packet.getPacketBody(), DTOPurchaseOrder.class);     
        if(dtoPurchaseOrder == null || dtoPurchaseOrder.getEntityPurchaseOrder() == null || StringUtils.isNullOrEmpty(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo()) )
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid purchase order.");
            return response;
        }
        
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder(packet.getPacketHeader().getAppId());
        EntityPurchaseOrder entityPurchaseOrder = entityManagerPurchaseOrder.getPurchaseOrderByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
        if(entityPurchaseOrder != null)
        {
            dtoPurchaseOrder.setEntityPurchaseOrder(entityPurchaseOrder);
            dtoPurchaseOrder.setInvoiceDate(TimeUtils.convertUnixToHuman(entityPurchaseOrder.getInvoiceOn(), "yyyy-MM-dd", ""));
                    
            //getting products for this purchase order
            EntityManagerPOShowRoomProduct entityManagerPOShowRoomProduct = new EntityManagerPOShowRoomProduct(packet.getPacketHeader().getAppId());
            List<EntityPOShowRoomProduct> entityPOShowRoomProducts = entityManagerPOShowRoomProduct.getPOShowRoomProductsByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            if(entityPOShowRoomProducts != null && !entityPOShowRoomProducts.isEmpty())
            {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(packet.getPacketHeader().getAppId());
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
                for(int counter = 0; counter < entityPOShowRoomProducts.size(); counter++)
                {
                    EntityPOShowRoomProduct entityPOShowRoomProduct = entityPOShowRoomProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductByPurchaseOrderNoAndTransactionCategoryId(entityPOShowRoomProduct.getProductId(), dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    
                    EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());

                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setQuantity(stockProduct.getStockIn());
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.getEntityProduct().setCostPrice(entityPOShowRoomProduct.getUnitPrice());
                    dtoProduct.setDiscount(entityPOShowRoomProduct.getDiscount());
                    dtoPurchaseOrder.getProducts().add(dtoProduct);                    
                }
            }
            //getting returned products for this purchase order
            EntityManagerPOShowRoomReturnProduct entityManagerPOShowRoomReturnProduct = new EntityManagerPOShowRoomReturnProduct(packet.getPacketHeader().getAppId());
            List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProducts = entityManagerPOShowRoomReturnProduct.getPOShowRoomReturnProductsByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            if(entityPOShowRoomReturnProducts != null && !entityPOShowRoomReturnProducts.isEmpty())
            {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(packet.getPacketHeader().getAppId());
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
                for(int counter = 0; counter < entityPOShowRoomReturnProducts.size(); counter++)
                {
                    EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct = entityPOShowRoomReturnProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductByPurchaseOrderNoAndTransactionCategoryId(entityPOShowRoomReturnProduct.getProductId(), dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_RETURN);
                    
                    EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());
                    entityProduct.setCreatedOn(entityPOShowRoomReturnProduct.getCreatedOn());
                    entityProduct.setModifiedOn(entityPOShowRoomReturnProduct.getModifiedOn());
                    
                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setCreatedOn(TimeUtils.convertUnixToHuman(entityPOShowRoomReturnProduct.getCreatedOn(), "", ""));
                    dtoProduct.setModifiedOn(TimeUtils.convertUnixToHuman(entityPOShowRoomReturnProduct.getModifiedOn(), "", ""));
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.setQuantity(stockProduct.getStockOut());                    
                    dtoProduct.getEntityProduct().setCostPrice(entityPOShowRoomReturnProduct.getUnitPrice());
                    dtoProduct.setDiscount(entityPOShowRoomReturnProduct.getDiscount());                    
                    
                    dtoPurchaseOrder.getReturnProducts().add(dtoProduct);                    
                }
            }
            dtoPurchaseOrder.setSuccess(true);
            return dtoPurchaseOrder;
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
        if(dtoPurchaseOrder == null)
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid request to get purchase orders.");
            return response;
        }
        List<DTOPurchaseOrder> purchaseOrders = new ArrayList<>();
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder(packet.getPacketHeader().getAppId());
        List<EntityPurchaseOrder> entityPurchaseOrders = entityManagerPurchaseOrder.getPurchaseOrders(dtoPurchaseOrder.getOffset(), dtoPurchaseOrder.getLimit());
        if(entityPurchaseOrders != null)
        {
            for(int counter = 0; counter < entityPurchaseOrders.size(); counter++)
            {
                EntityPurchaseOrder entityPurchaseOrder = entityPurchaseOrders.get(counter);
                DTOPurchaseOrder dtoPO = new DTOPurchaseOrder();
                dtoPO.setEntityPurchaseOrder(entityPurchaseOrder);
                dtoPO.setOrderDate(TimeUtils.convertUnixToHuman(entityPurchaseOrder.getCreatedOn(), "", ""));
                dtoPO.setInvoiceDate(TimeUtils.convertUnixToHuman(entityPurchaseOrder.getInvoiceOn(), "dd-MM-yyyy", ""));
                purchaseOrders.add(dtoPO);
            } 
        }               
        ListPurchaseOrder listPurchaseOrder = new ListPurchaseOrder();
        listPurchaseOrder.setTotalPurchaseOrders(entityManagerPurchaseOrder.getTotalPurchaseOrders());
        listPurchaseOrder.setSuccess(true);
        listPurchaseOrder.setPurchaseOrders(purchaseOrders);
        return listPurchaseOrder;
    }
    
    @ClientRequest(action = ACTION.FETCH_PURCHASE_ORDERS_BY_ORDER_NO)
    public ClientResponse getPurchaseOrdersByOrderNo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOPurchaseOrder dtoPurchaseOrder = gson.fromJson(packet.getPacketBody(), DTOPurchaseOrder.class);  
        if(dtoPurchaseOrder == null)
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid request to get purchase orders.");
            return response;
        }
        List<DTOPurchaseOrder> purchaseOrders = new ArrayList<>();
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder(packet.getPacketHeader().getAppId());
        List<EntityPurchaseOrder> entityPurchaseOrders = entityManagerPurchaseOrder.searchPurchaseOrderByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo(), dtoPurchaseOrder.getOffset(), dtoPurchaseOrder.getLimit());
        if(entityPurchaseOrders != null)
        {
            for(int counter = 0; counter < entityPurchaseOrders.size(); counter++)
            {
                EntityPurchaseOrder entityPurchaseOrder = entityPurchaseOrders.get(counter);
                DTOPurchaseOrder dtoPO = new DTOPurchaseOrder();
                dtoPO.setEntityPurchaseOrder(entityPurchaseOrder);
                dtoPO.setOrderDate(TimeUtils.convertUnixToHuman(entityPurchaseOrder.getCreatedOn(), "", ""));
                purchaseOrders.add(dtoPO);
            } 
        }               
        ListPurchaseOrder listPurchaseOrder = new ListPurchaseOrder();
        listPurchaseOrder.setTotalPurchaseOrders(entityManagerPurchaseOrder.searchTotalPurchaseOrderByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo()));
        listPurchaseOrder.setSuccess(true);
        listPurchaseOrder.setPurchaseOrders(purchaseOrders);
        return listPurchaseOrder;
    }
    
    @ClientRequest(action = ACTION.FETCH_PURCHASE_ORDERS_BY_CELL)
    public ClientResponse getPurchaseOrdersByCell(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOPurchaseOrder dtoPurchaseOrder = gson.fromJson(packet.getPacketBody(), DTOPurchaseOrder.class);  
        if(dtoPurchaseOrder == null)
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid request to get purchase orders.");
            return response;
        }
        List<DTOPurchaseOrder> purchaseOrders = new ArrayList<>();
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder(packet.getPacketHeader().getAppId());
        List<EntityPurchaseOrder> entityPurchaseOrders = entityManagerPurchaseOrder.searchPurchaseOrderByCell(dtoPurchaseOrder.getEntityPurchaseOrder().getCell(), dtoPurchaseOrder.getOffset(), dtoPurchaseOrder.getLimit());
        if(entityPurchaseOrders != null)
        {
            for(int counter = 0; counter < entityPurchaseOrders.size(); counter++)
            {
                EntityPurchaseOrder entityPurchaseOrder = entityPurchaseOrders.get(counter);
                DTOPurchaseOrder dtoPO = new DTOPurchaseOrder();
                dtoPO.setEntityPurchaseOrder(entityPurchaseOrder);
                dtoPO.setOrderDate(TimeUtils.convertUnixToHuman(entityPurchaseOrder.getCreatedOn(), "", ""));
                purchaseOrders.add(dtoPO);
            } 
        }               
        ListPurchaseOrder listPurchaseOrder = new ListPurchaseOrder();
        listPurchaseOrder.setTotalPurchaseOrders(entityManagerPurchaseOrder.searchTotalPurchaseOrderByCell(dtoPurchaseOrder.getEntityPurchaseOrder().getCell()));
        listPurchaseOrder.setSuccess(true);
        listPurchaseOrder.setPurchaseOrders(purchaseOrders);
        return listPurchaseOrder;
    }    
}
