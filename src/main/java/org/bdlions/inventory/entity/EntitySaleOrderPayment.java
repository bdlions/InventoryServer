package org.bdlions.inventory.entity;

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
        name = "sale_order_payments",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
        name = "getSaleOrderPaymentById",
        query = "from EntitySaleOrderPayment saleOrderPayment where saleOrderPayment.id = :id"
    ),@NamedQuery(
        name = "getCustomerSaleOrderPaymentsByPaymentTypeId",
        query = "from EntitySaleOrderPayment saleOrderPayment where saleOrderPayment.customerUserId = :customerUserId AND saleOrderPayment.paymentTypeId = :paymentTypeId"
    ),
    @NamedQuery(
        name = "getSaleOrderPaymentsByReference",
        query = "from EntitySaleOrderPayment saleOrderPayment where saleOrderPayment.reference = :reference"
    ),
    @NamedQuery(
        name = "updateSaleOrderPayment",
        query = "update EntitySaleOrderPayment set amountIn = :amountIn, amountOut = :amountOut, description = :description where id = :id"
    ),
    @NamedQuery(
        name = "deleteSaleOrderPaymentsByReference",
        query = " delete from EntitySaleOrderPayment saleOrderPayment where saleOrderPayment.reference = :reference"
    ),
    @NamedQuery(
        name = "deleteCustomerSaleOrderPaymentsByPaymentTypeId",
        query = " delete from EntitySaleOrderPayment saleOrderPayment where saleOrderPayment.customerUserId = :customerUserId AND saleOrderPayment.paymentTypeId = :paymentTypeId"
    ),
    @NamedQuery(
        name = "updateSaleOrderPaymentCustomerInfo",
        query = "update EntitySaleOrderPayment set customerName = :customerName where customerUserId = :customerUserId"
    ),
    @NamedQuery(
        name = "getCustomerCurrentDue",
        query = " select sum(amountIn - amountOut), customerUserId from EntitySaleOrderPayment entitySaleOrderPayment where entitySaleOrderPayment.customerUserId = :customerUserId group by customerUserId"
    ),
    @NamedQuery(
        name = "getCustomerSaleAndPaymentAmount",
        query = " select sum(amountIn), sum(amountOut) from EntitySaleOrderPayment entitySaleOrderPayment where entitySaleOrderPayment.customerUserId = :customerUserId AND entitySaleOrderPayment.paymentTypeId IN (:paymentTypeIds)"
    )
})
public class EntitySaleOrderPayment extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
    
    @Column(name = "customer_user_id", length = 11, columnDefinition = "int(11) DEFAULT 0")
    private int customerUserId;

    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "payment_type_id", nullable = false, columnDefinition = "int default 1")
    private int paymentTypeId;
    
    @Column(name = "reference", length = 200)
    private String reference;

    @Column(name = "amount_in", columnDefinition = "double DEFAULT 0")
    private double amountIn;
    
    @Column(name = "amount_out", columnDefinition = "double DEFAULT 0")
    private double amountOut;
    
    @Column(name = "description", length = 2000)
    private String description = "";
    
    @Column(name = "payment_date", length = 200)
    private String paymentDate = "";
    
    @Column(name = "unix_payment_date", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long unixPaymentDate;
    
    @Column(name = "created_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long createdOn;

    @Column(name = "modified_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long modifiedOn;
    
    @Column(name = "created_by_user_id", length = 11, columnDefinition = "int(11) DEFAULT 0")
    private int createdByUserId;

    @Column(name = "created_by_user_name")
    private String createdByUserName;
    
    @Column(name = "modified_by_user_id", length = 11, columnDefinition = "int(11) DEFAULT 0")
    private int modifiedByUserId;

    @Column(name = "modified_by_user_name")
    private String modifiedByUserName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerUserId() {
        return customerUserId;
    }

    public void setCustomerUserId(int customerUserId) {
        this.customerUserId = customerUserId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(int paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getAmountIn() {
        return amountIn;
    }

    public void setAmountIn(double amountIn) {
        this.amountIn = amountIn;
    }

    public double getAmountOut() {
        return amountOut;
    }

    public void setAmountOut(double amountOut) {
        this.amountOut = amountOut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public long getUnixPaymentDate() {
        return unixPaymentDate;
    }

    public void setUnixPaymentDate(long unixPaymentDate) {
        this.unixPaymentDate = unixPaymentDate;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(long modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public int getModifiedByUserId() {
        return modifiedByUserId;
    }

    public void setModifiedByUserId(int modifiedByUserId) {
        this.modifiedByUserId = modifiedByUserId;
    }

    public String getModifiedByUserName() {
        return modifiedByUserName;
    }

    public void setModifiedByUserName(String modifiedByUserName) {
        this.modifiedByUserName = modifiedByUserName;
    }

    
    
    
}
