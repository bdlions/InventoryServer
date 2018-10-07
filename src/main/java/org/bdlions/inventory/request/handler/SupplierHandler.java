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
import java.util.List;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.dto.ListSupplier;
import org.bdlions.inventory.entity.EntityProductSupplier;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntityPurchaseOrderPayment;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.entity.manager.EntityManagerProductSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrderPayment;
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
        else if(dtoSupplier.getEntityUser().getUserName() == null || dtoSupplier.getEntityUser().getUserName().equals(""))
        {
            responseDTOSupplier.setSuccess(false);
            responseDTOSupplier.setMessage("Name is required.");
            return responseDTOSupplier;
        }
        else
        {
            //check whether supplier product list data is valid or not
            List<EntityProductSupplier> entityProductSupplierList = dtoSupplier.getEntityProductSupplierList();
            if(entityProductSupplierList != null && !entityProductSupplierList.isEmpty())
            {
                for(int counter = 0; counter < entityProductSupplierList.size(); counter++)
                {
                    if(entityProductSupplierList.get(counter).getProductId() <= 0)
                    {
                        responseDTOSupplier.setSuccess(false);
                        responseDTOSupplier.setMessage("Invalid product in Product List. Please try again later.");
                        return responseDTOSupplier;
                    }                    
                }
            }
            
            EntityPurchaseOrderPayment entityPurchaseOrderPaymentIn = null;
            if(dtoSupplier.getEntitySupplier().getPreviousBalance() >= 0.0)
            {
                entityPurchaseOrderPaymentIn = new EntityPurchaseOrderPayment();
                entityPurchaseOrderPaymentIn.setAmountIn(dtoSupplier.getEntitySupplier().getPreviousBalance());
                entityPurchaseOrderPaymentIn.setAmountOut(0.0);
                entityPurchaseOrderPaymentIn.setPaymentTypeId(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ID_ADD_PREVIOUS_DUE_IN);
                entityPurchaseOrderPaymentIn.setDescription(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ADD_ADD_PREVIOUS_DUE_IN_DESCRIPTION);
                
                //setting created by and modified by user
                int userId = (int)session.getUserId();
                entityPurchaseOrderPaymentIn.setCreatedByUserId(userId);
                entityPurchaseOrderPaymentIn.setModifiedByUserId(userId);
                EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
                EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
                if(tempEntityUser != null && tempEntityUser.getId() > 0)
                {
                    entityPurchaseOrderPaymentIn.setCreatedByUserName(tempEntityUser.getUserName());
                    entityPurchaseOrderPaymentIn.setModifiedByUserName(tempEntityUser.getUserName());
                }  
            }
            
            EntityUserRole entityUserRole = new EntityUserRole();
            entityUserRole.setRoleId(Constants.ROLE_ID_SUPPLIER);
            dtoSupplier.setEntityUserRole(entityUserRole);
            EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(packet.getPacketHeader().getAppId());
            
            //setting entity supplier name, email and cell from entity user
            dtoSupplier.getEntitySupplier().setSupplierName(dtoSupplier.getEntityUser().getUserName());            
            dtoSupplier.getEntitySupplier().setEmail(dtoSupplier.getEntityUser().getEmail());
            dtoSupplier.getEntitySupplier().setCell(dtoSupplier.getEntityUser().getCell());
            
            EntitySupplier resultEntitySupplier = entityManagerSupplier.createSupplier(dtoSupplier.getEntitySupplier(), dtoSupplier.getEntityUser(), dtoSupplier.getEntityUserRole(), dtoSupplier.getEntityProductSupplierList(), entityPurchaseOrderPaymentIn);
            if(resultEntitySupplier != null && resultEntitySupplier.getId() > 0)
            {
                //setting EntitySupplier
                responseDTOSupplier.setEntitySupplier(resultEntitySupplier);
                //setting EntityUser
                EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
        //GeneralResponse response = new GeneralResponse();
        DTOSupplier responseDTOSupplier = new DTOSupplier();
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);   
        if(dtoSupplier == null || dtoSupplier.getEntitySupplier() == null || dtoSupplier.getEntityUser() == null)
        {
            responseDTOSupplier.setSuccess(false);
            responseDTOSupplier.setMessage("Invalid Supplier Info. Please try again later.");
        }
        else if(dtoSupplier.getEntitySupplier().getId() <= 0)
        {
            responseDTOSupplier.setSuccess(false);
            responseDTOSupplier.setMessage("Invalid Supplier Info. Please try again later..");
        }
        else if(dtoSupplier.getEntityUser().getUserName() == null || dtoSupplier.getEntityUser().getUserName().equals(""))
        {
            responseDTOSupplier.setSuccess(false);
            responseDTOSupplier.setMessage("Name is required.");
        }
        else
        {
            //check whether supplier product list data is valid or not
            List<EntityProductSupplier> tempEPSList = dtoSupplier.getEntityProductSupplierList();
            if(tempEPSList != null && !tempEPSList.isEmpty())
            {
                for(int counter = 0; counter < tempEPSList.size(); counter++)
                {
                    if(tempEPSList.get(counter).getProductId() <= 0)
                    {
                        responseDTOSupplier.setSuccess(false);
                        responseDTOSupplier.setMessage("Invalid product in Product List. Please try again later.");
                        return responseDTOSupplier;
                    }                    
                }
            }
            
            EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(packet.getPacketHeader().getAppId());
            
            //setting entity supplier name, email and cell from entity user
            dtoSupplier.getEntitySupplier().setSupplierName(dtoSupplier.getEntityUser().getUserName());
            dtoSupplier.getEntitySupplier().setEmail(dtoSupplier.getEntityUser().getEmail());
            dtoSupplier.getEntitySupplier().setCell(dtoSupplier.getEntityUser().getCell());
            
            //setting purchase order info to be updated if supplier info is updated
            EntityPurchaseOrder entityPurchaseOrder = new EntityPurchaseOrder();
            entityPurchaseOrder.setSupplierName(dtoSupplier.getEntityUser().getUserName());
            entityPurchaseOrder.setEmail(dtoSupplier.getEntityUser().getEmail());
            entityPurchaseOrder.setCell(dtoSupplier.getEntityUser().getCell());
            entityPurchaseOrder.setSupplierUserId(dtoSupplier.getEntitySupplier().getUserId());
            
            List<EntityProductSupplier> entityProductSupplierList = dtoSupplier.getEntityProductSupplierList();  
            List<Integer> productIds = new ArrayList<>();
            if(entityProductSupplierList != null && !entityProductSupplierList.isEmpty())
            {
                for(EntityProductSupplier entityProductSupplier: entityProductSupplierList)
                {
                    if(!productIds.contains(entityProductSupplier.getProductId()))
                    {
                        productIds.add(entityProductSupplier.getProductId());
                    }
                }
            }
            List<EntityProductSupplier> epsList = dtoSupplier.getEpsListToBeDeleted(); 
            if(epsList != null && !epsList.isEmpty())
            {
                for(EntityProductSupplier entityProductSupplier: epsList)
                {
                    if(!productIds.contains(entityProductSupplier.getProductId()))
                    {
                        productIds.add(entityProductSupplier.getProductId());
                    }
                }
            }
            
            EntityPurchaseOrderPayment entityPurchaseOrderPaymentIn = null;
            if(dtoSupplier.getEntitySupplier().getPreviousBalance() >= 0.0)
            {
                entityPurchaseOrderPaymentIn = new EntityPurchaseOrderPayment();
                entityPurchaseOrderPaymentIn.setAmountIn(dtoSupplier.getEntitySupplier().getPreviousBalance());
                entityPurchaseOrderPaymentIn.setAmountOut(0.0);
                entityPurchaseOrderPaymentIn.setPaymentTypeId(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ID_ADD_PREVIOUS_DUE_IN);
                entityPurchaseOrderPaymentIn.setDescription(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ADD_ADD_PREVIOUS_DUE_IN_DESCRIPTION);
                
                //setting created by and modified by user
                int userId = (int)session.getUserId();
                entityPurchaseOrderPaymentIn.setCreatedByUserId(userId);
                entityPurchaseOrderPaymentIn.setModifiedByUserId(userId);
                EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
                EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
                if(tempEntityUser != null && tempEntityUser.getId() > 0)
                {
                    entityPurchaseOrderPaymentIn.setCreatedByUserName(tempEntityUser.getUserName());
                    entityPurchaseOrderPaymentIn.setModifiedByUserName(tempEntityUser.getUserName());
                }  
            }
            
            if(entityManagerSupplier.updateSupplier(dtoSupplier.getEntitySupplier(), dtoSupplier.getEntityUser(), entityPurchaseOrder, entityProductSupplierList, productIds, entityPurchaseOrderPaymentIn))
            {
                EntitySupplier resultEntitySupplier = entityManagerSupplier.getSupplierByUserId(dtoSupplier.getEntitySupplier().getUserId());
                responseDTOSupplier = dtoSupplier;
                responseDTOSupplier.setEntitySupplier(resultEntitySupplier);
                responseDTOSupplier.setSuccess(true);                
                responseDTOSupplier.setMessage("Supplier is updated successfully.");
            }
            else
            {
                responseDTOSupplier.setSuccess(false);
                responseDTOSupplier.setMessage("Unable to update supplier. Please try again later.");
            }
        }        
        return responseDTOSupplier;
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
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(packet.getPacketHeader().getAppId());
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
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
        dtoSupplier.setEntityUser(entityManagerUser.getUserByUserId(dtoSupplier.getEntitySupplier().getUserId()));

        dtoSupplier.setSuccess(true);
        return dtoSupplier;
    }
    
    @ClientRequest(action = ACTION.FETCH_SUPPLIER_PRODUCT_LIST)
    public ClientResponse getSupplierProductList(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        ClientListResponse response = new ClientListResponse();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);     
        if( dtoSupplier == null || dtoSupplier.getEntityUser() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid request to get product supplier list. Please try again later");
            return response;
        } 
        EntityManagerProductSupplier entityManagerProductSupplier = new EntityManagerProductSupplier(packet.getPacketHeader().getAppId());
        List<EntityProductSupplier> entityProductSupplierList = entityManagerProductSupplier.getProductSuppliersBySupplierUserId(dtoSupplier.getEntityUser().getId(), dtoSupplier.getOffset(), dtoSupplier.getLimit());
        int counter = entityManagerProductSupplier.getTotalProductSuppliersBySupplierUserId(dtoSupplier.getEntityUser().getId());
        response.setList(entityProductSupplierList);
        response.setCounter(counter);
        response.setSuccess(true);        
        return response;
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
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(packet.getPacketHeader().getAppId());
        List<EntitySupplier> entitySuppliers = entityManagerSupplier.getSuppliers(dtoSupplier.getOffset(), dtoSupplier.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
        response.setTotalSuppliers(entityManagerSupplier.getTotalSuppliers());
        response.setSuppliers(suppliers);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_SUPPLIERS_BY_NAME)
    public ClientResponse getSuppliersByName(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);  
        if(dtoSupplier == null || dtoSupplier.getEntitySupplier() == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get supplier list. Please try again later");
            return generalResponse;
        }
        
        List<DTOSupplier> suppliers = new ArrayList<>();
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(packet.getPacketHeader().getAppId());
        List<EntitySupplier> entitySuppliers = entityManagerSupplier.searchSuppliersByName(dtoSupplier.getEntitySupplier().getSupplierName(), dtoSupplier.getOffset(), dtoSupplier.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
        response.setTotalSuppliers(entityManagerSupplier.searchTotalSuppliersByName(dtoSupplier.getEntitySupplier().getSupplierName()));
        response.setSuppliers(suppliers);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_SUPPLIERS_BY_CELL)
    public ClientResponse getSuppliersByCell(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);  
        if(dtoSupplier == null || dtoSupplier.getEntitySupplier() == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get supplier list. Please try again later");
            return generalResponse;
        }
        
        List<DTOSupplier> suppliers = new ArrayList<>();
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(packet.getPacketHeader().getAppId());
        List<EntitySupplier> entitySuppliers = entityManagerSupplier.searchSuppliersByCell(dtoSupplier.getEntitySupplier().getCell(), dtoSupplier.getOffset(), dtoSupplier.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
        response.setTotalSuppliers(entityManagerSupplier.searchTotalSuppliersByCell(dtoSupplier.getEntitySupplier().getCell()));
        response.setSuppliers(suppliers);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_SUPPLIERS_BY_EMAIL)
    public ClientResponse getSuppliersByEmail(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOSupplier dtoSupplier = gson.fromJson(packet.getPacketBody(), DTOSupplier.class);  
        if(dtoSupplier == null || dtoSupplier.getEntitySupplier() == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid request to get supplier list. Please try again later");
            return generalResponse;
        }
        
        List<DTOSupplier> suppliers = new ArrayList<>();
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(packet.getPacketHeader().getAppId());
        List<EntitySupplier> entitySuppliers = entityManagerSupplier.searchSuppliersByEmail(dtoSupplier.getEntitySupplier().getEmail(), dtoSupplier.getOffset(), dtoSupplier.getLimit());
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
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
        response.setTotalSuppliers(entityManagerSupplier.searchTotalSuppliersByEmail(dtoSupplier.getEntitySupplier().getEmail()));
        response.setSuppliers(suppliers);
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.ADD_PURCHASE_ORDER_PAYMENT)
    public ClientResponse addPurchaseOrderPayment(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        EntityPurchaseOrderPayment entityPurchaseOrderPayment = gson.fromJson(packet.getPacketBody(), EntityPurchaseOrderPayment.class); 
        //validation of entityPuchaseOrderPayment
        if(entityPurchaseOrderPayment == null)
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid purchase order payment.");
            return response;
        }
        if(entityPurchaseOrderPayment.getSupplierUserId() == 0)
        {
            GeneralResponse response = new GeneralResponse();
            response.setSuccess(false);
            response.setMessage("Invalid supplier for the purchase order payment.");
            return response;
        }
        //setting created by and modified by user
        int userId = (int)session.getUserId();
        entityPurchaseOrderPayment.setCreatedByUserId(userId);
        entityPurchaseOrderPayment.setModifiedByUserId(userId);
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
        EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
        if(tempEntityUser != null && tempEntityUser.getId() > 0)
        {
            entityPurchaseOrderPayment.setCreatedByUserName(tempEntityUser.getUserName());
            entityPurchaseOrderPayment.setModifiedByUserName(tempEntityUser.getUserName());
        } 
        if(!StringUtils.isNullOrEmpty(entityPurchaseOrderPayment.getPaymentDate()))
        {
            entityPurchaseOrderPayment.setUnixPaymentDate(TimeUtils.convertHumanToUnix(entityPurchaseOrderPayment.getPaymentDate(), "", ""));
        }
        else
        {
            entityPurchaseOrderPayment.setUnixPaymentDate(TimeUtils.getCurrentTime());
            entityPurchaseOrderPayment.setPaymentDate(TimeUtils.getCurrentDate("", ""));
        }
        entityPurchaseOrderPayment.setPaymentTypeId(Constants.PURCHASE_ORDER_PAYMENT_TYPE_ID_ADD_NEW_PAYMENT_OUT);
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(packet.getPacketHeader().getAppId());
        entityPurchaseOrderPayment = entityManagerSupplier.addSupplierPurchasePayment(entityPurchaseOrderPayment);
        entityPurchaseOrderPayment.setSuccess(true);
        return entityPurchaseOrderPayment;
    }
    
    @ClientRequest(action = ACTION.UPDATE_PURCHASE_ORDER_PAYMENT)
    public ClientResponse updatePurchaseOrderPayment(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        EntityPurchaseOrderPayment entityPurchaseOrderPayment = gson.fromJson(packet.getPacketBody(), EntityPurchaseOrderPayment.class); 
        //validation of entityPuchaseOrderPayment
        if(entityPurchaseOrderPayment == null || entityPurchaseOrderPayment.getId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid purchase order payment.");
            return response;
        }
        if(entityPurchaseOrderPayment.getSupplierUserId() == 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid supplier for the purchase order payment.");
            return response;
        }
        //setting created by and modified by user
        int userId = (int)session.getUserId();
        entityPurchaseOrderPayment.setModifiedByUserId(userId);
        EntityManagerUser entityManagerUser = new EntityManagerUser(packet.getPacketHeader().getAppId());
        EntityUser tempEntityUser = entityManagerUser.getUserByUserId(userId);
        if(tempEntityUser != null && tempEntityUser.getId() > 0)
        {
            entityPurchaseOrderPayment.setModifiedByUserName(tempEntityUser.getUserName());
        } 
        if(!StringUtils.isNullOrEmpty(entityPurchaseOrderPayment.getPaymentDate()))
        {
            entityPurchaseOrderPayment.setUnixPaymentDate(TimeUtils.convertHumanToUnix(entityPurchaseOrderPayment.getPaymentDate(), "", ""));
        }
        else
        {
            entityPurchaseOrderPayment.setUnixPaymentDate(TimeUtils.getCurrentTime());
            entityPurchaseOrderPayment.setPaymentDate(TimeUtils.getCurrentDate("", ""));
        }
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(packet.getPacketHeader().getAppId());
        if(entityManagerSupplier.updateSupplierPurchasePayment(entityPurchaseOrderPayment))
        {            
            response.setSuccess(true);            
        }
        else
        {
            response.setSuccess(false);
        }
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_ENTITY_SUPPLIER_INFO)
    public ClientResponse getEntitySupplierInfo(ISession session, IPacket packet) throws Exception 
    {
        EntitySupplier entitySupplier;
        JsonObject jsonObject = new Gson().fromJson(packet.getPacketBody(), JsonObject.class);  
        int supplierUserId = jsonObject.get("supplierUserId").getAsInt();
        if(supplierUserId == 0)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Supplier invalid or doesn't exist.");
            return generalResponse;
        }
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(packet.getPacketHeader().getAppId());
        entitySupplier = entityManagerSupplier.getSupplierByUserId(supplierUserId);
        if(entitySupplier == null || entitySupplier.getUserId() == 0)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Supplier invalid or doesn't exist.");
            return generalResponse;
        }
        entitySupplier.setSuccess(true);
        return entitySupplier;
    }
}
