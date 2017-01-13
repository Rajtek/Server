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
import Shared.Model.User;
import Shared.Model.Room;

/**
 *
 * @author Rajtek
 */
public class ServerControler implements SocketListener, ControlerInterface, ModelListener {
//    private List<ControlerListener> listeners = new ArrayList<>();

    private HashMap<String, ControlerListener> listeners = new HashMap<>();
    private List<ControlerListener> clientsInLobby = new ArrayList<>();
    private HashMap<Integer, List<ControlerListener>> clientOnRooms = new HashMap<>();
    private ServerModel serverModel;
    private ClientsMap clients;
    private UserSocketMap userMap;

    public ServerControler(ServerModel serverModel) {
        this.serverModel = serverModel;
        clients = new ClientsMap();
        userMap = new UserSocketMap();
        serverModel.setModelListener(this);
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

    private void notifyLobbyListeners(Shared.Messages.Message msg) {
        for (ControlerListener listener : clientsInLobby) {
            listener.messageToSend(msg);
        }
    }

    private void notifyRoomListeners(Shared.Messages.Message msg, int id) {
        for (ControlerListener listener : clientOnRooms.get(id)) {
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
    public void disconnectedClient(String source) {
        ControlerListener listener = listeners.get(source);
        clients.RemoveClientFromList(source);
        String login = userMap.getLogin(source);
        Integer id = serverModel.getRoomIDByLogin(login);

        serverModel.DisconnectedUser(login);
        clientsInLobby.remove(listener);
        userMap.RemoveUserFromList(source);
        notifyLobbyListeners(new MessageRoomsList("server", serverModel.GetRoomsList()));
        disconnectPlayerFromRoom(userMap.getLogin(source), id);
    }

    @Override
    public void reactToMessageAskAboutRoom(String source, int id) {
        notifyListener(new MessageUsersList(source, serverModel.getRoomsMap().get(id).getUsers(), serverModel.getRoomsMap().get(id).getMaxUsers(), id), source);
    }

    @Override
    public void reactToMessageJoinRoom(String source, int id) {
        serverModel.addUserToRoom(userMap.getLogin(source), id);


    }

    public void addListenerInRoom(String source, int id) {
        if (!clientOnRooms.containsKey(id)) {
            clientOnRooms.put(id, new ArrayList<>());
        }
        clientOnRooms.get(id).add(listeners.get(source));
        clientsInLobby.remove(listeners.get(source));
    }

    @Override
    public void reactToMessageLogin(String source, String login) {

        if (serverModel.getUser(login) == null) {
            serverModel.AddUserToList(login);
            userMap.AddUserToList(source, login);
            clientsInLobby.add(listeners.get(source));
            notifyListener(new MessageLoginSuccessful(source, serverModel.getUser(login), serverModel.GetRoomsList()), source);

        } else {
            notifyListener(new MessageLoginFailed(source), source);
        }
    }

    @Override
    public void reactToMessageAskAboutRoomsList(String source) {
        notifyLobbyListeners(new MessageRoomsList("server", serverModel.GetRoomsList()));
    }

    @Override
    public void reactToMessageLoginFailed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageLoginSuccessful(User user, List<Room> roomsList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageRoomsList(List<Room> roomsList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageUserList(List<User> userList, int maxUsers, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageUsersOnRoom(List<User> userlist, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void reactToMessageTextMsg(String login, String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageImage(String source, int id, int[] data) {
        serverModel.setNewDrawing(id, data);
    }

    @Override
    public void reactToMessageAnswer(String source, int id, String answer) {
        serverModel.getAnswer(userMap.getLogin(source), id, answer);
    }

    @Override
    public void drawingChanged(int id, int[] data) {
        notifyRoomListeners(new MessageImage("server", id, data), id);
    }

    @Override
    public void playerLeavedRoom(String login, int id) {
        disconnectPlayerFromRoom(login, id);
    }

    @Override
    public void userJoined(String login, int id) {
        String source = userMap.getSocketAdress(login);
        addListenerInRoom(source, id);
        if (serverModel.getImageDate(id) != null) {
            notifyListener(new MessageImage("server", id, serverModel.getImageDate(id)), source);
        }
        notifyRoomListeners(new MessageRoomStatus("server", serverModel.getRoomByID(id).isGameStarted(), id), id);
        notifyRoomListeners(new MessageUsersInRoom("server", serverModel.getRoomByID(id).getUsers(), id), id);
        notifyLobbyListeners(new MessageRoomsList("server", serverModel.GetRoomsList()));
    }

    @Override
    public void playerIsDrawing(User drawingPlayer, String phrase){
        System.out.println("Server.ServerControler.playerIsDrawing()");
        String login=drawingPlayer.getLogin();
        int id = serverModel.getRoomIDByLogin(login);
        notifyListener(new MessagePlayerDrawing("server", id, phrase), userMap.getSocketAdress(login));
    }
    private void disconnectPlayerFromRoom(String login, int id) {
        clientOnRooms.get(id).remove(listeners.get(userMap.getSocketAdress(login)));
        notifyRoomListeners(new MessageUsersInRoom("server", serverModel.getRoomByID(id).getUsers(), id), id);
    }

    @Override
    public void reactToMessagePlayerDrawing(String phrase) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void broadcastAnswer(int id, String login, String answer) {
        notifyRoomListeners(new MessageTextMsg("server", login, answer), id);

    }

    @Override
    public void gameStopped(int id) {
        notifyRoomListeners(new MessageGameStopped("server"), id);
    }

    @Override
    public void reactToMessageGameStopped() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageRoomStatus(boolean gameStarted, int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reactToMessageGoodAnswer(String source, int id, String answer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void broadcastGoodAnswer(int id, String login, String answer) {
        notifyRoomListeners(new MessageGoodAnswer("server", id, answer), id);
    }
    
}
