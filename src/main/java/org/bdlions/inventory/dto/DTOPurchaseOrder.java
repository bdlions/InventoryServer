package org.bdlions.inventory.dto;

import org.bdlions.inventory.entity.EntityPurchaseOrder;
import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOPurchaseOrder extends ClientResponse implements java.io.Serializable{
    public int limit;
    public int offset;
    public EntityPurchaseOrder entityPurchaseOrder;
    public DTOSupplier dtoSupplier;
    public List<DTOProduct> products;
    public DTOPurchaseOrder()
    {
        entityPurchaseOrder = new EntityPurchaseOrder();
        dtoSupplier = new DTOSupplier();
        products = new ArrayList<>();
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public EntityPurchaseOrder getEntityPurchaseOrder() {
        return entityPurchaseOrder;
    }

    public void setEntityPurchaseOrder(EntityPurchaseOrder entityPurchaseOrder) {
        this.entityPurchaseOrder = entityPurchaseOrder;
    }

    public DTOSupplier getDtoSupplier() {
        return dtoSupplier;
    }

    public void setDtoSupplier(DTOSupplier dtoSupplier) {
        this.dtoSupplier = dtoSupplier;
    }

    public List<DTOProduct> getProducts() {
        return products;
    }

    public void setProducts(List<DTOProduct> products) {
        this.products = products;
    }
    
}
