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
public class Message implements Serializable{
    private String source;
    
    
    public Message(String source){
        this.source=source;
    }
    public String getSource(){
        return source;
    }
}
