package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerPOShowRoomProduct {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerPOShowRoomProduct.class);
    
    public EntityPOShowRoomProduct addPurchaseOrderShowRoomProduct(EntityPOShowRoomProduct entityPOShowRoomProduct)
    {
        EntityPOShowRoomProduct resultEntityPOShowRoomProduct = null;
        Session session = HibernateUtil.getSession();
        try 
        {
            resultEntityPOShowRoomProduct = this.addPurchaseOrderShowRoomProduct(entityPOShowRoomProduct, session);
        } 
        finally 
        {
            session.close();
        }
        return resultEntityPOShowRoomProduct;
    }
    
    public EntityPOShowRoomProduct addPurchaseOrderShowRoomProduct(EntityPOShowRoomProduct entityPOShowRoomProduct, Session session)
    {
        try
        {
            session.save(entityPOShowRoomProduct);
            return entityPOShowRoomProduct;
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return null;
    }
    
    public List<EntityPOShowRoomProduct> getPurchaseOrderProductsByOrderNo(String orderNo)
    {
        List<EntityPOShowRoomProduct> purchaseOrderProducts =  new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {            
            if(orderNo != null && !orderNo.equals(""))
            {
                Query<EntityPOShowRoomProduct> queryShowRoomProducts = session.getNamedQuery("getPurchaseOrderProductsByOrderNo");
                queryShowRoomProducts.setParameter("orderNo", orderNo);
                purchaseOrderProducts =  queryShowRoomProducts.getResultList();
            }                     
        } 
        finally 
        {
            session.close();
        }
        return purchaseOrderProducts;
    }
    
    public int deletePurchaseOrderProductsByOrderNo(String orderNo)
    {
        int responseCode = 0;
        Session session = HibernateUtil.getSession();
        try 
        {            
            responseCode = deletePurchaseOrderProductsByOrderNo(orderNo, session);
        } 
        finally 
        {
            session.close();
        }
        return responseCode;
    }
    
    public int deletePurchaseOrderProductsByOrderNo(String orderNo, Session session)
    {
        int responseCode = 0;
        try 
        {            
            if(orderNo != null && !orderNo.equals(""))
            {
                Query<EntityPOShowRoomProduct> queryShowRoomProducts = session.getNamedQuery("deletePurchaseOrderProductsByOrderNo");
                queryShowRoomProducts.setParameter("orderNo", orderNo);
                responseCode =  queryShowRoomProducts.executeUpdate();
            }                     
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return responseCode;
    }
}
