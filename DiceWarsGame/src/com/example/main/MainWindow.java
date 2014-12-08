package com.example.main;


import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;

public class MainWindow {
    private static GraphCanvas canvas;

    public GraphCanvas getCanvas() {
        return canvas;
    }

    public static void main(String[] args) throws Exception {

        Graph graph;

        JSONConverter converter = new JSONConverter();

        int choice = Integer.parseInt(args[0]);

        switch (choice) {

            //Create the graph
            case 1: {

                graph = new Graph(Integer.parseInt(args[1]), Integer.parseInt(args[2]));

                while (!graph.isSuccessful()) {
                    graph.generateGraph();
                }

                JSONObject jsonGraph = converter.parseGraphToJSON(graph);

                try {
                    FileWriter file = new FileWriter(args[3]);
                    file.write(jsonGraph.toString());
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }

            //Launch the game
            case 2: {


                JFrame frame = new JFrame("GraphCanvas");

                graph = converter.parseJSONToGraph(args[1]);

                //System.out.println("GRAPH CREATED");
                //System.out.println(graph.toString());


                GameState gameState =   new GameState(graph, canvas, Integer.parseInt(args[0]));
                gameState.initGame();

                frame.setSize(new Dimension(640, 480));
                frame.setMinimumSize(new Dimension(640, 480));
                frame.getContentPane().add(new GraphCanvas(graph, Integer.parseInt(args[0])));
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

                break;
            }

            case 3:{
                JFrame frame = new JFrame("GraphCanvas");

                graph = converter.parseJSONToGraph(args[1]);

                //System.out.println("GRAPH CREATED");
                //System.out.println(graph.toString());


                GameState gameState =   new GameState(graph, canvas,args[2],args[3]);
                gameState.initGame();

                frame.setSize(new Dimension(640, 480));
                frame.setMinimumSize(new Dimension(640, 480));
                frame.getContentPane().add(new GraphCanvas(graph, Integer.parseInt(args[0])));
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

                break;


            }

            case 4:{
                graph = converter.parseJSONToGraph(args[1]);

                GameState gameState =   new GameState(graph, canvas,args[2],args[3]);
                gameState.initGame();

                GraphCanvas graphCanvas = new GraphCanvas(graph, Integer.parseInt(args[0]));

                graphCanvas.getGameState().gameLoop();

                break;

            }
        }
    }
}