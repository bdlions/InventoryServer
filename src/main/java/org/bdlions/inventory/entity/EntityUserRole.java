package org.bdlions.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author nazmul hasan
 */
@Entity
@Table(
        name = "users_roles",
        indexes = {
            @Index(name = "idx_name", columnList = "user_id, role_id", unique = true)
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getUserRolesByUserId", 
            query = "from EntityUserRole userRole where userRole.userId = :userId"
    ),
    @NamedQuery(
            name = "getUserRolesByRoleIds", 
            query = "from EntityUserRole userRole where userRole.roleId IN (:roleIds)"
    ),
    @NamedQuery(
            name = "deleteUserRoles",
            query = "delete from EntityUserRole role where role.userId = :userId"
    )
})
public class EntityUserRole {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    
    private int id;

    @Column(name = "user_id")
    private int userId;
    
    @Column(name = "role_id")
    private int roleId;

    public EntityUserRole() 
    {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
