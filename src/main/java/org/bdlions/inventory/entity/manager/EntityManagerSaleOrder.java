package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerSaleOrder 
{
    private final Logger logger = LoggerFactory.getLogger(EntityManagerSaleOrder.class);
    
    public EntitySaleOrder createSaleOrder(EntitySaleOrder entitySaleOrder)
    {
        EntitySaleOrder resultEntitySaleOrder = null;
        Session session = HibernateUtil.getSession();
        try 
        {
            resultEntitySaleOrder = createSaleOrder(entitySaleOrder, session);
        } 
        finally 
        {
            session.close();
        }
        return resultEntitySaleOrder;
    }
    
    public EntitySaleOrder createSaleOrder(EntitySaleOrder entitySaleOrder, Session session)
    {
        try
        {
            session.save(entitySaleOrder);
            return entitySaleOrder;
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return null;
    }
    
    public EntitySaleOrder getSaleOrderByOrderNo(String orderNo)
    {
        if(orderNo == null || orderNo.equals(""))
        {
            return null;
        }
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntitySaleOrder> query = session.getNamedQuery("getSaleOrderByOrderNo");
            query.setParameter("orderNo", orderNo);
            return query.getSingleResult();                    
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntitySaleOrder getEntitySaleOrderById(int id)
    {
        if(id <= 0)
        {
            return null;
        }
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntitySaleOrder> query = session.getNamedQuery("getSaleOrderById");
            query.setParameter("id", id);
            return query.getSingleResult();                 
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntitySaleOrder getEntitySaleOrderByOrderNo(String orderNo) 
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntitySaleOrder> query = session.getNamedQuery("getSaleOrderByOrderNo");
            query.setParameter("orderNo", orderNo);
            return query.getSingleResult();

        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntitySaleOrder> getSaleOrders(int offset, int limit)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntitySaleOrder> query = session.getNamedQuery("getAllSaleOrders");
            return query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    public boolean updateSaleOrder(EntitySaleOrder entitySaleOrder)
    {
        if(entitySaleOrder == null || entitySaleOrder.getId() <= 0)
        {
            return false;
        }
        Session session = HibernateUtil.getSession();
        try 
        {
            session.update(entitySaleOrder);
        } 
        finally 
        {
            session.close();
        }
        return true;
    }
    
}
