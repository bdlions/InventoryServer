package org.bdlions.inventory.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOCustomer;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.entity.manager.EntityManagerCustomer;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.entity.manager.EntityManagerUserRole;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class Customer {
    private final Logger logger = LoggerFactory.getLogger(Customer.class);
    
    public DTOCustomer createCustomer(DTOCustomer dtoCustomer) 
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        try 
        {
            if(dtoCustomer != null && dtoCustomer.getEntityUser() != null && dtoCustomer.getEntityUserRole() != null && dtoCustomer.getEntityCustomer() != null)
            {
                tx.begin();
                EntityManagerUser entityManagerUser = new EntityManagerUser();
                EntityUser entityUser = entityManagerUser.createUser(dtoCustomer.getEntityUser(), session);
                if (entityUser != null && entityUser.getId() > 0 ) 
                {
                    dtoCustomer.getEntityUserRole().setUserId(entityUser.getId());
                    EntityManagerUserRole entityManagerUserRole = new EntityManagerUserRole();
                    EntityUserRole entityUserRole = entityManagerUserRole.createUserRole(dtoCustomer.getEntityUserRole(), session); 
                    if(entityUserRole != null && entityUserRole.getId() > 0)
                    {
                        dtoCustomer.setEntityUserRole(entityUserRole);
                        dtoCustomer.getEntityCustomer().setUserId(entityUser.getId());
                        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
                        EntityCustomer entityCustomer = entityManagerCustomer.createCustomer(dtoCustomer.getEntityCustomer(), session);
                        if(entityCustomer != null && entityCustomer.getId() > 0)
                        {
                            dtoCustomer.setEntityCustomer(entityCustomer);
                            tx.commit();
                            status = true;
                        }                        
                    }
                    /*EntityUser user = dtoCustomer.getEntityUser();
                    session.save(user);
                    dtoCustomer.getEntityUserRole().setUserId(user.getId());
                    dtoCustomer.getEntityCustomer().setUserId(user.getId());
                    session.save(dtoCustomer.getEntityUserRole());
                    session.save(dtoCustomer.getEntityCustomer());
                    tx.commit();
                    status = true;*/
                }
                if(!status)
                {
                    tx.rollback();                
                } 
            }            
        }
        catch(Exception ex){
            logger.error(ex.toString());
            tx.rollback();
        }
        finally {
            session.close();
        }
        if(status)
        {
            return dtoCustomer;
        }
        else
        {
            return null;
        }
    }
    
    public boolean updateCustomer(DTOCustomer dtoCustomer) 
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try 
        {
            if(dtoCustomer != null && dtoCustomer.getEntityUser() != null && dtoCustomer.getEntityCustomer() != null)
            {
                tx.begin();
                EntityManagerUser entityManagerUser = new EntityManagerUser();
                if(entityManagerUser.updateUser(dtoCustomer.getEntityUser(), session))
                {
                    //update user role if required.
                    
                    EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
                    if(entityManagerCustomer.updateCustomer(dtoCustomer.getEntityCustomer(), session))
                    {
                        tx.commit();
                        status = true;
                    }
                }
                if(!status)
                {
                    tx.rollback();
                }
            }
            /*tx.begin();
            if (dtoCustomer != null && dtoCustomer.getEntityUser() != null) {
                EntityUser user = dtoCustomer.getEntityUser();
                session.update(user);
                //update user role if required.
                session.update(dtoCustomer.getEntityCustomer());
                tx.commit();
                return true;
            }*/
        }
        catch(Exception ex){
            logger.error(ex.toString());
            tx.rollback();
        }
        finally {
            session.close();
        }
        return status;
    }
    
    public DTOCustomer getCustomerInfo(EntityCustomer reqEntityCustomer) 
    {
        DTOCustomer dtoCustomer = null;
        Session session = HibernateUtil.getSession();
        try 
        {
            EntityCustomer entityCustomer = new EntityCustomer();
            if(reqEntityCustomer != null && reqEntityCustomer.getId() > 0)
            {
                EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
                entityCustomer = entityManagerCustomer.getCustomerByCustomerId(reqEntityCustomer);
                
                /*Query<EntityCustomer> query1 = session.getNamedQuery("getCustomerByCustomerId");
                query1.setParameter("customerId", reqEntityCustomer.getId());
                entityCustomer = query1.getSingleResult();*/
            }
            else if(reqEntityCustomer != null && reqEntityCustomer.getUserId() > 0)
            {
                EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
                entityCustomer = entityManagerCustomer.getCustomerByUserId(reqEntityCustomer);
                
                /*Query<EntityCustomer> query1 = session.getNamedQuery("getCustomerByUserId");
                query1.setParameter("userId", reqEntityCustomer.getUserId());
                entityCustomer = query1.getSingleResult();*/
            }
            if(entityCustomer != null && entityCustomer.getId() > 0)
            {
                EntityUser reqEntityUser = new EntityUser();
                reqEntityUser.setId(entityCustomer.getUserId());
                EntityManagerUser entityManagerUser = new EntityManagerUser();
                EntityUser entityUser = entityManagerUser.getUserByUserId(reqEntityUser.getId());
                if(entityUser != null && entityUser.getId() > 0)
                {
                    //set user role if required
                    dtoCustomer = new DTOCustomer();
                    dtoCustomer.setEntityCustomer(entityCustomer);
                    dtoCustomer.setEntityUser(entityUser);
                }    
                
                /*Query<EntityUser> query2 = session.getNamedQuery("getUserByUserId");
                query2.setParameter("userId", entityCustomer.getUserId());
                EntityUser entityUser = query2.getSingleResult();
                //set user role if required
                dtoCustomer = new DTOCustomer();
                dtoCustomer.setEntityCustomer(entityCustomer);
                dtoCustomer.setEntityUser(entityUser);*/
            }
        } 
        finally 
        {
            session.close();
        }
        return dtoCustomer;
    }
    
    public List<DTOCustomer> getCustomers(DTOCustomer dtoCustomer) {
        List<DTOCustomer> customers = new ArrayList<>();
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
        List<EntityCustomer> entityCustomers = entityManagerCustomer.getCustomers(0, 10);
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        for(EntityCustomer entityCustomer : entityCustomers)
        {
            EntityUser reqEntityUser = new EntityUser();
            reqEntityUser.setId(entityCustomer.getUserId());
            EntityUser entityUser =  entityManagerUser.getUserByUserId(reqEntityUser.getId());

            DTOCustomer tempDTOCustomer = new DTOCustomer();
            tempDTOCustomer.setEntityCustomer(entityCustomer);
            tempDTOCustomer.setEntityUser(entityUser);
            //set user role if required
            customers.add(tempDTOCustomer);
        }        
        /*Session session = HibernateUtil.getSession();
        try {
            //set limit, offset and other params in named query
            Query<EntityCustomer> query = session.getNamedQuery("getCustomers");
            List<EntityCustomer> entityCustomers = query.getResultList();
            for(EntityCustomer entityCustomer : entityCustomers)
            {
                Query<EntityUser> queryUser = session.getNamedQuery("getUserByUserId");
                queryUser.setParameter("userId", entityCustomer.getUserId());
                EntityUser entityUser = queryUser.uniqueResult();
                DTOCustomer tempDTOCustomer = new DTOCustomer();
                tempDTOCustomer.setEntityCustomer(entityCustomer);
                tempDTOCustomer.setEntityUser(entityUser);
                //set user role if required
                customers.add(tempDTOCustomer);
            }
        } finally {
            session.close();
        }*/
        return customers;
    }
}
