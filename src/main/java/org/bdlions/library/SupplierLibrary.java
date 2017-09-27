package org.bdlions.library;

import com.bdlions.dto.response.GeneralResponse;
import org.bdlions.dto.DTOSupplier;
import org.bdlions.manager.Supplier;

/**
 *
 * @author Nazmul Hasan
 */
public class SupplierLibrary {
    public GeneralResponse createSupplier(DTOSupplier dtoSupplier)
    {
        GeneralResponse response = new GeneralResponse();
        Supplier supplier = new Supplier();
        //check whether supplier identity exists or not
        if(supplier.createSupplier(dtoSupplier))
        {
            response.setSuccess(true);
            response.setMessage("Supplier is added successfully.");
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Unable to create a supplier. Please try again later.");
        }
        return response;
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
