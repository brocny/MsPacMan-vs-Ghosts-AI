package game.controllers.pacman.examples;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import game.core.Game;
import search.*;

public class PacmanProblem implements Problem<Integer> {
	Game game;

    public PacmanProblem(Game game) {
        this.game = game;
    }

    @Override
    public Integer initialState() {
        return game.getCurPacManLoc();
    }

    @Override
    public List<Integer> actions(Integer state) {
        int[] possibleDirs = IntStream.range(0, 4)
        		.filter(x -> game.getNeighbour(state, x) != -1)
        		.toArray();
        
        List<Integer> indebileGhostLocations = IntStream.range(0, Game.NUM_GHOSTS)
        		.filter(g -> !game.isEdible(g))
        		.map(g -> game.getCurGhostLoc(g))
        		.boxed()
        		.collect(Collectors.toList());
        
        List<Integer> ret = Arrays.stream(possibleDirs)
        		.filter(i -> !indebileGhostLocations.contains(game.getNeighbour(state, i)))
        		.boxed()
        		.collect(Collectors.toList());
        
        if (ret.isEmpty())
        	System.err.println("Nowhere to go");
        
        return ret;
    }

    @Override
    public Integer result(Integer state, int action) {
        return game.getNeighbour(state, action);
    }

    @Override
    public boolean isGoal(Integer state) {
    	if(hasPill(state) || hasPowerPill(state))
    		return true;
        
        for (int i = 0; i < Game.NUM_GHOSTS; i++) {
            if (game.getCurGhostLoc(i) == state) {
            	return game.isEdible(i);
            }
        }
        
        return false;
        
    }  
    
    private boolean hasPill(int state) {
    	int pillIndex = game.getPillIndex(state);
    	if(pillIndex == -1) 
    		return false;
    	return game.checkPill(pillIndex);
    }
    
    private boolean hasPowerPill(int state) {
    	int powerPillIndex = game.getPowerPillIndex(state);
    	if(powerPillIndex == -1) 
    		return false;
    	return game.checkPowerPill(powerPillIndex);
    }
    

    @Override
    public double cost(Integer state, int action) {
        int targetState = result(state, action);
        double cost = 1;

        double threshold = 32;
        
        for (int i = 0; i < Game.NUM_GHOSTS; i++) {
            int distanceToGhost = game.getPathDistance(game.getCurGhostLoc(i), targetState);

            if (game.getEdibleTime(i) < 2 && distanceToGhost <= threshold)
                cost += Math.pow(6.5, threshold - distanceToGhost);
        }
        
        return cost;
    }
	
}
