package org.bdlions.inventory.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.manager.User;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class UserHandler {

    private final ISessionManager sessionManager;

    public UserHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.FETCH_USER_INFO)
    public ClientResponse getProfileInfo(ISession session, IPacket packet) throws Exception 
    {
        int userId = (int)session.getUserId();
        User user = new User();
        EntityUser entiryUser = user.getUserById(userId);
        entiryUser.setMessage("User Info.");
        entiryUser.setSuccess(true);
        return entiryUser;
    }
}
