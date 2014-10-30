package com.example.main;

import java.awt.*;

/**
 * Created by Krzysiek on 2014-10-29.
 */
public class UIEdge {
    private UIVertex otherVertex;
    private UIVertex firstVertex;

    public UIEdge(UIVertex firstVertex, UIVertex otherVertex) {
        this.otherVertex = otherVertex;
        this.firstVertex = firstVertex;
    }

    public void paint(Graphics g) {
        Color edgeColor = determineEdgeColor();
        g.setColor(edgeColor);
        int halfVertexSize = UIVertex.SIZE_VERTEX/2;
        g.drawLine(this.firstVertex.getX()+halfVertexSize, this.firstVertex.getY()+halfVertexSize,
                this.otherVertex.getX()+halfVertexSize, this.otherVertex.getY()+halfVertexSize);
    }

    private Color determineEdgeColor() {
        if(this.firstVertex.getBaseVertex().getPlayer() == this.otherVertex.getBaseVertex().getPlayer()) {
            return this.firstVertex.getBaseVertex().getPlayer()%2 == 0 ? GraphCanvas.COLOR_PLAYER2 : GraphCanvas.COLOR_PLAYER1;
        }
        else {
            return GraphCanvas.COLOR_PLAYER_BOTH;
        }
    }
}
