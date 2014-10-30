package com.example.main;


public class MainWindow {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GraphCanvas");

        Graph graph = new Graph(12, 4);

        while(!graph.isSuccessful()){
            graph.generateGraph();
        }
        System.out.println("GRAPH CREATED");
        System.out.println(graph.toString());

        GameState gameState = new GameState(graph);
        gameState.initGame();

        frame.setSize(new Dimension(640, 480));
        frame.setMinimumSize(new Dimension(640,480));
        frame.getContentPane().add(new GraphCanvas(graph));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
