package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityPOShowRoomReturnProduct;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerPOShowRoomReturnProduct 
{
    private int appId;
    public EntityManagerPOShowRoomReturnProduct(int appId)
    {
        this.appId = appId;
    }
    
    /**
     * This method will add purchase order show room return product using session
     * @param entityPOShowRoomReturnProduct entity purchase order show room return product
     * @param session session
     * @return EntityPOShowRoomReturnProduct EntityPOShowRoomReturnProduct
     */
    public EntityPOShowRoomReturnProduct addPurchaseOrderShowRoomReturnProduct(EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct, Session session)
    {
        session.save(entityPOShowRoomReturnProduct);
        return entityPOShowRoomReturnProduct;
    }
    
    /**
     * This method will add purchase order show room return product
     * @param entityPOShowRoomReturnProduct entity purchase order show room return product
     * @return EntityPOShowRoomReturnProduct EntityPOShowRoomReturnProduct
     */
    public EntityPOShowRoomReturnProduct addPurchaseOrderShowRoomReturnProduct(EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return addPurchaseOrderShowRoomReturnProduct(entityPOShowRoomReturnProduct, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will add purchase order show room return products using session
     * @param entityPOShowRoomReturnProductList entity purchase order show room return product list
     * @param session session
     * @return List entity purchase order show room return product list
     */
    public List<EntityPOShowRoomReturnProduct> addPurchaseOrderShowRoomReturnProducts(List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProductList, Session session)
    {
        List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProducts = new ArrayList<>();
        if(entityPOShowRoomReturnProductList != null && !entityPOShowRoomReturnProductList.isEmpty())
        {
            for(int counter = 0; counter < entityPOShowRoomReturnProductList.size(); counter++)
            {
                EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct = entityPOShowRoomReturnProductList.get(counter);
                entityPOShowRoomReturnProduct = addPurchaseOrderShowRoomReturnProduct(entityPOShowRoomReturnProduct, session);
                entityPOShowRoomReturnProducts.add(entityPOShowRoomReturnProduct);
            }
        }
        return entityPOShowRoomReturnProducts;
    }
    
    public EntityPOShowRoomReturnProduct saveOrUpdatePurchaseOrderShowRoomReturnProduct(EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct, Session session)
    {
        session.saveOrUpdate(entityPOShowRoomReturnProduct);
        return entityPOShowRoomReturnProduct;
    }
    public List<EntityPOShowRoomReturnProduct> saveOrUpdatePurchaseOrderShowRoomReturnProducts(List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProductList, Session session)
    {
        List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProducts = new ArrayList<>();
        if(entityPOShowRoomReturnProductList != null && !entityPOShowRoomReturnProductList.isEmpty())
        {
            for(int counter = 0; counter < entityPOShowRoomReturnProductList.size(); counter++)
            {
                EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct = entityPOShowRoomReturnProductList.get(counter);
                entityPOShowRoomReturnProduct = saveOrUpdatePurchaseOrderShowRoomReturnProduct(entityPOShowRoomReturnProduct, session);
                entityPOShowRoomReturnProducts.add(entityPOShowRoomReturnProduct);
            }
        }
        return entityPOShowRoomReturnProducts;
    }
    
    /**
     * This method will delete purchase order show room return products using session
     * @param orderNo purchase order no
     * @param session session
     * @return int
     */
    public int deletePOShowRoomReturnProductsByOrderNo(String orderNo, Session session)
    {
        if(!StringUtils.isNullOrEmpty(orderNo))
        {
            Query<EntityPOShowRoomReturnProduct> queryShowRoomReturnProducts = session.getNamedQuery("deletePOShowRoomReturnProductsByOrderNo");
            queryShowRoomReturnProducts.setParameter("orderNo", orderNo);
            return queryShowRoomReturnProducts.executeUpdate();
        }
        return 0;
    }
    
    /**
     * This method will delete purchase order show room return products
     * @param orderNo purchase order no
     * @return int
     */
    public int deletePOShowRoomReturnProductsByOrderNo(String orderNo)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            return deletePOShowRoomReturnProductsByOrderNo(orderNo, session);
        } 
        finally 
        {
            session.close();
        }
    }
        
    /**
     * This method will return purchase order show room return products by order no
     * @param orderNo purchase order no
     * @return List entity purchase order show room return product list
     */
    public List<EntityPOShowRoomReturnProduct> getPOShowRoomReturnProductsByOrderNo(String orderNo)
    {
        if(StringUtils.isNullOrEmpty(orderNo))
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityPOShowRoomReturnProduct> query = session.getNamedQuery("getPOShowRoomReturnProductsByOrderNo");
            query.setParameter("orderNo", orderNo);
            return query.getResultList();                       
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return EntityPOShowRoomReturnProduct based on product id and sale order no
     * @param productId product id
     * @param orderNo order no
     * @return EntityPOShowRoomReturnProduct
     */
    public EntityPOShowRoomReturnProduct getPOShowRoomReturnProductByProductIdAndOrderNo(int productId, String orderNo)
    {
        if(StringUtils.isNullOrEmpty(orderNo) || productId <= 0)
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityPOShowRoomReturnProduct> queryShowRoomProducts = session.getNamedQuery("getPOShowRoomReturnProductByProductIdAndOrderNo");
            queryShowRoomProducts.setParameter("productId", productId);
            queryShowRoomProducts.setParameter("orderNo", orderNo);
            List<EntityPOShowRoomReturnProduct> result = queryShowRoomProducts.getResultList();    
            if(result == null || result.isEmpty())
            {
                return null;
            }
            else
            {
                return result.get(0);
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total sub total amount of purchase order returned products based on product id for a time range
     * @param productId product id
     * @param startTime unix start time
     * @param endTime unix end time
     * @return double total subtotal amount
     */
    public double getSubtotalPOShowRoomReturnProductByProductIdInTimeRange(int productId, long startTime, long endTime)
    {
        double subtotal = 0;
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try
        {
            Query<Object[]> query = session.getNamedQuery("getSubtotalPOShowRoomReturnProductByProductIdInTimeRange");
            query.setParameter("productId", productId);
            query.setParameter("startTime", startTime);
            query.setParameter("endTime", endTime);
            List<Object[]> result = query.getResultList();
            for(Object[] object : result)
            {
                if(object[1] != null)
                {
                    subtotal = (double)object[1];
                }                
            }
        }
        finally 
        {
            session.close();
        }
        return subtotal;
    }
}
