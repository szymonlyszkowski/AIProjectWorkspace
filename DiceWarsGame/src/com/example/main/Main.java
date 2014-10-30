package com.example.main;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

		Graph graph = new Graph(12,4);
		
		while(!graph.isSuccessful()){
			graph.generateGraph();
		}
		System.out.println("GRAPH CREATED");
		System.out.println(graph.toString());
	}
}
