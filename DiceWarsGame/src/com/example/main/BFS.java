package com.example.main;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by szymonlyszkowski on 10/16/14.
 */
public class BFS {

    private Queue<Vertex> queue;

    public BFS() {
        this.queue = new LinkedBlockingQueue<Vertex>();
    }

    public void bfs(Graph graph, Vertex root, int playerNumber) {
        System.out.println("ROOT" + root.getIndex());

        this.queue.add(root);
        //for every child

        while (!queue.isEmpty()) {
            Vertex vertex = queue.poll();
            System.out.println("\n" + "VERTEX POLLED" + vertex.getIndex());
            ArrayList<Integer> adjacencyList = vertex.getAdjacencyList();
            for (Integer vertexIndex : adjacencyList) {
                Vertex vertexConsidered = graph.getGraphStructure().get(vertexIndex);

                if (!(vertexConsidered.getPlayer() == playerNumber) && vertexConsidered.getNrOfDices() > 3) {
                    queue.add(vertexConsidered);
                    System.out.print("Child ID:" + vertexConsidered.getIndex() + "\t");
                }

            }

        }

    }

    public void searchBfs(Graph graph, Vertex root) {

        int level = 0;
        Vertex parent = null;
        this.queue.add(root); //frontier  = [s]
        while (queue.isEmpty()) {
            Vertex vertex = queue.poll(); //next = []
        }

    }

    public static void main(String[] args) {

        Graph graph = new Graph(10, 3);

        new BFS().bfs(graph, graph.getGraphStructure().get(0), 1);
    }

}
