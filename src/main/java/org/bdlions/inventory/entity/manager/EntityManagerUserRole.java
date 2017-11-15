package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityUserRole;
import org.hibernate.Session;

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
        Session session = HibernateUtil.getSession();
        try 
        {
            return createUserRole(entityUserRole, session);
        } 
        finally 
        {
            session.close();
        }
    }
}
