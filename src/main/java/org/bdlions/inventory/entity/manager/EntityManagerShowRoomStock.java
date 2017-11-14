package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.util.Constants;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerShowRoomStock {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerShowRoomStock.class);
    
    public EntityShowRoomStock addShowRoomStock(EntityShowRoomStock entityShowRoomStock)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            return addShowRoomStock(entityShowRoomStock, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityShowRoomStock addShowRoomStock(EntityShowRoomStock entityShowRoomStock, Session session)
    {
        session.save(entityShowRoomStock);
        return entityShowRoomStock;
    }
    
    public EntityShowRoomStock getPurchaseOrderProductByOrderNoAndCategoryId(EntityShowRoomStock entityShowRoomStock)
    {
        EntityShowRoomStock resultEntityShowRoomStock = new EntityShowRoomStock();
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("getPurchaseOrderProductByOrderNoAndCategoryId");
            queryStockProducts.setParameter("purchaseOrderNo", entityShowRoomStock.getPurchaseOrderNo());
            queryStockProducts.setParameter("transactionCategoryId", entityShowRoomStock.getTransactionCategoryId());
            queryStockProducts.setParameter("productId", entityShowRoomStock.getProductId());
            resultEntityShowRoomStock =  queryStockProducts.getSingleResult();                    
        } 
        finally 
        {
            session.close();
        }
        return resultEntityShowRoomStock;
    }
    
    public int deletePurchaseOrderShowRoomProductsByOrderNo(EntityShowRoomStock entityShowRoomStock)
    {
        int responseCode = 0;
        Session session = HibernateUtil.getSession();
        try 
        {            
            responseCode = deletePurchaseOrderShowRoomProductsByOrderNo(entityShowRoomStock, session);
        } 
        finally 
        {
            session.close();
        }
        return responseCode;
    }
    
    public int deletePurchaseOrderShowRoomProductsByOrderNo(EntityShowRoomStock entityShowRoomStock, Session session)
    {
        int responseCode = 0;
        try 
        {            
            if(entityShowRoomStock != null)
            {
                Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("deletePurchaseOrderShowRoomProductsByOrderNo");
                queryStockProducts.setParameter("purchaseOrderNo", entityShowRoomStock.getPurchaseOrderNo());
                queryStockProducts.setParameter("transactionCategoryId", entityShowRoomStock.getTransactionCategoryId());
                responseCode =  queryStockProducts.executeUpdate();
            }                     
        } 
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return responseCode;
    }
    
    public EntityShowRoomStock getSaleOrderProductByOrderNoAndCategoryId(int productId, String saleOrderNo, int transactionCategoryId)
    {
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("getSaleOrderProductByOrderNoAndCategoryId");
            queryStockProducts.setParameter("saleOrderNo", saleOrderNo);
            queryStockProducts.setParameter("transactionCategoryId", transactionCategoryId);
            queryStockProducts.setParameter("productId", productId);
            return queryStockProducts.getSingleResult();                    
        } 
        finally 
        {
            session.close();
        }
    }
    
    public int deleteProductsBySaleOrderNoAndTransactionCategoryId(String saleOrderNo, int transactionCategoryId, Session session)
    {
        Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("deleteSaleOrderShowRoomProductsByOrderNo");
        queryStockProducts.setParameter("saleOrderNo", saleOrderNo);
        queryStockProducts.setParameter("transactionCategoryId", transactionCategoryId);
        return queryStockProducts.executeUpdate();
    }
}
