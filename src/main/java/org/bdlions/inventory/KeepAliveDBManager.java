package org.bdlions.inventory;

import org.bdlions.inventory.db.DatabaseLoader;
import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityOrganization;
import org.bdlions.inventory.entity.EntityUOM;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nazmul hasan
 */
public class KeepAliveDBManager implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(KeepAliveDBManager.class.getName());
    private Thread t;
    private String threadName;
    long sleepTime = 1800 * 1000;
    public KeepAliveDBManager(String threadName)
    {
        this.threadName = threadName;        
    }
    
    @Override
    public void run() 
    {
        while(true)
        {
            try
            {
                System.out.println("Keep Alive DB Manager execute starts.");
                Session session1 = DatabaseLoader.getInstance().getSession();
                try 
                {
                    Query<EntityOrganization> query = session1.getNamedQuery("getOrganizations");
                    query.getResultList().size();
                } 
                finally 
                {
                    session1.close();
                }
                
                Session session2 = HibernateUtil.getInstance().getSession(10001);
                try 
                {
                    Query<EntityUOM> query = session2.getNamedQuery("getAllUOMs");
                    query.getResultList();
                } 
                finally 
                {
                    session2.close();
                }
                System.out.println("Keep Alive DB Manager execute ends.");
                Thread.sleep(sleepTime);
            }
            catch (Exception ex) {
                logger.error(ex.toString());
            } finally {

            }            
        }
    }
        
    
    public void start ()
    {
        if (t == null)
        {
           t = new Thread (this, threadName);
           t.start ();
        }
    }
}
