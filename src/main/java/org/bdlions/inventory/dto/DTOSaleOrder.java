package org.bdlions.inventory.dto;

import org.bdlions.inventory.entity.EntitySaleOrder;
import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOSaleOrder extends ClientResponse implements java.io.Serializable{
    private int limit;
    private int offset;
    private EntitySaleOrder entitySaleOrder;
    private DTOCustomer dtoCustomer;
    private List<DTOProduct> products;
    private List<DTOProduct> returnProducts;
    private String orderDate;
    public DTOSaleOrder()
    {
        entitySaleOrder = new EntitySaleOrder();
        dtoCustomer = new DTOCustomer();
        products = new ArrayList<>();
        returnProducts = new ArrayList<>();
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<DTOProduct> getReturnProducts() {
        return returnProducts;
    }

    public void setReturnProducts(List<DTOProduct> returnProducts) {
        this.returnProducts = returnProducts;
    }    
}
