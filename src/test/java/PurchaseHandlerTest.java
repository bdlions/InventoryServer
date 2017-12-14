/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.GsonBuilder;
import org.bdlions.inventory.packet.PacketHeaderImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nazmul
 */
public class PurchaseHandlerTest extends HTTPRequestHelper{
    
    public PurchaseHandlerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    //@Test
    public void addPurchaseOrderInfoTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.ADD_PURCHASE_ORDER_INFO);
        mockPacketHeader.setRequestType(REQUEST_TYPE.UPDATE);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"entityPurchaseOrder\":{\"orderNo\":\"order1\", \"supplierUserId\":3}, \"products\":[{\"quantity\": 100, \"entityProduct\":{\"id\":1, \"unitPrice\":500}}, {\"quantity\": 150, \"entityProduct\":{\"id\":2, \"unitPrice\":600}}]}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void updatePurchaseOrderInfoTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.UPDATE_PURCHASE_ORDER_INFO);
        mockPacketHeader.setRequestType(REQUEST_TYPE.UPDATE);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"entityPurchaseOrder\":{\"id\":1, \"orderNo\":\"order1\", \"supplierUserId\":4}, \"products\":[{\"quantity\": 91, \"entityProduct\":{\"id\":1, \"unitPrice\":510}}, {\"quantity\": 144, \"entityProduct\":{\"id\":2, \"unitPrice\":610}}]}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void getPurchaseOrderInfoTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_PURCHASE_ORDER_INFO);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"entityPurchaseOrder\":{\"orderNo\":\"order1\"}}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void getPurchaseOrdersTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_PURCHASE_ORDERS);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"limit\":10, \"offset\":0}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void getPurchaseOrdersByOrderNoTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_PURCHASE_ORDERS_BY_ORDER_NO);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"limit\":10, \"offset\":0, \"entityPurchaseOrder\":{\"orderNo\":\"order1\"}}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
    
    //@Test
    public void getPurchaseOrdersByCellTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_PURCHASE_ORDERS_BY_CELL);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"limit\":10, \"offset\":0, \"entityPurchaseOrder\":{\"cell\":\"01\"}}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
}
