package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerUser 
{
    public EntityUser createUser(EntityUser entityUser)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            return createUser(entityUser, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityUser createUser(EntityUser entityUser, Session session)
    {
        session.save(entityUser);
        return entityUser;
    }

    public boolean updateUser(EntityUser entityUser)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            return updateUser(entityUser, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public boolean updateUser(EntityUser entityUser, Session session)
    {
        session.update(entityUser);
        return true;
    }
    
    public EntityUser getUserByUserId(int userId)
    {
        if(userId <= 0)
        {
            return null;
        }
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityUser> query = session.getNamedQuery("getUserByUserId");
            query.setParameter("userId", userId);
            return query.getSingleResult();                     
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityUser getUserByEmail(String email)
    {
        if(StringUtils.isNullOrEmpty(email))
        {
            return null;
        }
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityUser> query = session.getNamedQuery("getUserByEmail");
            query.setParameter("email", email);
            return query.getSingleResult();                    
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityUser> getUsers(int offset, int limit) 
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityUser> query = session.getNamedQuery("getUsers");
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
}
