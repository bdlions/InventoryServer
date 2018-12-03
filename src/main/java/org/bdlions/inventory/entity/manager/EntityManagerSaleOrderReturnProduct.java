package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntitySaleOrderReturnProduct;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerSaleOrderReturnProduct 
{
    private int appId;
    public EntityManagerSaleOrderReturnProduct(int appId)
    {
        this.appId = appId;
    }
    
    /**
     * This method will add sale order return product using session
     * @param entitySaleOrderReturnProduct entity sale order return product
     * @param session session
     * @return EntitySaleOrderReturnProduct EntitySaleOrderReturnProduct
     */
    public EntitySaleOrderReturnProduct addSaleOrderReturnProduct(EntitySaleOrderReturnProduct entitySaleOrderReturnProduct, Session session)
    {
        session.save(entitySaleOrderReturnProduct);
        return entitySaleOrderReturnProduct;
    }
    
    /**
     * This method will add sale order return product
     * @param entitySaleOrderReturnProduct entity sale order return product
     * @return EntitySaleOrderReturnProduct EntitySaleOrderReturnProduct
     */
    public EntitySaleOrderReturnProduct addSaleOrderReturnProduct(EntitySaleOrderReturnProduct entitySaleOrderReturnProduct)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return addSaleOrderReturnProduct(entitySaleOrderReturnProduct, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will add sale order return products using session
     * @param entitySaleOrderReturnProductList entity sale order return product list
     * @param session session
     * @return List entity sale order return product list
     */
    public List<EntitySaleOrderReturnProduct> addSaleOrderReturnProducts(List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProductList, Session session)
    {
        List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProducts = new ArrayList<>();
        if(entitySaleOrderReturnProductList != null && !entitySaleOrderReturnProductList.isEmpty())
        {
            for(int counter = 0; counter < entitySaleOrderReturnProductList.size(); counter++)
            {
                EntitySaleOrderReturnProduct entitySaleOrderReturnProduct = entitySaleOrderReturnProductList.get(counter);
                entitySaleOrderReturnProduct = addSaleOrderReturnProduct(entitySaleOrderReturnProduct, session);
                entitySaleOrderReturnProducts.add(entitySaleOrderReturnProduct);
            }
        }
        return entitySaleOrderReturnProducts;
    }
    
    public EntitySaleOrderReturnProduct saveOrUpdateSaleOrderReturnProduct(EntitySaleOrderReturnProduct entitySaleOrderReturnProduct, Session session)
    {
        session.saveOrUpdate(entitySaleOrderReturnProduct);
        return entitySaleOrderReturnProduct;
    }
    
    public List<EntitySaleOrderReturnProduct> saveOrUpdateSaleOrderReturnProducts(List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProductList, Session session)
    {
        List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProducts = new ArrayList<>();
        if(entitySaleOrderReturnProductList != null && !entitySaleOrderReturnProductList.isEmpty())
        {
            for(int counter = 0; counter < entitySaleOrderReturnProductList.size(); counter++)
            {
                EntitySaleOrderReturnProduct entitySaleOrderReturnProduct = entitySaleOrderReturnProductList.get(counter);
                entitySaleOrderReturnProduct = saveOrUpdateSaleOrderReturnProduct(entitySaleOrderReturnProduct, session);
                entitySaleOrderReturnProducts.add(entitySaleOrderReturnProduct);
            }
        }
        return entitySaleOrderReturnProducts;
    }
    
    /**
     * This method will delete sale order return products using session
     * @param saleOrderNo sale order no
     * @param session session
     * @return int
     */
    public int deleteSaleOrderReturnProductsByOrderNo(String saleOrderNo, Session session)
    {
        if(!StringUtils.isNullOrEmpty(saleOrderNo))
        {
            Query<EntitySaleOrderReturnProduct> querySaleOrderReturnProducts = session.getNamedQuery("deleteSaleOrderReturnProductsByOrderNo");
            querySaleOrderReturnProducts.setParameter("saleOrderNo", saleOrderNo);
            return querySaleOrderReturnProducts.executeUpdate();
        }
        return 0;        
    }
    
    /**
     * This method will delete sale order return products
     * @param saleOrderNo sale order no
     * @return int
     */
    public int deleteSaleOrderReturnProductsByOrderNo(String saleOrderNo)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            return deleteSaleOrderReturnProductsByOrderNo(saleOrderNo, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return sale order return products by order no
     * @param saleOrderNo sale order no
     * @return List entity sale order product list
     */
    public List<EntitySaleOrderReturnProduct> getSaleOrderReturnProductsByOrderNo(String saleOrderNo)
    {
        if(StringUtils.isNullOrEmpty(saleOrderNo))
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntitySaleOrderReturnProduct> query = session.getNamedQuery("getSaleOrderReturnProductsByOrderNo");
            query.setParameter("saleOrderNo", saleOrderNo);
            return query.getResultList();                     
        } 
        finally 
        {
            session.close();
        }
    }
}
