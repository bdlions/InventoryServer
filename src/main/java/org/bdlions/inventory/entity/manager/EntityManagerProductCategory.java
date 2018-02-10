package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityProductCategory;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerProductCategory 
{
    private int appId;
    public EntityManagerProductCategory(int appId)
    {
        this.appId = appId;
    }
    
    /**
     * This method will return all product categories
     * @return List, product category list
     */
    public List<EntityProductCategory> getAllProductCategories()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProductCategory> query = session.getNamedQuery("getAllProductCategories");
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
}
