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
    /**
     * This method will return all product categories
     * @return List, product category list
     */
    public List<EntityProductCategory> getAllProductCategories()
    {
        Session session = HibernateUtil.getSession();
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
