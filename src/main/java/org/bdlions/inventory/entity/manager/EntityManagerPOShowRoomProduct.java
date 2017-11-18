package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerPOShowRoomProduct 
{
    public EntityPOShowRoomProduct addPurchaseOrderShowRoomProduct(EntityPOShowRoomProduct entityPOShowRoomProduct, Session session)
    {
        session.save(entityPOShowRoomProduct);
        return entityPOShowRoomProduct;
    }
    
    public EntityPOShowRoomProduct addPurchaseOrderShowRoomProduct(EntityPOShowRoomProduct entityPOShowRoomProduct)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            return addPurchaseOrderShowRoomProduct(entityPOShowRoomProduct, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityPOShowRoomProduct> addPurchaseOrderShowRoomProducts(List<EntityPOShowRoomProduct> entityPOShowRoomProductList, Session session)
    {
        List<EntityPOShowRoomProduct> entityPOShowRoomProducts = new ArrayList<>();
        if(entityPOShowRoomProductList != null && !entityPOShowRoomProductList.isEmpty())
        {
            for(int counter = 0; counter < entityPOShowRoomProductList.size(); counter++)
            {
                EntityPOShowRoomProduct entityPOShowRoomProduct = entityPOShowRoomProductList.get(counter);
                entityPOShowRoomProduct = addPurchaseOrderShowRoomProduct(entityPOShowRoomProduct, session);
                entityPOShowRoomProducts.add(entityPOShowRoomProduct);
            }
        }
        return entityPOShowRoomProducts;
    }
    
    public int deletePOShowRoomProductsByOrderNo(String orderNo)
    {
        Session session = HibernateUtil.getSession();
        try 
        {            
            return deletePOShowRoomProductsByOrderNo(orderNo, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public int deletePOShowRoomProductsByOrderNo(String orderNo, Session session)
    {
        int responseCode = 0;
        if(!StringUtils.isNullOrEmpty(orderNo))
        {
            Query<EntityPOShowRoomProduct> queryShowRoomProducts = session.getNamedQuery("deletePOShowRoomProductsByOrderNo");
            queryShowRoomProducts.setParameter("orderNo", orderNo);
            responseCode =  queryShowRoomProducts.executeUpdate();
        }
        return responseCode;
    }
    
    public List<EntityPOShowRoomProduct> getPOShowRoomProductsByOrderNo(String orderNo)
    {
        if(StringUtils.isNullOrEmpty(orderNo))
        {
            return null;
        }
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityPOShowRoomProduct> query = session.getNamedQuery("getPOShowRoomProductsByOrderNo");
            query.setParameter("orderNo", orderNo);
            return query.getResultList();                       
        } 
        finally 
        {
            session.close();
        }
    }
}
