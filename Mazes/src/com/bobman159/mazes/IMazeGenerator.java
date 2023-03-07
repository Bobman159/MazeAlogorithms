package com.bobman159.mazes;

import java.io.PrintStream;

public interface IMazeGenerator {

	/**
	 * Create a maze using a specific algorithim
	 */
	public void createMaze();
	public void writeMaze(PrintStream outputStream);
}
