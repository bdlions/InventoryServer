package org.bdlions.inventory.library;

import com.bdlions.dto.response.GeneralResponse;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.manager.Supplier;
import org.bdlions.inventory.util.Constants;

/**
 *
 * @author Nazmul Hasan
 */
public class SupplierLibrary {
    public DTOSupplier createSupplier(DTOSupplier dtoSupplier)
    {
        //GeneralResponse response = new GeneralResponse();
        EntityUserRole entityUserRole = new EntityUserRole();
        entityUserRole.setRoleId(Constants.ROLE_ID_SUPPLIER);
        dtoSupplier.setEntityUserRole(entityUserRole);
        Supplier supplier = new Supplier();
        //check whether supplier identity exists or not
        DTOSupplier resultDTOSupplier = supplier.createSupplier(dtoSupplier);
        if(resultDTOSupplier != null && resultDTOSupplier.getEntitySupplier().getId() > 0)
        {
            resultDTOSupplier.setSuccess(true);
            resultDTOSupplier.setMessage("Supplier is added successfully.");
        }
        else
        {
            resultDTOSupplier = new DTOSupplier();            
            resultDTOSupplier.setSuccess(false);
            resultDTOSupplier.setMessage("Unable to create a supplier. Please try again later.");
        }
        return resultDTOSupplier;
    }
    
    public GeneralResponse updateSupplier(DTOSupplier dtoSupplier)
    {
        GeneralResponse response = new GeneralResponse();
        Supplier supplier = new Supplier();
        //check whether supplier identity exists or not
        if(supplier.updateSupplier(dtoSupplier))
        {
            response.setSuccess(true);
            response.setMessage("Supplier is updated successfully.");
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Unable to update supplier. Please try again later.");
        }
        return response;
    }
}
