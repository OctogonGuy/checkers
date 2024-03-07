package tech.octopusdragon.checkers.model;

/**
 * A position with a field representing a captured piece and a field
 * representing the maximum capture value of a maximum capture
 * @author Alex Gill
 *
 */
public class MaximumCapturePosition extends CapturePosition {
	
	private int maxCaptureValue;	// Maximum capture value
	
	/**
	 * Creates an empty capture position. Row and column will be initialized
	 * to -1. Captured piece will be initialized to null.
	 */
	public MaximumCapturePosition() {
		super();
	}
	
	/**
	 * Creates a capture position
	 * @param row The row
	 * @param col The column
	 * @param capturedPiece The captured piece
	 */
	public MaximumCapturePosition(int row, int col, Piece capturedPiece) {
		super(row, col, capturedPiece);
	}
	
	/**
	 * Creates a capture position with a maximum capture value
	 * @param row The row
	 * @param col The column
	 * @param capturedPiece The captured piece
	 * @param maxCaptureValue The maximum capture value
	 */
	public MaximumCapturePosition(int row, int col, Piece capturedPiece,
			int maxCaptureValue) {
		super(row, col, capturedPiece);
		this.setMaxCaptureValue(maxCaptureValue);
	}
	
	/**
	 * Copy constructor
	 */
	public MaximumCapturePosition(MaximumCapturePosition orig) {
		super(orig);
		this.maxCaptureValue = orig.maxCaptureValue;
	}

	/**
	 * @return the maximum capture value
	 */
	public int getMaxCaptureValue() {
		return maxCaptureValue;
	}

	/**
	 * Sets the maximum capture value
	 * @param maxCaptureValue the maximum capture value to set
	 */
	public void setMaxCaptureValue(int maxCaptureValue) {
		this.maxCaptureValue = maxCaptureValue;
	}
}