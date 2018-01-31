
package org.bdlions.inventory.dto;

import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.entity.EntitySupplier;
import com.bdlions.dto.response.ClientResponse;
import java.util.List;
import org.bdlions.inventory.entity.EntityProductSupplier;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOSupplier extends ClientResponse implements java.io.Serializable{
    public int limit;
    public int offset;
    public EntitySupplier entitySupplier;
    public EntityUser entityUser;
    public EntityUserRole entityUserRole;
    public List<EntityProductSupplier> entityProductSupplierList;
    public List<EntityProductSupplier> epsListToBeDeleted;
    public DTOSupplier()
    {
        entitySupplier = new EntitySupplier();
        entityUser = new EntityUser();
        entityUserRole = new EntityUserRole();
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public EntitySupplier getEntitySupplier() {
        return entitySupplier;
    }

    public void setEntitySupplier(EntitySupplier entitySupplier) {
        this.entitySupplier = entitySupplier;
    }

    public EntityUser getEntityUser() {
        return entityUser;
    }

    public void setEntityUser(EntityUser entityUser) {
        this.entityUser = entityUser;
    }

    public EntityUserRole getEntityUserRole() {
        return entityUserRole;
    }

    public void setEntityUserRole(EntityUserRole entityUserRole) {
        this.entityUserRole = entityUserRole;
    }

    public List<EntityProductSupplier> getEntityProductSupplierList() {
        return entityProductSupplierList;
    }

    public void setEntityProductSupplierList(List<EntityProductSupplier> entityProductSupplierList) {
        this.entityProductSupplierList = entityProductSupplierList;
    }

    public List<EntityProductSupplier> getEpsListToBeDeleted() {
        return epsListToBeDeleted;
    }

    public void setEpsListToBeDeleted(List<EntityProductSupplier> epsListToBeDeleted) {
        this.epsListToBeDeleted = epsListToBeDeleted;
    }
    
}
