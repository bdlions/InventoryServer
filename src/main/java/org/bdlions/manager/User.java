package org.bdlions.manager;

import org.bdlions.db.HibernateUtil;
import org.bdlions.dto.EntityCompany;
import org.bdlions.dto.EntityProfile;
import org.bdlions.dto.EntityUser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nazmul hasan
 */
public class User {

    private final Logger logger = LoggerFactory.getLogger(User.class);

    /**
     * This method will return user by identity
     *
     * @param identity user identity
     * @return User user info
     * @author nazmul hasan on 2nd August 2017
     */
    public EntityUser getUserByIdentity(String identity) {
        Session session = HibernateUtil.getSession();
        try {

            Query<EntityUser> query = session.getNamedQuery("getUserByEmail");
            query.setParameter("email", identity);

            return query.uniqueResult();
        } finally {
            session.close();
        }
    }

    /**
     * This method will return user by credential
     *
     * @param identity user identity
     * @param password user password
     * @return User user info
     * @author nazmul hasan on 2nd August 2017
     */
    public EntityUser getUserByCredential(String identity, String password) {
        EntityUser selectedUser = getUserByIdentity(identity);
        if (selectedUser == null) {
            return selectedUser;
        }

        if (selectedUser.getPassword().equals(password)) {
            return selectedUser;
        }
        return null;
    }

    /**
     * This method will return user info by user id
     *
     * @param userId user id
     * @return User user info
     * @author nazmul hasan on 2nd August 2017
     */
    public EntityUser getUserById(int userId) {
        Session session = HibernateUtil.getSession();
        try {
            Query<EntityUser> query = session.getNamedQuery("getUserByUserId");
            query.setParameter("userId", userId);

            return query.getSingleResult();
        } finally {
            session.close();
        }
    }

    /**
     * This method will return company info by company id or user id
     *
     * @param userId user id
     * @param companyId company id
     * @return Company company info
     * @author nazmul hasan on 2nd August 2017
     */
    public EntityCompany getUserCompanyById(int userId, int companyId) {
        EntityCompany company = null;
        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            //if we have company id then we can query into company table, otherwise we have to query into profile table to get company id
            if (companyId == 0) {
                Query query = session.createSQLQuery("select {p.*} from profile p where user_id = :user_id")
                        .addEntity("p", EntityProfile.class)
                        .setInteger("user_id", userId);
                EntityProfile profile = (EntityProfile) query.uniqueResult();
                companyId = profile.getCompanyId();
            }
            Query query = session.createSQLQuery("select {c.*} from company c where id = :id")
                    .addEntity("c", EntityCompany.class)
                    .setInteger("id", companyId);
            company = (EntityCompany) query.uniqueResult();
            transaction.commit();
            session.close();

        } catch (Exception ex) {
            logger.error(ex.toString());
            return null;
        }
        return company;
    }

}
