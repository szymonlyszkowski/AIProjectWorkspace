package com.example.main;


import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class MainWindow {
    public static void main(String[] args) {
        int gameType;

        Graph graph;// = new Graph();

        JSONConverter conv = new JSONConverter();

        int choice = Integer.parseInt(args[0]);

        switch (choice) {

            //Create the graph
            case 1: {

                graph = new Graph(Integer.parseInt(args[1]), Integer.parseInt(args[2]));

                while (!graph.isSuccessful()) {
                    graph.generateGraph();
                }

                JSONObject jsonGraph = conv.parseGraphToJSON(graph);

                try {
                    FileWriter file = new FileWriter(args[3]);
                    file.write(jsonGraph.toString());
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
            case 2: {


                JFrame frame = new JFrame("GraphCanvas");

                graph = conv.parseJSONToGraph(args[1]);

                System.out.println("GRAPH CREATED");
                System.out.println(graph.toString());


                GameState gameState = new GameState(graph);
                gameState.initGame();

                frame.setSize(new Dimension(640, 480));
                frame.setMinimumSize(new Dimension(640, 480));
                frame.getContentPane().add(new GraphCanvas(graph));
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

                //gameType = JOptionPane.showConfirmDialog(null,"Play AI vs AI?","Choose game type", JOptionPane.YES_NO_OPTION);
                // System.out.println("mode " + gameType);
                break;
            }
        }
    }
}