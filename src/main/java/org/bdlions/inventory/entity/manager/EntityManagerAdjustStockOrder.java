package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityAdjustStockOrder;
import org.bdlions.inventory.entity.EntityAdjustStockOrderProduct;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.entity.EntityPOShowRoomReturnProduct;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntityPurchaseOrderPayment;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderPayment;
import org.bdlions.inventory.entity.EntitySaleOrderProduct;
import org.bdlions.inventory.entity.EntitySaleOrderReturnProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.util.Constants;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerAdjustStockOrder {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerAdjustStockOrder.class);
    private int appId;
    public EntityManagerAdjustStockOrder(int appId)
    {
        this.appId = appId;
    }
    
    public EntityAdjustStockOrder createAdjustStockOrder(EntityAdjustStockOrder entityAdjustStockOrder, Session session)
    {
        entityAdjustStockOrder.setCreatedOn(TimeUtils.getCurrentTime());
        entityAdjustStockOrder.setModifiedOn(TimeUtils.getCurrentTime());
        session.save(entityAdjustStockOrder);
        return entityAdjustStockOrder;
    }
    
    public EntityAdjustStockOrder createAdjustStockOrder(EntityAdjustStockOrder entityAdjustStockOrder)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return createAdjustStockOrder(entityAdjustStockOrder, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityAdjustStockOrder createAdjustStockOrder(EntityAdjustStockOrder entityAdjustStockOrder, List<EntityAdjustStockOrderProduct> entityAdjustStockOrderProductList, List<EntityShowRoomStock> entityShowRoomStockList)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        long currentUnixTime = TimeUtils.getCurrentTime();
        try 
        {
            entityAdjustStockOrder = createAdjustStockOrder(entityAdjustStockOrder, session);
            if(entityAdjustStockOrder != null && !StringUtils.isNullOrEmpty(entityAdjustStockOrder.getOrderNo()))
            {
                if(entityAdjustStockOrderProductList != null && !entityAdjustStockOrderProductList.isEmpty())
                {
                    List<EntityAdjustStockOrderProduct> entityAdjustStockOrderProducts = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entityAdjustStockOrderProductList.size(); counter++)
                    {
                        EntityAdjustStockOrderProduct entityAdjustStockOrderProduct = entityAdjustStockOrderProductList.get(counter);
                        entityAdjustStockOrderProduct.setOrderNo(entityAdjustStockOrder.getOrderNo());
                        entityAdjustStockOrderProducts.add(entityAdjustStockOrderProduct);
                    }
                    EntityManagerAdjustStockOrderProduct entityManagerAdjustStockOrderProduct = new EntityManagerAdjustStockOrderProduct(this.appId);
                    List<EntityAdjustStockOrderProduct> resultEntityAdjustStockOrderProducts = entityManagerAdjustStockOrderProduct.addAdjustStockOrderProducts(entityAdjustStockOrderProducts, session);
                    if(resultEntityAdjustStockOrderProducts == null || resultEntityAdjustStockOrderProducts.isEmpty())
                    {
                        tx.rollback();
                        return null;
                    }                    
                }
                
                if(entityShowRoomStockList != null && !entityShowRoomStockList.isEmpty())
                {
                    List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entityShowRoomStockList.size(); counter++)
                    {
                        EntityShowRoomStock entityShowRoomStock = entityShowRoomStockList.get(counter);
                        //entityShowRoomStock.setPurchaseOrderNo(entityPurchaseOrder.getOrderNo());
                        entityShowRoomStock.setOrderNo(entityAdjustStockOrder.getOrderNo());
                        entityShowRoomStock.setCreatedOn(currentUnixTime);
                        entityShowRoomStock.setModifiedOn(currentUnixTime);
                        entityShowRoomStocks.add(entityShowRoomStock);
                    }
                    EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(this.appId);
                    List<EntityShowRoomStock> resultEntityShowRoomStocks = entityManagerShowRoomStock.addShowRoomStocks(entityShowRoomStocks, session);                    
                    if(resultEntityShowRoomStocks == null || resultEntityShowRoomStocks.isEmpty())
                    {
                        tx.rollback();
                        return null;
                    }
                }                                    
                tx.commit();
                return entityAdjustStockOrder;
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
    
    public boolean updateAdjustStockOrder(EntityAdjustStockOrder entityAdjustStockOrder, Session session)
    {
        entityAdjustStockOrder.setModifiedOn(TimeUtils.getCurrentTime());
        session.update(entityAdjustStockOrder);
        return true;
    }
    
    public boolean updateAdjustStockOrder(EntityAdjustStockOrder entityAdjustStockOrder)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return updateAdjustStockOrder(entityAdjustStockOrder, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public boolean updateAdjustStockOrder(EntityAdjustStockOrder currentEntityAdjustStockOrder, EntityAdjustStockOrder entityAdjustStockOrder, List<EntityAdjustStockOrderProduct> entityAdjustStockOrderProductList, List<EntityShowRoomStock> entityShowRoomStockList)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        long currentUnixTime = TimeUtils.getCurrentTime();
        try 
        {
            if(entityAdjustStockOrder != null && entityAdjustStockOrder.getId() > 0 && updateAdjustStockOrder(entityAdjustStockOrder, session))
            {
                EntityManagerAdjustStockOrderProduct entityManagerAdjustStockOrderProduct = new EntityManagerAdjustStockOrderProduct(this.appId);
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(this.appId);
                //deleting existing products
                if(!StringUtils.isNullOrEmpty(currentEntityAdjustStockOrder.getOrderNo()))
                {
                    entityManagerAdjustStockOrderProduct.deleteAdjustStockOrderProductsByOrderNo(currentEntityAdjustStockOrder.getOrderNo(), session);
                    List<Integer> transactionCategoryIds = new ArrayList<>();
                    transactionCategoryIds.add(Constants.SS_TRANSACTION_CATEGORY_ID_STOCK_ADJUSTMENT);
                    entityManagerShowRoomStock.deleteShowRoomProductsBySaleOrderNoAndTransactionCategoryIds(currentEntityAdjustStockOrder.getOrderNo(), transactionCategoryIds, session);
                }
                if(entityAdjustStockOrderProductList != null && !entityAdjustStockOrderProductList.isEmpty())
                {
                    List<EntityAdjustStockOrderProduct> entityAdjustStockOrderProducts = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entityAdjustStockOrderProductList.size(); counter++)
                    {
                        EntityAdjustStockOrderProduct entityAdjustStockOrderProduct = entityAdjustStockOrderProductList.get(counter);
                        entityAdjustStockOrderProduct.setOrderNo(entityAdjustStockOrder.getOrderNo());
                        entityAdjustStockOrderProducts.add(entityAdjustStockOrderProduct);
                    }
                    List<EntityAdjustStockOrderProduct> resultEntityAdjustStockOrderProducts = entityManagerAdjustStockOrderProduct.addAdjustStockOrderProducts(entityAdjustStockOrderProducts, session);
                    if(resultEntityAdjustStockOrderProducts == null || resultEntityAdjustStockOrderProducts.isEmpty())
                    {
                        tx.rollback();
                        return false;
                    }
                }
                if(entityShowRoomStockList != null && !entityShowRoomStockList.isEmpty())
                {
                    List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entityShowRoomStockList.size(); counter++)
                    {
                        EntityShowRoomStock entityShowRoomStock = entityShowRoomStockList.get(counter);
                        //entityShowRoomStock.setSaleOrderNo(entitySaleOrder.getOrderNo());
                        entityShowRoomStock.setOrderNo(entityAdjustStockOrder.getOrderNo());
                        entityShowRoomStock.setCreatedOn(currentUnixTime);
                        entityShowRoomStock.setModifiedOn(currentUnixTime);
                        entityShowRoomStocks.add(entityShowRoomStock);
                    }
                    List<EntityShowRoomStock> resultEntityShowRoomStocks = entityManagerShowRoomStock.addShowRoomStocks(entityShowRoomStocks, session);                    
                    if(resultEntityShowRoomStocks == null || resultEntityShowRoomStocks.isEmpty())
                    {
                        tx.rollback();
                        return false;
                    }
                }                
                tx.commit();
                return true;
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
    
    public EntityAdjustStockOrder getLastAdjustStockOrder()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityAdjustStockOrder> query = session.getNamedQuery("getLastAdjustStockeOrder");
            query.setMaxResults(1);
            List<EntityAdjustStockOrder> adjustStockOrderList = query.getResultList();
            if(adjustStockOrderList == null || adjustStockOrderList.isEmpty())
            {
                return null;
            }
            else
            {
                return adjustStockOrderList.get(0);
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityAdjustStockOrder getAdjustStockOrderByOrderNo(String orderNo)
    {
        if(StringUtils.isNullOrEmpty(orderNo))
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityAdjustStockOrder> query = session.getNamedQuery("getAdjustStockeOrderByOrderNo");
            query.setParameter("orderNo", orderNo);
            List<EntityAdjustStockOrder> adjustStockOrderList = query.getResultList();
            if(adjustStockOrderList == null || adjustStockOrderList.isEmpty())
            {
                return null;
            }
            else
            {
                return adjustStockOrderList.get(0);
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityAdjustStockOrder getAdjustStockOrderById(int id)
    {
        if(id <= 0)
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityAdjustStockOrder> query = session.getNamedQuery("getAdjustStockOrderById");
            query.setParameter("id", id);
            List<EntityAdjustStockOrder> adjustStockOrderList = query.getResultList();
            if(adjustStockOrderList == null || adjustStockOrderList.isEmpty())
            {
                return null;
            }
            else
            {
                return adjustStockOrderList.get(0);
            }            
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityAdjustStockOrder> getAdjustStockOrders(int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityAdjustStockOrder> query = session.getNamedQuery("getAllAdjustStockeOrders");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityAdjustStockOrder> searchAdjustStockOrdersDQ(String orderNo, long startTime, long endTime, int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            String where = "";
            if(!StringUtils.isNullOrEmpty(orderNo))
            {
                if(StringUtils.isNullOrEmpty(where))
                {
                    where += " where order_no like '%" + orderNo + "%' ";
                }
                else
                {
                    where += " AND order_no like '%" + orderNo + "%' ";
                }
                if(startTime > 0)
                {
                    if( StringUtils.isNullOrEmpty(where) )
                    {
                        where += " where created_on >=  " + startTime ;
                    }
                    else
                    {
                        where += " AND created_on >=  " + startTime ;
                    }
                }
                if(endTime > 0)
                {
                    if( StringUtils.isNullOrEmpty(where) )
                    {
                        where += " where created_on <=  " + endTime ;
                    }
                    else
                    {
                        where += " AND created_on <=  " + endTime ;
                    }
                }
                
            }
            Query query = session.createSQLQuery("select {aso.*} from adjust_stock_orders aso " + where + " order by modified_on desc limit :limit offset :offset")
                    .addEntity("aso",EntityAdjustStockOrder.class)
                    .setInteger("limit", limit)
                    .setInteger("offset", offset);
            return query.list();           
        } 
        finally 
        {
            session.close();
        }
    }
}
