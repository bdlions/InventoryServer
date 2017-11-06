package org.bdlions.inventory.entity.manager;

import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerUser {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerUser.class);
    
    public EntityUser createUser(EntityUser entityUser)
    {
        EntityUser resultEntityUser = null;
        Session session = HibernateUtil.getSession();
        try 
        {
            resultEntityUser = this.createUser(entityUser, session);
        } 
        finally 
        {
            session.close();
        }
        return resultEntityUser;
    }
    
    public EntityUser createUser(EntityUser entityUser, Session session)
    {
        try
        {
            session.save(entityUser);
            return entityUser;
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return null;
    }

    public boolean updateUser(EntityUser entityUser)
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        try 
        {
            status = this.updateUser(entityUser, session);
        } 
        finally 
        {
            session.close();
        }
        return status;
    }
    
    public boolean updateUser(EntityUser entityUser, Session session)
    {
        try
        {
            session.update(entityUser);
            return true;
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return false;
    }
    
    public EntityUser getUserByUserId(EntityUser entityUser)
    {
        EntityUser resultEntityUser = new EntityUser();
        Session session = HibernateUtil.getSession();
        try 
        {            
            if(entityUser != null && entityUser.getId() > 0)
            {
                Query<EntityUser> query = session.getNamedQuery("getUserByUserId");
                query.setParameter("userId", entityUser.getId());
                resultEntityUser = query.getSingleResult();
            }                     
        } 
        finally 
        {
            session.close();
        }
        return resultEntityUser;
    }
}
