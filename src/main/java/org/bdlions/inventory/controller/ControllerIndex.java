/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.inventory.controller;

import org.bdlions.inventory.db.HibernateUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alamgir
 */
@CrossOrigin
@RestController
@RequestMapping("/testindex")
public class ControllerIndex {
    @RequestMapping("/testhome")
    String home() {
        HibernateUtil.getSession();
        return "Inventory Server service.";
    }
}
