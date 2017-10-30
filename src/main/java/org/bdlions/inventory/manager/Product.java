package org.bdlions.inventory.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityProductCategory;
import org.bdlions.inventory.entity.EntityProductType;
import org.bdlions.inventory.entity.EntityUOM;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul
 */
public class Product {
    private final Logger logger = LoggerFactory.getLogger(Product.class);
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
    
    public EntityProduct getEntityProductByName(EntityProduct entityProduct)
    {
        EntityProduct resultEntityProduct = null;
        Session session = HibernateUtil.getSession();
        try {
            
            Query<EntityProduct> query = session.getNamedQuery("getProductByName");
            query.setParameter("name", entityProduct.getName());
            resultEntityProduct =  query.getSingleResult();
            
        }
        catch(Exception ex)
        {
        
        }
        finally {
            session.close();
        }
        return resultEntityProduct;
    }
    
    public EntityProduct createProduct(EntityProduct product) {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if(product != null)
            {
                session.save(product);
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
            return product;
        }
        else
        {
            return null;
        }
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
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if(product != null && product.getId() > 0)
            {
                session.update(product);
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
        return status;
    }
    
    public List<EntityProduct> getProducts(DTOProduct dtoProduct) 
    {
        Session session = HibernateUtil.getSession();
        try {
            //set limit, offset and other params in named query
            Query<EntityProduct> query = session.getNamedQuery("getProducts");
            return query.getResultList();
        } finally {
            session.close();
        }
    }
}
