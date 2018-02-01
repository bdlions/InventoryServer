package org.bdlions.inventory.request.handler;

import com.bdlions.dto.response.ClientListResponse;
import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.bdlions.inventory.dto.DTOUser;
import org.bdlions.inventory.entity.EntityRole;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.entity.manager.EntityManagerRole;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.entity.manager.EntityManagerUserRole;
import org.bdlions.inventory.util.Constants;


//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class UserHandler {

    private final ISessionManager sessionManager;

    public UserHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.FETCH_USER_INFO)
    public ClientResponse getProfileInfo(ISession session, IPacket packet) throws Exception 
    {
        int userId = (int)session.getUserId();
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        EntityUser entiryUser = entityManagerUser.getUserByUserId(userId);
        entiryUser.setMessage("User Info.");
        entiryUser.setSuccess(true);
        return entiryUser;
    }
    
    @ClientRequest(action = ACTION.FETCH_USER_WITH_ROLES)
    public ClientResponse getUserWithRoles(ISession session, IPacket packet) throws Exception 
    {
        DTOUser response = new DTOUser();
        Gson gson = new Gson();
        EntityUser reqEntityUser = gson.fromJson(packet.getPacketBody(), EntityUser.class);  
        if(reqEntityUser == null || reqEntityUser.getId() == 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid request to get user info. Please try again later.");
            return response;
        }
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        EntityUser entityUser = entityManagerUser.getUserByUserId(reqEntityUser.getId());
        if(entityUser != null)
        {
            EntityManagerUserRole entityManagerUserRole = new EntityManagerUserRole();
            List<EntityUserRole> entityUserRoles = entityManagerUserRole.getUserRolesByUserId(reqEntityUser.getId());
            response.setEntityUser(entityUser);
            response.setEntityUserRoles(entityUserRoles);
            response.setSuccess(true);
        }
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_USERS)
    public ClientResponse getUsers(ISession session, IPacket packet) throws Exception 
    {
        ClientListResponse response = new ClientListResponse();
        response.setSuccess(true);
        EntityManagerUserRole entityManagerUserRole = new EntityManagerUserRole();
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(Constants.ROLE_ID_ADMIN);
        roleIds.add(Constants.ROLE_ID_STAFF);
        List<EntityUserRole> entityUserRoles = entityManagerUserRole.getUserRolesByRoleIds(roleIds);
        List<Integer> userIds = new ArrayList<>();
        for(EntityUserRole entityUserRole : entityUserRoles)
        {
            if(!userIds.contains(entityUserRole.getUserId()))
            {
                userIds.add(entityUserRole.getUserId());
            }
        }
        if(!userIds.isEmpty())
        {
            EntityManagerUser entityManagerUser = new EntityManagerUser();
            List<EntityUser> entityUsers = entityManagerUser.getUsersByUserIds(userIds);
            response.setList(entityUsers);            
        }
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_USER_ROLE_LIST)
    public ClientResponse getUserRoleList(ISession session, IPacket packet) throws Exception 
    {
        //we are sending admin and staff as user role list
        EntityManagerRole entityManagerRole = new EntityManagerRole();
        List<EntityRole> roleList = entityManagerRole.getRoles();
        List<EntityRole> roles = new ArrayList<>();
        for(EntityRole entityRole: roleList)
        {
            if(entityRole.getId() == Constants.ROLE_ID_ADMIN || entityRole.getId() == Constants.ROLE_ID_STAFF)
            {
                roles.add(entityRole);
            }
        }
        ClientListResponse response = new ClientListResponse();
        response.setSuccess(true);
        response.setList(roles);
        return response;        
    }
    
    @ClientRequest(action = ACTION.ADD_USER_INFO)
    public ClientResponse addUserInfo(ISession session, IPacket packet) throws Exception 
    {
        DTOUser response = new DTOUser();
        Gson gson = new Gson();
        DTOUser dtoUser = gson.fromJson(packet.getPacketBody(), DTOUser.class);     
        
        if(dtoUser == null || dtoUser.getEntityUser() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid User Info. Please try again later.");
            return response;
        }
        else if(dtoUser.getEntityUser().getEmail() == null || dtoUser.getEntityUser().getEmail().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Email is required.");
            return response;
        }
        else if(dtoUser.getEntityUser().getPassword()== null || dtoUser.getEntityUser().getPassword().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Password is required.");
            return response;
        }
        
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        EntityUser tempEntityUser = entityManagerUser.getUserByEmail(dtoUser.getEntityUser().getEmail());
        if(tempEntityUser != null && tempEntityUser.getId() > 0)
        {
            response.setSuccess(false);
            response.setMessage("Email already exists or invalid.");
            return response;
        }
        EntityUser entityUser = entityManagerUser.createUser(dtoUser.getEntityUser(), dtoUser.getEntityUserRoles());
        if(entityUser != null && entityUser.getId() > 0)
        {
            dtoUser.setEntityUser(entityUser);
            dtoUser.setSuccess(true);
            dtoUser.setMessage("User is created successfully.");
        }
        else
        {
            dtoUser.setSuccess(false);
            dtoUser.setMessage("Unable to create user. Please try again later.");
        }
        return dtoUser;
    }
    
    @ClientRequest(action = ACTION.UPDATE_USER_INFO)
    public ClientResponse updateUserInfo(ISession session, IPacket packet) throws Exception 
    {
        DTOUser response = new DTOUser();
        Gson gson = new Gson();
        DTOUser dtoUser = gson.fromJson(packet.getPacketBody(), DTOUser.class);     
        
        if(dtoUser == null || dtoUser.getEntityUser() == null || dtoUser.getEntityUser().getId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid User Info. Please try again later.");
            return response;
        }
        else if(dtoUser.getEntityUser().getEmail() == null || dtoUser.getEntityUser().getEmail().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Email is required.");
            return response;
        }
        else if(dtoUser.getEntityUser().getPassword()== null || dtoUser.getEntityUser().getPassword().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Password is required.");
            return response;
        }
        
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        EntityUser tempEntityUser = entityManagerUser.getUserByEmail(dtoUser.getEntityUser().getEmail());
        if(tempEntityUser != null && tempEntityUser.getId() != dtoUser.getEntityUser().getId())
        {
            response.setSuccess(false);
            response.setMessage("Email already used or invalid.");
            return response;
        }
        if(entityManagerUser.updateUser(dtoUser.getEntityUser(), dtoUser.getEntityUserRoles()))
        {
            dtoUser.setSuccess(true);
            dtoUser.setMessage("User is updated successfully.");
        }
        else
        {
            dtoUser.setSuccess(false);
            dtoUser.setMessage("Unable to create user. Please try again later.");
        }
        return dtoUser;
    }
}
