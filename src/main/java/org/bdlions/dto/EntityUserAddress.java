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
        name = "user_addresses",
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
    private int address;
    
    @Column(name = "city", length = 500)
    private int city;
   
    @Column(name = "state", length = 500)
    private int state;
   
    @Column(name = "zip", length = 500)
    private int zip;

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

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }
    
    
    
}
