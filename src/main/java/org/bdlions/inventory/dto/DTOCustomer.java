package org.bdlions.inventory.dto;

import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.EntityUserRole;
import org.bdlions.inventory.entity.EntityCustomer;
import com.bdlions.dto.response.ClientResponse;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOCustomer extends ClientResponse implements java.io.Serializable{
    private int limit;
    private int offset;
    private EntityCustomer entityCustomer;
    private EntityUser entityUser;
    private EntityUserRole entityUserRole;
    private double totalSaleAmount;
    private double totalPaymentAmount;
    public DTOCustomer()
    {
        entityCustomer = new EntityCustomer();
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

    public EntityUserRole getEntityUserRole() {
        return entityUserRole;
    }

    public void setEntityUserRole(EntityUserRole entityUserRole) {
        this.entityUserRole = entityUserRole;
    }

    public double getTotalSaleAmount() {
        return totalSaleAmount;
    }

    public void setTotalSaleAmount(double totalSaleAmount) {
        this.totalSaleAmount = totalSaleAmount;
    }

    public double getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(double totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }
    
}
