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
import org.bdlions.inventory.dto.DTOProduct;
import org.bdlions.inventory.dto.DTOPurchaseOrder;
import org.bdlions.inventory.dto.DTOSupplier;
import org.bdlions.inventory.entity.EntityPOShowRoomProduct;
import org.bdlions.inventory.entity.EntityProduct;
import org.bdlions.inventory.entity.EntityPurchaseOrder;
import org.bdlions.inventory.entity.EntityShowRoomStock;
import org.bdlions.inventory.entity.EntitySupplier;
import org.bdlions.inventory.entity.EntityUser;
import org.bdlions.inventory.entity.manager.EntityManagerPOShowRoomProduct;
import org.bdlions.inventory.entity.manager.EntityManagerProduct;
import org.bdlions.inventory.entity.manager.EntityManagerPurchaseOrder;
import org.bdlions.inventory.entity.manager.EntityManagerShowRoomStock;
import org.bdlions.inventory.entity.manager.EntityManagerSupplier;
import org.bdlions.inventory.entity.manager.EntityManagerUser;
import org.bdlions.inventory.util.Constants;
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
    public void getReport(HttpServletResponse response) {

        response.setContentType("application/pdf");

        String sourceFileName = getClass().getClassLoader().getResource("reports/purchase.jasper").getFile();
        
        DTOPurchaseOrder dtoPurchaseOrder = new DTOPurchaseOrder();
        dtoPurchaseOrder.setEntityPurchaseOrder(new EntityPurchaseOrder());
        dtoPurchaseOrder.getEntityPurchaseOrder().setOrderNo("order1");
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
        } 
        DTOSupplier dtoSupplier = new DTOSupplier();
        EntityManagerSupplier entityManagerSupplier = new EntityManagerSupplier();
        EntitySupplier entitySupplier = entityManagerSupplier.getSupplierByUserId(dtoPurchaseOrder.getEntityPurchaseOrder().getSupplierUserId());
        dtoSupplier.setEntitySupplier(entitySupplier);
        EntityManagerUser entityManagerUser = new EntityManagerUser();
        dtoSupplier.setEntityUser(entityManagerUser.getUserByUserId(dtoSupplier.getEntitySupplier().getUserId()));
        
        Map parameters = new HashMap();
        parameters.put("Date", "2017-11-20");
        parameters.put("OrderNo", dtoPurchaseOrder.getEntityPurchaseOrder().getOrderNo());
        parameters.put("SupplierName", dtoSupplier.getEntityUser().getFirstName()+" "+dtoSupplier.getEntityUser().getLastName());
        parameters.put("Address", "Dhaka, Bangladesh");
        parameters.put("Email", dtoSupplier.getEntityUser().getEmail());
        parameters.put("Phone", dtoSupplier.getEntityUser().getCell());
        
        //EntityManagerUser entityManagerUser = new EntityManagerUser();
        List<EntityUser> dataList = entityManagerUser.getUsers(1, 10);

        //JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dtoPurchaseOrder.getProducts());

        
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
            OutputStream os = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, os);

        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
    }
}
