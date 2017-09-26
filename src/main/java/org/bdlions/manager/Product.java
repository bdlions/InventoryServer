package org.bdlions.manager;

import java.util.List;
import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.EntityProduct;
import org.bdlions.dto.EntityProductCategory;
import org.bdlions.dto.EntityProductType;
import org.bdlions.dto.EntityUOM;
import org.bdlions.dto.EntityUser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul
 */
public class Product {
    public List<EntityProductCategory> getAllProductCategories(){
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityProductCategory> query = session.getNamedQuery("getAllProductCategories");
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    public List<EntityProductType> getAllProductTypes(){
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityProductType> query = session.getNamedQuery("getAllProductTypes");
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    public List<EntityUOM> getAllUOMs(){
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityUOM> query = session.getNamedQuery("getAllUOMs");
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    public boolean createProduct(EntityProduct product) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if(product != null)
            {
                session.save(product);
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
    
    public EntityProduct getProductInfo(int productId) {
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityProduct> query = session.getNamedQuery("getProductByProductId");
            query.setParameter("productId", productId);
            return query.getSingleResult();
        } finally {
            session.close();
        }
    }
    
    public boolean updateProduct(EntityProduct product) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if(product != null && product.getId() > 0)
            {
                session.update(product);
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
    
    public List<EntityProduct> getProducts() 
    {
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityProduct> query = session.getNamedQuery("getProducts");
            return query.getResultList();
        } finally {
            session.close();
        }
    }
}