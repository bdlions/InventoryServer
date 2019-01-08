package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntityPurchaseOrderPayment;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderPayment;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.util.Constants;
import org.bdlions.inventory.util.TimeUtils;
import org.bdlions.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerCustomer 
{
    private int appId;
    public EntityManagerCustomer(int appId)
    {
        this.appId = appId;
    }
    
    /**
     * This method will return entity customer by customer id
     * @param customerId customer id
     * @return EntityCustomer EntityCustomer
     */
    public EntityCustomer getCustomerByCustomerId(int customerId)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            return this.getCustomerByUserId(userId, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityCustomer getCustomerByUserId(int userId, Session session)
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
    
    /**
     * This method will create new customer using session
     * @param entityCustomer entity customer
     * @param session session
     * @return EntityCustomer EntityCustomer
     */
    public EntityCustomer createCustomer(EntityCustomer entityCustomer, Session session)
    {
        TimeUtils timeUtils = new TimeUtils();
        entityCustomer.setCreatedOn(timeUtils.getCurrentTime());
        entityCustomer.setModifiedOn(timeUtils.getCurrentTime());
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
     * @param entitySaleOrderPaymentIn customer last year balance
     * @return EntityCustomer EntityCustomer
     */
    public EntityCustomer createCustomer(EntityCustomer entityCustomer, EntityUser entityUser, EntityUserRole entityUserRole, EntitySaleOrderPayment entitySaleOrderPaymentIn)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try 
        {
            if(entityUser != null)
            {
                EntityManagerUser entityManagerUser = new EntityManagerUser(this.appId);
                EntityUser resultEntityUser = entityManagerUser.createUser(entityUser, entityUserRole, session);
                entityCustomer.setUserId(resultEntityUser.getId());
            }
            if(entitySaleOrderPaymentIn != null)
            {
                entitySaleOrderPaymentIn.setCustomerUserId(entityCustomer.getUserId());
                entitySaleOrderPaymentIn.setCustomerName(entityCustomer.getCustomerName());
                entitySaleOrderPaymentIn.setUnixPaymentDate(TimeUtils.getCurrentTime());
                entitySaleOrderPaymentIn.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                EntityManagerSaleOrderPayment entityManagerSaleOrderPayment = new EntityManagerSaleOrderPayment(appId);
                entityManagerSaleOrderPayment.createSaleOrderPayment(entitySaleOrderPaymentIn, session);

                //setting customer current balance because of sale order payment table entry
                double currentDue = entityManagerSaleOrderPayment.getCustomerCurrentDue( entityCustomer.getUserId(), session);
                entityCustomer.setBalance(currentDue);
            }
        
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
        TimeUtils timeUtils = new TimeUtils();
        entityCustomer.setModifiedOn(timeUtils.getCurrentTime());
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
     * This method will update entity customer and/or entity user and/or entity sale order
     * @param entityCustomer entity customer
     * @param entityUser entity user
     * @param entitySaleOrder entity sale order
     * @param entitySaleOrderPaymentIn customer last year balance
     * @return boolean true
     */
    public boolean updateCustomer(EntityCustomer entityCustomer, EntityUser entityUser, EntitySaleOrder entitySaleOrder, EntitySaleOrderPayment entitySaleOrderPaymentIn)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();        
        try 
        {
            if(entityUser != null)
            {
                EntityManagerUser entityManagerUser = new EntityManagerUser(this.appId);
                entityManagerUser.updateUser(entityUser, session);
            }
            if(entitySaleOrder != null)
            {
                EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder(this.appId);
                entityManagerSaleOrder.updateSaleOrderCustomerInfo(entitySaleOrder, session);
            }
            if(entitySaleOrderPaymentIn != null)
            {
                EntityManagerSaleOrderPayment entityManagerSaleOrderPayment = new EntityManagerSaleOrderPayment(appId);
                //delete current entry
                entityManagerSaleOrderPayment.deleteCustomerSaleOrderPaymentsByPaymentTypeId(entityCustomer.getUserId(), Constants.SALE_ORDER_PAYMENT_TYPE_ID_ADD_PREVIOUS_DUE_IN, session);
                entitySaleOrderPaymentIn.setCustomerUserId(entityCustomer.getUserId());
                entitySaleOrderPaymentIn.setCustomerName(entityCustomer.getCustomerName()); 
                entitySaleOrderPaymentIn.setUnixPaymentDate(TimeUtils.getCurrentTime());
                entitySaleOrderPaymentIn.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                entityManagerSaleOrderPayment.createSaleOrderPayment(entitySaleOrderPaymentIn, session);
                 //setting customer current balance because of sale order payment table entry
                double currentDue = entityManagerSaleOrderPayment.getCustomerCurrentDue( entityCustomer.getUserId(), session);
                entityCustomer.setBalance(currentDue);
            }
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
     * This method will return entity customer list
     * @return List entity customer list
     */
    public List<EntityCustomer> getCustomers() {
        List<EntityCustomer> entityCustomers = new ArrayList<>();
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityCustomer> query = session.getNamedQuery("getCustomers");
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
     * This method will return total due of all customers
     * @return double customers total due
     */
    public double getCustomersTotalDue() {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<Object> query = session.getNamedQuery("getCustomersTotalDue");
            List<Object> resultList = query.getResultList();
            if(resultList == null || resultList.isEmpty())
            {
                return 0;
            }
            return (double)resultList.get(0);
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
    
    public EntitySaleOrderPayment addCustomerSalePayment(EntitySaleOrderPayment entitySaleOrderPaymentIn)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();           
        try 
        {
            if(entitySaleOrderPaymentIn != null)
            {
                EntityManagerSaleOrderPayment entityManagerSaleOrderPayment = new EntityManagerSaleOrderPayment(appId);
                entitySaleOrderPaymentIn = entityManagerSaleOrderPayment.createSaleOrderPayment(entitySaleOrderPaymentIn, session);
                 //setting customer current balance because of sale order payment table entry
                double currentDue = entityManagerSaleOrderPayment.getCustomerCurrentDue( entitySaleOrderPaymentIn.getCustomerUserId(), session);
                EntityCustomer entityCustomer = getCustomerByUserId(entitySaleOrderPaymentIn.getCustomerUserId());
                entityCustomer.setBalance(currentDue);
                updateCustomer(entityCustomer, session);
            }
            tx.commit();
            return entitySaleOrderPaymentIn;
        } 
        finally 
        {
            session.close();
        }
    }
    
    public boolean updateCustomerSalePayment(EntitySaleOrderPayment entitySaleOrderPaymentIn)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();           
        try 
        {
            if(entitySaleOrderPaymentIn != null)
            {
                EntityManagerSaleOrderPayment entityManagerSaleOrderPayment = new EntityManagerSaleOrderPayment(appId);
                entityManagerSaleOrderPayment.updateSaleOrderPayment(entitySaleOrderPaymentIn, session);
                 //setting customer current balance because of sake order payment table entry
                double currentDue = entityManagerSaleOrderPayment.getCustomerCurrentDue( entitySaleOrderPaymentIn.getCustomerUserId(), session);
                EntityCustomer entityCustomer = getCustomerByUserId(entitySaleOrderPaymentIn.getCustomerUserId());
                entityCustomer.setBalance(currentDue);
                updateCustomer(entityCustomer, session);
                
                if(!StringUtils.isNullOrEmpty(entitySaleOrderPaymentIn.getReference()))
                {
                    //updating paid in purchase order
                    EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder(appId);
                    entityManagerSaleOrder.updateSaleOrderPaidByOrderNo(entitySaleOrderPaymentIn.getAmountOut(), entitySaleOrderPaymentIn.getReference(), session);
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
}
