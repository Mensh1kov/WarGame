package game.models;

import game.models.components.GameObject;
import game.models.components.Player;
import game.models.components.PlayerControls;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ServerGameModel extends GameModel
{
    private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);
    private final int PORT = 8888;
    private ServerSocket serverSocket;
    private final ConcurrentHashMap<Socket, Player> socketPlayerHashMap = new ConcurrentHashMap<>();
    private GameState gameState = new GameState();


    private void startServer()
    {
        try {
            serverSocket = new ServerSocket(PORT);
            threadPool.submit(this::processConnection);
            System.out.println("Server started!");
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
                Player player = getPlayer(name);
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
            throw new RuntimeException(e);
        }
    }

    private void writeObject(Socket socket, Object obj)
    {
        try {
            new ObjectOutputStream(socket.getOutputStream()).writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void synchronization()
    {
        gameState.setObjects(objects);
        socketPlayerHashMap.keySet().forEach(socket -> writeObject(socket, gameState));
        gameState.clearRemove();
    }

    private void processConnectedPlayerInput()
    {
        socketPlayerHashMap.forEach((socket, player) -> {
            PlayerControls controls = (PlayerControls) readObject(socket);
            handlePlayers(controls, player);
        });
    }

    @Override
    public void removeGameObject(GameObject object) {
        super.removeGameObject(object);
        gameState.addRemove(object.getId());
    }

    @Override
    public void startGameLoop()
    {
        addGameObject(player);
        running = true;
        gameLoopThread = new Thread(() -> {
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
        gameLoopThread.start();
        startServer();
    }
}
