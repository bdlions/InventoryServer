/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.inventory;

import java.util.List;
import org.bdlions.inventory.db.DatabaseLoader;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityOrganization;
import org.bdlions.inventory.entity.manager.EntityManagerOrganization;
import org.hibernate.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAutoConfiguration
public class JarRunner{


    public static void main(String[] args){
        SpringApplication.run(JarRunner.class, args);
        try
        {
            Session session1 = DatabaseLoader.getInstance().getSession();
            session1.close();
            
            EntityManagerOrganization manager = new EntityManagerOrganization();
            List<EntityOrganization> organizations = manager.getOrganizations();
            for (EntityOrganization organization : organizations) 
            {
                Session session2 = HibernateUtil.getInstance().getSession(organization.getDatabaseName());
                session2.close();
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        } 
        
        System.out.println("Server started");
    }

}
