package org.bdlions.inventory.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.entity.EntityAdjustStockOrder;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOAdjustStockOrder extends ClientResponse implements java.io.Serializable{
    private int limit;
    private int offset;
    private EntityAdjustStockOrder entityAdjustStockOrder;
    private List<DTOProduct> products;
    private String adjustDate;
    public DTOAdjustStockOrder()
    {
        entityAdjustStockOrder = new EntityAdjustStockOrder();
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

    public EntityAdjustStockOrder getEntityAdjustStockOrder() {
        return entityAdjustStockOrder;
    }

    public void setEntityAdjustStockOrder(EntityAdjustStockOrder entityAdjustStockOrder) {
        this.entityAdjustStockOrder = entityAdjustStockOrder;
    }

    public List<DTOProduct> getProducts() {
        return products;
    }

    public void setProducts(List<DTOProduct> products) {
        this.products = products;
    }

    public String getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(String adjustDate) {
        this.adjustDate = adjustDate;
    }
}
