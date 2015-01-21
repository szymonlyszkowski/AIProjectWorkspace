package ai.dicewars.fuzzy;

import com.example.main.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to create input for
 * fuzzy logic agent
 */
public class FCLParameters {
    private int dicesAmountInCurrentVertex;
    private int dicesAmountInVertexToAttack;
    private double mineOverallDicesPossessionRatio;
    private double range1Possession;
    private double range2Possession;
    private double range3Possession;
    private int mineOverallDicesAmount;
    private int enemiesOverallDicesAmount;
    private int mineSafeVertexAmount;
    private int mineOverallVertexAmount;
    private double mineOverallVertexPossessionRatio;
    private int enemiesOverallVertexAmount;

    public FCLParameters(Vertex mineVertex,
            Vertex enemiesVertex,
            ArrayList<Vertex> allVertices,
            int playerNumber) {
        dicesAmountInCurrentVertex = mineVertex.getNrOfDices();
        dicesAmountInVertexToAttack = enemiesVertex.getNrOfDices();
        mineOverallDicesAmount = 0;
        enemiesOverallDicesAmount = 0;

        int allVerticesSize = allVertices.size();
        for (int i = 0; i < allVerticesSize; i++) {
            Vertex currentVertex = allVertices.get(i);

            if (currentVertex.getPlayer() == playerNumber) {
                mineOverallDicesAmount += currentVertex.getNrOfDices();
            } else {
                enemiesOverallDicesAmount += currentVertex.getNrOfDices();
            }
        }
        ;

        mineSafeVertexAmount = calculateMineSafeVertexAmount(allVertices);
        mineOverallDicesPossessionRatio = (double) mineOverallDicesAmount / ((double) mineOverallDicesAmount + (double) enemiesOverallDicesAmount);
        mineOverallVertexAmount = calculateMineOverallVertexAmount(playerNumber, allVertices);
        enemiesOverallVertexAmount = calculateEnemiesOverallVertexAmount(playerNumber, allVertices);
        mineOverallVertexPossessionRatio = calculateMineOverallVertexPossessionRatio();

        range1Possession = getPossesion(getAdjacentInRange(mineVertex, allVertices, 1), playerNumber);
        range2Possession = getPossesion(getAdjacentInRange(mineVertex, allVertices, 2), playerNumber);
        range3Possession = getPossesion(getAdjacentInRange(mineVertex, allVertices, 3), playerNumber);
    }



    private double calculateMineOverallVertexPossessionRatio() {

        return (double) mineOverallVertexAmount / ((double) mineOverallVertexAmount + (double) enemiesOverallVertexAmount);
    }

    private int calculateEnemiesOverallVertexAmount(int playerNumber, ArrayList<Vertex> allVertices) {
        int result = 0;
        for (Vertex vertex : allVertices) {
            if (vertex.getPlayer() != playerNumber) {
                ++result;
            }
        }
        return result;
    }

    private int calculateMineOverallVertexAmount(int playerNumber, ArrayList<Vertex> allVertices) {
        int result = 0;
        for (Vertex vertex : allVertices) {
            if (vertex.getPlayer() == playerNumber) {
                ++result;
            }
        }
        return result;
    }

    private int calculateMineSafeVertexAmount(ArrayList<Vertex> allVertices) {
        int safeVertices = 0;
        boolean isVertexFriendly = true;
        for (Vertex vertex : allVertices) {
            ArrayList<Vertex> adjacent = getAdjacentVertices(vertex, allVertices);
            for (Vertex vertexToCompare : adjacent) {
                if (vertex.getPlayer() != vertexToCompare.getPlayer()) {
                    isVertexFriendly = false;
                }

            }
            if (isVertexFriendly) {
                ++safeVertices;
            }
        }
        return safeVertices;
    }

    public int getDicesAmountInCurrentVertex() {
        return dicesAmountInCurrentVertex;
    }

    public int getDicesAmountInVertexToAttack() {
        return dicesAmountInVertexToAttack;
    }

    public double getMineOverallDicesPossessionRatio() {
        return mineOverallDicesPossessionRatio;
    }

    public double getRange1Possession() {
        return range1Possession;
    }

    public double getRange2Possession() {
        return range2Possession;
    }

    public double getRange3Possession() {
        return range3Possession;
    }

    public int getMineOverallDicesAmount() {
        return mineOverallDicesAmount;
    }

    public int getEnemiesOverallDicesAmount() {
        return enemiesOverallDicesAmount;
    }

    public int getMineSafeVertexAmount() {
        return mineSafeVertexAmount;
    }

    public int getEnemiesOverallVertexAmount() {
        return enemiesOverallVertexAmount;
    }

    public int getMineOverallVertexAmount() {
        return mineOverallVertexAmount;
    }

    public double getMineOverallVertexPossessionRatio() {
        return mineOverallVertexPossessionRatio;
    }

    private ArrayList<Vertex> getAdjacentInRange(Vertex center, ArrayList<Vertex> allVertices, int range) {
        ArrayList<Vertex> result = new ArrayList<Vertex>();
        if (range == 0) {
            result.add(center);
        } else {
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
            if (!whereToAdd.contains(currentVertex)) {
                whereToAdd.add(currentVertex);
            }
        }
        return whereToAdd;
    }

    private double getPossesion(ArrayList<Vertex> source, int minePlayerNumber) {
        int mineDicesCount = 0;
        int enemysDicesCount = 0;
        int allVerticesSize = source.size();
        for (int i = 0; i < allVerticesSize; i++) {
            Vertex currentVertex = source.get(i);

            if (currentVertex.getPlayer() == minePlayerNumber) {
                mineDicesCount += currentVertex.getNrOfDices();
            } else {
                enemysDicesCount += currentVertex.getNrOfDices();
            }
        }
        ;

        double result = (double) mineDicesCount / ((double) (mineDicesCount + enemysDicesCount));
        return result;
    }
}
