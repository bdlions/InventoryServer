package org.bdlions.inventory.manager;

import java.util.List;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityProductCategory;
import org.bdlions.inventory.entity.EntityProductType;
import org.bdlions.inventory.entity.EntityUOM;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProductCategory;
import org.bdlions.inventory.entity.manager.EntityManagerProductType;
import org.bdlions.inventory.entity.manager.EntityManagerUOM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nazmul Hasan
 */
public class Product {
    private final Logger logger = LoggerFactory.getLogger(Product.class);
    
    public List<EntityProductCategory> getAllProductCategories()
    {
        EntityManagerProductCategory entityManagerProductCategory = new EntityManagerProductCategory();
        return entityManagerProductCategory.getAllProductCategories();
    }
    
    public List<EntityProductType> getAllProductTypes()
    {
        EntityManagerProductType entityManagerProductType = new EntityManagerProductType();
        return entityManagerProductType.getAllProductTypes();
    }
    
    public List<EntityUOM> getAllUOMs()
    {
        EntityManagerUOM entityManagerUOM = new EntityManagerUOM();
        return entityManagerUOM.getAllUOMs();
    }
    
    public EntityProduct getEntityProductByName(EntityProduct entityProduct)
    {
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
        return entityManagerProduct.getEntityProductByName(entityProduct);        
    }
    
    public EntityProduct createProduct(EntityProduct entityProduct) 
    {
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
        return entityManagerProduct.createProduct(entityProduct);        
    }
    
    public EntityProduct getProductInfo(int productId) 
    {
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
        return entityManagerProduct.getProductInfo(productId);
    }
    
    public boolean updateProduct(EntityProduct entityProduct) 
    {
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
        return entityManagerProduct.updateProduct(entityProduct);
    }
    
    public List<EntityProduct> getProducts(DTOProduct dtoProduct) 
    {
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
        return entityManagerProduct.getProducts(dtoProduct);
    }
}
