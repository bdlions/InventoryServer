package org.bdlions.inventory.db;

import java.util.HashMap;
import java.util.Properties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author alamgir
 */
public class DatabaseLoader {
    private static DatabaseLoader instance;
    private static SessionFactory sessionFactory;

    private DatabaseLoader() {
        Configuration config = getDBLoaderConfig("organizations_db");
        sessionFactory = config.buildSessionFactory();
    }

    public static DatabaseLoader getInstance() {
        if (instance == null) {
            instance = new DatabaseLoader();
        }
        return instance;
    }

    
    private static Configuration getDBLoaderConfig(String dbName) {
        //String connectionURL = "jdbc:mysql://localhost:3306/%s?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
        Configuration config = new Configuration().configure(String.format("%s.xml", dbName));
        Properties properties = new Properties();
        //properties.setProperty("hibernate.connection.url", String.format(connectionURL, dbName));
        config.addProperties(properties);
        return config;
    }

    public Session getSession() {
        Session session = sessionFactory.openSession();
        return session;
    }
}
