/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Shared.Messages.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Shared.Model.ControlerInterface;
import Shared.Model.Player;
import Shared.Model.Table;


/**
 *
 * @author Rajtek
 */
public class ServerControler implements SocketListener, ControlerInterface {
//    private List<ControlerListener> listeners = new ArrayList<>();

    private HashMap<String, ControlerListener> listeners = new HashMap<>();
    private List<ControlerListener> clientsInLobby = new ArrayList<>();
    private HashMap<Integer, List<ControlerListener>> clientOnTables= new HashMap<>();
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
            listener.messageToSend(msg);
        }
    }
    private void notifyLobbyListeners(Shared.Messages.Message msg){
        for(ControlerListener listener : clientsInLobby){
            listener.messageToSend(msg);
        }
    }
    private void notifyTableListeners(Shared.Messages.Message msg, int id){
        for(ControlerListener listener : clientOnTables.get(id)){
            listener.messageToSend(msg);
        }
    }

    synchronized private void notifyListener(Shared.Messages.Message msg, String source) {
        listeners.get(source).messageToSend(msg);
    }
    

    @Override
    synchronized public void getMessage(Message msg) {
        System.err.println("a tutaj może");
        msg.performAction(this);
    }

    @Override
    synchronized public void disconnectedClient(String source) {
        ControlerListener listener = listeners.get(source);
        clients.RemoveClientFromList(source);
        String login = playerMap.getLogin(source);
        Integer id = serverModel.getTableID(login);
        
        serverModel.DisconnectedPlayer(login);
        clientsInLobby.remove(listener);
        playerMap.RemovePlayerFromList(source);
        notifyLobbyListeners(new MessageTablesList("server", serverModel.GetTablesList()));
        if (id!=null){
            clientOnTables.get(id).remove(listener);
            notifyTableListeners(new MessageTableStatus("serwer", serverModel.getTablesMap().get(id)), id);
            
//            notifyTableListeners(new MessagePlayersOnTable("server", serverModel.getTablesMap().get(id).getPlayers(),serverModel.getTablesMap().get(id).getMaxPlayers(), id), id);
        }
    }

   

    @Override
    public void reactToMessageAskAboutTable(String source, int id) {
        notifyListener(new MessagePlayerList(source, serverModel.getTablesMap().get(id).getPlayers(), serverModel.getTablesMap().get(id).getMaxPlayers(),id ), source);
    }

    @Override
    public void reactToMessageJoinTable(String source, int id) {
        serverModel.addPlayerToTable(playerMap.getLogin(source), id);
        if(!clientOnTables.containsKey(id)){
            clientOnTables.put(id, new ArrayList<>());
        }
        notifyTableListeners(new MessageTableStatus("serwer", serverModel.getTablesMap().get(id)), id);
        clientOnTables.get(id).add(listeners.get(source));
        clientsInLobby.remove(listeners.get(source));
        notifyListener(new MessageTableStatus("serwer", serverModel.getTablesMap().get(id)), source);
        notifyLobbyListeners(new MessageTablesList("server", serverModel.GetTablesList()));
    }

    @Override
    public void reactToMessageLogin(String source, String login) {
        System.err.println("tutaj ejste");
        if (serverModel.getPlayer(login) == null) {
            serverModel.AddPlayerToList(login);
            playerMap.AddPlayerToList(source, login);
            clientsInLobby.add(listeners.get(source));
            notifyListener(new MessageLoginSuccessful(source, serverModel.getPlayer(login), serverModel.GetTablesList()), source);
        
        } else {
            notifyListener(new MessageLoginFailed(source), source);
        }
    }
    @Override
    public void reactToMessageAskAboutTablesList(String source){
        notifyLobbyListeners(new MessageTablesList("server", serverModel.GetTablesList()));
    }
    @Override
    public void reactToMessageLoginFailed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageLoginSuccessful(Player player, List<Table> tablesList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageTablesList(List<Table> tablesList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessagePlayerList(Player[] playerList, int maxPlayers, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessagePlayersOnTable(Player[] playerlist, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageCall(int call) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageCheck() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageFold() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageTableStatus(Table table) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
