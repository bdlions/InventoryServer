package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerUser 
{
    private int appId;
    public EntityManagerUser(int appId)
    {
        this.appId = appId;
    }
    /**
     * This method will return entity user by email
     * @param email email
     * @return EntityUser EntityUser
     */
    public EntityUser getUserByEmail(String email)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
        TimeUtils timeUtils = new TimeUtils();
        entityUser.setCreatedOn(timeUtils.getCurrentTime());
        entityUser.setModifiedOn(timeUtils.getCurrentTime());
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
            EntityManagerUserRole entityManagerUserRole = new EntityManagerUserRole(this.appId);
            entityManagerUserRole.createUserRole(entityUserRole, session);
        }
        return entityUser;
    }
    
    public EntityUser createUser(EntityUser entityUser, List<EntityUserRole> entityUserRoles)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try
        {
            entityUser = createUser(entityUser, session);
            if(entityUser != null && entityUserRoles != null && !entityUserRoles.isEmpty())
            {
                for(EntityUserRole entityUserRole: entityUserRoles)
                {
                    entityUserRole.setUserId(entityUser.getId());
                    EntityManagerUserRole entityManagerUserRole = new EntityManagerUserRole(this.appId);
                    entityManagerUserRole.createUserRole(entityUserRole, session);
                }            
            }
            tx.commit();
            return entityUser;
        }
        finally
        {
            session.close();
        }
        
    }
    
    /**
     * This method will create entity user and/or entity user role
     * @param entityUser entity user
     * @param entityUserRole entity user role
     * @return EntityUser EntityUser
     */
    public EntityUser createUser(EntityUser entityUser, EntityUserRole entityUserRole)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
        TimeUtils timeUtils = new TimeUtils();
        entityUser.setModifiedOn(timeUtils.getCurrentTime());
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return updateUser(entityUser, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public boolean updateUser(EntityUser entityUser, List<EntityUserRole> entityUserRoles)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try
        {
            if(entityUser != null && entityUser.getId() > 0)
            {
                updateUser(entityUser, session);
                if(entityUserRoles != null && !entityUserRoles.isEmpty())
                {
                    EntityManagerUserRole entityManagerUserRole = new EntityManagerUserRole(this.appId);
                    entityManagerUserRole.deleteUserRoles(entityUser.getId(), session);
                    for(EntityUserRole entityUserRole: entityUserRoles)
                    {
                        entityUserRole.setUserId(entityUser.getId());                        
                        entityManagerUserRole.createUserRole(entityUserRole, session);
                    }            
                }
            }
            tx.commit();
            return true;
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
    
    public List<EntityUser> getUsersByUserIds(List<Integer> userIds) 
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityUser> query = session.getNamedQuery("getUsersByUserIds");
            query.setParameter("userIds", userIds);
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
}
