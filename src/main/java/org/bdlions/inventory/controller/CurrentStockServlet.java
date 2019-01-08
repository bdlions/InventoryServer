package org.bdlions.inventory.controller;

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
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.entity.EntityCompany;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.manager.EntityManagerCompany;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.manager.Stock;
import org.bdlions.inventory.report.ReportProduct;
import org.bdlions.inventory.util.ServerConfig;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alamgir
 */
@CrossOrigin
@RestController
public class CurrentStockServlet {

    @RequestMapping("/currentstockreport")
    public void getReport(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/pdf");
        //-------------------------------------------------------------------//
        //right now app is hardcoded
        int appId = 10001;
        
        EntityManagerProduct entityManagerProduct = new EntityManagerProduct(appId);
        List<EntityProduct> products = entityManagerProduct.getProducts();
        List<Integer> productIds = new ArrayList<>();
        List<DTOProduct> productWithStocks = new ArrayList<>();
        if(products != null && !products.isEmpty())
        {
            for(int counter = 0; counter < products.size(); counter++)
            {
                if(!productIds.contains(products.get(counter).getId()))
                {
                    productIds.add(products.get(counter).getId());
                }
            }
            Stock stock = new Stock(appId);
            productWithStocks = stock.getCurrentStockByProductIds(productIds);
            List<Integer> excludedProductIds = new ArrayList<>();
            List<Integer> tempProductIds = new ArrayList<>();
            if(productWithStocks != null && !productWithStocks.isEmpty())
            {
                for(int counter = 0; counter < productWithStocks.size(); counter++)
                {
                    if(!tempProductIds.contains(productWithStocks.get(counter).getEntityProduct().getId()))
                    {
                        tempProductIds.add(productWithStocks.get(counter).getEntityProduct().getId());
                    }
                }
            }
            for(int productId: productIds)
            {
                if(!tempProductIds.contains(productId))
                {
                    excludedProductIds.add(productId);
                }
            }
            for(EntityProduct entityProduct: products)
            {
                if(excludedProductIds.contains(entityProduct.getId()))
                {
                    DTOProduct tempDTOProduct = new DTOProduct();
                    tempDTOProduct.setQuantity(0);
                    tempDTOProduct.setEntityProduct(entityProduct);
                    productWithStocks.add(tempDTOProduct);
                }
            }
        }
        
        List<ReportProduct> reportProducts = new ArrayList<>();
        for(int counter = 0; counter < productWithStocks.size(); counter++)
        {
            DTOProduct dtoProduct = productWithStocks.get(counter);
            ReportProduct reportProduct = new ReportProduct();
            reportProduct.setId(counter+1);
            reportProduct.setCategory(dtoProduct.getEntityProduct().getCategoryTitle());
            reportProduct.setName(dtoProduct.getEntityProduct().getName());
            reportProduct.setQuantity(dtoProduct.getQuantity());
            reportProducts.add(reportProduct);
        }
        
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
        
        Map parameters = new HashMap();
        parameters.put("CompanyName", companyName);
        parameters.put("CompanyAddress", companyAddress);
        parameters.put("CompanyCell", companyCell);
        parameters.put("logoURL", reportDirectory + companyLogo);
        
        //JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportProducts, true);

        
        String sourceFileName = getClass().getClassLoader().getResource("reports/currentstock.jasper").getFile();
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
            OutputStream os = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, os);

        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
    }
}
