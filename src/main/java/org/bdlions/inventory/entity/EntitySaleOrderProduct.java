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
        name = "sale_order_products",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getSaleOrderProductsByOrderNo",
            query = "from EntitySaleOrderProduct product where product.saleOrderNo = :saleOrderNo"
    ),
    @NamedQuery(
            name = "deleteSaleOrderProductsByOrderNo",
            query = " delete from EntitySaleOrderProduct product where product.saleOrderNo = :saleOrderNo"
    )
})
public class EntitySaleOrderProduct extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
   
    @Column(name = "product_id")
    private int productId;
    
    @Column(name = "sale_order_no", length = 200)
    private String saleOrderNo;
    
    @Column(name = "purchase_order_no", length = 200)
    private String purchaseOrderNo;
    
    @Column(name = "cost_price", columnDefinition = "double DEFAULT 0")
    private double costPrice;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "discount")
    private double discount;
    
    @Column(name = "vat", columnDefinition = "double DEFAULT 0")
    private double vat;
    
    @Column(name = "created_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long createdOn;

    @Column(name = "modified_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long modifiedOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getSaleOrderNo() {
        return saleOrderNo;
    }

    public void setSaleOrderNo(String saleOrderNo) {
        this.saleOrderNo = saleOrderNo;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
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

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }    
}
