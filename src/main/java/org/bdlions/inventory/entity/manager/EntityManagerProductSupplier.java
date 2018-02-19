package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityProductSupplier;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerProductSupplier {
    private int appId;
    public EntityManagerProductSupplier(int appId)
    {
        this.appId = appId;
    }
    
    public List<EntityProductSupplier> getProductSuppliersByProductId(int productId, int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProductSupplier> query = session.getNamedQuery("getProductSuppliersByProductId");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            query.setParameter("productId", productId);
            return query.getResultList();

        } 
        finally 
        {
            session.close();
        }
    }
    
    public int getTotalProductSuppliersByProductId(int productId)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProductSupplier> query = session.getNamedQuery("getProductSuppliersByProductId");
            query.setParameter("productId", productId);
            return query.getResultList().size();
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityProductSupplier> getProductSuppliersBySupplierUserId(int supplierUserId, int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProductSupplier> query = session.getNamedQuery("getProductSuppliersBySupplierUserId");
            if(limit > 0)
            {
                query.setFirstResult(offset);
                query.setMaxResults(limit);
            }            
            query.setParameter("supplierUserId", supplierUserId);
            return query.getResultList();

        } 
        finally 
        {
            session.close();
        }
    }
    public int getTotalProductSuppliersBySupplierUserId(int supplierUserId)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProductSupplier> query = session.getNamedQuery("getProductSuppliersBySupplierUserId");
            query.setParameter("supplierUserId", supplierUserId);
            return query.getResultList().size();

        } 
        finally 
        {
            session.close();
        }
    }
    
    public int updateProductSupplierSupplierUserName(int supplierUserId, String supplierUserName, Session session)
    {
        if(supplierUserId > 0 && !StringUtils.isNullOrEmpty(supplierUserName))
        {
            Query<EntityProductSupplier> query = session.getNamedQuery("updateProductSupplierSupplierUserName");
            query.setParameter("supplierUserName", supplierUserName);
            query.setParameter("supplierUserId", supplierUserId);
            return query.executeUpdate();
        }
        return 0;
    }
    
    public int deleteProductSuppliersByProductId(int productId, Session session)
    {
        if(productId > 0)
        {
            Query<EntityProductSupplier> query = session.getNamedQuery("deleteProductSuppliers");
            query.setParameter("productId", productId);
            return query.executeUpdate();
        }
        return 0;
    }
    
    public EntityProductSupplier addProductSupplier(EntityProductSupplier entityProductSupplier, Session session)
    {
        session.save(entityProductSupplier);
        return entityProductSupplier;
    }
    
    public int deleteProductSuppliersBySupplierUserIds(int productId, List<Integer> supplierUserIds, Session session)
    {
        if(supplierUserIds != null && !supplierUserIds.isEmpty())
        {
            Query<EntityProductSupplier> query = session.getNamedQuery("deleteProductSuppliersBySupplierUserIds");
            query.setParameter("productId", productId);
            query.setParameter("supplierUserIds", supplierUserIds);
            return query.executeUpdate();
        }
        return 0;
    }
    
    public int deleteSupplierProductsByProductIds(int supplierUserId, List<Integer> productIds, Session session)
    {
        if(productIds != null && !productIds.isEmpty())
        {
            Query<EntityProductSupplier> query = session.getNamedQuery("deleteSupplierProductsByProductIds");
            query.setParameter("supplierUserId", supplierUserId);
            query.setParameter("productIds", productIds);
            return query.executeUpdate();
        }
        return 0;
    }
}
