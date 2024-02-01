package tech.octopusdragon.checkers;

/**
 * Represents a direction from a player's perspective in which a piece can move
 * @author Alex Gill
 *
 */
public enum RelativeDirection {
	DIAGONAL_FORWARD,		// NW, NE
	DIAGONAL_BACKWARD,		// SW, SE
	ORTHOGONAL_SIDEWAYS,	// W, E
	ORTHOGONAL_FORWARD,		// N
	ORTHOGONAL_BACKWARD,	// S
}
