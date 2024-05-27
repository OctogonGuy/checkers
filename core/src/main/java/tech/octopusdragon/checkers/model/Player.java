package tech.octopusdragon.checkers.model;

import java.io.Serializable;

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


	@Override
	public Player clone() {
		Player copy = new Player(this.getType());
		return copy;
	}
}
