package com.example.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Filip on 2014-11-17.
 */
public class JSONConverter {

    public JSONObject parseGraphToJSON(Graph graph) {

        JSONObject output = new JSONObject();

        output.put("nrOfVertices", graph.getNrOfVertices());
        output.put("maxNrOfEdges", graph.getMaxNrOfEdges());

        JSONArray vertices = new JSONArray();

        for (int i = 0; i < graph.getNrOfVertices(); i++) {
            JSONObject vertex = new JSONObject();
            JSONArray adjacentVertices = new JSONArray();
            Vertex temp = graph.getGraphStructure().get(i);
            ArrayList<Integer> adjacency = temp.getAdjacencyList();

            vertex.put("index", temp.getIndex());
            vertex.put("player", temp.getPlayer());
            vertex.put("nrOfDices", temp.getNrOfDices());

            for (int j = 0; j < temp.getNrOfAdjacentEdges(); j++) {
                adjacentVertices.add(adjacency.get(j));
            }

            vertex.put("adjacencyList", adjacentVertices);

            vertices.add(vertex);
        }

        output.put("vertices", vertices);

        return output;
    }

    public Graph parseJSONToGraph(String path){

        Graph graph = new Graph();

        FileReader reader;
        try {
            reader = new FileReader(path);

            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            graph.setMaxNrOfEdges(Integer.parseInt(jsonObject.get("maxNrOfEdges").toString()));
            graph.setNrOfVertices(Integer.parseInt(jsonObject.get("nrOfVertices").toString()));

            ArrayList<Vertex> graphStructure = new ArrayList<Vertex>();

            JSONArray verticesArray = (JSONArray)jsonObject.get("vertices");

            for (int i = 0; i < verticesArray.size(); i++) {
                JSONObject temp = (JSONObject)verticesArray.get(i);
                int index = Integer.parseInt(temp.get("index").toString());

                Vertex vertex = new Vertex(index);

                vertex.setNrOfDices(Integer.parseInt(temp.get("nrOfDices").toString()));
                vertex.setPlayer(Integer.parseInt(temp.get("player").toString()));

                ArrayList<Integer> adjacencyList = new ArrayList<Integer>();

                JSONArray jsonAdjList = (JSONArray)temp.get("adjacencyList");

                for (int j = 0; j < jsonAdjList.size(); j++) {
                    adjacencyList.add(Integer.parseInt(jsonAdjList.get(j).toString()));
                }

                vertex.setAdjacencyList(adjacencyList);

                graphStructure.add(vertex);
            }

            graph.setGraphStructure(graphStructure);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return graph;
    }
}
