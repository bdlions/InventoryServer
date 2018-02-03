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
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.dto.DTOPurchaseOrder;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.inventory.entity.EntityCompany;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerCompany;
import org.bdlions.inventory.entity.manager.EntityManagerPOShowRoomProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrder;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
import org.bdlions.inventory.entity.manager.EntityManagerSupplier;
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
 * @author alamgir
 */
@CrossOrigin
@RestController
public class PurchaseServlet {

    @RequestMapping("/purchasereport")
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
        
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        
        DTOSupplier dtoSupplier = new DTOSupplier();
        dtoSupplier.setEntitySupplier(new EntitySupplier());
        dtoSupplier.setEntityUser(new EntityUser());
        
        DTOPurchaseOrder dtoPurchaseOrder = new DTOPurchaseOrder();
        dtoPurchaseOrder.setEntityPurchaseOrder(new EntityPurchaseOrder());
        dtoPurchaseOrder.getEntityPurchaseOrder().setOrderNo(orderNo);
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder();
        EntityPurchaseOrder entityPurchaseOrder = entityManagerPurchaseOrder.getPurchaseOrderByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
        if(entityPurchaseOrder != null)
        {
            dtoPurchaseOrder.setEntityPurchaseOrder(entityPurchaseOrder);
            EntityManagerPOShowRoomProduct entityManagerPOShowRoomProduct = new EntityManagerPOShowRoomProduct();
            List<EntityPOShowRoomProduct> entityPOShowRoomProducts = entityManagerPOShowRoomProduct.getPOShowRoomProductsByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            if(entityPOShowRoomProducts != null && !entityPOShowRoomProducts.isEmpty())
            {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock();
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct();
                for(int counter = 0; counter < entityPOShowRoomProducts.size(); counter++)
                {
                    EntityPOShowRoomProduct entityPOShowRoomProduct = entityPOShowRoomProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductByPurchaseOrderNoAndTransactionCategoryId(entityPOShowRoomProduct.getProductId(), dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_IN);
                    EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());

                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setQuantity(stockProduct.getStockIn());
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.getEntityProduct().setUnitPrice(entityPOShowRoomProduct.getUnitPrice());
                    dtoPurchaseOrder.getProducts().add(dtoProduct);
                }
            }
            EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
            EntitySupplier entitySupplier = entityManagerSupplier.getSupplierByUserId(dtoPurchaseOrder.getEntityPurchaseOrder().getSupplierUserId());
            dtoSupplier.setEntitySupplier(entitySupplier);
            dtoSupplier.setEntityUser(entityManagerUser.getUserByUserId(dtoSupplier.getEntitySupplier().getUserId()));
        }
        else
        {
            //invalid purchase. return from here.
            return;
        }
        
        double totalPurchasePrice = 0;
        List<DTOProduct> productList = dtoPurchaseOrder.getProducts();
        List<ReportProduct> products = new ArrayList<>();
        for(int counter = 0; counter < productList.size(); counter++)
        {
            DTOProduct dtoProduct = productList.get(counter);
            ReportProduct reportProduct = new ReportProduct();
            reportProduct.setId(counter+1);
            reportProduct.setName(dtoProduct.getEntityProduct().getName());
            reportProduct.setQuantity(dtoProduct.getQuantity());
            reportProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());
            reportProduct.setDiscount(0);
            reportProduct.setSubTotal(dtoProduct.getQuantity()*dtoProduct.getEntityProduct().getUnitPrice());
            products.add(reportProduct);
            totalPurchasePrice += reportProduct.getSubTotal();
        }
        
        TimeUtils timeUtils = new TimeUtils();
        String currentDate = timeUtils.convertUnixToHuman(timeUtils.getCurrentTime(), "", "");
        
        String companyName = "";
        String companyAddress = "";
        String companyCell = "";
        String companyLogo = "";
        EntityManagerCompany entityManagerCompany = new EntityManagerCompany();
        EntityCompany entityCompany = entityManagerCompany.getCompanyInfo();
        if(entityCompany != null)
        {
            companyName = entityCompany.getName();
            companyAddress = entityCompany.getAddress();
            companyCell = entityCompany.getCell();
            companyLogo = entityCompany.getLogo();
        }
        else
        {
            companyName = ServerConfig.getInstance().get(ServerConfig.COMPANY_NAME);
            companyAddress = ServerConfig.getInstance().get(ServerConfig.COMPANY_ADDRESS);
            companyCell = ServerConfig.getInstance().get(ServerConfig.COMPANY_CELL);
            companyLogo = ServerConfig.getInstance().get(ServerConfig.COMPANY_LOGO);
        }
        
        String reportDirectory = ServerConfig.getInstance().get(ServerConfig.SERVER_BASE_ABS_PATH) + ServerConfig.getInstance().get(ServerConfig.REPORT_PATH);
        Map parameters = new HashMap();
        parameters.put("Date", currentDate);
        parameters.put("CompanyName", companyName);
        parameters.put("CompanyAddress", companyAddress);
        parameters.put("CompanyCell", companyCell);
        parameters.put("OrderNo", dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
        parameters.put("SupplierName", dtoSupplier.getEntityUser().getFirstName()+" "+dtoSupplier.getEntityUser().getLastName());
        parameters.put("Address", "Dhaka, Bangladesh");
        parameters.put("Email", dtoSupplier.getEntityUser().getEmail());
        parameters.put("Phone", dtoSupplier.getEntityUser().getCell());
        parameters.put("logoURL", reportDirectory + companyLogo);
        parameters.put("TotalPurchasePrice", totalPurchasePrice);
        parameters.put("Remarks", dtoPurchaseOrder.getEntityPurchaseOrder().getRemarks() == null ? "" : dtoPurchaseOrder.getEntityPurchaseOrder().getRemarks());
        
        
        ReportPayment reportPayment = new ReportPayment();
        reportPayment.setId(1);
        reportPayment.setType("Cash");
        reportPayment.setAmount(totalPurchasePrice);
        
        List<ReportPayment> payments = new ArrayList<>();
        payments.add(reportPayment);
        parameters.put("payments", payments);
        parameters.put("TotalPaymentAmount", totalPurchasePrice);
        parameters.put("TotalReturnAmount", 0.0);
        try
        {
            JasperReport subReport = (JasperReport) JRLoader.loadObject(new File(ServerConfig.getInstance().get(ServerConfig.SERVER_BASE_ABS_PATH) + ServerConfig.getInstance().get(ServerConfig.JASPER_FILE_PATH) + "payments.jasper"));
            parameters.put("subreportFile", subReport);
        }
        catch(Exception ex)
        {
        
        }
        

        
        

        //JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(products, true);

        
        String sourceFileName = getClass().getClassLoader().getResource("reports/purchase.jasper").getFile();
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
            OutputStream os = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, os);

        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
    }
}
