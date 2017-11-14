package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.entity.EntitySaleOrderProduct;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerSaleOrderProduct {
    public EntitySaleOrderProduct addSaleOrderProduct(EntitySaleOrderProduct entitySaleOrderProduct)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            return addSaleOrderProduct(entitySaleOrderProduct, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntitySaleOrderProduct addSaleOrderProduct(EntitySaleOrderProduct entitySaleOrderProduct, Session session)
    {
        session.save(entitySaleOrderProduct);
        return entitySaleOrderProduct;
    }
    
    public List<EntitySaleOrderProduct> getSaleOrderProductsByOrderNo(String saleOrderNo)
    {
        if(saleOrderNo == null || saleOrderNo.equals(""))
        {
            return null;
        }
        Session session = HibernateUtil.getSession();
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
    
    public int deleteSaleOrderProductsByOrderNo(String saleOrderNo, Session session)
    {
        Query<EntitySaleOrderProduct> querySaleOrderProducts = session.getNamedQuery("deleteSaleOrderProductsByOrderNo");
        querySaleOrderProducts.setParameter("saleOrderNo", saleOrderNo);
        return querySaleOrderProducts.executeUpdate();        
    }
}
