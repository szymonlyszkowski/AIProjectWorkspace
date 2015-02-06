package ai.dicewars.fuzzy;

import ai.dicewars.common.Agent;
import ai.dicewars.common.Answer;
import ai.dicewars.common.AnswerEx;
import com.example.main.Vertex;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krzysiek on 2014-11-16.
 */
public class FuzzyAgentSzymon implements Agent {
    private FIS fis;
    private FunctionBlock functionBlock;
    private int playerNumber;

    /*
     * A default constructor, should be used in modes 3 and 4 only.
     * Remember to set playerNumber afterwards.
     */
    public FuzzyAgentSzymon() throws IOException {
        // still doesn't change anything, we will set it after constructing
        fis = FIS.load(this.getClass().getResource("fuzzyLogicSzymon.fcl").openStream(), true);
        functionBlock = fis.getFunctionBlock(null);
    }

    ;

    /*
     * A constructor which sets FCL file to "fuzzyPlayer.fcl".
     * In general, it shouldn't be used.
     */
    private FuzzyAgentSzymon(int playerNumber) throws FileNotFoundException {
        this(playerNumber, "fuzzyPlayer.fcl");
    }

    ;

    public FuzzyAgentSzymon(int playerNumber, String filename) throws FileNotFoundException {
        this.playerNumber = playerNumber;
        fis = FIS.load(filename, true);
        if (fis == null) {
            throw new FileNotFoundException("Can't load file: '" + filename + "'");
        }
        functionBlock = fis.getFunctionBlock("diceWarsGame");
    }

    @Override
    public Answer makeMove(ArrayList<Vertex> vertices) {

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
                        FuzzyDecision decision = determineDecision(
                                currentVertex, currentVertex2, vertices);

                        switch (decision) {
                        case Fight:
                            return new AnswerEx(false, currentVertex.getIndex(), currentVertex2.getIndex());
                        case DoNothing:
                            return new AnswerEx(true, 0, 0);
                        }
                    }
                } // end of adjacent vertices iteration
            }
        }

        // no moves made - skip round
        return new AnswerEx(true, 0, 0);

    }

    private FuzzyDecision determineDecision(Vertex mineVertex,
            Vertex enemiesVertex,
            ArrayList<Vertex> allVertices) {
        FCLParameters fclParameters = new FCLParameters(mineVertex,
                enemiesVertex,
                allVertices,
                playerNumber);

        functionBlock.setVariable("dicesAmountInCurrentVertex", fclParameters.getDicesAmountInCurrentVertex());
        functionBlock.setVariable("dicesAmountInVertexToAttack", fclParameters.getDicesAmountInVertexToAttack());
        functionBlock.setVariable("mineOverallDicesPossessionRatio", fclParameters.getMineOverallDicesPossessionRatio());
        functionBlock.setVariable("mineOverallDicesAmount", fclParameters.getMineOverallDicesAmount());
        functionBlock.setVariable("enemiesOverallDicesAmount", fclParameters.getEnemiesOverallDicesAmount());
        functionBlock.setVariable("mineSafeVertexAmount", fclParameters.getMineSafeVertexAmount());
        functionBlock.setVariable("mineOverallVertexAmount", fclParameters.getMineOverallVertexAmount());
        functionBlock.setVariable("mineOverallVertexPossessionRatio", fclParameters.getMineOverallVertexPossessionRatio());

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
