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
    private int numberOfPlayers=0;
    private int maxPlayers;
    private int blind;
    private int turn;
    private Player[] players = new Player[10];

    
    public Table(int id){
        this.id = id;
    }
    public Table(int id, int maxPlayers, int blind){
        this.id=id;
        
        this.maxPlayers=maxPlayers;
        this.blind=blind;
        for(int i=0; i<10; i++){
            players[i]=null;
        }
    }
    
    public void PlayerJoin(Player player){
        if(!isFull()){
            for(int i=0; i<10; i++){
                if (players[i]==null){
                    players[i]=player;
                    break;
                    
                   
                }
            }
            numberOfPlayers++;
            System.out.println(id);
            System.out.println(numberOfPlayers);
            for(int i=0; i<10; i++){
                if(players[i]==null)System.out.println(i+" null");
                else
                System.out.println(i+" "+players[i].getLogin());
            }
        }
    }
    public boolean isFull(){
        return numberOfPlayers >= maxPlayers; //nadmiarowe sprawdzenie
    }
    public void PlayerLeave(Player player){
        for(int i=0; i<10; i++){
            if (players[i]==player){
                players[i]=null;
                numberOfPlayers--;
                break;
            }
        }
    }
    
    
    public Player[] getPlayers(){
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
