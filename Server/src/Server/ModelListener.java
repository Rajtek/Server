/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Shared.Model.User;

/**
 *
 * @author Rajtek
 */
public interface ModelListener {


    public void drawingChanged(int id, int[] data);

    public void playerLeavedRoom(String login, int id);

    public void userJoined(String login, int id);

    public void playerIsDrawing(User drawingPlayer, String phrase);

    public void broadcastAnswer(int id, String login, String answer);

    public void gameStopped(int id);

    public void broadcastGoodAnswer(int id, String login, String answer);
}
