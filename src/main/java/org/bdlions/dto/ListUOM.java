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
public class ListUOM extends ClientResponse{
    private List<EntityUOM> uoms = new ArrayList<>();

    public List<EntityUOM> getUoms() {
        return uoms;
    }

    public void setUoms(List<EntityUOM> uoms) {
        this.uoms = uoms;
    }
    
}
