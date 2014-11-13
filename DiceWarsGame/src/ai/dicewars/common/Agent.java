package ai.dicewars.common;
import com.example.main.Vertex;
import java.util.ArrayList;

public interface Agent {
	Answer makeMove(ArrayList<Vertex> vertices);
	void setPlayerNumber(int number);
}
