package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityProductSupplier;
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
