package org.bdlions.inventory.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class ListPurchaseOrderPayment extends ClientResponse{
    private List<DTOPurchaseOrderPayment> purchaseOrderPayments = new ArrayList<>();    
    //this is for total PurchaseOrders Payments under pagination
    private int totalPurchaseOrderPayments;
    private double totalPaymentAmount;

    public List<DTOPurchaseOrderPayment> getPurchaseOrderPayments() {
        return purchaseOrderPayments;
    }

    public void setPurchaseOrderPayments(List<DTOPurchaseOrderPayment> purchaseOrderPayments) {
        this.purchaseOrderPayments = purchaseOrderPayments;
    }

    public int getTotalPurchaseOrderPayments() {
        return totalPurchaseOrderPayments;
    }

    public void setTotalPurchaseOrderPayments(int totalPurchaseOrderPayments) {
        this.totalPurchaseOrderPayments = totalPurchaseOrderPayments;
    }

    public double getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(double totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }
}
