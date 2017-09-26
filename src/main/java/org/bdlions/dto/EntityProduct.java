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
        name = "products",
        indexes = {
            
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getProductByProductId",
            query = "from EntityProduct product where product.id = :productId"
    ),
    @NamedQuery(
            name = "getProducts",
            query = "from EntityProduct product"
    )
})
public class EntityProduct extends ClientResponse implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "code", length = 200)
    private String code;
    
    @Column(name = "category_id", columnDefinition = "int(11) DEFAULT NULL")
    private int categoryId;
    
    @Column(name = "category_title", length = 200)
    private String categoryTitle;
    
    @Column(name = "type_id", columnDefinition = "int(11) DEFAULT NULL")
    private int typeId;
    
    @Column(name = "type_title", length = 200)
    private String typeTitle;
    
    @Column(name = "width", length = 200)
    private String width;

    @Column(name = "height", length = 200)
    private String height;
    
    @Column(name = "weight", length = 200)
    private String weight;

    @Column(name = "unit_price", length = 200)
    private double unitPrice;

    @Column(name = "length", length = 200)
    private String length;
    
    @Column(name = "standard_uom_id", columnDefinition = "int(11) DEFAULT NULL")
    private int standardUOMId;
    
    @Column(name = "sale_uom_id", columnDefinition = "int(11) DEFAULT NULL")
    private int saleUOMId;
    
    @Column(name = "purchase_uom_id", columnDefinition = "int(11) DEFAULT NULL")
    private int purchaseUOMId;
    
    
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getStandardUOMId() {
        return standardUOMId;
    }

    public void setStandardUOMId(int standardUOMId) {
        this.standardUOMId = standardUOMId;
    }

    public int getSaleUOMId() {
        return saleUOMId;
    }

    public void setSaleUOMId(int saleUOMId) {
        this.saleUOMId = saleUOMId;
    }

    public int getPurchaseUOMId() {
        return purchaseUOMId;
    }

    public void setPurchaseUOMId(int purchaseUOMId) {
        this.purchaseUOMId = purchaseUOMId;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public void setTypeTitle(String typeTitle) {
        this.typeTitle = typeTitle;
    }
    
}
