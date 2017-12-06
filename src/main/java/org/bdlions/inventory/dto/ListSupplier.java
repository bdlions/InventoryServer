package org.bdlions.inventory.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class ListSupplier extends ClientResponse{
    private List<DTOSupplier> suppliers = new ArrayList<>();
    //this is for totalSuppliers under pagination
    private int totalSuppliers;

    public List<DTOSupplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<DTOSupplier> suppliers) {
        this.suppliers = suppliers;
    }

    public int getTotalSuppliers() {
        return totalSuppliers;
    }

    public void setTotalSuppliers(int totalSuppliers) {
        this.totalSuppliers = totalSuppliers;
    }
}
