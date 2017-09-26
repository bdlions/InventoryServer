package org.bdlions.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import com.bdlions.dto.response.GeneralResponse;
import com.google.gson.Gson;
import java.util.List;
import org.bdlions.dto.DTOCustomer;
import org.bdlions.dto.EntityProduct;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.dto.EntityUser;
import org.bdlions.dto.ListCustomer;
import org.bdlions.library.CustomerLibrary;
import org.bdlions.library.ProductLibrary;
import org.bdlions.manager.Customer;
import org.bdlions.manager.User;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class CustomerHandler {

    private final ISessionManager sessionManager;

    public CustomerHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.ADD_CUSTOMER_INFO)
    public ClientResponse addCustomerInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);     
        CustomerLibrary customerLibrary = new CustomerLibrary();
        GeneralResponse response = customerLibrary.createCustomer(dtoCustomer);
        return response;
    }
    
    @ClientRequest(action = ACTION.UPDATE_CUSTOMER_INFO)
    public ClientResponse updateCustomerInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);     
        CustomerLibrary customerLibrary = new CustomerLibrary();
        GeneralResponse response = customerLibrary.updateCustomer(dtoCustomer);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_CUSTOMER_INFO)
    public ClientResponse getCustomerInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);      
        Customer customer = new Customer();
        DTOCustomer response = customer.getCustomerInfo(dtoCustomer.getEntityCustomer().getId());
        if(response == null)
        {
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setSuccess(false);
            generalResponse.setMessage("Invalid customer. Please try again later");
            return generalResponse;
        }
        response.setSuccess(true);
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_CUSTOMERS)
    public ClientResponse getCustomers(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);      
        Customer customer = new Customer();
        List<DTOCustomer> customers = customer.getCustomers(dtoCustomer);
        ListCustomer response = new ListCustomer();
        response.setCustomers(customers);
        response.setSuccess(true);
        return response;
    }
}
