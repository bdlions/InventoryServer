package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityProduct;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerProduct 
{
    /**
     * This method will return product info by name
     * @param name, product name
     * @return EntityProduct, product info
     */
    public EntityProduct getProductByName(String name)
    {
        Session session = HibernateUtil.getSession();
        try {
            
            Query<EntityProduct> query = session.getNamedQuery("getProductByName");
            query.setParameter("name", name);            
            //getSingleResult will throw NoResultException if query has no result
            //return query.getSingleResult();
            List<EntityProduct> productList = query.getResultList();
            if(productList == null || productList.isEmpty())
            {
                return null;
            }
            else
            {
                return productList.get(0);
            }           
        }
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return product info by product id
     * @param productId, product id
     * @return EntityProduct, product info
     */
    public EntityProduct getProductByProductId(int productId) 
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityProduct> query = session.getNamedQuery("getProductByProductId");
            query.setParameter("productId", productId);
            List<EntityProduct> productList = query.getResultList();
            if(productList == null || productList.isEmpty())
            {
                return null;
            }
            else
            {
                return productList.get(0);
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will create a new product
     * @param entityProduct, product info
     * @return EntityProduct, product info assigning id
     */
    public EntityProduct createProduct(EntityProduct entityProduct) 
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            session.save(entityProduct);
            return entityProduct;
        }
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will update product info
     * @param entityProduct, product info
     * @return boolean whether product info is updated or not
     */
    public boolean updateProduct(EntityProduct entityProduct) 
    {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try 
        {
            session.update(entityProduct);
            tx.commit();
            return true;
        }
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return product list
     * @param offset, offset
     * @param limit, limit
     * @return List, product info list
     */
    public List<EntityProduct> getProducts(int offset, int limit) 
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityProduct> query = session.getNamedQuery("getProducts");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
}
