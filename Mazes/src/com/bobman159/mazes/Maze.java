package com.bobman159.mazes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;

public class Maze {

	public enum Direction {UP,DOWN,LEFT,RIGHT}
	private int rows;
	private int columns;
	private MazeCell[][] maze;
	Random randomGenerator;
	
	public Maze(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		maze = new MazeCell[rows][columns];
		createMazeCells();
		randomGenerator = new Random();
	}

	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	/**
	 * Retrieve a maze cell for a given row and column.
	 * @param row the row in the maze of the cell 
	 * @param column the column in the maze of the cell
	 * @return the new cell, null if the new cell is out of bounds
	 */
	public MazeCell getMazeCell(int row, int column) {
		
		if (isCellInBounds(row,column)) {
			return maze[row][column];
		} else {
			return null;
		}
	}
	
	/**
	 * Retrieve a maze cell based on a given cell.
	 * @param cell the cell in the maze to start from
	 * @param direction the direction (UP, DOWN, LEFT, RIGHT) from the start cell 
	 * @return the new cell or null if the new cell is out of bounds
	 */
	public MazeCell getMazeCell(MazeCell startCell, Direction direction) {
		
		MazeCell newCell = null;
		int newCellRow = startCell.getRow();
		int newCellColumn = startCell.getColumn();
		switch (direction) {
			case UP:
				newCellRow = startCell.getRow() - 1;
				if (isCellInBounds(newCellRow,newCellColumn)) {
					newCell = maze[newCellRow][newCellColumn];
				}
				break;
			case DOWN:
				newCellRow = startCell.getRow() + 1;
				if (isCellInBounds(newCellRow,newCellColumn)) {
					newCell = maze[newCellRow][newCellColumn];
				}
				break;
			case LEFT:
				newCellColumn = startCell.getColumn() - 1;
				if (isCellInBounds(newCellRow,newCellColumn)) {
					newCell = maze[newCellRow][newCellColumn];
				}
				break;
			case RIGHT:
				newCellColumn = startCell.getColumn() + 1;
				if (isCellInBounds(newCellRow,newCellColumn)) {
					newCell = maze[newCellRow][newCellColumn];
				}
				break;	
		}
		
		return newCell;
	}
	
	/**
	 * Build a list of unvisited neighbors for a given cell in the maze.  to the list of frontier
	 * cells.  If a cell is a neighbor to the frontier cell but it's already in 
	 * the list of frontier cells it's not added here.
	 * 
	 * @param startingCell the starting cell for finding un-visited neighbors
	 * @return a list of unvisited neighbor cells around the starting cell
	 */
	public  List<MazeCell> findNotVisitedNeighbors(MazeCell startingCell) {
		
		List<MazeCell> unvisitedNeighbors = new ArrayList<>();
		
		MazeCell upCell = getMazeCell(startingCell, Direction.UP);
		if (isOkToAddAsUnvisitedNeighbor(upCell)) {
			unvisitedNeighbors.add(upCell);
		}
		
		MazeCell downCell = getMazeCell(startingCell, Direction.DOWN);
		if (isOkToAddAsUnvisitedNeighbor(downCell)) {
			unvisitedNeighbors.add(downCell);
		}
		
		MazeCell rightCell = getMazeCell(startingCell, Direction.RIGHT);
		if (isOkToAddAsUnvisitedNeighbor(rightCell)) {
			unvisitedNeighbors.add(rightCell);
		}	

		MazeCell leftCell = getMazeCell(startingCell, Direction.LEFT);
		if (isOkToAddAsUnvisitedNeighbor(leftCell)) {
			unvisitedNeighbors.add(leftCell);
		}
		
		return unvisitedNeighbors;
	}
	
	/**
	 * Gets a random cell as the starting point in the maze
	 * @return a random cell in the maze, null if the cell is not in the maze bounds
	 */
	public MazeCell getRandomStartingCell() {
		randomGenerator = new Random();
		int startRow = randomGenerator.nextInt(getRows()-1);
		int startColumn = randomGenerator.nextInt(getColumns()-1);
		return getMazeCell(startRow,startColumn);
	}

	/**
	 * Sets the starting and end of a maze using random columns for the top and
	 * bottom of the maze.
	 */
	public void setStartEndTopAndBottom() {
		/*
		 * ASSUME: that if the maze is generated correctly then randomly picking
		 * the start and end columns should allow for a valid path through the 
		 * maze.
		 */
		int topRowColumn = randomGenerator.nextInt(getColumns()-1);
		getMazeCell(0, topRowColumn).clearWall(MazeCell.TOP);
		
		int bottomRowColumn = randomGenerator.nextInt(getColumns()-1);
		getMazeCell(getRows()-1,bottomRowColumn).clearWall(MazeCell.BOTTOM);
		
		if (LogManager.getLogger().isDebugEnabled()) {
			MazeWriter.debugMazeCellContents(this);
		}
	}

	/*
	 * Returns true if a cell is in bounds, false otherwise
	 */
	private boolean isCellInBounds(int row, int column) {
		
		if (row < 0 || row >= rows ) {
			return false;
		}
		
		if (column < 0 || column >= columns) {
			return false;
		}
		
		return true;
	}
	
	private boolean isOkToAddAsUnvisitedNeighbor(MazeCell cell) {
		
		if (cell != null && !cell.hasCellBeenVisited()) {
				return true;
		}
		return false;
	}
	
	private void createMazeCells() {
		for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
			for (int columnIndex=0; columnIndex < columns; columnIndex++) {
				maze[rowIndex][columnIndex] = new MazeCell(rowIndex,columnIndex);
			}
		}	
	}
}
