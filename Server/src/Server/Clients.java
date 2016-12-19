/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Rajtek
 */
public class Clients {

    private static HashMap<String, Socket> clientsMap = new HashMap<String, Socket>();
    private static int connected = 0;

    public static void AddClientToList(Socket sock) {
        clientsMap.put(sock.getRemoteSocketAddress().toString(), sock);
        connected++;
    }

    public static void RemoveClientFromList(String RemoteSocketAddress) {
        clientsMap.remove(RemoteSocketAddress);
        connected--;
    }

    public static int getNumberOfClients() {
        return clientsMap.size();
    }

    public static Socket getSocket(String RemoteSocketAddress) {
        return clientsMap.getOrDefault(RemoteSocketAddress, null);
    }

    public static void WaitForNewClients() {
        try {
            ServerSocket serv = new ServerSocket(50017);
            while (true) {
                System.out.println("Oczekiwanei na połączenie");
                Socket sock = serv.accept();
                new Thread(new ConnectionService(sock)).start();
                Clients.AddClientToList(sock);
                System.out.println(Clients.getNumberOfClients());

            }
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

}
