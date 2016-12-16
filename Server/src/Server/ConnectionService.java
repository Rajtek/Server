/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.net.Socket;
import java.io.*;
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Rajtek
 */
public class ConnectionService implements Runnable{
    Socket sock;
    
    ConnectionService(Socket clientSocket){
        this.sock=clientSocket;
    }
    @Override
    public void run() {
        try {
            System.out.println("Nasluchuje: "+sock.getRemoteSocketAddress());
            
            
            //tworzenie strumienia danych pobieranych z gniazda sieciowego
            BufferedReader inp;
            inp=new BufferedReader(new InputStreamReader(sock.getInputStream()));
            //ObjectInputStream  ois = new ObjectInputStream(sock.getInputStream());
            //komunikacja - czytanie danych ze strumienia
            try{
            while(true){
            String str;
            str=inp.readLine();
            System.out.println("<Nadeszlo:> " + str +" "+sock.getRemoteSocketAddress());
            if(str.equals("exit"))break;
            }
            }
            catch(java.net.SocketException e){
                System.out.println("Klient "+sock.getRemoteSocketAddress()+" rozłączony");
            }
            finally{
                inp.close();               
                sock.close();
                
                
            }
            //zamykanie polaczenia
            
        } catch (IOException ex) {
            Logger.getLogger(ConnectionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}