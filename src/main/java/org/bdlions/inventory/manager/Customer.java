/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.inventory.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOCustomer;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntityUser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul
 */
public class Customer {
    private final Logger logger = LoggerFactory.getLogger(Customer.class);
    public DTOCustomer createCustomer(DTOCustomer dtoCustomer) {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        try {
            tx.begin();
            if (dtoCustomer != null && dtoCustomer.getEntityUser() != null) {
                EntityUser user = dtoCustomer.getEntityUser();
                session.save(user);
                dtoCustomer.getEntityUserRole().setUserId(user.getId());
                dtoCustomer.getEntityCustomer().setUserId(user.getId());
                session.save(dtoCustomer.getEntityUserRole());
                session.save(dtoCustomer.getEntityCustomer());
                tx.commit();
                status = true;
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
    
    public boolean updateCustomer(DTOCustomer dtoCustomer) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if (dtoCustomer != null && dtoCustomer.getEntityUser() != null) {
                EntityUser user = dtoCustomer.getEntityUser();
                session.update(user);
                //update user role if required.
                session.update(dtoCustomer.getEntityCustomer());
                tx.commit();
                return true;
            }
        }
        catch(Exception ex){
            logger.error(ex.toString());
            tx.rollback();
        }
        finally {
            session.close();
        }
        return false;
    }
    
    public DTOCustomer getCustomerInfo(EntityCustomer reqEntityCustomer) {
        DTOCustomer dtoCustomer = null;
        Session session = HibernateUtil.getSession();
        try {
            EntityCustomer entityCustomer = new EntityCustomer();
            if(reqEntityCustomer.getId() > 0)
            {
                Query<EntityCustomer> query1 = session.getNamedQuery("getCustomerByCustomerId");
                query1.setParameter("customerId", reqEntityCustomer.getId());
                entityCustomer = query1.getSingleResult();
            }
            else if(reqEntityCustomer.getUserId() > 0)
            {
                Query<EntityCustomer> query1 = session.getNamedQuery("getCustomerByUserId");
                query1.setParameter("userId", reqEntityCustomer.getUserId());
                entityCustomer = query1.getSingleResult();
            }
            if(entityCustomer.getId() > 0)
            {
                Query<EntityUser> query2 = session.getNamedQuery("getUserByUserId");
                query2.setParameter("userId", entityCustomer.getUserId());
                EntityUser entityUser = query2.getSingleResult();
                //set user role if required
                dtoCustomer = new DTOCustomer();
                dtoCustomer.setEntityCustomer(entityCustomer);
                dtoCustomer.setEntityUser(entityUser);
            }
        } finally {
            session.close();
        }
        return dtoCustomer;
    }
    
    public List<DTOCustomer> getCustomers(DTOCustomer dtoCustomer) {
        List<DTOCustomer> customers = new ArrayList<>();
        Session session = HibernateUtil.getSession();
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
        }
        return customers;
    }
}
