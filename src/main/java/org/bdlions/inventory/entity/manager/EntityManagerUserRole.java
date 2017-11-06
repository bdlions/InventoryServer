package org.bdlions.inventory.entity.manager;

import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityUserRole;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerUserRole {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerUserRole.class);
    
    public EntityUserRole createUserRole(EntityUserRole entityUserRole)
    {
        EntityUserRole resultEntityUserRole = null;
        Session session = HibernateUtil.getSession();
        try 
        {
            resultEntityUserRole = this.createUserRole(entityUserRole, session);
        } 
        finally 
        {
            session.close();
        }
        return resultEntityUserRole;
    }
    
    public EntityUserRole createUserRole(EntityUserRole entityUserRole, Session session)
    {
        try
        {
            session.save(entityUserRole);
            return entityUserRole;
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return null;
    }
}
