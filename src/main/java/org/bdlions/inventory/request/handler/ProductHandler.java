package org.bdlions.inventory.request.handler;

import com.bdlions.dto.response.ClientListResponse;
import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.ArrayList;
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
import org.bdlions.inventory.entity.EntityProductSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProductCategory;
import org.bdlions.inventory.entity.manager.EntityManagerProductSupplier;
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

    /*@ClientRequest(action = ACTION.FETCH_ALL_PRODUCT_CATEGORIES)
    public ClientResponse getAllProductCategories(ISession session, IPacket packet) throws Exception 
    {
        EntityManagerProductCategory entityManagerProductCategory = new EntityManagerProductCategory(packet.getPacketHeader().getAppId());
        List<EntityProductCategory> productCategorys = entityManagerProductCategory.getAllProductCategories();
        ListProductCategory response = new ListProductCategory();
        response.setProductCategories(productCategorys);
        response.setSuccess(true);
        return response;
    }*/
    
    @ClientRequest(action = ACTION.FETCH_ALL_PRODUCT_TYPES)
    public ClientResponse getAllProductTypes(ISession session, IPacket packet) throws Exception 
    {
        EntityManagerProductType entityManagerProductType = new EntityManagerProductType(packet.getPacketHeader().getAppId());
        List<EntityProductType> productTypes = entityManagerProductType.getAllProductTypes();
        ListProductType response = new ListProductType();
        response.setProductTypes(productTypes);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_ALL_UOMS)
    public ClientResponse getAllUOMs(ISession session, IPacket packet) throws Exception 
    {
        EntityManagerUOM entityManagerUOM = new EntityManagerUOM(packet.getPacketHeader().getAppId());
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
        DTOProduct dtoProduct = gson.fromJson(packet.getPacketBody(), DTOProduct.class);    
        if(dtoProduct == null || dtoProduct.getEntityProduct() == null)
        {
            responseEntityProduct.setSuccess(false);
            responseEntityProduct.setMessage("Invalid Porduct Info. Please try again later.");
            return responseEntityProduct;
        }
        EntityProduct entityProduct = dtoProduct.getEntityProduct();
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
        
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
        EntityProduct resultEntityProduct = entityManagerProduct.getProductByName(entityProduct.getName());
        if(resultEntityProduct != null)
        {
            responseEntityProduct.setSuccess(false);
            responseEntityProduct.setMessage("Product Name is already exists or invalid.");
            return responseEntityProduct;
        }
        
        List<EntityProductSupplier> entityProductSupplierList = dtoProduct.getEntityProductSupplierList();        
        responseEntityProduct = entityManagerProduct.createProduct(entityProduct, entityProductSupplierList);
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
        if( entityProduct == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get product info. Please try again later");
            return generalResponse;
        } 
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
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
    
    @ClientRequest(action = ACTION.FETCH_PRODUCT_SUPPLIER_LIST)
    public ClientResponse getProductSupplierList(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        ClientListResponse response = new ClientListResponse();
        DTOProduct dtoProduct = gson.fromJson(packet.getPacketBody(), DTOProduct.class);     
        if( dtoProduct == null || dtoProduct.getEntityProduct() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid request to get product supplier list. Please try again later");
            return response;
        } 
        EntityManagerProductSupplier entityManagerProductSupplier = new EntityManagerProductSupplier(packet.getPacketHeader().getAppId());
        List<EntityProductSupplier> supplierList = entityManagerProductSupplier.getProductSuppliersByProductId(dtoProduct.getEntityProduct().getId(), dtoProduct.getOffset(), dtoProduct.getLimit());
        int counter = entityManagerProductSupplier.getTotalProductSuppliersByProductId(dtoProduct.getEntityProduct().getId());
        response.setCounter(counter);
        response.setList(supplierList);
        response.setSuccess(true);        
        return response;
    }
    
    @ClientRequest(action = ACTION.UPDATE_PRODUCT_INFO)
    public ClientResponse updateProductInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOProduct dtoProduct = gson.fromJson(packet.getPacketBody(), DTOProduct.class);    
        if(dtoProduct == null || dtoProduct.getEntityProduct() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Porduct Info. Please try again later.");
            return response;
        }
        EntityProduct entityProduct = dtoProduct.getEntityProduct();
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
        
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
        EntityProduct resultEntityProduct = entityManagerProduct.getProductByName(entityProduct.getName());
        if(resultEntityProduct != null && resultEntityProduct.getId() != entityProduct.getId())
        {
            response.setSuccess(false);
            response.setMessage("Product Name is already exists or invalid.");
            return response;
        }
        List<EntityProductSupplier> entityProductSupplierList = dtoProduct.getEntityProductSupplierList();  
        List<Integer> supplierUserIds = new ArrayList<>();
        if(entityProductSupplierList != null && !entityProductSupplierList.isEmpty())
        {
            for(EntityProductSupplier entityProductSupplier: entityProductSupplierList)
            {
                if(!supplierUserIds.contains(entityProductSupplier.getSupplierUserId()))
                {
                    supplierUserIds.add(entityProductSupplier.getSupplierUserId());
                }
            }
        }
        List<EntityProductSupplier> epsList = dtoProduct.getEpsListToBeDeleted(); 
        if(epsList != null && !epsList.isEmpty())
        {
            for(EntityProductSupplier entityProductSupplier: epsList)
            {
                if(!supplierUserIds.contains(entityProductSupplier.getSupplierUserId()))
                {
                    supplierUserIds.add(entityProductSupplier.getSupplierUserId());
                }
            }
        }        
        if(entityManagerProduct.updateProduct(entityProduct, entityProductSupplierList, supplierUserIds))
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
        
        if( dtoProduct == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get product list. Please try again later");
            return generalResponse;
        }
        
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
        List<EntityProduct> products = entityManagerProduct.getProducts(dtoProduct.getOffset(), dtoProduct.getLimit());
        int totalProducts = entityManagerProduct.getTotalProducts();
        ListProduct response = new ListProduct();
        response.setProducts(products);
        response.setTotalProducts(totalProducts);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_PRODUCTS_BY_NAME)
    public ClientResponse getProductsByName(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOProduct dtoProduct = gson.fromJson(packet.getPacketBody(), DTOProduct.class);   
        
        if( dtoProduct == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get product list. Please try again later");
            return generalResponse;
        }
        
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
        List<EntityProduct> products = entityManagerProduct.searchProductByName(dtoProduct.getEntityProduct().getName(), dtoProduct.getOffset(), dtoProduct.getLimit());
        int totalProducts = entityManagerProduct.searchTotalProductByName(dtoProduct.getEntityProduct().getName());
        ListProduct response = new ListProduct();
        response.setProducts(products);
        response.setTotalProducts(totalProducts);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_PRODUCT_BY_CODE)
    public ClientResponse getProductByCode(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        EntityProduct entityProduct = gson.fromJson(packet.getPacketBody(), EntityProduct.class);
        if( entityProduct == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get product info by code. Please try again later");
            return generalResponse;
        }        
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
        EntityProduct response = entityManagerProduct.getProductByCode(entityProduct.getCode());
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
}
