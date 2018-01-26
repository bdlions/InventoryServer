package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderProduct;
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
        Session session = HibernateUtil.getSession();
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
    public EntitySaleOrder createSaleOrder(EntitySaleOrder entitySaleOrder, List<EntitySaleOrderProduct> entitySaleOrderProductList, List<EntityShowRoomStock> entityShowRoomStockList)
    {
        boolean status = true;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try 
        {
            entitySaleOrder = createSaleOrder(entitySaleOrder, session);
            if(entitySaleOrder != null && !StringUtils.isNullOrEmpty(entitySaleOrder.getOrderNo()))
            {
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
                    EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct();
                    List<EntitySaleOrderProduct> resultEntitySaleOrderProducts = entityManagerSaleOrderProduct.addSaleOrderProducts(entitySaleOrderProducts, session);
                    if(resultEntitySaleOrderProducts == null || resultEntitySaleOrderProducts.isEmpty())
                    {
                        status = false;
                    }
                }
                if(entityShowRoomStockList != null && !entityShowRoomStockList.isEmpty())
                {
                    List<EntityShowRoomStock> entityShowRoomStocks = new ArrayList<>();
                    //for each item, set sale order no into list
                    for(int counter = 0; counter < entityShowRoomStockList.size(); counter++)
                    {
                        EntityShowRoomStock entityShowRoomStock = entityShowRoomStockList.get(counter);
                        entityShowRoomStock.setSaleOrderNo(entitySaleOrder.getOrderNo());
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
                    EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
                    EntityCustomer entityCustomer = entityManagerCustomer.getCustomerByUserId(entitySaleOrder.getCustomerUserId(), session);
                    entityCustomer.setBalance(this.getCustomerrCurrentDue(entitySaleOrder.getCustomerUserId(), session));
                    entityManagerCustomer.updateCustomer(entityCustomer, session);
                    tx.commit();
                    return entitySaleOrder;
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
        Session session = HibernateUtil.getSession();
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
    public boolean updateSaleOrder(EntitySaleOrder currentEntitySaleOrder, EntitySaleOrder entitySaleOrder, List<EntitySaleOrderProduct> entitySaleOrderProductList, List<EntityShowRoomStock> entityShowRoomStockList)
    {
        boolean status = true;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        tx.begin();
        try 
        {
            if(entitySaleOrder != null && entitySaleOrder.getId() > 0 && updateSaleOrder(entitySaleOrder, session))
            {
                EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct();
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                //deleting existing products
                if(!StringUtils.isNullOrEmpty(entitySaleOrder.getOrderNo()))
                {
                    entityManagerSaleOrderProduct.deleteSaleOrderProductsByOrderNo(currentEntitySaleOrder.getOrderNo(), session);
                    entityManagerShowRoomStock.deleteShowRoomProductsBySaleOrderNoAndTransactionCategoryId(currentEntitySaleOrder.getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT, session);
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
                        entityShowRoomStock.setSaleOrderNo(entitySaleOrder.getOrderNo());
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
                    EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
                    EntityCustomer entityCustomer = entityManagerCustomer.getCustomerByUserId(entitySaleOrder.getCustomerUserId(), session);
                    entityCustomer.setBalance(this.getCustomerrCurrentDue(entitySaleOrder.getCustomerUserId(), session));
                    entityManagerCustomer.updateCustomer(entityCustomer, session);
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
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
     * This method will return sale order list search by order no, case insensitive
     * @param orderNo sale order no
     * @param offset offset
     * @param limit limit
     * @return List entity sale order list
     */
    public List<EntitySaleOrder> searchSaleOrderByOrderNo(String orderNo, int offset, int limit)
    {
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
     * This method will return sale order list search by cell, case insensitive
     * @param cell cell no
     * @param offset offset
     * @param limit limit
     * @return List entity sale order list
     */
    public List<EntitySaleOrder> searchSaleOrderByCell(String cell, int offset, int limit)
    {
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
    
    public double getCustomerrCurrentDue(int customerUserId, Session session)
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
    
    public double getCustomerrCurrentDue(int customerUserId)
    {
        if(customerUserId <= 0)
        {
            return 0;
        }
        Session session = HibernateUtil.getSession();
        try 
        {            
            return this.getCustomerrCurrentDue(customerUserId, session);
        } 
        finally 
        {
            session.close();
        }
    }
}
