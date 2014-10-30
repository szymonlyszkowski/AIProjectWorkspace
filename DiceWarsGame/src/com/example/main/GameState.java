package com.example.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by szymonlyszkowski on 30.10.14.
 */
public class GameState {

    private Graph graph;
    private ArrayList<Vertex> vertices;
    private HashMap<Integer, Vertex> verticesHashMap;

    public GameState(Graph graph) {
        this.graph = graph;
        this.vertices = this.graph.getGraphStructure();
        this.verticesHashMap = new HashMap<Integer, Vertex>();
    }

    public void initGame() {
        assignPlayersToVertices(this.vertices);
        assignDicesToVertices(this.vertices);
        addVerticesToHashMap(this.getVerticesHashMap(), this.vertices);
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

        int dicesDefensive = getVerticesHashMap().get(defensive.getIndex()).getNrOfDices();
        int dicesAttacker = getVerticesHashMap().get(attacker.getIndex()).getNrOfDices();

        if (dicesAttacker <= dicesDefensive) {
            getVerticesHashMap().get(attacker.getIndex()).setNrOfDices(1);
            result = false;
        } else {

            getVerticesHashMap().get(attacker.getIndex()).setNrOfDices(1);
            getVerticesHashMap().get(defensive.getIndex()).setNrOfDices(dicesAttacker - 1);
            getVerticesHashMap().get(defensive.getIndex()).setPlayer(getVerticesHashMap().get(attacker.getIndex()).getPlayer());
            result = true;
        }

        return result;
    }

    public void addDicesToFields(int player) {

        for (Vertex vertex : getVerticesHashMap().values()) {

            if (vertex.getPlayer() == player) {
                int edges = vertex.getNrOfAdjacentEdges();
                if (edges <= 0) {
                    vertex.setNrOfDices(vertex.getNrOfDices() + generateRandomDices());
                } else {

                    vertex.setNrOfDices(vertex.getNrOfDices() + edges);
                }

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
        for (Vertex vertex : getVerticesHashMap().values()) {

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

    private void addVerticesToHashMap(HashMap<Integer, Vertex> verticesHashMap, ArrayList<Vertex> vertices) {

        for (Vertex vertex : vertices) {
            verticesHashMap.put(vertex.getIndex(), vertex);
        }
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


    public HashMap<Integer, Vertex> getVerticesHashMap() {
        return verticesHashMap;
    }
}
