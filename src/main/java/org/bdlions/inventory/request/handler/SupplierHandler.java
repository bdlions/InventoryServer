package org.bdlions.inventory.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.List;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.dto.ListSupplier;
import org.bdlions.inventory.library.SupplierLibrary;
import org.bdlions.inventory.manager.Supplier;

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
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);     
        if(dtoSupplier == null || dtoSupplier.getEntityUser() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Supplier Info. Please try again later.");
        }
        else if(dtoSupplier.getEntityUser().getFirstName() == null || dtoSupplier.getEntityUser().getFirstName().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Supplier First Name is required.");
        }
        else
        {
            SupplierLibrary supplierLibrary = new SupplierLibrary();
            response = supplierLibrary.createSupplier(dtoSupplier);
        }        
        return response;
    }
    
    @ClientRequest(action = ACTION.UPDATE_SUPPLIER_INFO)
    public ClientResponse updateSupplierInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);   
        if(dtoSupplier == null || dtoSupplier.getEntitySupplier() == null || dtoSupplier.getEntityUser() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Supplier Info. Please try again later.");
        }
        else if(dtoSupplier.getEntitySupplier().getId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Supplier Info. Please try again later..");
        }
        else if(dtoSupplier.getEntityUser().getFirstName() == null || dtoSupplier.getEntityUser().getFirstName().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Supplier First Name is required.");
        }
        else
        {
            SupplierLibrary supplierLibrary = new SupplierLibrary();
            response = supplierLibrary.updateSupplier(dtoSupplier);
        }        
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
