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
    //this is for totalCustomers under pagination
    private int totalCustomers;
    private double totalDue;

    public List<DTOCustomer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<DTOCustomer> customers) {
        this.customers = customers;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(double totalDue) {
        this.totalDue = totalDue;
    }
    
}
