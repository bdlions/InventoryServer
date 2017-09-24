package org.bdlions.dto;

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
        name = "company",
        indexes = {
            @Index(name = "idx_name", columnList = "title", unique = true)
        }
)
@NamedQueries({
    @NamedQuery(
            name = "getCompanyById",
            query = "from EntityCompany company where company.id = :companyId"
    )
    
})
public class EntityCompany {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")    
    private int id;

    @Column(name = "title")
    private String title;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "website")
    private String website;

    public EntityCompany() 
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
    
}
