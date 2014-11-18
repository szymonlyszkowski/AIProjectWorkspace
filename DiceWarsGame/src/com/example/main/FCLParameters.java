package com.example.main;

import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Krzysiek on 2014-11-16.
 * Helper class used to generate FCL input parameters.
 */
public class FCLParameters {
    private int mineDicesCountInCurrentVertex;
    private int enemysDicesCountInCurrentVertex;
    private double overallPossesion;
    private double range1Possesion;
    private double range2Possesion;
    private double range3Possesion;
    private int mineOverallDicesCount;
    private int enemysOverallDicesCount;
    public FCLParameters(Vertex mineVertex,
                         Vertex enemysVertex,
                         ArrayList<Vertex> verticesAdjacentToMine,
                         ArrayList<Vertex> allVertices,
                         int playerNumber) {
        mineDicesCountInCurrentVertex = mineVertex.getNrOfDices();
        enemysDicesCountInCurrentVertex = enemysVertex.getNrOfDices();
        mineOverallDicesCount = 0;
        enemysOverallDicesCount = 0;

        int allVerticesSize = allVertices.size();
        for (int i = 0; i <allVerticesSize; i++) {
            Vertex currentVertex = allVertices.get(i);

            if(currentVertex.getPlayer() == playerNumber) {
                mineOverallDicesCount+=currentVertex.getNrOfDices();
            }
            else {
                enemysOverallDicesCount+=currentVertex.getNrOfDices();
            }
        };

        overallPossesion = mineOverallDicesCount / (mineOverallDicesCount + enemysOverallDicesCount);

        range1Possesion = getPossesion(getAdjacentInRange(mineVertex, allVertices, 1), playerNumber);
        range2Possesion = getPossesion(getAdjacentInRange(mineVertex, allVertices, 2), playerNumber);
        range3Possesion = getPossesion(getAdjacentInRange(mineVertex, allVertices, 3), playerNumber);
    }

    public int getMineDicesCountInCurrentVertex() {
        return mineDicesCountInCurrentVertex;
    }

    public int getEnemysDicesCountInCurrentVertex() {
        return enemysDicesCountInCurrentVertex;
    }

    public double getOverallPossesion() {
        return overallPossesion;
    }

    public double getRange1Possesion() {
        return range1Possesion;
    }

    public double getRange2Possesion() {
        return range2Possesion;
    }

    public double getRange3Possesion() {
        return range3Possesion;
    }

    public int getMineOverallDicesCount() {
        return mineOverallDicesCount;
    }

    public int getEnemysOverallDicesCount() {
        return enemysOverallDicesCount;
    }


    private ArrayList<Vertex> getAdjacentInRange(Vertex center, ArrayList<Vertex> allVertices, int range) {
        ArrayList<Vertex> result = new ArrayList<Vertex>();
        if(range == 0) {
            result.add(center);
        }
        else {
            ArrayList<Vertex> adjacentVertices = getAdjacentVertices(center, allVertices);
            int adjacentEdgesSize = adjacentVertices.size();
            for (int i = 0; i < adjacentEdgesSize; i++) {
                Vertex currentVertex = adjacentVertices.get(i);
                ArrayList<Vertex> adjacentToCurrent = getAdjacentInRange(currentVertex, allVertices, range - 1);
                result = addAllDistinct(result, adjacentToCurrent);
            }
        }
        return result;
    }

    private ArrayList<Vertex> getAdjacentVertices(Vertex vertex, ArrayList<Vertex> allVertices) {
        List<Integer> adjacentIndexes = vertex.getAdjacencyList();
        ArrayList<Vertex> result = new ArrayList<Vertex>();

        int adjacentEdgesSize = vertex.getNrOfAdjacentEdges();
        for (int i = 0; i < adjacentEdgesSize; i++) {
            result.add(allVertices.get(adjacentIndexes.get(i)));
        }

        return result;
    }

    private ArrayList<Vertex> addAllDistinct(ArrayList<Vertex> whereToAdd, ArrayList<Vertex> whatToAdd) {
        int whatToAddSize = whatToAdd.size();
        for (int i = 0; i < whatToAddSize; i++) {
            Vertex currentVertex = whatToAdd.get(i);
            if(!whereToAdd.contains(currentVertex)) {
                whereToAdd.add(currentVertex);
            }
        }
        return whereToAdd;
    }

    private double getPossesion(ArrayList<Vertex> source, int minePlayerNumber) {
        int mineDicesCount = 0;
        int enemysDicesCount = 0;
        int allVerticesSize = source.size();
        for (int i = 0; i <allVerticesSize; i++) {
            Vertex currentVertex = source.get(i);

            if(currentVertex.getPlayer() == minePlayerNumber) {
                mineDicesCount+=currentVertex.getNrOfDices();
            }
            else {
                enemysDicesCount+=currentVertex.getNrOfDices();
            }
        };


        return mineDicesCount / (mineDicesCount + enemysDicesCount);
    }
}
