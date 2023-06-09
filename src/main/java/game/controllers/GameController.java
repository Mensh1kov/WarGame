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
    private Map<Long, GameObjectView> gameObjectViewMap;
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
        model.startGame();
    }

    public void stopGame()
    {
        model.stopGame();
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
            gameObjectViewMap.remove((long) evt.getOldValue());
        }
    }
}
