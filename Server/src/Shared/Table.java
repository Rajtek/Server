/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rajtek
 */
public class Table implements Serializable{
    
    private int id;
    private int numberOfPlayers;
    private int maxPlayers;
    private int blind;
    private int turn;
    private List<Player> players;

    
    public Table(int id){
        this.id = id;
    }
    public Table(int id, int maxPlayers, int blind){
        this.id=id;
        this.maxPlayers=maxPlayers;
        this.blind=blind;
        players = new ArrayList();
    }
    
    public void PlayerJoin(Player player){
        if(!isFull()){
            players.add(player);
            numberOfPlayers++;
            
        }
    }
    public boolean isFull(){
        return players.size() >= maxPlayers; //nadmiarowe sprawdzenie
    }
    public void PlayerLeave(Player player){
        for(int i=0; i<numberOfPlayers; i++){
            if (players.get(i)==player){
                players.remove(i);
                numberOfPlayers--;
                break;
            }
        }
    }
    
    public List<Player> getPlayers(){
        return players;
    }
    

    public int getId() {
        return id;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getBlind() {
        return blind;
    }

}
