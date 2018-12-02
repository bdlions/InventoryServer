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
        name = "adjust_stock_order_products",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getAdjustStockOrderProductsByOrderNo",
            query = "from EntityAdjustStockOrderProduct product where product.orderNo = :orderNo"
    ),
    @NamedQuery(
            
            name = "deleteAdjustStockOrderProductsByOrderNo",
            query = " delete from EntityAdjustStockOrderProduct product where product.orderNo = :orderNo"
    )
})
public class EntityAdjustStockOrderProduct extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
  
    @Column(name = "order_no", length = 200)
    private String orderNo;
    
    @Column(name = "product_id", columnDefinition = "int(11) NOT NULL")
    private int productId;
   
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "old_quantity")
    private double oldQuantity;
    
    @Column(name = "new_quantity")
    private double newQuantity;
    
    @Column(name = "difference")
    private double difference;
    
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(double oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public double getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(double newQuantity) {
        this.newQuantity = newQuantity;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
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
}
