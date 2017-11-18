package org.bdlions.inventory.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.dto.ListSupplier;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.entity.manager.EntityManagerSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.util.Constants;

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
        //GeneralResponse response = new GeneralResponse();
        DTOSupplier responseDTOSupplier = new DTOSupplier();
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);     
        if(dtoSupplier == null || dtoSupplier.getEntityUser() == null)
        {
            responseDTOSupplier.setSuccess(false);
            responseDTOSupplier.setMessage("Invalid Supplier Info. Please try again later.");
            return responseDTOSupplier;
        }
        else if(dtoSupplier.getEntityUser().getFirstName() == null || dtoSupplier.getEntityUser().getFirstName().equals(""))
        {
            responseDTOSupplier.setSuccess(false);
            responseDTOSupplier.setMessage("Supplier First Name is required.");
            return responseDTOSupplier;
        }
        else
        {
            EntityUserRole entityUserRole = new EntityUserRole();
            entityUserRole.setRoleId(Constants.ROLE_ID_SUPPLIER);
            dtoSupplier.setEntityUserRole(entityUserRole);
            EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
            EntitySupplier resultEntitySupplier = entityManagerSupplier.createSupplier(dtoSupplier.getEntitySupplier(), dtoSupplier.getEntityUser(), dtoSupplier.getEntityUserRole());
            if(resultEntitySupplier != null && resultEntitySupplier.getId() > 0)
            {
                //setting EntitySupplier
                responseDTOSupplier.setEntitySupplier(resultEntitySupplier);
                //setting EntityUser
                EntityManagerUser entityManagerUser = new EntityManagerUser();
                responseDTOSupplier.setEntityUser(entityManagerUser.getUserByUserId(resultEntitySupplier.getUserId()));
                
                responseDTOSupplier.setSuccess(true);
                responseDTOSupplier.setMessage("Supplier is added successfully.");
            }
            else
            {
                responseDTOSupplier = new DTOSupplier();            
                responseDTOSupplier.setSuccess(false);
                responseDTOSupplier.setMessage("Unable to create a supplier. Please try again later.");
            }
        }        
        return responseDTOSupplier;
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
            EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
            if(entityManagerSupplier.updateSupplier(dtoSupplier.getEntitySupplier(), dtoSupplier.getEntityUser()))
            {
                response.setSuccess(true);
                response.setMessage("Supplier is updated successfully.");
            }
            else
            {
                response.setSuccess(false);
                response.setMessage("Unable to update supplier. Please try again later.");
            }
        }        
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_SUPPLIER_INFO)
    public ClientResponse getSupplierInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);   
        if(dtoSupplier == null || (dtoSupplier.getEntitySupplier().getId() <= 0 && dtoSupplier.getEntitySupplier().getUserId() <= 0))
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid supplier. Please try again later");
            return generalResponse;
        }
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
        EntitySupplier entitySupplier = null;
        if(dtoSupplier.getEntitySupplier().getId() > 0)
        {
            entitySupplier = entityManagerSupplier.getSupplierBySupplierId(dtoSupplier.getEntitySupplier().getId());
        }
        else if(dtoSupplier.getEntitySupplier().getUserId() > 0)
        {
            entitySupplier = entityManagerSupplier.getSupplierByUserId(dtoSupplier.getEntitySupplier().getUserId());
        }
        
        if(entitySupplier == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid supplier. Please try again later");
            return generalResponse;
        }
        dtoSupplier.setEntitySupplier(entitySupplier);
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        dtoSupplier.setEntityUser(entityManagerUser.getUserByUserId(dtoSupplier.getEntitySupplier().getUserId()));

        dtoSupplier.setSuccess(true);
        return dtoSupplier;
    }
    
    @ClientRequest(action = ACTION.FETCH_SUPPLIERS)
    public ClientResponse getSuppliers(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);  
        if(dtoSupplier == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get supplier list. Please try again later");
            return generalResponse;
        }
        
        List<DTOSupplier> suppliers = new ArrayList<>();
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
        List<EntitySupplier> entitySuppliers = entityManagerSupplier.getSuppliers(dtoSupplier.getOffset(), dtoSupplier.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        for(EntitySupplier entitySupplier : entitySuppliers)
        {
            EntityUser reqEntityUser = new EntityUser();
            reqEntityUser.setId(entitySupplier.getUserId());
            EntityUser entityUser =  entityManagerUser.getUserByUserId(reqEntityUser.getId());

            DTOSupplier tempDTOSupplier = new DTOSupplier();
            tempDTOSupplier.setEntitySupplier(entitySupplier);
            tempDTOSupplier.setEntityUser(entityUser);
            //set user role if required
            suppliers.add(tempDTOSupplier);
        }        
        ListSupplier response = new ListSupplier();
        response.setSuppliers(suppliers);
        response.setSuccess(true);
        return response;
    }
}
