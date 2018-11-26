package search;


public class Node<S> implements Comparable<Node<S>> {
	public Node(S state) {
		this.state = state;
	}
	
	public S state;
	public double pathCost = Double.POSITIVE_INFINITY;
	public Node<S> parent = null;  // parent node, or null if this is the start node
	public int action = 0;  // the action we took to get here from the parent
	
	@Override
	public int compareTo(Node<S> node) {
		return ((Double) pathCost).compareTo(node.pathCost);
	}
	
	@Override 
	public int hashCode(){
		return state.hashCode();
		
	}
}
