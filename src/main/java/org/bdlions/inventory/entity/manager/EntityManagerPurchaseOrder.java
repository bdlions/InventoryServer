package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.util.Constants;
import org.bdlions.inventory.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerPurchaseOrder 
{
    public EntityPurchaseOrder createPurchaseOrder(EntityPurchaseOrder entityPurchaseOrder, Session session)
    {
        session.save(entityPurchaseOrder);
        return entityPurchaseOrder;
    }
    
    public EntityPurchaseOrder createPurchaseOrder(EntityPurchaseOrder entityPurchaseOrder)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            return createPurchaseOrder(entityPurchaseOrder, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityPurchaseOrder createPurchaseOrder(EntityPurchaseOrder entityPurchaseOrder, List<EntityPOShowRoomProduct> entityPOShowRoomProductList, List<EntityShowRoomStock> entityShowRoomStockList)
    {
        boolean status = true;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try 
        {
            entityPurchaseOrder = createPurchaseOrder(entityPurchaseOrder, session);
            if(entityPurchaseOrder != null && !StringUtils.isNullOrEmpty(entityPurchaseOrder.getOrderNo()))
            {
                if(entityPOShowRoomProductList != null && !entityPOShowRoomProductList.isEmpty())
                {
                    List<EntityPOShowRoomProduct> entityPOShowRoomProducts = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entityPOShowRoomProductList.size(); counter++)
                    {
                        EntityPOShowRoomProduct entityPOShowRoomProduct = entityPOShowRoomProductList.get(counter);
                        entityPOShowRoomProduct.setOrderNo(entityPurchaseOrder.getOrderNo());
                        entityPOShowRoomProducts.add(entityPOShowRoomProduct);
                    }
                    EntityManagerPOShowRoomProduct entityManagerPOShowRoomProduct = new EntityManagerPOShowRoomProduct();
                    List<EntityPOShowRoomProduct> resultEntityPOShowRoomProducts = entityManagerPOShowRoomProduct.addPurchaseOrderShowRoomProducts(entityPOShowRoomProducts, session);
                    if(resultEntityPOShowRoomProducts == null || resultEntityPOShowRoomProducts.isEmpty())
                    {
                        status = false;
                    }
                }
                if(entityShowRoomStockList != null && !entityShowRoomStockList.isEmpty())
                {
                    List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entityShowRoomStockList.size(); counter++)
                    {
                        EntityShowRoomStock entityShowRoomStock = entityShowRoomStockList.get(counter);
                        entityShowRoomStock.setPurchaseOrderNo(entityPurchaseOrder.getOrderNo());
                        entityShowRoomStocks.add(entityShowRoomStock);
                    }
                    EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                    List<EntityShowRoomStock> resultEntityShowRoomStocks = entityManagerShowRoomStock.addShowRoomStocks(entityShowRoomStocks, session);                    
                    if(resultEntityShowRoomStocks == null || resultEntityShowRoomStocks.isEmpty())
                    {
                        status = false;
                    }
                }
                if(status)
                {
                    tx.commit();
                    return entityPurchaseOrder;
                }
                else
                {
                    tx.rollback();
                    return null;
                }
            }
            else
            {
                tx.rollback();
                return null;
            }
        } 
        finally 
        {
            session.close();
        }
    }
    
    public boolean updatePurchaseOrder(EntityPurchaseOrder entityPurchaseOrder, Session session)
    {
        session.update(entityPurchaseOrder);
        return true;
    }
    
    public boolean updatePurchaseOrder(EntityPurchaseOrder entityPurchaseOrder)
    {
        Session session = HibernateUtil.getSession();
        try 
        {
            return updatePurchaseOrder(entityPurchaseOrder, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public boolean updatePurchaseOrder(EntityPurchaseOrder entityPurchaseOrder, List<EntityPOShowRoomProduct> entityPOShowRoomProductList, List<EntityShowRoomStock> entityShowRoomStockList)
    {
        boolean status = true;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try 
        {
            if(entityPurchaseOrder != null && entityPurchaseOrder.getId() > 0 && updatePurchaseOrder(entityPurchaseOrder, session))
            {
                EntityManagerPOShowRoomProduct entityManagerPOShowRoomProduct = new EntityManagerPOShowRoomProduct();
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                //deleting existing products
                if(!StringUtils.isNullOrEmpty(entityPurchaseOrder.getOrderNo()))
                {
                    entityManagerPOShowRoomProduct.deletePOShowRoomProductsByOrderNo(entityPurchaseOrder.getOrderNo(), session);
                    entityManagerShowRoomStock.deleteShowRoomProductsByPurchaseOrderNoAndTransactionCategoryId(entityPurchaseOrder.getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN, session);
                }
                if(entityPOShowRoomProductList != null && !entityPOShowRoomProductList.isEmpty())
                {
                    List<EntityPOShowRoomProduct> entityPOShowRoomProducts = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entityPOShowRoomProductList.size(); counter++)
                    {
                        EntityPOShowRoomProduct entityPOShowRoomProduct = entityPOShowRoomProductList.get(counter);
                        entityPOShowRoomProduct.setOrderNo(entityPurchaseOrder.getOrderNo());
                        entityPOShowRoomProducts.add(entityPOShowRoomProduct);
                    }
                    List<EntityPOShowRoomProduct> resultEntityPOShowRoomProducts = entityManagerPOShowRoomProduct.addPurchaseOrderShowRoomProducts(entityPOShowRoomProducts, session);
                    if(resultEntityPOShowRoomProducts == null || resultEntityPOShowRoomProducts.isEmpty())
                    {
                        status = false;
                    }
                }
                if(entityShowRoomStockList != null && !entityShowRoomStockList.isEmpty())
                {
                    List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entityShowRoomStockList.size(); counter++)
                    {
                        EntityShowRoomStock entityShowRoomStock = entityShowRoomStockList.get(counter);
                        entityShowRoomStock.setPurchaseOrderNo(entityPurchaseOrder.getOrderNo());
                        entityShowRoomStocks.add(entityShowRoomStock);
                    }
                    List<EntityShowRoomStock> resultEntityShowRoomStocks = entityManagerShowRoomStock.addShowRoomStocks(entityShowRoomStocks, session);                    
                    if(resultEntityShowRoomStocks == null || resultEntityShowRoomStocks.isEmpty())
                    {
                        status = false;
                    }
                }
                if(status)
                {
                    tx.commit();
                    return true;
                }
                else
                {
                    tx.rollback();
                    return false;
                }
            }
            else
            {
                tx.rollback();
                return false;
            }
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityPurchaseOrder getPurchaseOrderByOrderNo(String orderNo)
    {
        if(StringUtils.isNullOrEmpty(orderNo))
        {
            return null;
        }
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getPurchaseOrderByOrderNo");
            query.setParameter("orderNo", orderNo);
            List<EntityPurchaseOrder> purchaseOrderList = query.getResultList();
            if(purchaseOrderList == null || purchaseOrderList.isEmpty())
            {
                return null;
            }
            else
            {
                return purchaseOrderList.get(0);
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityPurchaseOrder getPurchaseOrderById(int id)
    {
        if(id <= 0)
        {
            return null;
        }
        Session session = HibernateUtil.getSession();
        try 
        {            
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getPurchaseOrderById");
            query.setParameter("id", id);
            List<EntityPurchaseOrder> purchaseOrderList = query.getResultList();
            if(purchaseOrderList == null || purchaseOrderList.isEmpty())
            {
                return null;
            }
            else
            {
                return purchaseOrderList.get(0);
            }                
        } 
        finally 
        {
            session.close();
        }
    }    
    
    public List<EntityPurchaseOrder> getPurchaseOrders(int offset, int limit)
    {
        List<EntityPurchaseOrder> purchaseOrders = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getAllPurchaseOrders");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            purchaseOrders =  query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return purchaseOrders;
    }
    
}
