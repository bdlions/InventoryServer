package org.bdlions.inventory.entity;

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
        name = "product_categories",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getProductCategoryById",
            query = "from EntityProductCategory category where category.id = :id"
    ),
    @NamedQuery(
            name = "getProductCategoryByTitle",
            query = "from EntityProductCategory category where category.title = :title"
    ),
    @NamedQuery(
            name = "getAllProductCategories",
            query = "from EntityProductCategory productCategory"
    ),
    @NamedQuery(
            name = "searchProductCategoryByTitle",
            query = "from EntityProductCategory category where lower(category.title) like :title"
    )
})
public class EntityProductCategory extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;

    @Column(name = "title", length = 200)
    private String title;
    
    @Column(name = "vat", columnDefinition = "double DEFAULT 0")
    private double vat;
    
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }
}
