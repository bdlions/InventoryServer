package org.bdlions.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.DTOProduct;
import org.bdlions.dto.DTOPurchaseOrder;
import org.bdlions.dto.EntityPOShowRoomProduct;
import org.bdlions.dto.EntityProduct;
import org.bdlions.dto.EntityPurchaseOrder;
import org.bdlions.dto.EntityShowRoomStock;
import org.bdlions.util.Constants;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class Purchase {
    private final Logger logger = LoggerFactory.getLogger(Purchase.class);
    public boolean addPurchaseOrderInfo(DTOPurchaseOrder dtoPurchaseOrder)
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        try {            
            if (dtoPurchaseOrder != null && dtoPurchaseOrder.getEntityPurchaseOrder() != null) {
                tx.begin();
                EntityPurchaseOrder entityPurchaseOrder = dtoPurchaseOrder.getEntityPurchaseOrder();
                session.save(entityPurchaseOrder);
                List<DTOProduct> products = dtoPurchaseOrder.getProducts();
                int totalProducts = products.size();
                for(int counter = 0; counter < totalProducts; counter++)
                {
                    DTOProduct dtoProduct = products.get(counter);
                    EntityPOShowRoomProduct entityPOShowRoomProduct = new EntityPOShowRoomProduct();
                    entityPOShowRoomProduct.setOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                    entityPOShowRoomProduct.setProductId(dtoProduct.getEntityProduct().getId());
                    entityPOShowRoomProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());                    
                    session.save(entityPOShowRoomProduct);
                    
                    EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                    entityShowRoomStock.setPurchaseOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                    entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                    entityShowRoomStock.setStockIn(dtoProduct.getQuantity());
                    entityShowRoomStock.setStockOut(0);
                    entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    session.save(entityShowRoomStock);
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
        return status;
    }
    
    public DTOPurchaseOrder getPurchaseOrderInfo(DTOPurchaseOrder dtoPurchaseOrder)
    {
        Session session = HibernateUtil.getSession();
        try {
            
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getPurchaseOrderByOrderNo");
            query.setParameter("orderNo", dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            EntityPurchaseOrder entityPurchaseOrder =  query.getSingleResult();
            dtoPurchaseOrder.setEntityPurchaseOrder(entityPurchaseOrder);
            
            Query<EntityPOShowRoomProduct> queryShowRoomProducts = session.getNamedQuery("getPurchaseOrderProductsByOrderNo");
            queryShowRoomProducts.setParameter("orderNo", dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            List<EntityPOShowRoomProduct> showRoomProducts =  queryShowRoomProducts.getResultList();
            if(showRoomProducts != null)
            {
                for(int counter = 0; counter < showRoomProducts.size(); counter++)
                {
                    EntityPOShowRoomProduct entityPOShowRoomProduct = showRoomProducts.get(counter);
                    Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("getPurchaseOrderProductByOrderNoAndCategoryId");
                    queryStockProducts.setParameter("purchaseOrderNo", dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                    queryStockProducts.setParameter("transactionCategoryId", Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    queryStockProducts.setParameter("productId", entityPOShowRoomProduct.getProductId());
                    EntityShowRoomStock stockProduct =  queryStockProducts.getSingleResult();
                    
                    Query<EntityProduct> queryEntityEntityProduct = session.getNamedQuery("getProductByProductId");
                    queryEntityEntityProduct.setParameter("productId", stockProduct.getProductId());
                    EntityProduct entityProduct =  queryEntityEntityProduct.getSingleResult();
                    
                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setQuantity(stockProduct.getStockIn());
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.getEntityProduct().setUnitPrice(entityPOShowRoomProduct.getUnitPrice());
                    
                    dtoPurchaseOrder.getProducts().add(dtoProduct);
                }
            }
            
        } finally {
            session.close();
        }
        return dtoPurchaseOrder;
    }
    
    public EntityPurchaseOrder getEntityPurchaseOrder(EntityPurchaseOrder entityPurchaseOrder)
    {
        EntityPurchaseOrder resultEntityPurchaseOrder = null;
        Session session = HibernateUtil.getSession();
        try {
            
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getPurchaseOrderById");
            query.setParameter("id", entityPurchaseOrder.getId());
            resultEntityPurchaseOrder =  query.getSingleResult();
            
        } finally {
            session.close();
        }
        return resultEntityPurchaseOrder;
    }
    
    public EntityPurchaseOrder getEntityPurchaseOrderByOrderNo(EntityPurchaseOrder entityPurchaseOrder)
    {
        EntityPurchaseOrder resultEntityPurchaseOrder = null;
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
        return resultEntityPurchaseOrder;
    }
    
    public List<DTOPurchaseOrder> getPurchaseOrders(DTOPurchaseOrder dtoPurchaseOrder)
    {
        List<DTOPurchaseOrder> purchaseOrders = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getAllPurchaseOrders");
            List<EntityPurchaseOrder> entityPurchaseOrders =  query.getResultList();
            for(int counter = 0; counter < entityPurchaseOrders.size(); counter++)
            {
                EntityPurchaseOrder entityPurchaseOrder = entityPurchaseOrders.get(counter);
                DTOPurchaseOrder dtoPO = new DTOPurchaseOrder();
                dtoPO.setEntityPurchaseOrder(entityPurchaseOrder);
                purchaseOrders.add(dtoPO);
            }
        } finally {
            session.close();
        }
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
                    EntityPurchaseOrder entityPurchaseOrder = dtoPurchaseOrder.getEntityPurchaseOrder();
                    session.update(entityPurchaseOrder);
                    
                    //we have to follow delete then insert approach and update some tables.
                    //delete entityPOShowRoomProduct based on existing order no
                    Query<EntityPOShowRoomProduct> queryShowRoomProducts = session.getNamedQuery("deletePurchaseOrderProductsByOrderNo");
                    queryShowRoomProducts.setParameter("orderNo", currEntityPurchaseOrder.getOrderNo());
                    int code =  queryShowRoomProducts.executeUpdate();

                    //delete entityShowRoomStock based on existing order no
                    Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("deletePurchaseOrderShowRoomProductsByOrderNo");
                    queryStockProducts.setParameter("purchaseOrderNo", currEntityPurchaseOrder.getOrderNo());
                    queryStockProducts.setParameter("transactionCategoryId", Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    code =  queryStockProducts.executeUpdate();

                    List<DTOProduct> products = dtoPurchaseOrder.getProducts();
                    int totalProducts = products.size();
                    for(int counter = 0; counter < totalProducts; counter++)
                    {
                        DTOProduct dtoProduct = products.get(counter);
                        EntityPOShowRoomProduct entityPOShowRoomProduct = new EntityPOShowRoomProduct();
                        entityPOShowRoomProduct.setOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                        entityPOShowRoomProduct.setProductId(dtoProduct.getEntityProduct().getId());
                        entityPOShowRoomProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());                    
                        session.save(entityPOShowRoomProduct);

                        EntityShowRoomStock entityShowRoomStock = new EntityShowRoomStock();
                        entityShowRoomStock.setPurchaseOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
                        entityShowRoomStock.setProductId(dtoProduct.getEntityProduct().getId());
                        entityShowRoomStock.setStockIn(dtoProduct.getQuantity());
                        entityShowRoomStock.setStockOut(0);
                        entityShowRoomStock.setTransactionCategoryId(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                        session.save(entityShowRoomStock);
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
