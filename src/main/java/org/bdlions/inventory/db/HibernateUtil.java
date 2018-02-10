package org.bdlions.inventory.db;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.bdlions.inventory.entity.EntityOrganization;
import org.bdlions.inventory.entity.manager.EntityManagerOrganization;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author alamgir
 */
public final class HibernateUtil {

//    private static final SessionFactory sessionFactory = buildSessionFactory();
    private final static HashMap<Integer, SessionFactory> sessionFactoryMap = new HashMap<Integer, SessionFactory>();
    private static HibernateUtil instance;

    private HibernateUtil() {
        EntityManagerOrganization manager = new EntityManagerOrganization();
        buildSessionFactory(manager.getOrganizations());
    }

    public static HibernateUtil getInstance() {
        if (instance == null) {
            instance = new HibernateUtil();
        }
        return instance;
    }

    public void buildSessionFactory(List<EntityOrganization> organizations) {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            for (EntityOrganization organization : organizations) {
                if(!sessionFactoryMap.containsKey(organization.getDatabaseName())){
                    System.out.println("##############################-----------databasename:"+ organization.getDatabaseName());
                    Configuration config = getConfigByAppID(organization.getDatabaseName());
                    SessionFactory factory = config.buildSessionFactory();
                    sessionFactoryMap.put(organization.getDatabaseName(), factory);
                }
            }

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Configuration getConfigByAppID(int appID) {
        String connectionURL = "jdbc:mysql://localhost:3306/inventory_db_%s?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
        Configuration config = new Configuration().configure();
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", String.format(connectionURL, appID));
        config.addProperties(properties);
        return config;
    }

    public static Session getSession(Integer appID) {
        SessionFactory sessionFactory = null;
        
        if(sessionFactoryMap.isEmpty() ){
            return null;
        }
        
        if (sessionFactoryMap.containsKey(appID)) {
            sessionFactory = sessionFactoryMap.get(appID);
        }
        
        if (sessionFactory == null) {
            Integer key = sessionFactoryMap.keySet().iterator().next();
            sessionFactory = sessionFactoryMap.get(key);
        }
        
        Session session = sessionFactory.openSession();
        return session;
    }
    
    /*public Session getSession() 
    {
        Session session = getSession(10001);
        return session;
        //return getSession(10001);
    }*/
}
