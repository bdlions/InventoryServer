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
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerPOShowRoomProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrder;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
import org.bdlions.inventory.entity.manager.EntityManagerSupplier;
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
        else if(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo() == null || dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo().equals(""))
        {
            responseDTOPurchaseOrder.setSuccess(false);
            responseDTOPurchaseOrder.setMessage("Order no is required.");
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
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
        //check whether order no exists or not
        EntityPurchaseOrder resultEntityPurchaseOrder = entityManagerPurchaseOrder.getPurchaseOrderByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
        if(resultEntityPurchaseOrder != null)
        {
            responseDTOPurchaseOrder.setSuccess(false);
            responseDTOPurchaseOrder.setMessage("Order No already exists or invalid.");
            return responseDTOPurchaseOrder;
        }
        
        List<DTOProduct> products = dtoPurchaseOrder.getProducts();
        int totalProducts = products.size();
        List<EntityPOShowRoomProduct> entityPOShowRoomProducts = new ArrayList<>();
        List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
        for(int counter = 0; counter < totalProducts; counter++)
        {
            DTOProduct dtoProduct = products.get(counter);
            EntityPOShowRoomProduct entityPOShowRoomProduct = new EntityPOShowRoomProduct();
            entityPOShowRoomProduct.setOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            entityPOShowRoomProduct.setProductId(dtoProduct.getEntityProduct().getId());
            entityPOShowRoomProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());   
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
        
        //setting user profile info into purchase order info
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        EntityUser entityUser = entityManagerUser.getUserByUserId(dtoPurchaseOrder.getEntityPurchaseOrder().getSupplierUserId());
        if(entityUser != null)
        {
            dtoPurchaseOrder.getEntityPurchaseOrder().setSupplierName(entityUser.getFirstName()+" "+entityUser.getLastName());
            dtoPurchaseOrder.getEntityPurchaseOrder().setEmail(entityUser.getEmail());
            dtoPurchaseOrder.getEntityPurchaseOrder().setCell(entityUser.getCell());
        }
        
        EntityPurchaseOrder entityPurchaseOrder = entityManagerPurchaseOrder.createPurchaseOrder(dtoPurchaseOrder.getEntityPurchaseOrder(), entityPOShowRoomProducts, entityShowRoomStocks);
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
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
        //check whether order no exists or not
        EntityPurchaseOrder resultEntityPurchaseOrder = entityManagerPurchaseOrder.getPurchaseOrderByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
        if(resultEntityPurchaseOrder != null && resultEntityPurchaseOrder.getId() != dtoPurchaseOrder.getEntityPurchaseOrder().getId())
        {
            response.setSuccess(false);
            response.setMessage("Order No already exists or invalid.");
            return response;
        }
        
        if(dtoPurchaseOrder.getEntityPurchaseOrder().getId() > 0)
        {            
            List<Integer> productIds = new ArrayList<>();
            HashMap<Integer, Double> productIdQuantityMap = new HashMap<>();
            
            List<DTOProduct> products = dtoPurchaseOrder.getProducts();
            int totalProducts = products.size();
            List<EntityPOShowRoomProduct> entityPOShowRoomProducts = new ArrayList<>();
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
                }
                
                EntityPOShowRoomProduct entityPOShowRoomProduct = new EntityPOShowRoomProduct();
                entityPOShowRoomProduct.setOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                entityPOShowRoomProduct.setProductId(dtoProduct.getEntityProduct().getId());
                entityPOShowRoomProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());   
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
            
            //checking whether stock is available or not
            Stock stock = new Stock();
            List<DTOProduct> stockProducts = stock.getCurrentStockByProductIds(productIds);
            for(int productCounter = 0; productCounter < stockProducts.size(); productCounter++)
            {
                DTOProduct stockProduct = stockProducts.get(productCounter);
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                EntityShowRoomStock showRoomStockProduct = entityManagerShowRoomStock.getShowRoomProductByPurchaseOrderNoAndTransactionCategoryId(stockProduct.getEntityProduct().getId(), dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                
                if(productIdQuantityMap.containsKey(stockProduct.getEntityProduct().getId()) && (productIdQuantityMap.get(stockProduct.getEntityProduct().getId()) < showRoomStockProduct.getStockIn()) && (showRoomStockProduct.getStockIn() - productIdQuantityMap.get(stockProduct.getEntityProduct().getId()) > stockProduct.getQuantity()) )
                {
                    //insufficient stock for the product
                    response.setSuccess(false);
                    response.setMessage("You cannot reduce quantity which is already sold. For the product " + stockProduct.getEntityProduct().getName() + " you can reduce maximum of " + (showRoomStockProduct.getStockIn() - stockProduct.getQuantity()) );
                    return response;
                }
            }
            
            //setting user profile info into purchase order info
            EntityManagerUser entityManagerUser = new EntityManagerUser();
            EntityUser entityUser = entityManagerUser.getUserByUserId(dtoPurchaseOrder.getEntityPurchaseOrder().getSupplierUserId());
            if(entityUser != null)
            {
                dtoPurchaseOrder.getEntityPurchaseOrder().setSupplierName(entityUser.getFirstName()+" "+entityUser.getLastName());
                dtoPurchaseOrder.getEntityPurchaseOrder().setEmail(entityUser.getEmail());
                dtoPurchaseOrder.getEntityPurchaseOrder().setCell(entityUser.getCell());
            }
            
            if(entityManagerPurchaseOrder.updatePurchaseOrder(dtoPurchaseOrder.getEntityPurchaseOrder(), entityPOShowRoomProducts, entityShowRoomStocks))
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
        
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
        EntityPurchaseOrder entityPurchaseOrder = entityManagerPurchaseOrder.getPurchaseOrderByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
        if(entityPurchaseOrder != null)
        {
            dtoPurchaseOrder.setEntityPurchaseOrder(entityPurchaseOrder);
            EntityManagerPOShowRoomProduct entityManagerPOShowRoomProduct = new EntityManagerPOShowRoomProduct();
            List<EntityPOShowRoomProduct> entityPOShowRoomProducts = entityManagerPOShowRoomProduct.getPOShowRoomProductsByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            if(entityPOShowRoomProducts != null && !entityPOShowRoomProducts.isEmpty())
            {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
                for(int counter = 0; counter < entityPOShowRoomProducts.size(); counter++)
                {
                    EntityPOShowRoomProduct entityPOShowRoomProduct = entityPOShowRoomProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductByPurchaseOrderNoAndTransactionCategoryId(entityPOShowRoomProduct.getProductId(), dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());

                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setQuantity(stockProduct.getStockIn());
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.getEntityProduct().setUnitPrice(entityPOShowRoomProduct.getUnitPrice());
                    dtoPurchaseOrder.getProducts().add(dtoProduct);
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
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
        List<EntityPurchaseOrder> entityPurchaseOrders = entityManagerPurchaseOrder.getPurchaseOrders(dtoPurchaseOrder.getOffset(), dtoPurchaseOrder.getLimit());
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
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
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
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
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
