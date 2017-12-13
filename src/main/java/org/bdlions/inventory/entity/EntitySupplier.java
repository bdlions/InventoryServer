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
        name = "suppliers",
        indexes = {
            @Index(name = "idx_supplier_name", columnList = "supplier_name"),
            @Index(name = "idx_supplier_cell", columnList = "cell"),
            @Index(name = "idx_supplier_email", columnList = "email")
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
    ),
    @NamedQuery(
            name = "searchSupplierByName",
            query = "from EntitySupplier supplier where lower(supplier.supplierName) like :supplierName"
    ),
    @NamedQuery(
            name = "searchSupplierByCell",
            query = "from EntitySupplier supplier where lower(supplier.cell) like :cell"
    ),
    @NamedQuery(
            name = "searchSupplierByEmail",
            query = "from EntitySupplier supplier where lower(supplier.email) like :email"
    )        
})
public class EntitySupplier extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
    
    @Column(name = "user_id", columnDefinition = "int(11) NOT NULL")
    private int userId;
    
    @Column(name = "supplier_name")
    private String supplierName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "cell")
    private String cell;
    
    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Column(name = "balance")
    private double balance;
    
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
    
}
