package ai.dicewars.agent;

import ai.dicewars.common.Agent;
import ai.dicewars.common.Answer;
import ai.dicewars.common.AnswerEx;
import com.example.main.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Szymon.Lyszkowski@tomtom.com on 15.01.15.
 */
public class BfsAgentSzymon implements Agent {

    private int playerNumber;

    //To keep track of the nodes we check so we know if it connects to the source.
    //If a node was checked, then there is a path to the node from a source
    private boolean[] checked;

    public ArrayList<Vertex> init(ArrayList<Vertex> vertices, Vertex rootVertex) {
        //Initialize arrays with the size of the graph
        checked = new boolean[vertices.size()];
        return bfs(vertices, rootVertex, playerNumber);
    }

    public BfsAgentSzymon() {
    }

    private ArrayList<Vertex> bfs(ArrayList<Vertex> vertexes, Vertex root, int playerNumber) {
        ArrayList<Vertex> qualifiedToBeAttacked = new ArrayList<Vertex>();
        Queue<Vertex> queue = new LinkedBlockingQueue<Vertex>();
        System.out.println("ROOT" + root.getIndex());

        queue.add(root);
        //for every child

        while (!queue.isEmpty()) {
            Vertex currentVertex = queue.poll();
            System.out.println("\n" + "VERTEX POLLED" + currentVertex.getIndex());
            ArrayList<Vertex> adjacentVertices = getAdjacentVertices(currentVertex, vertexes);
            for (Vertex child : adjacentVertices) {

                if (!(child.getPlayer() == playerNumber) && child.getNrOfDices() < 3 && !checked[child.getIndex()]) {

                    //So we don't check this node again
                    checked[child.getIndex()] = true;
                    queue.add(child);
                    qualifiedToBeAttacked.add(child);
                    System.out.print("Child ID:" + child.getIndex() + "\t");
                }

            }

        }
        return qualifiedToBeAttacked;

    }

    @Override public Answer makeMove(ArrayList<Vertex> vertices) {

        int verticesSize = vertices.size();
        for (int i = 0; i < verticesSize; i++) {
            // iterate through all of my vertices
            Vertex currentVertex = vertices.get(i);
            if (isVertexMine(currentVertex)) {
                ArrayList<Vertex> qualifiedToAttack = init(vertices, currentVertex);

                if (qualifiedToAttack.size() > 0) {

                    Vertex smallestAmountDices = qualifiedToAttack.get(0);

                    for (Vertex v : qualifiedToAttack) {
                        if (smallestAmountDices.getNrOfDices() > v.getNrOfDices()) {
                            smallestAmountDices = v;
                        }
                    }

                    ArrayList<Vertex> adjacentVertices = getAdjacentVertices(smallestAmountDices, vertices);
                    for (Vertex v : adjacentVertices) {
                        if (v.getPlayer() == playerNumber) {
                            return new AnswerEx(false, v.getIndex(), smallestAmountDices.getIndex());

                        }
                    }

                } else {
                    return new AnswerEx(true, 0, 0);
                }
            }

        }
        // no moves made - skip round
        return new AnswerEx(true, 0, 0);
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

    private enum BFSDecision {
        Fight,
        DoNothing
    }
}
