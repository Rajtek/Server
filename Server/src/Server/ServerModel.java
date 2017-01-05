/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Shared.Model.Player;
import Shared.Model.Table;
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
    private HashMap<Player, Table> playerTableMap = new HashMap<>();

    public ServerModel() {
        
//        tablesMap.put(1, new Table(1, 10, 150));
//
//        tablesMap.put(2, new Table(2, 2, 1500));
        for (int i = 0; i < 30; i++) {
            AddTable(150,6);
//            AddPlayerToList("test"+i);
//            tablesMap.get(i).PlayerJoin(playersMap.get("test"+i));
            
        }
//        for (int i = 3; i < 200; i++) {
//            tablesMap.put(i * 2, new Table(i * 2, 2, 100));
//            AddPlayerToList("gracz" + i);
//
//            tablesMap.get(i * 2).PlayerJoin(playersMap.get("gracz" + i));
//
//        }

    }

    public void AddPlayerToList(String login) {

        playersMap.put(login, new Player(login));
//        System.out.println("Server.ServerModel.AddPlayerToList()" + login);
    }

    public void DisconnectedPlayer(String login) {
//        Integer id = playerTableMap.get(playersMap.get(login));
//        if(id!=null){
//            removePlayerFromTable(login);
//        }
        removePlayerFromTable(login);
        playersMap.remove(login);
        
        
    }
    public Integer getTableID(String login){
        try{
        return playerTableMap.get(playersMap.get(login)).getId();
        }
        catch(NullPointerException ex){
            return null;
        }
        

    }
    public int getNumberOfPlayers() {
        return playersMap.size();
    }

    public Player getPlayer(String login) {
        return playersMap.getOrDefault(login, null);
    }

    public void AddTable(int blind, int maxplayers) {
        int id = 0;
        while (true) {
            if (tablesMap.containsKey(id)) {
                id++;
            } else {
                break;
            }
        }
        tablesMap.put(id, new Table(id, maxplayers, blind));
    }

    public Player[] GetPlayersOnTable(int id) {
        if (tablesMap.containsKey(id)) {
            return tablesMap.get(id).getPlayers();
        }
        return null;
    }

    public List<Table> GetTablesList() {
        List<Table> tablesList = new ArrayList();
        for (Table table : tablesMap.values()) {
            tablesList.add(table);
        }
        return tablesList;
    }

    public HashMap<Integer, Table> getTablesMap() {
        return tablesMap;
    }

    public void addPlayerToTable(String login, int id) {
        Player p = getPlayer(login);
        tablesMap.get(id).PlayerJoin(p);
        playerTableMap.put(p, tablesMap.get(id));
    }
    public void removePlayerFromTable(String login){
        Player p = getPlayer(login);
        Table table = playerTableMap.get(p);
        if(table!=null) table.PlayerLeave(p);
        playerTableMap.remove(p);

    }
}
