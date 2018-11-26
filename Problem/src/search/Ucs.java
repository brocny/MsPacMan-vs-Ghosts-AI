package search;

import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Ucs {
	public static <S> Node<S> search(Problem<S> prob){
		// initialize data structures
		PriorityQueue<Node<S>> frontier = new PriorityQueue<Node<S>>();
		HashSet<Node<S>> exploredSet = new HashSet<Node<S>>();
		HashMap<S, Node<S>> frontierNodes = new HashMap<S, Node<S>>();
		
		// add first node to frontier
		var initialState = prob.initialState();
		var initialNode = new Node<S>(initialState);
		initialNode.pathCost = 0;
		frontier.add(initialNode);
		frontierNodes.put(initialState, initialNode);
		
		// explore nodes in the frontier
		while (!frontier.isEmpty()) {
			Node<S> node = frontier.poll();
			if (prob.isGoal(node.state)) {
			//	System.out.println("returning node " + node.state + " with cost " + node.pathCost);
				return node;
			}
			
			if (exploredSet.contains(node))
				continue;
			
			List<Integer> actions = prob.actions(node.state);
			S state = node.state;
			
			for (int action: actions) {
				S targetState = prob.result(state, action);
				
				// the Node we are about to explore
				Node<S> relaxedNode = frontierNodes.get(targetState);
				
				if (exploredSet.contains(relaxedNode))
					continue;
				
				if (relaxedNode == null) {
					relaxedNode = new Node<S>(targetState);
				}
				
				double cost = node.pathCost + prob.cost(state, action);
				if (cost < relaxedNode.pathCost) {
					relaxedNode.pathCost = cost;
					relaxedNode.parent = node;
					relaxedNode.action = action;
					
					frontier.add(relaxedNode);
					frontierNodes.put(targetState, relaxedNode);
				}
			}
			
			exploredSet.add(node);
		}
		
		return null;
	}
}
