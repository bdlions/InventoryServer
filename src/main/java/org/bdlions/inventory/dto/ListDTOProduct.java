package org.bdlions.inventory.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class ListDTOProduct extends ClientResponse{
    private List<DTOProduct> products = new ArrayList<>();
    //this is for totalProducts under pagination
    private int totalProducts;
    private double totalAmount;

    public List<DTOProduct> getProducts() {
        return products;
    }

    public void setProducts(List<DTOProduct> products) {
        this.products = products;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }        
}
