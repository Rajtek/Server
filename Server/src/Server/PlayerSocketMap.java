/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.HashMap;

/**
 *
 * @author Rajtek
 */
public class PlayerSocketMap {

    private HashMap<String, String> playersMap = new HashMap<>();
    private HashMap<String, String> reversedPlayersMap = new HashMap<>();

    public void AddPlayerToList(String socketAddress, String Login) {
        playersMap.put(socketAddress, Login);
        reversedPlayersMap.put(Login, socketAddress);
    }

    public void RemovePlayerFromList(String socketAddress) {
        reversedPlayersMap.remove(playersMap.get(socketAddress));
        playersMap.remove(socketAddress);

    }

    public int getNumberOfPlayers() {
        return playersMap.size();
    }

    public String getLogin(String socketAddress) {
        return playersMap.getOrDefault(socketAddress, null);
    }

    public String getSocketAdress(String login) {
        return reversedPlayersMap.getOrDefault(login, null);
    }
}
