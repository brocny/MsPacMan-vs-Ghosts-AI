package search;

import java.util.List;

public interface Problem<S> {
	S initialState();
	List<Integer> actions(S state);
	S result(S state, int action);
	boolean isGoal(S state);
	double cost(S state, int action);  
}
