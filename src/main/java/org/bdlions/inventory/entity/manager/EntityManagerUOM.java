package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityUOM;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerUOM 
{
    /**
     * This method will return all unit of measure list
     * @return List, unit of measure list
     */
    public List<EntityUOM> getAllUOMs()
    {
        Session session = HibernateUtil.getInstance().getSession();
        try 
        {
            Query<EntityUOM> query = session.getNamedQuery("getAllUOMs");
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
}
