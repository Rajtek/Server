/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;


import Shared.Player;
import Shared.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




/**
 *
 * @author Rajtek
 */
public class ServerModel {

    
    private HashMap<String, Player> playersMap = new HashMap<>();
    private HashMap<Integer, Table> tablesMap = new HashMap<>();
    
    public ServerModel() {
        tablesMap.put(1, new Table(1, 6, 150));
        tablesMap.put(2, new Table(2, 2, 1500));
        for(int i=3; i<30; i++){
            tablesMap.put(i, new Table(i,2,100));
        }
        
    }
    public void AddPlayerToList(String Login) {
        
        playersMap.put(Login, new Player(Login));
        System.out.println("Server.ServerModel.AddPlayerToList()"+ Login);
    }

    public void RemovePlayerFromList(String Login) {
        playersMap.remove(Login);
        
    }

    public int getNumberOfPlayers() {
        return playersMap.size();
    }

    public Player getPlayer(String Login) {
        return playersMap.getOrDefault(Login, null);
    }
    
    public void AddTable(int blind, int maxplayers ){
        int id=0;
        while(true){
            if(tablesMap.containsKey(id))
                id++;
            else
                break;
        }
        tablesMap.put(id, new Table(id, maxplayers, blind));
    }

    public List<Player> GetPlayersOnTable(int id){
        if (tablesMap.containsKey(id))
            return tablesMap.get(id).getPlayers();
        return null;
    }
    public List<Table> GetTablesList(){
        List<Table> tablesList= new ArrayList();
        for (Table table : tablesMap.values()) {
            tablesList.add(table);
        }
        return tablesList;
    }
 }

