package org.bdlions.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.List;
import org.bdlions.dto.DTOProduct;
import org.bdlions.dto.EntityProduct;
import org.bdlions.dto.EntityProductCategory;
import org.bdlions.dto.EntityProductType;
import org.bdlions.dto.EntityUOM;
import org.bdlions.dto.ListProduct;
import org.bdlions.dto.ListProductCategory;
import org.bdlions.dto.ListProductType;
import org.bdlions.dto.ListUOM;
import org.bdlions.library.ProductLibrary;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.manager.Product;

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
        Product product = new Product();
        List<EntityProductCategory> productCategorys = product.getAllProductCategories();
        ListProductCategory response = new ListProductCategory();
        response.setProductCategories(productCategorys);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_ALL_PRODUCT_TYPES)
    public ClientResponse getAllProductTypes(ISession session, IPacket packet) throws Exception 
    {
        Product product = new Product();
        List<EntityProductType> productTypes = product.getAllProductTypes();
        ListProductType response = new ListProductType();
        response.setProductTypes(productTypes);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_ALL_UOMS)
    public ClientResponse getAllUOMs(ISession session, IPacket packet) throws Exception 
    {
        Product product = new Product();
        List<EntityUOM> uoms = product.getAllUOMs();
        ListUOM response = new ListUOM();
        response.setUoms(uoms);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.ADD_PRODUCT_INFO)
    public ClientResponse addProductInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        EntityProduct entityProduct = gson.fromJson(packet.getPacketBody(), EntityProduct.class);     
        ProductLibrary productLibrary = new ProductLibrary();
        GeneralResponse response = productLibrary.addProduct(entityProduct);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_PRODUCT_INFO)
    public ClientResponse getProductInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        EntityProduct entityProduct = gson.fromJson(packet.getPacketBody(), EntityProduct.class);     
        Product product = new Product();
        EntityProduct response = product.getProductInfo(entityProduct.getId());
        if(response.getId() > 0)
        {
            response.setSuccess(true);
        }
        else
        {
            response.setSuccess(false);
            response.setMessage("Invalid product.");
        }
        return response;
    }
    
    @ClientRequest(action = ACTION.UPDATE_PRODUCT_INFO)
    public ClientResponse updateProductInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        EntityProduct entityProduct = gson.fromJson(packet.getPacketBody(), EntityProduct.class);     
        ProductLibrary productLibrary = new ProductLibrary();
        GeneralResponse response = productLibrary.updateProduct(entityProduct);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_PRODUCTS)
    public ClientResponse getProducts(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOProduct dtoProduct = gson.fromJson(packet.getPacketBody(), DTOProduct.class);     
        Product product = new Product();
        List<EntityProduct> products = product.getProducts(dtoProduct);
        ListProduct response = new ListProduct();
        response.setProducts(products);
        response.setSuccess(true);
        return response;
    }
}
