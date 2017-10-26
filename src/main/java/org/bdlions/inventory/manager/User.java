package org.bdlions.inventory.manager;

import org.bdlions.inventory.db.HibernateUtil;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.util.StringUtils;
import org.hibernate.Session;
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
        if(password == null || StringUtils.isNullOrEmpty(password)){
            return null;
        }
        
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

}
