package org.bdlions.dto;

import com.bdlions.dto.response.ClientResponse;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOCustomer extends ClientResponse implements java.io.Serializable{
    public int limit;
    public int offset;
    public EntityCustomer entityCustomer;
    public EntityUser entityUser;
    public DTOCustomer()
    {
        entityCustomer = new EntityCustomer();
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
    
    public EntityCustomer getEntityCustomer() {
        return entityCustomer;
    }

    public void setEntityCustomer(EntityCustomer entityCustomer) {
        this.entityCustomer = entityCustomer;
    }

    public EntityUser getEntityUser() {
        return entityUser;
    }

    public void setEntityUser(EntityUser entityUser) {
        this.entityUser = entityUser;
    }
    
}
