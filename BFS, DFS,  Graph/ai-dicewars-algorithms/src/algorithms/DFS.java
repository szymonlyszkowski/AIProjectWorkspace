package algorithms;

import structure.MyVertex;

/**
 * Created by szymonlyszkowski on 10/16/14.
 */
public class DFS {

  public void dfs(MyVertex root)
  {
    System.out.print(root.getId() + "\t");
    root.setAsVisited();

    //for every child
    for(MyVertex v: root.getChildren())
    {
      //if childs state is not visited then recurse
      if(v.isVisited() == false)
      {
        //System.out.print(v.getId() + "\t");
        dfs(v);
      }
    }

  }


  public static void main(String [] args){


    new DFS().dfs(new Graph().createGraph().get(0));
  }

}
