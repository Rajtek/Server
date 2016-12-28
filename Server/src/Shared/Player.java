/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;

import java.io.Serializable;

/**
 *
 * @author Rajtek
 */
public class Player implements Serializable{

    private String login;
    private int cash;

    public Player(String login) {
        this.login = login;
        cash=25000;
    }

    public String getLogin() {
        return login;
    }

    public int getCash() {
        return cash;
    }
}
