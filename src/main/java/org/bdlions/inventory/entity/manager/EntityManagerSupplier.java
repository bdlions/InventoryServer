package org.bdlions.inventory.entity.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.inventory.entity.EntitySupplier;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class EntityManagerSupplier {
    private final Logger logger = LoggerFactory.getLogger(EntityManagerSupplier.class);
    
    public EntitySupplier createSupplier(EntitySupplier entitySupplier)
    {
        EntitySupplier resultEntitySupplier = null;
        Session session = HibernateUtil.getSession();
        try 
        {
            resultEntitySupplier = this.createSupplier(entitySupplier, session);
        } 
        finally 
        {
            session.close();
        }
        return resultEntitySupplier;
    }
    
    public EntitySupplier createSupplier(EntitySupplier entitySupplier, Session session)
    {
        try
        {
            session.save(entitySupplier);
            return entitySupplier;
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return null;
    }
    
    public boolean updateSupplier(EntitySupplier entitySupplier)
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        try 
        {
            status = this.updateSupplier(entitySupplier, session);
        } 
        finally 
        {
            session.close();
        }
        return status;
    }
    
    public boolean updateSupplier(EntitySupplier entitySupplier, Session session)
    {
        try
        {
            session.update(entitySupplier);
            return true;
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        return false;
    }
    
    public EntitySupplier getSupplierBySupplierId(EntitySupplier entitySupplier)
    {
        EntitySupplier resultEntitySupplier = new EntitySupplier();
        Session session = HibernateUtil.getSession();
        try 
        {            
            if(entitySupplier != null && entitySupplier.getId() > 0)
            {
                Query<EntitySupplier> query = session.getNamedQuery("getSupplierBySupplierId");
                query.setParameter("supplierId", entitySupplier.getId());
                resultEntitySupplier = query.getSingleResult();
            }                     
        } 
        finally 
        {
            session.close();
        }
        return resultEntitySupplier;
    }
    
    public EntitySupplier getSupplierByUserId(EntitySupplier entitySupplier)
    {
        EntitySupplier resultEntitySupplier = new EntitySupplier();
        Session session = HibernateUtil.getSession();
        try 
        {            
            if(entitySupplier != null && entitySupplier.getUserId() > 0)
            {
                Query<EntitySupplier> query = session.getNamedQuery("getSupplierByUserId");
                query.setParameter("userId", entitySupplier.getUserId());
                resultEntitySupplier = query.getSingleResult();
            }                     
        } 
        finally 
        {
            session.close();
        }
        return resultEntitySupplier;
    }
    
    public List<EntitySupplier> getSuppliers(DTOSupplier dtoSupplier) {
        List<EntitySupplier> entitySuppliers = new ArrayList<>();
        Session session = HibernateUtil.getSession();
        try 
        {
            //set limit, offset and other params in named query
            Query<EntitySupplier> query = session.getNamedQuery("getSuppliers");
            entitySuppliers = query.getResultList();            
        } 
        finally 
        {
            session.close();
        }
        return entitySuppliers;
    }
}
