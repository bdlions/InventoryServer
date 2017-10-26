package org.bdlions.inventory.dto;

import org.bdlions.inventory.entity.EntityProduct;
import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class ListProduct extends ClientResponse{
    private List<EntityProduct> products = new ArrayList<>();

    public List<EntityProduct> getProducts() {
        return products;
    }

    public void setProducts(List<EntityProduct> products) {
        this.products = products;
    }
}
