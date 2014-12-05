package com.example.main;

import ai.dicewars.common.Agent;
import ai.dicewars.common.Answer;
import ai.dicewars.common.AnswerEx;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Czyzewscy
 */
public class MyAgent implements Agent{

    private Graph graph;
    private ArrayList<Vertex> vertices;
    private int playerNumber;
    private static int doNothingRounded = 0;


    public MyAgent (int n){
        this.playerNumber = n;
        this.graph = graph;
        this.vertices = new ArrayList<Vertex>();


    }

    @Override
    public Answer makeMove(ArrayList<Vertex> vertices) {
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
