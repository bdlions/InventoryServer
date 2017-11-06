package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityProductType;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerProductType {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerProductType.class);
    
    public EntityManagerProductType()
    {
    
    }
    
    public List<EntityProductType> getAllProductTypes()
    {
        List<EntityProductType> listProductType = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityProductType> query = session.getNamedQuery("getAllProductTypes");
            listProductType = query.getResultList();
        } 
        finally 
        {
            session.close();
        }
        return listProductType;
    }
}
