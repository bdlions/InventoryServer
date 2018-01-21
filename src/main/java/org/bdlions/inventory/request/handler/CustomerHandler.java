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
import org.bdlions.inventory.dto.DTOCustomer;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.dto.ListCustomer;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.entity.manager.EntityManagerCustomer;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.util.Constants;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class CustomerHandler {

    private final ISessionManager sessionManager;

    public CustomerHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.ADD_CUSTOMER_INFO)
    public ClientResponse addCustomerInfo(ISession session, IPacket packet) throws Exception 
    {
        DTOCustomer responseDTOCustomer = new DTOCustomer();
        //GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);     
        if(dtoCustomer == null || dtoCustomer.getEntityUser() == null)
        {
            responseDTOCustomer.setSuccess(false);
            responseDTOCustomer.setMessage("Invalid Customer Info. Please try again later.");
            return responseDTOCustomer;
        }
        else if(dtoCustomer.getEntityUser().getFirstName() == null || dtoCustomer.getEntityUser().getFirstName().equals(""))
        {
            responseDTOCustomer.setSuccess(false);
            responseDTOCustomer.setMessage("Customer First Name is required.");
            return responseDTOCustomer;
        }
        else
        {
            EntityUserRole entityUserRole = new EntityUserRole();
            entityUserRole.setRoleId(Constants.ROLE_ID_CUSTOMER);
            dtoCustomer.setEntityUserRole(entityUserRole);
            EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
            
            //setting entity customer name, email and cell from entity user
            dtoCustomer.getEntityCustomer().setCustomerName(dtoCustomer.getEntityUser().getUserName());
            dtoCustomer.getEntityCustomer().setEmail(dtoCustomer.getEntityUser().getEmail());
            dtoCustomer.getEntityCustomer().setCell(dtoCustomer.getEntityUser().getCell());
            
            EntityCustomer resultEntityCustomer = entityManagerCustomer.createCustomer(dtoCustomer.getEntityCustomer(), dtoCustomer.getEntityUser(), entityUserRole);
            if(resultEntityCustomer != null && resultEntityCustomer.getId() > 0)
            {
                //setting EntitySupplier
                responseDTOCustomer.setEntityCustomer(resultEntityCustomer);
                //setting EntityUser
                EntityManagerUser entityManagerUser = new EntityManagerUser();
                responseDTOCustomer.setEntityUser(entityManagerUser.getUserByUserId(resultEntityCustomer.getUserId()));
                
                responseDTOCustomer.setSuccess(true);
                responseDTOCustomer.setMessage("Customer is added successfully.");
            }
            else
            {
                responseDTOCustomer = new DTOCustomer();
                responseDTOCustomer.setSuccess(false);
                responseDTOCustomer.setMessage("Unable to create a customer. Please try again later.");
            }
        }        
        return responseDTOCustomer;
    }
    
    @ClientRequest(action = ACTION.UPDATE_CUSTOMER_INFO)
    public ClientResponse updateCustomerInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class); 
        if(dtoCustomer == null || dtoCustomer.getEntityCustomer() == null || dtoCustomer.getEntityUser() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Customer Info. Please try again later.");
        }
        else if(dtoCustomer.getEntityCustomer().getId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Customer Info. Please try again later..");
        }
        else if(dtoCustomer.getEntityUser().getFirstName() == null || dtoCustomer.getEntityUser().getFirstName().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Customer First Name is required.");
        }
        else
        {
            //--------------------if customer first name, last name, cell, email are updated then update EntitySaleOrder for these fields
            
            EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
            
            //setting entity customer name, email and cell from entity user
            dtoCustomer.getEntityCustomer().setCustomerName(dtoCustomer.getEntityUser().getUserName());
            dtoCustomer.getEntityCustomer().setEmail(dtoCustomer.getEntityUser().getEmail());
            dtoCustomer.getEntityCustomer().setCell(dtoCustomer.getEntityUser().getCell());
            
            EntitySaleOrder entitySaleOrder = new EntitySaleOrder();
            entitySaleOrder.setCustomerName(dtoCustomer.getEntityUser().getFirstName() + " " + dtoCustomer.getEntityUser().getLastName());
            entitySaleOrder.setEmail(dtoCustomer.getEntityUser().getEmail());
            entitySaleOrder.setCell(dtoCustomer.getEntityUser().getCell());
            entitySaleOrder.setCustomerUserId(dtoCustomer.getEntityCustomer().getUserId());
            
            if(entityManagerCustomer.updateCustomer(dtoCustomer.getEntityCustomer(), dtoCustomer.getEntityUser(), entitySaleOrder))
            {
                response.setSuccess(true);
                response.setMessage("Customer is updated successfully.");
            }
            else
            {
                response.setSuccess(false);
                response.setMessage("Unable to update customer. Please try again later.");
            }
        }        
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_CUSTOMER_INFO)
    public ClientResponse getCustomerInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);    
        if( dtoCustomer == null || (dtoCustomer.getEntityCustomer().getId() <= 0 && dtoCustomer.getEntityCustomer().getUserId() <= 0) )
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid customer. Please try again later");
            return generalResponse;
        }
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
        EntityCustomer entityCustomer = null;
        if(dtoCustomer.getEntityCustomer().getId() > 0)
        {
            entityCustomer = entityManagerCustomer.getCustomerByCustomerId(dtoCustomer.getEntityCustomer().getId());
        }
        else if(dtoCustomer.getEntityCustomer().getUserId() > 0)
        {
            entityCustomer = entityManagerCustomer.getCustomerByUserId(dtoCustomer.getEntityCustomer().getUserId());
        }
        
        if(entityCustomer == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid customer info. Please try again later");
            return generalResponse;
        }
        dtoCustomer.setEntityCustomer(entityCustomer);
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        dtoCustomer.setEntityUser(entityManagerUser.getUserByUserId(entityCustomer.getUserId()));
        dtoCustomer.setSuccess(true);
        return dtoCustomer;
    }
    
    @ClientRequest(action = ACTION.FETCH_CUSTOMERS)
    public ClientResponse getCustomers(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);
        if( dtoCustomer == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get customer list. Please try again later");
            return generalResponse;
        }
        
        List<DTOCustomer> customers = new ArrayList<>();
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
        List<EntityCustomer> entityCustomers = entityManagerCustomer.getCustomers(dtoCustomer.getOffset(), dtoCustomer.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        for(EntityCustomer entityCustomer : entityCustomers)
        {
            EntityUser reqEntityUser = new EntityUser();
            reqEntityUser.setId(entityCustomer.getUserId());
            EntityUser entityUser =  entityManagerUser.getUserByUserId(reqEntityUser.getId());

            DTOCustomer tempDTOCustomer = new DTOCustomer();
            tempDTOCustomer.setEntityCustomer(entityCustomer);
            tempDTOCustomer.setEntityUser(entityUser);
            customers.add(tempDTOCustomer);
        }
        ListCustomer response = new ListCustomer();
        response.setTotalCustomers(entityManagerCustomer.getTotalCustomers());
        response.setCustomers(customers);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_CUSTOMERS_BY_NAME)
    public ClientResponse getCustomersByName(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);
        if( dtoCustomer == null || dtoCustomer.getEntityCustomer() == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get customer list. Please try again later");
            return generalResponse;
        }
        
        List<DTOCustomer> customers = new ArrayList<>();
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
        List<EntityCustomer> entityCustomers = entityManagerCustomer.searchCustomersByName(dtoCustomer.getEntityCustomer().getCustomerName(), dtoCustomer.getOffset(), dtoCustomer.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        for(EntityCustomer entityCustomer : entityCustomers)
        {
            EntityUser reqEntityUser = new EntityUser();
            reqEntityUser.setId(entityCustomer.getUserId());
            EntityUser entityUser =  entityManagerUser.getUserByUserId(reqEntityUser.getId());

            DTOCustomer tempDTOCustomer = new DTOCustomer();
            tempDTOCustomer.setEntityCustomer(entityCustomer);
            tempDTOCustomer.setEntityUser(entityUser);
            customers.add(tempDTOCustomer);
        }
        ListCustomer response = new ListCustomer();
        response.setTotalCustomers(entityManagerCustomer.searchTotalCustomersByName(dtoCustomer.getEntityCustomer().getCustomerName()));
        response.setCustomers(customers);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_CUSTOMERS_BY_CELL)
    public ClientResponse getCustomersByCell(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);
        if( dtoCustomer == null || dtoCustomer.getEntityCustomer() == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get customer list. Please try again later");
            return generalResponse;
        }
        
        List<DTOCustomer> customers = new ArrayList<>();
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
        List<EntityCustomer> entityCustomers = entityManagerCustomer.searchCustomersByCell(dtoCustomer.getEntityCustomer().getCell(), dtoCustomer.getOffset(), dtoCustomer.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        for(EntityCustomer entityCustomer : entityCustomers)
        {
            EntityUser reqEntityUser = new EntityUser();
            reqEntityUser.setId(entityCustomer.getUserId());
            EntityUser entityUser =  entityManagerUser.getUserByUserId(reqEntityUser.getId());

            DTOCustomer tempDTOCustomer = new DTOCustomer();
            tempDTOCustomer.setEntityCustomer(entityCustomer);
            tempDTOCustomer.setEntityUser(entityUser);
            customers.add(tempDTOCustomer);
        }
        ListCustomer response = new ListCustomer();
        response.setTotalCustomers(entityManagerCustomer.searchTotalCustomersByCell(dtoCustomer.getEntityCustomer().getCell()));
        response.setCustomers(customers);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_CUSTOMERS_BY_EMAIL)
    public ClientResponse getCustomersByEmail(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);
        if( dtoCustomer == null || dtoCustomer.getEntityCustomer() == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get customer list. Please try again later");
            return generalResponse;
        }
        
        List<DTOCustomer> customers = new ArrayList<>();
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
        List<EntityCustomer> entityCustomers = entityManagerCustomer.searchCustomersByEmail(dtoCustomer.getEntityCustomer().getEmail(), dtoCustomer.getOffset(), dtoCustomer.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        for(EntityCustomer entityCustomer : entityCustomers)
        {
            EntityUser reqEntityUser = new EntityUser();
            reqEntityUser.setId(entityCustomer.getUserId());
            EntityUser entityUser =  entityManagerUser.getUserByUserId(reqEntityUser.getId());

            DTOCustomer tempDTOCustomer = new DTOCustomer();
            tempDTOCustomer.setEntityCustomer(entityCustomer);
            tempDTOCustomer.setEntityUser(entityUser);
            customers.add(tempDTOCustomer);
        }
        ListCustomer response = new ListCustomer();
        response.setTotalCustomers(entityManagerCustomer.searchTotalCustomersByEmail(dtoCustomer.getEntityCustomer().getEmail()));
        response.setCustomers(customers);
        response.setSuccess(true);
        return response;
    }
}
