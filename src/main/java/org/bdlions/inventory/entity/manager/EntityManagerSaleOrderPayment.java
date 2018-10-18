package org.bdlions.inventory.entity.manager;

import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderPayment;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerSaleOrderPayment {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerSaleOrderPayment.class);
    private int appId;
    public EntityManagerSaleOrderPayment(int appId)
    {
        this.appId = appId;
    }
    
    public EntitySaleOrderPayment createSaleOrderPayment(EntitySaleOrderPayment entitySaleOrderPayment, Session session)
    {
        entitySaleOrderPayment.setCreatedOn(TimeUtils.getCurrentTime());
        entitySaleOrderPayment.setModifiedOn(TimeUtils.getCurrentTime());
        session.save(entitySaleOrderPayment);
        return entitySaleOrderPayment;
    }
    
    public EntitySaleOrderPayment createSaleOrderPayment(EntitySaleOrderPayment entitySaleOrderPayment)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return createSaleOrderPayment(entitySaleOrderPayment, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public boolean updateSaleOrderPayment(EntitySaleOrderPayment entitySaleOrderPayment, Session session)
    {
        entitySaleOrderPayment.setModifiedOn(TimeUtils.getCurrentTime());
        session.update(entitySaleOrderPayment);
        return true;
    }
    
    public boolean updateSaleOrderPayment(EntitySaleOrderPayment entitySaleOrderPayment)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return updateSaleOrderPayment(entitySaleOrderPayment, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntitySaleOrderPayment> getSaleOrderPaymentListByReference(String reference)
    {
        if(StringUtils.isNullOrEmpty(reference))
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntitySaleOrderPayment> query = session.getNamedQuery("getSaleOrderPaymentsByReference");
            query.setParameter("reference", reference);
            List<EntitySaleOrderPayment> saleOrderPaymentist = query.getResultList();
            if(saleOrderPaymentist == null || saleOrderPaymentist.isEmpty())
            {
                return null;
            }
            else
            {
                return saleOrderPaymentist;
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntitySaleOrderPayment getCustomerSaleOrderPaymentByPaymentTypeId(int customerUserId, int paymentTypeId)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntitySaleOrderPayment> query = session.getNamedQuery("getCustomerSaleOrderPaymentsByPaymentTypeId");
            query.setParameter("customerUserId", customerUserId);
            query.setParameter("paymentTypeId", paymentTypeId);
            List<EntitySaleOrderPayment> saleOrderPaymentist = query.getResultList();
            if(saleOrderPaymentist == null || saleOrderPaymentist.isEmpty())
            {
                return null;
            }
            else
            {
                return saleOrderPaymentist.get(0);
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntitySaleOrderPayment> getCustomerSaleOrderPaymentListByPaymentTypeId(int customerUserId, int paymentTypeId)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntitySaleOrderPayment> query = session.getNamedQuery("getCustomerSaleOrderPaymentsByPaymentTypeId");
            query.setParameter("customerUserId", customerUserId);
            query.setParameter("paymentTypeId", paymentTypeId);
            List<EntitySaleOrderPayment> saleOrderPaymentist = query.getResultList();
            if(saleOrderPaymentist == null || saleOrderPaymentist.isEmpty())
            {
                return null;
            }
            else
            {
                return saleOrderPaymentist;
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    public EntitySaleOrderPayment getSaleOrderPaymentById(int id)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntitySaleOrderPayment> query = session.getNamedQuery("getSaleOrderPaymentById");
            query.setParameter("id", id);
            List<EntitySaleOrderPayment> saleOrderPaymentist = query.getResultList();
            if(saleOrderPaymentist == null || saleOrderPaymentist.isEmpty())
            {
                return null;
            }
            else
            {
                return saleOrderPaymentist.get(0);
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    public int deleteSaleOrderPaymentsByReference(String reference, Session session)
    {
        if(!StringUtils.isNullOrEmpty(reference))
        {
            Query<EntitySaleOrderPayment> query = session.getNamedQuery("deleteSaleOrderPaymentsByReference");
            query.setParameter("reference", reference);
            return query.executeUpdate();
        }
        return 0;
    }
    
    public int deleteCustomerSaleOrderPaymentsByPaymentTypeId(int customerUserId, int paymentTypeId, Session session)
    {
        if(customerUserId > 0 && paymentTypeId > 0)
        {
            Query<EntitySaleOrderPayment> query = session.getNamedQuery("deleteCustomerSaleOrderPaymentsByPaymentTypeId");
            query.setParameter("customerUserId", customerUserId);
            query.setParameter("paymentTypeId", paymentTypeId);
            return query.executeUpdate();
        }
        return 0;
    }
    
    public int updateSaleOrderPaymentCustomerInfo(int customerUserId, String customerName, Session session)
    {
        Query<EntitySaleOrder> query = session.getNamedQuery("updateSaleOrderPaymentCustomerInfo");
        query.setParameter("customerName", customerName);
        query.setParameter("customerUserId", customerUserId);
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
    
    public double getCustomerCurrentDue(int customerUserId)
    {
        if(customerUserId <= 0)
        {
            return 0;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            return this.getCustomerCurrentDue(customerUserId, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    // --------------------------- Dynamic Query Section Starts ----------------------------------//
    // We need dynamic query because for some query, search params are dynamic i.e. param may be included or not
    public List<EntitySaleOrderPayment> getSaleOrderPaymentsDQ(int customerUserId, int paymentTypeId, long startTime, long endTime, long paymentStartTime, long paymentEndTime, int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            String whereQuery = "";
            if(customerUserId > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " customer_user_id = " + customerUserId;
            }
            if(paymentTypeId > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " payment_type_id = " + paymentTypeId;
            }
            if(startTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " created_on >= " + startTime;
            }
            if(endTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " created_on <= " + endTime;
            }
            if(paymentStartTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " unix_payment_date >= " + paymentStartTime;
            }
            if(paymentEndTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " unix_payment_date <= " + paymentEndTime;
            }
            if(!StringUtils.isNullOrEmpty(whereQuery))
            {
                whereQuery = " where " + whereQuery;
            }
            
            Query query = session.createSQLQuery("select {esop.*} from sale_order_payments esop " + whereQuery + " order by created_on desc limit :limit offset :offset ")
                    .addEntity("esop",EntitySaleOrderPayment.class)
                    .setInteger("limit", limit)
                    .setInteger("offset", offset);
            return query.list();           
        } 
        finally 
        {
            session.close();
        }
    }
    public int getTotalSaleOrderPaymentsDQ(int customerUserId, int paymentTypeId, long startTime, long endTime, long paymentStartTime, long paymentEndTime)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            String whereQuery = "";
            if(customerUserId > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " customer_user_id = " + customerUserId;
            }
            if(paymentTypeId > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " payment_type_id = " + paymentTypeId;
            }
            if(startTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " created_on >= " + startTime;
            }
            if(endTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " created_on <= " + endTime;
            }
            if(paymentStartTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " unix_payment_date >= " + paymentStartTime;
            }
            if(paymentEndTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " unix_payment_date <= " + paymentEndTime;
            }
            if(!StringUtils.isNullOrEmpty(whereQuery))
            {
                whereQuery = " where " + whereQuery;
            }
            
            Query query = session.createSQLQuery("select {esop.*} from sale_order_payments esop " + whereQuery)
                    .addEntity("esop",EntitySaleOrderPayment.class);
            return query.list().size();           
        } 
        finally 
        {
            session.close();
        }
    }
    public double getTotalPaymentAmountDQ(int customerUserId, int paymentTypeId, long startTime, long endTime, long paymentStartTime, long paymentEndTime)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        double totalSalePaymentAmount = 0 ;
        try 
        {
            String whereQuery = "";
            if(customerUserId > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " customer_user_id = " + customerUserId;
            }
            if(paymentTypeId > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " payment_type_id = " + paymentTypeId;
            }
            if(startTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " created_on >= " + startTime;
            }
            if(endTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " created_on <= " + endTime;
            }
            if(paymentStartTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " unix_payment_date >= " + paymentStartTime;
            }
            if(paymentEndTime > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " unix_payment_date <= " + paymentEndTime;
            }
            if(!StringUtils.isNullOrEmpty(whereQuery))
            {
                whereQuery = " where " + whereQuery;
            }
            
            Query query = session.createSQLQuery("select sum(amount_out) from sale_order_payments " + whereQuery);
            List<Object> results = query.getResultList();
            for(Object result : results)
            {
                if(result != null)
                {
                    totalSalePaymentAmount = (double)result;
                    break;
                }                
            }        
        } 
        finally 
        {
            session.close();
        }
        return totalSalePaymentAmount;
    }
}
