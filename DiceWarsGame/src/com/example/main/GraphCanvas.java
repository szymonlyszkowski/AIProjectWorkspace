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
    public static final Color COLOR_PLAYER1 = Color.CYAN;
    public static final Color COLOR_PLAYER2 = Color.YELLOW;
    public static final Color COLOR_PLAYER_BOTH = Color.GREEN;
    public static final Color COLOR_PLAYER_ACTIVE = Color.RED;
    public static final Color COLOR_FINISH_TURN = Color.WHITE;
    private ArrayList<UIVertex> uiVertices;
    private ArrayList<UIEdge> uiEdges;
    private ArrayList<UIVertex> uiVertexList;
    private UIVertex activeVertex;
    private Graph graph;

    
    
    private GameState gameState;
    
    private Boolean validMove;
    private int whoseTurn;

    public GraphCanvas(Graph graph) {
        this.graph = graph;
        this.uiVertices = new ArrayList<UIVertex>();
        this.uiEdges = new ArrayList<UIEdge>();
        this.activeVertex = null;

        gameState = new GameState(graph);
        this.whoseTurn = 1;

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
        
        g.setColor(COLOR_FINISH_TURN);
        g.fillRect(10, 10, 85, 25);
        g.setColor(Color.BLACK);
        g.drawString("NEXT TURN",15,27);
        
        if (whoseTurn == 1) {
           
            g.setColor(COLOR_PLAYER1);
            g.fillRect(10, 45, 120, 25);
            g.setColor(Color.BLACK);
            g.drawString("Player 1 Turn", 15, 66);
        } else {
            
            g.setColor(COLOR_PLAYER2);
            g.fillRect(10, 45, 120, 25);
            g.setColor(Color.BLACK);
            g.drawString("Player 2 Turn", 15, 66);
        }
       
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        // isButton ? refactor!
        if ((x>10 && x<95) && (y>10 && y<35)) {
        	//JOptionPane.showMessageDialog(null, "Turn has been finished!");
        	gameState.addDicesToFields(whoseTurn);
        	whoseTurn =  (whoseTurn == 1) ? 2 : 1;
                
                
        }
        
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
        	if(activeVertex == null && clickedVertex.getBaseVertex().getPlayer() != whoseTurn) {
        		JOptionPane.showMessageDialog(null, "It is not yours!");
        		activeVertex = null;
        	}
        	else if(clickedVertex == activeVertex) {
                activeVertex = null;
                clickedVertex.setActive(false);
            }
            else if (activeVertex == null) {
                activeVertex = clickedVertex;
                activeVertex.setActive(true);
            } else {
                //JOptionPane.showMessageDialog(null, "Clicked from Vertex:\n" + activeVertex.getBaseVertex().toString() + "\nto vertex:\n"+clickedVertex.getBaseVertex().toString());

                if((activeVertex.getBaseVertex().getAdjacencyList()).indexOf(clickedVertex.getBaseVertex().getIndex()) >= 0
                        &&
                        activeVertex.getBaseVertex().getPlayer() != clickedVertex.getBaseVertex().getPlayer()
                        &&
                        activeVertex.getBaseVertex().getNrOfDices() > 1) {
                	gameState.subjugationSuccess(activeVertex.getBaseVertex(), clickedVertex.getBaseVertex());
                	validMove = true;
                } else {
                	JOptionPane.showMessageDialog(null, "Invalid move!");
                	validMove = false;
                }
                activeVertex.setActive(false);
                clickedVertex.setActive(false);
                activeVertex = null;
            }
        }
        else if (activeVertex != null) {
            activeVertex.setActive(false);
            activeVertex = null;
        }
        if (!gameState.gameEnds())
        	this.repaint();
        else
        	JOptionPane.showMessageDialog(null, "The game is over!");
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
