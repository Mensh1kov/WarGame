package game.models;

import game.models.components.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ClientGameModel
{
    protected Thread gameLoopThread;
    protected boolean running;
    protected final int FPS = 60;
    private Socket socket;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private String name;
    private PlayerControls playerControls = new PlayerControls();

    public ClientGameModel(String name)
    {
        this.name = name;
    }

    private void connect()
    {
        try {
            // Установка соединения с сервером на localhost:8888
            Properties props = new Properties();
            props.load(new FileInputStream("src/main/resources/config_client.properties"));
            socket = new Socket(props.getProperty("ip"), Integer.parseInt(props.getProperty("port")));
            new ObjectOutputStream(socket.getOutputStream()).writeObject(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    public void addGameObject(GameObject object)
    {
        propertyChangeSupport.firePropertyChange("gameObjectAdded", null, object);
    }

    public void removeGameObject(int id)
    {
        propertyChangeSupport.firePropertyChange("gameObjectRemoved", id, null);
    }
    private Object readObject(Socket socket)
    {
        try {
            return new ObjectInputStream(socket.getInputStream()).readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Read exception!");
            throw new RuntimeException(e);
        }
    }

    private void writeObject(Socket socket, Object obj)
    {
        try {
            new ObjectOutputStream(socket.getOutputStream()).writeObject(obj);
        } catch (IOException e) {
            System.out.println("Write exception!");
            throw new RuntimeException(e);
        }
    }

    public PlayerControls getPlayerControls()
    {
        return playerControls;
    }

    public void synchronization()
    {
        writeObject(socket, playerControls); // передаем на сервер информацию о действиях пользователя
        GameState state = (GameState) readObject(socket); // получаем с сервера состояние игры
        state.getRemove().forEach(this::removeGameObject); // удаляем лишние объекты
        state.getObjects().forEach(this::addGameObject); // обновляем и добавляем объекты
    }
    public void startGameLoop()
    {
        connect(); // подключаемя к серверу
        running = true;
        gameLoopThread = new Thread(() -> {
            long targetTime = 1000 / FPS; // Желаемое время между обновлениями

            while (running)
            {
                long startTime = System.currentTimeMillis();

                synchronization(); // синхронизация с сервером

                long elapsedTime = System.currentTimeMillis() - startTime;
                long sleepTime = targetTime - elapsedTime;

                if (sleepTime > 0)
                {
                    try
                    {
                        Thread.sleep(sleepTime); // Ожидание до следующего обновления
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        gameLoopThread.start();
    }
}
