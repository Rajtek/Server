/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.*;
import java.net.*;

/**
 *
 * @author Rajtek
 */
public class Server {
    // public static final int PORT=50017;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Clients.WaitForNewClients();

    }
}
