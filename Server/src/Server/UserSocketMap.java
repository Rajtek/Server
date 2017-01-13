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
public class UserSocketMap {

    private HashMap<String, String> usersMap = new HashMap<>();
    private HashMap<String, String> reversedUsersMap = new HashMap<>();

    public void AddUserToList(String socketAddress, String Login) {
        usersMap.put(socketAddress, Login);
        reversedUsersMap.put(Login, socketAddress);
    }

    public void RemoveUserFromList(String socketAddress) {
        reversedUsersMap.remove(usersMap.get(socketAddress));
        usersMap.remove(socketAddress);

    }

    public int getNumberOfUsers() {
        return usersMap.size();
    }

    public String getLogin(String socketAddress) {
        return usersMap.getOrDefault(socketAddress, null);
    }

    public String getSocketAdress(String login) {
        return reversedUsersMap.getOrDefault(login, null);
    }
}
