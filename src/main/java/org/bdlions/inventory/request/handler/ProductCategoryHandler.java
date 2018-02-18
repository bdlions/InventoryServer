package org.bdlions.inventory.request.handler;

import com.bdlions.dto.response.ClientListResponse;
import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;
import org.bdlions.inventory.entity.EntityProductCategory;
import org.bdlions.inventory.dto.ListProductCategory;
import org.bdlions.inventory.entity.manager.EntityManagerProductCategory;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.util.annotation.ClientRequest;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class ProductCategoryHandler {

    private final ISessionManager sessionManager;

    public ProductCategoryHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.FETCH_PRODUCT_CATEGORY_INFO)
    public ClientResponse getProductCategoryInfo(ISession session, IPacket packet) throws Exception 
    {
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);       
        int categoryId = jsonObject.get("categoryId").getAsInt();
        EntityManagerProductCategory entityManagerProductCategory = new EntityManagerProductCategory(packet.getPacketHeader().getAppId());
        EntityProductCategory entityProductCategory = entityManagerProductCategory.getProductCategoryById(categoryId);
        if(entityProductCategory != null && entityProductCategory.getId() > 0)
        {
            entityProductCategory.setSuccess(true);
        }
        else
        {
            entityProductCategory = new EntityProductCategory();
            entityProductCategory.setSuccess(false);
            entityProductCategory.setMessage("Invalid product category.");
        }
        return entityProductCategory;
    }
    
    @ClientRequest(action = ACTION.ADD_PRODUCT_CATEGORY_INFO)
    public ClientResponse addProductCategoryInfo(ISession session, IPacket packet) throws Exception 
    {
        EntityProductCategory response = new EntityProductCategory();
        Gson gson = new Gson();
        EntityProductCategory entityProductCategory = gson.fromJson(packet.getPacketBody(), EntityProductCategory.class);    
        if(entityProductCategory == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Porduct Category Info. Please try again later.");
            return response;
        }
        else if(StringUtils.isNullOrEmpty(entityProductCategory.getTitle()))
        {
            response.setSuccess(false);
            response.setMessage("Product Category Title is required.");
            return response;
        }        
        //check whether category name exists or not
        EntityManagerProductCategory entityManagerProductCategory = new EntityManagerProductCategory(packet.getPacketHeader().getAppId());
        EntityProductCategory resultEntityProductCategory = entityManagerProductCategory.getProductCategoryByTitle(entityProductCategory.getTitle());
        if(resultEntityProductCategory != null)
        {
            response.setSuccess(false);
            response.setMessage("Product Category Name is already exists or invalid.");
            return response;
        }
        
        response = entityManagerProductCategory.createProductCategory(entityProductCategory);
        if(response != null && response.getId() > 0)
        {
            response.setSuccess(true);
            response.setMessage("Product Category is added successfully.");
        }
        else
        {
            response = new EntityProductCategory();
            response.setSuccess(false);
            response.setMessage("Unable to add a new Product Category. Please try again later.");
        }
        
        return response;
    }
    
    @ClientRequest(action = ACTION.UPDATE_PRODUCT_CATEGORY_INFO)
    public ClientResponse updateProductCategoryInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        EntityProductCategory entityProductCategory = gson.fromJson(packet.getPacketBody(), EntityProductCategory.class);    
        if(entityProductCategory == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Porduct Info. Please try again later.");
            return response;
        }
        else if(entityProductCategory.getId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Porduct Category Info. Please try again later..");
            return response;
        }
        else if(StringUtils.isNullOrEmpty(entityProductCategory.getTitle()))
        {
            response.setSuccess(false);
            response.setMessage("Product Category Title is required.");
            return response;
        }
        //check whether category title is valid or not
        EntityManagerProductCategory entityManagerProductCategory = new EntityManagerProductCategory(packet.getPacketHeader().getAppId());
        EntityProductCategory resultEntityProductCategory = entityManagerProductCategory.getProductCategoryByTitle(entityProductCategory.getTitle());
        if(resultEntityProductCategory != null && resultEntityProductCategory.getId() != entityProductCategory.getId())
        {
            response.setSuccess(false);
            response.setMessage("Product Category Name is already exists or invalid.");
            return response;
        }
        if(entityManagerProductCategory.updateProductCategory(entityProductCategory))
        {
            response.setSuccess(true);
            response.setMessage("Product Category is updated successfully.");
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Unable to update Product Category. Please try again later.");
        }        
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_ALL_PRODUCT_CATEGORIES)
    public ClientResponse getAllProductCategories(ISession session, IPacket packet) throws Exception 
    {
        EntityManagerProductCategory entityManagerProductCategory = new EntityManagerProductCategory(packet.getPacketHeader().getAppId());
        List<EntityProductCategory> productCategorys = entityManagerProductCategory.getAllProductCategories();
        ListProductCategory response = new ListProductCategory();
        response.setProductCategories(productCategorys);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_PRODUCT_CATEGORY_BY_TITLE)
    public ClientResponse getProductCategoriesByTitle(ISession session, IPacket packet) throws Exception 
    {
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);       
        String title = jsonObject.get("title").getAsString();        
        EntityManagerProductCategory entityManagerProductCategory = new EntityManagerProductCategory(packet.getPacketHeader().getAppId());
        List<EntityProductCategory> productCategories = entityManagerProductCategory.searchProductCategoryByTitle(title);
        ClientListResponse response = new ClientListResponse();
        response.setList(productCategories);
        response.setSuccess(true);
        return response;
    }
}
