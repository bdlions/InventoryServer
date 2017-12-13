package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.util.TimeUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerSupplier 
{
    /**
     * This method will return entity supplier by supplier id
     * @param supplierId supplier id
     * @return EntitySupplier EntitySupplier
     */
    public EntitySupplier getSupplierBySupplierId(int supplierId)
    {
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
        try 
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
        finally 
        {
            session.close();
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
        Session session = HibernateUtil.getSession();
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
     * @return EntitySupplier EntitySupplier
     */
    public EntitySupplier createSupplier(EntitySupplier entitySupplier, EntityUser entityUser, EntityUserRole entityUserRole)
    {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        tx.begin();
        if(entityUser != null)
        {
            EntityManagerUser entityManagerUser = new EntityManagerUser();
            EntityUser resultEntityUser = entityManagerUser.createUser(entityUser, entityUserRole, session);
            entitySupplier.setUserId(resultEntityUser.getId());
        }
        try 
        {
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
        Session session = HibernateUtil.getSession();
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
     * This method will update entity supplier and/or entity user
     * @param entitySupplier entity supplier
     * @param entityUser entity user
     * @return boolean true
     */
    public boolean updateSupplier(EntitySupplier entitySupplier, EntityUser entityUser)
    {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        tx.begin();
        if(entityUser != null)
        {
            EntityManagerUser entityManagerUser = new EntityManagerUser();
            entityManagerUser.updateUser(entityUser, session);
        }
        try 
        {
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
        Session session = HibernateUtil.getSession();
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
     * This method will return total number of suppliers
     * @return Integer total number of suppliers
     */
    public int getTotalSuppliers() {
        Session session = HibernateUtil.getSession();
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
     * This method will return entity supplier list - search by name, case insensitive
     * @param supplierName supplier name
     * @param offset offset
     * @param limit limit
     * @return List entity supplier list
     */
    public List<EntitySupplier> searchSuppliersByName(String supplierName, int offset, int limit) {
        List<EntitySupplier> entitySuppliers = new ArrayList<>();
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
        Session session = HibernateUtil.getSession();
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
}
