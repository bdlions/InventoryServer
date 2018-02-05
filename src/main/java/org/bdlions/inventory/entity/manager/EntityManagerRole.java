package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityRole;
import org.bdlions.inventory.entity.EntityUser;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerRole 
{
    public EntityRole getRoleByRoleId(int roleId)
    {
        Session session = HibernateUtil.getInstance().getSession();
        try
        {
            return getRoleByRoleId(session, roleId);
        }
        finally
        {
            session.close();
        }
    }
    public EntityRole getRoleByRoleId(Session session, int roleId)
    {
        Query<EntityRole> query = session.getNamedQuery("getRoleByRoleId");
        query.setParameter("roleId", roleId);
        return query.uniqueResult();
    }
    
    public List<EntityRole> getRoles() 
    {
        Session session = HibernateUtil.getInstance().getSession();
        try 
        {
            Query<EntityRole> query = session.getNamedQuery("getRoles");
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
}
