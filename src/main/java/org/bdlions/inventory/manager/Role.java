package org.bdlions.inventory.manager;

import org.bdlions.inventory.entity.EntityRole;
import org.bdlions.inventory.entity.manager.EntityManagerRole;
import org.hibernate.Session;

/**
 *
 * @author Nazmul Hasan
 */
public class Role 
{
    public EntityRole getRoleByRoleId(int roleId)
    {
        EntityManagerRole entityManagerRole = new EntityManagerRole();
        EntityRole entityRole = entityManagerRole.getRoleByRoleId(roleId);
        return entityRole;
        /*Session session = HibernateUtil.getSession();
        try
        {
            return getRoleByRoleId(session, roleId);
        }
        finally
        {
            session.close();
        }*/
    }
    public EntityRole getRoleByRoleId(Session session, int roleId)
    {
        EntityManagerRole entityManagerRole = new EntityManagerRole();
        EntityRole entityRole = entityManagerRole.getRoleByRoleId(session, roleId);
        return entityRole;
        /*Query<EntityRole> query = session.getNamedQuery("getRoleById");
        query.setParameter("roleId", roleId);
        return query.uniqueResult();*/
    }
}
