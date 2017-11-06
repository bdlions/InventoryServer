package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityProductCategory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerProductCategory {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerProductCategory.class);
    
    public EntityManagerProductCategory()
    {
    
    }
    
    public List<EntityProductCategory> getAllProductCategories()
    {
        List<EntityProductCategory> listProductCategory = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityProductCategory> query = session.getNamedQuery("getAllProductCategories");
            listProductCategory = query.getResultList();
        } 
        finally 
        {
            session.close();
        }
        return listProductCategory;
    }
}
