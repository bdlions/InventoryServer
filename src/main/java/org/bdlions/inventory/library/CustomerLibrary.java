package org.bdlions.inventory.library;

import com.bdlions.dto.response.GeneralResponse;
import org.bdlions.inventory.dto.DTOCustomer;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.manager.Customer;
import org.bdlions.inventory.manager.Product;
import org.bdlions.inventory.util.Constants;

/**
 *
 * @author Nazmul Hasan
 */
public class CustomerLibrary {
    public GeneralResponse createCustomer(DTOCustomer dtoCustomer)
    {
        GeneralResponse response = new GeneralResponse();
        EntityUserRole entityUserRole = new EntityUserRole();
        entityUserRole.setRoleId(Constants.ROLE_ID_CUSTOMER);
        dtoCustomer.setEntityUserRole(entityUserRole);
        Customer customer = new Customer();
        //check whether customer identity exists or not
        if(customer.createCustomer(dtoCustomer))
        {
            response.setSuccess(true);
            response.setMessage("Customer is added successfully.");
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Unable to create a customer. Please try again later.");
        }
        return response;
    }
    
    public GeneralResponse updateCustomer(DTOCustomer dtoCustomer)
    {
        GeneralResponse response = new GeneralResponse();
        Customer customer = new Customer();
        //check whether customer identity exists or not
        if(customer.updateCustomer(dtoCustomer))
        {
            response.setSuccess(true);
            response.setMessage("Customer is updated successfully.");
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Unable to update customer. Please try again later.");
        }
        return response;
    }
}
