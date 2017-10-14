package org.bdlions.dto;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOProduct {
    public int limit;
    public int offset;
    public double quantity;
    public double discount;
    public double total;
    public EntityProductType entityProductType;
    public EntityProductCategory entityProductCategory;
    public EntityProduct entityProduct;
    public DTOProduct()
    {
        entityProductType = new EntityProductType();
        entityProductCategory = new EntityProductCategory();
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public EntityProduct getEntityProduct() {
        return entityProduct;
    }

    public void setEntityProduct(EntityProduct entityProduct) {
        this.entityProduct = entityProduct;
    }

    public EntityProductType getEntityProductType() {
        return entityProductType;
    }

    public void setEntityProductType(EntityProductType entityProductType) {
        this.entityProductType = entityProductType;
    }

    public EntityProductCategory getEntityProductCategory() {
        return entityProductCategory;
    }

    public void setEntityProductCategory(EntityProductCategory entityProductCategory) {
        this.entityProductCategory = entityProductCategory;
    }    
}
