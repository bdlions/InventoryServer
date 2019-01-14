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
        name = "sale_order_return_products",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getSaleOrderReturnProductsByOrderNo",
            query = "from EntitySaleOrderReturnProduct product where product.saleOrderNo = :saleOrderNo"
    ),
    @NamedQuery(
            name = "getSaleOrderReturnProductByProductIdAndSaleOrderNo",
            query = "from EntitySaleOrderReturnProduct product where product.productId = :productId AND product.saleOrderNo = :saleOrderNo"
    ),
    @NamedQuery(
            name = "getSubtotalSaleOrderReturnProductByProductIdInTimeRange",
            query = " select productId, sum(subtotal) from EntitySaleOrderReturnProduct entitySaleOrderReturnProduct where entitySaleOrderReturnProduct.productId = :productId AND entitySaleOrderReturnProduct.createdOn >= :startTime AND entitySaleOrderReturnProduct.createdOn <= :endTime  "
    ),
    @NamedQuery(
            name = "deleteSaleOrderReturnProductsByOrderNo",
            query = " delete from EntitySaleOrderReturnProduct product where product.saleOrderNo = :saleOrderNo"
    )
})
public class EntitySaleOrderReturnProduct extends ClientResponse implements java.io.Serializable{

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
    
    @Column(name = "quantity", columnDefinition = "double DEFAULT 0")
    private double quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "discount")
    private double discount;
    
    @Column(name = "subtotal", columnDefinition = "double DEFAULT 0")
    private double subtotal;
    
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
