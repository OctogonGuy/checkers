package tech.octopusdragon.checkers.model;

/**
 * Represents a direction relative to the board
 * @author Alex Gill
 *
 */
public enum AbsoluteDirection {
	N,
	S,
	E,
	W,
	NW,
	NE,
	SW,
	SE;


	/**
	 * Returns the opposite direction
	 */
	public AbsoluteDirection opposite() {
		AbsoluteDirection oppositeDir = null;
		switch (this) {
		case N:
			oppositeDir = S;
			break;
		case S:
			oppositeDir = N;
			break;
		case E:
			oppositeDir = W;
			break;
		case W:
			oppositeDir = E;
			break;
		case NW:
			oppositeDir = SE;
			break;
		case NE:
			oppositeDir = SW;
			break;
		case SW:
			oppositeDir = NE;
			break;
		case SE:
			oppositeDir = NW;
			break;
		}
		return oppositeDir;
	}
}
