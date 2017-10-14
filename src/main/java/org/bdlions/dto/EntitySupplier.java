package org.bdlions.dto;

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
        name = "suppliers",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getSupplierBySupplierId",
            query = "from EntitySupplier supplier where supplier.id = :supplierId"
    ),
    @NamedQuery(
            name = "getSupplierByUserId",
            query = "from EntitySupplier supplier where supplier.userId = :userId"
    ),
    @NamedQuery(
            name = "getSuppliers",
            query = "from EntitySupplier supplier"
    )
})
public class EntitySupplier extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
    
    @Column(name = "user_id", columnDefinition = "int(11) NOT NULL")
    private int userId;
    
    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Column(name = "balance")
    private double balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
}
