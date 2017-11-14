package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntitySupplier;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerCustomer 
{
    private final Logger logger = LoggerFactory.getLogger(EntityManagerCustomer.class);
    public EntityCustomer createCustomer(EntityCustomer entityCustomer)
    {
        EntityCustomer resultEntityCustomer = null;
        Session session = HibernateUtil.getSession();
        try 
        {
            resultEntityCustomer = createCustomer(entityCustomer, session);
        } 
        finally 
        {
            session.close();
        }
        return resultEntityCustomer;
    }
    
    public EntityCustomer createCustomer(EntityCustomer entityCustomer, Session session)
    {
        try
        {
            session.save(entityCustomer);
            return entityCustomer;
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return null;
    }
    
    public boolean updateCustomer(EntityCustomer entityCustomer)
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        try 
        {
            status = this.updateCustomer(entityCustomer, session);
        } 
        finally 
        {
            session.close();
        }
        return status;
    }
    
    public boolean updateCustomer(EntityCustomer entityCustomer, Session session)
    {
        try
        {
            session.update(entityCustomer);
            return true;
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return false;
    }
    
    public EntityCustomer getCustomerByCustomerId(EntityCustomer entityCustomer)
    {
        EntityCustomer resultEntityCustomer = new EntityCustomer();
        Session session = HibernateUtil.getSession();
        try 
        {            
            if(entityCustomer != null && entityCustomer.getId() > 0)
            {
                Query<EntityCustomer> query = session.getNamedQuery("getCustomerByCustomerId");
                query.setParameter("customerId", entityCustomer.getId());
                resultEntityCustomer = query.getSingleResult();
            }                     
        } 
        finally 
        {
            session.close();
        }
        return resultEntityCustomer;
    }
    
    public EntityCustomer getCustomerByUserId(EntityCustomer entityCustomer)
    {
        EntityCustomer resultEntityCustomer = new EntityCustomer();
        Session session = HibernateUtil.getSession();
        try 
        {            
            if(entityCustomer != null && entityCustomer.getUserId() > 0)
            {
                Query<EntityCustomer> query = session.getNamedQuery("getCustomerByUserId");
                query.setParameter("userId", entityCustomer.getUserId());
                resultEntityCustomer = query.getSingleResult();
            }                     
        } 
        finally 
        {
            session.close();
        }
        return resultEntityCustomer;
    }
    
    public List<EntityCustomer> getCustomers(int offset, int limit) {
        List<EntityCustomer> entityCustomers = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {
            //set limit, offset and other params in named query
            Query<EntityCustomer> query = session.getNamedQuery("getCustomers");
            entityCustomers = query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return entityCustomers;
    }
}
