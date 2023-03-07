package com.bobman159.mazes;

import java.text.MessageFormat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A cell in a maze.  
 *
 */
public class MazeCell {

	public static final int TOP = 0;
	public static final int RIGHT = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	private static final String THIS_POSITION = "this: {}";
	private static final String NEIGHBOR_POSITION = "neighborCell: {}";
	
	private int row;
	private int column;
	private boolean cellVisited;
	private boolean[] walls = new boolean[4];
	
	Logger logger = LogManager.getLogger(MazeCell.class);
	
	/**
	 * Construct a cell in the maze
	 * @param row
	 * @param column
	 */
	public MazeCell(int row,int column) {
		this.row = row;
		this.column = column;
		cellVisited = false;
		walls[TOP] = true;
		walls[RIGHT] = true;
		walls[BOTTOM] = true;
		walls[LEFT] = true;
	}

	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	/**
	 * Mark a cell as visited in the maze.
	 */
	public void cellVisited() {
		cellVisited = true;
	}
	
	/**
	 * 
	 * @return true if cell has been visited, false otherwise.
	 */
	public boolean hasCellBeenVisited() {
		return cellVisited;
	}
	
	/**
	 * 
	 * @return true if the cell has a top wall, false otherwise
	 */
	public boolean hasTopWall() {
		return walls[TOP];
	}
		
	/**
	 * 
	 * @return true if the cell has a bottom wall, false otherwise
	 */
	public boolean hasBottomWall() {
		return walls[BOTTOM];
	}
	
	/**
	 * 
	 * @return true if the cell has a left wall, false otherwise
	 */
	public boolean hasLeftWall() {
		return walls[LEFT];
	}
	
	/**
	 * 
	 * @return true if the cell has a right wall, false otherwise
	 */
	public boolean hasRightWall() {
		return walls[RIGHT];
	}
	
	/**
	 * Clear a specified wall from the cell (TOP=0,RIGHT=1,BOTTOM=2,LEFT=3)
	 * @param wall
	 */
	public void clearWall(int wall) {
		walls[wall] = false;
	}
	
	/**
	 * @return the cell information as string for logging.
	 */
	public String cellDebugToString() {

		return MessageFormat.format("Maze Cell: {0}:{1} visited: {2} "
				+ "topWall: {3} rightWall: {4} bottomWall: {5} leftWall: {6}", 
				row, column,cellVisited,walls[TOP],walls[RIGHT],walls[BOTTOM],
				walls[LEFT]);
	}
	
	/**
	 * Return the cell position (row and column) as a string 
	 * 
	 */
	public String cellPositionAsString() {
//		if (logger.isDebugEnabled()) {
			return MessageFormat.format("{0}:{1}", row,column);
//		} else {
//			return " ";
//		}
	}

	/**
	 * Carves or opens a passage between this cell and a given neighbor cell.
	 * @param neighborCell the cell to open a passage with.
	 */
	public void carvePassage(MazeCell neighborCell) {

		if (isAbove(neighborCell)) {
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG,"Neighbor cell {} is ABOVE frontier cell {}, "
					+ "remove bottom wall",neighborCell.cellPositionAsString(),
					cellPositionAsString());
			}
			this.clearWall(TOP);
			neighborCell.clearWall(BOTTOM);
		}
		
		if (//this.getRow() != 0 &&
			isBelow(neighborCell)) {
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG,"Neighbor cell {} is BELOW frontier cell {}, "
					+ "remove top wall",neighborCell.cellPositionAsString(),cellPositionAsString());
			}
			this.clearWall(BOTTOM);
			neighborCell.clearWall(TOP);	
		}
		
		
		if (isLeft(neighborCell)) {
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG,"Neighbor cell {} is LEFT of frontier cell {},"
					+ " remove right wall",neighborCell.cellPositionAsString(), cellPositionAsString());
			}
			this.clearWall(LEFT);
			neighborCell.clearWall(RIGHT);	
		}
		
		if (isRight(neighborCell)) {
			if (logger.isDebugEnabled()) {
				logger.log(Level.DEBUG,"Neighbor cell {} is RIGHT of frontier cell {}, "
					+ "remove left wall",neighborCell.cellPositionAsString(),cellPositionAsString());
			}
			this.clearWall(RIGHT);
			neighborCell.clearWall(LEFT);	
		}		
	}

	/*
	 * Returns true if the frontier cell is Above (row # is lower) than the 
	 * neighbor cell (this) cell, false otherwise.
	 */
	private boolean isAbove(MazeCell neighborCell) {
		boolean isAbove = false;
		if (logger.isDebugEnabled()) {
			logger.log(Level.DEBUG, THIS_POSITION, this.cellPositionAsString());
			logger.log(Level.DEBUG, NEIGHBOR_POSITION, neighborCell.cellPositionAsString());
		}
		if (this.row > neighborCell.row) {
			isAbove = true;
		}
		
		return isAbove;
	}
	
	/*
	 * Returns true if the frontier cell is BELOW (row number is lower) than
	 * the neighborCell, false otherwise
	 */
	private boolean isBelow(MazeCell neighborCell) {

		boolean isBelow = false;
		if (logger.isDebugEnabled()) {
			logger.log(Level.DEBUG, THIS_POSITION, this.cellPositionAsString());
			logger.log(Level.DEBUG, NEIGHBOR_POSITION, neighborCell.cellPositionAsString());
		}
		if (this.row < neighborCell.row) {
			isBelow = true;
		}
		return isBelow;
	}
	
	/*
	 * Returns true if the frontier cell is LEFT (column number is lower) than
	 * the neighborCell, false otherwise
	 */
	private boolean isLeft(MazeCell neighborCell) {
		boolean isLeft = false;
		if (logger.isDebugEnabled()) {
			logger.log(Level.DEBUG, THIS_POSITION, this.cellPositionAsString());
			logger.log(Level.DEBUG, NEIGHBOR_POSITION, neighborCell.cellPositionAsString());
		}
		if (this.column > neighborCell.column) {
			isLeft = true;
		}
		return isLeft;
	}
	
	/*
	 * Returns true if the frontier cell is RIGHT (column number is higher) than
	 */
	private boolean isRight(MazeCell neighborCell) {

		boolean isRight = false;
		if (logger.isDebugEnabled()) {
			logger.log(Level.DEBUG, THIS_POSITION, this.cellPositionAsString());
			logger.log(Level.DEBUG, NEIGHBOR_POSITION, neighborCell.cellPositionAsString());
		}

		if (this.column < neighborCell.column) {
			isRight = true;
		}
		return isRight;
	}	
}
