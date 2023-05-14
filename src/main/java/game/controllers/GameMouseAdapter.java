package game.controllers;

import game.models.GameModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMouseAdapter extends MouseAdapter
{
    private GameModel model;

    public GameMouseAdapter(GameModel model)
    {
        this.model = model;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            double deltaX = e.getX() - model.gameObject.getX();
            double deltaY = e.getY() - model.gameObject.getY();
            double len = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double directionX = deltaX / len;
            double directionY = deltaY / len;
            model.shoot(directionX, directionY);
        }
    }
}
