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
        name = "showroom_stocks",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getPurchaseOrderProductByOrderNoAndCategoryId",
            query = "from EntityShowRoomStock showRoomStock where showRoomStock.purchaseOrderNo = :purchaseOrderNo AND showRoomStock.transactionCategoryId = :transactionCategoryId AND showRoomStock.productId = :productId"
    ),
    @NamedQuery(
            name = "getSaleOrderProductByOrderNoAndCategoryId",
            query = "from EntityShowRoomStock showRoomStock where showRoomStock.saleOrderNo = :saleOrderNo AND showRoomStock.transactionCategoryId = :transactionCategoryId AND showRoomStock.productId = :productId"
    ),
    @NamedQuery(
            name = "getCurrentStock",
            query = " select productId, sum(stockIn - stockOut) from EntityShowRoomStock showRoomStock group by productId"
    ),
    @NamedQuery(
            name = "deletePurchaseOrderShowRoomProductsByOrderNo",
            query = " delete from EntityShowRoomStock product where product.purchaseOrderNo = :purchaseOrderNo AND product.transactionCategoryId = :transactionCategoryId"
    ),
    @NamedQuery(
            name = "deleteSaleOrderShowRoomProductsByOrderNo",
            query = " delete from EntityShowRoomStock product where product.saleOrderNo = :saleOrderNo AND product.transactionCategoryId = :transactionCategoryId"
    )
})
public class EntityShowRoomStock extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
 
    @Column(name = "purchase_order_no", length = 200)
    private String purchaseOrderNo;
    
    @Column(name = "sale_order_no", length = 200)
    private String saleOrderNo;
    
    @Column(name = "product_id")
    private int productId;
    
    @Column(name = "stock_in")
    private double stockIn;
    
    @Column(name = "stock_out")
    private double stockOut;
    
    @Column(name = "transaction_category_id")
    private int transactionCategoryId;
    
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

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getSaleOrderNo() {
        return saleOrderNo;
    }

    public void setSaleOrderNo(String saleOrderNo) {
        this.saleOrderNo = saleOrderNo;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getStockIn() {
        return stockIn;
    }

    public void setStockIn(double stockIn) {
        this.stockIn = stockIn;
    }

    public double getStockOut() {
        return stockOut;
    }

    public void setStockOut(double stockOut) {
        this.stockOut = stockOut;
    }

    public int getTransactionCategoryId() {
        return transactionCategoryId;
    }

    public void setTransactionCategoryId(int transactionCategoryId) {
        this.transactionCategoryId = transactionCategoryId;
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