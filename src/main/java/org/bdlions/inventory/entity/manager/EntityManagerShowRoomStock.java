package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerShowRoomStock 
{
    public EntityShowRoomStock addShowRoomStock(EntityShowRoomStock entityShowRoomStock, Session session)
    {
        session.save(entityShowRoomStock);
        return entityShowRoomStock;
    }
    
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
    
    public List<EntityShowRoomStock> addShowRoomStocks(List<EntityShowRoomStock> entityShowRoomStockList, Session session)
    {
        List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
        if(entityShowRoomStockList != null && !entityShowRoomStockList.isEmpty())
        {
            for(int counter = 0; counter < entityShowRoomStockList.size(); counter++)
            {
                EntityShowRoomStock entityShowRoomStock = entityShowRoomStockList.get(counter);
                entityShowRoomStock = addShowRoomStock(entityShowRoomStock, session);
                entityShowRoomStocks.add(entityShowRoomStock);
            }
        }
        return entityShowRoomStocks;
    }
    
    public int deleteShowRoomProductsByPurchaseOrderNoAndTransactionCategoryId(String purchaseOrderNo, int transactionCategoryId)
    {
        Session session = HibernateUtil.getSession();
        try 
        {            
            return deleteShowRoomProductsByPurchaseOrderNoAndTransactionCategoryId(purchaseOrderNo, transactionCategoryId, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public int deleteShowRoomProductsByPurchaseOrderNoAndTransactionCategoryId(String purchaseOrderNo, int transactionCategoryId, Session session)
    {
        int responseCode = 0;
        if(!StringUtils.isNullOrEmpty(purchaseOrderNo) && transactionCategoryId > 0)
        {
            Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("deletePurchaseOrderShowRoomProductsByOrderNo");
            queryStockProducts.setParameter("purchaseOrderNo", purchaseOrderNo);
            queryStockProducts.setParameter("transactionCategoryId", transactionCategoryId);
            responseCode =  queryStockProducts.executeUpdate();
        }
        return responseCode;
    }
    
    public EntityShowRoomStock getShowRoomProductByPurchaseOrderNoAndTransactionCategoryId(int productId, String purchaseOrderNo, int transactionCategoryId)
    {
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityShowRoomStock> query = session.getNamedQuery("getShowRoomProductByPurchaseOrderNoAndTransactionCategoryId");
            query.setParameter("purchaseOrderNo", purchaseOrderNo);
            query.setParameter("transactionCategoryId", transactionCategoryId);
            query.setParameter("productId", productId);
            List<EntityShowRoomStock> productList = query.getResultList();
            if(productList == null || productList.isEmpty())
            {
                return null;
            }
            else
            {
                return productList.get(0);
            }              
        } 
        finally 
        {
            session.close();
        }
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
