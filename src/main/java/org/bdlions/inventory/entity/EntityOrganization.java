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
        name = "organizations",
        indexes = {
            @Index(name = "idx_name", columnList = "database_name", unique = true)
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getOrganizations",
            query = "from EntityOrganization organization"
    )
})
public class EntityOrganization {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;
    
    @Column(name = "database_name")
    private int databaseName;

    @Column(name = "title")
    private String title;

    public EntityOrganization() 
    {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(int databaseName) {
        this.databaseName = databaseName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
