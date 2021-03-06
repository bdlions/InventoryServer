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
import org.bdlions.inventory.entity.EntityPOShowRoomReturnProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerCompany;
import org.bdlions.inventory.entity.manager.EntityManagerPOShowRoomProduct;
import org.bdlions.inventory.entity.manager.EntityManagerPOShowRoomReturnProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrder;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
import org.bdlions.inventory.entity.manager.EntityManagerSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.report.ReportPayment;
import org.bdlions.inventory.report.ReportProduct;
import org.bdlions.inventory.util.Constants;
import org.bdlions.inventory.util.ServerConfig;
import org.bdlions.inventory.util.StringUtils;
import org.bdlions.inventory.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(PurchaseServlet.class);
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
            logger.error(ex.toString());
            return;
        }  
        //-------------------------------------------------------------------//
        //right now app is hardcoded
        int appId = 10001;
        
        EntityManagerUser entityManagerUser = new EntityManagerUser(appId);
        
        DTOSupplier dtoSupplier = new DTOSupplier();
        dtoSupplier.setEntitySupplier(new EntitySupplier());
        dtoSupplier.setEntityUser(new EntityUser());
        
        DTOPurchaseOrder dtoPurchaseOrder = new DTOPurchaseOrder();
        dtoPurchaseOrder.setEntityPurchaseOrder(new EntityPurchaseOrder());
        dtoPurchaseOrder.getEntityPurchaseOrder().setOrderNo(orderNo);
        EntityManagerPurchaseOrder entityManagerPurchaseOrder = new EntityManagerPurchaseOrder(appId);
        EntityPurchaseOrder entityPurchaseOrder = entityManagerPurchaseOrder.getPurchaseOrderByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
        if(entityPurchaseOrder != null)
        {
            dtoPurchaseOrder.setEntityPurchaseOrder(entityPurchaseOrder);
            //getting products for this purchase order
            EntityManagerPOShowRoomProduct entityManagerPOShowRoomProduct = new EntityManagerPOShowRoomProduct(appId);
            List<EntityPOShowRoomProduct> entityPOShowRoomProducts = entityManagerPOShowRoomProduct.getPOShowRoomProductsByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            if(entityPOShowRoomProducts != null && !entityPOShowRoomProducts.isEmpty())
            {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(appId);
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(appId);
                for(int counter = 0; counter < entityPOShowRoomProducts.size(); counter++)
                {
                    EntityPOShowRoomProduct entityPOShowRoomProduct = entityPOShowRoomProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductByPurchaseOrderNoAndTransactionCategoryId(entityPOShowRoomProduct.getProductId(), dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_ORDER_RECEIVE);
                    EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());

                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setQuantity(stockProduct.getStockIn());
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.getEntityProduct().setUnitPrice(entityPOShowRoomProduct.getUnitPrice());
                    dtoProduct.setDiscount(entityPOShowRoomProduct.getDiscount());
                    dtoPurchaseOrder.getProducts().add(dtoProduct);
                }
            }
            
            //getting returned products for this purchase order
            EntityManagerPOShowRoomReturnProduct entityManagerPOShowRoomReturnProduct = new EntityManagerPOShowRoomReturnProduct(appId);
            List<EntityPOShowRoomReturnProduct> entityPOShowRoomReturnProducts = entityManagerPOShowRoomReturnProduct.getPOShowRoomReturnProductsByOrderNo(dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
            if(entityPOShowRoomReturnProducts != null && !entityPOShowRoomReturnProducts.isEmpty())
            {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(appId);
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(appId);
                for(int counter = 0; counter < entityPOShowRoomReturnProducts.size(); counter++)
                {
                    EntityPOShowRoomReturnProduct entityPOShowRoomReturnProduct = entityPOShowRoomReturnProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductByPurchaseOrderNoAndTransactionCategoryId(entityPOShowRoomReturnProduct.getProductId(), dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_PURCASE_ORDER_UNSTOCK);
                    if(stockProduct != null)
                    {
                        EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());
                        if(entityProduct != null)
                        {
                            entityProduct.setCreatedOn(entityPOShowRoomReturnProduct.getCreatedOn());
                            entityProduct.setModifiedOn(entityPOShowRoomReturnProduct.getModifiedOn());
                            DTOProduct dtoProduct = new DTOProduct();
                            dtoProduct.setId(entityPOShowRoomReturnProduct.getId());
                            dtoProduct.setStockId(stockProduct.getId());
                            dtoProduct.setCreatedOn(TimeUtils.convertUnixToHuman(entityPOShowRoomReturnProduct.getCreatedOn(), "", ""));
                            dtoProduct.setModifiedOn(TimeUtils.convertUnixToHuman(entityPOShowRoomReturnProduct.getModifiedOn(), "", ""));
                            dtoProduct.setEntityProduct(entityProduct);
                            dtoProduct.setQuantity(stockProduct.getStockOut());                    
                            dtoProduct.getEntityProduct().setCostPrice(entityPOShowRoomReturnProduct.getUnitPrice());
                            dtoProduct.setDiscount(entityPOShowRoomReturnProduct.getDiscount());
                            dtoProduct.getEntityProduct().setCreatedOn(entityPOShowRoomReturnProduct.getCreatedOn());
                            dtoPurchaseOrder.getReturnProducts().add(dtoProduct);
                        }                              
                    }                                  
                }
            }
            
            EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(appId);
            EntitySupplier entitySupplier = entityManagerSupplier.getSupplierByUserId(dtoPurchaseOrder.getEntityPurchaseOrder().getSupplierUserId());
            dtoSupplier.setEntitySupplier(entitySupplier);
            dtoSupplier.setEntityUser(entityManagerUser.getUserByUserId(dtoSupplier.getEntitySupplier().getUserId()));
        }
        else
        {
            //invalid purchase. return from here.
            logger.error("Invalid purchase order.");
            return;
        }
        
        double discountInTotal = entityPurchaseOrder.getDiscount();
        List<DTOProduct> productList = dtoPurchaseOrder.getProducts();
        List<ReportProduct> products = new ArrayList<>();
        int productCounter = 1;
        for(int counter = 0; counter < productList.size(); counter++)
        {
            DTOProduct dtoProduct = productList.get(counter);
            ReportProduct reportProduct = new ReportProduct();
            reportProduct.setId(productCounter++);
            reportProduct.setName(dtoProduct.getEntityProduct().getName());
            reportProduct.setQuantity(dtoProduct.getQuantity());
            reportProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());
            reportProduct.setDiscount(dtoProduct.getDiscount());
            double subTotal = (dtoProduct.getQuantity()*dtoProduct.getEntityProduct().getUnitPrice()) - (dtoProduct.getQuantity()*dtoProduct.getEntityProduct().getUnitPrice() * dtoProduct.getDiscount() / 100);
            reportProduct.setSubTotal(subTotal);
            products.add(reportProduct);
        }
        
        List<DTOProduct> returnedProductList = dtoPurchaseOrder.getReturnProducts();
        for (int counter = 0; counter < returnedProductList.size(); counter++) {
            DTOProduct dtoProduct = returnedProductList.get(counter);
            ReportProduct reportProduct = new ReportProduct();
            reportProduct.setId(productCounter++);
            reportProduct.setName(dtoProduct.getEntityProduct().getName());
            reportProduct.setQuantity(-dtoProduct.getQuantity());
            reportProduct.setUnitPrice(dtoProduct.getEntityProduct().getUnitPrice());
            reportProduct.setDiscount(dtoProduct.getDiscount());
            double subTotal = (dtoProduct.getQuantity()*dtoProduct.getEntityProduct().getUnitPrice()) - (dtoProduct.getQuantity()*dtoProduct.getEntityProduct().getUnitPrice() * dtoProduct.getDiscount() / 100);
            reportProduct.setSubTotal(-subTotal);            
            products.add(reportProduct);
        }
        
        String currentDate = TimeUtils.convertUnixToHuman(TimeUtils.getCurrentTime(), "", "");
        
        String companyName = "";
        String companyAddress = "";
        String companyCell = "";
        String companyLogo = "";
        EntityManagerCompany entityManagerCompany = new EntityManagerCompany(appId);
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
        
        String supplierName= "";
        if(dtoSupplier.getEntityUser() != null && !StringUtils.isNullOrEmpty(dtoSupplier.getEntityUser().getUserName()))
        {
            supplierName = dtoSupplier.getEntityUser().getUserName();
        }
        String address = "";
        if(dtoPurchaseOrder.getEntityPurchaseOrder() != null && !StringUtils.isNullOrEmpty(dtoPurchaseOrder.getEntityPurchaseOrder().getAddress()))
        {
            address = dtoPurchaseOrder.getEntityPurchaseOrder().getAddress();
        }
        
        
        Map parameters = new HashMap();
        parameters.put("Date", currentDate);
        parameters.put("CompanyName", companyName);
        parameters.put("CompanyAddress", companyAddress);
        parameters.put("CompanyCell", companyCell);
        parameters.put("OrderNo", orderNo);
        parameters.put("SupplierName", supplierName);
        parameters.put("Address", address);
        parameters.put("Email", dtoSupplier.getEntityUser().getEmail() == null ? "" : dtoSupplier.getEntityUser().getEmail());
        parameters.put("Phone", dtoSupplier.getEntityUser().getCell() == null ? "" : dtoSupplier.getEntityUser().getCell());
        parameters.put("logoURL", reportDirectory + companyLogo);
        parameters.put("DiscountInTotal", discountInTotal);
        parameters.put("TotalPurchasePrice", entityPurchaseOrder.getTotal());
        parameters.put("Paid", entityPurchaseOrder.getPaid());
        parameters.put("TotalReturnCash", entityPurchaseOrder.getCashReturn());
        parameters.put("Remarks", dtoPurchaseOrder.getEntityPurchaseOrder().getRemarks() == null ? "" : dtoPurchaseOrder.getEntityPurchaseOrder().getRemarks());
        
        
        ReportPayment reportPayment1 = new ReportPayment();
        reportPayment1.setId(1);
        reportPayment1.setType("Paid");
        reportPayment1.setAmount(entityPurchaseOrder.getPaid());
        ReportPayment reportPayment2 = new ReportPayment();
        reportPayment2.setId(2);
        reportPayment2.setType("Cash Return");
        reportPayment2.setAmount(entityPurchaseOrder.getCashReturn());
        
        List<ReportPayment> payments = new ArrayList<>();
        payments.add(reportPayment1);
        payments.add(reportPayment2);
        parameters.put("payments", payments);
        parameters.put("TotalPaymentAmount", dtoPurchaseOrder.getEntityPurchaseOrder().getPaid());
        //parameters.put("TotalReturnAmount", 0.0);
        try
        {
            JasperReport subReport = (JasperReport) JRLoader.loadObject(new File(ServerConfig.getInstance().get(ServerConfig.SERVER_BASE_ABS_PATH) + ServerConfig.getInstance().get(ServerConfig.JASPER_FILE_PATH) + "payments.jasper"));
            parameters.put("subreportFile", subReport);
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
        

        
        

        //JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(products, true);

        
        String sourceFileName = getClass().getClassLoader().getResource("reports/purchase.jasper").getFile();
        try 
        {
            JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
            OutputStream os = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, os);

        } 
        catch (JRException | IOException ex) 
        {
            logger.error(ex.toString());
        }
    }
}
