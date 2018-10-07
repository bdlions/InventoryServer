package org.bdlions.inventory.dto;

import org.bdlions.inventory.entity.EntitySaleOrderPayment;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOSaleOrderPayment {
    private EntitySaleOrderPayment entitySaleOrderPayment;
    private String createdDate;
    
    public DTOSaleOrderPayment()
    {
        entitySaleOrderPayment = new EntitySaleOrderPayment();
    }

    public EntitySaleOrderPayment getEntitySaleOrderPayment() {
        return entitySaleOrderPayment;
    }

    public void setEntitySaleOrderPayment(EntitySaleOrderPayment entitySaleOrderPayment) {
        this.entitySaleOrderPayment = entitySaleOrderPayment;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    
}
