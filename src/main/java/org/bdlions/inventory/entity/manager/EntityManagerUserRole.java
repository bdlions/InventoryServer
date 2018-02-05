package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityUserRole;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerUserRole 
{
    public EntityUserRole createUserRole(EntityUserRole entityUserRole, Session session)
    {
        session.save(entityUserRole);
        return entityUserRole;
    }
    
    public EntityUserRole createUserRole(EntityUserRole entityUserRole)
    {
        Session session = HibernateUtil.getInstance().getSession();
        try 
        {
            return createUserRole(entityUserRole, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityUserRole> getUserRolesByUserId(int userId) 
    {
        Session session = HibernateUtil.getInstance().getSession();
        try 
        {
            Query<EntityUserRole> query = session.getNamedQuery("getUserRolesByUserId");
            query.setParameter("userId", userId);
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityUserRole> getUserRolesByRoleIds(List<Integer> roleIds) 
    {
        Session session = HibernateUtil.getInstance().getSession();
        try 
        {
            Query<EntityUserRole> query = session.getNamedQuery("getUserRolesByRoleIds");
            query.setParameter("roleIds", roleIds);
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
    
    public int deleteUserRoles(int userId, Session session)
    {
        if(userId > 0)
        {
            Query<EntityUserRole> query = session.getNamedQuery("deleteUserRoles");
            query.setParameter("userId", userId);
            return query.executeUpdate();
        }
        return 0;
    }
}
