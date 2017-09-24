package org.bdlions.dto;

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
        name = "warehouse_stocks",
        indexes = {
            
        }
)
@NamedQueries({
    
})
public class EntityWarehouseStock extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;

   
    @Column(name = "order_no", length = 200)
    private String order_no;
    
    @Column(name = "product_id", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private int productId;

    @Column(name = "stock_in", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private double stockIn;
    
    @Column(name = "stock_out", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private double stockOut;
    
    @Column(name = "created_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private int created_on;

    @Column(name = "modified_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private int modified_on;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
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

    public int getCreated_on() {
        return created_on;
    }

    public void setCreated_on(int created_on) {
        this.created_on = created_on;
    }

    public int getModified_on() {
        return modified_on;
    }

    public void setModified_on(int modified_on) {
        this.modified_on = modified_on;
    }
    

    
}
