package ai.dicewars.common;
import com.example.main.Vertex;
import net.sf.clipsrules.jni.CLIPSError;

import java.util.ArrayList;

public interface Agent {
	Answer makeMove(ArrayList<Vertex> vertices) throws CLIPSError;
	void setPlayerNumber(int number);
}
