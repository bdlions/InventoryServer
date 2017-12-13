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
public class StockHandlerTest extends HTTPRequestHelper{
    
    public StockHandlerTest() {
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
    public void getCurrentStockTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_CURRENT_STOCK);
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
    public void getCurrentStockByProductNameTest() {
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.FETCH_CURRENT_STOCK_BY_PRODUCT_NAME);
        mockPacketHeader.setRequestType(REQUEST_TYPE.REQUEST);
        mockPacketHeader.setSessionId(getSessionId());

        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        System.out.println(packetHeader);

        String packetBody = "{\"limit\":10, \"offset\":0, \"entityProduct\":{\"name\":\"jeans\"}}";
        System.out.println(packetBody);

        String result = getResult(packetHeader, packetBody);
        System.out.println("Result : " + result);
    }
}
