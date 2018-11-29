package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderPayment;
import org.bdlions.inventory.entity.EntitySaleOrderProduct;
import org.bdlions.inventory.entity.EntitySaleOrderReturnProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
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
public class EntityManagerSaleOrder 
{
    private int appId;
    public EntityManagerSaleOrder(int appId)
    {
        this.appId = appId;
    }
    
    private final Logger logger = LoggerFactory.getLogger(EntityManagerSaleOrder.class);
    /**
     * This method will create new sale order using session
     * @param entitySaleOrder entity sale order
     * @param session session
     * @return EntitySaleOrder EntitySaleOrder
     */
    public EntitySaleOrder createSaleOrder(EntitySaleOrder entitySaleOrder, Session session)
    {
        TimeUtils timeUtils = new TimeUtils();
        entitySaleOrder.setCreatedOn(timeUtils.getCurrentTime());
        entitySaleOrder.setModifiedOn(timeUtils.getCurrentTime());
        session.save(entitySaleOrder);
        return entitySaleOrder;
    }
    
    /**
     * This method will create new sale order
     * @param entitySaleOrder entity sale order
     * @return EntitySaleOrder EntitySaleOrder
     */
    public EntitySaleOrder createSaleOrder(EntitySaleOrder entitySaleOrder)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return createSaleOrder(entitySaleOrder, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will create new sale order with product list
     * @param entitySaleOrder entity sale order
     * @param entitySaleOrderProductList entity sale order product list
     * @param entityShowRoomStockList entity show room stock list
     * @return EntitySaleOrder EntitySaleOrder
     */
    public EntitySaleOrder createSaleOrder(EntitySaleOrder entitySaleOrder, List<EntitySaleOrderProduct> entitySaleOrderProductList, List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProductList, List<EntityShowRoomStock> entityShowRoomStockList)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        long currentUnixTime = TimeUtils.getCurrentTime();
        try 
        {
            entitySaleOrder = createSaleOrder(entitySaleOrder, session);
            if(entitySaleOrder != null && !StringUtils.isNullOrEmpty(entitySaleOrder.getOrderNo()))
            {
                
                //adding entry for sale order payment table
                EntityManagerSaleOrderPayment entityManagerSaleOrderPayment = new EntityManagerSaleOrderPayment(appId);
                EntitySaleOrderPayment entitySaleOrderPaymentIn = new EntitySaleOrderPayment();
                entitySaleOrderPaymentIn.setCustomerUserId(entitySaleOrder.getCustomerUserId());
                entitySaleOrderPaymentIn.setCustomerName(entitySaleOrder.getCustomerName());
                entitySaleOrderPaymentIn.setReference(entitySaleOrder.getOrderNo());
                entitySaleOrderPaymentIn.setAmountIn(entitySaleOrder.getTotal());
                entitySaleOrderPaymentIn.setAmountOut(0.0);
                entitySaleOrderPaymentIn.setPaymentTypeId(Constants.SALE_ORDER_PAYMENT_TYPE_ID_ADD_SALE_IN);
                entitySaleOrderPaymentIn.setDescription(Constants.SALE_ORDER_PAYMENT_TYPE_ADD_SALE_IN_DESCRIPTION);
                entitySaleOrderPaymentIn.setUnixPaymentDate(currentUnixTime);
                entitySaleOrderPaymentIn.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                entitySaleOrderPaymentIn.setCreatedByUserId(entitySaleOrder.getCreatedByUserId());
                entitySaleOrderPaymentIn.setCreatedByUserName(entitySaleOrder.getCreatedByUserName());
                entitySaleOrderPaymentIn.setModifiedByUserId(entitySaleOrder.getModifiedByUserId());
                entitySaleOrderPaymentIn.setModifiedByUserName(entitySaleOrder.getModifiedByUserName());
                entityManagerSaleOrderPayment.createSaleOrderPayment(entitySaleOrderPaymentIn, session);
                
                double paid = entitySaleOrder.getPaid();
                //if user pays any amount then we are storing it into the db
                if(paid > 0.0)
                {
                    EntitySaleOrderPayment entitySaleOrderPaymentOut = new EntitySaleOrderPayment();
                    entitySaleOrderPaymentOut.setCustomerUserId(entitySaleOrder.getCustomerUserId());
                    entitySaleOrderPaymentOut.setCustomerName(entitySaleOrder.getCustomerName());
                    entitySaleOrderPaymentOut.setReference(entitySaleOrder.getOrderNo());
                    entitySaleOrderPaymentOut.setAmountIn(0.0);
                    entitySaleOrderPaymentOut.setAmountOut(paid);
                    entitySaleOrderPaymentOut.setPaymentTypeId(Constants.SALE_ORDER_PAYMENT_TYPE_ID_SALE_PAYMENT_OUT);
                    entitySaleOrderPaymentOut.setDescription(Constants.SALE_ORDER_PAYMENT_TYPE_SALE_PAYMENT_OUT_DESCRIPTION);
                    entitySaleOrderPaymentOut.setUnixPaymentDate(TimeUtils.getCurrentTime());
                    entitySaleOrderPaymentOut.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                    entitySaleOrderPaymentOut.setCreatedByUserId(entitySaleOrder.getCreatedByUserId());
                    entitySaleOrderPaymentOut.setCreatedByUserName(entitySaleOrder.getCreatedByUserName());
                    entitySaleOrderPaymentOut.setModifiedByUserId(entitySaleOrder.getModifiedByUserId());
                    entitySaleOrderPaymentOut.setModifiedByUserName(entitySaleOrder.getModifiedByUserName());
                    entityManagerSaleOrderPayment.createSaleOrderPayment(entitySaleOrderPaymentOut, session);
                }
                //updating customer current balance because of sale order payment table entry
                if(entitySaleOrder.getCustomerUserId() > 0)
                {
                    EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(this.appId);
                    EntityCustomer entityCustomer = entityManagerCustomer.getCustomerByUserId(entitySaleOrder.getCustomerUserId(), session);
                    if(entityCustomer != null && entityCustomer.getId() > 0)
                    {
                        double currentDue = entityManagerSaleOrderPayment.getCustomerCurrentDue( entitySaleOrder.getCustomerUserId(), session);
                        entityCustomer.setBalance(currentDue);
                        entityManagerCustomer.updateCustomer(entityCustomer, session);
                    }                        
                }
                
                if(entitySaleOrderProductList != null && !entitySaleOrderProductList.isEmpty())
                {
                    List<EntitySaleOrderProduct> entitySaleOrderProducts = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entitySaleOrderProductList.size(); counter++)
                    {
                        EntitySaleOrderProduct entitySaleOrderProduct = entitySaleOrderProductList.get(counter);
                        entitySaleOrderProduct.setSaleOrderNo(entitySaleOrder.getOrderNo());
                        entitySaleOrderProducts.add(entitySaleOrderProduct);
                    }
                    EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct(this.appId);
                    List<EntitySaleOrderProduct> resultEntitySaleOrderProducts = entityManagerSaleOrderProduct.addSaleOrderProducts(entitySaleOrderProducts, session);
                    if(resultEntitySaleOrderProducts == null || resultEntitySaleOrderProducts.isEmpty())
                    {
                        tx.rollback();
                        return null;
                    }
                }
                //sale order return product list
                if(entitySaleOrderReturnProductList != null && !entitySaleOrderReturnProductList.isEmpty())
                {
                    List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProducts = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entitySaleOrderReturnProductList.size(); counter++)
                    {
                        EntitySaleOrderReturnProduct entitySaleOrderReturnProduct = entitySaleOrderReturnProductList.get(counter);
                        entitySaleOrderReturnProduct.setSaleOrderNo(entitySaleOrder.getOrderNo());
                        entitySaleOrderReturnProducts.add(entitySaleOrderReturnProduct);
                    }
                    EntityManagerSaleOrderReturnProduct entityManagerSaleOrderReturnProduct = new EntityManagerSaleOrderReturnProduct(this.appId);
                    List<EntitySaleOrderReturnProduct> resultEntitySaleOrderReturnProducts = entityManagerSaleOrderReturnProduct.addSaleOrderReturnProducts(entitySaleOrderReturnProducts, session);
                    if(resultEntitySaleOrderReturnProducts == null || resultEntitySaleOrderReturnProducts.isEmpty())
                    {
                        tx.rollback();
                        return null;
                    }
                }
                if(entityShowRoomStockList != null && !entityShowRoomStockList.isEmpty())
                {
                    List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
                    //for each item, set sale order no into list
                    for(int counter = 0; counter < entityShowRoomStockList.size(); counter++)
                    {
                        EntityShowRoomStock entityShowRoomStock = entityShowRoomStockList.get(counter);
                        //entityShowRoomStock.setSaleOrderNo(entitySaleOrder.getOrderNo());
                        entityShowRoomStock.setOrderNo(entitySaleOrder.getOrderNo());
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
                /*if(entitySaleOrder.getCustomerUserId() > 0)
                {
                    EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(this.appId);
                    EntityCustomer entityCustomer = entityManagerCustomer.getCustomerByUserId(entitySaleOrder.getCustomerUserId(), session);
                    if(entityCustomer != null && entityCustomer.getUserId() > 0)
                    {
                        double currentDue = this.getCustomerCurrentDue(entitySaleOrder.getCustomerUserId(), session);
                        entityCustomer.setBalance(currentDue);
                        entityManagerCustomer.updateCustomer(entityCustomer, session);
                    }                        
                }*/                    
                tx.commit();
                return entitySaleOrder;
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
    
    /**
     * This method will update sale order using session
     * @param entitySaleOrder entity sale order
     * @param session session
     * @return boolean true
     */
    public boolean updateSaleOrder(EntitySaleOrder entitySaleOrder, Session session)
    {
        TimeUtils timeUtils = new TimeUtils();
        entitySaleOrder.setModifiedOn(timeUtils.getCurrentTime());
        session.update(entitySaleOrder);
        return true;
    }
    
    /**
     * This method will update sale order
     * @param entitySaleOrder entity sale order
     * @return boolean true
     */
    public boolean updateSaleOrder(EntitySaleOrder entitySaleOrder)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return updateSaleOrder(entitySaleOrder, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will update sale order
     * @param currentEntitySaleOrder current entity sale order
     * @param entitySaleOrder entity sale order
     * @param entitySaleOrderProductList entity sale order product list
     * @param entityShowRoomStockList entity show room stock list
     * @return boolean true
     */
    public boolean updateSaleOrder(EntitySaleOrder currentEntitySaleOrder, EntitySaleOrder entitySaleOrder, List<EntitySaleOrderProduct> entitySaleOrderProductList, List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProductList, List<EntityShowRoomStock> entityShowRoomStockList)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        long currentUnixTime = TimeUtils.getCurrentTime();
        try 
        {
            if(entitySaleOrder != null && entitySaleOrder.getId() > 0 && updateSaleOrder(entitySaleOrder, session))
            {
                EntityManagerSaleOrderPayment entityManagerSaleOrderPayment = new EntityManagerSaleOrderPayment(appId);
                //deleting sale payments entries based on reference where reference is order no
                entityManagerSaleOrderPayment.deleteSaleOrderPaymentsByReference(entitySaleOrder.getOrderNo(), session);
                //adding entry for sale order payment table
                EntitySaleOrderPayment entitySaleOrderPaymentIn = new EntitySaleOrderPayment();
                entitySaleOrderPaymentIn.setCustomerUserId(entitySaleOrder.getCustomerUserId());
                entitySaleOrderPaymentIn.setCustomerName(entitySaleOrder.getCustomerName());
                entitySaleOrderPaymentIn.setReference(entitySaleOrder.getOrderNo());
                entitySaleOrderPaymentIn.setAmountIn(entitySaleOrder.getTotal());
                entitySaleOrderPaymentIn.setAmountOut(0.0);
                entitySaleOrderPaymentIn.setPaymentTypeId(Constants.SALE_ORDER_PAYMENT_TYPE_ID_ADD_SALE_IN);
                entitySaleOrderPaymentIn.setDescription(Constants.SALE_ORDER_PAYMENT_TYPE_ADD_SALE_IN_DESCRIPTION);
                entitySaleOrderPaymentIn.setUnixPaymentDate(currentUnixTime);
                entitySaleOrderPaymentIn.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                entitySaleOrderPaymentIn.setCreatedByUserId(entitySaleOrder.getCreatedByUserId());
                entitySaleOrderPaymentIn.setCreatedByUserName(entitySaleOrder.getCreatedByUserName());
                entitySaleOrderPaymentIn.setModifiedByUserId(entitySaleOrder.getModifiedByUserId());
                entitySaleOrderPaymentIn.setModifiedByUserName(entitySaleOrder.getModifiedByUserName());
                entityManagerSaleOrderPayment.createSaleOrderPayment(entitySaleOrderPaymentIn, session);
                
                double paid = entitySaleOrder.getPaid();
                //if user pays any amount then we are storing it into the db
                if(paid > 0.0)
                {
                    EntitySaleOrderPayment entitySaleOrderPaymentOut = new EntitySaleOrderPayment();
                    entitySaleOrderPaymentOut.setCustomerUserId(entitySaleOrder.getCustomerUserId());
                    entitySaleOrderPaymentOut.setCustomerName(entitySaleOrder.getCustomerName());
                    entitySaleOrderPaymentOut.setReference(entitySaleOrder.getOrderNo());
                    entitySaleOrderPaymentOut.setAmountIn(0.0);
                    entitySaleOrderPaymentOut.setAmountOut(paid);
                    entitySaleOrderPaymentOut.setPaymentTypeId(Constants.SALE_ORDER_PAYMENT_TYPE_ID_SALE_PAYMENT_OUT);
                    entitySaleOrderPaymentOut.setDescription(Constants.SALE_ORDER_PAYMENT_TYPE_SALE_PAYMENT_OUT_DESCRIPTION);
                    entitySaleOrderPaymentOut.setUnixPaymentDate(TimeUtils.getCurrentTime());
                    entitySaleOrderPaymentOut.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                    entitySaleOrderPaymentOut.setCreatedByUserId(entitySaleOrder.getCreatedByUserId());
                    entitySaleOrderPaymentOut.setCreatedByUserName(entitySaleOrder.getCreatedByUserName());
                    entitySaleOrderPaymentOut.setModifiedByUserId(entitySaleOrder.getModifiedByUserId());
                    entitySaleOrderPaymentOut.setModifiedByUserName(entitySaleOrder.getModifiedByUserName());
                    entityManagerSaleOrderPayment.createSaleOrderPayment(entitySaleOrderPaymentOut, session);
                }
                //updating customer current balance because of sale order payment table entry
                if(entitySaleOrder.getCustomerUserId() > 0)
                {
                    EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(this.appId);
                    EntityCustomer entityCustomer = entityManagerCustomer.getCustomerByUserId(entitySaleOrder.getCustomerUserId(), session);
                    if(entityCustomer != null && entityCustomer.getId() > 0)
                    {
                        double currentDue = entityManagerSaleOrderPayment.getCustomerCurrentDue( entitySaleOrder.getCustomerUserId(), session);
                        entityCustomer.setBalance(currentDue);
                        entityManagerCustomer.updateCustomer(entityCustomer, session);
                    }                        
                }
                
                EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct(this.appId);
                EntityManagerSaleOrderReturnProduct entityManagerSaleOrderReturnProduct = new EntityManagerSaleOrderReturnProduct(this.appId);
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(this.appId);
                //deleting existing products
                if(!StringUtils.isNullOrEmpty(entitySaleOrder.getOrderNo()))
                {
                    entityManagerSaleOrderProduct.deleteSaleOrderProductsByOrderNo(currentEntitySaleOrder.getOrderNo(), session);
                    entityManagerSaleOrderReturnProduct.deleteSaleOrderReturnProductsByOrderNo(currentEntitySaleOrder.getOrderNo(), session);
                    List<Integer> transactionCategoryIds = new ArrayList<>();
                    transactionCategoryIds.add(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_FULFILLED);
                    transactionCategoryIds.add(Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_RESTOCK);
                    entityManagerShowRoomStock.deleteShowRoomProductsBySaleOrderNoAndTransactionCategoryIds(currentEntitySaleOrder.getOrderNo(), transactionCategoryIds, session);
                }
                if(entitySaleOrderProductList != null && !entitySaleOrderProductList.isEmpty())
                {
                    List<EntitySaleOrderProduct> entitySaleOrderProducts = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entitySaleOrderProductList.size(); counter++)
                    {
                        EntitySaleOrderProduct entitySaleOrderProduct = entitySaleOrderProductList.get(counter);
                        entitySaleOrderProduct.setSaleOrderNo(entitySaleOrder.getOrderNo());
                        entitySaleOrderProducts.add(entitySaleOrderProduct);
                    }
                    List<EntitySaleOrderProduct> resultEntitySaleOrderProducts = entityManagerSaleOrderProduct.addSaleOrderProducts(entitySaleOrderProducts, session);
                    if(resultEntitySaleOrderProducts == null || resultEntitySaleOrderProducts.isEmpty())
                    {
                        tx.rollback();
                        return false;
                    }
                }
                if(entitySaleOrderReturnProductList != null && !entitySaleOrderReturnProductList.isEmpty())
                {
                    List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProducts = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entitySaleOrderReturnProductList.size(); counter++)
                    {
                        EntitySaleOrderReturnProduct entitySaleOrderReturnProduct = entitySaleOrderReturnProductList.get(counter);
                        entitySaleOrderReturnProduct.setSaleOrderNo(entitySaleOrder.getOrderNo());
                        entitySaleOrderReturnProducts.add(entitySaleOrderReturnProduct);
                    }
                    List<EntitySaleOrderReturnProduct> resultEntitySaleOrderReturnProducts = entityManagerSaleOrderReturnProduct.addSaleOrderReturnProducts(entitySaleOrderReturnProducts, session);
                    if(resultEntitySaleOrderReturnProducts == null || resultEntitySaleOrderReturnProducts.isEmpty())
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
                        entityShowRoomStock.setOrderNo(entitySaleOrder.getOrderNo());
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
                /*if(entitySaleOrder.getCustomerUserId() > 0)
                {
                    EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(this.appId);
                    EntityCustomer entityCustomer = entityManagerCustomer.getCustomerByUserId(entitySaleOrder.getCustomerUserId(), session);
                    if(entityCustomer != null && entityCustomer.getUserId() > 0)
                    {
                        double currentDue = this.getCustomerCurrentDue(entitySaleOrder.getCustomerUserId(), session);
                        entityCustomer.setBalance(currentDue);
                        //double currentDueOfOrder = entitySaleOrder.getPaid() - entitySaleOrder.getTotal();
                        //double newDueOfOrder = entitySaleOrder.getPaid() - entitySaleOrder.getTotal();
                        //entityCustomer.setBalance(this.getCustomerCurrentDue(entitySaleOrder.getCustomerUserId(), session) - currentDueOfOrder + newDueOfOrder);
                        entityManagerCustomer.updateCustomer(entityCustomer, session);
                    }                        
                }*/
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
    
    /**
     * This method will return sale order by order no
     * @param orderNo sale order no
     * @return EntitySaleOrder EntitySaleOrder
     */
    public EntitySaleOrder getSaleOrderByOrderNo(String orderNo)
    {
        if(orderNo == null || orderNo.equals(""))
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntitySaleOrder> query = session.getNamedQuery("getSaleOrderByOrderNo");
            query.setParameter("orderNo", orderNo);
            List<EntitySaleOrder> saleOrderList = query.getResultList();
            if(saleOrderList == null || saleOrderList.isEmpty())
            {
                return null;
            }
            else
            {
                return saleOrderList.get(0);
            }                 
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return sale order by id
     * @param id id
     * @return EntitySaleOrder EntitySaleOrder
     */
    public EntitySaleOrder getSaleOrderById(int id)
    {
        if(id <= 0)
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntitySaleOrder> query = session.getNamedQuery("getSaleOrderById");
            query.setParameter("id", id);
            List<EntitySaleOrder> saleOrderList = query.getResultList();
            if(saleOrderList == null || saleOrderList.isEmpty())
            {
                return null;
            }
            else
            {
                return saleOrderList.get(0);
            }            
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntitySaleOrder getLastSaleOrder()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntitySaleOrder> query = session.getNamedQuery("getLastSaleOrder");
            query.setMaxResults(1);
            List<EntitySaleOrder> saleOrderList = query.getResultList();
            if(saleOrderList == null || saleOrderList.isEmpty())
            {
                return null;
            }
            else
            {
                return saleOrderList.get(0);
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return sale order list
     * @param offset offset
     * @param limit limit
     * @return List entity sale order list
     */
    public List<EntitySaleOrder> getSaleOrders(int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySaleOrder> query = session.getNamedQuery("getAllSaleOrders");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total number of sale orders
     * @return Integer total number of sale orders
     */
    public int getTotalSaleOrders()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySaleOrder> query = session.getNamedQuery("getAllSaleOrders");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total amount of sale orders
     * @return double total amount of sale orders
     */
    public double getTotalSaleAmount()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        double totalSaleAmount = 0 ;
        try 
        {
            Query<Object> query = session.getNamedQuery("getTotalSaleAmount");
            List<Object> results = query.getResultList();
            for(Object result : results)
            {
                if(result != null)
                {
                    totalSaleAmount = (double)result;
                    break;
                } 
            }            
        } 
        finally 
        {
            session.close();
        }
        return totalSaleAmount;
    }
    
    /**
     * This method will return sale order list search by order no, case insensitive
     * @param orderNo sale order no
     * @param offset offset
     * @param limit limit
     * @return List entity sale order list
     */
    public List<EntitySaleOrder> searchSaleOrderByOrderNo(String orderNo, int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySaleOrder> query = session.getNamedQuery("searchSaleOrderByOrderNo");
            query.setParameter("orderNo", "%" + orderNo.toLowerCase() + "%");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total number of sale order list search by order no, case insensitive
     * @param orderNo sale order no
     * @return Integer total number of sale orders
     */
    public int searchTotalSaleOrderByOrderNo(String orderNo)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySaleOrder> query = session.getNamedQuery("searchSaleOrderByOrderNo");
            query.setParameter("orderNo", "%" + orderNo.toLowerCase() + "%");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total amount of sale orders search by orderNo, case insensitive
     * @param orderNo Order No
     * @return double total amount of sale orders
     */
    public double searchTotalSaleAmountByOrderNo(String orderNo)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        double totalSaleAmount = 0 ;
        try 
        {
            Query<Object> query = session.getNamedQuery("searchTotalSaleAmountByOrderNo");
            query.setParameter("orderNo", "%" + orderNo.toLowerCase() + "%");
            List<Object> results = query.getResultList();
            for(Object result : results)
            {
                if(result != null)
                {
                    totalSaleAmount = (double)result;
                    break;
                }
            }            
        } 
        finally 
        {
            session.close();
        }
        return totalSaleAmount;
    }
    
    /**
     * This method will return sale order list search by cell, case insensitive
     * @param cell cell no
     * @param offset offset
     * @param limit limit
     * @return List entity sale order list
     */
    public List<EntitySaleOrder> searchSaleOrderByCell(String cell, int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySaleOrder> query = session.getNamedQuery("searchSaleOrderByCell");
            query.setParameter("cell", "%" + cell.toLowerCase() + "%");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total number of sale order list search by cell, case insensitive
     * @param cell cell no
     * @return Integer total number of sale orders
     */
    public int searchTotalSaleOrderByCell(String cell)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySaleOrder> query = session.getNamedQuery("searchSaleOrderByCell");
            query.setParameter("cell", "%" + cell.toLowerCase() + "%");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total amount of sale orders search by cell, case insensitive
     * @param cell cell no
     * @return double total amount of sale orders
     */
    public double searchTotalSaleAmountByCell(String cell)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        double totalSaleAmount = 0 ;
        try 
        {
            Query<Object> query = session.getNamedQuery("searchTotalSaleAmountByCell");
            query.setParameter("cell", "%" + cell.toLowerCase() + "%");
            List<Object> results = query.getResultList();
            for(Object result : results)
            {
                if(result != null)
                {
                    totalSaleAmount = (double)result;
                    break;
                }
            }            
        } 
        finally 
        {
            session.close();
        }
        return totalSaleAmount;
    }
    
    /**
     * This method will update sale order customer info using session
     * @param entitySaleOrder entity sale order
     * @param session session
     * @return boolean true
     */
    public int updateSaleOrderCustomerInfo(EntitySaleOrder entitySaleOrder, Session session)
    {
        Query<EntitySaleOrder> query = session.getNamedQuery("updateSaleOrderCustomerInfo");
        query.setParameter("customerName", entitySaleOrder.getCustomerName());
        query.setParameter("cell", entitySaleOrder.getCell());
        query.setParameter("email", entitySaleOrder.getEmail());
        query.setParameter("customerUserId", entitySaleOrder.getCustomerUserId());
        return query.executeUpdate();
    }
    
    /**
     * This method will update sale order paid amount by order no using session
     * @param paid paid amount
     * @param orderNo sale order No
     * @param session session
     * @return boolean true
     */
    public int updateSaleOrderPaidByOrderNo(double paid, String orderNo, Session session)
    {
        Query<EntitySaleOrder> query = session.getNamedQuery("updateSaleOrderPaidByOrderNo");
        query.setParameter("paid", paid);
        query.setParameter("orderNo", orderNo);
        return query.executeUpdate();
    }
    
    public double getCustomerCurrentDue(int customerUserId, Session session)
    {
        if(customerUserId <= 0)
        {
            return 0;
        }
        double currentDue = 0;
        Query<Object[]> query = session.getNamedQuery("getCustomerCurrentDue");
        query.setParameter("customerUserId", customerUserId);
        List<Object[]> saleOrderList = query.getResultList();
        if(saleOrderList == null || saleOrderList.isEmpty())
        {
            return 0;
        }
        else
        {
            try
            {
                currentDue = (double)saleOrderList.get(0)[0];
            }
            catch(Exception ex)
            {
                logger.error(ex.toString());
                currentDue = 0;
            }
        } 
        return currentDue;
    }
    
//    public double getCustomerCurrentDue(int customerUserId)
//    {
//        if(customerUserId <= 0)
//        {
//            return 0;
//        }
//        Session session = HibernateUtil.getInstance().getSession(this.appId);
//        try 
//        {            
//            return this.getCustomerCurrentDue(customerUserId, session);
//        } 
//        finally 
//        {
//            session.close();
//        }
//    }
    
    // --------------------------- Dynamic Query Section Starts ----------------------------------//
    public List<EntitySaleOrder> getSaleOrdersDQ(long startTime, long endTime, int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            String where = " where created_on >= " + startTime + " AND created_on <= " + endTime + " ";
            Query query = session.createSQLQuery("select {eso.*} from sale_orders eso " + where + " order by created_on desc limit :limit offset :offset")
                    .addEntity("eso",EntitySaleOrder.class)
                    .setInteger("limit", limit)
                    .setInteger("offset", offset);
            return query.list();           
        } 
        finally 
        {
            session.close();
        }
    }
    
    public int getTotalSaleOrdersDQ(long startTime, long endTime)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            String where = " where created_on >= " + startTime + " AND created_on <= " + endTime + " ";
            Query query = session.createSQLQuery("select {eso.*} from sale_orders eso " + where)
                    .addEntity("eso",EntitySaleOrder.class);
            return query.list().size();           
        } 
        finally 
        {
            session.close();
        }
    }
    
    public double getTotalSaleAmountDQ(long startTime, long endTime)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        double totalSaleAmount = 0 ;
        try 
        {
            String where = " where created_on >= " + startTime + " AND created_on <= " + endTime + " ";
            Query query = session.createSQLQuery("select sum(total) from sale_orders " + where);
            List<Object> results = query.getResultList();
            for(Object result : results)
            {
                if(result != null)
                {
                    totalSaleAmount = (double)result;
                    break;
                }                
            }            
        } 
        finally 
        {
            session.close();
        }
        return totalSaleAmount;
    }
    
}
