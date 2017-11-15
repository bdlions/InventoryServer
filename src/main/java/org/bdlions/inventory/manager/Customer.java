package org.bdlions.inventory.manager;

import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.dto.DTOCustomer;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerCustomer;
import org.bdlions.inventory.entity.manager.EntityManagerUser;

/**
 *
 * @author Nazmul Hasan
 */
public class Customer 
{
    public List<DTOCustomer> getCustomers(int offset, int limit) 
    {
        List<DTOCustomer> customers = new ArrayList<>();
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
        List<EntityCustomer> entityCustomers = entityManagerCustomer.getCustomers(offset, limit);
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        for(EntityCustomer entityCustomer : entityCustomers)
        {
            EntityUser reqEntityUser = new EntityUser();
            reqEntityUser.setId(entityCustomer.getUserId());
            EntityUser entityUser =  entityManagerUser.getUserByUserId(reqEntityUser.getId());

            DTOCustomer tempDTOCustomer = new DTOCustomer();
            tempDTOCustomer.setEntityCustomer(entityCustomer);
            tempDTOCustomer.setEntityUser(entityUser);
            customers.add(tempDTOCustomer);
        }        
        return customers;
    }
}
