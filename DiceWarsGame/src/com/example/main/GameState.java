package com.example.main;

import ai.dicewars.common.Answer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Created by szymonlyszkowski on 30.10.14.
 */
public class GameState {

    private Graph graph;
    private ArrayList<Vertex> vertices;
    //private HashMap<Integer, Vertex> verticesHashMap;
    
    private Boolean validMove;
    private int whoseTurn;
    
    //create instances of agents 
    AgentRandom player1 = new AgentRandom(1);
    AgentRandom player2 = new AgentRandom(2);

    public GameState(Graph graph) {
        this.graph = graph;
        this.vertices = this.graph.getGraphStructure();
      //  this.verticesHashMap = new HashMap<Integer, Vertex>();
        this.whoseTurn = 1;
    }

    public void initGame() {
        assignPlayersToVertices(this.vertices);
        assignDicesToVertices(this.vertices);
//        addVerticesToHashMap(this.getVerticesHashMap(), this.vertices);
    }

    /**
     * Return true if subjugation was success else return false.
     * When subjugation success set number of dices in Vertex attacker to 1, change owner of Vertex defensive to owner or Vertex attacker, set number of dices in Vertex defensive
     * to number of dices in attacker-1
     * When subjugation fail set number of dices in Vertex to 1.
     *
     * @param attacker
     * @param defensive
     * @return
     */
    public boolean subjugationSuccess(Vertex attacker, Vertex defensive) {
        boolean result;
        // throw all dices
        int dicesDefensive = throwTheDice(defensive.getNrOfDices());
        int dicesAttacker = throwTheDice(attacker.getNrOfDices());
        
        
        
        if (dicesAttacker <= dicesDefensive) {
            attacker.setNrOfDices(1);
            JOptionPane.showMessageDialog(null, 
                    "Player " + attacker.getPlayer() + " moves from " + attacker.getIndex() + " to " + defensive.getIndex() + "\n" +
                            dicesAttacker + " to " + dicesDefensive + "\n" + 
                            "Fight lost!" );
            
            System.out.println("lost");
            result = false;
        } else {
            defensive.setNrOfDices(attacker.getNrOfDices() - 1);
            attacker.setNrOfDices(1);
            defensive.setPlayer(attacker.getPlayer());
            JOptionPane.showMessageDialog(null, 
                   "Player " + attacker.getPlayer() + " moves from " + attacker.getIndex() + " to " + defensive.getIndex() + "\n" +
                    dicesAttacker + " to " + dicesDefensive + "\n" + "Fight won!" );
            System.out.println("won");
            result = true;
        }

        return result;
    }



    // editted by Lukasz
    public void addDicesToFields(int player) {
    	int mostAdjacentEdges = 0;
    	//determine max adjacent edges
        for (Vertex vertex : vertices) {
            if (vertex.getPlayer() == player) {
            	if (vertex.getNrOfAdjacentEdges() > mostAdjacentEdges)
            		mostAdjacentEdges = vertex.getNrOfAdjacentEdges();
            }
        }
        //adding new dices
        Random rand = new Random();
        for (Vertex vertex : vertices) {
            if (vertex.getPlayer() == player) {
        		int newDices = rand.nextInt(mostAdjacentEdges);
        		vertex.setNrOfDices(vertex.getNrOfDices() + newDices);
        		mostAdjacentEdges -= newDices;
            }
        }
    }

    /**
     * Return true if game has ended.
     * Return false if not.
     * @return
     */
    public boolean gameEnds() {

        boolean end = false;
        int player1 = 0;
        int player2 = 0;
//        for (Vertex vertex : getVerticesHashMap().values()) {
        for (Vertex vertex : vertices) {

            if (vertex.getPlayer() == 1) {
                ++player1;
            } else {
                ++player2;
            }
        }
        if (player1 == 0 || player2 == 0) {
            end = true;
        }
        return end;
    }



    private ArrayList<Integer> createPlayersShuffle() {
        ArrayList<Integer> players = new ArrayList();
        int numOfFields = graph.getNrOfVertices();

        for (int i = 0; i < numOfFields; i++) {

            if (i % 2 == 0) {
                players.add(1);
            } else {
                players.add(2);
            }

        }

        Collections.shuffle(players);
        return players;
    }

    private void assignPlayersToVertices(ArrayList<Vertex> vertices) {

        ArrayList<Integer> players = createPlayersShuffle();

        if (players.size() != vertices.size()) {
            System.out.println("Number of fields different than number of players! Sth. went wrong :( ");
            return;
        } else {

            for (int i = 0; i < vertices.size(); i++) {

                vertices.get(i).setPlayer(players.get(i));
            }

        }
    }

    private int generateRandomDices() {
        Random randomGen = new Random();
        return randomGen.nextInt(10) + 1;
    }

    private void assignDicesToVertices(ArrayList<Vertex> vertices) {

        for (Vertex vertex : vertices) {
            vertex.setNrOfDices(generateRandomDices());
        }
    }



    
    int throwTheDice(int dices) {
		Random rand = new Random();
		int result = 0;
		for (int i=0; i<dices; i++) {
			int randomNum = rand.nextInt( 6 ) + 1;
			result += randomNum;
		}
		return result;
	}

//edit by Marcin
    public void gameLoop() {
        System.out.println("The game is rolling");
        while (gameEnds() == false) {

            if (getWhoseTurn() == 1) {

                Answer ans = player1.makeMove(vertices);

                if (ans.isEmptyMove() == true) {
                    endTurn();

                } else {
                    int from = ans.getFrom();
                    Vertex vFrom = null;

                    int to = ans.getTo();
                    Vertex vTo = null;

                    for (Vertex vertf : vertices) {

                        if (vertf.getIndex() == from) {
                            vFrom = vertf;
                            break;
                        }
                    }
                    for (Vertex vertt : vertices) {

                        if (vertt.getIndex() == to) {
                            vTo = vertt;
                            break;
                        }
                    }
                    doMove(vFrom, vTo);
                }

                if (getWhoseTurn() == 2) {

                    Answer ans2 = player2.makeMove(vertices);

                    if (ans2.isEmptyMove() == true) {
                        endTurn();

                    } else {

                        int from = ans.getFrom();
                        Vertex vFrom = null;

                        int to = ans.getTo();
                        Vertex vTo = null;

                        for (Vertex vertf : vertices) {

                            if (vertf.getIndex() == from) {
                                vFrom = vertf;
                                break;
                            }
                        }
                        for (Vertex vertt : vertices) {

                            if (vertt.getIndex() == to) {
                                vTo = vertt;
                                break;
                            }
                        }
                        doMove(vFrom, vTo);
                    }
                }
            }
        }
    }
    
        public void doMove(Vertex attackerVertex, Vertex defenderVertex){

                if((attackerVertex.getAdjacencyList()).indexOf(defenderVertex.getIndex()) >= 0
                        &&
                        attackerVertex.getPlayer() != defenderVertex.getPlayer()
                        &&
                        attackerVertex.getNrOfDices() > 1
                        &&
                        attackerVertex.getPlayer() == whoseTurn) {
                	subjugationSuccess(attackerVertex, defenderVertex);
                	validMove = true;
                } else {
                	JOptionPane.showMessageDialog(null, "Invalid move!");
                	validMove = false;
                }
            
    }
            
        public void endTurn() {  
            addDicesToFields(whoseTurn);
            whoseTurn =  (whoseTurn == 1) ? 2 : 1;
            JOptionPane.showMessageDialog(null, "end of the turn" );
    
    }
        public int getWhoseTurn(){
            return whoseTurn;
        }



}

