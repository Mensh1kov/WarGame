package game.models;

import game.models.components.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ServerGameModel extends GameModel
{
    private final int PORT = 8888;
    private ServerSocket serverSocket;
    private final ConcurrentHashMap<Socket, Player> socketPlayerHashMap = new ConcurrentHashMap<>();
    private GameState gameState = new GameState();


    private void startServer()
    {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("src/main/resources/config_server.properties"));
            serverSocket = new ServerSocket(Integer.parseInt(props.getProperty("port")));
            threadPool.submit(this::processConnection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processConnection()
    {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                String name = (String) readObject(socket);
                Player player = getNewPlayer(name, manager.getIdGenerator());
                socketPlayerHashMap.put(socket, player);
                addGameObject(player);
            } catch (IOException e) {
                System.out.println("Fail connection!");
            }
        }
    }

    private Object readObject(Socket socket)
    {
        try {
            return new ObjectInputStream(socket.getInputStream()).readObject();
        } catch (IOException | ClassNotFoundException e) {
            disconnect(socket);
            return null;
        }
    }

    private void writeObject(Socket socket, Object obj)
    {
        try {
            new ObjectOutputStream(socket.getOutputStream()).writeObject(obj);
        } catch (IOException e) {
            disconnect(socket);
        }
    }

    public void synchronization()
    {
        gameState.setObjects(manager.getObjects());
        socketPlayerHashMap.keySet().forEach(socket -> writeObject(socket, gameState));
        gameState.clearRemove();
    }

    public void disconnect(Socket socket)
    {
        removeGameObject(socketPlayerHashMap.remove(socket));
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processConnectedPlayerInput()
    {
        socketPlayerHashMap.forEach((socket, player) -> {
            PlayerControls controls = (PlayerControls) readObject(socket);
            if (controls != null) handlePlayers(controls, player);
        });
    }

    @Override
    public void removeGameObject(GameObject object)
    {
        super.removeGameObject(object);
        gameState.addRemove(object.getId());
    }

    @Override
    public void startGame()
    {
        loadNewGame();
        startGameLoop();
    }

    @Override
    public void stopGame()
    {
        super.stopGameLoop();
    }

    @Override
    public void startGameLoop()
    {
        running = true;
        threadPool.submit(() -> {
            long targetTime = 1000 / FPS; // Желаемое время между обновлениями

            while (running)
            {
                long startTime = System.currentTimeMillis();

                update(); // Выполнение обновления модели
                synchronization(); // синхронизация с сервером подключеных игроков
                processConnectedPlayerInput(); // обработака действий подключенных игроков

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
        startServer();
    }
}
