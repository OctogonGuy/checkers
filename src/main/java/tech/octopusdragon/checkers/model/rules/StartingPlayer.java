package tech.octopusdragon.checkers.model.rules;

/**
 * The starting player
 * @author Alex Gill
 *
 */
public enum StartingPlayer {
	BLACK,	// Black starts
	WHITE,	// White starts
	EITHER;	// Either black or white can start
	
	
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
