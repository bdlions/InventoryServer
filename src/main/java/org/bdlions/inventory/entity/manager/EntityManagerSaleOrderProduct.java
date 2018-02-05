package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntitySaleOrderProduct;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerSaleOrderProduct 
{
    /**
     * This method will add sale order product using session
     * @param entitySaleOrderProduct entity sale order product
     * @param session session
     * @return EntitySaleOrderProduct EntitySaleOrderProduct
     */
    public EntitySaleOrderProduct addSaleOrderProduct(EntitySaleOrderProduct entitySaleOrderProduct, Session session)
    {
        session.save(entitySaleOrderProduct);
        return entitySaleOrderProduct;
    }
    
    /**
     * This method will add sale order product
     * @param entitySaleOrderProduct entity sale order product
     * @return EntitySaleOrderProduct EntitySaleOrderProduct
     */
    public EntitySaleOrderProduct addSaleOrderProduct(EntitySaleOrderProduct entitySaleOrderProduct)
    {
        Session session = HibernateUtil.getInstance().getSession();
        try 
        {
            return addSaleOrderProduct(entitySaleOrderProduct, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will add sale order products using session
     * @param entitySaleOrderProductList entity sale order product list
     * @param session session
     * @return List entity sale order product list
     */
    public List<EntitySaleOrderProduct> addSaleOrderProducts(List<EntitySaleOrderProduct> entitySaleOrderProductList, Session session)
    {
        List<EntitySaleOrderProduct> entitySaleOrderProducts = new ArrayList<>();
        if(entitySaleOrderProductList != null && !entitySaleOrderProductList.isEmpty())
        {
            for(int counter = 0; counter < entitySaleOrderProductList.size(); counter++)
            {
                EntitySaleOrderProduct entitySaleOrderProduct = entitySaleOrderProductList.get(counter);
                entitySaleOrderProduct = addSaleOrderProduct(entitySaleOrderProduct, session);
                entitySaleOrderProducts.add(entitySaleOrderProduct);
            }
        }
        return entitySaleOrderProducts;
    }
    
    /**
     * This method will delete sale order products using session
     * @param saleOrderNo sale order no
     * @param session session
     * @return int
     */
    public int deleteSaleOrderProductsByOrderNo(String saleOrderNo, Session session)
    {
        if(!StringUtils.isNullOrEmpty(saleOrderNo))
        {
            Query<EntitySaleOrderProduct> querySaleOrderProducts = session.getNamedQuery("deleteSaleOrderProductsByOrderNo");
            querySaleOrderProducts.setParameter("saleOrderNo", saleOrderNo);
            return querySaleOrderProducts.executeUpdate();
        }
        return 0;        
    }
    
    /**
     * This method will delete sale order products
     * @param saleOrderNo sale order no
     * @return int
     */
    public int deleteSaleOrderProductsByOrderNo(String saleOrderNo)
    {
        Session session = HibernateUtil.getInstance().getSession();
        try 
        {            
            return deleteSaleOrderProductsByOrderNo(saleOrderNo, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return sale order products by order no
     * @param saleOrderNo sale order no
     * @return List entity sale order product list
     */
    public List<EntitySaleOrderProduct> getSaleOrderProductsByOrderNo(String saleOrderNo)
    {
        if(StringUtils.isNullOrEmpty(saleOrderNo))
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession();
        try 
        {            
            Query<EntitySaleOrderProduct> queryShowRoomProducts = session.getNamedQuery("getSaleOrderProductsByOrderNo");
            queryShowRoomProducts.setParameter("saleOrderNo", saleOrderNo);
            return queryShowRoomProducts.getResultList();                     
        } 
        finally 
        {
            session.close();
        }
    }
}
