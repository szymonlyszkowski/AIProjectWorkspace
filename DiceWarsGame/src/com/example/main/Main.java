package com.example.main;

import java.util.Random;


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
		//System.out.println("GRAPH CREATED");
		//System.out.println(graph.toString());
		int a= throwTheDice(2);
		System.out.println(a);
	}
	
	static int throwTheDice(int dices) {
		Random rand = new Random();
		int result = 0;
		for (int i=0; i<dices; i++) {
			int randomNum = rand.nextInt(6) + 1;
			result += randomNum;
		}
		return result;
	}
}
