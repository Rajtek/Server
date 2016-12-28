/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Rajtek
 */
public class MessageGetTablesList extends Message implements Serializable{
    private List<Table> tablesList;
    public MessageGetTablesList(String source, List<Table> tablesList) {
        super(source);
        this.tablesList=tablesList;
    }
    public List<Table> getTablesList(){
        return tablesList;
    }
}
