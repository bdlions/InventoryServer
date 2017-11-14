package org.bdlions.inventory.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.dto.DTOPurchaseOrder;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.manager.EntityManagerPOShowRoomProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrder;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
import org.bdlions.inventory.util.Constants;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class Purchase 
{
    private final Logger logger = LoggerFactory.getLogger(Purchase.class);
    
    public DTOPurchaseOrder addPurchaseOrderInfo(DTOPurchaseOrder dtoPurchaseOrder)
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        try {            
            if (dtoPurchaseOrder != null && dtoPurchaseOrder.getEntityPurchaseOrder() != null) {
                tx.begin();
                EntityPurchaseOrder entityPurchaseOrder = dtoPurchaseOrder.getEntityPurchaseOrder();
                EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
                dtoPurchaseOrder.setEntityPurchaseOrder(entityManagerPurchaseOrder.createPurchaseOrder(entityPurchaseOrder, session));
                //session.save(entityPurchaseOrder);
                
                List<DTOProduct> products = dtoPurchaseOrder.getProducts();
                int totalProducts = products.size();
                EntityManagerPOShowRoomProduct entityManagerPOShowRoomProduct = new EntityManagerPOShowRoomProduct();
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                for(int counter = 0; counter < totalProducts; counter++)
                {
                    DTOProduct dtoProduct = products.get(counter);
                    EntityPOShowRoomProduct entityPOShowRoomProduct = new EntityPOShowRoomProduct();
                    entityPOShowRoomProduct.setOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                    entityPOShowRoomProduct.setProductId(dtoProduct.getEntityProduct().getId());
                    entityPOShowRoomProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());   
                    entityManagerPOShowRoomProduct.addPurchaseOrderShowRoomProduct(entityPOShowRoomProduct, session);
                    //session.save(entityPOShowRoomProduct);
                    
                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setPurchaseOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                    entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                    entityShowRoomStock.setStockIn(dtoProduct.getQuantity());
                    entityShowRoomStock.setStockOut(0);
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    entityManagerShowRoomStock.addShowRoomStock(entityShowRoomStock, session);
                    //session.save(entityShowRoomStock);
                }
                tx.commit();
                status = true;
            }
        }
        catch(Exception ex){
            logger.error(ex.toString());
            tx.rollback();
        }
        finally {
            session.close();
        }
        if(status)
        {
            return dtoPurchaseOrder;
        }
        else
        {
            return null;
        }
    }
    
    public DTOPurchaseOrder getPurchaseOrderInfo(DTOPurchaseOrder dtoPurchaseOrder)
    {
        //--------------if order no is null or empty then return from here
        //Session session = HibernateUtil.getSession();
        try 
        {   
            EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
            dtoPurchaseOrder.setEntityPurchaseOrder(entityManagerPurchaseOrder.getPurchaseOrderByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo()));
            
            /*Query<EntityPurchaseOrder> query = session.getNamedQuery("getPurchaseOrderByOrderNo");
            query.setParameter("orderNo", dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            EntityPurchaseOrder entityPurchaseOrder =  query.getSingleResult();
            dtoPurchaseOrder.setEntityPurchaseOrder(entityPurchaseOrder);*/
            
            EntityManagerPOShowRoomProduct entityManagerPOShowRoomProduct = new EntityManagerPOShowRoomProduct();
            List<EntityPOShowRoomProduct> showRoomProducts = entityManagerPOShowRoomProduct.getPurchaseOrderProductsByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            
            /*Query<EntityPOShowRoomProduct> queryShowRoomProducts = session.getNamedQuery("getPurchaseOrderProductsByOrderNo");
            queryShowRoomProducts.setParameter("orderNo", dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            List<EntityPOShowRoomProduct> showRoomProducts =  queryShowRoomProducts.getResultList();*/
            
            EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
            if(showRoomProducts != null)
            {
                for(int counter = 0; counter < showRoomProducts.size(); counter++)
                {
                    EntityPOShowRoomProduct entityPOShowRoomProduct = showRoomProducts.get(counter);
                    /*Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("getPurchaseOrderProductByOrderNoAndCategoryId");
                    queryStockProducts.setParameter("purchaseOrderNo", dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                    queryStockProducts.setParameter("transactionCategoryId", Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    queryStockProducts.setParameter("productId", entityPOShowRoomProduct.getProductId());
                    EntityShowRoomStock stockProduct =  queryStockProducts.getSingleResult();*/
                    
                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setPurchaseOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    entityShowRoomStock.setProductId(entityPOShowRoomProduct.getProductId());
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getPurchaseOrderProductByOrderNoAndCategoryId(entityShowRoomStock);
                    
                    /*Query<EntityProduct> queryEntityEntityProduct = session.getNamedQuery("getProductByProductId");
                    queryEntityEntityProduct.setParameter("productId", stockProduct.getProductId());
                    EntityProduct entityProduct =  queryEntityEntityProduct.getSingleResult();*/
                    
                    EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
                    EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());
                    
                    
                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setQuantity(stockProduct.getStockIn());
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.getEntityProduct().setUnitPrice(entityPOShowRoomProduct.getUnitPrice());
                    
                    dtoPurchaseOrder.getProducts().add(dtoProduct);
                }
            }
            
        } 
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        /*finally {
            session.close();
        }*/
        return dtoPurchaseOrder;
    }
    
    public EntityPurchaseOrder getEntityPurchaseOrder(EntityPurchaseOrder entityPurchaseOrder)
    {
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
        return entityManagerPurchaseOrder.getPurchaseOrderById(entityPurchaseOrder.getId());        
        /*EntityPurchaseOrder resultEntityPurchaseOrder = null;
        Session session = HibernateUtil.getSession();
        try {
            
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getPurchaseOrderById");
            query.setParameter("id", entityPurchaseOrder.getId());
            resultEntityPurchaseOrder =  query.getSingleResult();
            
        } finally {
            session.close();
        }
        return resultEntityPurchaseOrder;*/
    }
    
    public EntityPurchaseOrder getEntityPurchaseOrderByOrderNo(EntityPurchaseOrder entityPurchaseOrder)
    {
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
        return entityManagerPurchaseOrder.getPurchaseOrderByOrderNo(entityPurchaseOrder.getOrderNo());  
        
        /*EntityPurchaseOrder resultEntityPurchaseOrder = null;
        Session session = HibernateUtil.getSession();
        try {
            
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getPurchaseOrderByOrderNo");
            query.setParameter("orderNo", entityPurchaseOrder.getOrderNo());
            resultEntityPurchaseOrder =  query.getSingleResult();
            
        }
        catch(Exception ex)
        {
        
        }
        finally {
            session.close();
        }
        return resultEntityPurchaseOrder;*/
    }
    
    public List<DTOPurchaseOrder> getPurchaseOrders(DTOPurchaseOrder dtoPurchaseOrder)
    {
        List<DTOPurchaseOrder> purchaseOrders = new ArrayList<>();
        //Session session = HibernateUtil.getSession();
        try {
            EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
            List<EntityPurchaseOrder> entityPurchaseOrders = entityManagerPurchaseOrder.getPurchaseOrders(0, 10);
            
            /*Query<EntityPurchaseOrder> query = session.getNamedQuery("getAllPurchaseOrders");
            List<EntityPurchaseOrder> entityPurchaseOrders =  query.getResultList();*/
            
            for(int counter = 0; counter < entityPurchaseOrders.size(); counter++)
            {
                EntityPurchaseOrder entityPurchaseOrder = entityPurchaseOrders.get(counter);
                DTOPurchaseOrder dtoPO = new DTOPurchaseOrder();
                dtoPO.setEntityPurchaseOrder(entityPurchaseOrder);
                purchaseOrders.add(dtoPO);
            }
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        /*finally {
            session.close();
        }*/
        
        return purchaseOrders;
    }
    
    public boolean updatePurchaseOrderInfo(DTOPurchaseOrder dtoPurchaseOrder)
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        try 
        {
            if (dtoPurchaseOrder != null && dtoPurchaseOrder.getEntityPurchaseOrder() != null) 
            {   
                //getting current entity purchase order based on entity purchase order id
                EntityPurchaseOrder currEntityPurchaseOrder =  this.getEntityPurchaseOrder(dtoPurchaseOrder.getEntityPurchaseOrder());
                if(currEntityPurchaseOrder != null && currEntityPurchaseOrder.getId() > 0)
                {
                    tx.begin();
                    //updating entity purchase order table
                    EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
                    entityManagerPurchaseOrder.updatePurchaseOrder(dtoPurchaseOrder.getEntityPurchaseOrder());
                    
                    /*EntityPurchaseOrder entityPurchaseOrder = dtoPurchaseOrder.getEntityPurchaseOrder();
                    session.update(entityPurchaseOrder);*/
                    
                    //we have to follow delete then insert approach and update some tables.
                    //delete entityPOShowRoomProduct based on existing order no
                    /*Query<EntityPOShowRoomProduct> queryShowRoomProducts = session.getNamedQuery("deletePurchaseOrderProductsByOrderNo");
                    queryShowRoomProducts.setParameter("orderNo", currEntityPurchaseOrder.getOrderNo());
                    int code =  queryShowRoomProducts.executeUpdate();*/
                    
                    EntityManagerPOShowRoomProduct entityManagerPOShowRoomProduct = new EntityManagerPOShowRoomProduct();
                    entityManagerPOShowRoomProduct.deletePurchaseOrderProductsByOrderNo(currEntityPurchaseOrder.getOrderNo(), session);

                    //delete entityShowRoomStock based on existing order no
                    /*Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("deletePurchaseOrderShowRoomProductsByOrderNo");
                    queryStockProducts.setParameter("purchaseOrderNo", currEntityPurchaseOrder.getOrderNo());
                    queryStockProducts.setParameter("transactionCategoryId", Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    code =  queryStockProducts.executeUpdate();*/
                    
                    EntityShowRoomStock eShowRoomStock = new EntityShowRoomStock();
                    eShowRoomStock.setPurchaseOrderNo(currEntityPurchaseOrder.getOrderNo());
                    eShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                    entityManagerShowRoomStock.deletePurchaseOrderShowRoomProductsByOrderNo(eShowRoomStock, session);

                    List<DTOProduct> products = dtoPurchaseOrder.getProducts();
                    int totalProducts = products.size();
                    for(int counter = 0; counter < totalProducts; counter++)
                    {
                        DTOProduct dtoProduct = products.get(counter);
                        EntityPOShowRoomProduct entityPOShowRoomProduct = new EntityPOShowRoomProduct();
                        entityPOShowRoomProduct.setOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                        entityPOShowRoomProduct.setProductId(dtoProduct.getEntityProduct().getId());
                        entityPOShowRoomProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());    
                        entityManagerPOShowRoomProduct.addPurchaseOrderShowRoomProduct(entityPOShowRoomProduct, session);
                        //session.save(entityPOShowRoomProduct);

                        EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                        entityShowRoomStock.setPurchaseOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                        entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                        entityShowRoomStock.setStockIn(dtoProduct.getQuantity());
                        entityShowRoomStock.setStockOut(0);
                        entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                        entityManagerShowRoomStock.addShowRoomStock(entityShowRoomStock, session);
                        //session.save(entityShowRoomStock);
                    }
                    tx.commit();
                    status = true;
                }                
            }
        }
        catch(Exception ex){
            logger.error(ex.toString());
            tx.rollback();
        }
        finally {
            session.close();
        }
        return status;
    }
}
