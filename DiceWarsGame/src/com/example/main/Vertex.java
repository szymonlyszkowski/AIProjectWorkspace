package com.example.main;
import com.example.main.UIVertex;

import java.util.ArrayList;
import java.util.Random;


public class Vertex {
	
	
	private int index;
	private int player;// 0 - no player, 1 - first player, 2 - second player
	private int nrOfDices;
	private int maxNrOfEdges;
	private ArrayList<Integer> adjacencyList;// other vertices connected to this one
	private boolean hasChildren;
	private UIVertex uiVertex;
	
	
	
	public Vertex(int index, int maxNrOfEdges){
		this.index = index;
		this.player = 0;
		this.nrOfDices = 0;
		this.maxNrOfEdges = maxNrOfEdges;
		this.adjacencyList = new ArrayList<Integer>();
		this.hasChildren = false;
		 this.player = new Random().nextInt(2);

	}
	

	

	public String toString(){
		StringBuilder ret = new StringBuilder();
		
		ret.append("Player: " + player + "\n");
		ret.append("Nr of dices: " + nrOfDices + "\n");
		ret.append("Max nr of edges: " + maxNrOfEdges + "\n");
		ret.append("Current nr of edges: " + adjacencyList.size() + "\n");
		ret.append("Adjacency list: \n");
		for(int i=0; i<adjacencyList.size(); i++){
			ret.append(Integer.toString(adjacencyList.get(i)) + " ");
		}
		ret.append("\n");
		
		return ret.toString();
	}
	
	
	public int getIndex(){
		return index;
	}
	
	
	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}
	
	
	public int getNrOfDices() {
		return nrOfDices;
	}

	public void setNrOfDices(int nrOfDices) {
		this.nrOfDices = nrOfDices;
	}
	
	
	public int getMaxNrOfEdges(){
		return this.maxNrOfEdges;
	}
	
	
	public int getNrOfAdjacentEdges(){
		return this.adjacencyList.size();
	}


	public ArrayList<Integer> getAdjacencyList() {
		return adjacencyList;
	}

	public void setAdjacencyList(ArrayList<Integer> adjacencyList) {
		this.adjacencyList = adjacencyList;
	}

	
	public boolean hasConnections(){
		if(this.adjacencyList.size() > 0)
			return true;
		else
			return false;
	}
	
		
	public void addAdjacentVertex(int v){
		adjacencyList.add(v);
	}
	
	
	// how many free edges are left (how many vertices we can add to the current vertex)
	public int getFreeEdges(){
		return (maxNrOfEdges - this.adjacencyList.size());
	}
	
	
	public boolean hasChildren(){
		return hasChildren;
	}
	
	
	public void setHasChildren(boolean hasChildren){
		this.hasChildren = hasChildren;
	}
	

	
    public UIVertex getUiVertex() {
        return uiVertex;
    }

    public void setUiVertex(UIVertex uiVertex) {
        this.uiVertex = uiVertex;
    }
}
