package game.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMouseAdapter extends MouseAdapter
{
    private boolean shootPressed = false;
    private int x = 0;
    private int y = 0;

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton())
        {
            case MouseEvent.BUTTON1 -> shootPressed = true;
        }
        setPos(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        switch (e.getButton())
        {
            case MouseEvent.BUTTON1 -> shootPressed = false;
        }
        setPos(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (shootPressed)
        {
            setPos(e.getX(), e.getY());
        }
    }

    public boolean isShootPressed()
    {
        return shootPressed;
    }

    public void setPos(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
