package org.bdlions.inventory;
import java.util.List;
import org.bdlions.inventory.db.DatabaseLoader;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityOrganization;
import org.bdlions.inventory.entity.manager.EntityManagerOrganization;
import org.bdlions.session.ISessionManager;
import org.bdlions.transport.channel.provider.ChannelProviderImpl;
import org.bdlions.inventory.util.ClientRequestHandler;
import org.bdlions.util.handler.request.IClientRequestHandler;
import org.hibernate.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Main extends SpringBootServletInitializer {

    private static ChannelProviderImpl channelProviderImpl = null;
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Main.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
        
        IClientRequestHandler requestHandler = ClientRequestHandler.getInstance();
        ISessionManager sessionManager = ClientRequestHandler.getInstance().getSessionManager();
        channelProviderImpl = new ChannelProviderImpl(requestHandler, sessionManager);
        channelProviderImpl.start();
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
    }
}
