/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Shared.Messages.*;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Rajtek
 */
public class ServerControler implements SocketListener {
//    private List<ControlerListener> listeners = new ArrayList<>();

    private HashMap<String, ControlerListener> listeners = new HashMap<String, ControlerListener>();
    private ServerModel serverModel;
    private ClientsMap clients;
    private PlayerSocketMap playerMap;

    public ServerControler(ServerModel serverModel) {
        this.serverModel = serverModel;
        clients = new ClientsMap();
        playerMap = new PlayerSocketMap();
    }

    synchronized public void addClient(Socket sock) {
        clients.AddClientToList(sock);
    }

    synchronized public void addListener(ControlerListener toAdd, String source) {
        listeners.put(source, toAdd);
    }

    synchronized private void notifyAllListener(Shared.Messages.Message msg) {
        for (ControlerListener listener : listeners.values()) {
            listener.MessageToSend(msg);
        }

    }

    synchronized private void notifyListener(Shared.Messages.Message msg, String source) {
        listeners.get(source).MessageToSend(msg);
    }

    @Override
    synchronized public void getMessage(Message msg) {
        String source;
        source = msg.getSource();
        if (msg instanceof MessageLogin) {
            String login = ((MessageLogin) msg).getLogin();
            if (serverModel.getPlayer(login) == null) {
                serverModel.AddPlayerToList(login);
                playerMap.AddPlayerToList(source, login);
                notifyListener(new MessageLoginSuccessful(source, serverModel.getPlayer(login), serverModel.GetTablesList()), source);
//                System.out.println(serverModel.GetTablesList().get(0).getNumberOfPlayers());
                
            } else {
                notifyListener(new MessageLoginFailed(source), source);
            }
        }

        if (msg instanceof MessageTablesList) {
            notifyListener(new MessageTablesList(source, serverModel.GetTablesList()), source);
//            System.out.println(serverModel.GetTablesList().get(0).getNumberOfPlayers());
            
        }

        if (msg instanceof MessageAskAboutTable) {
            int id = ((MessageAskAboutTable) msg).getID();
            notifyListener(new MessagePlayerList(source, serverModel.getTablesMap().get(id).getPlayers(),serverModel.getTablesMap().get(id).getMaxPlayers()), source);
        }
        if (msg instanceof MessageJoinTable) {
            int id = ((MessageJoinTable) msg).getID();
            serverModel.getTablesMap().get(id).PlayerJoin(serverModel.getPlayer(playerMap.getLogin(source)));
            
//            notifyListener(new MessageTablesList(source, serverModel.GetTablesList()), source);
            notifyListener(new MessagePlayersOnTable(source, serverModel.getTablesMap().get(id).getPlayers(), id), source);
            
//            System.out.println(serverModel.GetTablesList().get(0).getNumberOfPlayers());
            
            
            //serverModel.GetTablesList().get(id).PlayerJoin(serverModel.getPlayer(playerMap.getLogin(source)));
            
            //notifyListener(new MessagePlayerList(source, serverModel.getTablesMap().get(id).getPlayers(),serverModel.getTablesMap().get(id).getMaxPlayers()), source);
        }
        

    }

    @Override
    synchronized public void disconnected(String source) {
        clients.RemoveClientFromList(source);
        serverModel.RemovePlayerFromList(playerMap.getLogin(source));
        playerMap.RemovePlayerFromList(source);
        System.out.println(clients.getNumberOfClients() + " " + serverModel.getNumberOfPlayers() + " " + playerMap.getNumberOfPlayers());
    }

}
