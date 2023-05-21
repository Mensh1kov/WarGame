package game.controllers;

import game.models.GameModel;
import game.models.Zombie;
import game.models.components.GameObject;
import game.models.components.SpawnerZombies;
import game.models.components.Wall;
import game.views.GameObjectView;
import game.views.GameObjectViewFactory;
import game.views.GameView;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameController implements PropertyChangeListener
{
    private GameModel model;
    private GameView view;
    private Map<GameObject, GameObjectView> gameObjectViewMap;
    private GameKeyAdapter keyAdapter = new GameKeyAdapter();
    private GameMouseAdapter mouseAdapter = new GameMouseAdapter();
    private ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);

    public GameController(GameModel model, GameView view)
    {
        this.gameObjectViewMap = new ConcurrentHashMap<>();
        this.model = model;
        this.view = view;

        setupView();
        setupModel();
    }

    public void startGame()
    {
        threadPool.scheduleAtFixedRate(this::updateView, 0, 1000 / model.getFps(), TimeUnit.MILLISECONDS);
        threadPool.scheduleAtFixedRate(this::updateModel, 0, 1000 / model.getFps(), TimeUnit.MILLISECONDS);
        model.startGameLoop();
    }

    public void setupView()
    {
        view.setFocusable(true);
        view.addKeyListener(keyAdapter);
        view.addMouseListener(mouseAdapter);
        view.addMouseMotionListener(mouseAdapter);
    }

    public void setupModel()
    {
        model.addPropertyChangeListener(this);
        setupGameWorld();
    }

    public void setupGameWorld()
    {
        model.addGameObject(new Wall(0, 0, 20, 500));
        model.addGameObject(new Wall(20, 0, 480, 20));
        model.addGameObject(new Wall(480, 20, 20, 480));
        model.addGameObject(new Wall(20, 480, 460, 20));
        model.addGameObject(new Wall(80, 80,  340, 20));
        model.addGameObject(new Wall(80, 380,  340, 20));

        model.addGameObject(new Zombie(20, 20, 20, 20, 100, 2, model.gameObject.getX(), model.gameObject.getY()));
        model.addGameObject(new Zombie(150, 150, 40, 40, 500, 2, model.gameObject.getX(), model.gameObject.getY()));
        model.addGameObject(new Zombie(350, 150, 10, 10, 50, 3, model.gameObject.getX(), model.gameObject.getY()));
        model.addGameObject(new Zombie(460, 20, 20, 20, 100, 2, model.gameObject.getX(), model.gameObject.getY()));

        model.addSpawner(new SpawnerZombies(100, 250));
        model.addSpawner(new SpawnerZombies(400, 250));
    }

    public void updateView()
    {
        view.printObjectsViews(gameObjectViewMap.values().stream().toList());
    }

    private void updateModel()
    {
        if (keyAdapter.isUpPressed()) model.movePlayerUp();
        if (keyAdapter.isDownPressed()) model.movePlayerDown();
        if (keyAdapter.isLeftPressed()) model.movePlayerLeft();
        if (keyAdapter.isRightPressed()) model.movePlayerRight();
        if (mouseAdapter.isShootPressed()) model.shoot(mouseAdapter.getX(), mouseAdapter.getY());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getPropertyName().equals("gameObjectAdded"))
        {
            GameObject newGameObject = (GameObject) evt.getNewValue();
            gameObjectViewMap.put(newGameObject, GameObjectViewFactory.getGameObjectView(newGameObject));
        }
        else if (evt.getPropertyName().equals("gameObjectRemoved"))
        {
            // TODO в model object удалился, а в gameObjectViewMap он остался (происходит очень редко)
            gameObjectViewMap.remove((GameObject) evt.getOldValue());
        }
    }
}
