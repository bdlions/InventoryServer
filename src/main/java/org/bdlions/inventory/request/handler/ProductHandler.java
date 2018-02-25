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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityProductType;
import org.bdlions.inventory.entity.EntityUOM;
import org.bdlions.inventory.dto.ListProduct;
import org.bdlions.inventory.dto.ListProductType;
import org.bdlions.inventory.dto.ListUOM;
import org.bdlions.inventory.entity.EntityProductSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProductSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerProductType;
import org.bdlions.inventory.entity.manager.EntityManagerUOM;
import org.bdlions.inventory.manager.Stock;
import org.bdlions.inventory.util.StringUtils;
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
        if(StringUtils.isNullOrEmpty(entityProduct.getName()))
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
    
    @ClientRequest(action = ACTION.FETCH_PRODUCTS_WITH_STOCKS)
    public ClientResponse getProductsWithStocks(ISession session, IPacket packet) throws Exception 
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
        List<DTOProduct> productWithStocks = new ArrayList<>();
        int totalProducts = 0;
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
        List<EntityProduct> products = entityManagerProduct.getProducts(dtoProduct.getOffset(), dtoProduct.getLimit());
        List<Integer> productIds = new ArrayList<>();
        if(products != null && !products.isEmpty())
        {
            for(int counter = 0; counter < products.size(); counter++)
            {
                if(!productIds.contains(products.get(counter).getId()))
                {
                    productIds.add(products.get(counter).getId());
                }
            }
            Stock stock = new Stock(packet.getPacketHeader().getAppId());
            productWithStocks = stock.getCurrentStockByProductIds(productIds);
            List<Integer> excludedProductIds = new ArrayList<>();
            List<Integer> tempProductIds = new ArrayList<>();
            if(productWithStocks != null && !productWithStocks.isEmpty())
            {
                for(int counter = 0; counter < productWithStocks.size(); counter++)
                {
                    if(!tempProductIds.contains(productWithStocks.get(counter).getEntityProduct().getId()))
                    {
                        tempProductIds.add(productWithStocks.get(counter).getEntityProduct().getId());
                    }
                }
            }
            for(int productId: productIds)
            {
                if(!tempProductIds.contains(productId))
                {
                    excludedProductIds.add(productId);
                }
            }
            for(EntityProduct entityProduct: products)
            {
                if(excludedProductIds.contains(entityProduct.getId()))
                {
                    DTOProduct tempDTOProduct = new DTOProduct();
                    tempDTOProduct.setQuantity(0);
                    tempDTOProduct.setEntityProduct(entityProduct);
                    productWithStocks.add(tempDTOProduct);
                }
            }
            totalProducts = entityManagerProduct.getTotalProducts();
        }        
        
        ClientListResponse response = new ClientListResponse();
        response.setList(productWithStocks);
        response.setCounter(totalProducts);
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
    
    @ClientRequest(action = ACTION.SEARCH_PRODUCTS)
    public ClientResponse searchProducts(ISession session, IPacket packet) throws Exception 
    {
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);     
        String name = jsonObject.get("name").getAsString();
        int typeId = jsonObject.get("typeId").getAsInt();
        int categoryId = jsonObject.get("categoryId").getAsInt();
        int limit = jsonObject.get("limit").getAsInt();
        int offset = jsonObject.get("offset").getAsInt();
        
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
        List<EntityProduct> products = entityManagerProduct.searchProduct(name, typeId, categoryId, offset, limit);
        int totalProducts = entityManagerProduct.searchTotalProducts(name, typeId, categoryId);
        ClientListResponse response = new ClientListResponse();
        response.setList(products);
        response.setCounter(totalProducts);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.SEARCH_PRODUCTS_WITH_STOCKS)
    public ClientResponse searchProductsWithStocks(ISession session, IPacket packet) throws Exception 
    {
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);     
        String name = jsonObject.get("name").getAsString();
        int typeId = jsonObject.get("typeId").getAsInt();
        int categoryId = jsonObject.get("categoryId").getAsInt();
        int limit = jsonObject.get("limit").getAsInt();
        int offset = jsonObject.get("offset").getAsInt();
        
        List<DTOProduct> productWithStocks = new ArrayList<>();
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
        List<EntityProduct> products = entityManagerProduct.searchProduct(name, typeId, categoryId, offset, limit);
        int totalProducts = 0;
        List<Integer> productIds = new ArrayList<>();
        if(products != null && !products.isEmpty())
        {
            for(int counter = 0; counter < products.size(); counter++)
            {
                if(!productIds.contains(products.get(counter).getId()))
                {
                    productIds.add(products.get(counter).getId());
                }
            }
            Stock stock = new Stock(packet.getPacketHeader().getAppId());
            productWithStocks = stock.getCurrentStockByProductIds(productIds);
            List<Integer> excludedProductIds = new ArrayList<>();
            List<Integer> tempProductIds = new ArrayList<>();
            if(productWithStocks != null && !productWithStocks.isEmpty())
            {
                for(int counter = 0; counter < productWithStocks.size(); counter++)
                {
                    if(!tempProductIds.contains(productWithStocks.get(counter).getEntityProduct().getId()))
                    {
                        tempProductIds.add(productWithStocks.get(counter).getEntityProduct().getId());
                    }
                }
            }
            for(int productId: productIds)
            {
                if(!tempProductIds.contains(productId))
                {
                    excludedProductIds.add(productId);
                }
            }
            for(EntityProduct entityProduct: products)
            {
                if(excludedProductIds.contains(entityProduct.getId()))
                {
                    DTOProduct tempDTOProduct = new DTOProduct();
                    tempDTOProduct.setQuantity(0);
                    tempDTOProduct.setEntityProduct(entityProduct);
                    productWithStocks.add(tempDTOProduct);
                }
            }
            totalProducts = entityManagerProduct.searchTotalProducts(name, typeId, categoryId);
        }        
        
        ClientListResponse response = new ClientListResponse();
        response.setList(productWithStocks);
        response.setCounter(totalProducts);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.SEARCH_PRODUCTS_WITH_STOCK_SUPPLIERS_PRICE)
    public ClientResponse searchProductsWithStockSuppliersPrice(ISession session, IPacket packet) throws Exception 
    {
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);     
        String name = jsonObject.get("name").getAsString();
        int typeId = jsonObject.get("typeId").getAsInt();
        int categoryId = jsonObject.get("categoryId").getAsInt();
        int limit = jsonObject.get("limit").getAsInt();
        int offset = jsonObject.get("offset").getAsInt();
        
        List<DTOProduct> productWithStockSuppliersPrice = new ArrayList<>();
        List<DTOProduct> productWithSuppliersPrice = new ArrayList<>();
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct(packet.getPacketHeader().getAppId());
        List<EntityProduct> products = entityManagerProduct.searchProduct(name, typeId, categoryId, offset, limit);
        int totalProducts = 0;
        List<Integer> productIds = new ArrayList<>();
        if(products != null && !products.isEmpty())
        {
            for(int counter = 0; counter < products.size(); counter++)
            {
                if(!productIds.contains(products.get(counter).getId()))
                {
                    productIds.add(products.get(counter).getId());
                }
            } 
            HashMap<Integer, List<EntityProductSupplier>> productIdProductSuppliersMap = new HashMap<>();
            EntityManagerProductSupplier entityManagerProductSupplier = new EntityManagerProductSupplier(packet.getPacketHeader().getAppId());
            List<EntityProductSupplier> entityProductSuppliers = entityManagerProductSupplier.getProductSuppliersByProductIds(productIds);
            if(entityProductSuppliers != null && !entityProductSuppliers.isEmpty())
            {
                for(EntityProductSupplier entityProductSupplier : entityProductSuppliers)
                {
                    if(!productIdProductSuppliersMap.containsKey(entityProductSupplier.getProductId()))
                    {
                        List<EntityProductSupplier> tempEntityProductSuppliers = new ArrayList<>();
                        tempEntityProductSuppliers.add(entityProductSupplier);
                        productIdProductSuppliersMap.put(entityProductSupplier.getProductId(), tempEntityProductSuppliers);
                    }
                    else
                    {
                        productIdProductSuppliersMap.get(entityProductSupplier.getProductId()).add(entityProductSupplier);
                    }
                }
            }
            for(EntityProduct entityProduct : products)
            {
                DTOProduct dtoProduct = new DTOProduct();
                dtoProduct.setEntityProduct(entityProduct);
                if(productIdProductSuppliersMap.containsKey(entityProduct.getId()))
                {
                    dtoProduct.setEntityProductSupplierList(productIdProductSuppliersMap.get(entityProduct.getId()));
                }
                else
                {
                    dtoProduct.setEntityProductSupplierList(new ArrayList<>());
                }
                productWithSuppliersPrice.add(dtoProduct);
            }
            Stock stock = new Stock(packet.getPacketHeader().getAppId());
            List<DTOProduct> productWithStocks = stock.getCurrentStockByProductIds(productIds);
            if(productWithStocks == null)
            {
                productWithStocks = new ArrayList<>();
            }
            List<Integer> excludedProductIds = new ArrayList<>();
            List<Integer> tempProductIds = new ArrayList<>();
            if(!productWithStocks.isEmpty())
            {
                for(int counter = 0; counter < productWithStocks.size(); counter++)
                {
                    if(!tempProductIds.contains(productWithStocks.get(counter).getEntityProduct().getId()))
                    {
                        tempProductIds.add(productWithStocks.get(counter).getEntityProduct().getId());
                    }
                }
            }
            for(int productId: productIds)
            {
                if(!tempProductIds.contains(productId))
                {
                    excludedProductIds.add(productId);
                }
            }
            for(EntityProduct entityProduct: products)
            {
                if(excludedProductIds.contains(entityProduct.getId()))
                {
                    DTOProduct tempDTOProduct = new DTOProduct();
                    tempDTOProduct.setQuantity(0);
                    tempDTOProduct.setEntityProduct(entityProduct);
                    productWithStocks.add(tempDTOProduct);
                }
            }
            HashMap<Integer, Double> productIdQuantityMap = new HashMap<>();
            for(DTOProduct dtoProduct : productWithStocks)
            {
                if(!productIdQuantityMap.containsKey(dtoProduct.getEntityProduct().getId()))
                {
                    productIdQuantityMap.put(dtoProduct.getEntityProduct().getId(), dtoProduct.getQuantity());
                }
            }
            for(DTOProduct dtoProduct : productWithSuppliersPrice)
            {
                if(productIdQuantityMap.containsKey(dtoProduct.getEntityProduct().getId()))
                {
                    dtoProduct.setQuantity(productIdQuantityMap.get(dtoProduct.getEntityProduct().getId()));
                }
                productWithStockSuppliersPrice.add(dtoProduct);
            }
            totalProducts = entityManagerProduct.searchTotalProducts(name, typeId, categoryId);
        }        
        
        ClientListResponse response = new ClientListResponse();
        response.setList(productWithStockSuppliersPrice);
        response.setCounter(totalProducts);
        response.setSuccess(true);
        return response;
    }
}
