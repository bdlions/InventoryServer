
import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bdlions.inventory.controller.RequestIndex;
import org.bdlions.inventory.packet.PacketHeaderImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class HTTPRequestHelper {

    public MockMvc mockMvc;

    @InjectMocks
    private RequestIndex requestController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(requestController)
                .build();
    }

    public String getResult(String packetHeader, String packetBody){
        try {
            MvcResult result = mockMvc.perform(post("/request").param("packetHeader", packetHeader).param("packetBody", packetBody)).andReturn();
            return result.getResponse().getContentAsString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public String getSessionId(){
        PacketHeaderImpl mockPacketHeader = new PacketHeaderImpl();
        mockPacketHeader.setAction(ACTION.SIGN_IN);
        mockPacketHeader.setRequestType(REQUEST_TYPE.AUTH);
        String packetHeader = new GsonBuilder().create().toJson(mockPacketHeader);
        String packetBody = "{\"userName\":\"admin@gmail.com\", \"password\":\"pass\"}";
        String result = getResult(packetHeader, packetBody);
        JsonParser parser = new JsonParser();
        return  parser.parse(result).getAsJsonObject().get("sessionId").getAsString();
    }
    
    @Test
    public void test(){}
    
    
    
    
    
}