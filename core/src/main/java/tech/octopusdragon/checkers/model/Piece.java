package tech.octopusdragon.checkers.model;

import java.io.Serializable;

/**
 * Represents a piece that has a type
 * @author Alex Gill
 *
 */
public class Piece implements Serializable {
	private static final long serialVersionUID = -3719252967902203014L;

	// --- Instance variables ---
	private PieceType type;			// The type of piece
	private boolean hasMoved;		// Whether the piece has been moved
	private int consecutiveMoves;	// Current number of consecutive moves


	/**
	 * Creates a piece
	 * @param type The type of piece
	 */
	public Piece(PieceType type) {
		this.type = type;
	}


	/**
	 * Promotes the piece to a king if possible
	 */
	public void crown() {
		switch (type) {
		case BLACK_MAN:
			type = PieceType.BLACK_KING;
			break;
		case WHITE_MAN:
			type = PieceType.WHITE_KING;
			break;
		default:
		}
	}


	/**
	 * @return The type of piece
	 */
	public PieceType getType() {
		return type;
	}


	/**
	 *
	 * @return Whether the piece has been moved at least once
	 */
	public boolean hasMoved() {
		return hasMoved;
	}


	/**
	 *
	 * @return The number of moves this piece has made in succession
	 */
	public int getConsecutiveMoves() {
		return consecutiveMoves;
	}


	/**
	 * Resets the number of moves this piece has made in succession
	 */
	public void resetConsecutiveMoves() {
		consecutiveMoves = 0;
	}


	/**
	 * Records whether this piece has just been moved
	 * @param moved Whether the piece was moved
	 */
	public void markMoved(boolean moved) {
		// If this piece was just moved and it has not been moved before,
		// indicate that it has been moved
		if (moved && !hasMoved) {
			hasMoved = true;
		}

		// Update the consecutive moves
		if (moved) {
			consecutiveMoves++;
		}
		else if (consecutiveMoves > 0) {
			consecutiveMoves = 0;
		}
	}


	/**
	 * @return The player type who owns this piece
	 */
	public PlayerType getPlayerType() {
		PlayerType playerType = null;
		switch (type) {
		case BLACK_MAN:
		case BLACK_KING:
			playerType = PlayerType.BLACK;
			break;
		case WHITE_MAN:
		case WHITE_KING:
			playerType = PlayerType.WHITE;
			break;
		}
		return playerType;
	}


	/**
	 * @return Whether the piece is black
	 */
	public boolean isBlack() {
		return type == PieceType.BLACK_MAN || type == PieceType.BLACK_KING;
	}


	/**
	 * @return Whether the piece is white
	 */
	public boolean isWhite() {
		return type == PieceType.WHITE_MAN || type == PieceType.WHITE_KING;
	}


	/**
	 * @return Whether the piece is a man
	 */
	public boolean isMan() {
		return type == PieceType.BLACK_MAN || type == PieceType.WHITE_MAN;
	}


	/**
	 * @return Whether the piece is a king
	 */
	public boolean isKing() {
		return type == PieceType.BLACK_KING || type == PieceType.WHITE_KING;
	}


	@Override
	public Piece clone() {
		Piece copy = new Piece(this.getType());
		return copy;
	}


	/**
	 * The possible types of pieces in checkers
	 * @author Alex Gill
	 *
	 */
	public enum PieceType {
		BLACK_MAN,
		WHITE_MAN,
		BLACK_KING,
		WHITE_KING;

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(this.name().toLowerCase());
			// Replace underscores with spaces
			for (int i = 0; i < sb.length(); i++) {
				if (sb.charAt(i) == '_') {
					sb.replace(i, i + 1, " ");
				}
			}
			// Capitalize
			sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
			for (int i = 0; i < sb.length(); i++) {
				if (i >= 1 && sb.charAt(i - 1) == ' ') {
					sb.replace(i, i + 1, sb.substring(i, i + 1).toUpperCase());
				}
			}
			return sb.toString();
		}
	}
}
