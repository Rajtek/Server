/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Shared.MessageLogin;
import Shared.Message;
import Shared.MessageGetTablesList;
import Shared.MessageLoginFailed;
import Shared.MessageLoginSuccessful;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rajtek
 */
public class ServerControler implements SocketListener{
//    private List<ControlerListener> listeners = new ArrayList<>();
    private HashMap<String, ControlerListener> listeners = new HashMap<String, ControlerListener>();
    private ServerModel serverModel;
    private ClientsMap clients;
    private PlayerSocketMap playerMap;
 
    public ServerControler(ServerModel serverModel){
        this.serverModel=serverModel;
        clients = new ClientsMap();
        playerMap = new PlayerSocketMap();
    }
    public void addClient(Socket sock){
        clients.AddClientToList(sock);
    }
    
    public void addListener(ControlerListener toAdd,String source) {
        listeners.put(source,toAdd);
    }
    
    private void notifyAllListener(Shared.Message msg) {
        for (ControlerListener listener : listeners.values()) {
            listener.MessageToSend(msg);
        }
    
    }
    private void notifyListener(Shared.Message msg, String source) {
        listeners.get(source).MessageToSend(msg);
    }
    
    @Override
    public void getMessage(Message msg){
        String source;
        source=msg.getSource();
        if (msg instanceof MessageLogin){
            String login=((MessageLogin) msg).getLogin();
            if(serverModel.getPlayer(login)==null){
                serverModel.AddPlayerToList(login);
                playerMap.AddPlayerToList(source, login);
                notifyListener(new MessageLoginSuccessful(source,serverModel.getPlayer(login)),source);
                notifyListener(new MessageGetTablesList(source, serverModel.GetTablesList()), source);
            }
            else{
                notifyListener(new MessageLoginFailed(source),source);
            }
        }
        if (msg instanceof MessageGetTablesList){
            notifyListener(new MessageGetTablesList(source, serverModel.GetTablesList()), source);
        }
        
    }

    @Override
    public void disconnected(String source) {
       clients.RemoveClientFromList(source);
       serverModel.RemovePlayerFromList(playerMap.getLogin(source));
       playerMap.RemovePlayerFromList(source);
       System.out.println(clients.getNumberOfClients() + " " + serverModel.getNumberOfPlayers() + " " + playerMap.getNumberOfPlayers());
    }
    
    

    
}
