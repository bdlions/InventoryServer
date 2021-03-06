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
        name = "po_showroom_return_products",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getPOShowRoomReturnProductsByOrderNo",
            query = "from EntityPOShowRoomReturnProduct product where product.orderNo = :orderNo"
    ),
    @NamedQuery(
            name = "getPOShowRoomReturnProductByProductIdAndOrderNo",
            query = "from EntityPOShowRoomReturnProduct product where product.productId = :productId AND product.orderNo = :orderNo"
    ),
    @NamedQuery(
            name = "getSubtotalPOShowRoomReturnProductByProductIdInTimeRange",
            query = " select productId, sum(subtotal) from EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct where entityPOShowRoomReturnProduct.productId = :productId AND entityPOShowRoomReturnProduct.createdOn >= :startTime AND entityPOShowRoomReturnProduct.createdOn <= :endTime  "
    ),
    @NamedQuery(
            name = "deletePOShowRoomReturnProductsByOrderNo",
            query = " delete from EntityPOShowRoomReturnProduct product where product.orderNo = :orderNo"
    )
})
public class EntityPOShowRoomReturnProduct extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
  
    @Column(name = "order_no", length = 200)
    private String orderNo;
    
    @Column(name = "product_id", columnDefinition = "int(11) NOT NULL")
    private int productId;  
    
    @Column(name = "quantity", columnDefinition = "double DEFAULT 0")
    private double quantity;
    
    @Column(name = "discount")
    private double discount;

    @Column(name = "unit_price")
    private double unitPrice;
    
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
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
