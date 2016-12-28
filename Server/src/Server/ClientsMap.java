/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Rajtek
 */
public class ClientsMap {

    private HashMap<String, Socket> clientsMap = new HashMap<>();

    public void AddClientToList(Socket sock) {
        clientsMap.put(sock.getRemoteSocketAddress().toString(), sock);

    }

    public void RemoveClientFromList(String RemoteSocketAddress) {
        clientsMap.remove(RemoteSocketAddress);

    }

    public int getNumberOfClients() {
        return clientsMap.size();
    }

    public Socket getSocket(String RemoteSocketAddress) {
        return clientsMap.getOrDefault(RemoteSocketAddress, null);
    }

}
