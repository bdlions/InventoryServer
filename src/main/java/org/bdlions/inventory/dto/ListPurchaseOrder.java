package org.bdlions.inventory.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class ListPurchaseOrder extends ClientResponse{
    private List<DTOPurchaseOrder> purchaseOrders = new ArrayList<>();    
    //this is for totalPurchaseOrders under pagination
    private int totalPurchaseOrders;

    public List<DTOPurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(List<DTOPurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public int getTotalPurchaseOrders() {
        return totalPurchaseOrders;
    }

    public void setTotalPurchaseOrders(int totalPurchaseOrders) {
        this.totalPurchaseOrders = totalPurchaseOrders;
    }
    
}
