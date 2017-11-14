package org.bdlions.inventory.manager;

import java.util.List;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.util.StringUtils;
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
        EntityUser reqEntityUser = new EntityUser();
        reqEntityUser.setEmail(identity);
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        EntityUser entityUser = entityManagerUser.getUserByEmail(reqEntityUser.getEmail());
        return entityUser;
        /*Session session = HibernateUtil.getSession();
        try {

            Query<EntityUser> query = session.getNamedQuery("getUserByEmail");
            query.setParameter("email", identity);

            return query.uniqueResult();
        } finally {
            session.close();
        }*/
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
    public EntityUser getUserById(int userId) 
    {
        EntityUser reqEntityUser = new EntityUser();
        reqEntityUser.setId(userId);
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        EntityUser entityUser = entityManagerUser.getUserByUserId(reqEntityUser.getId());
        return entityUser;
        /*Session session = HibernateUtil.getSession();
        try {
            Query<EntityUser> query = session.getNamedQuery("getUserByUserId");
            query.setParameter("userId", userId);

            return query.getSingleResult();
        } finally {
            session.close();
        }*/
    }

    public List<EntityUser> getUsers(int offset, int limit) 
    {
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        List<EntityUser> entityUserList = entityManagerUser.getUsers(offset, limit);
        return entityUserList;        
        /*Session session = HibernateUtil.getSession();
        try {
            Query<EntityUser> query = session.getNamedQuery("getUsers");
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            return query.getResultList();
        } finally {
            session.close();
        }*/
    }

}
