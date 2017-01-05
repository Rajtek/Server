/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author Rajtek
 */
public interface ControlerListener {
    public void messageToSend(Shared.Messages.Message msg);
    
}
