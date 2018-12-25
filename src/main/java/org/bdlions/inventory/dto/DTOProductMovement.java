package org.bdlions.inventory.dto;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOProductMovement {
    private int id;
    private String transactionType;
    private String date;
    private String orderNo;
    private double quantityBefore;
    private double quantity;
    private double quantityAfter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getQuantityBefore() {
        return quantityBefore;
    }

    public void setQuantityBefore(double quantityBefore) {
        this.quantityBefore = quantityBefore;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantityAfter() {
        return quantityAfter;
    }

    public void setQuantityAfter(double quantityAfter) {
        this.quantityAfter = quantityAfter;
    }
    
}
