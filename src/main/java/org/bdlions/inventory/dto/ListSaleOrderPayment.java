package org.bdlions.inventory.dto;

import com.bdlions.dto.response.ClientResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class ListSaleOrderPayment extends ClientResponse{
    private List<DTOSaleOrderPayment> saleOrderPayments = new ArrayList<>();    
    //this is for total SaleOrders Payments under pagination
    private int totalSaleOrderPayments;
    private double totalPaymentAmount;

    public List<DTOSaleOrderPayment> getSaleOrderPayments() {
        return saleOrderPayments;
    }

    public void setSaleOrderPayments(List<DTOSaleOrderPayment> saleOrderPayments) {
        this.saleOrderPayments = saleOrderPayments;
    }

    public int getTotalSaleOrderPayments() {
        return totalSaleOrderPayments;
    }

    public void setTotalSaleOrderPayments(int totalSaleOrderPayments) {
        this.totalSaleOrderPayments = totalSaleOrderPayments;
    }

    public double getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(double totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    
}
