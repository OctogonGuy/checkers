package tech.octopusdragon.checkers.model;

import tech.octopusdragon.checkers.model.Piece.PieceType;

/**
 * The possible types of players in checkers
 * @author Alex Gill
 *
 */
public enum PlayerType {
	
	BLACK,
	WHITE;
	

	/**
	 * @return The man piece of this player's type
	 */
	public PieceType manPiece() {
		PieceType manPiece = null;
		switch (this) {
		case BLACK:
			manPiece = PieceType.BLACK_MAN;
			break;
		case WHITE:
			manPiece = PieceType.WHITE_MAN;
			break;
		}
		return manPiece;
	}
	
	
	/**
	 * @return The king piece of this player's type
	 */
	public PieceType kingPiece() {
		PieceType kingPiece = null;
		switch (this) {
		case BLACK:
			kingPiece = PieceType.BLACK_KING;
			break;
		case WHITE:
			kingPiece = PieceType.WHITE_KING;
			break;
		}
		return kingPiece;
	}
	
	
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