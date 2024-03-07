package tech.octopusdragon.checkers.model.rules;

/**
 * Type of king piece which differ in movement
 * @author Alex Gill
 *
 */
public enum KingType {
	SHORT,			// Can only move or capture one square forward or backward
	FLYING,			// Can move and capture any number of squares forward or backward
	SHORT_FLYING;	// Can move any number of squares forward or backward but
					// must land directly behind a captured piece when capturing
	
	/**
	 * @return Whether the type is some type of short (regular short, short
	 * flying, etc.)
	 */
	public boolean isShort() {
		return	this == SHORT ||
				this == SHORT_FLYING;
	}
	
	/**
	 * @return Whether the type is some type of flying (regular flying, short
	 * flying, etc.)
	 */
	public boolean isFlying() {
		return	this == FLYING ||
				this == SHORT_FLYING;
	}
}
