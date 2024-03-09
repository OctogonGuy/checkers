package tech.octopusdragon.checkers.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tech.octopusdragon.checkers.control.BoardGraphic;
import tech.octopusdragon.checkers.control.GhostPieceGraphic;
import tech.octopusdragon.checkers.control.PieceGraphic;
import tech.octopusdragon.checkers.model.Board;
import tech.octopusdragon.checkers.model.Capture;
import tech.octopusdragon.checkers.model.Checkers;
import tech.octopusdragon.checkers.model.ComputerPlayer;
import tech.octopusdragon.checkers.model.Move;
import tech.octopusdragon.checkers.model.Piece;
import tech.octopusdragon.checkers.model.Player;
import tech.octopusdragon.checkers.model.PlayerType;
import tech.octopusdragon.checkers.model.Position;
import tech.octopusdragon.checkers.model.rules.StartingPlayer;
import tech.octopusdragon.checkers.window.NewGameDialog;
import tech.octopusdragon.checkers.window.StartingPlayerDialog;
import tech.octopusdragon.checkers.window.VariantInfoWindow;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameRootController {
	
	// --- Global constants ---
	// Duration of the move animation
	private static final double MOVE_DURATION = 900.0;
	// Delay of non-immediate captures
	private static final double CAPTURE_DELAY = 100.0;
	// Amount by which to lift removed pieces before disappearing
	private static final double REMOVE_SCALE = 2.0;
	// Duration of removal animation of pieces
	private static final double REMOVE_DURATION = 450;
	// Size of the piece graphic of the message label in ems
	private static final double MESSAGE_GRAPHIC_SIZE = 3;
	
	
	// --- Global variables ---
	// Whether to highlight potential moves
	private static boolean highlightMoves = true;
	// Whether each player is a computer player
	private static boolean topPlayerComputer = false;
	private static boolean bottomPlayerComputer = false;
	
	
	// --- Global variables ---
	private Checkers game;			// The game
	private boolean selected;		// Whether a position is selected
	private Position selectedPos;	// The last selected position on the board
	private boolean dragging;		// Whether a drag gesture is occurring
	private PieceGraphic draggedPiece;	// The piece on the board before drag
	private PieceGraphic dragPiece;	// The graphic of a piece being dragged
	
	
	// --- GUI Components ---
	// The root node
	@FXML private BorderPane root;
	// Panes holding the board for animation and scaling purposes
	@FXML private StackPane outerContainer;
	@FXML private StackPane innerContainer;
	@FXML private StackPane boardContainer;
	// Sides
	@FXML private StackPane topPane;
	@FXML private StackPane bottomPane;
	// Menu
	@FXML private CheckMenuItem highlightMovesMenuItem;
	// End turn buttons
	private Map<PlayerType, Button> endTurnButtons;
	@FXML private Button topPlayerEndTurnButton;
	@FXML private Button bottomPlayerEndTurnButton;
	// Message labels
	private Map<PlayerType, Label> messageLabels;
	@FXML private Label topPlayerMessageLabel;
	@FXML private Label bottomPlayerMessageLabel;
	// The board
	private BoardGraphic board;
	
	
	
	@FXML
	private void initialize() {
		
		// Initialize GUI components
		messageLabels = new HashMap<>();
		messageLabels.put(Board.getTopPlayerType(), topPlayerMessageLabel);
		messageLabels.put(Board.getBottomPlayerType(), bottomPlayerMessageLabel);
		endTurnButtons = new HashMap<>();
		endTurnButtons.put(Board.getTopPlayerType(), topPlayerEndTurnButton);
		endTurnButtons.put(Board.getBottomPlayerType(), bottomPlayerEndTurnButton);
		
		// Scale the grid to the container size
		outerContainer.prefHeightProperty().bind(((Pane)root.getCenter()).heightProperty().subtract(Bindings.max(
				topPlayerMessageLabel.heightProperty().multiply(topPlayerMessageLabel.scaleYProperty()).multiply(2),
				topPlayerEndTurnButton.heightProperty().multiply(topPlayerEndTurnButton.scaleYProperty()).multiply(2))));
		innerContainer.scaleXProperty().bind(Bindings.min(
				outerContainer.widthProperty().divide(innerContainer.widthProperty()),
				outerContainer.prefHeightProperty().divide(innerContainer.heightProperty())));
		innerContainer.scaleYProperty().bind(Bindings.min(
				outerContainer.widthProperty().divide(innerContainer.widthProperty()),
				outerContainer.prefHeightProperty().divide(innerContainer.heightProperty())));
		
		for (Button endTurnButton : endTurnButtons.values()) {
			endTurnButton.scaleXProperty().bind(Bindings.min(
					outerContainer.widthProperty().divide(innerContainer.widthProperty()),
					outerContainer.heightProperty().divide(innerContainer.heightProperty())));
			endTurnButton.scaleYProperty().bind(Bindings.min(
					outerContainer.widthProperty().divide(innerContainer.widthProperty()),
					outerContainer.heightProperty().divide(innerContainer.heightProperty())));
		}
		
		topPlayerEndTurnButton.translateYProperty().bind(topPlayerMessageLabel.heightProperty().multiply(Bindings.min(
				outerContainer.widthProperty().divide(innerContainer.widthProperty()),
				outerContainer.heightProperty().divide(innerContainer.heightProperty()))).divide(2).subtract(topPlayerMessageLabel.heightProperty().divide(2)));
		
		bottomPlayerEndTurnButton.translateYProperty().bind(bottomPlayerMessageLabel.heightProperty().multiply(Bindings.min(
				outerContainer.widthProperty().divide(innerContainer.widthProperty()),
				outerContainer.heightProperty().divide(innerContainer.heightProperty()))).divide(2).subtract(bottomPlayerMessageLabel.heightProperty().divide(2)).negate());
		
		for (Label messageLabel : messageLabels.values()) {
			messageLabel.scaleXProperty().bind(Bindings.min(
					outerContainer.widthProperty().divide(innerContainer.widthProperty()),
					outerContainer.heightProperty().divide(innerContainer.heightProperty())));
			messageLabel.scaleYProperty().bind(Bindings.min(
					outerContainer.widthProperty().divide(innerContainer.widthProperty()),
					outerContainer.heightProperty().divide(innerContainer.heightProperty())));
		}
		
		topPlayerMessageLabel.translateXProperty().bind(topPlayerMessageLabel.widthProperty().multiply(Bindings.min(
				outerContainer.widthProperty().divide(innerContainer.widthProperty()),
				outerContainer.heightProperty().divide(innerContainer.heightProperty()))).divide(2).subtract(topPlayerMessageLabel.widthProperty().divide(2)));
		topPlayerMessageLabel.translateYProperty().bind(topPlayerMessageLabel.heightProperty().multiply(Bindings.min(
				outerContainer.widthProperty().divide(innerContainer.widthProperty()),
				outerContainer.heightProperty().divide(innerContainer.heightProperty()))).divide(2).subtract(topPlayerMessageLabel.heightProperty().divide(2)));
		
		bottomPlayerMessageLabel.translateXProperty().bind(bottomPlayerMessageLabel.widthProperty().multiply(Bindings.min(
				outerContainer.widthProperty().divide(innerContainer.widthProperty()),
				outerContainer.heightProperty().divide(innerContainer.heightProperty()))).divide(2).subtract(bottomPlayerMessageLabel.widthProperty().divide(2)).negate());
		bottomPlayerMessageLabel.translateYProperty().bind(bottomPlayerMessageLabel.heightProperty().multiply(Bindings.min(
				outerContainer.widthProperty().divide(innerContainer.widthProperty()),
				outerContainer.heightProperty().divide(innerContainer.heightProperty()))).divide(2).subtract(bottomPlayerMessageLabel.heightProperty().divide(2)).negate());
	}
	
	
	/**
	 * Starts a new game by setting variables and building the GUI
	 * @param game The new checkers game to start
	 */
	public void newGame(Checkers game) {
		
		// Set title of window to game variant
		((Stage)root.getScene().getWindow()).setTitle(game.getVariant().getName());
		
		// Declare the new game value
		this.game = game;
		
		// Assign values to variables
		selected = false;
		selectedPos = new Position();
		
		// Create a new board
		boardContainer.getChildren().clear();
		board = new BoardGraphic(game);
		boardContainer.getChildren().add(board);
		
		// Set up mouse click event listeners for each square
		for (int i = 0; i < game.getVariant().getRows(); i++) {
			for (int j = 0; j < game.getVariant().getCols(); j++) {
				board.getSpace(i, j).setOnMouseClicked(new ClickSpaceHandler());
				board.getSpace(i, j).setOnDragDetected(new DragDetectedSpaceHandler());
				board.getSpace(i, j).setOnMouseDragEntered(new DragEnteredSpaceHandler());
				board.getSpace(i, j).setOnMouseDragExited(new DragExitedSpaceHandler());
				board.getSpace(i, j).setOnMouseDragReleased(new DragReleasedSpaceHandler());
			}
		}
		boardContainer.setOnMouseDragOver(new DragOverSpaceHandler());
		root.setOnMouseReleased(new DragReleasedHandler());
		
		// Set appropriate keys to player labels and buttons
		messageLabels.put(Board.getTopPlayerType(), topPlayerMessageLabel);
		messageLabels.put(Board.getBottomPlayerType(), bottomPlayerMessageLabel);
		endTurnButtons.put(Board.getTopPlayerType(), topPlayerEndTurnButton);
		endTurnButtons.put(Board.getBottomPlayerType(), bottomPlayerEndTurnButton);
		
		// Set appropriate check value for check menu item
		highlightMovesMenuItem.setSelected(highlightMoves);
		
		// Highlight movable pieces
		updatePlayer();
	}
	
	
	
	/**
	 * Highlights pieces that can move or spaces a piece can move to depending
	 * on whether or not a piece is selected
	 */
	private void highlightMoves() {
		if (selected) {
			highlightMovableSpaces();
		}
		else {
			highlightMovablePieces();
		}
	}
	
	
	
	/**
	 * Highlights all pieces that can move.
	 */
	private void highlightMovablePieces() {
		if (!highlightMoves ||
				game.getCurPlayer() == game.topPlayer() && topPlayerComputer ||
				game.getCurPlayer() == game.bottomPlayer() && bottomPlayerComputer)
			return;
		
		for (Piece piece: game.movablePieces()) {
			int row = game.getBoard().getPosition(piece).getRow();
			int col = game.getBoard().getPosition(piece).getCol();

			// Add highlight to movable piece
			board.getPiece(row, col).highlight();
			board.activateSpace(row, col);
		}
	}
	
	
	
	/**
	 * Highlights all spaces a piece can move to.
	 * @param pieceRow The row of the piece
	 * @param pieceCol The column of the piece
	 */
	private void highlightMovableSpaces() {
		if (!highlightMoves) return;
		
		Piece piece = game.getBoard().getPiece(selectedPos);
		for (Move move : game.validMoves(piece)) {
			int row = move.getToPos().getRow();
			int col = move.getToPos().getCol();
			
			// Add ghost piece to indicate movable space
			board.setPiece(row, col, new GhostPieceGraphic(board.getSquareSize()));
			board.activateSpace(row, col);
		}
	}
	
	
	
	/**
	 * Remove highlight effect from any highlighted pieces or spaces
	 */
	private void removeHighlight() {
		for (int i = 0; i < game.getVariant().getRows(); i++) {
			for (int j = 0; j < game.getVariant().getCols(); j++) {
				
				// Remove ghost piece
				if (board.getPiece(i, j) != null && board.getPiece(i, j) instanceof GhostPieceGraphic) {
					board.removePiece(i, j);
					board.deactivateSpace(i, j);
				}
				
				// Remove highlight from piece
				else if (board.getPiece(i, j) != null && board.getPiece(i, j).isHighlighted()) {
					board.getPiece(i, j).deactivate();
					board.deactivateSpace(i, j);
				}
			}
		}
	}
	
	
	/**
	 * Toggles whether or not moves are highlighted
	 */
	private void toggleHighlightMoves() {
		highlightMoves = !highlightMoves;
		if (highlightMoves) {
			highlightMoves();
		}
		if (!highlightMoves) {
			removeHighlight();
		}
	}
	
	
	
	/**
	 * Selects the piece at the given position.
	 * @param row The row of the piece
	 * @param col The column of the piece
	 */
	private void selectPiece(int row, int col) {
		
		// Set the selected row and column
		selectedPos.setRow(row);
		selectedPos.setCol(col);
		selected = true;
		
		// Remove any existing effects from all the pieces
		for (int i = 0; i < game.getVariant().getRows(); i++) {
			for (int j = 0; j < game.getVariant().getCols(); j++) {
				if (board.getPiece(i, j) != null && board.getPiece(i, j).isActive()) {
					board.getPiece(i, j).deactivate();
					board.deactivateSpace(i, j);
				}
			}
		}
		
		// Select the piece to select
		if (game.canMove(game.getBoard().getPiece(row, col))) {
			board.getPiece(row, col).select();
		}
		else if (board.getPiece(row, col) != null && board.getPiece(row, col).isActive()) {
			board.getPiece(row, col).deactivate();
			board.deactivateSpace(row, col);
		}
	}
	
	
	
	/**
	 * Deselects any selected pieces.
	 */
	private void deselect() {
		
		// Remove selected position
		selected = false;
		
		// Remove any effects and ghost pieces
		for (int i = 0; i < game.getVariant().getRows(); i++) {
			for (int j = 0; j < game.getVariant().getCols(); j++) {
				
				// Remove ghost piece
				if (board.getPiece(i, j) != null && board.getPiece(i, j) instanceof GhostPieceGraphic) {
					board.removePiece(i, j);
					board.deactivateSpace(i, j);
				}
				
				// Remove effect from piece
				else if (board.getPiece(i, j) != null && board.getPiece(i, j).isActive()) {
					board.getPiece(i, j).deactivate();
					board.deactivateSpace(i, j);
				}
			}
		}
	}
	
	
	/**
	 * Moves a piece on the game board and executes what needs to be done
	 * following that
	 * @param move The move
	 */
	private void move(Move move) {
		move(move.getFromPos().getRow(), move.getFromPos().getCol(),
				move.getToPos().getRow(), move.getToPos().getCol());
	}
	
	
	/**
	 * Moves a piece on the game board and executes what needs to be done
	 * following that
	 * @param fromRow The row from which the piece is to be moved
	 * @param fromCol The column from which the piece is to be moved
	 * @param toRow The row to which the piece is to be moved
	 * @param toCol The column to which the piece is to be moved
	 */
	private void move(int fromRow, int fromCol, int toRow, int toCol) {
		
		// Get the piece to move
		Piece piece = game.getBoard().getPiece(fromRow, fromCol);
		
		
		// Move the piece
		game.move(piece, toRow, toCol);
		
		
		// Crown the piece if the piece is now a king
		if (piece.isKing()) {
			board.getPiece(toRow, toCol).crown();
		}
		
		
		// If the game is over...
		if (game.isOver()) {
			// Display message 
			displayMessage();
			// Hide end turn buttons
			for (Button endTurnButton : endTurnButtons.values()) {
				endTurnButton.setVisible(false);
			}
		}
		
		// Or if the player can make another capture, indicate that
		else if (game.canCapture(piece)) {
			selectPiece(toRow, toCol);
			highlightMovableSpaces();
		}
		
		// Otherwise, the next player's movable pieces
		else {
			updatePlayer();
		}
		
		
		// If huffing is enforced and captured, display the end turn button
		if (game.getVariant().isHuffing() && game.hasCaptured()) {
			endTurnButtons.get(piece.getPlayerType()).setVisible(true);
		}
		
		// If huffing is enforced and not captured, go to next player
		else if (game.getVariant().isHuffing()) {
			endTurn();
		}
		
		
		
		// --- Serialize ---
		game.serialize();
	}
	
	
	/**
	 * Updates the GUI to show the next player's turn
	 */
	private void updatePlayer() {
		// Display message
		displayMessage();
		
		// Hide end turn buttons
		for (Button endTurnButton : endTurnButtons.values()) {
			endTurnButton.setVisible(false);
		}
		
		// If player is computer, move for it
		if (game.getCurPlayer() == game.topPlayer() && topPlayerComputer ||
				game.getCurPlayer() == game.bottomPlayer() && bottomPlayerComputer) {
			Platform.runLater(() -> {
				Move computerMove = ComputerPlayer.getMove(game);
				Animation moveAnimation = moveAnimation(computerMove);
				moveAnimation.setOnFinished(e -> {
					move(computerMove);
				});
				moveAnimation.play();
			});
		}
		
		// Highlight movable pieces
		deselect();
		highlightMovablePieces();
	}
	
	
	/**
	 * Displays an appropriate message regarding the current player or state of
	 * the game
	 */
	private void displayMessage() {
		for (PlayerType playerType : PlayerType.values()) {
			Label messageLabel = messageLabels.get(playerType);

			// Display on player labels
			Platform.runLater(() -> {
				messageLabel.setGraphic(new PieceGraphic(new Piece(
						game.getCurPlayer().getType().manPiece()),
						messageLabel.getFont().getSize() * MESSAGE_GRAPHIC_SIZE));
			});
			
			if (game.isOver()) {
				Player winner = game.winner();
				if (winner == null) {
					messageLabel.setText("It is a draw.");
				}
				
				else if (playerType == winner.getType()) {
					messageLabel.setText("You win!");
				}
				
				else {
					messageLabel.setText("You lose.");
				}
			}
			
			else if (playerType == game.getCurPlayer().getType()) {
				messageLabel.setText("Your turn");
			}
			
			else {
				messageLabel.setText("Opponent's turn");
			}
		}
	}
	
	
	/**
	 * Moves to the next player. This is only really called for variants in
	 * which huffing is enforced as other variants automatically move to the
	 * next player after a turn is over. If a piece did not capture when it was
	 * supposed to, this shows the animation of that piece being removed
	 */
	private void endTurn() {
		if (!game.getVariant().isHuffing() || !game.hasMoved()) return;
		
		if (game.missedCapture()) {
			Position piecePosition = game.getBoard().getPosition(game.missedCapturePiece());
			int pieceRow = piecePosition.getRow();
			int pieceCol = piecePosition.getCol();
			removeAnimation(pieceRow, pieceCol);
		}
		else {
			delayedCaptureAnimation().play();
		}
		
		game.endTurn();
		
		updatePlayer();
	}
	
	
	
	/**
	 * Returns an animation showing the player's move.
	 * @param move The move
	 * @return The animation
	 */
	private Animation moveAnimation(Move move) {
		return moveAnimation(move.getFromPos().getRow(), move.getFromPos().getCol(),
				move.getToPos().getRow(), move.getToPos().getCol());
	}
	
	
	
	/**
	 * Returns an animation showing the player's move.
	 * @param fromRow The row from which the piece is to be moved
	 * @param fromCol The column from which the piece is to be moved
	 * @param toRow The row to which the piece is to be moved
	 * @param toCol The column to which the piece is to be moved
	 * @return The animation
	 */
	private Animation moveAnimation(int fromRow, int fromCol, int toRow, int toCol) {
		
		// Get player piece
		Piece piece = game.getBoard().getPiece(fromRow, fromCol);
		
		
		// Create transition to hold all the transitions
		Transition trans;
		ObservableList<Animation> children;
		if (game.getVariant().isRemovePiecesImmediately()) {
			ParallelTransition ptrans = new ParallelTransition();
			trans = ptrans;
			children = ptrans.getChildren();
		}
		else {
			SequentialTransition strans = new SequentialTransition();
			trans = strans;
			children = strans.getChildren();
		}
		

		// Get the graphic for the player piece
		PieceGraphic playerPiece = board.getPiece(fromRow, fromCol);
		
		// Create a temporary player piece to be used for the animation
		PieceGraphic tempPlayerPiece = new PieceGraphic(piece, board.getSquareSize());
		StackPane.setAlignment(tempPlayerPiece, Pos.TOP_LEFT);
		
		// Create a translate transition to show the piece moving
		TranslateTransition ttrans = new TranslateTransition(
				Duration.millis(MOVE_DURATION),
				tempPlayerPiece);
		ttrans.setFromX(getPieceX(fromRow, fromCol));
		ttrans.setFromY(getPieceY(fromRow, fromCol));
		ttrans.setToX(getDestinationX(fromRow, fromCol, toRow, toCol));
		ttrans.setToY(getDestinationY(fromRow, fromCol, toRow, toCol));
		children.add(ttrans);
		
		// Remove the temporary piece and add real piece when finished
		ttrans.setOnFinished(e -> {
			board.setPiece(toRow, toCol, playerPiece);
			boardContainer.getChildren().remove(tempPlayerPiece);
		});
		

		// Create an animation showing a capture if applicable
		if (game.isValidCapture(piece, toRow, toCol) && game.getVariant().isRemovePiecesImmediately()) {
			children.add(immediateCaptureAnimation(fromRow, fromCol, toRow, toCol));
		}
		else if (game.isValidCapture(piece, toRow, toCol) && !game.canMultiCapture(piece) && !game.getVariant().isHuffing()) {
			Piece capturedPiece = null;
			for (Capture capture : game.validCaptures(piece)) {
				if (capture.getToPos().getRow() == toRow && capture.getToPos().getCol() == toCol) {
					Position curCapturedPos = capture.getCapturePos();
					capturedPiece = game.getBoard().getPiece(curCapturedPos);
					break;
				}
			}
			children.add(delayedCaptureAnimation(capturedPiece));
		}
		
		
		// Add the temporary player piece and remove the real piece
		boardContainer.getChildren().add(tempPlayerPiece);
		board.removePiece(fromRow, fromCol);
		
		
		return trans;
	}
	

	/**
	 * Creates and returns an animation showing non-immediate capture
	 * @return The animation
	 */
	private Animation delayedCaptureAnimation() {
		return delayedCaptureAnimation(null);
	}
	
	
	/**
	 * Creates and returns an animation showing non-immediate capture
	 * @param A piece in addition to previously captured pieces to show captured
	 * @return The animation
	 */
	private Animation delayedCaptureAnimation(Piece curCapturedPiece) {
		
		// Create parallel transition to hold both disappearing and going up
		ParallelTransition ptrans = new ParallelTransition();
		
		// Get captured pieces
		List<Piece> capturedPieces = new ArrayList<>(game.getCapturedPieces());
		if (curCapturedPiece != null) {
			capturedPieces.add(curCapturedPiece);
		}
		
		// Create an animation for each piece
		for (int i = 0; i < capturedPieces.size(); i++) {
			
			// Get the position of the captured piece
			Position capturedPos = game.getBoard().getPosition(capturedPieces.get(i));
			int capturedRow = capturedPos.getRow();
			int capturedCol = capturedPos.getCol();
			
			// Create a temporary opponent piece to be used for the animation
			PieceGraphic tempOpponentPiece = new PieceGraphic(
					game.getBoard().getPiece(capturedRow, capturedCol),
					board.getSquareSize());
			StackPane.setAlignment(tempOpponentPiece, Pos.TOP_LEFT);
			tempOpponentPiece.setTranslateX(getPieceX(capturedRow, capturedCol));
			tempOpponentPiece.setTranslateY(getPieceY(capturedRow, capturedCol));
			
			// Create the fade transition to show the piece disappearing
			FadeTransition ftrans = new FadeTransition(
					Duration.millis(REMOVE_DURATION),
					tempOpponentPiece);
			ftrans.setDelay(Duration.millis(CAPTURE_DELAY * i));
			ftrans.setToValue(0.0);
			ptrans.getChildren().add(ftrans);
			
			// Create the scale transition to show the piece going up
			ScaleTransition strans = new ScaleTransition(
					Duration.millis(REMOVE_DURATION),
					tempOpponentPiece);
			strans.setDelay(Duration.millis(CAPTURE_DELAY * i));
			strans.setByX(REMOVE_SCALE);
			strans.setByY(REMOVE_SCALE);
			ptrans.getChildren().add(strans);
			
			// Remove the temporary piece when finished
			ftrans.setOnFinished(e -> {
				boardContainer.getChildren().remove(tempOpponentPiece);
			});
			
			
			// Add the temporary opponent piece and remove the real piece
			boardContainer.getChildren().add(tempOpponentPiece);
			board.removePiece(capturedRow, capturedCol);
		}
		
		return ptrans;
	}
	
	
	/**
	 * Creates and returns an animation showing immediate capture
	 * @param fromRow The row from which the piece is to be moved
	 * @param fromCol The column from which the piece is to be moved
	 * @param toRow The row to which the piece is to be moved
	 * @param toCol The column to which the piece is to be moved
	 * @return The animation
	 */
	private Animation immediateCaptureAnimation(int fromRow, int fromCol, int toRow, int toCol) {
		
		// Get player piece
		Piece piece = game.getBoard().getPiece(fromRow, fromCol);

		
		// Look for current capture piece and position
		Piece capturedPiece = null;
		for (Capture capture : game.validCaptures(piece)) {
			if (capture.getToPos().getRow() == toRow && capture.getToPos().getCol() == toCol) {
				Position curCapturedPos = capture.getCapturePos();
				capturedPiece = game.getBoard().getPiece(curCapturedPos);
				break;
			}
		}
		
		// Create parallel transition to hold both disappearing and going up
		ParallelTransition ptrans = new ParallelTransition();
			
		// Get the position of the captured piece
		Position capturedPos = game.getBoard().getPosition(capturedPiece);
		int capturedRow = capturedPos.getRow();
		int capturedCol = capturedPos.getCol();
		
		// Create a temporary opponent piece to be used for the animation
		PieceGraphic tempOpponentPiece = new PieceGraphic(
				game.getBoard().getPiece(capturedRow, capturedCol),
				board.getSquareSize());
		StackPane.setAlignment(tempOpponentPiece, Pos.TOP_LEFT);
		tempOpponentPiece.setTranslateX(getPieceX(capturedRow, capturedCol));
		tempOpponentPiece.setTranslateY(getPieceY(capturedRow, capturedCol));
		
		// Determine the duration of the transition
		double duration = MOVE_DURATION;
		if (game.getVariant().isRemovePiecesImmediately()) {
			double captureDistanceProportion, moveDistance, moveDistanceX, moveDistanceY, captureDistance, captureDistanceX, captureDistanceY;
			moveDistanceX = Math.abs(getDestinationX(fromRow, fromCol, toRow, toCol) - getPieceX(fromRow, fromCol));
			moveDistanceY = Math.abs(getDestinationY(fromRow, fromCol, toRow, toCol) - getPieceY(fromRow, fromCol));
			moveDistance = Math.sqrt(Math.pow(moveDistanceX, 2) + Math.pow(moveDistanceY, 2));
			captureDistanceX = Math.abs(getPieceX(capturedRow, capturedCol) - getPieceX(fromRow, fromCol));
			captureDistanceY = Math.abs(getPieceY(capturedRow, capturedCol) - getPieceY(fromRow, fromCol));
			captureDistance = Math.sqrt(Math.pow(captureDistanceX, 2) + Math.pow(captureDistanceY, 2));
			captureDistanceProportion = captureDistance / moveDistance;
			duration *= captureDistanceProportion;
		}
		
		// Create the fade transition to show the piece disappearing
		FadeTransition ftrans = new FadeTransition(
				Duration.millis(REMOVE_DURATION),
				tempOpponentPiece);
		ftrans.setDelay(Duration.millis(duration));
		ftrans.setToValue(0.0);
		ptrans.getChildren().add(ftrans);
		
		// Create the scale transition to show the piece going up
		ScaleTransition strans = new ScaleTransition(
				Duration.millis(REMOVE_DURATION),
				tempOpponentPiece);
		strans.setDelay(Duration.millis(duration));
		strans.setByX(REMOVE_SCALE);
		strans.setByY(REMOVE_SCALE);
		ptrans.getChildren().add(strans);
		
		// Remove the temporary piece when finished
		ftrans.setOnFinished(e -> {
			boardContainer.getChildren().remove(tempOpponentPiece);
		});
		
		
		// Add the temporary opponent piece and remove the real piece
		boardContainer.getChildren().add(tempOpponentPiece);
		board.removePiece(capturedRow, capturedCol);
		
		return ptrans;
	}
	
	
	/**
	 * Shows an animation of a piece being removed
	 * @param row The row of the piece
	 * @param col The column of the piece
	 */
	private void removeAnimation(int row, int col) {
		
		// Create parallel transition to hold both transitions
		ParallelTransition ptrans = new ParallelTransition();
		
		// Create a temporary opponent piece to be used for the animation
		PieceGraphic tempPiece = new PieceGraphic(
				game.getBoard().getPiece(row, col),
				board.getSquareSize());
		StackPane.setAlignment(tempPiece, Pos.TOP_LEFT);
		tempPiece.setTranslateX(getPieceX(row, col));
		tempPiece.setTranslateY(getPieceY(row, col));
		
		// Create the fade transition to show the piece disappearing
		FadeTransition ftrans = new FadeTransition(
				Duration.millis(REMOVE_DURATION),
				tempPiece);
		ftrans.setToValue(0.0);
		ptrans.getChildren().add(ftrans);
		
		// Create the scale transition to show the piece going up
		ScaleTransition strans = new ScaleTransition(
				Duration.millis(REMOVE_DURATION),
				tempPiece);
		strans.setByX(REMOVE_SCALE);
		strans.setByY(REMOVE_SCALE);
		ptrans.getChildren().add(strans);
		
		// Remove the temporary piece when finished
		ftrans.setOnFinished(e -> {
			boardContainer.getChildren().remove(tempPiece);
		});
		
		// Add the temporary opponent piece and remove the real piece
		boardContainer.getChildren().add(tempPiece);
		board.removePiece(row, col);
		
		// Play the animations
		ptrans.play();
	}
	
	
	/**
	 * Updates the scene to show the current state of the game
	 */
	private void refresh() {
		// Update the pieces at each space
		for (int i = 0; i < game.getBoard().getRows(); i++) {
			for (int j = 0; j < game.getBoard().getCols(); j++) {
				Piece piece = game.getBoard().getPiece(i, j);
				if (piece == null) {
					board.removePiece(i, j);
				}
				else {
					board.setPiece(i, j, new PieceGraphic(piece, board.getSquareSize()));
				}
			}
		}
	}
	
	
	/**
	 * Returns the x coordinate of the space at the given position
	 * @param row The row of the space
	 * @param col The column of the space
	 * @return The x coordinate
	 */
	private double getSpaceX(int row, int col) {
		return	board.getLayoutX() +
				board.getSpace(row, col).getLayoutX();
	}
	
	
	/**
	 * Returns the y coordinate of the space at the given position
	 * @param row The row of the space
	 * @param col The column of the space
	 * @return The y coordinate
	 */
	private double getSpaceY(int row, int col) {
		return	board.getLayoutY() +
				board.getSpace(row, col).getLayoutY();
	}
	
	
	/**
	 * Returns the x coordinate of the piece at the given position
	 * @param row The row of the piece
	 * @param col The column of the piece
	 * @return The x coordinate
	 */
	private double getPieceX(int row, int col) {
		return	board.getLayoutX() +
				board.getSpace(row, col).getLayoutX() +
				board.getPiece(row, col).getLayoutX();
	}
	
	
	/**
	 * Returns the y coordinate of the piece at the given position
	 * @param row The row of the piece
	 * @param col The column of the piece
	 * @return The y coordinate
	 */
	private double getPieceY(int row, int col) {
		return	board.getLayoutY() +
				board.getSpace(row, col).getLayoutY() +
				board.getPiece(row, col).getLayoutY();
	}
	
	
	/**
	 * Returns the x coordinate of the position a piece should move to
	 * @param fromRow The row of the piece
	 * @param fromCol The column of the piece
	 * @param toRow The row of the destination
	 * @param toCol The column of the destination
	 * @return The x coordinate
	 */
	private double getDestinationX(int fromRow, int fromCol, int toRow, int toCol) {
		return	board.getLayoutX() +
				board.getSpace(toRow, toCol).getLayoutX() +
				board.getPiece(fromRow, fromCol).getLayoutX();
	}
	
	
	/**
	 * Returns the y coordinate of the position a piece should move to
	 * @param row The row of the piece
	 * @param col The column of the piece
	 * @return The y coordinate
	 */
	private double getDestinationY(int fromRow, int fromCol, int toRow, int toCol) {
		return	board.getLayoutY() +
				board.getSpace(toRow, toCol).getLayoutY() +
				board.getPiece(fromRow, fromCol).getLayoutY();
	}
	
	
	/**
	 * Contains what to do when the a player clicks on a space. If nothing has
	 * been selected yet, it selects the piece. If a piece has been selected and
	 * the user clicks on a valid move, it moves the piece. Otherwise,
	 * everything reverts to an unselected state.
	 * @author Alex Gill
	 *
	 */
	private class ClickSpaceHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			
			// Do nothing if the game is over or a dragged move is ending
			if (game.isOver() || !event.isStillSincePress())
				return;
			
			// Get the piece of the selected row and column
			Piece selectedPiece = null;
			if (selected) {
				selectedPiece = game.getBoard().getPiece(
						selectedPos.getRow(), selectedPos.getCol());
			}
			
			// Get the row and column of the clicked space
			int row = GridPane.getRowIndex((Node)event.getSource());
			int col = GridPane.getColumnIndex((Node)event.getSource());
			Piece piece = game.getBoard().getPiece(row, col);
			
			
			// If selected and clicked on an invalid square, deselect it.
			if (selected && !game.isValidMove(selectedPiece, row, col)) {
				deselect();
				highlightMovablePieces();
			}
			
			
			// If a piece is not selected and is player piece, select the piece.
			else if (!selected && piece != null && game.canMove(piece)) {
				selectPiece(row, col);
				highlightMovableSpaces();
			}
			
			
			// If a piece is already selected, move it to the space
			else if (selected) {
				deselect();
				Animation moveAnimation = moveAnimation(selectedPos.getRow(), selectedPos.getCol(), row, col);
				moveAnimation.setOnFinished(e -> {
					move(selectedPos.getRow(), selectedPos.getCol(), row, col);
				});
				moveAnimation.play();
			}
		}
	}
	
	
	/**
	 * Allows a space's piece to be dragged and placed on a new space
	 * @author Alex Gill
	 *
	 */
	private class DragDetectedSpaceHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			
			// Do nothing if the game is over
			if (game.isOver())
				return;
			
			// Get the row and column of the clicked space
			int row = GridPane.getRowIndex((Node)event.getSource());
			int col = GridPane.getColumnIndex((Node)event.getSource());
			Piece piece = game.getBoard().getPiece(row, col);
			
			// Do nothing if invalid space
			if (piece == null || piece.getPlayerType() != game.getCurPlayer().getType() ||
					!game.canMove(piece))
				return;
			
			// Show moves
			deselect();
			selectedPos = new Position(row, col);
			highlightMovableSpaces();
			
			// Create a temporary player piece to be used for the drag
			dragPiece = new PieceGraphic(piece, board.getSquareSize());
			StackPane.setAlignment(dragPiece, Pos.TOP_LEFT);
			dragPiece.setTranslateX(getSpaceX(row, col) + event.getX() - dragPiece.getRadius());
			dragPiece.setTranslateY(getSpaceY(row, col) + event.getY() - dragPiece.getRadius());
			boardContainer.getChildren().add(dragPiece);
			draggedPiece = board.getPiece(row, col);
			board.removePiece(row, col);
			
			// Start the drag
			dragPiece.startFullDrag();
			dragPiece.setMouseTransparent(true);
			dragging = true;
		}
	}
	
	
	/**
	 * Causes the dragged piece to be moved
	 * @author Alex Gill
	 *
	 */
	private class DragOverSpaceHandler implements EventHandler<MouseDragEvent> {
		@Override
		public void handle(MouseDragEvent event) {
			dragPiece.setTranslateX(event.getX() - dragPiece.getRadius());
			dragPiece.setTranslateY(event.getY() - dragPiece.getRadius());
		}
	}
	
	
	/**
	 * Adds a style class to the hovered over square
	 * @author Alex Gill
	 *
	 */
	private class DragEnteredSpaceHandler implements EventHandler<MouseDragEvent> {
		@Override
		public void handle(MouseDragEvent event) {
			int row = GridPane.getRowIndex((Node)event.getSource());
			int col = GridPane.getColumnIndex((Node)event.getSource());
			board.getSquare(row, col).getStyleClass().add("hovered");
		}
	}
	
	
	/**
	 * Removes a style class from the hovered over square
	 * @author Alex Gill
	 *
	 */
	private class DragExitedSpaceHandler implements EventHandler<MouseDragEvent> {
		@Override
		public void handle(MouseDragEvent event) {
			int row = GridPane.getRowIndex((Node)event.getSource());
			int col = GridPane.getColumnIndex((Node)event.getSource());
			board.getSquare(row, col).getStyleClass().remove("hovered");
		}
	}
	
	
	/**
	 * Moves the dragged piece to the currently hovered over space
	 * @author Alex Gill
	 *
	 */
	private class DragReleasedSpaceHandler implements EventHandler<MouseDragEvent> {
		@Override
		public void handle(MouseDragEvent event) {
			
			// Get the row and column of the clicked space
			int row = GridPane.getRowIndex((Node)event.getSource());
			int col = GridPane.getColumnIndex((Node)event.getSource());
			
			
			// If released on an invalid square, move it back to where it was
			if (!game.isValidMove(dragPiece.getPiece(), row, col)) {
				board.setPiece(selectedPos.getRow(), selectedPos.getCol(), draggedPiece);
				
			}
			
			
			// Otherwise, move it
			else {
				if (game.isValidCapture(draggedPiece.getPiece(), row, col) &&
						game.getVariant().isRemovePiecesImmediately()) {
					for (Capture capture : game.validCaptures(draggedPiece.getPiece())) {
						if (capture.getToPos().getRow() == row && capture.getToPos().getCol() == col) {
							Position capturedPos = capture.getCapturePos();
							removeAnimation(capturedPos.getRow(), capturedPos.getCol());
							break;
						}
					}
				}
				else if (game.isValidCapture(draggedPiece.getPiece(), row, col) &&
						!game.canMultiCapture(draggedPiece.getPiece()) &&
						!game.getVariant().isHuffing()) {
					for (Capture capture : game.validCaptures(draggedPiece.getPiece())) {
						if (capture.getToPos().getRow() == row && capture.getToPos().getCol() == col) {
							Position capturedPos = capture.getCapturePos();
							delayedCaptureAnimation(game.getBoard().getPiece(capturedPos)).play();
							break;
						}
					}
				}
				deselect();	// So that ghost piece doesn't stay
				board.setPiece(row, col, draggedPiece);
				boardContainer.getChildren().remove(dragPiece);
				move(selectedPos.getRow(), selectedPos.getCol(), row, col);
			}

			boardContainer.getChildren().remove(dragPiece);
			dragging = false;
			
			// Show moves
			deselect();
			highlightMovablePieces();
		}
	}
	
	
	/**
	 * Removes the dragged piece if released outside of board
	 * @author Alex Gill
	 *
	 */
	private class DragReleasedHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (!dragging) return;
			boardContainer.getChildren().remove(dragPiece);
			board.setPiece(selectedPos.getRow(), selectedPos.getCol(), draggedPiece);
			dragging = false;
			
			// Show moves
			deselect();
			highlightMovablePieces();
		}
	}
	
	
	
	// --- Menu bar ---
	
	@FXML
	private void newGame(ActionEvent event) {
		newGame();
	}
	
	@FXML
	private void restartGame(ActionEvent event) {
		restartGame();
	}
	
	@FXML
	private void showInfo(ActionEvent event) {
		showInfo();
	}
	
	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}
	
	@FXML
	private void undoMove(ActionEvent event) {
		undoMove();
	}
	
	@FXML
	private void undoAllMoves(ActionEvent event) {
		undoAllMoves();
	}
	
	@FXML
	private void redoMove(ActionEvent event) {
		redoMove();
	}
	
	@FXML
	private void redoAllMoves(ActionEvent event) {
		redoAllMoves();
	}
	
	@FXML
	private void invertBoard(ActionEvent event) {
		invertBoard();
	}
	
	@FXML
	private void toggleHighlightMoves(ActionEvent event) {
		toggleHighlightMoves();
	}
	
	
	
	// --- Misc. ---
	
	@FXML
	private void endTurn(ActionEvent event) {
		endTurn();
	}
	
	
	
	// --- Private methods ---
	
	/**
	 * Opens the new game dialog
	 */
	private void newGame() {
		Optional<Checkers> result = new NewGameDialog().showAndWait();
		if (result.isPresent()) {
			newGame(result.get());
		}
	}
	
	/**
	 * Starts a new game of the current variant
	 */
	private void restartGame() {
		Checkers newGame = new Checkers(game.getVariant());
		
		// Show dialog to choose first player if starting player is either
		if (game.getVariant().getStartingPlayer() == StartingPlayer.EITHER) {
			PlayerType startingPlayer;
			Optional<PlayerType> startingPlayerResult = new StartingPlayerDialog().showAndWait();
			if (startingPlayerResult.isPresent()) {
				startingPlayer = startingPlayerResult.get();
				game.setStartingPlayer(startingPlayer);
			}
			else {
				return;
			}
		}
		
		newGame(newGame);
	}
	
	/**
	 * Shows the info of the current variant
	 */
	private void showInfo() {
		new VariantInfoWindow(game.getVariant()).showAndWait();
	}
	
	/**
	 * Undoes the last move and updates the board to reflect it
	 */
	private void undoMove() {
		deselect();
		game.undoMove();
		refresh();
		updatePlayer();
	}
	
	/**
	 * Undoes all moves and updates the board to reflect it
	 */
	private void undoAllMoves() {
		deselect();
		game.undoAllMoves();
		refresh();
		updatePlayer();
	}
	
	/**
	 * Goes forward a move and updates the board to reflect it
	 */
	private void redoMove() {
		deselect();
		game.redoMove();
		refresh();
		updatePlayer();
	}
	
	/**
	 * Goes forward to the last move and updates the board to reflect it
	 */
	private void redoAllMoves() {
		deselect();
		game.redoAllMoves();
		refresh();
		updatePlayer();
	}
	
	
	
	// --- Public methods ---
	
	/**
	 * @return Whether possible moves are highlighted
	 */
	public static boolean getHighlightMoves() {
		return highlightMoves;
	}
	
	
	/**
	 * Sets whether to highlight possible moves to the players
	 * @param highlight Whether to highlight possible moves
	 */
	public static void setHighlightMoves(boolean highlight) {
		highlightMoves = highlight;
	}
	
	
	/**
	 * @return the topPlayerComputer
	 */
	public static boolean isTopPlayerComputer() {
		return topPlayerComputer;
	}


	/**
	 * @param topPlayerComputer the topPlayerComputer to set
	 */
	public static void setTopPlayerComputer(boolean topPlayerComputer) {
		GameRootController.topPlayerComputer = topPlayerComputer;
	}


	/**
	 * @return the bottomPlayerComputer
	 */
	public static boolean isBottomPlayerComputer() {
		return bottomPlayerComputer;
	}


	/**
	 * @param bottomPlayerComputer the bottomPlayerComputer to set
	 */
	public static void setBottomPlayerComputer(boolean bottomPlayerComputer) {
		GameRootController.bottomPlayerComputer = bottomPlayerComputer;
	}


	/**
	 * Swaps the top and bottom player, reflecting the board layout
	 */
	public void invertBoard() {
		deselect();
		game.getBoard().invert();
		board.invert();
		refresh();
		highlightMovablePieces();
		
		// Set appropriate keys to player labels and buttons
		messageLabels.put(Board.getTopPlayerType(), topPlayerMessageLabel);
		messageLabels.put(Board.getBottomPlayerType(), bottomPlayerMessageLabel);
		endTurnButtons.put(Board.getTopPlayerType(), topPlayerEndTurnButton);
		endTurnButtons.put(Board.getBottomPlayerType(), bottomPlayerEndTurnButton);
		
		// Display the updated message
		displayMessage();
		
		// Swap the end turn button visibilities
		boolean topEndTurnButtonVisible = topPlayerEndTurnButton.isVisible();
		boolean bottomEndTurnButtonVisible = bottomPlayerEndTurnButton.isVisible();
		topPlayerEndTurnButton.setVisible(bottomEndTurnButtonVisible);
		bottomPlayerEndTurnButton.setVisible(topEndTurnButtonVisible);
	}
}
