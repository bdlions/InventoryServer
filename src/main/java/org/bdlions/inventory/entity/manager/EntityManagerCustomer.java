package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerCustomer 
{
    /**
     * This method will return entity customer by customer id
     * @param customerId customer id
     * @return EntityCustomer EntityCustomer
     */
    public EntityCustomer getCustomerByCustomerId(int customerId)
    {
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityCustomer> query = session.getNamedQuery("getCustomerByCustomerId");
            query.setParameter("customerId", customerId);
            List<EntityCustomer> customerList = query.getResultList();
            if(customerList == null || customerList.isEmpty())
            {
                return null;
            }
            else
            {
                return customerList.get(0);
            }
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity customer by user id
     * @param userId user id
     * @return EntityCustomer EntityCustomer
     */
    public EntityCustomer getCustomerByUserId(int userId)
    {
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityCustomer> query = session.getNamedQuery("getCustomerByUserId");
            query.setParameter("userId", userId);
            List<EntityCustomer> customerList = query.getResultList();
            if(customerList == null || customerList.isEmpty())
            {
                return null;
            }
            else
            {
                return customerList.get(0);
            }                    
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will create new customer using session
     * @param entityCustomer entity customer
     * @param session session
     * @return EntityCustomer EntityCustomer
     */
    public EntityCustomer createCustomer(EntityCustomer entityCustomer, Session session)
    {
        session.save(entityCustomer);
        return entityCustomer;
    }
    
    /**
     * This method will create new customer
     * @param entityCustomer entity customer
     * @return EntityCustomer EntityCustomer
     */
    public EntityCustomer createCustomer(EntityCustomer entityCustomer)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            return createCustomer(entityCustomer, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will create new customer and/or entity user and/or entity user role
     * @param entityCustomer entity customer
     * @param entityUser entity user
     * @param entityUserRole entity user role
     * @return EntityCustomer EntityCustomer
     */
    public EntityCustomer createCustomer(EntityCustomer entityCustomer, EntityUser entityUser, EntityUserRole entityUserRole)
    {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        tx.begin();
        if(entityUser != null)
        {
            EntityManagerUser entityManagerUser = new EntityManagerUser();
            EntityUser resultEntityUser = entityManagerUser.createUser(entityUser, entityUserRole, session);
            entityCustomer.setUserId(resultEntityUser.getId());
        }
        try 
        {
            EntityCustomer resultEntityCustomer = createCustomer(entityCustomer, session);
            tx.commit();
            return resultEntityCustomer;
        } 
        finally 
        {
            session.close();
        }
    }    
    
    /**
     * This method will update entity customer using session
     * @param entityCustomer entity customer
     * @param session session
     * @return boolean true
     */
    public boolean updateCustomer(EntityCustomer entityCustomer, Session session)
    {
        session.update(entityCustomer);
        return true;
    }
    
    /**
     * This method will update entity customer
     * @param entityCustomer entity customer
     * @return boolean true
     */
    public boolean updateCustomer(EntityCustomer entityCustomer)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            return updateCustomer(entityCustomer, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will update entity customer and/or entity user
     * @param entityCustomer entity customer
     * @param entityUser entity user
     * @return boolean true
     */
    public boolean updateCustomer(EntityCustomer entityCustomer, EntityUser entityUser)
    {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        tx.begin();
        if(entityUser != null)
        {
            EntityManagerUser entityManagerUser = new EntityManagerUser();
            entityManagerUser.updateUser(entityUser, session);
        }
        try 
        {
            updateCustomer(entityCustomer, session);
            tx.commit();
            return true;
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity customer list
     * @param offset offset
     * @param limit limit
     * @return List entity customer list
     */
    public List<EntityCustomer> getCustomers(int offset, int limit) {
        List<EntityCustomer> entityCustomers = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityCustomer> query = session.getNamedQuery("getCustomers");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            entityCustomers = query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return entityCustomers;
    }
    
    /**
     * This method will return total number of customers
     * @return Integer total number of customers
     */
    public int getTotalCustomers() {
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityCustomer> query = session.getNamedQuery("getCustomers");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity customer list - search by name, case insensitive
     * @param customerName customer name
     * @param offset offset
     * @param limit limit
     * @return List entity customer list
     */
    public List<EntityCustomer> searchCustomersByName(String customerName, int offset, int limit) {
        List<EntityCustomer> entityCustomers = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityCustomer> query = session.getNamedQuery("searchCustomerByName");
            query.setParameter("customerName", "%" + customerName.toLowerCase() + "%");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            entityCustomers = query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return entityCustomers;
    }
    
    /**
     * This method will return total number of customers - search by name, case insensitive
     * @param customerName customer name
     * @return Integer total number of customers
     */
    public int searchTotalCustomersByName(String customerName) {
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityCustomer> query = session.getNamedQuery("searchCustomerByName");
            query.setParameter("customerName", "%" + customerName.toLowerCase() + "%");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity customer list - search by cell, case insensitive
     * @param cell customer cell number
     * @param offset offset
     * @param limit limit
     * @return List entity customer list
     */
    public List<EntityCustomer> searchCustomersByCell(String cell, int offset, int limit) {
        List<EntityCustomer> entityCustomers = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityCustomer> query = session.getNamedQuery("searchCustomerByCell");
            query.setParameter("cell", "%" + cell.toLowerCase() + "%");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            entityCustomers = query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return entityCustomers;
    }
    
    /**
     * This method will return total number of customers - search by cell, case insensitive
     * @param cell customer cell number
     * @return Integer total number of customers
     */
    public int searchTotalCustomersByCell(String cell) {
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityCustomer> query = session.getNamedQuery("searchCustomerByCell");
            query.setParameter("cell", "%" + cell.toLowerCase() + "%");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity customer list - search by email, case insensitive
     * @param email customer email address
     * @param offset offset
     * @param limit limit
     * @return List entity customer list
     */
    public List<EntityCustomer> searchCustomersByEmail(String email, int offset, int limit) {
        List<EntityCustomer> entityCustomers = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityCustomer> query = session.getNamedQuery("searchCustomerByEmail");
            query.setParameter("email", "%" + email.toLowerCase() + "%");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            entityCustomers = query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return entityCustomers;
    }
    
    /**
     * This method will return total number of customers - search by email, case insensitive
     * @param email customer email address
     * @return Integer total number of customers
     */
    public int searchTotalCustomersByEmail(String email) {
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityCustomer> query = session.getNamedQuery("searchCustomerByEmail");
            query.setParameter("email", "%" + email.toLowerCase() + "%");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
}
