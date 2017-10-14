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
        name = "customers",
        indexes = {
            
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
    )
})
public class EntityCustomer extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
    
    @Column(name = "user_id", columnDefinition = "int(11) NOT NULL")
    private int userId;
    
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
    
}
