/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.dicewars.agent;

import ai.dicewars.common.Agent;
import ai.dicewars.common.Answer;
import com.example.main.Graph;
import com.example.main.Vertex;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Czyzewscy
 */
public class AgentRandom implements Agent{

    private Graph graph;
    private ArrayList<Vertex> vertices;
    private int playerNumber;
    

    
    
    public AgentRandom (int n){
        this.playerNumber = n;
        this.graph = graph;
        this.vertices = new ArrayList<Vertex>();
        
       
    }
    
    @Override
    public Answer makeMove(ArrayList<Vertex> list) {
     
        return new Answer() {

            
            @Override
            public boolean isEmptyMove() {
                
                return true;
            }

            @Override
            public int getTo() {
                
                return 0;
            }

            @Override
            public int getFrom() {
                return 0;
            }
        };
    }

    @Override
    public void setPlayerNumber(int i) {
        this.playerNumber = i;
        
    }


    
}
