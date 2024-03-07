package tech.octopusdragon.checkers.model;

/**
 * A position with a field representing a captured piece
 * @author Alex Gill
 *
 */
public class CapturePosition extends Position {
	
	private Piece capturedPiece;	// The captured piece
	
	/**
	 * Creates an empty capture position. Row and column will be initialized
	 * to -1. Captured piece will be initialized to null.
	 */
	public CapturePosition() {
		super();
		capturedPiece = null;
	}
	
	/**
	 * Creates a capture position without a captured piece, which will be
	 * initialized to null.
	 * @param row The row
	 * @param col The column
	 */
	public CapturePosition(int row, int col) {
		super(row, col);
		capturedPiece = null;
	}
	
	/**
	 * Creates a capture position
	 * @param capturedPiece The captured piece
	 */
	public CapturePosition(Piece capturedPiece) {
		super();
		this.capturedPiece = capturedPiece;
	}
	
	/**
	 * Creates a capture position
	 * @param row The row
	 * @param col The column
	 * @param capturedPiece The captured piece
	 */
	public CapturePosition(int row, int col, Piece capturedPiece) {
		super(row, col);
		this.capturedPiece = capturedPiece;
	}
	
	/**
	 * Copy constructor
	 */
	public CapturePosition(CapturePosition orig) {
		super(orig);
		this.capturedPiece = orig.capturedPiece;
	}
	
	/**
	 * @return The captured piece
	 */
	public Piece getCapturedPiece() {
		return capturedPiece;
	}
	
	/**
	 * Sets the captured piece
	 * @param capturedPiece The captured piece to set
	 */
	public void setCapturedPiece(Piece capturedPiece) {
		this.capturedPiece = capturedPiece;
	}
}