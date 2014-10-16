package algorithms;

import structure.MyVertex;

import java.util.ArrayList;

/**
 * Created by szymonlyszkowski on 10/16/14.
 */
public class Graph {

  private ArrayList<MyVertex> myGraph;

  public Graph(){

    this.myGraph = new ArrayList<MyVertex>();
  }

  public ArrayList<MyVertex> createGraph()
  {
    MyVertex four = new MyVertex (4);
    MyVertex five = new MyVertex(5);
    MyVertex one = new MyVertex(1);
    MyVertex three = new MyVertex(3);
    MyVertex zero = new MyVertex(0);
    MyVertex two = new MyVertex(2);
    MyVertex six = new MyVertex(6);

    one.addChild(four);
    one.addChild(five);
    zero.addChild(one);
    zero.addChild(two);
    zero.addChild(three);
    two.addChild(six);

    this.myGraph.add(zero);
    this.myGraph.add(one);
    this.myGraph.add(two);
    this.myGraph.add(three);
    this.myGraph.add(four);
    this.myGraph.add(five);
    this.myGraph.add(six);


    return this.myGraph;
  }
}
