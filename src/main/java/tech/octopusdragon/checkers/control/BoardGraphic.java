package tech.octopusdragon.checkers.control;

import java.io.IOException;

import tech.octopusdragon.checkers.model.Checkers;
import tech.octopusdragon.checkers.model.Piece;
import tech.octopusdragon.checkers.model.Variant;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class BoardGraphic extends GridPane {
	
	// --- Constants ---
	// Default height of the board
	private static final double DEFAULT_BOARD_HEIGHT = 400.0;
	
	// --- Variables ---
	private double squareSize;	// Height and width of squares
	private int rows;				// The number of rows
	private int cols;				// The number of columns
	
	// --- UI components ---
	private StackPane[][] grid;			// Panes including squares and pieces
	private SquareGraphic[][] squares;	// The squares
	private PieceGraphic[][] pieces;	// The pieces

	
	/**
	 * Default constructor
	 */
	public BoardGraphic() {
		super();

		// Load FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Board.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Builds a new board
	 * @param game The game whose variant the board will be based on
	 */
	public BoardGraphic(Checkers game) {
		this();
		
		// Relevant variables
		Variant variant = game.getVariant();
		rows = variant.getRows();
		cols = variant.getCols();
		squareSize = Math.round(DEFAULT_BOARD_HEIGHT / rows);
		
		// Add a style class based on the board pattern
		StringBuilder styleClass = new StringBuilder(variant.getBoardPattern().name());
		// Convert to lowercase
		styleClass.replace(0, styleClass.length(), styleClass.toString().toLowerCase());
		// Replace underscores with hyphens
		for (int i = 0; i < styleClass.length(); i++) {
			if (styleClass.charAt(i) == '_') {
				styleClass.replace(i, i + 1, "-");
			}
		}
		// Set as style class
		this.getStyleClass().add(styleClass.toString());
		
		// Create the board
		grid = new StackPane[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = new StackPane();
				grid[i][j].getStyleClass().add("space");
				this.add(grid[i][j], j, i);
			}
		}
		
		// Add the squares
		squares = new SquareGraphic[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				// Add the square
				squares[i][j] = new SquareGraphic(game.getBoard().getSquare(i, j), squareSize);
				grid[i][j].getChildren().add(squares[i][j]);
				
				// Add style classes for square location types
				if (i == 0) {
					squares[i][j].getStyleClass().add("top");
				}
				else if (i == rows - 1) {
					squares[i][j].getStyleClass().add("bottom");
				}
				if (j == 0) {
					squares[i][j].getStyleClass().add("left");
				}
				else if (j == cols - 1) {
					squares[i][j].getStyleClass().add("right");
				}
				if ((i + j) % 2 == 0) {
					squares[i][j].getStyleClass().add("odd");
				}
				else {
					squares[i][j].getStyleClass().add("even");
				}
			}
		}
		
		// Add the pieces
		pieces = new PieceGraphic[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Piece piece = game.getBoard().getPiece(i, j);
				if (piece == null) continue;
				pieces[i][j] = new PieceGraphic(piece, squareSize);
				grid[i][j].getChildren().add(pieces[i][j]);
			}
		}
	}


	/**
	 * @return the square size
	 */
	public double getSquareSize() {
		return squareSize;
	}
	

	/**
	 * Returns a space on the board that includes the square and piece, if any
	 * @param row The row
	 * @param col The column
	 * @return The space
	 */
	public StackPane getSpace(int row, int col) {
		return grid[row][col];
	}
	

	/**
	 * Adds a styleClass of "active" to the given space
	 * @param row The row
	 * @param col The column
	 */
	public void activateSpace(int row, int col) {
		if (grid[row][col].getStyleClass().contains("active")) return;
		grid[row][col].getStyleClass().add("active");
	}
	

	/**
	 * Removes the "active" styleClass from the given space
	 * @param row The row
	 * @param col The column
	 */
	public void deactivateSpace(int row, int col) {
		if (!grid[row][col].getStyleClass().contains("active")) return;
		grid[row][col].getStyleClass().remove("active");
	}
	

	/**
	 * Returns the square graphic at the given position
	 * @param row The row
	 * @param col The column
	 * @return The square
	 */
	public SquareGraphic getSquare(int row, int col) {
		return squares[row][col];
	}
	

	/**
	 * Returns the piece graphic at the given position
	 * @param row The row
	 * @param col The column
	 * @return The piece
	 */
	public PieceGraphic getPiece(int row, int col) {
		return pieces[row][col];
	}
	

	/**
	 * Replaces the piece graphic at the given position with a new piece graphic
	 * @param row The row
	 * @param col The column
	 * @param piece The piece
	 */
	public void setPiece(int row, int col, PieceGraphic piece) {
		if (piece == null) return;
		removePiece(row, col);
		pieces[row][col] = piece;
		grid[row][col].getChildren().add(piece);
	}
	

	/**
	 * Returns the piece graphic at the given position
	 * @param row The row
	 * @param col The column
	 * @return The piece
	 */
	public void removePiece(int row, int col) {
		if (pieces[row][col] == null) return;
		grid[row][col].getChildren().remove(pieces[row][col]);
		pieces[row][col] = null;
	}
	
	
	/**
	 * Swaps the top and bottom player, reflecting the board layout
	 */
	public void invert() {
		PieceGraphic[][] newPieces = new PieceGraphic[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				newPieces[i][j] = pieces[(rows - 1) - i][(cols - 1) - j];
			}
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				removePiece(i, j);
				pieces[i][j] = newPieces[i][j];
				if (newPieces[i][j] != null) {
					grid[i][j].getChildren().add(pieces[i][j]);
				}
			}
		}
	}

}
