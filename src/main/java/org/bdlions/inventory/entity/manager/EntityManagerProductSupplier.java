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
    public List<EntityProductSupplier> getProductSuppliersByProductId(int productId)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityProductSupplier> query = session.getNamedQuery("getProductSuppliersByProductId");
            query.setParameter("productId", productId);
            return query.getResultList();

        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityProductSupplier> getProductSuppliersBySupplierUserId(int supplierUserId)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityProductSupplier> query = session.getNamedQuery("getProductSuppliersBySupplierUserId");
            query.setParameter("supplierUserId", supplierUserId);
            return query.getResultList();

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
}
