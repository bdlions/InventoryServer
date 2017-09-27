package org.bdlions.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class ListSaleOrder extends ClientResponse{
    private List<DTOSaleOrder> saleOrders = new ArrayList<>();

    public List<DTOSaleOrder> getSaleOrders() {
        return saleOrders;
    }

    public void setSaleOrders(List<DTOSaleOrder> saleOrders) {
        this.saleOrders = saleOrders;
    }
    
}
