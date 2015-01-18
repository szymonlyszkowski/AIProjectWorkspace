package ai.dicewars.clips;

import ai.dicewars.common.Agent;
import ai.dicewars.common.Answer;
import ai.dicewars.common.AnswerEx;
import ai.dicewars.fuzzy.FCLParameters;
import com.example.main.Vertex;
import net.sf.clipsrules.jni.CLIPSError;
import net.sf.clipsrules.jni.Environment;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    /*
     * A default constructor, should be used in modes 3 and 4 only.
     * Remember to set playerNumber afterwards.
     */
    public CLIPSAgent() throws IOException, CLIPSError {
        //create temp helper file
        Random random = new Random();
        Path path = FileSystems.getDefault().getPath("temp" + random.nextInt() + ".tmp");

        Files.copy(this.getClass().getResourceAsStream("clipsAgent.clp"), path);

        clips = new Environment(); // Create the environment
        clips.load(path.toAbsolutePath().toString()); // Load your file

        Files.delete(path);
    }

    public CLIPSAgent(int playerNumber, String filename) throws CLIPSError {
        this.playerNumber = playerNumber;
        this.agentFilePath = filename;

        clips = new Environment(); // Create the environment
        clips.load(agentFilePath); // Load your file
    }

    @Override
    public Answer makeMove(ArrayList<Vertex> vertices) throws CLIPSError {
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
            if (isVertexMine(currentVertex)) {

                if (currentVertex.getNrOfDices() < 2) {
                    continue;
                }
                //fetch adjacent ones
                ArrayList<Vertex> adjacentVertices = getAdjacentVertices(currentVertex, vertices);

                int adjacentVerticesSize = adjacentVertices.size();
                for (int j = 0; j < adjacentVerticesSize; j++) {
                    // iterate through them
                    Vertex currentVertex2 = adjacentVertices.get(j);

                    if (!isVertexMine(currentVertex2)) {
                        // if the adjacent vertex is not mine, decide
                        // "to be (fighting) or not to be (fighting),
                        // that is the question"
                        CLIPSDecision decision = determineDecision(
                                currentVertex, currentVertex2, adjacentVertices, vertices);

                        switch (decision) {
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
            ArrayList<Vertex> allVertices) throws CLIPSError {
        FCLParameters fclParameters = new FCLParameters(mineVertex,
                enemysVertex,
                verticesAdjacentToMine,
                allVertices,
                playerNumber);
        Integer mineDicesCountInCurrentVertex = fclParameters.getDicesAmountInCurrentVertex();
        Integer enemysDicesCountInCurrentVertex = fclParameters.getDicesAmountInVertexToAttack();
        Double overallPossesion = fclParameters.getMineOverallDicesPossessionRatio();
        Double range1Possesion = fclParameters.getRange1Possession();
        Double range2Possesion = fclParameters.getRange2Possession();
        Double range3Possesion = fclParameters.getRange3Possession();
        Integer mineOverallDicesCount = fclParameters.getMineOverallDicesAmount();
        Integer enemysOverallDicesCount = fclParameters.getEnemiesOverallDicesAmount();

        clips.reset(); // Reset the values

        //Assert all facts about the current world state
        clips.assertString("(game_situation (mineDicesCountInCurrentVertex " + Integer.toString(mineDicesCountInCurrentVertex) + "))");
        clips.assertString("(game_situation (enemysDicesCountInCurrentVertex " + Integer.toString(enemysDicesCountInCurrentVertex) + "))");
        clips.assertString("(game_situation (overallPossesion " + Double.toString(overallPossesion) + "))");
        clips.assertString("(game_situation (range1Possesion " + Double.toString(range1Possesion) + "))");
        clips.assertString("(game_situation (range2Possesion " + Double.toString(range2Possesion) + "))");
        clips.assertString("(game_situation (range3Possesion " + Double.toString(range3Possesion) + "))");
        clips.assertString("(game_situation (mineOverallDicesCount " + Integer.toString(mineOverallDicesCount) + "))");
        clips.assertString("(game_situation (enemysOverallDicesCount " + Integer.toString(enemysOverallDicesCount) + "))");
        clips.assertString("(mojazmienna 99)");
        //clips.assertString("(game_situation (mineDicesCountInCurrentVertex " + Integer.toString(mineDicesCountInCurrentVertex) + ") (enemysDicesCountInCurrentVertex " + Integer.toString(enemysDicesCountInCurrentVertex) + ") (overallPossesion " + Double.toString(overallPossesion) + ") (range1Possesion " + Double.toString(range1Possesion) + ") (range2Possesion " + Double.toString(range2Possesion) + ") (range3Possesion " + Double.toString(range3Possesion) + ") (mineOverallDicesCount " + Integer.toString(mineOverallDicesCount) + ") (enemysOverallDicesCount " + Integer.toString(enemysOverallDicesCount) + "))");

        //clips.assertString("(node (idNumber 1) (belongsTo 2) (squares 3) (circles 4) (triangles 5) (unitCreationType 6) (unitCreationSpeed 7) (availableAdjacentNodes "+a+" "+b+" "+c+" "+d+" "+e+") ))");

        clips.run(); // Runs the agent

        String chosenAction = "?*decision*";

        int decision = 0;
        try {
            decision = clips.eval(chosenAction).hashCode();
            //System.out.println(response.intValue());
        } catch (Exception e) {
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
