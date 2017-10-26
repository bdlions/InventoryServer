package org.bdlions.inventory.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class ListCustomer extends ClientResponse{
    private List<DTOCustomer> customers = new ArrayList<>();

    public List<DTOCustomer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<DTOCustomer> customers) {
        this.customers = customers;
    }

    
}
