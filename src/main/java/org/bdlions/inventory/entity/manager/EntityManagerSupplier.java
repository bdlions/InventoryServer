package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityProductSupplier;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntityPurchaseOrderPayment;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.util.Constants;
import org.bdlions.inventory.util.TimeUtils;
import org.bdlions.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerSupplier 
{
    private int appId;
    public EntityManagerSupplier(int appId)
    {
        this.appId = appId;
    }
    
    /**
     * This method will return entity supplier by supplier id
     * @param supplierId supplier id
     * @return EntitySupplier EntitySupplier
     */
    public EntitySupplier getSupplierBySupplierId(int supplierId)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            Query<EntitySupplier> query = session.getNamedQuery("getSupplierBySupplierId");
            query.setParameter("supplierId", supplierId);
            List<EntitySupplier> supplierList = query.getResultList();
            if(supplierList == null || supplierList.isEmpty())
            {
                return null;
            }
            else
            {
                return supplierList.get(0);
            }
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity supplier by user id
     * @param userId user id
     * @return EntitySupplier EntitySupplier
     */
    public EntitySupplier getSupplierByUserId(int userId)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {            
            return this.getSupplierByUserId(userId, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntitySupplier getSupplierByUserId(int userId, Session session)
    {
        Query<EntitySupplier> query = session.getNamedQuery("getSupplierByUserId");
        query.setParameter("userId", userId);
        List<EntitySupplier> supplierList = query.getResultList();
        if(supplierList == null || supplierList.isEmpty())
        {
            return null;
        }
        else
        {
            return supplierList.get(0);
        } 
    }
    
    /**
     * This method will create new entity supplier using session
     * @param entitySupplier entity supplier
     * @param session session
     * @return EntitySupplier EntitySupplier
     */
    public EntitySupplier createSupplier(EntitySupplier entitySupplier, Session session)
    {
        TimeUtils timeUtils = new TimeUtils();
        entitySupplier.setCreatedOn(timeUtils.getCurrentTime());
        entitySupplier.setModifiedOn(timeUtils.getCurrentTime());
        session.save(entitySupplier);
        return entitySupplier;
    }
    
    /**
     * This method will create new entity supplier
     * @param entitySupplier entity supplier
     * @return EntitySupplier EntitySupplier
     */
    public EntitySupplier createSupplier(EntitySupplier entitySupplier)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return createSupplier(entitySupplier, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will create new entity supplier and/or entity user and/or entity user role
     * @param entitySupplier entity supplier
     * @param entityUser entity user
     * @param entityUserRole entity user role
     * @param suplierProducts supplier product list
     * @param entityPurchaseOrderPaymentIn supplier last year due
     * @return EntitySupplier EntitySupplier
     */
    public EntitySupplier createSupplier(EntitySupplier entitySupplier, EntityUser entityUser, EntityUserRole entityUserRole, List<EntityProductSupplier> suplierProducts, EntityPurchaseOrderPayment entityPurchaseOrderPaymentIn)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();        
        try 
        {
            if(entityUser != null)
            {
                EntityManagerUser entityManagerUser = new EntityManagerUser(this.appId);
                EntityUser resultEntityUser = entityManagerUser.createUser(entityUser, entityUserRole, session);
                entitySupplier.setUserId(resultEntityUser.getId());
                
                if(entityPurchaseOrderPaymentIn != null)
                {
                    entityPurchaseOrderPaymentIn.setSupplierUserId(entitySupplier.getUserId());
                    entityPurchaseOrderPaymentIn.setSupplierName(entitySupplier.getSupplierName());
                    entityPurchaseOrderPaymentIn.setUnixPaymentDate(TimeUtils.getCurrentTime());
                    entityPurchaseOrderPaymentIn.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                    EntityManagerPurchaseOrderPayment entityManagerPurchaseOrderPayment = new EntityManagerPurchaseOrderPayment(appId);
                    entityManagerPurchaseOrderPayment.createPurchaseOrderPayment(entityPurchaseOrderPaymentIn, session);
                    
                    //setting supplier current balance because of purchase order payment table entry
                    double currentDue = entityManagerPurchaseOrderPayment.getSupplierCurrentDue( entitySupplier.getUserId(), session);
                    entitySupplier.setBalance(currentDue);
                }
                
                if(suplierProducts != null && !suplierProducts.isEmpty())
                {
                    EntityManagerProductSupplier entityManagerProductSupplier = new EntityManagerProductSupplier(this.appId);
                    for(EntityProductSupplier entityProductSupplier: suplierProducts)
                    {
                        entityProductSupplier.setSupplierUserId(resultEntityUser.getId());
                        entityProductSupplier.setSupplierUserName(resultEntityUser.getUserName());
                        entityManagerProductSupplier.addProductSupplier(entityProductSupplier, session);
                    }
                }
            }
            EntitySupplier resultEntitySupplier = createSupplier(entitySupplier, session);
            tx.commit();
            return resultEntitySupplier;
        } 
        finally 
        {
            session.close();
        }        
    }
    
    /**
     * This method will update entity supplier using session
     * @param entitySupplier entity supplier
     * @param session session
     * @return boolean true
     */
    public boolean updateSupplier(EntitySupplier entitySupplier, Session session)
    {
        TimeUtils timeUtils = new TimeUtils();
        entitySupplier.setModifiedOn(timeUtils.getCurrentTime());
        session.update(entitySupplier);
        return true;
    }
    
    /**
     * This method will update entity supplier
     * @param entitySupplier entity supplier
     * @return boolean true
     */
    public boolean updateSupplier(EntitySupplier entitySupplier)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            return updateSupplier(entitySupplier, session);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will update entity supplier and/or entity user and/or entity purchase order
     * @param entitySupplier entity supplier
     * @param entityUser entity user
     * @param entityPurchaseOrder entity purchase order
     * @param supplierProducts supplier products
     * @param productIds product id list
     * @param entityPurchaseOrderPaymentIn supplier last year balance
     * @return boolean true
     */
    public boolean updateSupplier(EntitySupplier entitySupplier, EntityUser entityUser, EntityPurchaseOrder entityPurchaseOrder, List<EntityProductSupplier> supplierProducts, List<Integer> productIds, EntityPurchaseOrderPayment entityPurchaseOrderPaymentIn)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();           
        try 
        {
            if(entityUser != null)
            {
                EntityManagerUser entityManagerUser = new EntityManagerUser(this.appId);
                entityManagerUser.updateUser(entityUser, session);
                
                EntityManagerProductSupplier entityManagerProductSupplier = new EntityManagerProductSupplier(this.appId);
                entityManagerProductSupplier.updateProductSupplierSupplierUserName(entityUser.getId(), entityUser.getUserName(), session);
                
                if(entityPurchaseOrderPaymentIn != null)
                {
                    EntityManagerPurchaseOrderPayment entityManagerPurchaseOrderPayment = new EntityManagerPurchaseOrderPayment(appId);
                    //delete current entry
                    entityManagerPurchaseOrderPayment.deleteSupplierPurchaseOrderPaymentsByPaymentTypeId(entitySupplier.getUserId(), Constants.PURCHASE_ORDER_PAYMENT_TYPE_ID_ADD_PREVIOUS_DUE_IN, session);
                    entityPurchaseOrderPaymentIn.setSupplierUserId(entitySupplier.getUserId());
                    entityPurchaseOrderPaymentIn.setSupplierName(entitySupplier.getSupplierName()); 
                    entityPurchaseOrderPaymentIn.setUnixPaymentDate(TimeUtils.getCurrentTime());
                    entityPurchaseOrderPaymentIn.setPaymentDate(TimeUtils.getCurrentDate("", ""));
                    entityManagerPurchaseOrderPayment.createPurchaseOrderPayment(entityPurchaseOrderPaymentIn, session);
                     //setting supplier current balance because of purchase order payment table entry
                    double currentDue = entityManagerPurchaseOrderPayment.getSupplierCurrentDue( entitySupplier.getUserId(), session);
                    entitySupplier.setBalance(currentDue);
                }
                
                if(supplierProducts != null && !supplierProducts.isEmpty())
                {
                    if(productIds != null && !productIds.isEmpty())
                    {
                        entityManagerProductSupplier.deleteSupplierProductsByProductIds(entityUser.getId(), productIds, session);
                    }                
                    for(EntityProductSupplier entityProductSupplier: supplierProducts)
                    {
                        entityProductSupplier.setSupplierUserId(entityUser.getId());
                        entityProductSupplier.setSupplierUserName(entityUser.getUserName());
                        entityManagerProductSupplier.addProductSupplier(entityProductSupplier, session);
                    }
                }
            }
            if(entityPurchaseOrder != null)
            {
                EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder(this.appId);
                entityManagerPurchaseOrder.updatePurchaseOrderSupplierInfo(entityPurchaseOrder, session);
                
                EntityManagerPurchaseOrderPayment entityManagerPurchaseOrderPayment = new EntityManagerPurchaseOrderPayment(appId);
                entityManagerPurchaseOrderPayment.updatePurchaseOrderPaymentSupplierInfo(entityPurchaseOrder.getSupplierUserId(), entityPurchaseOrder.getSupplierName(), session);
            }            
            updateSupplier(entitySupplier, session);
            tx.commit();
            return true;
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity supplier list
     * @param offset offset
     * @param limit limit
     * @return List entity supplier list
     */
    public List<EntitySupplier> getSuppliers(int offset, int limit) {
        List<EntitySupplier> entitySuppliers = new ArrayList<>();
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySupplier> query = session.getNamedQuery("getSuppliers");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            entitySuppliers = query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return entitySuppliers;
    }
    
    /**
     * This method will return entity supplier list
     * @return List entity supplier list
     */
    public List<EntitySupplier> getSuppliers() {
        List<EntitySupplier> entitySuppliers = new ArrayList<>();
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySupplier> query = session.getNamedQuery("getSuppliers");
            entitySuppliers = query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return entitySuppliers;
    }
    
    /**
     * This method will return total number of suppliers
     * @return Integer total number of suppliers
     */
    public int getTotalSuppliers() {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySupplier> query = session.getNamedQuery("getSuppliers");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
     /**
     * This method will return total due of all suppliers
     * @return double suppliers total due
     */
    public double getSuppliersTotalDue() {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<Object> query = session.getNamedQuery("getSuppliersTotalDue");
            List<Object> resultList = query.getResultList();
            if(resultList == null || resultList.isEmpty())
            {
                return 0;
            }
            return (double)resultList.get(0);
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity supplier list - search by name, case insensitive
     * @param supplierName supplier name
     * @param offset offset
     * @param limit limit
     * @return List entity supplier list
     */
    public List<EntitySupplier> searchSuppliersByName(String supplierName, int offset, int limit) {
        List<EntitySupplier> entitySuppliers = new ArrayList<>();
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySupplier> query = session.getNamedQuery("searchSupplierByName");
            query.setParameter("supplierName", "%" + supplierName.toLowerCase() + "%");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            entitySuppliers = query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return entitySuppliers;
    }
    
    /**
     * This method will return total number of supplier list - search by name, case insensitive
     * @param supplierName supplier name
     * @return Integer total number of suppliers
     */
    public int searchTotalSuppliersByName(String supplierName) {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySupplier> query = session.getNamedQuery("searchSupplierByName");
            query.setParameter("supplierName", "%" + supplierName.toLowerCase() + "%");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity supplier list - search by cell, case insensitive
     * @param cell supplier cell number
     * @param offset offset
     * @param limit limit
     * @return List entity supplier list
     */
    public List<EntitySupplier> searchSuppliersByCell(String cell, int offset, int limit) {
        List<EntitySupplier> entitySuppliers = new ArrayList<>();
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySupplier> query = session.getNamedQuery("searchSupplierByCell");
            query.setParameter("cell", "%" + cell.toLowerCase() + "%");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            entitySuppliers = query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return entitySuppliers;
    }
    
    /**
     * This method will return total number of supplier list - search by name, case insensitive
     * @param cell supplier cell number
     * @return Integer total number of suppliers
     */
    public int searchTotalSuppliersByCell(String cell) {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySupplier> query = session.getNamedQuery("searchSupplierByCell");
            query.setParameter("cell", "%" + cell.toLowerCase() + "%");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    /**
     * This method will return entity supplier list - search by email, case insensitive
     * @param email supplier email address
     * @param offset offset
     * @param limit limit
     * @return List entity supplier list
     */
    public List<EntitySupplier> searchSuppliersByEmail(String email, int offset, int limit) {
        List<EntitySupplier> entitySuppliers = new ArrayList<>();
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySupplier> query = session.getNamedQuery("searchSupplierByEmail");
            query.setParameter("email", "%" + email.toLowerCase() + "%");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            entitySuppliers = query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return entitySuppliers;
    }
    
    /**
     * This method will return total number of supplier list - search by email, case insensitive
     * @param email supplier email address
     * @return Integer total number of suppliers
     */
    public int searchTotalSuppliersByEmail(String email) {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        try 
        {
            Query<EntitySupplier> query = session.getNamedQuery("searchSupplierByEmail");
            query.setParameter("email", "%" + email.toLowerCase() + "%");
            return query.getResultList().size();            
        } 
        finally 
        {
            session.close();
        }
    }
    
    public EntityPurchaseOrderPayment addSupplierPurchasePayment(EntityPurchaseOrderPayment entityPurchaseOrderPaymentIn)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();           
        try 
        {
            if(entityPurchaseOrderPaymentIn != null)
            {
                EntityManagerPurchaseOrderPayment entityManagerPurchaseOrderPayment = new EntityManagerPurchaseOrderPayment(appId);
                entityPurchaseOrderPaymentIn = entityManagerPurchaseOrderPayment.createPurchaseOrderPayment(entityPurchaseOrderPaymentIn, session);
                 //setting supplier current balance because of purchase order payment table entry
                double currentDue = entityManagerPurchaseOrderPayment.getSupplierCurrentDue( entityPurchaseOrderPaymentIn.getSupplierUserId(), session);
                EntitySupplier entitySupplier = getSupplierByUserId(entityPurchaseOrderPaymentIn.getSupplierUserId());
                entitySupplier.setBalance(currentDue);
                updateSupplier(entitySupplier, session);
            }
            tx.commit();
            return entityPurchaseOrderPaymentIn;
        } 
        finally 
        {
            session.close();
        }
    }
    
    public boolean updateSupplierPurchasePayment(EntityPurchaseOrderPayment entityPurchaseOrderPaymentOut)
    {
        Session session = HibernateUtil.getInstance().getSession(this.appId);
        Transaction tx = session.getTransaction(); 
        tx.begin();           
        try 
        {
            if(entityPurchaseOrderPaymentOut != null)
            {
                EntityManagerPurchaseOrderPayment entityManagerPurchaseOrderPayment = new EntityManagerPurchaseOrderPayment(appId);
                entityManagerPurchaseOrderPayment.updatePurchaseOrderPayment(entityPurchaseOrderPaymentOut, session);
                 //setting supplier current balance because of purchase order payment table entry
                double currentDue = entityManagerPurchaseOrderPayment.getSupplierCurrentDue( entityPurchaseOrderPaymentOut.getSupplierUserId(), session);
                EntitySupplier entitySupplier = getSupplierByUserId(entityPurchaseOrderPaymentOut.getSupplierUserId());
                entitySupplier.setBalance(currentDue);
                updateSupplier(entitySupplier, session);
                
                if(!StringUtils.isNullOrEmpty(entityPurchaseOrderPaymentOut.getReference()))
                {
                    //updating paid in purchase order
                    EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder(appId);
                    entityManagerPurchaseOrder.updatePurchaseOrderPaidByOrderNo(entityPurchaseOrderPaymentOut.getAmountOut(), entityPurchaseOrderPaymentOut.getReference(), session);
                }                
            }
            tx.commit();
            return true;
        } 
        finally 
        {
            session.close();
        }
    }
}
