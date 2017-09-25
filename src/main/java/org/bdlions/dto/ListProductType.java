/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class ListProductType extends ClientResponse{
    private List<EntityProductType> productTypes = new ArrayList<>();

    public List<EntityProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<EntityProductType> productTypes) {
        this.productTypes = productTypes;
    }
    
}
