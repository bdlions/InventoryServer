/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.udp;

import org.bdlions.db.HibernateUtil;
import org.bdlions.util.ClientRequestHandler;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.bdlions.session.ISessionManager;
import org.bdlions.transport.channel.provider.ChannelProviderImpl;
import org.bdlions.util.handler.request.IClientRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alamgir
 */
@WebListener
public class ServerExecutor implements ServletContextListener{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ChannelProviderImpl channelProviderImpl = null;
    
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            IClientRequestHandler requestHandler = ClientRequestHandler.getInstance();
            ISessionManager sessionManager = ClientRequestHandler.getInstance().getSessionManager();
            channelProviderImpl = new ChannelProviderImpl(requestHandler, sessionManager);
            channelProviderImpl.start();
            ServletContext ctx = servletContextEvent.getServletContext();
            ctx.setAttribute("channelProviderImpl", channelProviderImpl);
            HibernateUtil.getSession();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        channelProviderImpl = (ChannelProviderImpl)ctx.getAttribute("channelProviderImpl");
        if(channelProviderImpl != null){    
            channelProviderImpl.stop();
        }
    }
    
}
