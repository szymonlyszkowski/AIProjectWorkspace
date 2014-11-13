package ai.dicewars.common;
import java.util.List;

public interface VertexBase {
	int getIndex();
	int getPlayer();
	int getNrOfDices();
	List<Integer> getNeighbours();
}
