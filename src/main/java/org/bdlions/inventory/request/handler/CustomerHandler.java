package org.bdlions.inventory.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.dto.DTOCustomer;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.dto.ListCustomer;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntityPurchaseOrderPayment;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderPayment;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.entity.manager.EntityManagerCustomer;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrderPayment;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderPayment;
import org.bdlions.inventory.entity.manager.EntityManagerSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.util.Constants;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;

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
        else if(StringUtils.isNullOrEmpty(dtoCustomer.getEntityUser().getUserName()))
        {
            responseDTOCustomer.setSuccess(false);
            responseDTOCustomer.setMessage("Name is required.");
            return responseDTOCustomer;
        }
        else
        {
            
            EntitySaleOrderPayment entitySaleOrderPaymentIn = null;
            if(dtoCustomer.getEntityCustomer().getPreviousBalance() >= 0.0)
            {
                entitySaleOrderPaymentIn = new EntitySaleOrderPayment();
                entitySaleOrderPaymentIn.setAmountIn(dtoCustomer.getEntityCustomer().getPreviousBalance());
                entitySaleOrderPaymentIn.setAmountOut(0.0);
                entitySaleOrderPaymentIn.setPaymentTypeId(Constants.SALE_ORDER_PAYMENT_TYPE_ID_ADD_PREVIOUS_DUE_IN);
                entitySaleOrderPaymentIn.setDescription(Constants.SALE_ORDER_PAYMENT_TYPE_ADD_ADD_PREVIOUS_DUE_IN_DESCRIPTION);
                
                //setting created by and modified by user
                int userId = (int)session.getUserId();
                entitySaleOrderPaymentIn.setCreatedByUserId(userId);
                entitySaleOrderPaymentIn.setModifiedByUserId(userId);
                EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
                EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
                if(tempEntityUser != null && tempEntityUser.getId() > 0)
                {
                    entitySaleOrderPaymentIn.setCreatedByUserName(tempEntityUser.getUserName());
                    entitySaleOrderPaymentIn.setModifiedByUserName(tempEntityUser.getUserName());
                }  
            }
            
            EntityUserRole entityUserRole = new EntityUserRole();
            entityUserRole.setRoleId(Constants.ROLE_ID_CUSTOMER);
            dtoCustomer.setEntityUserRole(entityUserRole);
            EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(packet.getPacketHeader().getAppId());
            
            //setting entity customer name, email and cell from entity user
            dtoCustomer.getEntityCustomer().setCustomerName(dtoCustomer.getEntityUser().getUserName());
            dtoCustomer.getEntityCustomer().setEmail(dtoCustomer.getEntityUser().getEmail());
            dtoCustomer.getEntityCustomer().setCell(dtoCustomer.getEntityUser().getCell());
            
            EntityCustomer resultEntityCustomer = entityManagerCustomer.createCustomer(dtoCustomer.getEntityCustomer(), dtoCustomer.getEntityUser(), entityUserRole, entitySaleOrderPaymentIn);
            if(resultEntityCustomer != null && resultEntityCustomer.getId() > 0)
            {
                //setting EntitySupplier
                responseDTOCustomer.setEntityCustomer(resultEntityCustomer);
                //setting EntityUser
                EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
        DTOCustomer response = new DTOCustomer();
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
        else if(StringUtils.isNullOrEmpty(dtoCustomer.getEntityUser().getUserName()))
        {
            response.setSuccess(false);
            response.setMessage("Name is required.");
        }
        else
        {
            //--------------------if customer first name, last name, cell, email are updated then update EntitySaleOrder for these fields
            
            EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(packet.getPacketHeader().getAppId());
            
            //setting entity customer name, email and cell from entity user
            dtoCustomer.getEntityCustomer().setCustomerName(dtoCustomer.getEntityUser().getUserName());
            dtoCustomer.getEntityCustomer().setEmail(dtoCustomer.getEntityUser().getEmail());
            dtoCustomer.getEntityCustomer().setCell(dtoCustomer.getEntityUser().getCell());
            
            EntitySaleOrder entitySaleOrder = new EntitySaleOrder();
            entitySaleOrder.setCustomerName(dtoCustomer.getEntityUser().getUserName());
            entitySaleOrder.setEmail(dtoCustomer.getEntityUser().getEmail());
            entitySaleOrder.setCell(dtoCustomer.getEntityUser().getCell());
            entitySaleOrder.setCustomerUserId(dtoCustomer.getEntityCustomer().getUserId());
            
            EntitySaleOrderPayment entitySaleOrderPaymentIn = null;
            if(dtoCustomer.getEntityCustomer().getPreviousBalance() >= 0.0)
            {
                entitySaleOrderPaymentIn = new EntitySaleOrderPayment();
                entitySaleOrderPaymentIn.setAmountIn(dtoCustomer.getEntityCustomer().getPreviousBalance());
                entitySaleOrderPaymentIn.setAmountOut(0.0);
                entitySaleOrderPaymentIn.setPaymentTypeId(Constants.SALE_ORDER_PAYMENT_TYPE_ID_ADD_PREVIOUS_DUE_IN);
                entitySaleOrderPaymentIn.setDescription(Constants.SALE_ORDER_PAYMENT_TYPE_ADD_ADD_PREVIOUS_DUE_IN_DESCRIPTION);
                
                //setting created by and modified by user
                int userId = (int)session.getUserId();
                entitySaleOrderPaymentIn.setCreatedByUserId(userId);
                entitySaleOrderPaymentIn.setModifiedByUserId(userId);
                EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
                EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
                if(tempEntityUser != null && tempEntityUser.getId() > 0)
                {
                    entitySaleOrderPaymentIn.setCreatedByUserName(tempEntityUser.getUserName());
                    entitySaleOrderPaymentIn.setModifiedByUserName(tempEntityUser.getUserName());
                }  
            }
            
            if(entityManagerCustomer.updateCustomer(dtoCustomer.getEntityCustomer(), dtoCustomer.getEntityUser(), entitySaleOrder, entitySaleOrderPaymentIn))
            {
                EntityCustomer resultEntityCustomer = entityManagerCustomer.getCustomerByUserId(dtoCustomer.getEntityCustomer().getUserId());
                response = dtoCustomer;
                response.setEntityCustomer(resultEntityCustomer);
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
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(packet.getPacketHeader().getAppId());
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
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(packet.getPacketHeader().getAppId());
        List<EntityCustomer> entityCustomers = entityManagerCustomer.getCustomers(dtoCustomer.getOffset(), dtoCustomer.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(packet.getPacketHeader().getAppId());
        List<EntityCustomer> entityCustomers = entityManagerCustomer.searchCustomersByName(dtoCustomer.getEntityCustomer().getCustomerName(), dtoCustomer.getOffset(), dtoCustomer.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(packet.getPacketHeader().getAppId());
        List<EntityCustomer> entityCustomers = entityManagerCustomer.searchCustomersByCell(dtoCustomer.getEntityCustomer().getCell(), dtoCustomer.getOffset(), dtoCustomer.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(packet.getPacketHeader().getAppId());
        List<EntityCustomer> entityCustomers = entityManagerCustomer.searchCustomersByEmail(dtoCustomer.getEntityCustomer().getEmail(), dtoCustomer.getOffset(), dtoCustomer.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
    
    @ClientRequest(action = ACTION.ADD_SALE_ORDER_PAYMENT)
    public ClientResponse addSaleOrderPayment(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        EntitySaleOrderPayment entitySaleOrderPayment = gson.fromJson(packet.getPacketBody(), EntitySaleOrderPayment.class); 
        //validation of entitySaleOrderPayment
        if(entitySaleOrderPayment == null)
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid sale order payment.");
            return response;
        }
        if(entitySaleOrderPayment.getCustomerUserId() == 0)
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid customer for the purchase order payment.");
            return response;
        }
        //setting created by and modified by user
        int userId = (int)session.getUserId();
        entitySaleOrderPayment.setCreatedByUserId(userId);
        entitySaleOrderPayment.setModifiedByUserId(userId);
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
        EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
        if(tempEntityUser != null && tempEntityUser.getId() > 0)
        {
            entitySaleOrderPayment.setCreatedByUserName(tempEntityUser.getUserName());
            entitySaleOrderPayment.setModifiedByUserName(tempEntityUser.getUserName());
        } 
        if(!StringUtils.isNullOrEmpty(entitySaleOrderPayment.getPaymentDate()))
        {
            entitySaleOrderPayment.setUnixPaymentDate(TimeUtils.convertHumanToUnix(entitySaleOrderPayment.getPaymentDate(), "", ""));
        }
        else
        {
            entitySaleOrderPayment.setUnixPaymentDate(TimeUtils.getCurrentTime());
            entitySaleOrderPayment.setPaymentDate(TimeUtils.getCurrentDate("", ""));
        }
        entitySaleOrderPayment.setPaymentTypeId(Constants.SALE_ORDER_PAYMENT_TYPE_ID_ADD_NEW_PAYMENT_OUT);
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(packet.getPacketHeader().getAppId());
        entitySaleOrderPayment = entityManagerCustomer.addCustomerSalePayment(entitySaleOrderPayment);
        entitySaleOrderPayment.setSuccess(true);
        return entitySaleOrderPayment;
    }
    
    @ClientRequest(action = ACTION.UPDATE_SALE_ORDER_PAYMENT)
    public ClientResponse updateSaleOrderPayment(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        EntitySaleOrderPayment entitySaleOrderPayment = gson.fromJson(packet.getPacketBody(), EntitySaleOrderPayment.class); 
        //validation of entitySaleOrderPayment
        if(entitySaleOrderPayment == null || entitySaleOrderPayment.getId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid sale order payment.");
            return response;
        }
        if(entitySaleOrderPayment.getCustomerUserId() == 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid customer for the sale order payment.");
            return response;
        }
        //setting created by and modified by user
        int userId = (int)session.getUserId();
        entitySaleOrderPayment.setModifiedByUserId(userId);
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
        EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
        if(tempEntityUser != null && tempEntityUser.getId() > 0)
        {
            entitySaleOrderPayment.setModifiedByUserName(tempEntityUser.getUserName());
        } 
        if(!StringUtils.isNullOrEmpty(entitySaleOrderPayment.getPaymentDate()))
        {
            entitySaleOrderPayment.setUnixPaymentDate(TimeUtils.convertHumanToUnix(entitySaleOrderPayment.getPaymentDate(), "", ""));
        }
        else
        {
            entitySaleOrderPayment.setUnixPaymentDate(TimeUtils.getCurrentTime());
            entitySaleOrderPayment.setPaymentDate(TimeUtils.getCurrentDate("", ""));
        }
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(packet.getPacketHeader().getAppId());
        if(entityManagerCustomer.updateCustomerSalePayment(entitySaleOrderPayment))
        {            
            response.setSuccess(true);            
        }
        else
        {
            response.setSuccess(false);
        }
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_ENTITY_CUSTOMER_INFO)
    public ClientResponse getEntityCustomerInfo(ISession session, IPacket packet) throws Exception 
    {
        EntityCustomer entityCustomer;
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);  
        int customerUserId = jsonObject.get("customerUserId").getAsInt();
        if(customerUserId == 0)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Customer invalid or doesn't exist.");
            return generalResponse;
        }
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(packet.getPacketHeader().getAppId());
        entityCustomer = entityManagerCustomer.getCustomerByUserId(customerUserId);
        if(entityCustomer == null || entityCustomer.getUserId() == 0)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Customer invalid or doesn't exist.");
            return generalResponse;
        }
        entityCustomer.setSuccess(true);
        return entityCustomer;
    }
    
    @ClientRequest(action = ACTION.FETCH_CUSTOMER_SALE_AND_PAYMENT_AMOUNT)
    public ClientResponse getCustomerSaleAndPaymentAmount(ISession session, IPacket packet) throws Exception 
    {
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);  
        int customerUserId = jsonObject.get("customerUserId").getAsInt();
        if(customerUserId == 0)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Customer invalid or doesn't exist.");
            return generalResponse;
        }
        EntityManagerSaleOrderPayment entityManagerSaleOrderPayment = new EntityManagerSaleOrderPayment(packet.getPacketHeader().getAppId());
        DTOCustomer dtoCustomer = entityManagerSaleOrderPayment.getCustomerPurchaseAndPaymentAmount(customerUserId);
        dtoCustomer.setSuccess(true);
        return dtoCustomer;
    }
}
