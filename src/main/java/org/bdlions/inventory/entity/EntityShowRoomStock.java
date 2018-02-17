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
        name = "showroom_stocks",
        indexes = {
            @Index(name = "idx_showroom_stock_product_name", columnList = "product_name")
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getShowRoomProductByPurchaseOrderNoAndTransactionCategoryId",
            query = "from EntityShowRoomStock showRoomStock where showRoomStock.purchaseOrderNo = :purchaseOrderNo AND showRoomStock.transactionCategoryId = :transactionCategoryId AND showRoomStock.productId = :productId"
    ),
    @NamedQuery(
            name = "getShowRoomProductByPurchaseOrderNoAndTransactionCategoryIds",
            query = "from EntityShowRoomStock showRoomStock where showRoomStock.purchaseOrderNo = :purchaseOrderNo AND showRoomStock.productId = :productId AND showRoomStock.transactionCategoryId IN (:transactionCategoryIds)"
    ),
    @NamedQuery(
            name = "getShowRoomProductBySaleOrderNoAndTransactionCategoryId",
            query = "from EntityShowRoomStock showRoomStock where showRoomStock.saleOrderNo = :saleOrderNo AND showRoomStock.transactionCategoryId = :transactionCategoryId AND showRoomStock.productId = :productId"
    ),
    @NamedQuery(
            name = "getShowRoomProductBySaleOrderNoAndTransactionCategoryIds",
            query = "from EntityShowRoomStock showRoomStock where showRoomStock.saleOrderNo = :saleOrderNo AND showRoomStock.productId = :productId AND showRoomStock.transactionCategoryId IN (:transactionCategoryIds)"
    ),
    @NamedQuery(
            name = "getCurrentStock",
            query = " select productId, sum(stockIn - stockOut) from EntityShowRoomStock showRoomStock group by productId"
    ),
    @NamedQuery(
            name = "getDefaultEndingCurrentStock",
            query = " select productId, sum(stockIn - stockOut) from EntityShowRoomStock showRoomStock group by productId order by sum(stockIn - stockOut) asc "
    ),
    @NamedQuery(
            name = "getEndingCurrentStock",
            query = " select productId, sum(stockIn - stockOut) from EntityShowRoomStock showRoomStock group by productId having sum(stockIn - stockOut) <= :maxStock order by sum(stockIn - stockOut) asc "
    ),
    @NamedQuery(
            name = "getCurrentStockByProductIds",
            query = " select productId, sum(stockIn - stockOut) from EntityShowRoomStock showRoomStock where showRoomStock.productId IN (:productIds) group by productId"
    ),
    @NamedQuery(
            name = "searchCurrentStockByProductName",
            query = " select productId, sum(stockIn - stockOut) from EntityShowRoomStock showRoomStock where lower(showRoomStock.productName) like :productName group by productId"
    ),
    @NamedQuery(
            name = "updateShowRoomStockProductInfo",
            query = "update EntityShowRoomStock set productName = :productName where productId = :productId"
    ),
    @NamedQuery(
            name = "deleteShowRoomProductsByPurchaseOrderNoAndTransactionCategoryId",
            query = " delete from EntityShowRoomStock product where product.purchaseOrderNo = :purchaseOrderNo AND product.transactionCategoryId = :transactionCategoryId"
    ),    
    @NamedQuery(
            name = "deleteShowRoomProductsByPurchaseOrderNoAndTransactionCategoryIds",
            query = " delete from EntityShowRoomStock product where product.purchaseOrderNo = :purchaseOrderNo AND product.transactionCategoryId IN (:transactionCategoryIds)"
    ),
    @NamedQuery(
            name = "deleteShowRoomProductsBySaleOrderNoAndTransactionCategoryId",
            query = " delete from EntityShowRoomStock product where product.saleOrderNo = :saleOrderNo AND product.transactionCategoryId = :transactionCategoryId"
    ),
    @NamedQuery(
            name = "deleteShowRoomProductsBySaleOrderNoAndTransactionCategoryIds",
            query = " delete from EntityShowRoomStock product where product.saleOrderNo = :saleOrderNo AND product.transactionCategoryId IN (:transactionCategoryIds)"
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
    
    @Column(name = "product_name")
    private String productName;
    
    @Column(name = "stock_in", columnDefinition = "double DEFAULT 0")
    private double stockIn;
    
    @Column(name = "stock_out", columnDefinition = "double DEFAULT 0")
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
