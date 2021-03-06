package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntityPurchaseOrderPayment;
import org.bdlions.inventory.util.Constants;
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
public class EntityManagerPurchaseOrderPayment {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerPurchaseOrderPayment.class);
    private int appId;
    public EntityManagerPurchaseOrderPayment(int appId)
    {
        this.appId = appId;
    }
    
    public EntityPurchaseOrderPayment createPurchaseOrderPayment(EntityPurchaseOrderPayment entityPurchaseOrderPayment, Session session)
    {
        entityPurchaseOrderPayment.setCreatedOn(TimeUtils.getCurrentTime());
        entityPurchaseOrderPayment.setModifiedOn(TimeUtils.getCurrentTime());
        session.save(entityPurchaseOrderPayment);
        return entityPurchaseOrderPayment;
    }
    
    public EntityPurchaseOrderPayment createPurchaseOrderPayment(EntityPurchaseOrderPayment entityPurchaseOrderPayment)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return createPurchaseOrderPayment(entityPurchaseOrderPayment, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public boolean updatePurchaseOrderPayment(EntityPurchaseOrderPayment entityPurchaseOrderPayment, Session session)
    {
        entityPurchaseOrderPayment.setModifiedOn(TimeUtils.getCurrentTime());
        session.update(entityPurchaseOrderPayment);
        return true;
    }
    
    public boolean updatePurchaseOrderPayment(EntityPurchaseOrderPayment entityPurchaseOrderPayment)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return updatePurchaseOrderPayment(entityPurchaseOrderPayment, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityPurchaseOrderPayment> getPurchaseOrderPaymentListByReference(String reference)
    {
        if(StringUtils.isNullOrEmpty(reference))
        {
            return null;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityPurchaseOrderPayment> query = session.getNamedQuery("getPurchaseOrderPaymentsByReference");
            query.setParameter("reference", reference);
            List<EntityPurchaseOrderPayment> purchaseOrderPaymentist = query.getResultList();
            if(purchaseOrderPaymentist == null || purchaseOrderPaymentist.isEmpty())
            {
                return null;
            }
            else
            {
                return purchaseOrderPaymentist;
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityPurchaseOrderPayment getSupplierPurchaseOrderPaymentByPaymentTypeId(int supplierUserId, int paymentTypeId)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityPurchaseOrderPayment> query = session.getNamedQuery("getSupplierPurchaseOrderPaymentsByPaymentTypeId");
            query.setParameter("supplierUserId", supplierUserId);
            query.setParameter("paymentTypeId", paymentTypeId);
            List<EntityPurchaseOrderPayment> purchaseOrderPaymentist = query.getResultList();
            if(purchaseOrderPaymentist == null || purchaseOrderPaymentist.isEmpty())
            {
                return null;
            }
            else
            {
                return purchaseOrderPaymentist.get(0);
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    public List<EntityPurchaseOrderPayment> getSupplierPurchaseOrderPaymentListByPaymentTypeId(int supplierUserId, int paymentTypeId)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityPurchaseOrderPayment> query = session.getNamedQuery("getSupplierPurchaseOrderPaymentsByPaymentTypeId");
            query.setParameter("supplierUserId", supplierUserId);
            query.setParameter("paymentTypeId", paymentTypeId);
            List<EntityPurchaseOrderPayment> purchaseOrderPaymentist = query.getResultList();
            if(purchaseOrderPaymentist == null || purchaseOrderPaymentist.isEmpty())
            {
                return null;
            }
            else
            {
                return purchaseOrderPaymentist;
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    public EntityPurchaseOrderPayment getPurchaseOrderPaymentById(int id)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntityPurchaseOrderPayment> query = session.getNamedQuery("getPurchaseOrderPaymentById");
            query.setParameter("id", id);
            List<EntityPurchaseOrderPayment> purchaseOrderPaymentist = query.getResultList();
            if(purchaseOrderPaymentist == null || purchaseOrderPaymentist.isEmpty())
            {
                return null;
            }
            else
            {
                return purchaseOrderPaymentist.get(0);
            } 
        } 
        finally 
        {
            session.close();
        }
    }
    
    public int deletePurchaseOrderPaymentsByReference(String reference, Session session)
    {
        if(!StringUtils.isNullOrEmpty(reference))
        {
            Query<EntityPurchaseOrderPayment> query = session.getNamedQuery("deletePurchaseOrderPaymentsByReference");
            query.setParameter("reference", reference);
            return query.executeUpdate();
        }
        return 0;
    }
    
    public int deleteSupplierPurchaseOrderPaymentsByPaymentTypeId(int supplierUserId, int paymentTypeId, Session session)
    {
        if(supplierUserId > 0 && paymentTypeId > 0)
        {
            Query<EntityPurchaseOrderPayment> query = session.getNamedQuery("deleteSupplierPurchaseOrderPaymentsByPaymentTypeId");
            query.setParameter("supplierUserId", supplierUserId);
            query.setParameter("paymentTypeId", paymentTypeId);
            return query.executeUpdate();
        }
        return 0;
    }
    
    public int updatePurchaseOrderPaymentSupplierInfo(int supplierUserId, String supplierName, Session session)
    {
        Query<EntityPurchaseOrder> query = session.getNamedQuery("updatePurchaseOrderPaymentSupplierInfo");
        query.setParameter("supplierName", supplierName);
        query.setParameter("supplierUserId", supplierUserId);
        return query.executeUpdate();
    }
    
    public double getSupplierCurrentDue(int supplierUserId, Session session)
    {
        if(supplierUserId <= 0)
        {
            return 0;
        }
        double currentDue = 0;
        Query<Object[]> query = session.getNamedQuery("getSupplierCurrentDue");
        query.setParameter("supplierUserId", supplierUserId);
        List<Object[]> purchaseOrderList = query.getResultList();
        if(purchaseOrderList == null || purchaseOrderList.isEmpty())
        {
            return 0;
        }
        else
        {
            try
            {
                currentDue = (double)purchaseOrderList.get(0)[0];
            }
            catch(Exception ex)
            {
                logger.error(ex.toString());
                currentDue = 0;
            }
        } 
        return currentDue;
    }
    
    public double getSupplierCurrentDue(int supplierUserId)
    {
        if(supplierUserId <= 0)
        {
            return 0;
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            return this.getSupplierCurrentDue(supplierUserId, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public DTOSupplier getSupplierPurchaseAndPaymentAmount(int supplierUserId, Session session)
    {
        DTOSupplier dtoSupplier = new DTOSupplier();
        if(supplierUserId <= 0)
        {
            return dtoSupplier;
        }
        List<Integer> paymentTypeIds = new ArrayList<>();
        paymentTypeIds.add(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ID_ADD_PURCHASE_IN);
        paymentTypeIds.add(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ID_PURCHASE_PAYMENT_OUT);
        paymentTypeIds.add(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ID_ADD_NEW_PAYMENT_OUT);
        Query<Object[]> query = session.getNamedQuery("getSupplierPurchaseAndPaymentAmount");
        query.setParameter("supplierUserId", supplierUserId);
        query.setParameter("paymentTypeIds", paymentTypeIds);
        List<Object[]> paymentList = query.getResultList();
        if(paymentList == null || paymentList.isEmpty())
        {
            return dtoSupplier;
        }
        else
        {
            try
            {
                dtoSupplier.setTotalPurchaseAmount((double)paymentList.get(0)[0]);
                dtoSupplier.setTotalPaymentAmount((double)paymentList.get(0)[1]);
            }
            catch(Exception ex)
            {
                logger.error(ex.toString());  
                return new DTOSupplier();
            }
        } 
        return dtoSupplier;
    }
    
    public DTOSupplier getSupplierPurchaseAndPaymentAmount(int supplierUserId)
    {
        if(supplierUserId <= 0)
        {
            return new DTOSupplier();
        }
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            return this.getSupplierPurchaseAndPaymentAmount(supplierUserId, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    // --------------------------- Dynamic Query Section Starts ----------------------------------//
    // We need dynamic query because for some query, search params are dynamic i.e. param may be included or not
    public List<EntityPurchaseOrderPayment> getPurchaseOrderPaymentsDQ(List<Integer> paymentTypeIds, int supplierUserId, long startTime, long endTime, long paymentStartTime, long paymentEndTime, int offset, int limit)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            String whereQuery = "";
            if(supplierUserId > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " supplier_user_id = " + supplierUserId;
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
            
            String whereInQuery = "";
            if(paymentTypeIds != null && !paymentTypeIds.isEmpty())
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereInQuery += " AND ";
                }
                else 
                {
                    whereInQuery += " where ";
                }
                whereInQuery += " payment_type_id in (";
                for(int counter = 0; counter < paymentTypeIds.size(); counter++)
                {
                    int id = paymentTypeIds.get(counter);
                    if( counter > 0)
                    {
                        whereInQuery += ",";
                    }
                    whereInQuery += id;
                }
                whereInQuery += ")";
            }
            
            Query query = session.createSQLQuery("select {epop.*} from purchase_order_payments epop " + whereQuery + whereInQuery + " order by created_on desc limit :limit offset :offset ")
                    .addEntity("epop",EntityPurchaseOrderPayment.class)
                    .setInteger("limit", limit)
                    .setInteger("offset", offset);
            return query.list();           
        } 
        finally 
        {
            session.close();
        }
    }
    public int getTotalPurchaseOrderPaymentsDQ(List<Integer> paymentTypeIds, int supplierUserId, long startTime, long endTime, long paymentStartTime, long paymentEndTime)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            String whereQuery = "";
            if(supplierUserId > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " supplier_user_id = " + supplierUserId;
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
            
            String whereInQuery = "";
            if(paymentTypeIds != null && !paymentTypeIds.isEmpty())
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereInQuery += " AND ";
                }
                else 
                {
                    whereInQuery += " where ";
                }
                whereInQuery += " payment_type_id in (";
                for(int counter = 0; counter < paymentTypeIds.size(); counter++)
                {
                    int id = paymentTypeIds.get(counter);
                    if( counter > 0)
                    {
                        whereInQuery += ",";
                    }
                    whereInQuery += id;
                }
                whereInQuery += ")";
            }
            
            Query query = session.createSQLQuery("select {epop.*} from purchase_order_payments epop " + whereQuery + whereInQuery)
                    .addEntity("epop",EntityPurchaseOrderPayment.class);
            return query.list().size();           
        } 
        finally 
        {
            session.close();
        }
    }
    public double getTotalPaymentAmountDQ(List<Integer> paymentTypeIds, int supplierUserId, long startTime, long endTime, long paymentStartTime, long paymentEndTime)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        double totalPurchasePaymentAmount = 0 ;
        try 
        {
            String whereQuery = "";
            if(supplierUserId > 0)
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereQuery += " AND ";
                }
                whereQuery += " supplier_user_id = " + supplierUserId;
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
            
            String whereInQuery = "";
            if(paymentTypeIds != null && !paymentTypeIds.isEmpty())
            {
                if(!StringUtils.isNullOrEmpty(whereQuery))
                {
                    whereInQuery += " AND ";
                }
                else 
                {
                    whereInQuery += " where ";
                }
                whereInQuery += " payment_type_id in (";
                for(int counter = 0; counter < paymentTypeIds.size(); counter++)
                {
                    int id = paymentTypeIds.get(counter);
                    if( counter > 0)
                    {
                        whereInQuery += ",";
                    }
                    whereInQuery += id;
                }
                whereInQuery += ")";
            }
            
            Query query = session.createSQLQuery("select sum(amount_out) from purchase_order_payments " + whereQuery + whereInQuery);
            List<Object> results = query.getResultList();
            for(Object result : results)
            {
                if(result != null)
                {
                    totalPurchasePaymentAmount = (double)result;
                    break;
                }                
            }         
        } 
        finally 
        {
            session.close();
        }
        return totalPurchasePaymentAmount;
    }
}
