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
import org.bdlions.inventory.entity.EntityCompany;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntitySaleOrder;
import org.bdlions.inventory.entity.EntitySaleOrderProduct;
import org.bdlions.inventory.entity.EntitySaleOrderReturnProduct;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerCompany;
import org.bdlions.inventory.entity.manager.EntityManagerCustomer;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrder;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderProduct;
import org.bdlions.inventory.entity.manager.EntityManagerSaleOrderReturnProduct;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
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
 * @author nazmul hasan
 */
@CrossOrigin
@RestController
public class SaleServlet {
    private final Logger logger = LoggerFactory.getLogger(SaleServlet.class);
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
            logger.error(ex.toString());
            return;
        }    

        //-------------------------------------------------------------------//
        //right now app is hardcoded
        int appId = 10001;
        
        String sourceFileName = getClass().getClassLoader().getResource("reports/sale.jasper").getFile();

        EntityManagerUser entityManagerUser = new EntityManagerUser(appId);

        DTOSaleOrder dtoSaleOrder = new DTOSaleOrder();
        dtoSaleOrder.setEntitySaleOrder(new EntitySaleOrder());
        dtoSaleOrder.getEntitySaleOrder().setOrderNo(orderNo);

        DTOCustomer dtoCustomer = new DTOCustomer();
        dtoCustomer.setEntityCustomer(new EntityCustomer());
        dtoCustomer.setEntityUser(new EntityUser());

        EntityManagerSaleOrder entityManagerSaleOrder = new EntityManagerSaleOrder(appId);
        EntitySaleOrder entitySaleOrder = entityManagerSaleOrder.getSaleOrderByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
        if (entitySaleOrder != null) {
            dtoSaleOrder.setEntitySaleOrder(entitySaleOrder);
            //setting sale order products
            EntityManagerSaleOrderProduct entityManagerSaleOrderProduct = new EntityManagerSaleOrderProduct(appId);
            List<EntitySaleOrderProduct> entitySaleOrderProducts = entityManagerSaleOrderProduct.getSaleOrderProductsByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            if (entitySaleOrderProducts != null && !entitySaleOrderProducts.isEmpty()) {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(appId);
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(appId);
                for (int counter = 0; counter < entitySaleOrderProducts.size(); counter++) {
                    EntitySaleOrderProduct entitySaleOrderProduct = entitySaleOrderProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductBySaleOrderNoAndTransactionCategoryId(entitySaleOrderProduct.getProductId(), dtoSaleOrder.getEntitySaleOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_FULFILLED);
                    EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());

                    DTOProduct dtoProduct = new DTOProduct();
                    dtoProduct.setQuantity(stockProduct.getStockOut());
                    dtoProduct.setEntityProduct(entityProduct);
                    dtoProduct.getEntityProduct().setUnitPrice(entitySaleOrderProduct.getUnitPrice());
                    dtoProduct.setDiscount(entitySaleOrderProduct.getDiscount());
                    dtoSaleOrder.getProducts().add(dtoProduct);
                }
            }
            
            //setting sale order return products
            EntityManagerSaleOrderReturnProduct entityManagerSaleOrderReturnProduct = new EntityManagerSaleOrderReturnProduct(appId);
            List<EntitySaleOrderReturnProduct> entitySaleOrderReturnProducts = entityManagerSaleOrderReturnProduct.getSaleOrderReturnProductsByOrderNo(dtoSaleOrder.getEntitySaleOrder().getOrderNo());
            if(entitySaleOrderReturnProducts != null && !entitySaleOrderReturnProducts.isEmpty())
            {
                EntityManagerShowRoomStock entityManagerShowRoomStock = new EntityManagerShowRoomStock(appId);
                EntityManagerProduct entityManagerProduct = new EntityManagerProduct(appId);
                for(int counter = 0; counter < entitySaleOrderReturnProducts.size(); counter++)
                {
                    EntitySaleOrderReturnProduct entitySaleOrderReturnProduct = entitySaleOrderReturnProducts.get(counter);
                    EntityShowRoomStock stockProduct = entityManagerShowRoomStock.getShowRoomProductBySaleOrderNoAndTransactionCategoryId(entitySaleOrderReturnProduct.getProductId(), dtoSaleOrder.getEntitySaleOrder().getOrderNo(), Constants.SS_TRANSACTION_CATEGORY_ID_SALE_ORDER_RESTOCK);
                    if(stockProduct != null)
                    {
                        EntityProduct entityProduct = entityManagerProduct.getProductByProductId(stockProduct.getProductId());
                        if(entityProduct != null)
                        {
                            entityProduct.setCreatedOn(entitySaleOrderReturnProduct.getCreatedOn());
                            entityProduct.setModifiedOn(entitySaleOrderReturnProduct.getModifiedOn());
                            DTOProduct dtoProduct = new DTOProduct();
                            dtoProduct.setId(entitySaleOrderReturnProduct.getId());
                            dtoProduct.setStockId(stockProduct.getId());
                            dtoProduct.setCreatedOn(TimeUtils.convertUnixToHuman(entitySaleOrderReturnProduct.getCreatedOn(), "", ""));
                            dtoProduct.setModifiedOn(TimeUtils.convertUnixToHuman(entitySaleOrderReturnProduct.getModifiedOn(), "", ""));
                            dtoProduct.setQuantity(stockProduct.getStockIn());
                            dtoProduct.setEntityProduct(entityProduct);
                            dtoProduct.getEntityProduct().setUnitPrice(entitySaleOrderReturnProduct.getUnitPrice());
                            dtoProduct.setDiscount(entitySaleOrderReturnProduct.getDiscount());
                            dtoProduct.getEntityProduct().setCreatedOn(entitySaleOrderReturnProduct.getCreatedOn());
                            dtoSaleOrder.getReturnProducts().add(dtoProduct);
                        }                        
                    }                    
                }
            }
            
            if(dtoSaleOrder.getEntitySaleOrder().getCustomerUserId() > 0)
            {
                EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(appId);
                EntityCustomer entityCustomer = entityManagerCustomer.getCustomerByUserId(dtoSaleOrder.getEntitySaleOrder().getCustomerUserId());
                dtoCustomer.setEntityCustomer(entityCustomer);
                dtoCustomer.setEntityUser(entityManagerUser.getUserByUserId(dtoCustomer.getEntityCustomer().getUserId()));
            }            
        }
        else
        {
            //invalid sale. return from here.
            logger.error("Invalid sale order.");
            return;
        }

        //double totalSalePrice = 0;
        //double totalReturnCash = 0;
        double discountInTotal = entitySaleOrder.getDiscount();
        List<DTOProduct> productList = dtoSaleOrder.getProducts();
        List<ReportProduct> products = new ArrayList<>();
        int productCounter = 1;
        for (int counter = 0; counter < productList.size(); counter++) {
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
            //totalSalePrice += reportProduct.getSubTotal();
        }
        
        List<DTOProduct> returnedProductList = dtoSaleOrder.getReturnProducts();
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
            //totalSalePrice += reportProduct.getSubTotal();
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
        
        String customerName = "";
        if(dtoCustomer.getEntityUser() != null && !StringUtils.isNullOrEmpty(dtoCustomer.getEntityUser().getUserName()))
        {
            customerName = dtoCustomer.getEntityUser().getUserName();
        }
        String address = "";
        if(dtoSaleOrder.getEntitySaleOrder() != null && !StringUtils.isNullOrEmpty(dtoSaleOrder.getEntitySaleOrder().getAddress()))
        {
            address = dtoSaleOrder.getEntitySaleOrder().getAddress();
        }
        
        Map parameters = new HashMap();
        parameters.put("Date", currentDate);
        parameters.put("CompanyName", companyName);
        parameters.put("CompanyAddress", companyAddress);
        parameters.put("CompanyCell", companyCell);
        parameters.put("OrderNo", orderNo);
        parameters.put("CustomerName", customerName);
        parameters.put("Address", address);
        parameters.put("Email", dtoCustomer.getEntityUser().getEmail() == null ? "" : dtoCustomer.getEntityUser().getEmail());
        parameters.put("Phone", dtoCustomer.getEntityUser().getCell() == null ? "" : dtoCustomer.getEntityUser().getCell());
        parameters.put("logoURL", reportDirectory + companyLogo);
        parameters.put("DiscountInTotal", discountInTotal);
        parameters.put("TotalSalePrice", entitySaleOrder.getTotal());
        parameters.put("TotalReturnCash", entitySaleOrder.getCashReturn());
        parameters.put("Remarks", dtoSaleOrder.getEntitySaleOrder().getRemarks() == null ? "" : dtoSaleOrder.getEntitySaleOrder().getRemarks());

        ReportPayment reportPayment = new ReportPayment();
        reportPayment.setId(1);
        reportPayment.setType("Cash");
        reportPayment.setAmount(dtoSaleOrder.getEntitySaleOrder().getPaid());

        List<ReportPayment> payments = new ArrayList<>();
        payments.add(reportPayment);
        parameters.put("payments", payments);
        parameters.put("TotalPaymentAmount", dtoSaleOrder.getEntitySaleOrder().getPaid());
        //parameters.put("TotalReturnAmount", 0.0);
        try 
        {
            JasperReport subReport = (JasperReport) JRLoader.loadObject(new File(ServerConfig.getInstance().get(ServerConfig.SERVER_BASE_ABS_PATH) + ServerConfig.getInstance().get(ServerConfig.JASPER_FILE_PATH) + "payments.jasper"));
            parameters.put("subreportFile", subReport);
        } 
        catch (Exception ex) 
        {
            logger.error(ex.toString());
        }

        

        //JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(products);

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
