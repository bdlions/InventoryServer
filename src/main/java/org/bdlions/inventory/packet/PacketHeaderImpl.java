package org.bdlions.inventory.packet;

import org.bdlions.transport.packet.IPacketHeader;
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;

/**
 *
 * @author nazmul hasan
 */
public class PacketHeaderImpl implements IPacketHeader {

    private ACTION action;
    private REQUEST_TYPE requestType;
    private String sessionId;
    private String packetId;
    private int appId;

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public void setAction(ACTION action) {
        this.action = action;
    }

    public void setRequestType(REQUEST_TYPE requestType) {
        this.requestType = requestType;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }
    
    @Override
    public String getPacketId() {
        return packetId;
    }

    @Override
    public ACTION getAction() {
        return action;
    }

    @Override
    public REQUEST_TYPE getRequestType() {
        return requestType;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }
    
    @Override
    public int getAppId() {
        return appId;
    }

    @Override
    public boolean isBroken() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
