package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.DatabaseLoader;
import org.bdlions.inventory.entity.EntityOrganization;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul
 */
public class EntityManagerOrganization {
    public List<EntityOrganization> getOrganizations()
    {
        Session session = DatabaseLoader.getInstance().getSession();
        try 
        {
            Query<EntityOrganization> query = session.getNamedQuery("getOrganizations");
            return query.getResultList();

        } 
        finally 
        {
            session.close();
        }
    }
}
