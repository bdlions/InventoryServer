package org.bdlions.inventory.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.entity.manager.EntityManagerSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.entity.manager.EntityManagerUserRole;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class Supplier {
    private final Logger logger = LoggerFactory.getLogger(Supplier.class);
    
    public DTOSupplier createSupplier(DTOSupplier dtoSupplier) 
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction(); 
        try 
        {            
            if (dtoSupplier != null && dtoSupplier.getEntityUser() != null && dtoSupplier.getEntityUserRole() != null && dtoSupplier.getEntitySupplier() != null) 
            {
                tx.begin();
                EntityManagerUser entityManagerUser = new EntityManagerUser();
                EntityUser entityUser = entityManagerUser.createUser(dtoSupplier.getEntityUser(), session);
                if(entityUser != null && entityUser.getId() > 0)
                {
                    dtoSupplier.getEntityUserRole().setUserId(entityUser.getId());
                    EntityManagerUserRole entityManagerUserRole = new EntityManagerUserRole();
                    EntityUserRole entityUserRole = entityManagerUserRole.createUserRole(dtoSupplier.getEntityUserRole(), session); 
                    if(entityUserRole != null && entityUserRole.getId() > 0)
                    {
                        dtoSupplier.setEntityUserRole(entityUserRole);
                        
                        dtoSupplier.getEntitySupplier().setUserId(entityUser.getId());
                        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
                        EntitySupplier entitySupplier = entityManagerSupplier.createSupplier(dtoSupplier.getEntitySupplier(), session);
                        if(entitySupplier != null && entitySupplier.getId() > 0)
                        {
                            dtoSupplier.setEntitySupplier(entitySupplier);
                            tx.commit();
                            status = true;
                        }
                    }
                }
                if(!status)
                {
                    tx.rollback();                
                }             
            }
        }
        catch(Exception ex){
            logger.error(ex.toString());
            tx.rollback();
        }
        finally {
            session.close();
        }
        if(status)
        {
            return dtoSupplier;
        }
        else
        {
            return null;
        }
    }
    
    public boolean updateSupplier(DTOSupplier dtoSupplier) 
    {
        boolean status = false;
        Session session = HibernateUtil.getSession();
        Transaction tx = session.getTransaction();
        try 
        {            
            if (dtoSupplier != null && dtoSupplier.getEntityUser() != null && dtoSupplier.getEntitySupplier() != null) 
            {                
                tx.begin();
                EntityManagerUser entityManagerUser = new EntityManagerUser();
                if(entityManagerUser.updateUser(dtoSupplier.getEntityUser(), session))
                {
                    //update user role if required.
                    
                    EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
                    if(entityManagerSupplier.updateSupplier(dtoSupplier.getEntitySupplier(), session))
                    {
                        tx.commit();
                        status = true;
                    }
                }
                if(!status)
                {
                    tx.rollback();
                }                
            }
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
            tx.rollback();
        }
        finally 
        {
            session.close();
        }
        return status;
    }
    
    public DTOSupplier getSupplierInfo(EntitySupplier reqEntitySupplier) 
    {
        DTOSupplier dtoSupplier = null;
        EntitySupplier entitySupplier = new EntitySupplier();
        if(reqEntitySupplier != null && reqEntitySupplier.getId() > 0)
        {
            EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
            entitySupplier = entityManagerSupplier.getSupplierBySupplierId(reqEntitySupplier);
        }
        else if(reqEntitySupplier != null && reqEntitySupplier.getUserId() > 0)
        {
            EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
            entitySupplier = entityManagerSupplier.getSupplierByUserId(reqEntitySupplier);
        }
        if(entitySupplier != null && entitySupplier.getId() > 0)
        {
            EntityUser reqEntityUser = new EntityUser();
            reqEntityUser.setId(entitySupplier.getUserId());
            EntityManagerUser entityManagerUser = new EntityManagerUser();
            EntityUser entityUser = entityManagerUser.getUserByUserId(reqEntityUser);
            if(entityUser != null && entityUser.getId() > 0)
            {
                //set user role if required
                dtoSupplier = new DTOSupplier();
                dtoSupplier.setEntitySupplier(entitySupplier);
                dtoSupplier.setEntityUser(entityUser);
            }            
        }           
        return dtoSupplier;
    }
    
    public List<DTOSupplier> getSuppliers(DTOSupplier dtoSupplier) 
    {
        List<DTOSupplier> suppliers = new ArrayList<>();
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
        List<EntitySupplier> entitySuppliers = entityManagerSupplier.getSuppliers(dtoSupplier);
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        for(EntitySupplier entitySupplier : entitySuppliers)
        {
            EntityUser reqEntityUser = new EntityUser();
            reqEntityUser.setId(entitySupplier.getUserId());
            EntityUser entityUser =  entityManagerUser.getUserByUserId(reqEntityUser);

            DTOSupplier tempDTOSupplier = new DTOSupplier();
            tempDTOSupplier.setEntitySupplier(entitySupplier);
            tempDTOSupplier.setEntityUser(entityUser);
            //set user role if required
            suppliers.add(tempDTOSupplier);
        }
        return suppliers;
    }
}
