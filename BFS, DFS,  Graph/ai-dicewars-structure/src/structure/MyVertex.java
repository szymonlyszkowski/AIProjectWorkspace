package structure;

import ai.dicewars.common.Vertex;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by szymonlyszkowski on 10/16/14.
 */
public class MyVertex implements Vertex {

  private int id;
  private int player;
  private int dices;
  private List<Integer> neighbours;
  private MyVertex parent;
  private List <MyVertex> children;
  private boolean isVisited;

  public MyVertex (int id){
    this.id = id;
    this.children = new ArrayList<MyVertex>();
  }
  public MyVertex(int id,List<Integer> neighbours ){
    this.id = id;
    this.player = 0;
    this.dices = 0;
    this.neighbours = neighbours;
    this.children = new ArrayList<MyVertex>();
    this.isVisited = false;

  }

  public MyVertex(int id, int player, int dices, List<Integer> neighbours) {
    this.id = id;
    this.player = player;
    this.dices = dices;
    this.neighbours = neighbours;
    this.children = new ArrayList<MyVertex>();
    this.isVisited = false;
  }

  public void setAsVisited(){
    this.isVisited = true;
  }
  public void addChild(MyVertex neighbour)
  {
    this.getChildren().add(neighbour);
    neighbour.setParent(this);
  }

  @Override

  public int getId() {
    return this.id;
  }

  @Override
  public int getPlayer() {
    return this.player;
  }

  @Override
  public int getNumberOfDices() {
    return this.dices;
  }

  @Override
  public List<Integer> getNeighbours() {
    return this.neighbours;
  }

  public MyVertex getParent() {
    return parent;
  }

  public void setParent(MyVertex parent) {
    this.parent = parent;
  }

  public List<MyVertex> getChildren() {
    return children;
  }

  public boolean isVisited() {
    return isVisited;
  }
}
