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
    /**
     * This method will add show room stock product using session
     * @param entityShowRoomStock entity show room stock product
     * @param session session
     * @return EntityShowRoomStock EntityShowRoomStock
     */
    public EntityShowRoomStock addShowRoomStock(EntityShowRoomStock entityShowRoomStock, Session session)
    {
        session.save(entityShowRoomStock);
        return entityShowRoomStock;
    }
    
    /**
     * This method will add show room stock
     * @param entityShowRoomStock entity show room stock product
     * @return EntityShowRoomStock EntityShowRoomStock
     */
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
    
    /**
     * This method will add show room stock products using session
     * @param entityShowRoomStockList entity show room stock product list
     * @param session session
     * @return List entity show room stock list
     */
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
    
    /**
     * This method will delete show room stock products based on purchase order no and transaction category id using session
     * @param purchaseOrderNo purchase order no
     * @param transactionCategoryId transaction category id
     * @param session session
     * @return int
     */
    public int deleteShowRoomProductsByPurchaseOrderNoAndTransactionCategoryId(String purchaseOrderNo, int transactionCategoryId, Session session)
    {
        if(!StringUtils.isNullOrEmpty(purchaseOrderNo) && transactionCategoryId > 0)
        {
            Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("deleteShowRoomProductsByPurchaseOrderNoAndTransactionCategoryId");
            queryStockProducts.setParameter("purchaseOrderNo", purchaseOrderNo);
            queryStockProducts.setParameter("transactionCategoryId", transactionCategoryId);
            return queryStockProducts.executeUpdate();
        }
        return 0;
    }
    
    /**
     * This method will delete show room stock products based on purchase order no and transaction category id
     * @param purchaseOrderNo purchase order no
     * @param transactionCategoryId transaction category id
     * @return int
     */
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
    
    /**
     * This method will return show room stock product based on product id, purchase order no and transaction category id
     * @param productId product id
     * @param purchaseOrderNo purchase order no
     * @param transactionCategoryId transaction category id
     * @return EntityShowRoomStock EntityShowRoomStock
     */
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
    
    /**
     * This method will return show room stock product based on product id, sale order no and transaction category id
     * @param productId product id
     * @param saleOrderNo sale order no
     * @param transactionCategoryId transaction category id
     * @return EntityShowRoomStock EntityShowRoomStock
     */
    public EntityShowRoomStock getShowRoomProductBySaleOrderNoAndTransactionCategoryId(int productId, String saleOrderNo, int transactionCategoryId)
    {
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("getShowRoomProductBySaleOrderNoAndTransactionCategoryId");
            queryStockProducts.setParameter("saleOrderNo", saleOrderNo);
            queryStockProducts.setParameter("transactionCategoryId", transactionCategoryId);
            queryStockProducts.setParameter("productId", productId);
            List<EntityShowRoomStock> productList = queryStockProducts.getResultList();
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
    
    /**
     * This method will return delete room stock product based on sale order no and transaction category id using session
     * @param saleOrderNo sale order no
     * @param transactionCategoryId transaction category id
     * @param session session
     * @return int
     */
    public int deleteShowRoomProductsBySaleOrderNoAndTransactionCategoryId(String saleOrderNo, int transactionCategoryId, Session session)
    {
        if(!StringUtils.isNullOrEmpty(saleOrderNo) && transactionCategoryId > 0)
        {
            Query<EntityShowRoomStock> queryStockProducts = session.getNamedQuery("deleteShowRoomProductsBySaleOrderNoAndTransactionCategoryId");
            queryStockProducts.setParameter("saleOrderNo", saleOrderNo);
            queryStockProducts.setParameter("transactionCategoryId", transactionCategoryId);
            return queryStockProducts.executeUpdate();
        }
        return 0;
    }
    
    /**
     * This method will return delete room stock product based on sale order no and transaction category id
     * @param saleOrderNo sale order no
     * @param transactionCategoryId transaction category id
     * @return int
     */
    public int deleteShowRoomProductsBySaleOrderNoAndTransactionCategoryId(String saleOrderNo, int transactionCategoryId)
    {
        Session session = HibernateUtil.getSession();
        try 
        {            
            return deleteShowRoomProductsBySaleOrderNoAndTransactionCategoryId(saleOrderNo, transactionCategoryId, session);
        } 
        finally 
        {
            session.close();
        }
    }
}
