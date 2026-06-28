package tech.octopusdragon.checkers.model;

/**
 * A capturing move containing the position of the captured piece
 * @author Alex Gill
 *
 */
public class Capture extends Move {
	
	private Position capturePos;

	public Capture(Position fromPos, Position toPos) {
		super(fromPos, toPos);
	}

	public Capture(Position fromPos, Position toPos, Position capturePos) {
		super(fromPos, toPos);
		this.capturePos = capturePos;
	}

	public Capture(Move orig) {
		super(orig);
		if (orig.getClass() == Capture.class) {
			Capture origCapture = (Capture) orig;
			this.capturePos = origCapture.capturePos;
		}
	}

	/**
	 * @return the capture position
	 */
	public Position getCapturePos() {
		return capturePos;
	}

	/**
	 * @param capturePos the capture position to set
	 */
	public void setCapturePos(Position capturePos) {
		this.capturePos = capturePos;
	}

}
