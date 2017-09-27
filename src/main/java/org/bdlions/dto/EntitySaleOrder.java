package org.bdlions.dto;

import com.bdlions.dto.response.ClientResponse;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author nazmul hasan
 */
@Entity
@Table(
        name = "sale_orders",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getSaleOrderByOrderNo",
            query = "from EntitySaleOrder saleOrder where saleOrder.orderNo = :orderNo"
    ),
    @NamedQuery(
            name = "getAllSaleOrders",
            query = "from EntitySaleOrder saleOrder"
    )
})
public class EntitySaleOrder extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
   
    @Column(name = "order_no", length = 200)
    private String orderNo;
    
    @Column(name = "customer_user_id", columnDefinition = "int(11) NOT NULL")
    private int customerUserId;
    
    @Column(name = "status_id", columnDefinition = "int(11) NOT NULL")
    private int statusId;
    
    @Column(name = "sale_date", columnDefinition = "int(11) NOT NULL")
    private int saleDate;
    
    @Column(name = "discount")
    private double discount;
    
    @Column(name = "remarks", length = 500)
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

    public int getCustomerUserId() {
        return customerUserId;
    }

    public void setCustomerUserId(int customerUserId) {
        this.customerUserId = customerUserId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(int saleDate) {
        this.saleDate = saleDate;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
