package org.bdlions.inventory.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.List;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;

/**
 *
 * @author Nazmul
 */
public class DTOUser extends ClientResponse implements java.io.Serializable{
    private EntityUser entityUser;
    private List<EntityUserRole> entityUserRoles;

    public EntityUser getEntityUser() {
        return entityUser;
    }

    public void setEntityUser(EntityUser entityUser) {
        this.entityUser = entityUser;
    }

    public List<EntityUserRole> getEntityUserRoles() {
        return entityUserRoles;
    }

    public void setEntityUserRoles(List<EntityUserRole> entityUserRoles) {
        this.entityUserRoles = entityUserRoles;
    }    
}
