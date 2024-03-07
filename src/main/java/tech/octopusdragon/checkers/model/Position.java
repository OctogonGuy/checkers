package tech.octopusdragon.checkers.model;

/**
 * A position on the board
 * @author Alex Gill
 *
 */
public class Position {
	
	private int row;	// The row
	private int col;	// The column
	
	/**
	 * Creates an empty position. Row and column will be initialized to -1.
	 */
	public Position() {
		row = -1;
		col = -1;
	}
	
	/**
	 * Creates a position
	 * @param row The row
	 * @param col The column
	 */
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Copy constructor
	 */
	public Position(Position orig) {
		this.row = orig.row;
		this.col = orig.col;
	}

	/**
	 * @return The row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return The column
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Sets the row
	 * @param row The row
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Sets the column
	 * @param col The column
	 */
	public void setCol(int col) {
		this.col = col;
	}
	
	/**
	 * @return Whether this is an empty or null position
	 */
	public boolean isEmpty() {
		return row == -1 || col == -1;
	}
	
	@Override
	public boolean equals(Object obj) {
		Position other;
		try {
			other = (Position) obj;
		} catch (ClassCastException e) {
			return false;
		}
		
		if (this.row != other.row) {
			return false;
		}
		
		if (this.col != other.col) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return "Row: " + row + "\tColumn: " + col;
	}
}
