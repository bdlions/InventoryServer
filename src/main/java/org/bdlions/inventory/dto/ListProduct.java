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
    //this is for totalProducts under pagination
    private int totalProducts;

    public List<EntityProduct> getProducts() {
        return products;
    }

    public void setProducts(List<EntityProduct> products) {
        this.products = products;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }
}
