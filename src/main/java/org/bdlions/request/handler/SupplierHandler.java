package org.bdlions.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.List;
import org.bdlions.dto.DTOSupplier;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.dto.ListSupplier;
import org.bdlions.library.SupplierLibrary;
import org.bdlions.manager.Supplier;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class SupplierHandler {

    private final ISessionManager sessionManager;

    public SupplierHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.ADD_SUPPLIER_INFO)
    public ClientResponse addSupplierInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);     
        SupplierLibrary supplierLibrary = new SupplierLibrary();
        GeneralResponse response = supplierLibrary.createSupplier(dtoSupplier);
        return response;
    }
    
    @ClientRequest(action = ACTION.UPDATE_SUPPLIER_INFO)
    public ClientResponse updateSupplierInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);     
        SupplierLibrary supplierLibrary = new SupplierLibrary();
        GeneralResponse response = supplierLibrary.updateSupplier(dtoSupplier);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_SUPPLIER_INFO)
    public ClientResponse getSupplierInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);      
        Supplier supplier = new Supplier();
        DTOSupplier response = supplier.getSupplierInfo(dtoSupplier.getEntitySupplier());
        if(response == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid supplier. Please try again later");
            return generalResponse;
        }
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_SUPPLIERS)
    public ClientResponse getSuppliers(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);      
        Supplier supplier = new Supplier();
        List<DTOSupplier> suppliers = supplier.getSuppliers(dtoSupplier);
        ListSupplier response = new ListSupplier();
        response.setSuppliers(suppliers);
        response.setSuccess(true);
        return response;
    }
}
