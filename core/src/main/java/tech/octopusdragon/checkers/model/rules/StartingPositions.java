package tech.octopusdragon.checkers.model.rules;

/**
 * The starting position of the pieces
 * @author Alex Gill
 *
 */
public enum StartingPositions {
	ALTERNATING_FROM_BOTTOM_LEFT,	// Alternating spaces from bottom left
	ALTERNATING_FROM_BOTTOM_RIGHT,	// Alternating spaces from bottom right
	ALL_FROM_FIRST_ROW,		// All spaces are filled in from first row
	ALL_FROM_SECOND_ROW,	// All spaces are filled in from second row
	DAMEO;				// Special starting position for Dameo


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
