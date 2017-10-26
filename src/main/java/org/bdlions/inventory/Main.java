package org.bdlions.inventory;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.session.ISessionManager;
import org.bdlions.transport.channel.provider.ChannelProviderImpl;
import org.bdlions.inventory.util.ClientRequestHandler;
import org.bdlions.util.handler.request.IClientRequestHandler;
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
        HibernateUtil.getSession();
    }

}
