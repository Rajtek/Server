/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Messages;

/**
 *
 * @author Rajtek
 */
public class MessageJoinTable extends Message{
    private final int id;
    
    
    public MessageJoinTable(String source, int  id) {
        super(source);
        this.id=id;
    }
    public int getID(){
        return id;
    }
}
