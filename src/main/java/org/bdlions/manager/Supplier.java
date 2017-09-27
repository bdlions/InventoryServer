package org.bdlions.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.DTOSupplier;
import org.bdlions.dto.EntitySupplier;
import org.bdlions.dto.EntityUser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class Supplier {
    public boolean createSupplier(DTOSupplier dtoSupplier) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        try {
            tx.begin();
            if (dtoSupplier != null && dtoSupplier.getEntityUser() != null) {
                EntityUser user = dtoSupplier.getEntityUser();
                session.save(user);
                dtoSupplier.getEntitySupplier().setUserId(user.getId());
                session.save(dtoSupplier.getEntitySupplier());
                tx.commit();
                return true;
            }
        }
        catch(Exception ex){
            tx.rollback();
        }
        finally {
            session.close();
        }
        return false;
    }
    
    public boolean updateSupplier(DTOSupplier dtoSupplier) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if (dtoSupplier != null && dtoSupplier.getEntityUser() != null) {
                EntityUser user = dtoSupplier.getEntityUser();
                session.update(user);
                session.update(dtoSupplier.getEntitySupplier());
                tx.commit();
                return true;
            }
        }
        catch(Exception ex){
            tx.rollback();
        }
        finally {
            session.close();
        }
        return false;
    }
    
    public DTOSupplier getSupplierInfo(int supplierId) {
        DTOSupplier dtoSupplier = null;
        Session session = HibernateUtil.getSession();
        try {
            Query<EntitySupplier> query1 = session.getNamedQuery("getSupplierBySupplierId");
            query1.setParameter("supplierId", supplierId);
            EntitySupplier entitySupplier = query1.getSingleResult();
            
            Query<EntityUser> query2 = session.getNamedQuery("getUserByUserId");
            query2.setParameter("userId", entitySupplier.getUserId());
            EntityUser entityUser = query2.getSingleResult();
            
            dtoSupplier = new DTOSupplier();
            dtoSupplier.setEntitySupplier(entitySupplier);
            dtoSupplier.setEntityUser(entityUser);
            
        } finally {
            session.close();
        }
        return dtoSupplier;
    }
    
    public List<DTOSupplier> getSuppliers(DTOSupplier dtoSupplier) {
        List<DTOSupplier> suppliers = new ArrayList<>();
        return suppliers;
    }
}
