package com.example.main;

import java.awt.*;

/**
 * Created by Krzysiek on 2014-10-26.
 */
public class UIVertex {
    public static final int SIZE_VERTEX = 32;
    private Vertex baseVertex;
    private int x;
    private int y;
    private boolean active;

    public UIVertex(Vertex baseVertex, int x, int y) {
        this.baseVertex = baseVertex;
        this.x = x;
        this.y = y;
        baseVertex.setUiVertex(this);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Vertex getBaseVertex() {
        return baseVertex;
    }

    public void setBaseVertex(Vertex baseVertex) {
        this.baseVertex = baseVertex;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void paint(Graphics g) {
        if (active) {
            g.setColor(GraphCanvas.COLOR_PLAYER_ACTIVE);
        } else {
            g.setColor(this.baseVertex.getPlayer() % 2 == 0 ? GraphCanvas.COLOR_PLAYER2 : GraphCanvas.COLOR_PLAYER1);
        }
        g.fillOval(this.x, this.y, SIZE_VERTEX, SIZE_VERTEX);
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(this.baseVertex.getNrOfDices()), (int) (this.x + SIZE_VERTEX / 2.5), (int) (this.y + SIZE_VERTEX / 1.5));

        g.drawString(Integer.toString(this.baseVertex.getIndex()), (int) (this.x + 16 + SIZE_VERTEX / 2.5), (int) (this.y + 16 + SIZE_VERTEX / 1.5));
    }

    public boolean isClicked(int clickX, int clickY) {
        if (clickX >= this.x
                && clickX <= this.x + SIZE_VERTEX
                && clickY >= this.y
                && clickY <= this.y + SIZE_VERTEX) {
            return true;
        } else {
            return false;
        }
    }
}
