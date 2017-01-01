/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared.Messages;

import Shared.Table;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Rajtek
 */
public class MessageTablesList extends Message implements Serializable{
    private List<Table> tablesList;
    public MessageTablesList(String source, List<Table> tablesList) {
        super(source);
        this.tablesList=tablesList;
    }
    public List<Table> getTablesList(){
        return tablesList;
    }
}
