/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.inventory.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.bdlions.inventory.dto.DTOCustomer;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.dto.DTOSaleOrder;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerCustomer;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrder;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderProduct;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.report.ReportPayment;
import org.bdlions.inventory.report.ReportProduct;
import org.bdlions.inventory.util.Constants;
import org.bdlions.inventory.util.ServerConfig;
import org.bdlions.inventory.util.TimeUtils;
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
    public void getReport(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("application/pdf");
        String orderNo = "";
        try
        {   
            orderNo = request.getParameter("order_no");
        }
        catch(Exception ex)
        {
            //handle logic if invalid param of orderNo
        }    

        String sourceFileName = getClass().getClassLoader().getResource("reports/sale.jasper").getFile();

        EntityManagerUser entityManagerUser = new EntityManagerUser();

        DTOSaleOrder dtoSaleOrder = new DTOSaleOrder();
        dtoSaleOrder.setEntitySaleOrder(new EntitySaleOrder());
        dtoSaleOrder.getEntitySaleOrder().setOrderNo(orderNo);

        DTOCustomer dtoCustomer = new DTOCustomer();
        dtoCustomer.setEntityCustomer(new EntityCustomer());
        dtoCustomer.setEntityUser(new EntityUser());

        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder();
        EntitySaleOrder entitySaleOrder = entityManagerSaleOrder.getSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        if (entitySaleOrder != null) {
            dtoSaleOrder.setEntitySaleOrder(entitySaleOrder);
            EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct();
            List<EntitySaleOrderProduct> entitySaleOrderProducts = entityManagerSaleOrderProduct.getSaleOrderProductsByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            if (entitySaleOrderProducts != null && !entitySaleOrderProducts.isEmpty()) {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
                for (int counter = 0; counter < entitySaleOrderProducts.size(); counter++) {
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
        else
        {
            //invalid purchase. return from here.
            return;
        }

        double totalSalePrice = 0;
        List<DTOProduct> productList = dtoSaleOrder.getProducts();
        List<ReportProduct> products = new ArrayList<>();
        for (int counter = 0; counter < productList.size(); counter++) {
            DTOProduct dtoProduct = productList.get(counter);
            ReportProduct reportProduct = new ReportProduct();
            reportProduct.setId(counter + 1);
            reportProduct.setName(dtoProduct.getEntityProduct().getName());
            reportProduct.setQuantity(dtoProduct.getQuantity());
            reportProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());
            reportProduct.setDiscount(0);
            reportProduct.setSubTotal(dtoProduct.getQuantity() * dtoProduct.getEntityProduct().getUnitPrice());
            products.add(reportProduct);
            totalSalePrice += reportProduct.getSubTotal();
        }
        
        TimeUtils timeUtils = new TimeUtils();
        String currentDate = timeUtils.convertUnixToHuman(timeUtils.getCurrentTime(), "", "+6");
        
        String reportDirectory = ServerConfig.getInstance().get(ServerConfig.SERVER_BASE_ABS_PATH) + ServerConfig.getInstance().get(ServerConfig.REPORT_PATH);
        Map parameters = new HashMap();
        parameters.put("Date", currentDate);
        parameters.put("CompanyName", ServerConfig.getInstance().get(ServerConfig.COMPANY_NAME));
        parameters.put("CompanyAddress", ServerConfig.getInstance().get(ServerConfig.COMPANY_ADDRESS));
        parameters.put("CompanyCell", ServerConfig.getInstance().get(ServerConfig.COMPANY_CELL));
        parameters.put("OrderNo", dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        parameters.put("CustomerName", dtoCustomer.getEntityUser().getFirstName() + " " + dtoCustomer.getEntityUser().getLastName());
        parameters.put("Address", "Dhaka, Bangladesh");
        parameters.put("Email", dtoCustomer.getEntityUser().getEmail());
        parameters.put("Phone", dtoCustomer.getEntityUser().getCell());
        parameters.put("logoURL", reportDirectory + "logo.png");
        parameters.put("TotalSalePrice", totalSalePrice);
        parameters.put("Remarks", dtoSaleOrder.getEntitySaleOrder().getRemarks() == null ? "" : dtoSaleOrder.getEntitySaleOrder().getRemarks());

        ReportPayment reportPayment = new ReportPayment();
        reportPayment.setId(1);
        reportPayment.setType("Cash");
        reportPayment.setAmount(totalSalePrice);

        List<ReportPayment> payments = new ArrayList<>();
        payments.add(reportPayment);
        parameters.put("payments", payments);
        parameters.put("TotalPaymentAmount", totalSalePrice);
        parameters.put("TotalReturnAmount", 0.0);
        try {
            JasperReport subReport = (JasperReport) JRLoader.loadObject(new File("reports/payments.jasper"));
            parameters.put("subreportFile", subReport);
        } catch (Exception ex) {

        }

        

        //JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(products);

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
            OutputStream os = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, os);

        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
    }
}
