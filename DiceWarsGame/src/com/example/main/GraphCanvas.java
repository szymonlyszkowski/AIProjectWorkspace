package com.example.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created by Krzysiek on 2014-10-26.
 */
public class GraphCanvas extends Canvas implements MouseListener {
    public static final Color COLOR_PLAYER1 = Color.ORANGE;
    public static final Color COLOR_PLAYER2 = Color.YELLOW;
    public static final Color COLOR_PLAYER_BOTH = Color.GREEN;
    public static final Color COLOR_PLAYER_ACTIVE = Color.RED;
    private ArrayList<UIVertex> uiVertices;
    private ArrayList<UIEdge> uiEdges;
    private ArrayList<UIVertex> uiVertexList;
    private UIVertex activeVertex;
    private Graph graph;

    private GameState gameState;

    public GraphCanvas(Graph graph) {
        this.graph = graph;
        this.uiVertices = new ArrayList<UIVertex>();
        this.uiEdges = new ArrayList<UIEdge>();
        this.activeVertex = null;

        gameState = new GameState(graph);

        initUIVertices();
        initUIEdges();

        this.addMouseListener(this);
    }

    private void initUIEdges() {
        ArrayList<Vertex> vertices = this.graph.getGraphStructure();
        for(UIVertex singleUIVertex : this.uiVertices) {
            Vertex baseVertex = singleUIVertex.getBaseVertex();
            ArrayList<Integer> adjacencyList = baseVertex.getAdjacencyList();
            // oh noes, that's a consequence of arraylist<int> ~_~
            for(int singleId : adjacencyList) {
                UIVertex adjacentUIVertex = vertices.get(singleId).getUiVertex();
                this.uiEdges.add(new UIEdge(singleUIVertex, adjacentUIVertex));
            }
        }
    }

    private void initUIVertices() {
        ArrayList<Vertex> vertices = this.graph.getGraphStructure();
        int verticesCount = vertices.size();
        int height = 400;
        int width = 640;

        int circleRadius = (int)(height*0.4);
        double theta = 0;
        int x = 0;
        int y = 0;
        for(int i  = 0; i < verticesCount; i++) {
            theta = (2 * Math.PI * i) / verticesCount;
            x = (int)((width / 2) + (circleRadius * Math.cos(theta))-16);
            y = (int)((height / 2) + (circleRadius * Math.sin(theta))-16);
            uiVertices.add(new UIVertex(vertices.get(i), x, y));
        }
    }

    public ArrayList<UIVertex> getUiVertexList() {
        return uiVertexList;
    }

    public void setUiVertexList(ArrayList<UIVertex> uiVertexList) {
        this.uiVertexList = uiVertexList;
    }

    @Override
    public void paint(Graphics g) {
        int vertexCount = this.uiVertices.size();

        for(UIEdge singleEdge : this.uiEdges) {
            singleEdge.paint(g);
        }
        for (UIVertex singleVertex : this.uiVertices) {
            singleVertex.paint(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        UIVertex clickedVertex = null;
        for(UIVertex singleVertex : this.uiVertices) {
            if(singleVertex.isClicked(x,y)) {
                clickedVertex = singleVertex;
            }
        }
        /*if(clickedVertex != null) {
            activeVertex = clickedVertex;
            JOptionPane.showMessageDialog(null, "Click detected on vertex:\n" + clickedVertex.getBaseVertex().toString()+"\nX:" + e.getX() + " Y:" + e.getY());
        }
        else {
            JOptionPane.showMessageDialog(null, "No vertex clicked.\nX:" + e.getX() + " Y:" + e.getY());
        }*/
        if(clickedVertex != null) {
            if(clickedVertex == activeVertex) {
                activeVertex = null;
                clickedVertex.setActive(false);
            }
            else if (activeVertex == null) {
                activeVertex = clickedVertex;
                activeVertex.setActive(true);
            } else {
                JOptionPane.showMessageDialog(null, "Clicked from Vertex:\n" + activeVertex.getBaseVertex().toString() + "\nto vertex:\n"+clickedVertex.getBaseVertex().toString());

                if((activeVertex.getBaseVertex().getAdjacencyList()).indexOf(clickedVertex.getBaseVertex().getIndex()) >= 0
                        &&
                        activeVertex.getBaseVertex().getPlayer() != clickedVertex.getBaseVertex().getPlayer()
                        &&
                        activeVertex.getBaseVertex().getNrOfDices() > 1)
                         {
                /*if(*/
                    gameState.subjugationSuccess(activeVertex.getBaseVertex(), clickedVertex.getBaseVertex());/* == false)*/
                    // activeVertex.getBaseVertex().setNrOfDices(1);
                }
                activeVertex.setActive(false);
                clickedVertex.setActive(false);
                activeVertex = null;


            }
        }
        else {
            activeVertex.setActive(false);
            activeVertex = null;
        }
        this.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
