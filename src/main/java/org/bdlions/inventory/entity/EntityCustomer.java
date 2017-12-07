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
        name = "customers",
        indexes = {
            @Index(name = "idx_customer_name", columnList = "customer_name"),
            @Index(name = "idx_customer_cell", columnList = "cell"),
            @Index(name = "idx_customer_email", columnList = "email")
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getCustomerByCustomerId",
            query = "from EntityCustomer customer where customer.id = :customerId"
    ),
    @NamedQuery(
            name = "getCustomerByUserId",
            query = "from EntityCustomer customer where customer.userId = :userId"
    ),
    @NamedQuery(
            name = "getCustomers",
            query = "from EntityCustomer customer"
    ),
    @NamedQuery(
            name = "searchCustomerByName",
            query = "from EntityCustomer customer where lower(customer.customerName) like :customerName"
    ),
    @NamedQuery(
            name = "searchCustomerByCell",
            query = "from EntityCustomer customer where lower(customer.cell) like :cell"
    ),
    @NamedQuery(
            name = "searchCustomerByEmail",
            query = "from EntityCustomer customer where lower(customer.email) like :email"
    )
})
public class EntityCustomer extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
    
    @Column(name = "user_id", columnDefinition = "int(11) NOT NULL")
    private int userId;
    
    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "cell")
    private String cell;
    
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }    

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
    
}
