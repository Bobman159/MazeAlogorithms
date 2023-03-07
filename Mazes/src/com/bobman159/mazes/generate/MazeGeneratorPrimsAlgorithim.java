package com.bobman159.mazes.generate;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bobman159.mazes.IMazeGenerator;
import com.bobman159.mazes.Maze;
import com.bobman159.mazes.Maze.Direction;
import com.bobman159.mazes.MazeCell;
import com.bobman159.mazes.MazeWriter;

/**
 * A maze generator using Prims Algorithm.
 * Based on https://weblog.jamisbuck.org/2011/1/10/maze-generation-prim-s-algorithm
 * 
 *
 */
public class MazeGeneratorPrimsAlgorithim implements IMazeGenerator {

	private Maze maze;
	private List<MazeCell> frontierCellsList;
	private Random randomGenerator;
	private Logger logger = LogManager.getLogger(MazeGeneratorPrimsAlgorithim.class);
	
	public MazeGeneratorPrimsAlgorithim(int rows, int columns) {
		maze = new Maze(rows,columns);
		frontierCellsList = new ArrayList<>();
		randomGenerator = new Random();
	}
	
	@Override
	public void createMaze() {
		buildMaze();
		maze.setStartEndTopAndBottom();	
	}

	@Override
	public void writeMaze(PrintStream outputStream) {
		MazeWriter mazeOut = new MazeWriter(outputStream,maze);
		mazeOut.writeMaze();
	}
	
	private void buildMaze() {

		logger.log(Level.INFO,"Build maze using Prims Algorithim");
		
		/* Choose a point at random and add it to the maze */
		MazeCell startCell = maze.getRandomStartingCell();
		if (logger.isDebugEnabled()) {
			logger.log(Level.DEBUG, "Starting Cell: {}", startCell.cellPositionAsString());
		}
		
		if (startCell != null) {
			startCell.cellVisited();
			frontierCellsList.add(startCell);
		}
		
		while (!frontierCellsList.isEmpty()) {
			
			int frontierCellIndex = randomGenerator.nextInt(frontierCellsList.size());
			MazeCell frontierCell = frontierCellsList.get(frontierCellIndex);
			
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG, "frontierCell: {}",frontierCell.cellPositionAsString());
			}
			
			/* Build a list of frontier cells
			 * 		*	For efficiency, let’s call the set of all cells that are not 
			 * 		  	yet in the maze, but are adjacent to a cell that is in the 
			 * 			maze, the “frontier”. 
			 */
			 List<MazeCell> unvisitedNeighbors = maze.findNotVisitedNeighbors(frontierCell);
			 
			 /* Add the Unvisited neighbors to the list of frontier cells */
			 addUnvisitedNeighborsToFrontierCellsList(unvisitedNeighbors);
			 if (logger.isDebugEnabled()) {
				 debugArrayCells("unVisitedNeighbors",unvisitedNeighbors);
				 debugArrayCells("frontierCellsList",frontierCellsList);
				 logger.log(Level.DEBUG,"find visited neighbors for frontierCell: {}", 
						 frontierCell.cellPositionAsString());
			 }
			 
			 /* Then we choose a random “in” neighbor of that frontier cell:
			  * and carve a passage between the frontier cell and the neighbor cell.
			  */
			 carvePathBetweenNeighborAndFrontierCells(frontierCell);		 

			 /*
			  * And finally, we mark the frontier cell as being “in” the maze 
			  */
			 frontierCell.cellVisited();
			 debugArrayCells("frontierCellsList: ", frontierCellsList);

