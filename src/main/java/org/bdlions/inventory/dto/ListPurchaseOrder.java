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

    public List<DTOPurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(List<DTOPurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }
    
}
