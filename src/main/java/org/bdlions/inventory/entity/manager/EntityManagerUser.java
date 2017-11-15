package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerUser 
{
    /**
     * This method will return entity user by email
     * @param email email
     * @return EntityUser EntityUser
     */
    public EntityUser getUserByEmail(String email)
    {
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityUser> query = session.getNamedQuery("getUserByEmail");
            query.setParameter("email", email);
            List<EntityUser> userList = query.getResultList();
            if(userList == null || userList.isEmpty())
            {
                return null;
            }
            else
            {
                return userList.get(0);
            }           
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity user by user id
     * @param userId user id
     * @return EntityUser EntityUser
     */
    public EntityUser getUserByUserId(int userId)
    {
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityUser> query = session.getNamedQuery("getUserByUserId");
            query.setParameter("userId", userId);
            List<EntityUser> userList = query.getResultList();
            if(userList == null || userList.isEmpty())
            {
                return null;
            }
            else
            {
                return userList.get(0);
            }                   
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity user by identity and password
     * @param identity email
     * @param password password
     * @return EntityUser EntityUser
     */
    public EntityUser getUserByCredential(String identity, String password) {
        if(StringUtils.isNullOrEmpty(password))
        {
            return null;
        }        
        EntityUser selectedUser = getUserByEmail(identity);
        if (selectedUser == null) {
            return selectedUser;
        }

        if (selectedUser.getPassword().equals(password)) {
            return selectedUser;
        }
        return null;
    }
    
    /**
     * This method will create entity user using session
     * @param entityUser entity user
     * @param session session
     * @return EntityUser EntityUser
     */
    public EntityUser createUser(EntityUser entityUser, Session session)
    {
        session.save(entityUser);
        return entityUser;
    }
    
    /**
     * This method will create entity user
     * @param entityUser entity user
     * @return EntityUser EntityUser
     */
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
    
    /**
     * This method will create entity user and/or entity user role using session
     * @param entityUser entity user
     * @param entityUserRole entity user role
     * @param session session
     * @return EntityUser EntityUser
     */
    public EntityUser createUser(EntityUser entityUser, EntityUserRole entityUserRole, Session session)
    {
        entityUser = createUser(entityUser, session);
        if(entityUser != null && entityUserRole != null)
        {
            entityUserRole.setUserId(entityUser.getId());
            EntityManagerUserRole entityManagerUserRole = new EntityManagerUserRole();
            entityManagerUserRole.createUserRole(entityUserRole, session);
        }
        return entityUser;
    }
    
    /**
     * This method will create entity user and/or entity user role
     * @param entityUser entity user
     * @param entityUserRole entity user role
     * @return EntityUser EntityUser
     */
    public EntityUser createUser(EntityUser entityUser, EntityUserRole entityUserRole)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            return createUser(entityUser, entityUserRole, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will update entity user using session
     * @param entityUser entity user
     * @param session session
     * @return boolean true
     */
    public boolean updateUser(EntityUser entityUser, Session session)
    {
        session.update(entityUser);
        return true;
    }
    
    /**
     * This method will update entity user
     * @param entityUser entity user
     * @return boolean true
     */
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
    
    /**
     * This method will return user list
     * @param offset offset
     * @param limit limit
     * @return List entity user list
     */
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
