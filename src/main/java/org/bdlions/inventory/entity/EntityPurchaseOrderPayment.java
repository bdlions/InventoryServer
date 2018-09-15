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
        name = "purchase_order_payments",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
        name = "getPurchaseOrderPaymentById",
        query = "from EntityPurchaseOrderPayment purchaseOrderPayment where purchaseOrderPayment.id = :id"
    ),@NamedQuery(
        name = "getSupplierPurchaseOrderPaymentsByPaymentTypeId",
        query = "from EntityPurchaseOrderPayment purchaseOrderPayment where purchaseOrderPayment.supplierUserId = :supplierUserId AND purchaseOrderPayment.paymentTypeId = :paymentTypeId"
    ),
    @NamedQuery(
        name = "getPurchaseOrderPaymentsByReference",
        query = "from EntityPurchaseOrderPayment purchaseOrderPayment where purchaseOrderPayment.reference = :reference"
    ),
    @NamedQuery(
        name = "updatePurchaseOrderPayment",
        query = "update EntityPurchaseOrderPayment set amountIn = :amountIn, amountOut = :amountOut, description = :description where id = :id"
    ),
    @NamedQuery(
        name = "deletePurchaseOrderPaymentsByReference",
        query = " delete from EntityPurchaseOrderPayment purchaseOrderPayment where purchaseOrderPayment.reference = :reference"
    ),
    @NamedQuery(
        name = "deleteSupplierPurchaseOrderPaymentsByPaymentTypeId",
        query = " delete from EntityPurchaseOrderPayment purchaseOrderPayment where purchaseOrderPayment.supplierUserId = :supplierUserId AND purchaseOrderPayment.paymentTypeId = :paymentTypeId"
    ),
    @NamedQuery(
            name = "updatePurchaseOrderPaymentSupplierInfo",
            query = "update EntityPurchaseOrderPayment set supplierName = :supplierName where supplierUserId = :supplierUserId"
    ),
    @NamedQuery(
            name = "getSupplierCurrentDue",
            query = " select sum(amountIn - amountOut), supplierUserId from EntityPurchaseOrderPayment entityPurchaseOrderPayment where entityPurchaseOrderPayment.supplierUserId = :supplierUserId group by supplierUserId"
    )
})
public class EntityPurchaseOrderPayment extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
    
    @Column(name = "supplier_user_id", length = 11, columnDefinition = "int(11) DEFAULT 0")
    private int supplierUserId;

    @Column(name = "supplier_name")
    private String supplierName;
    
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

    public int getSupplierUserId() {
        return supplierUserId;
    }

    public void setSupplierUserId(int supplierUserId) {
        this.supplierUserId = supplierUserId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
