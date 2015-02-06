package ai.dicewars.agent;

import ai.dicewars.common.Agent;
import ai.dicewars.common.Answer;
import ai.dicewars.common.AnswerEx;
import com.example.main.Graph;
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

    public BfsAgentSzymon() {
    }

    public ArrayList<Vertex> bfs(Graph graph, Vertex root, int playerNumber) {
        ArrayList<Vertex> qualifiedToBeAttacked = new ArrayList<Vertex>();
        Queue<Vertex> queue = new LinkedBlockingQueue<Vertex>();
        System.out.println("ROOT" + root.getIndex());

        queue.add(root);
        //for every child

        while (!queue.isEmpty()) {
            Vertex vertex = queue.poll();
            System.out.println("\n" + "VERTEX POLLED" + vertex.getIndex());
            ArrayList<Integer> adjacencyList = vertex.getAdjacencyList();
            for (Integer vertexIndex : adjacencyList) {
                Vertex vertexConsidered = graph.getGraphStructure().get(vertexIndex);

                if (!(vertexConsidered.getPlayer() == playerNumber) && vertexConsidered.getNrOfDices() < 3) {
                    queue.add(vertexConsidered);
                    qualifiedToBeAttacked.add(vertexConsidered);
                    System.out.print("Child ID:" + vertexConsidered.getIndex() + "\t");
                }

            }

        }
        return qualifiedToBeAttacked;

    }

    public ArrayList<Vertex> bfs(ArrayList<Vertex> vertexes, Vertex root, int playerNumber) {
        ArrayList<Vertex> qualifiedToBeAttacked = new ArrayList<Vertex>();
        Queue<Vertex> queue = new LinkedBlockingQueue<Vertex>();
        System.out.println("ROOT" + root.getIndex());

        queue.add(root);
        //for every child

        while (!queue.isEmpty()) {
            Vertex vertex = queue.poll();
            System.out.println("\n" + "VERTEX POLLED" + vertex.getIndex());
            ArrayList<Integer> adjacencyList = vertex.getAdjacencyList();
            for (Integer vertexIndex : adjacencyList) {
                Vertex vertexConsidered = vertexes.get(vertexIndex);

                if (!(vertexConsidered.getPlayer() == playerNumber) && vertexConsidered.getNrOfDices() < 3) {
                    queue.add(vertexConsidered);
                    qualifiedToBeAttacked.add(vertexConsidered);
                    System.out.print("Child ID:" + vertexConsidered.getIndex() + "\t");
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
                ArrayList<Vertex> qualifiedToAttack = bfs(vertices, currentVertex, playerNumber);

                if (qualifiedToAttack.size() > 0) {

                    Vertex smallestAmountDices = qualifiedToAttack.get(0);

                    for (Vertex v : qualifiedToAttack) {
                        if (smallestAmountDices.getNrOfDices() > v.getNrOfDices()) {
                            smallestAmountDices = v;
                        }
                    }
                    return new AnswerEx(false, currentVertex.getIndex(), smallestAmountDices.getIndex());
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
