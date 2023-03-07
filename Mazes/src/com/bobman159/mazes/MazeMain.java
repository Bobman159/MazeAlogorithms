package com.bobman159.mazes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bobman159.mazes.MazeFactory.MazeType;

public class MazeMain {

	private static Logger logger = LogManager.getLogger(MazeMain.class);
	public static void main(String[] args) {

		IMazeGenerator primsGenerator = MazeFactory.createMazeGenerator(MazeType.PRIMS,3, 3);
		primsGenerator.createMaze();
		primsGenerator.writeMaze(System.out);
		
		IMazeGenerator recursiveBacktrackerGenerator = MazeFactory.createMazeGenerator(MazeType.RECURSIVEBACKTRACKER,3, 3);
		recursiveBacktrackerGenerator.createMaze();
		recursiveBacktrackerGenerator.writeMaze(System.out);

	}

}
