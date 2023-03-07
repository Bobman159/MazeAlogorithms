package com.bobman159.mazes;

import com.bobman159.mazes.generate.MazeGeneratorPrimsAlgorithim;
import com.bobman159.mazes.generate.MazeGeneratorRecursiveBacktracker;

public class MazeFactory {
	
	public enum MazeType {PRIMS, RECURSIVEBACKTRACKER}
	
	/**
	 * Creates a maze generator
	 * @param type the type of maze generation algorithm to use
	 * @param rows the number of rows in the maze
	 * @param columns the number of columns in the maze
	 * @return 
	 */
	public static IMazeGenerator createMazeGenerator(MazeType type,int rows, int columns) {
		
		IMazeGenerator mazeGenerator = null;
		switch(type) {
			case PRIMS:
				mazeGenerator = new MazeGeneratorPrimsAlgorithim(rows,columns);
				break;
			case RECURSIVEBACKTRACKER:
				mazeGenerator = new MazeGeneratorRecursiveBacktracker(rows,columns);
				break;
		}
		
		return mazeGenerator;
		
	}

}
