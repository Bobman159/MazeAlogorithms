package com.bobman159.mazes;

import java.io.PrintStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bobman159.mazes.Maze.Direction;

/**
 * A class to write mazes using a <code>PrintStream</code> object.
 *
 */
public class MazeWriter {

	private PrintStream outStream;
	private Maze maze;
	private MazeWriterConfiguration configuration;
	private StringBuilder mazeRow = new StringBuilder();
	private char emptyWall = ' ';
	
	/**
	 * Write a maze to an output stream using default characters
	 * @param outStream the stream to use for writing
	 * @param maze the maze to be written
	 */
	public MazeWriter(PrintStream outStream,Maze maze) {
		this.outStream = outStream;
		this.maze = maze;
		configuration = new MazeWriterConfiguration();
	}
	
	/**
	 * 
	 * @param outStream
	 * @param maze
	 * @param configuration
	 */
	public MazeWriter(PrintStream outStream,Maze maze, MazeWriterConfiguration configuration) {
		this.outStream = outStream;
		this.maze = maze;
		this.configuration = configuration;
	}
	
	public void writeMaze() {
		
		writeMazeTopRow();		
		
		/* Write the body of the maze */
		for (int rowIndex = 0; rowIndex < maze.getRows(); rowIndex++) {			
			int numberOfCellContentRows = configuration.getCellHeight() - 2;
			writeCellWallsRows(rowIndex,numberOfCellContentRows);
			writeCellBottomRow(rowIndex);
		}
	}
	
	/**
	 * Debug convenience method to print a maze's cell contents 
	 * @param maze the maze to debug
	 */
	public static void debugMazeCellContents(Maze maze) {
		
		Logger logger = LogManager.getLogger(MazeWriter.class);
		for(int rowIndex = 0;rowIndex < maze.getRows(); rowIndex++) {
			for(int columnIndex = 0; columnIndex < maze.getColumns(); columnIndex++) {
				MazeCell mazeCell = maze.getMazeCell(rowIndex, columnIndex);
				logger.log(Level.DEBUG,"{}",mazeCell.toString());
			}
		}
	}
	
	private void writeMazeTopRow() {
		
		mazeRow.delete(0, mazeRow.length());	
		for(int columnIndex = 0; columnIndex < maze.getColumns(); columnIndex++) {
			MazeCell cell = maze.getMazeCell(0, columnIndex);
			mazeRow.append(configuration.getCellSeparator());	
			if (cell.hasTopWall()) 
			{
				mazeRow.append(configuration.getTopBottomWall());
			} else {
				mazeRow.append(configuration.getCellContents());
			}
		}
		mazeRow.append(configuration.getCellSeparator());		
		outStream.println(mazeRow.toString());		
	}
	
	private void writeCellBottomRow(int rowIndex) {
		
		mazeRow.delete(0, mazeRow.length());
		for(int columnIndex = 0; columnIndex < maze.getColumns(); columnIndex++) {
			mazeRow.append(configuration.getCellSeparator());	
			MazeCell currentCell = maze.getMazeCell(rowIndex, columnIndex);
			MazeCell belowCell = maze.getMazeCell(currentCell, Direction.DOWN);
			if (belowCell == null) { 
				if (currentCell.hasBottomWall()) {
					mazeRow.append(configuration.getTopBottomWall());
				} else {
					mazeRow.append(configuration.getCellContents());
				}
			} else {				
				if (currentCell.hasBottomWall() ||
					belowCell.hasTopWall()) {
					mazeRow.append(configuration.getTopBottomWall());
				} else {
					mazeRow.append(configuration.getCellContents());
				}
			} 
		}
		mazeRow.append(configuration.getCellSeparator());		
		outStream.println(mazeRow.toString());		
	}
	
	private void writeCellWallsRows(int currentRowIndex, int numberOfCellContentRows) {
		
		mazeRow.delete(0, mazeRow.length());
		for (int rowsToWrite = numberOfCellContentRows; rowsToWrite > 0; rowsToWrite--) {
			for(int columnIndex = 0; columnIndex < maze.getColumns(); columnIndex++) {
				MazeCell currentCell = maze.getMazeCell(currentRowIndex, columnIndex);
				buildLeftWall(currentCell);			
				mazeRow.append(configuration.getCellContents());
				buildRightMazeWall(currentCell);
			}
		}
		outStream.println(mazeRow.toString());		
	}

	private void buildRightMazeWall(MazeCell currentCell) {
		/* Right Wall */
		MazeCell rightCell = maze.getMazeCell(currentCell, Direction.RIGHT);
		if (rightCell != null) {
			/* There is a logical possibility that current cell has a 
			 * right wall but the right cell does not have a left wall or
			 * vice versa.  To be "safe" print out the wall character
			 */
			if (currentCell.hasRightWall() ||
				rightCell.hasLeftWall()) {
				mazeRow.append(configuration.getLeftRightWall());
			} else {
				mazeRow.append(emptyWall);
			}
		} else {
			mazeRow.append(configuration.getLeftRightWall());
		}		
	}

	private void buildLeftWall(MazeCell currentCell) {
		
		/* Left Wall */
		if (currentCell.getColumn() == 0 && currentCell.hasLeftWall()) {
			mazeRow.append(configuration.getLeftRightWall());
		} 
	}
}
