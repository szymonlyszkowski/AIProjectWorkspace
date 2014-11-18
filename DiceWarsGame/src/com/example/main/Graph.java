package com.example.main;

import java.util.ArrayList;
import java.util.Random;

public class Graph {

    private int nrOfVertices; //nr of all vertices
    private int maxNrOfEdges; //maximum nr of edges that can come out of a given vertex; minimum value is 1
    private ArrayList<Vertex> graphStructure; //list of vertices
    private boolean graphSuccessful;


    public Graph(int nrOfVertices, int maxNrOfEdges) {
        this.nrOfVertices = nrOfVertices;
        this.maxNrOfEdges = maxNrOfEdges;
        graphStructure = new ArrayList<Vertex>();
        this.graphSuccessful = false;
    }

    public Graph() {
        this.nrOfVertices = 0;
        this.maxNrOfEdges = 0;
        graphStructure = new ArrayList<Vertex>();
        this.graphSuccessful = false;
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < graphStructure.size(); i++) {
            ret.append("Vertex " + i + ": " + graphStructure.get(i).toString() + "\n");
        }

        return ret.toString();
    }

    public void addEdge(int v, int w) {
        (graphStructure.get(v)).addAdjacentVertex(w);
        (graphStructure.get(w)).addAdjacentVertex(v);
    }

    public void connectChildren(Vertex vertex, int start) {
        // start: 0 if current vertex isn't connected to any vertex,
        // so only in case of the first vertex
        //
        // >0 an offset indicating where children start
        // (depending on previously added connections)

        ArrayList<Vertex> children = new ArrayList<Vertex>();


        for (int i = start; i < vertex.getAdjacencyList().size(); i++) {
            children.add(graphStructure.get(vertex.getAdjacencyList().get(i)));
        }

        for (int j = 0; j < children.size() - 1; j++) {
            if (children.get(j).getFreeEdges() > 0 && children.get(j + 1).getFreeEdges() > 0) {
                addEdge(children.get(j).getIndex(), children.get(j + 1).getIndex());
            }
        }

    }

    //if graph is successfully created (all vertices used) return true; otherwise in the main function use this function until it returns true
    public void generateGraph() {

        Random rand = new Random();

        int nrOfConnections = 0;

        for (int i = 0; i < this.nrOfVertices; i++) {

            boolean repeat = true;


            while (repeat) {
                int maxEdges = (int) rand.nextInt(maxNrOfEdges) + 1; //set maximum number of edges for current vertex
                nrOfConnections += maxEdges;

                //decreasing the chance to be too few connections
                if (((double) nrOfConnections / 2) > (double) i) {
                    this.graphStructure.add(new Vertex(i, maxEdges));
                    repeat = false;
                } else {
                    repeat = true;
                    nrOfConnections -= maxEdges;

                }
            }
        }

        int nextToAdd = 1;


        for (int j = nextToAdd; j <= graphStructure.get(0).getMaxNrOfEdges(); j++, nextToAdd++) {
            if ((j) < nrOfVertices)
                addEdge(0, j);
            graphStructure.get(0).setHasChildren(true);
        }

        connectChildren(graphStructure.get(0), 0);


        for (int i = 1; i < this.nrOfVertices; i++) {

            System.out.println("Adding vertex " + i);

            Vertex currentVertex = graphStructure.get(i);

            int start = 0;

            if (currentVertex.hasConnections()) {

                start = 1;

                Vertex previous = graphStructure.get(i - 1);

                //if possible, connect to the last child of the previous(i-1)  vertex
                if (currentVertex.getFreeEdges() > 0 && previous.hasChildren()) {

                    Vertex lastChildOfPrevious = graphStructure.get(previous.getAdjacencyList().get(previous.getNrOfAdjacentEdges() - 1));


                    if (currentVertex.getIndex() != lastChildOfPrevious.getIndex()
                            &&
                            currentVertex.getIndex() != (lastChildOfPrevious.getAdjacencyList()).get(lastChildOfPrevious.getNrOfAdjacentEdges() - 1)) {

                        if (lastChildOfPrevious.getFreeEdges() > 0) {
                            addEdge(currentVertex.getIndex(), lastChildOfPrevious.getIndex());
                            start = 2;
                        }
                    }
                }


                start = currentVertex.getNrOfAdjacentEdges();


                while (currentVertex.getFreeEdges() > 0) {
                    System.out.println(".");
                    if (nextToAdd < nrOfVertices) {

                        addEdge(currentVertex.getIndex(), nextToAdd);
                        currentVertex.setHasChildren(true);
                        nextToAdd += 1;
                    } else break;
                }

                connectChildren(currentVertex, start);

            } else {/* not all vertices used */
                graphSuccessful = false;

                graphStructure.clear();

                System.out.println("GRAPH NOT SUCCESSFULL");
                break;
            }

            graphSuccessful = true;

            System.out.println("Vertex added...");
        }

    }

    public boolean isSuccessful() {
        return graphSuccessful;
    }

    public int getNrOfVertices() {
        return nrOfVertices;
    }

    public void setNrOfVertices(int nrOfVertices) {
        this.nrOfVertices = nrOfVertices;
    }

    public int getMaxNrOfEdges() {
        return maxNrOfEdges;
    }

    public void setMaxNrOfEdges(int maxNrOfEdges) {
        this.maxNrOfEdges = maxNrOfEdges;
    }

    public ArrayList<Vertex> getGraphStructure() {
        return graphStructure;
    }

    public void setGraphStructure(ArrayList<Vertex> graphStructure) {
        this.graphStructure = graphStructure;
    }
}
