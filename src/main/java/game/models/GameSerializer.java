package game.models;

import game.models.components.GameManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class GameSerializer {
    public static void saveGame(GameManager gameManager, String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(gameManager);
        } catch (IOException ignored) {
            ignored.getStackTrace();
        }
    }

    public static GameManager loadGame(String filename) {
        GameManager gameManager = null;
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            gameManager = (GameManager) objectIn.readObject();
        } catch (IOException | ClassNotFoundException ignored) {
            ignored.getStackTrace();
        }
        return gameManager;
    }
}