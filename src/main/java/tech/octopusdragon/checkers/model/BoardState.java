package tech.octopusdragon.checkers.model;

import java.util.Arrays;
import java.util.Objects;

import tech.octopusdragon.checkers.model.Piece.PieceType;

/**
 * Like a board, but only keeps track of the types of pieces on the board rather
 * than piece objects and other information
 * @author Alex Gill
 *
 */
public class BoardState {
	
	// --- Instance variables ---
	private PieceType[][] pieceTypes;	// The piece types on the squares
	private PlayerType playerType;		// The current player's type
	
	/**
	 * Constructor
	 * @param board The board this is based off
	 * @param player The current player
	 */
	public BoardState(Board board, Player player) {
		int rows = board.getRows();
		int cols = board.getCols();
		pieceTypes = new PieceType[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (board.getPiece(i, j) == null) {
					pieceTypes[i][j] = null;
				}
				else {
					pieceTypes[i][j] = board.getPiece(i, j).getType();
				}
			}
		}
		playerType = player.getType();
	}
	
	
	/**
	 * @return The number of rows on the board
	 */
	public int rows() {
		return pieceTypes.length;
	}
	
	
	/**
	 * @return The number of columns on the board
	 */
	public int cols() {
		return pieceTypes[0].length;
	}
	
	
	/**
	 * @return The state of the board
	 */
	public PieceType[][] pieceTypes() {
		return pieceTypes.clone();
	}
	
	
	/**
	 * @return The type of the current player at the time of saving
	 */
	public PlayerType playerType() {
		return playerType;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof BoardState))
			return false;
		BoardState other = (BoardState) obj;
		return playerType == other.playerType && Arrays.deepEquals(pieceTypes, other.pieceTypes);
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(pieceTypes);
		result = prime * result + Objects.hash(playerType);
		return result;
	}

}
