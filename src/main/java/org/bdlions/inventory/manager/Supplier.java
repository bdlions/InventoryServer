package org.bdlions.inventory.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class Supplier {
    private final Logger logger = LoggerFactory.getLogger(Supplier.class);
    public DTOSupplier createSupplier(DTOSupplier dtoSupplier) {
        /*
        Nazmul vai:
        1. You are creating a user profile for a supplier, right??
        So why a user profile creating depens on creating supplier??
        Supplier is dependent with user but user is not dependent with supplier
       
        2. Plz adding or updating roles in role entity
        */
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        try {
            tx.begin();
            if (dtoSupplier != null && dtoSupplier.getEntityUser() != null) {
                EntityUser user = dtoSupplier.getEntityUser();
                session.save(user);
                dtoSupplier.getEntityUserRole().setUserId(user.getId());
                dtoSupplier.getEntitySupplier().setUserId(user.getId());
                session.save(dtoSupplier.getEntityUserRole());
                session.save(dtoSupplier.getEntitySupplier());
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
            return dtoSupplier;
        }
        else
        {
            return null;
        }
    }
    
    public boolean updateSupplier(DTOSupplier dtoSupplier) {
        /**
         * Nazmul vai: 
         * Updating user in EntityUser not here, there are lots of user type will be added
         * whill you update user for every userTypeEntity??? if there is any changes in user table you have to change every userType entity table
         * 
         */
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if (dtoSupplier != null && dtoSupplier.getEntityUser() != null) {
                EntityUser user = dtoSupplier.getEntityUser();
                session.update(user);
                //update user role if required.
                session.update(dtoSupplier.getEntitySupplier());
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
    
    public DTOSupplier getSupplierInfo(EntitySupplier reqEntitySupplier) {
        /**
         * Nazmul vai:
         * 
         * Don't return any object other that Entity
         * in this class you can only return object like EntitySupplier
         */
        DTOSupplier dtoSupplier = null;
        Session session = HibernateUtil.getSession();
        try {
            EntitySupplier entitySupplier = new EntitySupplier();
            if(reqEntitySupplier.getId() > 0)
            {
                Query<EntitySupplier> query1 = session.getNamedQuery("getSupplierBySupplierId");
                query1.setParameter("supplierId", reqEntitySupplier.getId());
                entitySupplier = query1.getSingleResult();
            }
            else if(reqEntitySupplier.getUserId() > 0)
            {
                Query<EntitySupplier> query1 = session.getNamedQuery("getSupplierByUserId");
                query1.setParameter("userId", reqEntitySupplier.getUserId());
                entitySupplier = query1.getSingleResult();
            }
            if(entitySupplier.getId() > 0)
            {
                Query<EntityUser> query2 = session.getNamedQuery("getUserByUserId");
                query2.setParameter("userId", entitySupplier.getUserId());
                EntityUser entityUser = query2.getSingleResult();
                //set user role if required
                dtoSupplier = new DTOSupplier();
                dtoSupplier.setEntitySupplier(entitySupplier);
                dtoSupplier.setEntityUser(entityUser);
            }            
        } finally {
            session.close();
        }
        return dtoSupplier;
    }
    
    public List<DTOSupplier> getSuppliers(DTOSupplier dtoSupplier) {
        /**
         * Nazmul vai:
         * 
         * Don't return any List other that List<Entity>
         * in this class you can only return list like List<EntitySupplier>
         */
        List<DTOSupplier> suppliers = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try {
            //set limit, offset and other params in named query
            Query<EntitySupplier> query = session.getNamedQuery("getSuppliers");
            List<EntitySupplier> entitySuppliers = query.getResultList();
            for(EntitySupplier entitySupplier : entitySuppliers)
            {
                Query<EntityUser> queryUser = session.getNamedQuery("getUserByUserId");
                queryUser.setParameter("userId", entitySupplier.getUserId());
                EntityUser entityUser = queryUser.uniqueResult();
                DTOSupplier tempDTOSupplier = new DTOSupplier();
                tempDTOSupplier.setEntitySupplier(entitySupplier);
                tempDTOSupplier.setEntityUser(entityUser);
                //set user role if required
                suppliers.add(tempDTOSupplier);
            }
        } finally {
            session.close();
        }
        return suppliers;
    }
}
