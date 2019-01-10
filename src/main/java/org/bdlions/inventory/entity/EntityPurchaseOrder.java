package org.bdlions.inventory.entity;

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

/**
 *
 * @author nazmul hasan
 */
@Entity
@Table(
        name = "purchase_orders",
        indexes = {
            @Index(name = "idx_purchase_order_no", columnList = "order_no"),
            @Index(name = "idx_supplier_name", columnList = "supplier_name"),
            @Index(name = "idx_supplier_cell", columnList = "cell"),
            @Index(name = "idx_supplier_email", columnList = "email")
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
            name = "getLastPurchaseOrder",
            query = "from EntityPurchaseOrder purchaseOrder order by purchaseOrder.id desc"
    ),
    @NamedQuery(
            name = "getAllPurchaseOrders",
            query = "from EntityPurchaseOrder purchaseOrder order by purchaseOrder.modifiedOn desc"
    ),
    @NamedQuery(
            name = "getTotalPurchaseAmount",
            query = "select sum(total) from EntityPurchaseOrder purchaseOrder"
    ),
    @NamedQuery(
            name = "searchPurchaseOrderByOrderNo",
            query = "from EntityPurchaseOrder purchaseOrder where lower(purchaseOrder.orderNo) like :orderNo"
    ),
    @NamedQuery(
            name = "searchTotalPurchaseAmountByOrderNo",
            query = "select sum(total) from EntityPurchaseOrder purchaseOrder where lower(purchaseOrder.orderNo) like :orderNo"
    ),
    @NamedQuery(
            name = "searchPurchaseOrderByCell",
            query = "from EntityPurchaseOrder purchaseOrder where lower(purchaseOrder.cell) like :cell"
    ),
    @NamedQuery(
            name = "searchTotalPurchaseAmountByCell",
            query = "select sum(total) from EntityPurchaseOrder purchaseOrder where lower(purchaseOrder.cell) like :cell"
    ),
    @NamedQuery(
            name = "updatePurchaseOrderSupplierInfo",
            query = "update EntityPurchaseOrder set supplierName = :supplierName, cell = :cell, email = :email where supplierUserId = :supplierUserId"
    ),
    @NamedQuery(
            name = "updatePurchaseOrderPaidByOrderNo",
            query = "update EntityPurchaseOrder set paid = :paid where orderNo = :orderNo"
    )
//    ,
//    @NamedQuery(
//            name = "getSupplierCurrentDue",
//            query = " select sum(total - paid), supplierUserId from EntityPurchaseOrder purchaseOrder where purchaseOrder.supplierUserId = :supplierUserId group by supplierUserId"
//    )
})
public class EntityPurchaseOrder extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
  
    @Column(name = "order_no", length = 200)
    private String orderNo;
    
    @Column(name = "next_order_no", columnDefinition = "int(11) DEFAULT 1")
    private int nextOrderNo;
    
    @Column(name = "supplier_user_id", columnDefinition = "int(11) NOT NULL")
    private int supplierUserId;
    
    @Column(name = "supplier_name")
    private String supplierName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "cell")
    private String cell;
    
    @Column(name = "invoice_on", columnDefinition = "int(11) unsigned DEFAULT 0")
    private long invoiceOn;

    @Column(name = "requested_ship_date", columnDefinition = "int(11) unsigned DEFAULT 0")
    private int requestedShipDate;
    
    @Column(name = "subtotal", columnDefinition = "double DEFAULT 0")
    private double subtotal;
    
    @Column(name = "discount", columnDefinition = "double DEFAULT 0")
    private double discount;
    
    @Column(name = "total_return", columnDefinition = "double DEFAULT 0")
    private double totalReturn;
    
    @Column(name = "total", columnDefinition = "double DEFAULT 0")
    private double total;
    
    @Column(name = "paid", columnDefinition = "double DEFAULT 0")
    private double paid;
    
    @Column(name = "cash", columnDefinition = "double DEFAULT 0")
    private double cash;
    
    @Column(name = "cash_return", columnDefinition = "double DEFAULT 0")
    private double cashReturn;
    
    @Column(name = "remarks", length = 1000)
    private String remarks;
    
    @Column(name = "address", length = 1000)
    private String address;
    
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getNextOrderNo() {
        return nextOrderNo;
    }

    public void setNextOrderNo(int nextOrderNo) {
        this.nextOrderNo = nextOrderNo;
    }
    
    public int getSupplierUserId() {
        return supplierUserId;
    }

    public void setSupplierUserId(int supplierUserId) {
        this.supplierUserId = supplierUserId;
    }

    public long getInvoiceOn() {
        return invoiceOn;
    }

    public void setInvoiceOn(long invoiceOn) {
        this.invoiceOn = invoiceOn;
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

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getCashReturn() {
        return cashReturn;
    }

    public void setCashReturn(double cashReturn) {
        this.cashReturn = cashReturn;
    }
    
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(double totalReturn) {
        this.totalReturn = totalReturn;
    }
    
}
