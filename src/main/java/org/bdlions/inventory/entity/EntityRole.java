package org.bdlions.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author nazmul hasan
 */
@Entity
@Table(
        name = "roles",
        indexes = {
            @Index(name = "idx_name", columnList = "title", unique = true)
        }
)
@NamedQueries(
{
    @NamedQuery(
            name = "getRoleByRoleId", 
            query =  "from EntityRole role where role.id = :roleId"
    ),
    @NamedQuery(
            name = "getRoles",
            query = "from EntityRole role"
    )        
}
)
public class EntityRole {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;

    @Column(name = "title", length = 200)
    private String title;
    
    @Column(name = "description", length = 200)
    private String description;

    public EntityRole() 
    {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }    
}
