/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Shared.Message;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ConnectionService implements Runnable, ControlerListener {
    
    private Socket sock;
    private ObjectOutputStream oos;
    private List<SocketListener> listeners = new ArrayList<>();

    ConnectionService(Socket clientSocket) {
        this.sock = clientSocket;
    }

    public void sendMessage(String msg) {
        
        try {
            oos.writeObject(new Message(msg));
            System.out.println("Wysyłam: " + msg);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        
    }
    
    public void sendMessage(Message msg) {
        
        try {
            oos.writeObject(msg);
            System.out.println("Wysyłam: " + msg.getSource());
        } catch (IOException ex) {
            System.err.println(ex);
        }
        
    }
    

    public void addListener(SocketListener toAdd) {
        listeners.add(toAdd);
    }
    
    private void notifyListener(Shared.Message msg) {
        for (SocketListener s : listeners) {
            s.getMessage(msg);
        }
    }
    
    private void notifyDisconnect(String client){
        for (SocketListener s : listeners) {
            s.disconnected(sock.getRemoteSocketAddress().toString());
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
                    
                    notifyListener(a);
                    //System.out.println("|" + a.getSource() + " | " + sock.getRemoteSocketAddress());
                    sendMessage(a.getSource()+" :odebrane");
                }
            } catch (java.net.SocketException e) {
                System.out.println("Klient " + sock.getRemoteSocketAddress() + " rozłączony");
                notifyDisconnect(sock.getRemoteSocketAddress().toString());
                
            } catch (ClassNotFoundException ex) {
                
            } finally {
                ois.close();
                sock.close();
                
            }
            //zamykanie polaczenia

        } catch (IOException ex) {
            
        }
    }

    @Override
    public void MessageToSend(Message msg) {
        sendMessage(msg);
    }
    
}
