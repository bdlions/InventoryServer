package org.bdlions.inventory.dto;

import java.util.List;
import org.bdlions.inventory.entity.EntityProductCategory;
import org.bdlions.inventory.entity.EntityProductType;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityProductSupplier;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOProduct {
    private int limit;
    private int offset;
    private double quantity;
    private double discount;
    private double total;
    private EntityProductType entityProductType;
    private EntityProductCategory entityProductCategory;
    private EntityProduct entityProduct;
    private List<EntityProductSupplier> entityProductSupplierList;
    private List<EntityProductSupplier> epsListToBeDeleted;
    private String createdOn;
    private String modifiedOn;
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

    public List<EntityProductSupplier> getEntityProductSupplierList() {
        return entityProductSupplierList;
    }

    public void setEntityProductSupplierList(List<EntityProductSupplier> entityProductSupplierList) {
        this.entityProductSupplierList = entityProductSupplierList;
    }  

    public List<EntityProductSupplier> getEpsListToBeDeleted() {
        return epsListToBeDeleted;
    }

    public void setEpsListToBeDeleted(List<EntityProductSupplier> epsListToBeDeleted) {
        this.epsListToBeDeleted = epsListToBeDeleted;
    } 

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
    
}
