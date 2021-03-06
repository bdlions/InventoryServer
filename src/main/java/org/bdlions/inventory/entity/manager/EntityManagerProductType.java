package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityProductType;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerProductType 
{
    private int appId;
    public EntityManagerProductType(int appId)
    {
        this.appId = appId;
    }
    
    /**
     * This method will return all product types
     * @return List, product type list
     */
    public List<EntityProductType> getAllProductTypes()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProductType> query = session.getNamedQuery("getAllProductTypes");
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
}
