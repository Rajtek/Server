/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Shared.Message;
import java.net.Socket;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rajtek
 */
public class ConnectionService implements Runnable {

    Socket sock;
    ObjectOutputStream oos;
    ConnectionService(Socket clientSocket) {
        this.sock = clientSocket;
    }
    public void sendMessage(String msg){
        
        try {
            oos.writeObject(new Message(msg));
            System.out.println("Wysyłam: "+ msg );
        } catch (IOException ex) {
            System.err.println(ex);
        }
            
        
    
    }
    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            System.out.println("Nasluchuje: " + sock.getRemoteSocketAddress());

            ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());

            try {
                while (true) {
                    Shared.Message a;
                    a = (Shared.Message) ois.readObject();
                    System.out.println("<Nadeszlo:> " + a.getSource() + " " + sock.getRemoteSocketAddress());
                    sendMessage("testtesttest");
                }
            } catch (java.net.SocketException e) {
                System.out.println("Klient " + sock.getRemoteSocketAddress() + " rozłączony");
                
            } catch (ClassNotFoundException ex) {

            } finally {
                Clients.RemoveClientFromList(sock.getRemoteSocketAddress().toString());
                ois.close();
                sock.close();

            }
            //zamykanie polaczenia

        } catch (IOException ex) {
            Logger.getLogger(ConnectionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
