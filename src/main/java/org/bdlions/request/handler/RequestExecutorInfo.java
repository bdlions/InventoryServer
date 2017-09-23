/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.request.handler;

import java.lang.invoke.MethodHandle;

/**
 *
 * @author alamgir
 */
public class RequestExecutorInfo {

    private MethodHandle method;
    private Object instance;

    public MethodHandle getMethod() {
        return method;
    }

    public void setMethod(MethodHandle method) {
        this.method = method;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

}
