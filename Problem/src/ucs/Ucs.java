package ucs;

import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Ucs {
	public static <S> Node<S> search(Problem<S> prob){
		// initialize data structures
		PriorityQueue<Node<S>> frontier = new PriorityQueue<Node<S>>();
		HashSet<Node<S>> exploredSet = new HashSet<Node<S>>();
		HashMap<S, Node<S>> frontierStates = new HashMap<S, Node<S>>();
		
		// add first node to frontier
		var initialState = prob.initialState();
		var initialNode = new Node<S>(initialState);
		frontier.add(initialNode);
		frontierStates.put(initialState, initialNode);
		
		// explore nodes in the frontier
		Node<S> node;
		while((node =  frontier.poll()) != null) {
			if(prob.isGoal(node.state)) {
				return node;
			}
			
			if(exploredSet.contains(node))
				continue;
			
			List<Integer> actions = prob.actions(node.state);
			S state = node.state;
			
			for(int action: actions) {
				S result = prob.result(state, action);
				// the Node we are about to explore
				Node<S> relaxedNode = frontierStates.get(result);
				
				if (exploredSet.contains(relaxedNode))
					continue;
				
				if (relaxedNode == null) {
					relaxedNode = new Node<S>(result);
				}
				
				int cost = node.pathCost + prob.cost(state, action);
				if(cost < relaxedNode.pathCost) {
					relaxedNode.pathCost = cost;
					relaxedNode.parent = node;
					relaxedNode.action = action;
					
					frontier.add(relaxedNode);
					frontierStates.put(result, relaxedNode);
				}
				
				
				exploredSet.add(node);
			}
		}
		
		
		return null;
	}
}
