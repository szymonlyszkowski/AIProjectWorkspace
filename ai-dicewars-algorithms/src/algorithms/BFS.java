package algorithms;

import structure.MyVertex;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by szymonlyszkowski on 10/16/14.
 */
public class BFS {

  private Queue<MyVertex> queue;

  public BFS() {
    this.queue = new LinkedBlockingQueue<MyVertex>();
  }


  public void bfs(MyVertex root)
  {
    System.out.println("ROOT" + root.getId());
    root.setAsVisited();
    this.queue.add(root);
    //for every child

    while(!queue.isEmpty()){
      MyVertex vertex = queue.poll();
      System.out.println("\n" + "VERTEX POLLED" + vertex.getId());
      for(MyVertex v: vertex.getChildren())
      {
         if(v.isVisited() == false){
           v.setAsVisited();
           queue.add(v);
           System.out.print("Child ID:" + v.getId() + "\t");
         }

      }


    }

  }

  public static void main(String [] args){


    new BFS().bfs(new Graph().createGraph().get(0));
  }

}
