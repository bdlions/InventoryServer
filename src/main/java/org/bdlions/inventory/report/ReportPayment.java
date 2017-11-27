package org.bdlions.inventory.report;

/**
 *
 * @author nazmul hasan
 */
public class ReportPayment {
    public int id;
    public String type;
    public double amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
}
