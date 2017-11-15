package org.bdlions.inventory.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerUser;

/**
 *
 * @author Nazmul Hasan
 */
public class Supplier 
{
    public List<DTOSupplier> getSuppliers(int offset, int limit) 
    {
        List<DTOSupplier> suppliers = new ArrayList<>();
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
        List<EntitySupplier> entitySuppliers = entityManagerSupplier.getSuppliers(offset, limit);
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        for(EntitySupplier entitySupplier : entitySuppliers)
        {
            EntityUser reqEntityUser = new EntityUser();
            reqEntityUser.setId(entitySupplier.getUserId());
            EntityUser entityUser =  entityManagerUser.getUserByUserId(reqEntityUser.getId());

            DTOSupplier tempDTOSupplier = new DTOSupplier();
            tempDTOSupplier.setEntitySupplier(entitySupplier);
            tempDTOSupplier.setEntityUser(entityUser);
            //set user role if required
            suppliers.add(tempDTOSupplier);
        }
        return suppliers;
    }
}
