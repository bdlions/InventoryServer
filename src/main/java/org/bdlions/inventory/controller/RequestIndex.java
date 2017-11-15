/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.inventory.controller;

import com.bdlions.commons.ClientMessages;
import com.bdlions.dto.response.ClientFailedResponse;
import com.bdlions.dto.response.ClientResponse;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.bdlions.inventory.packet.PacketHeaderImpl;
import org.bdlions.transport.packet.IPacket;
import org.bdlions.transport.packet.IPacketHeader;
import org.bdlions.transport.packet.RequestPacketImpl;
import org.bdlions.inventory.util.ClientRequestHandler;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author nazmul hasan
 */
@CrossOrigin
@RestController
public class RequestIndex {
    @RequestMapping("/")
    String home() {
        return "Inventory Server request index.";
    }
    
    @RequestMapping("/request")
    public String processRequest(@RequestParam("packetHeader") String paramPacketHeader, @RequestParam("packetBody") String paramPacketBody)
    {
        Gson gson = new GsonBuilder().create();
        try {
            String remoteAddress = "";
            int port = 0;
            JsonParser parser = new JsonParser();
            //JsonObject jsonObject = (JsonObject)parser.parse(request.getReader());
            JsonElement packetHeaderText = parser.parse(paramPacketHeader);
            String packetBody = paramPacketBody;
            if(packetHeaderText == null){
                ClientResponse cr = new ClientFailedResponse();
                cr.setMessage(ClientMessages.PACKET_HEADER_MISSING);
                return gson.toJson(cr);
            }
            
            /* TODO output your page here. You may use following sample code. */
            IPacketHeader packetHeader = gson.fromJson(packetHeaderText, PacketHeaderImpl.class);

            IPacket packet = new RequestPacketImpl(packetHeader, packetBody, remoteAddress, port);
            ClientResponse clientResponse = (ClientResponse)ClientRequestHandler.getInstance().executeRequest(packet);
            
            if (clientResponse != null) {
                return gson.toJson(clientResponse);
            } else {
                ClientResponse cr = new ClientFailedResponse();
                cr.setMessage(ClientMessages.REQUEST_DID_NOT_PROCESSED_SUCCESSFULLY);
                return gson.toJson(cr);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            ClientResponse cr = new ClientFailedResponse() ;
            cr.setMessage(ClientMessages.REQUEST_DID_NOT_PROCESSED_SUCCESSFULLY);
            return gson.toJson(cr);
        }
    }
}
