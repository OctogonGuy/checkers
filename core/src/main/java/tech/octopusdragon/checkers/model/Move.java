package tech.octopusdragon.checkers.model;

/**
 * A move from one position on the board to another
 * @author Alex Gill
 *
 */
public class Move {

	private Position fromPos;
	private Position toPos;

	/**
	 * Creates a move
	 * @param fromPos the "from" position
	 * @param toPos the "to" position
	 */
	public Move(Position fromPos, Position toPos) {
		this.fromPos = fromPos;
		this.toPos = toPos;
	}

	/**
	 * Copy constructor
	 */
	public Move(Move orig) {
		this.fromPos = new Position(orig.fromPos);
		this.toPos = new Position(orig.toPos);
	}

	/**
	 * @return the "from" position
	 */
	public Position getFromPos() {
		return fromPos;
	}

	/**
	 * @param fromPos the "from" position to set
	 */
	public void setFromPos(Position fromPos) {
		this.fromPos = fromPos;
	}

	/**
	 * @return the "to" position
	 */
	public Position getToPos() {
		return toPos;
	}

	/**
	 * @param toPos the "to" position to set
	 */
	public void setToPos(Position toPos) {
		this.toPos = toPos;
	}

	/**
	 * @return Whether this is an empty or null move
	 */
	public boolean isEmpty() {
		return fromPos == null || fromPos.isEmpty() || toPos == null || toPos.isEmpty();
	}

	@Override
	public boolean equals(Object obj) {
		Move other;
		try {
			other = (Move) obj;
		} catch (ClassCastException e) {
			return false;
		}

		if (!this.fromPos.equals(other.fromPos)) {
			return false;
		}

		if (!this.toPos.equals(other.toPos)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "From = " + fromPos + "\tTo = " + toPos;
	}

}
