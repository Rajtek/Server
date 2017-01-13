/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Shared.Model.Phrases;
import Shared.Model.User;
import Shared.Model.Room;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rajtek
 */
public class ServerModel {

    private HashMap<String, User> usersMap = new HashMap<>();
    private HashMap<Integer, Room> roomsMap = new HashMap<>();
    private HashMap<Integer, int[]> drawings = new HashMap<>();
    private HashMap<User, Room> userRoomMap = new HashMap<>();

    private Phrases phrases = new Phrases("phrases/phrases.txt");
    private ModelListener listener;

    public ServerModel() {

//        roomsMap.put(1, new Room(1, 10, 150));
//
//        roomsMap.put(2, new Room(2, 2, 1500));
        for (int i = 0; i < 30; i++) {
            AddRoom(150, 6);
//            AddUserToList("test"+i);
//            roomsMap.get(i).UserJoin(usersMap.get("test"+i));

        }
//        for (int i = 3; i < 200; i++) {
//            roomsMap.put(i * 2, new Room(i * 2, 2, 100));
//            AddUserToList("gracz" + i);
//
//            roomsMap.get(i * 2).UserJoin(usersMap.get("gracz" + i));
//
//        }

    }

    public void setModelListener(ModelListener listener) {
        this.listener = listener;
    }

    public void AddUserToList(String login) {

        usersMap.put(login, new User(login));
//        System.out.println("Server.ServerModel.AddUserToList()" + login);
    }

    public void DisconnectedUser(String login) {
        removeUserFromRoom(login);
        usersMap.remove(login);

    }

    public Integer getRoomIDByLogin(String login) {
        try {
            return userRoomMap.get(usersMap.get(login)).getId();
        } catch (NullPointerException ex) {
            return null;
        }

    }

    public Room getRoomByID(int id) {
        return roomsMap.get(id);
    }

    public int getNumberOfUsers() {
        return usersMap.size();
    }

    public User getUser(String login) {
        return usersMap.getOrDefault(login, null);
    }

    public void AddRoom(int blind, int maxusers) {
        int id = 0;
        while (true) {
            if (roomsMap.containsKey(id)) {
                id++;
            } else {
                break;
            }
        }
        roomsMap.put(id, new Room(id, maxusers));
    }

    public List<User> GetUsersOnRoom(int id) {
        if (roomsMap.containsKey(id)) {
            return roomsMap.get(id).getUsers();
        }
        return null;
    }

    public List<Room> GetRoomsList() {
        List<Room> roomsList = new ArrayList();
        for (Room room : roomsMap.values()) {
            roomsList.add(room);
        }
        return roomsList;
    }

    public HashMap<Integer, Room> getRoomsMap() {
        return roomsMap;
    }

    public void addUserToRoom(String login, int id) {
        User p = getUser(login);
        roomsMap.get(id).UserJoin(p);
        if (roomsMap.get(id).getNumberOfUsers() > 1) {
            startGameOnTable(id);
        }
        userRoomMap.put(p, roomsMap.get(id));
        listener.userJoined(login, id);
    }

    private void startGameOnTable(int id) {
        if (!roomsMap.get(id).isGameStarted()) {
            roomsMap.get(id).startGame();
        }
        String phrase = roomsMap.get(id).getNewPhrase();
        listener.playerIsDrawing(roomsMap.get(id).getDrawingPlayer(), phrase);
        
    }

    public void removeUserFromRoom(String login) {
        User p = getUser(login);
        Room room = userRoomMap.get(p);
        listener.playerLeavedRoom(login, room.getId());
        if (room != null) {
            room.UserLeave(p);
            if(room.getNumberOfUsers()<=1){
                room.stopGame();
                listener.gameStopped(room.getId());
                
            }
        }
        
        userRoomMap.remove(p);

    }

    public int getID(String login) {

        User p = getUser(login);
        return userRoomMap.get(p).getId();//nie działa
    }

    public void getAnswer(String login, int id, String answer) {
        
        if (roomsMap.get(id).checkAnswer(answer)) {
            
            listener.playerIsDrawing(roomsMap.get(id).getDrawingPlayer(), roomsMap.get(id).getNewPhrase());
            listener.broadcastGoodAnswer(id, login, answer);
        }
        else{
            listener.broadcastAnswer(id, login,answer);
        }
        

    }

    void setNewDrawing(int id, int[] data) {
        drawings.put(id, data);
        listener.drawingChanged(id, data);
    }

    public int[] getImageDate(int id) {
        return drawings.get(id);
    }

}