			 if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG, "frontierCellsList remove cell: {}",frontierCell.cellPositionAsString());
			 }
			 frontierCellsList.remove(frontierCell);
			 if (logger.isDebugEnabled()) { 
				 logger.log(Level.DEBUG, "frontierCellsList size after remove: {}",frontierCellsList.size());		 
			 }
		}	
		
		if (logger.isDebugEnabled()) {
			MazeWriter.debugMazeCellContents(maze);
		}
	}

	private void carvePathBetweenNeighborAndFrontierCells(MazeCell frontierCell) {
		 
		List<MazeCell> visitedNeighbors = findVisitedNeighbors(frontierCell);
		 if (!visitedNeighbors.isEmpty()) {
			 if (logger.isDebugEnabled()) {
				 debugArrayCells("visitedNeighbors: ", visitedNeighbors);
			 }
			 int randomCellIndex = randomGenerator.nextInt(visitedNeighbors.size());
			 MazeCell neighborCell = visitedNeighbors.get(randomCellIndex);
			 neighborCell.cellVisited();
			 frontierCell.carvePassage(neighborCell);				 
		 }
		
	}

	private void addUnvisitedNeighborsToFrontierCellsList(List<MazeCell> unvisitedNeighbors) {

		 for (MazeCell unVisitedNeighborCell: unvisitedNeighbors) {
			 if (isOkToAddAsUnvisitedNeighbor(unVisitedNeighborCell)) {
				 frontierCellsList.add(unVisitedNeighborCell);
			 }
		 }
		 logger.log(Level.DEBUG, "frontierCellsList.size: {}",frontierCellsList.size());
		
	}

	/*
	 * Print the frontierCellsList.  The method is only called when log level is
	 * DEBUG
	 */
	private void debugArrayCells(String arrayName, List<MazeCell> arrayList) {
		logger.log(Level.DEBUG, "{}.size: {}",  arrayName, arrayList.size());
		for (MazeCell currentCell : arrayList) {
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG, "{} [{}] {}",arrayName, 
					arrayList.indexOf(currentCell),currentCell.cellDebugToString());
			}
		}	
	}


	
	/*
	 * Build a list of visited neighbors for a given frontier cell in the maze.
	 * 
	 * NOTE: This method is currently in the generation class since it's currently
	 * only used by the prims algorithim logic.  If it's needed by other generators
	 * it could be moved to the MazeCell class.
	 */
	private  List<MazeCell> findVisitedNeighbors(MazeCell frontierCell) {
		
		List<MazeCell> visitedNeighbors = new ArrayList<>();
		
		MazeCell upCell = maze.getMazeCell(frontierCell, Direction.UP);
		if (isOkToAddAsVisitedNeighbor(upCell)) {
			visitedNeighbors.add(upCell);
		}
		
		MazeCell rightCell = maze.getMazeCell(frontierCell, Direction.RIGHT);
		if (isOkToAddAsVisitedNeighbor(rightCell)) {
			visitedNeighbors.add(rightCell);
		}
		
		MazeCell downCell = maze.getMazeCell(frontierCell, Direction.DOWN);
		if (isOkToAddAsVisitedNeighbor(downCell)) {
			visitedNeighbors.add(downCell);
		}
		
		MazeCell leftCell = maze.getMazeCell(frontierCell, Direction.LEFT);
		if (isOkToAddAsVisitedNeighbor(leftCell)) {
			visitedNeighbors.add(leftCell);
		}
		
		return visitedNeighbors;
	}
	
	/*
	 * IF cell is in maze boundaries (not equal null) AND
	 *    cell has NOT been visited AND
	 *    cell is NOT already in frontier cells list 
	 *    THEN cell is ok to add as neighbor cell
	 */
	private boolean isOkToAddAsUnvisitedNeighbor(MazeCell cell) {
	
		boolean isUnvisitedNeighborCell = false;
		if (cell != null && !cell.hasCellBeenVisited() &&
			!(frontierCellsList.contains(cell))) {
				isUnvisitedNeighborCell = true;
		}
		return isUnvisitedNeighborCell;
	}
	
	/*
	 * IF cell is in maze boundaries (not equal null) AND
	 *    cell HAS been visited 
	 *    THEN cell is ok to as visited neighbor cell
	 */
	private boolean isOkToAddAsVisitedNeighbor(MazeCell cell) {
	
		boolean isVisitedNeighborCell = false;
		if (cell != null && cell.hasCellBeenVisited()) {
				isVisitedNeighborCell = true;
		}
		return isVisitedNeighborCell;
	}
}
