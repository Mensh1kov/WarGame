package game.controllers;

import game.models.GameModel;
import game.models.components.*;
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
    private Map<Integer, GameObjectView> gameObjectViewMap;
    private GameKeyAdapter keyAdapter;
    private GameMouseAdapter mouseAdapter;
    private ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);

    public GameController(GameModel model, GameView view)
    {
        this.gameObjectViewMap = new ConcurrentHashMap<>();
        this.model = model;
        this.view = view;
        this.keyAdapter = new GameKeyAdapter(model.getPlayerControls());
        this.mouseAdapter = new GameMouseAdapter(model.getPlayerControls());

        setupView();
        setupModel();
    }

    public void startGame()
    {
        threadPool.scheduleAtFixedRate(this::updateView, 0, 1000 / model.getFps(), TimeUnit.MILLISECONDS);
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
        model.addGameObject(new Wall(IdGenerator.generateId(),0, 0, 20, 500));
        model.addGameObject(new Wall(IdGenerator.generateId(),20, 0, 480, 20));
        model.addGameObject(new Wall(IdGenerator.generateId(), 480, 20, 20, 480));
        model.addGameObject(new Wall(IdGenerator.generateId(), 20, 480, 460, 20));
        model.addGameObject(new Wall(IdGenerator.generateId(), 80, 80,  340, 20));
        model.addGameObject(new Wall(IdGenerator.generateId(), 80, 380,  340, 20));

        model.addGameObject(new Zombie(IdGenerator.generateId(), 20, 20, 20, 20, 100, 2, model.player.getX(), model.player.getY()));
        model.addGameObject(new Zombie(IdGenerator.generateId(), 150, 150, 40, 40, 500, 2, model.player.getX(), model.player.getY()));
        model.addGameObject(new Zombie(IdGenerator.generateId(), 350, 150, 10, 10, 50, 3, model.player.getX(), model.player.getY()));
        model.addGameObject(new Zombie(IdGenerator.generateId(), 460, 20, 20, 20, 100, 2, model.player.getX(), model.player.getY()));

        model.addSpawner(new SpawnerZombies(100, 250));
        model.addSpawner(new SpawnerZombies(400, 250));
    }

    public void updateView()
    {
        view.printObjectsViews(gameObjectViewMap.values().stream().toList());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getPropertyName().equals("gameObjectAdded"))
        {
            GameObject newGameObject = (GameObject) evt.getNewValue();
            gameObjectViewMap.put(newGameObject.getId(), GameObjectViewFactory.getGameObjectView(newGameObject));
        }
        else if (evt.getPropertyName().equals("gameObjectRemoved"))
        {
            // TODO в model object удалился, а в gameObjectViewMap он остался (происходит очень редко)
            gameObjectViewMap.remove((int) evt.getOldValue());
        }
    }
}
