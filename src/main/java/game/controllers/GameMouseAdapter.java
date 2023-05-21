package game.controllers;

import game.models.components.PlayerControls;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMouseAdapter extends MouseAdapter
{
    private PlayerControls playerControls;

    public GameMouseAdapter(PlayerControls playerControls)
    {
        this.playerControls = playerControls;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton())
        {
            case MouseEvent.BUTTON1 -> playerControls.setShoot(true);
        }
        setPos(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        switch (e.getButton())
        {
            case MouseEvent.BUTTON1 -> playerControls.setShoot(false);
        }
        setPos(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
      setPos(e.getX(), e.getY());
    }

    public void setPos(int x, int y)
    {
        playerControls.setX(x);
        playerControls.setY(y);
    }
}
