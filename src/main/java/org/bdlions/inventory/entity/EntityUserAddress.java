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
        name = "users_addresses",
        indexes = {
            
        }
)
@NamedQueries({
    
})
public class EntityUserAddress extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;

    @Column(name = "user_id", columnDefinition = "int(11) NOT NULL")
    private int userId;
    
    @Column(name = "address_type_id", columnDefinition = "int(11) NOT NULL")
    private int addressTypeId;
    @Column(name = "address_category_id", columnDefinition = "int(11) NOT NULL")
    private int addressCategoryId;
    
    @Column(name = "address", length = 500)
    private String address;
    
    @Column(name = "city", length = 500)
    private String city;
   
    @Column(name = "state", length = 500)
    private String state;
   
    @Column(name = "zip", length = 500)
    private String zip;

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

    public int getAddressTypeId() {
        return addressTypeId;
    }

    public void setAddressTypeId(int addressTypeId) {
        this.addressTypeId = addressTypeId;
    }

    public int getAddressCategoryId() {
        return addressCategoryId;
    }

    public void setAddressCategoryId(int addressCategoryId) {
        this.addressCategoryId = addressCategoryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
