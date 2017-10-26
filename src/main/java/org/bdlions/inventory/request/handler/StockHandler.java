package org.bdlions.inventory.request.handler;

import org.bdlions.transport.packet.IPacket;
import org.bdlions.session.ISession;
import org.bdlions.session.ISessionManager;
import com.bdlions.util.ACTION;
import com.bdlions.dto.response.ClientResponse;
import java.util.List;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.util.annotation.ClientRequest;
import org.bdlions.inventory.dto.ListDTOProduct;
import org.bdlions.inventory.manager.Stock;

//import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author nazmul hasan
 */
public class StockHandler {

    private final ISessionManager sessionManager;

    public StockHandler(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @ClientRequest(action = ACTION.FETCH_CURRENT_STOCK)
    public ClientResponse getCurrentStock(ISession session, IPacket packet) throws Exception 
    {
        ListDTOProduct listDTOProduct = new ListDTOProduct();
        Stock stock = new Stock();
        List<DTOProduct> products = stock.getCurrentStock();
        listDTOProduct.setProducts(products);
        listDTOProduct.setSuccess(true);
        return listDTOProduct;
    }
}
