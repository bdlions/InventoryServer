package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.entity.EntityPOShowRoomReturnProduct;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntityPurchaseOrderPayment;
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
public class EntityManagerPurchaseOrder 
{
    private int appId;
    public EntityManagerPurchaseOrder(int appId)
    {
        this.appId = appId;
    }
    
    private final Logger logger = LoggerFactory.getLogger(EntityManagerPurchaseOrder.class);
    /**
     * This method will create new purchase order using session
     * @param entityPurchaseOrder entity purchase order
     * @param session session
     * @return EntityPurchaseOrder EntityPurchaseOrder
     */
    public EntityPurchaseOrder createPurchaseOrder(EntityPurchaseOrder entityPurchaseOrder, Session session)
    {
        entityPurchaseOrder.setCreatedOn(TimeUtils.getCurrentTime());
        entityPurchaseOrder.setModifiedOn(TimeUtils.getCurrentTime());
        session.save(entityPurchaseOrder);
        return entityPurchaseOrder;
    }
    
    /**
     * This method will create new purchase order
     * @param entityPurchaseOrder entity purchase order
     * @return EntityPurchaseOrder EntityPurchaseOrder
     */
    public EntityPurchaseOrder createPurchaseOrder(EntityPurchaseOrder entityPurchaseOrder)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return createPurchaseOrder(entityPurchaseOrder, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will create new purchase order with product list
     * @param entityPurchaseOrder entity purchase order
     * @param entityPOShowRoomProductList entity purchase order show room product list
     * @param entityPOShowRoomReturnProductList entity purchase order show room return product list
     * @param entityShowRoomStockList entity show room stock list
     * @return EntityPurchaseOrder EntityPurchaseOrder
     */
    public EntityPurchaseOrder createPurchaseOrder(EntityPurchaseOrder entityPurchaseOrder, List<EntityPOShowRoomProduct> entityPOShowRoomProductList, List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProductList, List<EntityShowRoomStock> entityShowRoomStockList)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        long currentUnixTime = TimeUtils.getCurrentTime();
        try 
        {
            entityPurchaseOrder = createPurchaseOrder(entityPurchaseOrder, session);
            if(entityPurchaseOrder != null && !StringUtils.isNullOrEmpty(entityPurchaseOrder.getOrderNo()))
            {
                //adding entry for purchse order payment table
                EntityManagerPurchaseOrderPayment entityManagerPurchaseOrderPayment = new EntityManagerPurchaseOrderPayment(appId);
                EntityPurchaseOrderPayment entityPurchaseOrderPaymentIn = new EntityPurchaseOrderPayment();
                entityPurchaseOrderPaymentIn.setSupplierUserId(entityPurchaseOrder.getSupplierUserId());
                entityPurchaseOrderPaymentIn.setSupplierName(entityPurchaseOrder.getSupplierName());
                entityPurchaseOrderPaymentIn.setReference(entityPurchaseOrder.getOrderNo());
                entityPurchaseOrderPaymentIn.setAmountIn(entityPurchaseOrder.getTotal());
                entityPurchaseOrderPaymentIn.setAmountOut(0.0);
                entityPurchaseOrderPaymentIn.setPaymentTypeId(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ID_ADD_PURCHASE_IN);
                entityPurchaseOrderPaymentIn.setDescription(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ADD_PURCHASE_IN_DESCRIPTION);
                entityPurchaseOrderPaymentIn.setUnixPaymentDate(TimeUtils.getCurrentTime());
                entityPurchaseOrderPaymentIn.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                entityPurchaseOrderPaymentIn.setCreatedByUserId(entityPurchaseOrder.getCreatedByUserId());
                entityPurchaseOrderPaymentIn.setCreatedByUserName(entityPurchaseOrder.getCreatedByUserName());
                entityPurchaseOrderPaymentIn.setModifiedByUserId(entityPurchaseOrder.getModifiedByUserId());
                entityPurchaseOrderPaymentIn.setModifiedByUserName(entityPurchaseOrder.getModifiedByUserName());
                entityManagerPurchaseOrderPayment.createPurchaseOrderPayment(entityPurchaseOrderPaymentIn, session);
                
                double paid = entityPurchaseOrder.getPaid();
                //if user pays any amount then we are storing it into the db
                if(paid > 0.0)
                {
                    EntityPurchaseOrderPayment entityPurchaseOrderPaymentOut = new EntityPurchaseOrderPayment();
                    entityPurchaseOrderPaymentOut.setSupplierUserId(entityPurchaseOrder.getSupplierUserId());
                    entityPurchaseOrderPaymentOut.setSupplierName(entityPurchaseOrder.getSupplierName());
                    entityPurchaseOrderPaymentOut.setReference(entityPurchaseOrder.getOrderNo());
                    entityPurchaseOrderPaymentOut.setAmountIn(0.0);
                    entityPurchaseOrderPaymentOut.setAmountOut(paid);
                    entityPurchaseOrderPaymentOut.setPaymentTypeId(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ID_PURCHASE_PAYMENT_OUT);
                    entityPurchaseOrderPaymentOut.setDescription(Constants.PURCHASE_ORDER_PAYMENT_TYPE_PURCHASE_PAYMENT_OUT_DESCRIPTION);
                    entityPurchaseOrderPaymentOut.setUnixPaymentDate(currentUnixTime);
                    entityPurchaseOrderPaymentOut.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                    entityPurchaseOrderPaymentOut.setCreatedByUserId(entityPurchaseOrder.getCreatedByUserId());
                    entityPurchaseOrderPaymentOut.setCreatedByUserName(entityPurchaseOrder.getCreatedByUserName());
                    entityPurchaseOrderPaymentOut.setModifiedByUserId(entityPurchaseOrder.getModifiedByUserId());
                    entityPurchaseOrderPaymentOut.setModifiedByUserName(entityPurchaseOrder.getModifiedByUserName());
                    entityManagerPurchaseOrderPayment.createPurchaseOrderPayment(entityPurchaseOrderPaymentOut, session);
                }
                //updating supplier current balance because of purchase order payment table entry
                if(entityPurchaseOrder.getSupplierUserId() > 0)
                {
                    EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(this.appId);
                    EntitySupplier entitySupplier = entityManagerSupplier.getSupplierByUserId(entityPurchaseOrder.getSupplierUserId(), session);
                    if(entitySupplier != null && entitySupplier.getId() > 0)
                    {
                        double currentDue = entityManagerPurchaseOrderPayment.getSupplierCurrentDue( entityPurchaseOrder.getSupplierUserId(), session);
                        entitySupplier.setBalance(currentDue);
                        entityManagerSupplier.updateSupplier(entitySupplier, session);
                    }                        
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
                    EntityManagerPOShowRoomProduct entityManagerPOShowRoomProduct = new EntityManagerPOShowRoomProduct(this.appId);
                    List<EntityPOShowRoomProduct> resultEntityPOShowRoomProducts = entityManagerPOShowRoomProduct.addPurchaseOrderShowRoomProducts(entityPOShowRoomProducts, session);
                    if(resultEntityPOShowRoomProducts == null || resultEntityPOShowRoomProducts.isEmpty())
                    {
                        tx.rollback();
                        return null;
                    }                    
                }
                //storing return product list
                if(entityPOShowRoomReturnProductList != null && !entityPOShowRoomReturnProductList.isEmpty())
                {
                    List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProducts = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entityPOShowRoomReturnProductList.size(); counter++)
                    {
                        EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct = entityPOShowRoomReturnProductList.get(counter);
                        entityPOShowRoomReturnProduct.setOrderNo(entityPurchaseOrder.getOrderNo());
                        entityPOShowRoomReturnProducts.add(entityPOShowRoomReturnProduct);
                    }
                    EntityManagerPOShowRoomReturnProduct entityManagerPOShowRoomReturnProduct = new EntityManagerPOShowRoomReturnProduct(this.appId);
                    List<EntityPOShowRoomReturnProduct> resultEntityPOShowRoomReturnProducts = entityManagerPOShowRoomReturnProduct.addPurchaseOrderShowRoomReturnProducts(entityPOShowRoomReturnProducts, session);
                    if(resultEntityPOShowRoomReturnProducts == null || resultEntityPOShowRoomReturnProducts.isEmpty())
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
                        entityShowRoomStock.setOrderNo(entityPurchaseOrder.getOrderNo());
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
                /*if(entityPurchaseOrder.getSupplierUserId() > 0)
                {
                    EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(this.appId);
                    EntitySupplier entitySupplier = entityManagerSupplier.getSupplierByUserId(entityPurchaseOrder.getSupplierUserId(), session);
                    if(entitySupplier != null && entitySupplier.getId() > 0)
                    {
                        double currentDue = this.getSupplierCurrentDue( entityPurchaseOrder.getSupplierUserId(), session);
                        entitySupplier.setBalance(currentDue);
                        entityManagerSupplier.updateSupplier(entitySupplier, session);
                    }                        
                }*/                    
                tx.commit();
                return entityPurchaseOrder;
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
     * This method will update purchase order using session
     * @param entityPurchaseOrder entity purchase order
     * @param session session
     * @return boolean true
     */
    public boolean updatePurchaseOrder(EntityPurchaseOrder entityPurchaseOrder, Session session)
    {
        entityPurchaseOrder.setModifiedOn(TimeUtils.getCurrentTime());
        session.update(entityPurchaseOrder);
        return true;
    }
    
    /**
     * This method will update purchase order
     * @param entityPurchaseOrder entity purchase order
     * @return boolean true
     */
    public boolean updatePurchaseOrder(EntityPurchaseOrder entityPurchaseOrder)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return updatePurchaseOrder(entityPurchaseOrder, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will update purchase order
     * @param currentEntityPurchaseOrder current entity purchase order
     * @param entityPurchaseOrder entity purchase order
     * @param entityPOShowRoomProductList entity purchase order show room product list
     * @param entityPOShowRoomReturnProductList entity purchase order show room return product list
     * @param entityShowRoomStockList entity show room stock list
     * @return boolean true
     */
    public boolean updatePurchaseOrder(EntityPurchaseOrder currentEntityPurchaseOrder, EntityPurchaseOrder entityPurchaseOrder, List<EntityPOShowRoomProduct> entityPOShowRoomProductList, List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProductList, List<EntityShowRoomStock> entityShowRoomStockList)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();
        long currentUnixTime = TimeUtils.getCurrentTime();
        try 
        {
            if(entityPurchaseOrder != null && entityPurchaseOrder.getId() > 0 && updatePurchaseOrder(entityPurchaseOrder, session))
            {
                EntityManagerPurchaseOrderPayment entityManagerPurchaseOrderPayment = new EntityManagerPurchaseOrderPayment(appId);
                //deleting purchase payments entries based on reference where reference is order no
                entityManagerPurchaseOrderPayment.deletePurchaseOrderPaymentsByReference(entityPurchaseOrder.getOrderNo(), session);
                //adding entry for purchse order payment table
                EntityPurchaseOrderPayment entityPurchaseOrderPaymentIn = new EntityPurchaseOrderPayment();
                entityPurchaseOrderPaymentIn.setSupplierUserId(entityPurchaseOrder.getSupplierUserId());
                entityPurchaseOrderPaymentIn.setSupplierName(entityPurchaseOrder.getSupplierName());
                entityPurchaseOrderPaymentIn.setReference(entityPurchaseOrder.getOrderNo());
                entityPurchaseOrderPaymentIn.setAmountIn(entityPurchaseOrder.getTotal());
                entityPurchaseOrderPaymentIn.setAmountOut(0.0);
                entityPurchaseOrderPaymentIn.setPaymentTypeId(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ID_ADD_PURCHASE_IN);
                entityPurchaseOrderPaymentIn.setDescription(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ADD_PURCHASE_IN_DESCRIPTION);
                entityPurchaseOrderPaymentIn.setUnixPaymentDate(currentUnixTime);
                entityPurchaseOrderPaymentIn.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                entityPurchaseOrderPaymentIn.setCreatedByUserId(entityPurchaseOrder.getCreatedByUserId());
                entityPurchaseOrderPaymentIn.setCreatedByUserName(entityPurchaseOrder.getCreatedByUserName());
                entityPurchaseOrderPaymentIn.setModifiedByUserId(entityPurchaseOrder.getModifiedByUserId());
                entityPurchaseOrderPaymentIn.setModifiedByUserName(entityPurchaseOrder.getModifiedByUserName());
                entityManagerPurchaseOrderPayment.createPurchaseOrderPayment(entityPurchaseOrderPaymentIn, session);
                
                double paid = entityPurchaseOrder.getPaid();
                //if user pays any amount then we are storing it into the db
                if(paid > 0.0)
                {
                    EntityPurchaseOrderPayment entityPurchaseOrderPaymentOut = new EntityPurchaseOrderPayment();
                    entityPurchaseOrderPaymentOut.setSupplierUserId(entityPurchaseOrder.getSupplierUserId());
                    entityPurchaseOrderPaymentOut.setSupplierName(entityPurchaseOrder.getSupplierName());
                    entityPurchaseOrderPaymentOut.setReference(entityPurchaseOrder.getOrderNo());
                    entityPurchaseOrderPaymentOut.setAmountIn(0.0);
                    entityPurchaseOrderPaymentOut.setAmountOut(paid);
                    entityPurchaseOrderPaymentOut.setPaymentTypeId(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ID_PURCHASE_PAYMENT_OUT);
                    entityPurchaseOrderPaymentOut.setDescription(Constants.PURCHASE_ORDER_PAYMENT_TYPE_PURCHASE_PAYMENT_OUT_DESCRIPTION);
                    entityPurchaseOrderPaymentOut.setUnixPaymentDate(TimeUtils.getCurrentTime());
                    entityPurchaseOrderPaymentOut.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                    entityPurchaseOrderPaymentOut.setCreatedByUserId(entityPurchaseOrder.getCreatedByUserId());
                    entityPurchaseOrderPaymentOut.setCreatedByUserName(entityPurchaseOrder.getCreatedByUserName());
                    entityPurchaseOrderPaymentOut.setModifiedByUserId(entityPurchaseOrder.getModifiedByUserId());
                    entityPurchaseOrderPaymentOut.setModifiedByUserName(entityPurchaseOrder.getModifiedByUserName());
                    entityManagerPurchaseOrderPayment.createPurchaseOrderPayment(entityPurchaseOrderPaymentOut, session);
                }
                //updating supplier current balance because of purchase order payment table entry
                if(entityPurchaseOrder.getSupplierUserId() > 0)
                {
                    EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(this.appId);
                    EntitySupplier entitySupplier = entityManagerSupplier.getSupplierByUserId(entityPurchaseOrder.getSupplierUserId(), session);
                    if(entitySupplier != null && entitySupplier.getId() > 0)
                    {
                        double currentDue = entityManagerPurchaseOrderPayment.getSupplierCurrentDue( entityPurchaseOrder.getSupplierUserId(), session);
                        entitySupplier.setBalance(currentDue);
                        entityManagerSupplier.updateSupplier(entitySupplier, session);
                    }                        
                }
                
                EntityManagerPOShowRoomProduct entityManagerPOShowRoomProduct = new EntityManagerPOShowRoomProduct(this.appId);
                EntityManagerPOShowRoomReturnProduct entityManagerPOShowRoomReturnProduct = new EntityManagerPOShowRoomReturnProduct(this.appId);
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(this.appId);
                //deleting existing products
                /*if(!StringUtils.isNullOrEmpty(entityPurchaseOrder.getOrderNo()))
                {
                    entityManagerPOShowRoomProduct.deletePOShowRoomProductsByOrderNo(currentEntityPurchaseOrder.getOrderNo(), session);
                    entityManagerPOShowRoomReturnProduct.deletePOShowRoomReturnProductsByOrderNo(currentEntityPurchaseOrder.getOrderNo(), session);
                    List<Integer> transactionCategoryIds = new ArrayList<>();
                    transactionCategoryIds.add(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_ORDER_RECEIVE);
                    transactionCategoryIds.add(Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_ORDER_UNSTOCK);
                    entityManagerShowRoomStock.deleteShowRoomProductsByPurchaseOrderNoAndTransactionCategoryIds(currentEntityPurchaseOrder.getOrderNo(), transactionCategoryIds, session);
                }*/
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
                    List<EntityPOShowRoomProduct> resultEntityPOShowRoomProducts = entityManagerPOShowRoomProduct.saveOrUpdatePurchaseOrderShowRoomProducts(entityPOShowRoomProducts, session);
                    if(resultEntityPOShowRoomProducts == null || resultEntityPOShowRoomProducts.isEmpty())
                    {
                        tx.rollback();
                        return false;
                    }
                }
                if(entityPOShowRoomReturnProductList != null && !entityPOShowRoomReturnProductList.isEmpty())
                {
                    List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProducts = new ArrayList<>();
                    //for each item, set order no into list
                    for(int counter = 0; counter < entityPOShowRoomReturnProductList.size(); counter++)
                    {
                        EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct = entityPOShowRoomReturnProductList.get(counter);
                        entityPOShowRoomReturnProduct.setOrderNo(entityPurchaseOrder.getOrderNo());
                        entityPOShowRoomReturnProducts.add(entityPOShowRoomReturnProduct);
                    }
                    List<EntityPOShowRoomReturnProduct> resultEntityPOShowRoomReturnProducts = entityManagerPOShowRoomReturnProduct.saveOrUpdatePurchaseOrderShowRoomReturnProducts(entityPOShowRoomReturnProducts, session);
                    if(resultEntityPOShowRoomReturnProducts == null || resultEntityPOShowRoomReturnProducts.isEmpty())
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
                        //entityShowRoomStock.setPurchaseOrderNo(entityPurchaseOrder.getOrderNo());
                        entityShowRoomStock.setOrderNo(entityPurchaseOrder.getOrderNo());
                        entityShowRoomStocks.add(entityShowRoomStock);
                    }
                    List<EntityShowRoomStock> resultEntityShowRoomStocks = entityManagerShowRoomStock.saveOrUpdateShowRoomStocks(entityShowRoomStocks, session);                    
                    if(resultEntityShowRoomStocks == null || resultEntityShowRoomStocks.isEmpty())
                    {
                        tx.rollback();
                        return false;
                    }
                }                
                /*if(entityPurchaseOrder.getSupplierUserId() > 0)
                {
                    EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(this.appId);
                    EntitySupplier entitySupplier = entityManagerSupplier.getSupplierByUserId(entityPurchaseOrder.getSupplierUserId(), session);
                    if(entitySupplier != null && entitySupplier.getId() > 0)
                    {
                        double currentDue = this.getSupplierCurrentDue(entityPurchaseOrder.getSupplierUserId(), session);
                        //double currentDueOfOrder = currentEntityPurchaseOrder.getTotal() - currentEntityPurchaseOrder.getPaid();
                        //double newDueOfOrder = entityPurchaseOrder.getTotal() - entityPurchaseOrder.getPaid();
                        //entitySupplier.setBalance( - currentDueOfOrder + newDueOfOrder);
                        entitySupplier.setBalance(currentDue);
                        entityManagerSupplier.updateSupplier(entitySupplier, session);
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
     * This method will return purchase order by order no
     * @param orderNo purchase order no
     * @return EntityPurchaseOrder EntityPurchaseOrder
     */
    public EntityPurchaseOrder getPurchaseOrderByOrderNo(String orderNo)
    {
        if(StringUtils.isNullOrEmpty(orderNo))
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
    
    /**
     * This method will return purchase order by id
     * @param id id
     * @return EntityPurchaseOrder EntityPurchaseOrder
     */
    public EntityPurchaseOrder getPurchaseOrderById(int id)
    {
        if(id <= 0)
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
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
    
    public EntityPurchaseOrder getLastPurchaseOrder()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getLastPurchaseOrder");
            query.setMaxResults(1);
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
    
    /**
     * This method will return purchase order list
     * @param offset offset
     * @param limit limit
     * @return List entity purchase order list
     */
    public List<EntityPurchaseOrder> getPurchaseOrders(int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getAllPurchaseOrders");
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
     * This method will return total number of purchase orders
     * @return Integer total number of purchase orders
     */
    public int getTotalPurchaseOrders()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityPurchaseOrder> query = session.getNamedQuery("getAllPurchaseOrders");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total amount of purchase orders
     * @return double total amount of purchase orders
     */
    public double getTotalPurchaseAmount()
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        double totalPurchaseAmount = 0 ;
        try 
        {
            Query<Object> query = session.getNamedQuery("getTotalPurchaseAmount");
            List<Object> results = query.getResultList();
            for(Object result : results)
            {
                if(result != null)
                {
                    totalPurchaseAmount = (double)result;
                    break;
                } 
            }            
        } 
        finally 
        {
            session.close();
        }
        return totalPurchaseAmount;
    }
    
    /**
     * This method will return purchase order list search by order no, case insensitive
     * @param orderNo purchase order no
     * @param offset offset
     * @param limit limit
     * @return List entity purchase order list
     */
    public List<EntityPurchaseOrder> searchPurchaseOrderByOrderNo(String orderNo, int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityPurchaseOrder> query = session.getNamedQuery("searchPurchaseOrderByOrderNo");
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
     * This method will return total number of purchase orders search by order no, case insensitive
     * @param orderNo purchase order no
     * @return Integer total number of purchase orders
     */
    public int searchTotalPurchaseOrderByOrderNo(String orderNo)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityPurchaseOrder> query = session.getNamedQuery("searchPurchaseOrderByOrderNo");
            query.setParameter("orderNo", "%" + orderNo.toLowerCase() + "%");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total amount of purchase orders search by orderNo, case insensitive
     * @param orderNo Order No
     * @return double total amount of purchase orders
     */
    public double searchTotalPurchaseAmountByOrderNo(String orderNo)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        double totalPurchaseAmount = 0 ;
        try 
        {
            Query<Object> query = session.getNamedQuery("searchTotalPurchaseAmountByOrderNo");
            query.setParameter("orderNo", "%" + orderNo.toLowerCase() + "%");
            List<Object> results = query.getResultList();
            for(Object result : results)
            {
                if(result != null)
                {
                    totalPurchaseAmount = (double)result;
                    break;
                } 
            }            
        } 
        finally 
        {
            session.close();
        }
        return totalPurchaseAmount;
    }
    
    /**
     * This method will return purchase order list search by cell, case insensitive
     * @param cell cell no
     * @param offset offset
     * @param limit limit
     * @return List entity purchase order list
     */
    public List<EntityPurchaseOrder> searchPurchaseOrderByCell(String cell, int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityPurchaseOrder> query = session.getNamedQuery("searchPurchaseOrderByCell");
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
     * This method will return total number of purchase orders search by cell, case insensitive
     * @param cell cell no
     * @return Integer total number of purchase orders
     */
    public int searchTotalPurchaseOrderByCell(String cell)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntityPurchaseOrder> query = session.getNamedQuery("searchPurchaseOrderByCell");
            query.setParameter("cell", "%" + cell.toLowerCase() + "%");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return total amount of purchase orders search by cell, case insensitive
     * @param cell cell no
     * @return double total amount of purchase orders
     */
    public double searchTotalPurchaseAmountByCell(String cell)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        double totalPurchaseAmount = 0 ;
        try 
        {
            Query<Object> query = session.getNamedQuery("searchTotalPurchaseAmountByCell");
            query.setParameter("cell", "%" + cell.toLowerCase() + "%");
            List<Object> results = query.getResultList();
            for(Object result : results)
            {
                if(result != null)
                {
                    totalPurchaseAmount = (double)result;
                    break;
                }                
            }            
        } 
        finally 
        {
            session.close();
        }
        return totalPurchaseAmount;
    }
    
    /**
     * This method will update purchase order supplier info using session
     * @param entityPurchaseOrder entity purchase order
     * @param session session
     * @return boolean true
     */
    public int updatePurchaseOrderSupplierInfo(EntityPurchaseOrder entityPurchaseOrder, Session session)
    {
        Query<EntityPurchaseOrder> query = session.getNamedQuery("updatePurchaseOrderSupplierInfo");
        query.setParameter("supplierName", entityPurchaseOrder.getSupplierName());
        query.setParameter("cell", entityPurchaseOrder.getCell());
        query.setParameter("email", entityPurchaseOrder.getEmail());
        query.setParameter("supplierUserId", entityPurchaseOrder.getSupplierUserId());
        return query.executeUpdate();
    }
    
    /**
     * This method will update purchase order paid amount by order no using session
     * @param paid paid amount
     * @param orderNo purchase order no
     * @param session session
     * @return boolean true
     */
    public int updatePurchaseOrderPaidByOrderNo(double paid, String orderNo, Session session)
    {
        Query<EntityPurchaseOrder> query = session.getNamedQuery("updatePurchaseOrderPaidByOrderNo");
        query.setParameter("paid", paid);
        query.setParameter("orderNo", orderNo);
        return query.executeUpdate();
    }
    
    // --------------------------- Dynamic Query Section Starts ----------------------------------//
    public List<EntityPurchaseOrder> getPurchaseOrdersDQ(long startTime, long endTime, int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            String where = " where created_on >= " + startTime + " AND created_on <= " + endTime + " ";
            Query query = session.createSQLQuery("select {epo.*} from purchase_orders epo " + where + " order by created_on desc limit :limit offset :offset")
                    .addEntity("epo",EntityPurchaseOrder.class)
                    .setInteger("limit", limit)
                    .setInteger("offset", offset);
            return query.list();           
        } 
        finally 
        {
            session.close();
        }
    }
    
    public int getTotalPurchaseOrdersDQ(long startTime, long endTime)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            String where = " where created_on >= " + startTime + " AND created_on <= " + endTime + " ";
            Query query = session.createSQLQuery("select {epo.*} from purchase_orders epo " + where)
                    .addEntity("epo",EntityPurchaseOrder.class);
            return query.list().size();           
        } 
        finally 
        {
            session.close();
        }
    }
    
    public double getTotalPurchaseAmountDQ(long startTime, long endTime)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        double totalPurchaseAmount = 0 ;
        try 
        {
            String where = " where created_on >= " + startTime + " AND created_on <= " + endTime + " ";
            Query query = session.createSQLQuery("select sum(total) from purchase_orders " + where);
            List<Object> results = query.getResultList();
            for(Object result : results)
            {
                if(result != null)
                {
                    totalPurchaseAmount = (double)result;
                    break;
                }                
            }            
        } 
        finally 
        {
            session.close();
        }
        return totalPurchaseAmount;
    }
}
