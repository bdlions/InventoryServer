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

    public List<DTOSupplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<DTOSupplier> suppliers) {
        this.suppliers = suppliers;
    }

    

    
}
