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
import javax.persistence.Transient;

/**
 *
 * @author nazmul hasan
 */
@Entity
@Table(
        name = "sale_orders",
        indexes = {
            @Index(name = "idx_sale_order_no", columnList = "order_no"),
            @Index(name = "idx_customer_name", columnList = "customer_name"),
            @Index(name = "idx_customer_cell", columnList = "cell"),
            @Index(name = "idx_customer_email", columnList = "email")
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getSaleOrderById",
            query = "from EntitySaleOrder saleOrder where saleOrder.id = :id"
    ),
    @NamedQuery(
            name = "getSaleOrderByOrderNo",
            query = "from EntitySaleOrder saleOrder where saleOrder.orderNo = :orderNo"
    ),
    @NamedQuery(
            name = "getLastSaleOrder",
            query = "from EntitySaleOrder saleOrder order by saleOrder.id desc"
    ),
    @NamedQuery(
            name = "getAllSaleOrders",
            query = "from EntitySaleOrder saleOrder order by saleOrder.modifiedOn desc"
    ),
    @NamedQuery(
            name = "searchSaleOrderByOrderNo",
            query = "from EntitySaleOrder saleOrder where lower(saleOrder.orderNo) like :orderNo"
    ),
    @NamedQuery(
            name = "searchSaleOrderByCell",
            query = "from EntitySaleOrder saleOrder where lower(saleOrder.cell) like :cell"
    ),
    @NamedQuery(
            name = "updateSaleOrderCustomerInfo",
            query = "update EntitySaleOrder set customerName = :customerName, cell = :cell, email = :email where customerUserId = :customerUserId"
    )
//    ,
//    @NamedQuery(
//            name = "getCustomerCurrentDue",
//            query = " select sum(paid - total), customerUserId from EntitySaleOrder saleOrder where saleOrder.customerUserId = :customerUserId group by customerUserId"
//    )
})
public class EntitySaleOrder extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
   
    @Column(name = "order_no", length = 200)
    private String orderNo;
    
    @Column(name = "next_order_no", columnDefinition = "int(11) DEFAULT 1")
    private int nextOrderNo;
    
    @Column(name = "customer_user_id", columnDefinition = "int(11) NOT NULL")
    private int customerUserId;
    
    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "cell")
    private String cell;
    
    @Column(name = "status_id", columnDefinition = "int(11) NOT NULL")
    private int statusId;
    
    @Column(name = "sale_date", columnDefinition = "int(11) NOT NULL")
    private int saleDate;
    
    @Column(name = "remarks", length = 1000)
    private String remarks;
    
    @Column(name = "address", length = 1000)
    private String address;
    
    @Column(name = "subtotal", columnDefinition = "double DEFAULT 0")
    private double subtotal;
    
    @Column(name = "discount", columnDefinition = "double DEFAULT 0")
    private double discount;
    
    @Column(name = "total_return", columnDefinition = "double DEFAULT 0")
    private double totalReturn;
    
    @Column(name = "vat", columnDefinition = "double DEFAULT 0")
    private double vat;
    
    @Column(name = "total", columnDefinition = "double DEFAULT 0")
    private double total;
    
    @Column(name = "paid", columnDefinition = "double DEFAULT 0")
    private double paid;
    
    @Column(name = "created_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long createdOn;

    @Column(name = "modified_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long modifiedOn;
    
    @Column(name = "created_by_user_id", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private int createdByUserId;

    @Column(name = "created_by_user_name")
    private String createdByUserName;
    
    @Column(name = "modified_by_user_id", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
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

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }
    
}
