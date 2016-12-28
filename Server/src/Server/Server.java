/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Rajtek
 */
public class Server {
    private static ServerModel createServerModel(){
        return new ServerModel();
    }
    
    private static ServerControler createServerControler(ServerModel serverModel){
        return new ServerControler(serverModel);
    }
    
    public static void main(String[] args){
        ServerControler s =createServerControler(createServerModel());
        NewClientsWaiter waiter=new NewClientsWaiter();
        waiter.waitForConnection(s);
        
    }
}
