package org.bdlions.inventory.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class ListSaleOrder extends ClientResponse{
    private List<DTOSaleOrder> saleOrders = new ArrayList<>();
    //this is for totalSaleOrders under pagination
    private int totalSaleOrders;
    private double totalSaleAmount;

    public List<DTOSaleOrder> getSaleOrders() {
        return saleOrders;
    }

    public void setSaleOrders(List<DTOSaleOrder> saleOrders) {
        this.saleOrders = saleOrders;
    }

    public int getTotalSaleOrders() {
        return totalSaleOrders;
    }

    public void setTotalSaleOrders(int totalSaleOrders) {
        this.totalSaleOrders = totalSaleOrders;
    }

    public double getTotalSaleAmount() {
        return totalSaleAmount;
    }

    public void setTotalSaleAmount(double totalSaleAmount) {
        this.totalSaleAmount = totalSaleAmount;
    }
    
}
