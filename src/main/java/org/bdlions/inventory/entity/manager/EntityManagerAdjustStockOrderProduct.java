package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityAdjustStockOrderProduct;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerAdjustStockOrderProduct {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerAdjustStockOrderProduct.class);
    private int appId;
    public EntityManagerAdjustStockOrderProduct(int appId)
    {
        this.appId = appId;
    }
    
    public EntityAdjustStockOrderProduct addAdjustStockOrderProduct(EntityAdjustStockOrderProduct entityAdjustStockOrderProduct, Session session)
    {
        session.save(entityAdjustStockOrderProduct);
        return entityAdjustStockOrderProduct;
    }
    
    public EntityAdjustStockOrderProduct addAdjustStockOrderProduct(EntityAdjustStockOrderProduct entityAdjustStockOrderProduct)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return addAdjustStockOrderProduct(entityAdjustStockOrderProduct, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityAdjustStockOrderProduct> addAdjustStockOrderProducts(List<EntityAdjustStockOrderProduct> entityAdjustStockOrderProductList, Session session)
    {
        List<EntityAdjustStockOrderProduct> entityAdjustStockOrderProducts = new ArrayList<>();
        if(entityAdjustStockOrderProductList != null && !entityAdjustStockOrderProductList.isEmpty())
        {
            for(int counter = 0; counter < entityAdjustStockOrderProductList.size(); counter++)
            {
                EntityAdjustStockOrderProduct entityAdjustStockOrderProduct = entityAdjustStockOrderProductList.get(counter);
                entityAdjustStockOrderProduct = addAdjustStockOrderProduct(entityAdjustStockOrderProduct, session);
                entityAdjustStockOrderProducts.add(entityAdjustStockOrderProduct);
            }
        }
        return entityAdjustStockOrderProducts;
    }
    
    public int deleteAdjustStockOrderProductsByOrderNo(String orderNo, Session session)
    {
        if(!StringUtils.isNullOrEmpty(orderNo))
        {
            Query<EntityAdjustStockOrderProduct> query = session.getNamedQuery("deleteAdjustStockOrderProductsByOrderNo");
            query.setParameter("orderNo", orderNo);
            return query.executeUpdate();
        }
        return 0;
    }
    
    public int deleteAdjustStockOrderProductsByOrderNo(String orderNo)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            return deleteAdjustStockOrderProductsByOrderNo(orderNo, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityAdjustStockOrderProduct> getAdjustStockOrderProductsByOrderNo(String orderNo)
    {
        if(StringUtils.isNullOrEmpty(orderNo))
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityAdjustStockOrderProduct> query = session.getNamedQuery("getAdjustStockOrderProductsByOrderNo");
            query.setParameter("orderNo", orderNo);
            return query.getResultList();                       
        } 
        finally 
        {
            session.close();
        }
    }
}
