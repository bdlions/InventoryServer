package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerProduct {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerProduct.class);
    
    public EntityManagerProduct()
    {
    
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
    
    public EntityProduct createProduct(EntityProduct entityProduct) 
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if(entityProduct != null)
            {
                session.save(entityProduct);
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
            return entityProduct;
        }
        else
        {
            return null;
        }
    }
    
    public EntityProduct getProductInfo(int productId) 
    {
        EntityProduct resultEntityProduct = null;
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityProduct> query = session.getNamedQuery("getProductByProductId");
            query.setParameter("productId", productId);
            resultEntityProduct = query.getSingleResult();
        } 
        finally 
        {
            session.close();
        }
        return resultEntityProduct;
    }
    
    public boolean updateProduct(EntityProduct entityProduct) {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            if(entityProduct != null && entityProduct.getId() > 0)
            {
                session.update(entityProduct);
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
        List<EntityProduct> listEntityProduct = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try {
            //set limit, offset and other params in named query
            Query<EntityProduct> query = session.getNamedQuery("getProducts");
            listEntityProduct = query.getResultList();
        } 
        finally 
        {
            session.close();
        }
        return listEntityProduct;
    }
}
