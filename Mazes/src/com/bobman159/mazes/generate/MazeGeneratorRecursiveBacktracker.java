package com.bobman159.mazes.generate;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bobman159.mazes.IMazeGenerator;
import com.bobman159.mazes.Maze;
import com.bobman159.mazes.Maze.Direction;
import com.bobman159.mazes.MazeCell;
import com.bobman159.mazes.MazeWriter;

/**
 * An implementation of a RecursiveBacktracker algorithm to generate mazes.
 * Uses https://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
 * as a reference.
 *
 */
public class MazeGeneratorRecursiveBacktracker implements IMazeGenerator {

	private Maze maze;
	private Logger logger = LogManager.getLogger(MazeGeneratorRecursiveBacktracker.class);

	/**
	 * Generate a maze with the given number of rows and columns
	 * @param rows  number of rows in the maze
	 * @param columns number of columns in the maze
	 */
	public MazeGeneratorRecursiveBacktracker(int rows,int columns) {
		maze = new Maze(rows,columns);
	}
	
	@Override
	public void createMaze() {
		buildMaze();
		maze.setStartEndTopAndBottom();	
	}

	@Override
	public void writeMaze(PrintStream outputStream) {
		MazeWriter mazeWriter = new MazeWriter(outputStream,maze);
		mazeWriter.writeMaze();

	}
	
	private void buildMaze() {
		logger.log(Level.INFO,"Create a maze using the RecursiveBacktracker algorithm");
		
		/*
		 *	1 	Choose a starting point in the field.
	     */
		
		MazeCell startCell = maze.getRandomStartingCell();
		if (startCell != null) {
			startCell.cellVisited();
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG, "Starting Cell: {}", startCell.cellPositionAsString());
			}
			carvePassages(startCell);
		}

		if (logger.isDebugEnabled()) {
			MazeWriter.debugMazeCellContents(maze);
		}
	}
		
	private void carvePassages(MazeCell fromCell) {
	
		/*	This begins carving passages in the grid, starting at the 
		 * 	upper-left corner, (0,0). And as you might have guessed from the 
		 * 	algorithm’s name, this works recursively, as we’ll see next.
		 * 	The carve_passages_from method first creates a list of directions 
		 * 	that ought to be tried from the current cell:
		*/
		
		Direction[] directions = Direction.values();		
		Collections.shuffle(Arrays.asList(directions));
		for(int directionIndex = 0; directionIndex < directions.length; directionIndex++) {
		    logger.log(Level.DEBUG, "directions[{}]: {}", directionIndex, 
		    		   directions[directionIndex]);
		}

		for (Direction cellDirection : directions) {
			MazeCell newCell = maze.getMazeCell(fromCell, cellDirection);
			if (newCell != null &&
				!(newCell.hasCellBeenVisited()))
			{
				if (logger.isDebugEnabled()) {
					logger.log(Level.DEBUG, "carvePassage fromCell:{} newCell{}",
						fromCell.cellPositionAsString(),newCell.cellPositionAsString());
				}
				newCell.carvePassage(fromCell);
				newCell.cellVisited();
				carvePassages(newCell);
			}
		}
	}

}
