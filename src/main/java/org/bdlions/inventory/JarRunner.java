/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.inventory;

import org.bdlions.inventory.db.DatabaseLoader;
import org.bdlions.inventory.db.HibernateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAutoConfiguration
public class JarRunner{


    public static void main(String[] args){
        SpringApplication.run(JarRunner.class, args);
        //OrganizationLoaderManager.getInstance().start();
        
        DatabaseLoader.getInstance().getSession();
        //HibernateUtil.getInstance().getSession();
        
        System.out.println("Server started");
    }

}
