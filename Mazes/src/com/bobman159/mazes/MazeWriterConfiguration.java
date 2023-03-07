package com.bobman159.mazes;

/**
 * A configuration class for use in writing/printing mazes.
 *
 */
public class MazeWriterConfiguration {

	private int cellWidth;
	private int cellHeight;
	private char topBottomWallCharacter;
	private char leftRightWall;
	private char cellSeparator;
	private String topBottomWall;
	private String cellContents;
	
	/**
	 * Creates a default <Code>MazeWriterConfiguration</code> 
	 * <ul>
	 * <li>all cells have a width of 4</li>
	 * <li>all cells have a height of 3 counting the top and bottom cell walls</li>
	 * <li> top and bottom walls are printed with '-' for the width of the cell</li>
	 * <li> left and right walls are printed with '|' </li>
	 * <li> cells are separated using '+'</li>
	 * </ul>
	 */
	public MazeWriterConfiguration() {
		cellWidth = 4;
		cellHeight = 3;
		topBottomWallCharacter = '-';
		leftRightWall = '|';
		cellSeparator = '+';
		topBottomWall = String.valueOf(topBottomWallCharacter).repeat(getCellWidth());
		cellContents = String.valueOf(' ').repeat(getCellWidth());
	}

	public int getCellWidth() {
		return cellWidth;
	}

	/**
	 * Override the default cell width of 4
	 * @param cellWidth the new cell width
	 */
	public void setCellWidth(int cellWidth) {
		this.cellWidth = cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
	}

	/**
	 *
	 * @return a full top or bottom wall for the width of a cell
	 */
	public String getTopBottomWall() {
		return topBottomWall;
	}

	/**
	 * Override the default top and bottom wall character '-'
	 * @param topBottomWall the new top and bottom wall character
	 */
	public void setTopBottomWallCharacter(char topBottomWall) {
		this.topBottomWallCharacter = topBottomWall;
	}

	public char getLeftRightWall() {
		return leftRightWall;
	}

	/**
	 * Override the default left and right wall character of '|'
	 * @param leftRightWall the new left and right wall character
	 */
	public void setLeftRightWall(char leftRightWall) {
		this.leftRightWall = leftRightWall;
	}

	public char getCellSeparator() {
		return cellSeparator;
	}

	/**
	 * Override the default cell separator of '+'.  can be the same as the 
	 * left and right wall character to create simple mazes.
	 * @param cellSeparator the new cell separator
	 */
	public void setCellSeparator(char cellSeparator) {
		this.cellSeparator = cellSeparator;
	}

	/**
	 * 
	 * @return the contents of a cell ' ' as a string for the width of a cell
	 */
	public Object getCellContents() {
		return cellContents;
	}
	
	
}
