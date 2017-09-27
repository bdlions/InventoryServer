package org.bdlions.dto;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOProduct {
    public int limit;
    public int offset;
    public double quantity;
    public EntityProduct entityProduct;
    public DTOProduct()
    {
        entityProduct = new EntityProduct();
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public EntityProduct getEntityProduct() {
        return entityProduct;
    }

    public void setEntityProduct(EntityProduct entityProduct) {
        this.entityProduct = entityProduct;
    }
    
}
