package org.bdlions.manager;

import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.Company;
import org.bdlions.dto.Profile;
import org.bdlions.dto.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nazmul hasan
 */
public class UserManager {

    private final Logger logger = LoggerFactory.getLogger(UserManager.class);
    
    /**
     * This method will return user by identity
     * @param identity user identity
     * @return User user info
     * @author nazmul hasan on 2nd August 2017
     */
    public User getUserByIdentity(String identity) {
        User user = null;
        try
        {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery("select {u.*} from user u where email = :email")
                    .addEntity("u",User.class)
                    .setString("email", identity);
            user = (User)query.uniqueResult();
            transaction.commit();
            session.close();
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
            return null;
        }        
        return user;
    }
    
    /**
     * This method will return user by credential
     * @param identity user identity
     * @param password user password
     * @return User user info
     * @author nazmul hasan on 2nd August 2017
     */
    public User getUserByCredential(String identity, String password) {
        User user = null;
        try
        {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery("select {u.*} from user u where email = :email and password = :password")
                    .addEntity("u",User.class)
                    .setString("email", identity)
                    .setString("password", password);
            user = (User)query.uniqueResult();
            transaction.commit();
            session.close();
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
            return null;
        }        
        return user;
    }
    
    /**
     * This method will return user info by user id
     * @param userId user id
     * @return User user info
     * @author nazmul hasan on 2nd August 2017
     */
    public User getUserInfoById(int userId)
    {
        User user = null;
        try
        {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery("select {u.*} from user u where id = :id")
                    .addEntity("u",User.class)
                    .setInteger("id", userId);
            user = (User)query.uniqueResult();
            transaction.commit();
            session.close();
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
            return null;
        }
        return user;
    }
    
    /**
     * This method will return profile info by user id
     * @param userId user id
     * @return Profile profile info
     * @author nazmul hasan on 2nd August 2017
     */
    public Profile getUserProfileById(int userId)
    {
        Profile profile = null;
        try
        {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery("select {p.*} from profile p where user_id = :user_id")
                    .addEntity("p",Profile.class)
                    .setInteger("user_id", userId);
            profile = (Profile)query.uniqueResult();
            transaction.commit();
            session.close();
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
            return null;
        }
        return profile;
    }
    
    /**
     * This method will return company info by company id or user id
     * @param userId user id
     * @param companyId company id
     * @return Company company info
     * @author nazmul hasan on 2nd August 2017
     */
    public Company getUserCompanyById(int userId, int companyId)
    {
        Company company  = null;
        try
        {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            //if we have company id then we can query into company table, otherwise we have to query into profile table to get company id
            if(companyId == 0)
            {
                Query query = session.createSQLQuery("select {p.*} from profile p where user_id = :user_id")
                    .addEntity("p",Profile.class)
                    .setInteger("user_id", userId);
                Profile profile = (Profile)query.uniqueResult();
                companyId = profile.getCompanyId();
            }
            Query query = session.createSQLQuery("select {c.*} from company c where id = :id")
                    .addEntity("c",Company.class)
                    .setInteger("id", companyId);
            company = (Company)query.uniqueResult();
            transaction.commit();
            session.close();
            
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
            return null;
        }
        return company;
    }
    
    /**
     * This method will update user profile info
     * @param profile user profile
     * @return boolean whether profile is updated or not
     * @author nazmul hasan on 2nd August 2017
     */
    public boolean updateProfile(Profile profile)
    {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        try
        {
            if(profile != null && profile.getUser() != null)
            {                
                User user = profile.getUser();
                if(user.getId() > 0 && profile.getId() > 0)
                {
                    session.update(user);
                    session.update(profile);
                    transaction.commit();
                    session.close();
                    return true;
                }
                return false;
            }
        }
        catch(Exception ex)
        {
            try
            {
                transaction.rollback();
            }
            catch(Exception ex2)
            {
                logger.error(ex2.toString());
            }            
            logger.error(ex.toString());
            return false;
        }
        return false;
    }
    
    /**
     * This method will create a new user profile
     * @param profile user profile
     * @return boolean whether profile is created or not
     * @author nazmul hasan on 2nd August 2017
     */
    public boolean createProfile(Profile profile)
    {   
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        try
        {
            if(profile != null && profile.getUser() != null)
            {                
                User user = profile.getUser();
                session.save(user);
                profile.setUserId(user.getId());
                session.save(profile);
                transaction.commit();
                session.close();
                return true;
            }
        }
        catch(Exception ex)
        {
            try
            {
                transaction.rollback();
            }
            catch(Exception ex2)
            {
                logger.error(ex2.toString());
            }            
            logger.error(ex.toString());
            return false;
        }
        return false;
    }
}
