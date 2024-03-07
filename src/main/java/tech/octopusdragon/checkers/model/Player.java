package tech.octopusdragon.checkers.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * The players in checkers
 * @author Alex Gill
 *
 */
public class Player implements Serializable {
	private static final long serialVersionUID = 7116097390298259559L;
	
	// --- Instance variables ---
	private PlayerType type;	// The player type
	
	
	/**
	 * Initializes the player
	 */
	public Player(PlayerType type) {
		this.type = type;
	}
	
	
	/**
	 * @return The type of player
	 */
	public PlayerType getType() {
		return type;
	}
	
	
	/**
	 * Adds multiple pieces to the player's pieces
	 * @param pieces The pieces to add
	 */
	public void addAllPieces(Collection<Piece> pieces) {
		pieces.addAll(pieces);
	}
	
	
	/**
	 * Removes multiple pieces from the player's collection
	 * @param pieces The pieces to remove
	 */
	public void removePiece(Collection<Piece> pieces) {
		pieces.removeAll(pieces);
	}
	
	
	@Override
	public Player clone() {
		Player copy = new Player(this.getType());
		return copy;
	}
}
