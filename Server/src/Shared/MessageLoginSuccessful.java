/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;

/**
 *
 * @author Rajtek
 */
public class MessageLoginSuccessful extends Message{
    Player player;
    public MessageLoginSuccessful(String source,Player player) {
        super(source);
        this.player=player;
    }

    public Player getPlayer() {
        return player;
    }
    
}
