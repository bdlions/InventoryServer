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
import org.bdlions.inventory.entity.EntityCompany;
import org.bdlions.inventory.entity.EntityCustomer;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerCompany;
import org.bdlions.inventory.entity.manager.EntityManagerCustomer;
import org.bdlions.inventory.entity.manager.EntityManagerSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.report.ReportUser;
import org.bdlions.inventory.util.ServerConfig;
import org.bdlions.inventory.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alamgir
 */
@CrossOrigin
@RestController
public class UserServlet {

    @RequestMapping("/customerreport")
    public void getCustomerReport(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/pdf");
        //-------------------------------------------------------------------//
        //right now app is hardcoded
        int appId = 10001;
        List<ReportUser> reportUsers = new ArrayList<>();
        
        EntityManagerCustomer entityManagerCustomer = new EntityManagerCustomer(appId);
        List<EntityCustomer> entityCustomers = entityManagerCustomer.getCustomers();
        List<Integer> userIds = new ArrayList<>();
        HashMap<Integer, Double> userIdToBalance = new HashMap<>();
        for(int counter = 0; counter < entityCustomers.size(); counter++)
        {
            int userId = entityCustomers.get(counter).getUserId();
            if(!userIds.contains(userId))
            {
                userIds.add(userId);
            }
            userIdToBalance.put(userId, entityCustomers.get(counter).getBalance());
        }
        EntityManagerUser entityManagerUser = new EntityManagerUser(appId);
        List<EntityUser> entityUserList = entityManagerUser.getUsersByUserIds(userIds);
        int counter = 1;
        for(EntityUser entityUser : entityUserList)
        {
            ReportUser reportUser = new ReportUser();
            reportUser.setId(counter++);
            String name = !StringUtils.isNullOrEmpty(entityUser.getUserName())? entityUser.getUserName(): "";
            String fullName = (!StringUtils.isNullOrEmpty(entityUser.getFirstName())? entityUser.getFirstName(): "") + " " + (!StringUtils.isNullOrEmpty(entityUser.getLastName())? entityUser.getLastName(): "");
            String cell = !StringUtils.isNullOrEmpty(entityUser.getCell())? entityUser.getCell(): "";
            reportUser.setName(name);
            reportUser.setFullName(fullName);
            reportUser.setCell(cell);
            if(userIdToBalance.containsKey(entityUser.getId()))
            {
                reportUser.setBalance(userIdToBalance.get(entityUser.getId()));        
            }                
            reportUsers.add(reportUser);
        }
        
        String companyName = "";
        String companyAddress = "";
        String companyCell = "";
        String companyLogo = "";
        String tableTitle = "Customer List with Due Balance";
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
        parameters.put("TableTitle", tableTitle);
        
        //JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportUsers, true);

        
        String sourceFileName = getClass().getClassLoader().getResource("reports/user.jasper").getFile();
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
            OutputStream os = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, os);

        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping("/supplierreport")
    public void getSupplierReport(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/pdf");
        //-------------------------------------------------------------------//
        //right now app is hardcoded
        int appId = 10001;
        List<ReportUser> reportUsers = new ArrayList<>();
        
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier(appId);
        List<EntitySupplier> entitySuppliers = entityManagerSupplier.getSuppliers();
        List<Integer> userIds = new ArrayList<>();
        HashMap<Integer, Double> userIdToBalance = new HashMap<>();
        for(int counter = 0; counter < entitySuppliers.size(); counter++)
        {
            int userId = entitySuppliers.get(counter).getUserId();
            if(!userIds.contains(userId))
            {
                userIds.add(userId);
            }
            userIdToBalance.put(userId, entitySuppliers.get(counter).getBalance());
        }
        
        EntityManagerUser entityManagerUser = new EntityManagerUser(appId);
        List<EntityUser> entityUserList = entityManagerUser.getUsersByUserIds(userIds);
        int counter = 1;
        for(EntityUser entityUser : entityUserList)
        {
            ReportUser reportUser = new ReportUser();
            reportUser.setId(counter++);
            String name = !StringUtils.isNullOrEmpty(entityUser.getUserName())? entityUser.getUserName(): "";
            String fullName = (!StringUtils.isNullOrEmpty(entityUser.getFirstName())? entityUser.getFirstName(): "") + " " + (!StringUtils.isNullOrEmpty(entityUser.getLastName())? entityUser.getLastName(): "");
            String cell = !StringUtils.isNullOrEmpty(entityUser.getCell())? entityUser.getCell(): "";
            reportUser.setName(name);
            reportUser.setFullName(fullName);
            reportUser.setCell(cell);
            if(userIdToBalance.containsKey(entityUser.getId()))
            {
                reportUser.setBalance(userIdToBalance.get(entityUser.getId()));        
            }
            reportUsers.add(reportUser);
        }
        
        String companyName = "";
        String companyAddress = "";
        String companyCell = "";
        String companyLogo = "";
        String tableTitle = "Supplier List with Due Balance";
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
        parameters.put("TableTitle", tableTitle);
        
        //JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportUsers, true);

        
        String sourceFileName = getClass().getClassLoader().getResource("reports/user.jasper").getFile();
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
            OutputStream os = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, os);

        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
    }
}
