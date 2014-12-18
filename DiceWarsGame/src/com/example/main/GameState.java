package com.example.main;

import ai.dicewars.clips.CLIPSAgent;
import ai.dicewars.common.Agent;
import ai.dicewars.common.Answer;
import ai.dicewars.common.AnswerEx;
import ai.dicewars.fuzzy.FuzzyAgent;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by szymonlyszkowski on 30.10.14.
 */
public class GameState {

    private GraphCanvas canvas;
    private Graph graph;
    private ArrayList<Vertex> vertices;

    private Boolean validMove;
    private int whoseTurn;

    private int mode;

    private int roundsCount;
    private Boolean isDraw;

    //create instances of agents 
    Agent player1 = null;
    Agent player2 = null;

    //Used in mode 2
    public GameState(Graph graph, GraphCanvas canvas, int mode) throws MalformedURLException {

        player1 = new CLIPSAgent(1, "G:\\Moje dokumenty\\Information Technology\\Artificial Intelligence & Expert Systems\\krzysiekz.clp");
        player2 = new CLIPSAgent(2, "G:\\Moje dokumenty\\Information Technology\\Artificial Intelligence & Expert Systems\\krzysiekz.clp");


        this.canvas = canvas;
        this.graph = graph;
        this.vertices = this.graph.getGraphStructure();
        this.whoseTurn = 1;
        this.mode = mode;

        this.roundsCount = 0;
        this.isDraw = false;
    }

    //Used in modes 3 and 4
    public GameState(Graph graph, GraphCanvas canvas, int mode, String player1Path, String player1Namespace, String player2Path, String player2Namespace) throws Exception {
        player1 = new JarLoader().loadAgent(player1Path, player1Namespace);
        player2 = new JarLoader().loadAgent(player2Path, player2Namespace);

        player1.setPlayerNumber(1);
        player2.setPlayerNumber(2);

        this.canvas = canvas;
        this.graph = graph;
        this.vertices = this.graph.getGraphStructure();
        this.whoseTurn = 1;
        this.mode = mode;

        this.roundsCount = 0;
        this.isDraw = false;
    }


    public void initGame() {
        assignPlayersToVertices(this.vertices);
        assignDicesToVertices(this.vertices);
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
            if (mode != 4){
//                JOptionPane.showMessageDialog(null,
//                        "Player " + attacker.getPlayer() + " moves from " + attacker.getIndex() + " to " + defensive.getIndex() + "\n" +
//                                dicesAttacker + " to " + dicesDefensive + "\n" +
//                                "Fight lost!");
            }
            //System.out.println("lost");
            result = false;
        } else {
            defensive.setNrOfDices(attacker.getNrOfDices() - 1);
            attacker.setNrOfDices(1);
            defensive.setPlayer(attacker.getPlayer());

            if(mode != 4) {
            JOptionPane.showMessageDialog(null,
                    "Player " + attacker.getPlayer() + " moves from " + attacker.getIndex() + " to " + defensive.getIndex() + "\n" +
                            dicesAttacker + " to " + dicesDefensive + "\n" + "Fight won!");
            }
            //System.out.println("won");
            result = true;
        }

        this.canvas.repaint();

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
        //System.out.println(player);
        //adding new dices
        Random rand = new Random();
        for (Vertex vertex : vertices) {
            if (vertex.getPlayer() == player) {
                int newDices = rand.nextInt(mostAdjacentEdges);
                if ((vertex.getNrOfDices() + newDices) > 8) {
                    mostAdjacentEdges += (vertex.getNrOfDices()+newDices-8);
                    vertex.setNrOfDices(8);
                }

                vertex.setNrOfDices(vertex.getNrOfDices() + newDices);
                mostAdjacentEdges -= newDices;
            }
        }
    }

    /**
     * Return true if game has ended.
     * Return false if not.
     *
     * @return
     */
    public boolean gameEnds() {
        int player1 = 0;
        int player2 = 0;
        int whoWon = 0;

        for (Vertex vertex : vertices) {

            if (vertex.getPlayer() == 1) {
                ++player1;
            } else {
                ++player2;
            }
        }

        if(roundsCount < 1000) {
            if (player1 == 0 || player2 == 0) {
                if (vertices.get(0).getPlayer() == 1) {
                    whoWon = 1;
                } else {
                    whoWon = 2;
                }
                System.out.print(whoWon);
            }
        }
        else {
            whoWon = player1 > player2 ? 1 : 2;
        }

        return whoWon > 0;
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
        return randomGen.nextInt(8) + 1;
    }

    private void assignDicesToVertices(ArrayList<Vertex> vertices) {

        for (Vertex vertex : vertices) {
            vertex.setNrOfDices(generateRandomDices());
        }
    }


    int throwTheDice(int dices) {
        Random rand = new Random();
        int result = 0;
        for (int i = 0; i < dices; i++) {
            int randomNum = rand.nextInt(6) + 1;
            result += randomNum;
        }
        return result;
    }

    //edit by Marcin
    public void gameLoop() {

        //System.out.println("The game is rolling");
        while (gameEnds() == false) {

            if (getWhoseTurn() == 1) {

                Answer ans = player1.makeMove(vertices);
                roundsCount++;

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
            }
            if (getWhoseTurn() == 2) {

                Answer ans2 = player2.makeMove(vertices);
                roundsCount++;

                if (ans2.isEmptyMove() == true) {
                    endTurn();

                } else {

                    int from = ans2.getFrom();
                    Vertex vFrom = null;

                    int to = ans2.getTo();
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

    public void doMove(Vertex attackerVertex, Vertex defenderVertex) {
        boolean a = (attackerVertex.getAdjacencyList()).indexOf(defenderVertex.getIndex()) >= 0;
        boolean b = attackerVertex.getPlayer() != defenderVertex.getPlayer();
        boolean c = attackerVertex.getNrOfDices() > 1;
        boolean d = attackerVertex.getPlayer() == whoseTurn;
        if (a
                &&
                b
                &&
                c
                && d
                ) {
            subjugationSuccess(attackerVertex, defenderVertex);
            validMove = true;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid move!\n"+a+"\n"+b+"\n"+c+"\n"+d);
            validMove = false;
        }

    }

    public void endTurn() {
        addDicesToFields(whoseTurn);
        whoseTurn = (whoseTurn == 1) ? 2 : 1;

        //JOptionPane.showMessageDialog(null, "end of the turn");

    }

    public int getWhoseTurn() {
        return whoseTurn;
    }


}
