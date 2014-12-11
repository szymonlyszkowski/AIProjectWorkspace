package ai.dicewars.clips;

import ai.dicewars.common.Agent;
import ai.dicewars.common.Answer;
import ai.dicewars.common.AnswerEx;
import ai.dicewars.fuzzy.FCLParameters;
import com.example.main.Vertex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CLIPSJNI.*;

/**
 * Created by Łukasz Wieczorek on 2014-12-07.
 */
public class CLIPSAgent implements Agent {
    private int playerNumber;
    private String agentFilePath;
    private Environment clips;

//    public CLIPSAgent(int playerNumber) throws FileNotFoundException {
//        this(playerNumber, "CLIPSTemplate.clp");
//    };

    public CLIPSAgent(int playerNumber, String filename) {
        this.playerNumber = playerNumber;
        this.agentFilePath = filename;

        clips = new Environment(); // Create the environment
        clips.load(agentFilePath); // Load your file
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
                if (currentVertex.getNrOfDices() < 2) {
                    continue;
                }
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
                        CLIPSDecision decision = determineDecision(
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

    private CLIPSDecision determineDecision(Vertex mineVertex,
                                            Vertex enemysVertex,
                                            ArrayList<Vertex> verticesAdjacentToMine,
                                            ArrayList<Vertex> allVertices) {
        FCLParameters fclParameters = new FCLParameters(mineVertex,
                enemysVertex,
                verticesAdjacentToMine,
                allVertices,
                playerNumber);
        Integer mineDicesCountInCurrentVertex = fclParameters.getMineDicesCountInCurrentVertex();
        Integer enemysDicesCountInCurrentVertex = fclParameters.getEnemysDicesCountInCurrentVertex();
        Double overallPossesion = fclParameters.getOverallPossesion();
        Double range1Possesion = fclParameters.getRange1Possesion();
        Double range2Possesion = fclParameters.getRange2Possesion();
        Double range3Possesion = fclParameters.getRange3Possesion();
        Integer mineOverallDicesCount = fclParameters.getMineOverallDicesCount();
        Integer enemysOverallDicesCount = fclParameters.getEnemysOverallDicesCount();

        clips.reset(); // Reset the values

        // Assert all facts about the current world state
        clips.assertString("(game_situation (mineDicesCountInCurrentVertex " + Integer.toString(mineDicesCountInCurrentVertex) + "))");
        clips.assertString("(game_situation (enemysDicesCountInCurrentVertex " + Integer.toString(enemysDicesCountInCurrentVertex) + "))");
        clips.assertString("(game_situation (overallPossesion " + Double.toString(overallPossesion) + "))");
        clips.assertString("(game_situation (range1Possesion " + Double.toString(range1Possesion) + "))");
        clips.assertString("(game_situation (range2Possesion " + Double.toString(range2Possesion) + "))");
        clips.assertString("(game_situation (range3Possesion " + Double.toString(range3Possesion) + "))");
        clips.assertString("(game_situation (mineOverallDicesCount " + Integer.toString(mineOverallDicesCount) + "))");
        clips.assertString("(game_situation (enemysOverallDicesCount " + Integer.toString(enemysOverallDicesCount) + "))");

        clips.run(); // Runs the agent
        String chosenAction = "?*decision*";

        int decision = 0;
        try {
            decision = clips.eval(chosenAction).intValue();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return decision > 5 ? CLIPSDecision.Fight : CLIPSDecision.DoNothing;
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

    @Override
    public void setPlayerNumber(int number) {
        this.playerNumber = number;
    }

    private boolean isVertexMine(Vertex vertex) {
        return vertex.getPlayer() == getPlayerNumber();
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    private enum CLIPSDecision {
        Fight,
        DoNothing
    }
}
