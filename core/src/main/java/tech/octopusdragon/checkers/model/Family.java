package tech.octopusdragon.checkers.model;

/**
 * Represents general categories of variants
 * @author Alex Gill
 *
 */
public enum Family {
	TURKISH,
	INTERNATIONAL,
	GOTHIC,
	FRISIAN,
	SPANISH,
	MISCELLANEOUS,
	SPECIAL,
    CUSTOM;

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
