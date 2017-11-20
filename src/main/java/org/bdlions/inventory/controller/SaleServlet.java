/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.inventory.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.bdlions.inventory.dto.DTOCustomer;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.dto.DTOPurchaseOrder;
import org.bdlions.inventory.dto.DTOSaleOrder;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerCustomer;
import org.bdlions.inventory.entity.manager.EntityManagerPOShowRoomProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrder;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrder;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderProduct;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
import org.bdlions.inventory.entity.manager.EntityManagerSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.util.Constants;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nazmul hasan
 */
@CrossOrigin
@RestController
public class SaleServlet {

    @RequestMapping("/salereport")
    public void getReport(HttpServletResponse response) {

        response.setContentType("application/pdf");

        String sourceFileName = getClass().getClassLoader().getResource("reports/sale.jasper").getFile();
        
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        
        DTOSaleOrder dtoSaleOrder = new DTOSaleOrder();
        dtoSaleOrder.setEntitySaleOrder(new EntitySaleOrder());
        dtoSaleOrder.getEntitySaleOrder().setOrderNo("order1");
        
        DTOCustomer dtoCustomer = new DTOCustomer();
        dtoCustomer.setEntityCustomer(new EntityCustomer());
        dtoCustomer.setEntityUser(new EntityUser());
        
        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        EntitySaleOrder entitySaleOrder = entityManagerSaleOrder.getSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        if(entitySaleOrder != null)
        {
            dtoSaleOrder.setEntitySaleOrder(entitySaleOrder);
            EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct();
            List<EntitySaleOrderProduct> entitySaleOrderProducts = entityManagerSaleOrderProduct.getSaleOrderProductsByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            if(entitySaleOrderProducts != null && !entitySaleOrderProducts.isEmpty())
            {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
                for(int counter = 0; counter < entitySaleOrderProducts.size(); counter++)
                {
                    EntitySaleOrderProduct entitySaleOrderProduct = entitySaleOrderProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductBySaleOrderNoAndTransactionCategoryId(entitySaleOrderProduct.getProductId(), dtoSaleOrder.getEntitySaleOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_SALE_OUT);
                    EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());

                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setQuantity(stockProduct.getStockOut());
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.getEntityProduct().setUnitPrice(entitySaleOrderProduct.getUnitPrice());
                    dtoSaleOrder.getProducts().add(dtoProduct);
                }
            }            
            EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer();
            EntityCustomer entityCustomer = entityManagerCustomer.getCustomerByUserId(dtoSaleOrder.getEntitySaleOrder().getCustomerUserId());
            dtoCustomer.setEntityCustomer(entityCustomer);
            
            dtoCustomer.setEntityUser(entityManagerUser.getUserByUserId(dtoCustomer.getEntityCustomer().getUserId()));
        }
        
        Map parameters = new HashMap();
        parameters.put("Date", "2017-11-20");
        parameters.put("OrderNo", dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        parameters.put("CustomerName", dtoCustomer.getEntityUser().getFirstName()+" "+dtoCustomer.getEntityUser().getLastName());
        parameters.put("Address", "Dhaka, Bangladesh");
        parameters.put("Email", dtoCustomer.getEntityUser().getEmail());
        parameters.put("Phone", dtoCustomer.getEntityUser().getCell());
        
        List<EntityUser> dataList = entityManagerUser.getUsers(1, 10);

        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        //JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dtoPurchaseOrder.getProducts());

        
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
            OutputStream os = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, os);

        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
    }
}
