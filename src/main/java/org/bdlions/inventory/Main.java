package org.bdlions.inventory;
import java.util.List;
import org.bdlions.inventory.db.DatabaseLoader;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityOrganization;
import org.bdlions.inventory.entity.manager.EntityManagerOrganization;
import org.bdlions.inventory.util.LicenseKeyProvider;
import org.bdlions.license.KeyManager;
import org.bdlions.license.util.LicenseUtility;
import org.bdlions.transport.channel.provider.ChannelProviderImpl;
import org.hibernate.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



@SpringBootApplication
@EnableAutoConfiguration
public class Main extends SpringBootServletInitializer {

    private static ChannelProviderImpl channelProviderImpl = null;
    
    @Bean
    WebMvcConfigurer configurer () {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers (ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/html/**").
                          addResourceLocations("classpath:/");
            }
        };
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Main.class);
    }

    public static void main(String[] args) throws Exception {
        //This is licnese checking logic. Use it while installing into customer machine.
        /*KeyManager km = new KeyManager();
        int seed = LicenseUtility.getInstance().getSeed();
        String key = km.generateKey();

        String serialNumber = LicenseKeyProvider.get("LICENSE_NUMBER");
        if(serialNumber != null && !serialNumber.isEmpty())
        {
            if(serialNumber.equals(seed+key))
            {
                System.out.println("Valid License.");
            }
            else
            {   
                System.out.println("Invalid License.");
                return;
            }
        }
        else
        {   
            System.out.println("Invalid License.");
            return;
        }*/
        
        SpringApplication.run(Main.class, args);
        
//        IClientRequestHandler requestHandler = ClientRequestHandler.getInstance();
//        ISessionManager sessionManager = ClientRequestHandler.getInstance().getSessionManager();
//        channelProviderImpl = new ChannelProviderImpl(requestHandler, sessionManager);
//        channelProviderImpl.start();
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
            
            KeepAliveDBManager keepAliveDBManager = new KeepAliveDBManager("keepAliveDBManager");
            keepAliveDBManager.start();
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }        
    }
}
