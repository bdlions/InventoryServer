package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityUOM;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerUOM {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerUOM.class);
    
    public EntityManagerUOM()
    {
    
    }
    
    public List<EntityUOM> getAllUOMs(){
        List<EntityUOM> listUOM = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityUOM> query = session.getNamedQuery("getAllUOMs");
            listUOM = query.getResultList();
        } 
        finally 
        {
            session.close();
        }
        return listUOM;
    }
}
