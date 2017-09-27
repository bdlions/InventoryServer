/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.DTOCustomer;
import org.bdlions.dto.EntityCustomer;
import org.bdlions.dto.EntityUser;
import org.bdlions.dto.EntityUserRole;
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
    public boolean createCustomer(DTOCustomer dtoCustomer) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        try {
            tx.begin();
            if (dtoCustomer != null && dtoCustomer.getEntityUser() != null) {
                EntityUser user = dtoCustomer.getEntityUser();
                session.save(user);
                dtoCustomer.getEntityUserRole().setUserId(user.getId());
                dtoCustomer.getEntityCustomer().setUserId(user.getId());
                session.save(dtoCustomer.getEntityCustomer());
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
    
    public boolean updateCustomer(DTOCustomer dtoCustomer) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if (dtoCustomer != null && dtoCustomer.getEntityUser() != null) {
                EntityUser user = dtoCustomer.getEntityUser();
                session.update(user);
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
    
    public DTOCustomer getCustomerInfo(int customerId) {
        DTOCustomer dtoCustomer = null;
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityCustomer> query1 = session.getNamedQuery("getCustomerByCustomerId");
            query1.setParameter("customerId", customerId);
            EntityCustomer entityCustomer = query1.getSingleResult();
            
            Query<EntityUser> query2 = session.getNamedQuery("getUserByUserId");
            query2.setParameter("userId", entityCustomer.getUserId());
            EntityUser entityUser = query2.getSingleResult();
            
            dtoCustomer = new DTOCustomer();
            dtoCustomer.setEntityCustomer(entityCustomer);
            dtoCustomer.setEntityUser(entityUser);
            
        } finally {
            session.close();
        }
        return dtoCustomer;
    }
    
    public List<DTOCustomer> getCustomers(DTOCustomer dtoCustomer) {
        List<DTOCustomer> customers = new ArrayList<>();
        return customers;
    }
}
