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
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.dto.ListCustomer;
import org.bdlions.library.CustomerLibrary;
import org.bdlions.manager.Customer;

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
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);     
        if(dtoCustomer == null || dtoCustomer.getEntityUser() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Customer Info. Please try again later.");
        }
        else if(dtoCustomer.getEntityUser().getFirstName() == null || dtoCustomer.getEntityUser().getFirstName().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Customer First Name is required.");
        }
        else
        {
            CustomerLibrary customerLibrary = new CustomerLibrary();
            response = customerLibrary.createCustomer(dtoCustomer);
        }        
        return response;
    }
    
    @ClientRequest(action = ACTION.UPDATE_CUSTOMER_INFO)
    public ClientResponse updateCustomerInfo(ISession session, IPacket packet) throws Exception 
    {
        GeneralResponse response = new GeneralResponse();
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class); 
        if(dtoCustomer == null || dtoCustomer.getEntityCustomer() == null || dtoCustomer.getEntityUser() == null)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Customer Info. Please try again later.");
        }
        else if(dtoCustomer.getEntityCustomer().getId() <= 0)
        {
            response.setSuccess(false);
            response.setMessage("Invalid Customer Info. Please try again later..");
        }
        else if(dtoCustomer.getEntityUser().getFirstName() == null || dtoCustomer.getEntityUser().getFirstName().equals(""))
        {
            response.setSuccess(false);
            response.setMessage("Customer First Name is required.");
        }
        else
        {
            CustomerLibrary customerLibrary = new CustomerLibrary();
            response = customerLibrary.updateCustomer(dtoCustomer);
        }        
        return response;
    }
    
    @ClientRequest(action = ACTION.FETCH_CUSTOMER_INFO)
    public ClientResponse getCustomerInfo(ISession session, IPacket packet) throws Exception 
    {
        Gson gson = new Gson();
        DTOCustomer dtoCustomer = gson.fromJson(packet.getPacketBody(), DTOCustomer.class);      
        Customer customer = new Customer();
        DTOCustomer response = customer.getCustomerInfo(dtoCustomer.getEntityCustomer());
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
