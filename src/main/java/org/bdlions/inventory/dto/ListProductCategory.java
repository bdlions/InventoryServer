package org.bdlions.inventory.dto;

import org.bdlions.inventory.entity.EntityProductCategory;
import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class ListProductCategory extends ClientResponse{
    private List<EntityProductCategory> productCategories = new ArrayList<>();

    public List<EntityProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<EntityProductCategory> productCategories) {
        this.productCategories = productCategories;
    }    
}
