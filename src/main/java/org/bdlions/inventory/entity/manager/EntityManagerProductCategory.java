package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityProductCategory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerProductCategory 
{
    private int appId;
    public EntityManagerProductCategory(int appId)
    {
        this.appId = appId;
    }
    
    public EntityProductCategory getProductCategoryById(int id)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try {
            
            Query<EntityProductCategory> query = session.getNamedQuery("getProductCategoryById");
            query.setParameter("id", id);            
            //getSingleResult will throw NoResultException if query has no result
            //return query.getSingleResult();
            List<EntityProductCategory> categoryList = query.getResultList();
            if(categoryList == null || categoryList.isEmpty())
            {
                return null;
            }
            else
            {
                return categoryList.get(0);
            }           
        }
        finally 
        {
            session.close();
        }
    }
    
    public EntityProductCategory getProductCategoryByTitle(String title)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try {
            
            Query<EntityProductCategory> query = session.getNamedQuery("getProductCategoryByTitle");
            query.setParameter("title", title);            
            List<EntityProductCategory> categoryList = query.getResultList();
            if(categoryList == null || categoryList.isEmpty())
            {
                return null;
            }
            else
            {
                return categoryList.get(0);
            }           
        }
        finally 
        {
            session.close();
        }
    }
    
    public EntityProductCategory createProductCategory(EntityProductCategory entityProductCategory) 
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            session.save(entityProductCategory);
            return entityProductCategory;
        }
        finally 
        {
            session.close();
        }
    }
    
    public boolean updateProductCategory(EntityProductCategory entityProductCategory) 
    {
        if(entityProductCategory == null || entityProductCategory.getId() <= 0)
        {
            return false;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try 
        {
            session.update(entityProductCategory);
            EntityManagerProduct entityManagerProduct = new EntityManagerProduct(appId);
            entityManagerProduct.updateProductCategoryInfo(entityProductCategory, session);
            tx.commit();
            return true;
        }
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return all product categories
     * @return List, product category list
     */
    public List<EntityProductCategory> getAllProductCategories()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProductCategory> query = session.getNamedQuery("getAllProductCategories");
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityProductCategory> searchProductCategoryByTitle(String title) 
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityProductCategory> query = session.getNamedQuery("searchProductCategoryByTitle");
            query.setParameter("title", "%" + title.toLowerCase() + "%");
            return query.getResultList();
        } 
        finally 
        {
            session.close();
        }
    }
}
