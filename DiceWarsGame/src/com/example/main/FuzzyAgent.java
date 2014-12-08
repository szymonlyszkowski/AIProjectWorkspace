package com.example.main;

import ai.dicewars.common.Agent;
import ai.dicewars.common.Answer;
import ai.dicewars.common.AnswerEx;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krzysiek on 2014-11-16.
 */
public class FuzzyAgent implements Agent {
    private final FIS fis;
    private final FunctionBlock functionBlock;
    private int playerNumber;
    /*
     * A default constructor which sets FCL file to "fuzzyPlayer.fcl".
     * In general, it shouldn't be used.
     */
    public FuzzyAgent(int playerNumber) throws FileNotFoundException {
        this(playerNumber, "templateFuzzy.fcl");
    };

    public FuzzyAgent(int playerNumber, String filename) throws FileNotFoundException {
        this.playerNumber = playerNumber;
        fis = FIS.load(filename, true);

        if(fis == null) {
            throw new FileNotFoundException("Can't load file: '" + filename + "'");
        }

        functionBlock = fis.getFunctionBlock(null);
    }

    @Override
    public Answer makeMove(ArrayList<Vertex> vertices) {
        Boolean isEmpty = true;

        /* A. iterate through all of my vertices
         *    for any of them fetch adjacent ones that belong to the enemy
         *      B. for each of them:
         *         determine if fight or not (FCL; FuzzyLogic decides)
         *              1. Yup - fight!
         *              2. Nope - continue iterating through B.
         *
         * If no move done through the whole A. - skip round
         */

        int verticesSize = vertices.size();
        for (int i = 0; i < verticesSize; i++) {
            // iterate through all of my vertices
            Vertex currentVertex = vertices.get(i);
            if(isVertexMine(currentVertex)) {

                //fetch adjacent ones
                ArrayList<Vertex> adjacentVertices = getAdjacentVertices(currentVertex, vertices);

                int adjacentVerticesSize = adjacentVertices.size();
                for (int j = 0; j <adjacentVerticesSize; j++) {
                        // iterate through them
                        Vertex currentVertex2 = adjacentVertices.get(j);

                        if(!isVertexMine(currentVertex2)) {
                            // if the adjacent vertex is not mine, decide
                            // "to be (fighting) or not to be (fighting),
                            // that is the question"
                            FuzzyDecision decision = determineDecision(
                                    currentVertex, currentVertex2, adjacentVertices, vertices);

                            switch(decision) {
                                case Fight:
                                    return new AnswerEx(false, currentVertex.getIndex(), currentVertex2.getIndex());
                                case DoNothing:
                                    break;
                            }
                        }
                } // end of adjacent vertices iteration
            }
        }

        // no moves made - skip round
        return new AnswerEx(true, 0, 0);

    }

    private FuzzyDecision determineDecision(Vertex mineVertex,
                                            Vertex enemysVertex,
                                            ArrayList<Vertex> verticesAdjacentToMine,
                                            ArrayList<Vertex> allVertices) {
        FCLParameters fclParameters = new FCLParameters(mineVertex,
                enemysVertex,
                verticesAdjacentToMine,
                allVertices,
                playerNumber);

        functionBlock.setVariable("mineDicesCountInCurrentVertex", fclParameters.getMineDicesCountInCurrentVertex());
        functionBlock.setVariable("enemysDicesCountInCurrentVertex", fclParameters.getEnemysDicesCountInCurrentVertex());
        functionBlock.setVariable("overallPossesion", fclParameters.getOverallPossesion());
        functionBlock.setVariable("range1Possesion",fclParameters.getRange1Possesion());
        functionBlock.setVariable("range2Possesion",fclParameters.getRange2Possesion());
        functionBlock.setVariable("range3Possesion",fclParameters.getRange3Possesion());
        functionBlock.setVariable("mineOverallDicesCount",fclParameters.getMineOverallDicesCount());
        functionBlock.setVariable("enemysOverallDicesCount",fclParameters.getEnemysOverallDicesCount());

        functionBlock.evaluate();
        // is the line below really needed?
        functionBlock.getVariable("decision").defuzzify();

        Variable decision = functionBlock.getVariable("decision");

        return decision.getValue() > 5 ? FuzzyDecision.Fight : FuzzyDecision.DoNothing;
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

    private boolean isVertexMine(Vertex vertex) {
        return vertex.getPlayer() == getPlayerNumber();
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    @Override
    public void setPlayerNumber(int number) {
        this.playerNumber = number;
    }

    private enum FuzzyDecision {
        Fight,
        DoNothing
    }
}
