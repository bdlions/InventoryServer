package org.bdlions.inventory.dto;

import org.bdlions.inventory.entity.EntityPurchaseOrderPayment;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOPurchaseOrderPayment {
    private EntityPurchaseOrderPayment entityPurchaseOrderPayment;
    private String createdDate;
    
    public DTOPurchaseOrderPayment()
    {
        entityPurchaseOrderPayment = new EntityPurchaseOrderPayment();
    }

    public EntityPurchaseOrderPayment getEntityPurchaseOrderPayment() {
        return entityPurchaseOrderPayment;
    }

    public void setEntityPurchaseOrderPayment(EntityPurchaseOrderPayment entityPurchaseOrderPayment) {
        this.entityPurchaseOrderPayment = entityPurchaseOrderPayment;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    
}
