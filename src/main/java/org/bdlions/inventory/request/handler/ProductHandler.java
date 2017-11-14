package org.bdlions.inventory.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.List;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityProductCategory;
import org.bdlions.inventory.entity.EntityProductType;
import org.bdlions.inventory.entity.EntityUOM;
import org.bdlions.inventory.dto.ListProduct;
import org.bdlions.inventory.dto.ListProductCategory;
import org.bdlions.inventory.dto.ListProductType;
import org.bdlions.inventory.dto.ListUOM;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProductCategory;
import org.bdlions.inventory.entity.manager.EntityManagerProductType;
import org.bdlions.inventory.entity.manager.EntityManagerUOM;
import org.bdlions.util.annotation.ClientRequest;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class ProductHandler {

    private final ISessionManager sessionManager;

    public ProductHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.FETCH_ALL_PRODUCT_CATEGORIES)
    public ClientResponse getAllProductCategories(ISession session, IPacket packet) throws Exception 
    {
        EntityManagerProductCategory entityManagerProductCategory = new EntityManagerProductCategory();
        List<EntityProductCategory> productCategorys = entityManagerProductCategory.getAllProductCategories();
        ListProductCategory response = new ListProductCategory();
        response.setProductCategories(productCategorys);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_ALL_PRODUCT_TYPES)
    public ClientResponse getAllProductTypes(ISession session, IPacket packet) throws Exception 
    {
        EntityManagerProductType entityManagerProductType = new EntityManagerProductType();
        List<EntityProductType> productTypes = entityManagerProductType.getAllProductTypes();
        ListProductType response = new ListProductType();
        response.setProductTypes(productTypes);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_ALL_UOMS)
    public ClientResponse getAllUOMs(ISession session, IPacket packet) throws Exception 
    {
        EntityManagerUOM entityManagerUOM = new EntityManagerUOM();
        List<EntityUOM> uoms = entityManagerUOM.getAllUOMs();
        ListUOM response = new ListUOM();
        response.setUoms(uoms);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.ADD_PRODUCT_INFO)
    public ClientResponse addProductInfo(ISession session, IPacket packet) throws Exception 
    {
        EntityProduct responseEntityProduct = new EntityProduct();
        Gson gson = new Gson();
        EntityProduct entityProduct = gson.fromJson(packet.getPacketBody(), EntityProduct.class);     
        if(entityProduct == null)
        {
            responseEntityProduct.setSuccess(false);
            responseEntityProduct.setMessage("Invalid Porduct Info. Please try again later.");
            return responseEntityProduct;
        }
        else if(entityProduct.getName() == null || entityProduct.getName().equals(""))
        {
            responseEntityProduct.setSuccess(false);
            responseEntityProduct.setMessage("Product Name is required.");
            return responseEntityProduct;
        }
        else if(entityProduct.getTypeId() <= 0)
        {
            responseEntityProduct.setSuccess(false);
            responseEntityProduct.setMessage("Please select product type.");
            return responseEntityProduct;
        }
        else if(entityProduct.getCategoryId() <= 0)
        {
            responseEntityProduct.setSuccess(false);
            responseEntityProduct.setMessage("Please select product category.");
            return responseEntityProduct;
        } 
        
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
        EntityProduct resultEntityProduct = entityManagerProduct.getProductByName(entityProduct.getName());
        if(resultEntityProduct != null)
        {
            responseEntityProduct.setSuccess(false);
            responseEntityProduct.setMessage("Product Name is already exists or invalid.");
            return responseEntityProduct;
        }
        
        responseEntityProduct = entityManagerProduct.createProduct(entityProduct);
        if(responseEntityProduct != null && responseEntityProduct.getId() > 0)
        {
            responseEntityProduct.setSuccess(true);
            responseEntityProduct.setMessage("Product is added successfully.");
        }
        else
        {
            responseEntityProduct = new EntityProduct();
            responseEntityProduct.setSuccess(false);
            responseEntityProduct.setMessage("Unable to add a new product. Please try again later.");
        }
        
        return responseEntityProduct;
    }
    
    @ClientRequest(action = ACTION.FETCH_PRODUCT_INFO)
    public ClientResponse getProductInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        EntityProduct entityProduct = gson.fromJson(packet.getPacketBody(), EntityProduct.class);     
        
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
        EntityProduct response = entityManagerProduct.getProductByProductId(entityProduct.getId());
        if(response != null && response.getId() > 0)
        {
            response.setSuccess(true);
        }
        else
        {
            response = new EntityProduct();
            response.setSuccess(false);
            response.setMessage("Invalid product.");
        }
        return response;
    }
    
    @ClientRequest(action = ACTION.UPDATE_PRODUCT_INFO)
    public ClientResponse updateProductInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        EntityProduct entityProduct = gson.fromJson(packet.getPacketBody(), EntityProduct.class);     
        if(entityProduct == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Porduct Info. Please try again later.");
            return response;
        }
        else if(entityProduct.getId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Porduct Info. Please try again later..");
            return response;
        }
        else if(entityProduct.getName() == null || entityProduct.getName().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Product Name is required.");
            return response;
        }
        else if(entityProduct.getTypeId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Please select product type.");
            return response;
        }
        else if(entityProduct.getCategoryId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Please select product category.");
            return response;
        }
        
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
        EntityProduct resultEntityProduct = entityManagerProduct.getProductByName(entityProduct.getName());
        if(resultEntityProduct != null && resultEntityProduct.getId() != entityProduct.getId())
        {
            response.setSuccess(false);
            response.setMessage("Product Name is already exists or invalid.");
            return response;
        }
        
        if(entityManagerProduct.updateProduct(entityProduct))
        {
            response.setSuccess(true);
            response.setMessage("Product is updated successfully.");
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Unable to update product. Please try again later.");
        }        
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_PRODUCTS)
    public ClientResponse getProducts(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOProduct dtoProduct = gson.fromJson(packet.getPacketBody(), DTOProduct.class);     
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
        List<EntityProduct> products = entityManagerProduct.getProducts(0, 3);
        ListProduct response = new ListProduct();
        response.setProducts(products);
        response.setSuccess(true);
        return response;
    }
}
