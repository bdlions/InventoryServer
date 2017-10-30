package org.bdlions.inventory.library;

import com.bdlions.dto.response.GeneralResponse;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.manager.Product;

/**
 *
 * @author Nazmul Hasan
 */
public class ProductLibrary {
    public EntityProduct addProduct(EntityProduct entityProduct)
    {
        //GeneralResponse response = new GeneralResponse();
        Product product = new Product();
        //check whether product identity exists or not
        EntityProduct responseEntityProduct = product.createProduct(entityProduct);
        if(responseEntityProduct != null && responseEntityProduct.getId() > 0)
        {
            responseEntityProduct.setSuccess(true);
            responseEntityProduct.setMessage("Product is added successfully.");
        }
        else
        {
            responseEntityProduct = new EntityProduct();
            responseEntityProduct.setSuccess(false);
            responseEntityProduct.setMessage("Unable to add a new product. Please try again later.");
        }
        return responseEntityProduct;
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
