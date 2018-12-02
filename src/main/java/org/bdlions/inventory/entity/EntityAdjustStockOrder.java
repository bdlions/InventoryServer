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
        name = "adjust_stock_orders",
        indexes = {
            @Index(name = "idx_adjust_stock_order_no", columnList = "order_no")
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getAdjustStockOrderById",
            query = "from EntityAdjustStockOrder adjustStockOrder where adjustStockOrder.id = :id"
    ),
    @NamedQuery(
            name = "getAdjustStockeOrderByOrderNo",
            query = "from EntityAdjustStockOrder adjustStockOrder where adjustStockOrder.orderNo = :orderNo"
    ),
    @NamedQuery(
            name = "getLastAdjustStockeOrder",
            query = "from EntityAdjustStockOrder adjustStockOrder order by adjustStockOrder.id desc"
    ),
    @NamedQuery(
            name = "getAllAdjustStockeOrders",
            query = "from EntityAdjustStockOrder adjustStockOrder order by adjustStockOrder.modifiedOn desc"
    )
})
public class EntityAdjustStockOrder extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
  
    @Column(name = "order_no", length = 200)
    private String orderNo;
    
    @Column(name = "next_order_no", columnDefinition = "int(11) DEFAULT 1")
    private int nextOrderNo;
    
    @Column(name = "adjust_on", columnDefinition = "int(11) unsigned DEFAULT 0")
    private long adjustOn;

    @Column(name = "remarks", length = 1000)
    private String remarks;
    
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

    public long getAdjustOn() {
        return adjustOn;
    }

    public void setAdjustOn(long adjustOn) {
        this.adjustOn = adjustOn;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
