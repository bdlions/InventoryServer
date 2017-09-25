package org.bdlions.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import java.util.List;
import org.bdlions.dto.EntityProductCategory;
import org.bdlions.dto.EntityProductType;
import org.bdlions.dto.EntityUOM;
import org.bdlions.dto.ListProductCategory;
import org.bdlions.dto.ListProductType;
import org.bdlions.dto.ListUOM;
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
}
