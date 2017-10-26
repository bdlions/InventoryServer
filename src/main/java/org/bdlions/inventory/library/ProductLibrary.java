package org.bdlions.inventory.library;

import com.bdlions.dto.response.GeneralResponse;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.manager.Product;

/**
 *
 * @author Nazmul Hasan
 */
public class ProductLibrary {
    public GeneralResponse addProduct(EntityProduct entityProduct)
    {
        GeneralResponse response = new GeneralResponse();
        Product product = new Product();
        //check whether product identity exists or not
        if(product.createProduct(entityProduct))
        {
            response.setSuccess(true);
            response.setMessage("Product is added successfully.");
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Unable to add a new product. Please try again later.");
        }
        return response;
    }
    
    public GeneralResponse updateProduct(EntityProduct entityProduct)
    {
        GeneralResponse response = new GeneralResponse();
        Product product = new Product();
        //before updating product check whether product identity exists or not
        if(product.updateProduct(entityProduct))
        {
            response.setSuccess(true);
            response.setMessage("Product is updated successfully.");
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Unable to update product. Please try again later.");
        }
        return response;
    }
}
