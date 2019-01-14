package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerPOShowRoomProduct 
{
    private int appId;
    public EntityManagerPOShowRoomProduct(int appId)
    {
        this.appId = appId;
    }
    
    /**
     * This method will add purchase order show room product using session
     * @param entityPOShowRoomProduct entity purchase order show room product
     * @param session session
     * @return EntityPOShowRoomProduct EntityPOShowRoomProduct
     */
    public EntityPOShowRoomProduct addPurchaseOrderShowRoomProduct(EntityPOShowRoomProduct entityPOShowRoomProduct, Session session)
    {
        session.save(entityPOShowRoomProduct);
        return entityPOShowRoomProduct;
    }
    
    /**
     * This method will add purchase order show room product
     * @param entityPOShowRoomProduct entity purchase order show room product
     * @return EntityPOShowRoomProduct EntityPOShowRoomProduct
     */
    public EntityPOShowRoomProduct addPurchaseOrderShowRoomProduct(EntityPOShowRoomProduct entityPOShowRoomProduct)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return addPurchaseOrderShowRoomProduct(entityPOShowRoomProduct, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will add purchase order show room products using session
     * @param entityPOShowRoomProductList entity purchase order show room product list
     * @param session session
     * @return List entity purchase order show room product list
     */
    public List<EntityPOShowRoomProduct> addPurchaseOrderShowRoomProducts(List<EntityPOShowRoomProduct> entityPOShowRoomProductList, Session session)
    {
        List<EntityPOShowRoomProduct> entityPOShowRoomProducts = new ArrayList<>();
        if(entityPOShowRoomProductList != null && !entityPOShowRoomProductList.isEmpty())
        {
            for(int counter = 0; counter < entityPOShowRoomProductList.size(); counter++)
            {
                EntityPOShowRoomProduct entityPOShowRoomProduct = entityPOShowRoomProductList.get(counter);
                entityPOShowRoomProduct = addPurchaseOrderShowRoomProduct(entityPOShowRoomProduct, session);
                entityPOShowRoomProducts.add(entityPOShowRoomProduct);
            }
        }
        return entityPOShowRoomProducts;
    }
    
    public EntityPOShowRoomProduct saveOrUpdatePurchaseOrderShowRoomProduct(EntityPOShowRoomProduct entityPOShowRoomProduct, Session session)
    {
        session.saveOrUpdate(entityPOShowRoomProduct);
        return entityPOShowRoomProduct;
    }
    
    public List<EntityPOShowRoomProduct> saveOrUpdatePurchaseOrderShowRoomProducts(List<EntityPOShowRoomProduct> entityPOShowRoomProductList, Session session)
    {
        List<EntityPOShowRoomProduct> entityPOShowRoomProducts = new ArrayList<>();
        if(entityPOShowRoomProductList != null && !entityPOShowRoomProductList.isEmpty())
        {
            for(int counter = 0; counter < entityPOShowRoomProductList.size(); counter++)
            {
                EntityPOShowRoomProduct entityPOShowRoomProduct = entityPOShowRoomProductList.get(counter);
                entityPOShowRoomProduct = saveOrUpdatePurchaseOrderShowRoomProduct(entityPOShowRoomProduct, session);
                entityPOShowRoomProducts.add(entityPOShowRoomProduct);
            }
        }
        return entityPOShowRoomProducts;
    }
    
    /**
     * This method will delete purchase order show room products using session
     * @param orderNo purchase order no
     * @param session session
     * @return int
     */
    public int deletePOShowRoomProductsByOrderNo(String orderNo, Session session)
    {
        if(!StringUtils.isNullOrEmpty(orderNo))
        {
            Query<EntityPOShowRoomProduct> queryShowRoomProducts = session.getNamedQuery("deletePOShowRoomProductsByOrderNo");
            queryShowRoomProducts.setParameter("orderNo", orderNo);
            return queryShowRoomProducts.executeUpdate();
        }
        return 0;
    }
    
    /**
     * This method will delete purchase order show room products
     * @param orderNo purchase order no
     * @return int
     */
    public int deletePOShowRoomProductsByOrderNo(String orderNo)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            return deletePOShowRoomProductsByOrderNo(orderNo, session);
        } 
        finally 
        {
            session.close();
        }
    }
        
    /**
     * This method will return purchase order show room products by order no
     * @param orderNo purchase order no
     * @return List entity purchase order show room product list
     */
    public List<EntityPOShowRoomProduct> getPOShowRoomProductsByOrderNo(String orderNo)
    {
        if(StringUtils.isNullOrEmpty(orderNo))
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityPOShowRoomProduct> query = session.getNamedQuery("getPOShowRoomProductsByOrderNo");
            query.setParameter("orderNo", orderNo);
            return query.getResultList();                       
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return EntityPOShowRoomProduct based on product id and order no
     * @param productId product id
     * @param orderNo order no
     * @return EntityPOShowRoomProduct
     */
    public EntityPOShowRoomProduct getPOShowRoomProductByProductIdAndOrderNo(int productId, String orderNo)
    {
        if(StringUtils.isNullOrEmpty(orderNo) || productId <= 0)
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityPOShowRoomProduct> queryShowRoomProducts = session.getNamedQuery("getPOShowRoomProductByProductIdAndOrderNo");
            queryShowRoomProducts.setParameter("productId", productId);
            queryShowRoomProducts.setParameter("orderNo", orderNo);
            List<EntityPOShowRoomProduct> result = queryShowRoomProducts.getResultList();    
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
     * This method will return total sub total amount of purchase order products based on product id for a time range
     * @param productId product id
     * @param startTime unix start time
     * @param endTime unix end time
     * @return double total subtotal amount
     */
    public double getSubtotalPOShowRoomProductByProductIdInTimeRange(int productId, long startTime, long endTime)
    {
        double subtotal = 0;
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try
        {
            Query<Object[]> query = session.getNamedQuery("getSubtotalPOShowRoomProductByProductIdInTimeRange");
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
