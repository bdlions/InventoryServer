
package org.bdlions.dto;

import com.bdlions.dto.response.ClientResponse;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOSupplier extends ClientResponse implements java.io.Serializable{
    public int limit;
    public int offset;
    public EntitySupplier entitySupplier;
    public EntityUser entityUser;
    public DTOSupplier()
    {
        entitySupplier = new EntitySupplier();
        entityUser = new EntityUser();
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
    
}
