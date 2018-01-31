package org.bdlions.inventory.entity;

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
        name = "products_suppliers",
        indexes = {
            @Index(name = "idx_product_id", columnList = "product_id"),
            @Index(name = "idx_supplier_user_id", columnList = "supplier_user_id")
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getProductSuppliersByProductId",
            query = "from EntityProductSupplier product where product.productId = :productId"
    ),
    @NamedQuery(
            name = "getProductSuppliersBySupplierUserId",
            query = "from EntityProductSupplier product where product.supplierUserId = :supplierUserId order by product.productName asc"
    ),
    @NamedQuery(
            name = "updateProductSupplierSupplierUserName",
            query = "update EntityProductSupplier product set product.supplierUserName = :supplierUserName where product.supplierUserId = :supplierUserId"
    ),
    @NamedQuery(
            name = "deleteProductSuppliers",
            query = " delete from EntityProductSupplier product where product.productId = :productId order by product.supplierUserName asc"
    ),
    @NamedQuery(
            name = "deleteProductSuppliersBySupplierUserIds",
            query = " delete from EntityProductSupplier product where product.productId = :productId and product.supplierUserId IN (:supplierUserIds)"
    ),
    @NamedQuery(
            name = "deleteSupplierProductsByProductIds",
            query = " delete from EntityProductSupplier product where product.supplierUserId = :supplierUserId and product.productId IN (:productIds)"
    )
})
public class EntityProductSupplier {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;

    @Column(name = "product_id")
    private int productId;
    
    @Column(name = "product_name")
    private String productName;
    
    @Column(name = "supplier_user_id")
    private int supplierUserId;
    
    @Column(name = "supplier_user_name")
    private String supplierUserName;
    
    @Column(name = "supplier_price")
    private double supplierPrice;
    
    @Column(name = "supplier_product_code")
    private String supplierProductCode;

    public EntityProductSupplier() 
    {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSupplierUserId() {
        return supplierUserId;
    }

    public void setSupplierUserId(int supplierUserId) {
        this.supplierUserId = supplierUserId;
    }

    public String getSupplierUserName() {
        return supplierUserName;
    }

    public void setSupplierUserName(String supplierUserName) {
        this.supplierUserName = supplierUserName;
    }

    public double getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(double supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    public String getSupplierProductCode() {
        return supplierProductCode;
    }

    public void setSupplierProductCode(String supplierProductCode) {
        this.supplierProductCode = supplierProductCode;
    }
    
}
