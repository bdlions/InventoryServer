package org.bdlions.inventory.exceptions;

/**
 *
 * @author nazmul hasan
 */
public class InvalidRequestException extends Exception{

    public InvalidRequestException(String msg) {
        super(msg);
    }

    public InvalidRequestException() {
        this("Invalid Request.");
    }
    
    
}
