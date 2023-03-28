public class GameLoop implements Runnable {
    private boolean running = false;
    private final int FPS = 120;
    private final long TARGET_TIME = 1000 / FPS;
    private Visualizer visualizer;
    private Controller controller;
    private Player player;
    private Zombie zombie;
    private Zombie zombie2;

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        running = false;
    }
    public GameLoop(Visualizer visualizer, Controller controller) {
        this.visualizer = visualizer;
        this.controller = controller;
        this.player = new Player(0D, 0D);
        this.zombie = new Zombie(150D, 150D);
        this.zombie2 = new Zombie(150D, 300D);
        visualizer.addComponent(player, zombie, zombie2);
    }
    public void processEvent(ControllerEvent event) {
        switch (event) {
            case PLAYER_DOWN -> player.moveDown();
            case PLAYER_UP -> player.moveUp();
            case PLAYER_LEFT -> player.moveLeft();
            case PLAYER_RIGHT -> player.moveRight();
            case NOTHING -> {}
        }
    }
    @Override
    public void run() {
        running = true;
        long lastTime = System.currentTimeMillis();
        long start, elapsed, wait, currentTime;

        while (running) {
            start = System.nanoTime();
            currentTime = System.currentTimeMillis();
            processEvent(controller.getEvent());
            zombie.moveToTarget(player.x, player.y, (currentTime - lastTime) / 1000.0);
            zombie2.moveToTarget(player.x, player.y, (currentTime - lastTime) / 1000.0);

            visualizer.update();

            // Обновляем игровое состояние
//            gamePanel.update();

            // Отрисовываем графику
//            gamePanel.render();

            // Вычисляем время, которое заняло выполнение цикла
            elapsed = System.nanoTime() - start;
            lastTime = currentTime;

            // Вычисляем, сколько времени нужно подождать до следующей итерации цикла
            wait = TARGET_TIME - elapsed / 1000000;

            if (wait < 0)
                wait = 5;
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
