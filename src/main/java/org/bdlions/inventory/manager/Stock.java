package org.bdlions.inventory.manager;

import java.util.ArrayList;
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
    /**
     * This method will return current stock of product
     * @param offset offset
     * @param limit limit
     * @return List product list with current stock
     */
    public List<DTOProduct> getCurrentStock(int offset, int limit)
    {
        List<DTOProduct> products = new ArrayList<>();
        Session session = HibernateUtil.getSession();
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
                
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
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
        Session session = HibernateUtil.getSession();
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
}
