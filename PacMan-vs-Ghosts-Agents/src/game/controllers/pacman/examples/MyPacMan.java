package game.controllers.pacman.examples;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import game.PacManSimulator;
import game.controllers.ghosts.game.GameGhosts;
import game.controllers.pacman.PacManHijackController;
import game.core.G;
import game.core.Game;
import game.core.GameView;
import search.*;

public final class MyPacMan extends PacManHijackController
{	
	@Override
	public void tick(Game game, long timeDue) {
		Problem<Integer> pacmanProblem = new PacmanProblem(game.copy());
		
		Node<Integer> node = Ucs.search(pacmanProblem);
		
		if(node == null) {
			System.err.println("Search returned null!");
			return;
		}
		
		if(node.parent == null) {
			int[] directions = game.getPossiblePacManDirs(false);		
			pacman.set(directions[G.rnd.nextInt(directions.length)]);
			System.err.println("*** Choosing randomly ***");
			return;
		}
		
		List<Integer> states = new ArrayList<Integer>();
		
		while(node.parent.parent != null) {
			states.add(node.state);
			node = node.parent;
		}
		
		//GameView.addPoints(game, Color.GREEN, states.stream().mapToInt(i -> i).toArray());
		int action = node.action;
		pacman.set(action);
	}

	public static void main(String[] args) {
		// If seed = 0, a random seed is chosen on every run.  Set seed to a positive value
		// for repeatable play.
		int seed = 0;
		
		// The number of lives to start with.
		int lives = 3;
		
		// The number of milliseconds between frames; 40 ms = 25 frames per second.
		int thinkTime = 40;
		
		PacManSimulator.play(new MyPacMan(), new GameGhosts(), seed, lives, thinkTime);
	}
}