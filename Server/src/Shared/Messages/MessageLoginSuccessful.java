/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Messages;

import Shared.Player;
import Shared.Table;
import java.util.List;

/**
 *
 * @author Rajtek
 */
public class MessageLoginSuccessful extends Message{
    Player player;
    private List<Table> tablesList;
    public MessageLoginSuccessful(String source,Player player,List<Table> tablesList) {
        super(source);
        this.player=player;
        this.tablesList=tablesList;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Table> getTablesList() {
        return tablesList;
    }
    
}
