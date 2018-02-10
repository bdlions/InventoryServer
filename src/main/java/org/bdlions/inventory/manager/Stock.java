package org.bdlions.inventory.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class Stock 
{
    private int appId;
    public Stock(int appId)
    {
        this.appId = appId;
    }
    /**
     * This method will return current stock of product
     * @param offset offset
     * @param limit limit
     * @return List product list with current stock
     */
    public List<DTOProduct> getCurrentStock(int offset, int limit)
    {
        List<DTOProduct> products = new ArrayList<>();
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try
        {
            Query<Object[]> queryStockProducts = session.getNamedQuery("getCurrentStock");
            queryStockProducts.setFirstResult(offset);
            queryStockProducts.setMaxResults(limit);
            List<Object[]> showRoomProducts = queryStockProducts.getResultList();
            for(Object[] entityShowRoomStock : showRoomProducts)
            {
                int productId = (int)entityShowRoomStock[0];
                double quantity = (double)entityShowRoomStock[1];
                
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(this.appId);
                EntityProduct entityProduct = entityManagerProduct.getProductByProductId(productId);
                DTOProduct dtoProduct = new DTOProduct();
                dtoProduct.setQuantity(quantity);
                dtoProduct.setEntityProduct(entityProduct);
                products.add(dtoProduct);                
            }
        }
        finally 
        {
            session.close();
        }
        return products;
    }
    
    /**
     * This method will return total number of products in stock
     * @return Integer total number of products
     */
    public int getTotalCurrentStock()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try
        {
            Query<Object[]> queryStockProducts = session.getNamedQuery("getCurrentStock");
            return queryStockProducts.getResultList().size();            
        }
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return current stock of product - search by product name case insensitive
     * @param productName product name
     * @param offset offset
     * @param limit limit
     * @return List product list with current stock
     */
    public List<DTOProduct> searchCurrentStockByProductName(String productName, int offset, int limit)
    {
        List<DTOProduct> products = new ArrayList<>();
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try
        {
            Query<Object[]> queryStockProducts = session.getNamedQuery("searchCurrentStockByProductName");
            queryStockProducts.setParameter("productName", "%" + productName.toLowerCase() + "%");
            queryStockProducts.setFirstResult(offset);
            queryStockProducts.setMaxResults(limit);
            List<Object[]> showRoomProducts = queryStockProducts.getResultList();
            for(Object[] entityShowRoomStock : showRoomProducts)
            {
                int productId = (int)entityShowRoomStock[0];
                double quantity = (double)entityShowRoomStock[1];
                
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(this.appId);
                EntityProduct entityProduct = entityManagerProduct.getProductByProductId(productId);
                DTOProduct dtoProduct = new DTOProduct();
                dtoProduct.setQuantity(quantity);
                dtoProduct.setEntityProduct(entityProduct);
                products.add(dtoProduct);                
            }
        }
        finally 
        {
            session.close();
        }
        return products;
    }
    
    /**
     * This method will return total number of product in current stock - search by product name case insensitive
     * @param productName product name
     * @return Integer total number of product in current stock
     */
    public int searchTotalCurrentStockByProductName(String productName)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try
        {
            Query<Object[]> queryStockProducts = session.getNamedQuery("searchCurrentStockByProductName");
            queryStockProducts.setParameter("productName", "%" + productName.toLowerCase() + "%");
            return queryStockProducts.getResultList().size();
        }
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return current stock of product - based on product id list
     * @param productIds product id list
     * @return List product list with current stock
     */
    public List<DTOProduct> getCurrentStockByProductIds(List<Integer> productIds)
    {
        List<DTOProduct> products = new ArrayList<>();
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try
        {
            Query<Object[]> queryStockProducts = session.getNamedQuery("getCurrentStockByProductIds");
            queryStockProducts.setParameter("productIds", productIds);
            List<Object[]> showRoomProducts = queryStockProducts.getResultList();
            HashMap<Integer, EntityProduct> productIdEntityProductMap = new HashMap<>();
            List<Integer> reqProductIds = new ArrayList<>();
            for(Object[] entityShowRoomStock : showRoomProducts)
            {
                int productId = (int)entityShowRoomStock[0];
                reqProductIds.add(productId);
            }
            if(!reqProductIds.isEmpty())
            {
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(this.appId);
                List<EntityProduct> productList = entityManagerProduct.getProductsByProductIds(reqProductIds);
                for(EntityProduct entityProduct : productList)
                {
                    productIdEntityProductMap.put(entityProduct.getId(), entityProduct);
                }
            }
            for(Object[] entityShowRoomStock : showRoomProducts)
            {
                int productId = (int)entityShowRoomStock[0];
                double quantity = (double)entityShowRoomStock[1];
                if(productIdEntityProductMap.containsKey(productId))
                {
                    EntityProduct entityProduct = productIdEntityProductMap.get(productId);
                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setQuantity(quantity);
                    dtoProduct.setEntityProduct(entityProduct);
                    products.add(dtoProduct);
                }
            }
        }
        finally 
        {
            session.close();
        }
        return products;
    }
}
