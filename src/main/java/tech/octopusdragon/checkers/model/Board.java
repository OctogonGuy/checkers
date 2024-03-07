package tech.octopusdragon.checkers.model;

import tech.octopusdragon.checkers.model.Piece.PieceType;
import tech.octopusdragon.checkers.model.rules.BoardPattern;
import tech.octopusdragon.checkers.model.rules.StartingPositions;

/**
 * A checkers board
 * @author Alex Gill
 *
 */
public class Board {
	
	// --- Global variables  ---
	// The player on the top side of the board
	private static PlayerType topPlayerType = PlayerType.BLACK;
	// The player on the bottom side of the board
	private static PlayerType bottomPlayerType =
			topPlayerType == PlayerType.BLACK ? PlayerType.WHITE : PlayerType.BLACK;

	// --- Instance variables ---
	private int rows;				// The number of rows
	private int cols;				// The number of columns
	private BoardPattern pattern;	// The pattern of squares
	private Square[][] squares;		// The squares on the board
	private Piece[][] pieces;		// The pieces on the squares

	
	/**
	 * Creates an empty board
	 * @param rows The number of rows on the board
	 * @param cols The number of columns on the board
	 * @param pattern The pattern of squares on the board
	 */
	public Board(int rows, int cols, BoardPattern pattern) {
		this.rows = rows;
		this.cols = cols;
		this.pattern = pattern;

		// Initialize the squares on the board
		squares = new Square[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				switch (pattern) {
				case ALL_BLACK:
					squares[i][j] = Square.BLACK;
					break;
				case ALL_WHITE:
					squares[i][j] = Square.WHITE;
					break;
				case BOTTOM_LEFT_SQUARE_BLACK:
					squares[i][j] = (i + j) % 2 == 1 ? Square.BLACK : Square.WHITE;
					break;
				case BOTTOM_LEFT_SQUARE_WHITE:
					squares[i][j] = (i + j) % 2 == 0 ? Square.BLACK : Square.WHITE;
					break;
				case FILIPINO:
					squares[i][j] = Square.FILIPINO;
					break;
				}
			}
		}
		
