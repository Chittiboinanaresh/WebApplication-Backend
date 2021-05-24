/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.pw.secondi.users;

import javax.ejb.EJBException;

/**
 *
 * @author 588se
 */
public class UserAlreadyExistException extends EJBException {

    private final String username;

    public UserAlreadyExistException(String username) {
        System.out.println("--------------------------------------------------uso UserAlreadyExistException UserAlreadyExistException --------------------------------------------------");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    
}
