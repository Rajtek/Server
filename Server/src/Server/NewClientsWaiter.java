/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Rajtek
 */
public class NewClientsWaiter {
    public void waitForConnection(ServerControler serverControler){
        try {
            ServerSocket serv = new ServerSocket(50017);
            while (true) {
                System.out.println("Oczekiwanei na połączenie");
                Socket sock = serv.accept();
                ConnectionService newConnection=new ConnectionService(sock);
                newConnection.addListener(serverControler);
                new Thread(newConnection).start();
                serverControler.addClient(sock);
                serverControler.addListener(newConnection,sock.getRemoteSocketAddress().toString());
                

            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
                
    } 
}
