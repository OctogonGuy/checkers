package tech.octopusdragon.checkers.rules;

/**
 * The pattern of the board
 * @author Alex Gill
 *
 */
public enum BoardPattern {
	BOTTOM_LEFT_SQUARE_BLACK,	// Check pattern with black bottom left square
	BOTTOM_LEFT_SQUARE_WHITE,	// Check pattern with white bottom left square
	ALL_BLACK,			// All black squares
	ALL_WHITE,			// All white squares
	FILIPINO;			// Board with diagonal lines instead of squares
	
	
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
