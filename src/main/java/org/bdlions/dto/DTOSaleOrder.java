package org.bdlions.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOSaleOrder extends ClientResponse implements java.io.Serializable{
    public int limit;
    public int offset;
    public EntitySaleOrder entitySaleOrder;
    public DTOCustomer dtoCustomer;
    public List<DTOProduct> products;
    public DTOSaleOrder()
    {
        entitySaleOrder = new EntitySaleOrder();
        dtoCustomer = new DTOCustomer();
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

    public EntitySaleOrder getEntitySaleOrder() {
        return entitySaleOrder;
    }

    public void setEntitySaleOrder(EntitySaleOrder entitySaleOrder) {
        this.entitySaleOrder = entitySaleOrder;
    }

    public DTOCustomer getDtoCustomer() {
        return dtoCustomer;
    }

    public void setDtoCustomer(DTOCustomer dtoCustomer) {
        this.dtoCustomer = dtoCustomer;
    }

    public List<DTOProduct> getProducts() {
        return products;
    }

    public void setProducts(List<DTOProduct> products) {
        this.products = products;
    }
    
}
