package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerPurchaseOrder 
{
    private final Logger logger = LoggerFactory.getLogger(EntityManagerPurchaseOrder.class);
    
    public EntityPurchaseOrder createPurchaseOrder(EntityPurchaseOrder entityPurchaseOrder)
    {
        EntityPurchaseOrder resultEntityPurchaseOrder = null;
        Session session = HibernateUtil.getSession();
        try 
        {
            resultEntityPurchaseOrder = this.createPurchaseOrder(entityPurchaseOrder, session);
        } 
        finally 
        {
            session.close();
        }
        return resultEntityPurchaseOrder;
    }
    
    public EntityPurchaseOrder createPurchaseOrder(EntityPurchaseOrder entityPurchaseOrder, Session session)
    {
        try
        {
            session.save(entityPurchaseOrder);
            return entityPurchaseOrder;
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return null;
    }
    
    public EntityPurchaseOrder getPurchaseOrderById(int id)
    {
        EntityPurchaseOrder resultEntityPurchaseOrder = new EntityPurchaseOrder();
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getPurchaseOrderById");
            query.setParameter("id", id);
            resultEntityPurchaseOrder =  query.getSingleResult();                 
        } 
        finally 
        {
            session.close();
        }
        return resultEntityPurchaseOrder;
    }
    
    public EntityPurchaseOrder getPurchaseOrderByOrderNo(String orderNo)
    {
        EntityPurchaseOrder resultEntityPurchaseOrder = new EntityPurchaseOrder();
        Session session = HibernateUtil.getSession();
        try 
        {            
            if(orderNo != null && !orderNo.equals(""))
            {
                Query<EntityPurchaseOrder> query = session.getNamedQuery("getPurchaseOrderByOrderNo");
                query.setParameter("orderNo", orderNo);
                resultEntityPurchaseOrder = query.getSingleResult();
            }                     
        } 
        finally 
        {
            session.close();
        }
        return resultEntityPurchaseOrder;
    }
    
    public List<EntityPurchaseOrder> getPurchaseOrders(int offset, int limit)
    {
        List<EntityPurchaseOrder> purchaseOrders = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getAllPurchaseOrders");
            purchaseOrders =  query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return purchaseOrders;
    }
    
    public boolean updatePurchaseOrder(EntityPurchaseOrder entityPurchaseOrder)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            session.update(entityPurchaseOrder);
        } 
        finally 
        {
            session.close();
        }
        return true;
    }
}
