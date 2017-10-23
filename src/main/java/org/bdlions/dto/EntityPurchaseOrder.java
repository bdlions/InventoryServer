package org.bdlions.dto;

import com.bdlions.dto.response.ClientResponse;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author nazmul hasan
 */
@Entity
@Table(
        name = "purchase_orders",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getPurchaseOrderById",
            query = "from EntityPurchaseOrder purchaseOrder where purchaseOrder.id = :id"
    ),
    @NamedQuery(
            name = "getPurchaseOrderByOrderNo",
            query = "from EntityPurchaseOrder purchaseOrder where purchaseOrder.orderNo = :orderNo"
    ),
    @NamedQuery(
            name = "getAllPurchaseOrders",
            query = "from EntityPurchaseOrder purchaseOrder"
    )
})
public class EntityPurchaseOrder extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
  
    @Column(name = "order_no", length = 200)
    private String orderNo;
    
    @Column(name = "supplier_user_id", columnDefinition = "int(11) NOT NULL")
    private int supplierUserId;
    
    @Column(name = "order_date", columnDefinition = "int(11) unsigned DEFAULT 0")
    private int orderDate;

    @Column(name = "requested_ship_date", columnDefinition = "int(11) unsigned DEFAULT 0")
    private int requestedShipDate;
    
    @Column(name = "subtotal", columnDefinition = "int(11) unsigned DEFAULT 0")
    private double subtotal;
    
    @Column(name = "discount", columnDefinition = "int(11) unsigned DEFAULT 0")
    private double discount;
    
    @Column(name = "total", columnDefinition = "int(11) unsigned DEFAULT 0")
    private double total;
    
    @Column(name = "paid", columnDefinition = "int(11) unsigned DEFAULT 0")
    private double paid;
    
    @Column(name = "remarks", length = 1000)
    private String remarks;
    
    @Column(name = "created_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private int createdOn;

    @Column(name = "modified_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private int modifiedOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getSupplierUserId() {
        return supplierUserId;
    }

    public void setSupplierUserId(int supplierUserId) {
        this.supplierUserId = supplierUserId;
    }

    public int getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(int orderDate) {
        this.orderDate = orderDate;
    }

    public int getRequestedShipDate() {
        return requestedShipDate;
    }

    public void setRequestedShipDate(int requestedShipDate) {
        this.requestedShipDate = requestedShipDate;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(int createdOn) {
        this.createdOn = createdOn;
    }

    public int getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(int modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    
}