		// Initialize the pieces on the board
		pieces = new Piece[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				pieces[i][j] = null;
			}
		}
	}
	
	
	/**
	 * Creates a board with pieces laid out in the given starting positions
	 * @param rows The number of rows on the board
	 * @param cols The number of columns on the board
	 * @param pattern The pattern of squares on the board
	 * @param numPieces The number of pieces per side
	 * @param positions The positional layout of the starting pieces
	 */
	public Board(int rows, int cols, BoardPattern pattern,
			int numPieces, StartingPositions positions) {
		this(rows, cols, pattern);

		// Place the top pieces
		int topPiecesLeft = numPieces;
		outer: for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				switch (positions) {
				case ALL_FROM_FIRST_ROW:
					pieces[i][j] = new Piece(topPlayerType.manPiece());
					topPiecesLeft--;
					break;
				case ALL_FROM_SECOND_ROW:
					if (i >= 1) {
						pieces[i][j] = new Piece(topPlayerType.manPiece());
						topPiecesLeft--;
					}
					break;
				case ALTERNATING_FROM_BOTTOM_LEFT:
					if ((i + j) % 2 == 1) {
						pieces[i][j] = new Piece(topPlayerType.manPiece());
						topPiecesLeft--;
					}
					break;
				case ALTERNATING_FROM_BOTTOM_RIGHT:
					if ((i + j) % 2 == 0) {
						pieces[i][j] = new Piece(topPlayerType.manPiece());
						topPiecesLeft--;
					}
					break;
				case DAMEO:
					if (j >= i && j < rows - i) {
						pieces[i][j] = new Piece(topPlayerType.manPiece());
						topPiecesLeft--;
					}
					break;
				}
				if (topPiecesLeft == 0)
					break outer;
			}
		}
		
		// Place the bottom pieces
		int bottomPiecesLeft = numPieces;
		outer: for (int i = rows - 1; i >= 0; i--) {
			for (int j = cols - 1; j >= 0; j--) {
				switch (positions) {
				case ALL_FROM_FIRST_ROW:
					pieces[i][j] = new Piece(bottomPlayerType.manPiece());
					bottomPiecesLeft--;
					break;
				case ALL_FROM_SECOND_ROW:
					if (i < rows - 1) {
						pieces[i][j] = new Piece(bottomPlayerType.manPiece());
						bottomPiecesLeft--;
					}
					break;
				case ALTERNATING_FROM_BOTTOM_LEFT:
					if ((i + j) % 2 == 1) {
						pieces[i][j] = new Piece(bottomPlayerType.manPiece());
						bottomPiecesLeft--;
					}
					break;
				case ALTERNATING_FROM_BOTTOM_RIGHT:
					if ((i + j) % 2 == 0) {
						pieces[i][j] = new Piece(bottomPlayerType.manPiece());
						bottomPiecesLeft--;
					}
					break;
				case DAMEO:
					if (j >= rows - 1 - i && j <= i) {
						pieces[i][j] = new Piece(bottomPlayerType.manPiece());
						bottomPiecesLeft--;
					}
					break;
				}
				if (bottomPiecesLeft == 0)
					break outer;
			}
		}
	}
	
	
	/**
	 * Creates a board based on a board state with 2D array piece types
	 * @param boardState The board state
	 * @param pattern The pattern of squares on the board
	 */
	public Board(BoardState boardState, BoardPattern pattern) {
		this(boardState.rows(), boardState.cols(), pattern);

		// Place the pieces
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				PieceType pieceType = boardState.pieceTypes()[i][j];
				pieces[i][j] = pieceType == null ? null : new Piece(pieceType);
			}
		}
	}

	
	/**
	 * @return The number of rows
	 */
	public int getRows() {
		return rows;
	}

	
	/**
	 * @return The number of columns
	 */
	public int getCols() {
		return cols;
	}

	
	/**
	 * @return The pattern of squares
	 */
	public BoardPattern getPattern() {
		return pattern;
	}
	

	/**
	 * @param pos The position on the board
	 * @return The square at the given position
	 */
	public Square getSquare(Position pos) {
		return squares[pos.getRow()][pos.getCol()];
	}

	
	/**
	 * @param row The row on the board
	 * @param col The column on the board
	 * @return The square at the given position
	 */
	public Square getSquare(int row, int col) {
		return squares[row][col];
	}

	
	/**
	 * @param pos The position on the board
	 * @return The piece at the given position
	 */
	public Piece getPiece(Position pos) {
		return pieces[pos.getRow()][pos.getCol()];
	}

	
	/**
	 * @param row The row on the board
	 * @param col The column on the board
	 * @return The piece at the given position
	 */
	public Piece getPiece(int row, int col) {
		return pieces[row][col];
	}

	
	/**
	 * @param pos The position on the board
	 * @return Whether the position on the board has a piece on it
	 */
	public boolean isOccupied(Position pos) {
		return pieces[pos.getRow()][pos.getCol()] != null;
	}

	
	/**
	 * @param row The row on the board
	 * @param col The column on the board
	 * @return Whether the position on the board has a piece on it
	 */
	public boolean isOccupied(int row, int col) {
		return pieces[row][col] != null;
	}
	
	
	/**
	 * @param piece The piece to search for
	 * @return The position of a piece on the board
	 */
	public Position getPosition(Piece piece) {
		Position pos = null;
		outer: for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (pieces[i][j] == piece) {
					pos = new Position(i, j);
					break outer;
				}
			}
		}
		return pos;
	}
	
	
	/**
	 * @param position A position to check
	 * @return Whether the space at the given position exists on the board
	 */
	public boolean isValidSpace(Position position) {
		return isValidSpace(position.getRow(), position.getCol());
	}
	
	
	/**
	 * @param row The row on the board
	 * @param col The column on the board
	 * @return Whether the space at the given position exists on the board
	 */
	public boolean isValidSpace(int row, int col) {
		return row >= 0 && row < rows &&
				col >= 0 && col < cols;
	}
	
	
	/**
	 * @param position A position to check
	 * @return Whether the space at the given position is valid and can be moved
	 * to (i.e.: is not occupied)
	 */
	public boolean isMovableSpace(Position position) {
		return isValidSpace(position) && getPiece(position) == null;
	}
	
	
	/**
	 * @param row The row on the board
	 * @param col The column on the board
	 * @return Whether the space at the given position is valid and can be moved
	 * to (i.e.: is not occupied)
	 */
	public boolean isMovableSpace(int row, int col) {
		return isValidSpace(row, col) && getPiece(row, col) == null;
	}
	
	
	/**
	 * Returns whether the given piece is in the kings row
	 * @param piece The piece 
	 * @return Whether the given piece is in the kings row
	 */
	public boolean isInKingsRow(Piece piece) {
		boolean inKingsRow = false;
		if (piece.getPlayerType() == topPlayerType &&
				getPosition(piece).getRow() == rows - 1 ||
			piece.getPlayerType() == bottomPlayerType &&
				getPosition(piece).getRow() == 0) {
			inKingsRow = true;
		}
		return inKingsRow;
	}
	
	
	/**
	 * @param piece The piece moving
	 * @param position The position to which to move
	 * @return The absolute direction of a move or null if not a move
	 */
	public AbsoluteDirection absoluteDirection(Piece piece, Position position) {
		int curRow;	// The current row
		int curCol;	// The current column
		int rowInc;	// Amount to increment row
		int colInc;	// Amount to increment column
		
		for (AbsoluteDirection absDir : AbsoluteDirection.values()) {
			curRow = getPosition(piece).getRow();
			curCol = getPosition(piece).getCol();
			
			// Find row increment value
			switch (absDir) {
			case N:
			case NW:
			case NE:
				rowInc = -1;
				break;
			case S:
			case SW:
			case SE:
				rowInc = 1;
				break;
			default:
				rowInc = 0;
			}
			
			// Find column increment value
			switch (absDir) {
			case W:
			case NW:
			case SW:
				colInc = -1;
				break;
			case E:
			case NE:
			case SE:
				colInc = 1;
				break;
			default:
				colInc = 0;
			}
			
			// Go in the direction, and if the position is found, return the
			// direction
			do {
				if (curRow == position.getRow() && curCol == position.getCol()) {
					return absDir;
				}
				curRow += rowInc;
				curCol += colInc;
			} while (isValidSpace(curRow, curCol));
		}
		
		return null;
	}
	
	
	

	/**
	 * Moves a piece to a new position
	 * @param piece The piece to move
	 * @param toPos The position to move the piece to
	 */
	public void move(Piece piece, Position toPos) {
		Position fromPos = getPosition(piece);
		move(fromPos, toPos);
	}
	
	
	/**
	 * Moves a piece to a new position
	 * @param piece The piece to move
	 * @param toRow The row of the new position
	 * @param toCol The column of the new position
	 */
	public void move(Piece piece, int toRow, int toCol) {
		Position fromPos = getPosition(piece);
		move(fromPos.getRow(), fromPos.getCol(), toRow, toCol);
	}
	

	/**
	 * Moves the piece at a given position to a new position
	 * @param fromPos The position of the piece
	 * @param toPos The position to move the piece to
	 */
	public void move(Position fromPos, Position toPos) {
		move(fromPos.getRow(), fromPos.getCol(), toPos.getRow(), toPos.getCol());
	}
	
	
	/**
	 * Moves the piece at a given position to a new position
	 * @param fromRow The row of the position of the piece
	 * @param fromCol The column of the position of the piece
	 * @param toRow The row of the position to move the piece to
	 * @param toCol The column of the position to move the piece to
	 */
	public void move(int fromRow, int fromCol, int toRow, int toCol) {
		Piece piece = getPiece(fromRow, fromCol);
		pieces[fromRow][fromCol] = null;
		pieces[toRow][toCol] = piece;
		
		// Indicate that the piece has been moved
		piece.markMoved(true);
		
		// Indicate that all other pieces were not moved
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (pieces[i][j] != null &&
						pieces[i][j].getPlayerType() == piece.getPlayerType() &&
						pieces[i][j] != piece) {
					piece.markMoved(false);
				}
			}
		}
	}
	
	
	
	/**
	 * Removes a piece from the board
	 * @param piece The piece to remove
	 */
	public void remove(Piece piece) {
		remove(getPosition(piece));
	}
	
	
	/**
	 * Removes the piece at the given position from the board
	 * @param pos The position of the piece
	 */
	public void remove(Position pos) {
		remove (pos.getRow(), pos.getCol());
	}
	
	
	/**
	 * Removes the piece at the given position from the board
	 * @param row The row of the position of the piece
	 * @param col The column of the position of the piece
	 */
	public void remove(int row, int col) {
		pieces[row][col] = null;
	}
	
	
	/**
	 * Swaps the top and bottom player, reflecting the board layout
	 */
	public void invert() {
		setTopPlayerType(getBottomPlayerType());
		Piece[][] newPieces = new Piece[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				newPieces[i][j] = pieces[rows - (i + 1)][cols - (j + 1)];
			}
		}
		pieces = newPieces;
	}
	
	
	/**
	 * @return The top player type
	 */
	public static PlayerType getTopPlayerType() {
		return topPlayerType;
	}
	
	
	/**
	 * @return The top player type
	 */
	public static PlayerType getBottomPlayerType() {
		return bottomPlayerType;
	}
	
	
	/**
	 * Sets the top player type. Also sets the bottom player type to the
	 * opposite
	 * @param playerType The top player type
	 */
	public static void setTopPlayerType(PlayerType playerType) {
		topPlayerType = playerType;
		bottomPlayerType = topPlayerType == PlayerType.BLACK ? PlayerType.WHITE : PlayerType.BLACK;
	}
	
	
	/**
	 * Sets the bottom player type. Also sets the top player type to the
	 * opposite
	 * @param playerType The bottom player type
	 */
	public static void setBottomPlayerType(PlayerType playerType) {
		bottomPlayerType = playerType;
		topPlayerType = bottomPlayerType == PlayerType.BLACK ? PlayerType.WHITE : PlayerType.BLACK;
	}
	
	
	
	
	@Override
	public Board clone() {
		Board copy = new Board(rows, cols, pattern);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if(this.pieces[i][j] == null) {
					copy.pieces[i][j] = null;
				}
				else {
					copy.pieces[i][j] = this.pieces[i][j].clone();
				}
			}
		}
		return copy;
	}
	
}
