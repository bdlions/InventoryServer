package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityCompany;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerCompany {
    private int appId;
    public EntityManagerCompany(int appId)
    {
        this.appId = appId;
    }
    
    public EntityCompany getCompanyInfo()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try {
            
            Query<EntityCompany> query = session.getNamedQuery("getCompanyInfo");
            List<EntityCompany> companyList = query.getResultList();
            if(companyList == null || companyList.isEmpty())
            {
                return null;
            }
            else
            {
                return companyList.get(0);
            }           
        }
        finally 
        {
            session.close();
        }
    }
}
