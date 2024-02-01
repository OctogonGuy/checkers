package tech.octopusdragon.checkers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import tech.octopusdragon.checkers.rules.KingType;

// Main resource: https://www.fmjd.org/downloads/Checkers_families_and_rules.pdf
// Special variants: https://en.wikipedia.org/wiki/Checkers
// Includes more rules/information: http://mlwi.magix.net/bg/
// Includes more variants: https://brainking.com/en/GameRules?tp=7
// Includes even more variants: https://www.mindsports.nl/index.php/on-the-evolution-of-draughts-variants/

/**
 * Simulates a game of checkers.
 * @author Alex Gill
 *
 */
public class Checkers {
	
	// --- Instance variables ---
	private Variant variant;			// The variant to play
	private Board board;				// The board
	private Player blackPlayer;			// The black player
	private Player whitePlayer;			// The white player
	private Player curPlayer;			// The current player
	private List<Piece> pieces;			// The pieces in the game
	private Piece capturingPiece;		// Piece in a capturing sequence
	private List<Piece> capturedPieces;	// Pieces captured so far this turn
	private AbsoluteDirection lastDir;	// Last direction moved this turn
	private BoardHistory history;		// History of previous of board states
	private int kingVKingTurnsLeft;		// The number of turns left until draw
	private boolean kingVKingStarted;	// Whether to count down the turns
	private Player higherKingPlayer;	// Player with more kings at count start
	private boolean missedCapture;		// Whether player missed capture
	private Piece missedCapturePiece;	// The piece that missed the capture
	private boolean moved;				// Whether current player has moved
	private boolean captured;			// Whether current player has captured
	
	
	/**
	 * Instantiates a game of checkers.
	 * @param variant The variant to play
	 */
	public Checkers(Variant variant) {
		
		// Instantiate the variant to play
		this.variant = variant;
		
		// Create the players
		blackPlayer = new Player(PlayerType.BLACK);
		whitePlayer = new Player(PlayerType.WHITE);
		
		// Create the board
		board = new Board(variant.getRows(), variant.getCols(), variant.getBoardPattern(),
				variant.getNumPieces(), variant.getStartingPositions());
		
		// Add the pieces on the board to the list of pieces
		pieces = new ArrayList<Piece>();
		for (int i = 0; i < variant.getRows(); i++) {
			for (int j = 0; j < variant.getCols(); j++) {
				Piece curPiece = board.getPiece(i, j);
				if (curPiece == null) continue;
				pieces.add(curPiece);
			}
		}
		
		// No pieces have moved yet
		capturingPiece = null;
		capturedPieces = new ArrayList<Piece>();
		lastDir = null;
		
		
		// Determine who goes first
		switch (variant.getStartingPlayer()) {
		case BLACK:
			curPlayer = blackPlayer;
			break;
		case WHITE:
			curPlayer = whitePlayer;
			break;
		case EITHER:
			curPlayer = null;
			break;
		}
		
		// Initialize board history
		if (curPlayer != null) {
			history = new BoardHistory();
			history.push(board, curPlayer);
		}
		
		
		// --- Special rules ---
		
		// Initialize kings vs kings rule information
		if (variant.hasKingVKingDrawRule()) {
			kingVKingTurnsLeft = variant.getKingVKingDrawRule().getNumTurns();
			kingVKingStarted = false;
		}
	}
	
	
	/**
	 * @return The checkers variant
	 */
	public Variant getVariant() {
		return variant;
	}
	
	
	/**
	 * @return The checker board being used for this game
	 */
	public Board getBoard() {
		return board;
	}
	
	
	/**
	 * @return A list of pieces captured so far this turn
	 */
	public List<Piece> getCapturedPieces() {
		return capturedPieces;
	}
	
	
	
	/**
	 * @param startingPlayerType The starting player type
	 */
	public void setStartingPlayer(PlayerType startingPlayerType) {
		switch (startingPlayerType) {
		case BLACK:
			curPlayer = blackPlayer;
			break;
		case WHITE:
			curPlayer = whitePlayer;
			break;
		}
		
		// Initialize board history
		history = new BoardHistory();
		history.push(board, curPlayer);
	}
	
	
	
	/**
	 * @return The current player
	 */
	public Player getCurPlayer() {
		return curPlayer;
	}
	
	
	/**
	 * @return The top player
	 */
	public Player getTopPlayer() {
		return topPlayer();
	}
	
	
	/**
	 * @return The bottom player
	 */
	public Player getBottomPlayer() {
		return bottomPlayer();
	}
	
	
	/**
	 * 
	 * @return Whether the last player missed capturing (can only be true for
	 * variants that enforce huffing)
	 */
	public boolean missedCapture() {
		return missedCapture;
	}
	
	
	/**
	 * 
	 * @return The piece that missed capturing
	 */
	public Piece missedCapturePiece() {
		return missedCapturePiece;
	}
	
	
	/**
	 * 
	 * @return The piece that is capturing
	 */
	public Piece capturingPiece() {
		return capturingPiece;
	}
	
	
	/**
	 * 
	 * @return Whether the current player has moved
	 */
	public boolean hasMoved() {
		return moved;
	}
	
	
	/**
	 * 
	 * @return Whether the current player has captured
	 */
	public boolean hasCaptured() {
		return captured;
	}
	
	
	/**
	 * 
	 * @return The top player
	 */
	public Player topPlayer() {
		return Board.getTopPlayerType() == PlayerType.BLACK ? blackPlayer : whitePlayer;
	}
	
	
	/**
	 * 
	 * @return The bottom player
	 */
	public Player bottomPlayer() {
		return Board.getBottomPlayerType() == PlayerType.BLACK ? blackPlayer : whitePlayer;
	}
	
	
	
	/**
	 * Advances the turn to the next player.
	 */
	private void nextPlayer() {
		if (curPlayer == topPlayer())
			curPlayer = bottomPlayer();
		else
			curPlayer = topPlayer();
		
		moved = false;
		captured = false;
		
		// Add this board state to the board history
		history.push(board, curPlayer);
	}
	
	
	/**
	 * Ends the current player's turn. Only applies to variants that enforce
	 * huffing. Does not work if the player has not moved yet.
	 */
	public void endTurn() {
		if (!variant.isHuffing()) return;
		
		// If huffing is enforced, and last player missed capture, remove the
		// last moved piece from the board
		if (missedCapture) {
			board.remove(missedCapturePiece);
			pieces.remove(missedCapturePiece);
		}
		missedCapture = false;
		missedCapturePiece = null;
		
		// Terminate sequence
		terminateSequence();
		
		// Next player
		nextPlayer();
	}
	
	
	
	/**
	 * @return Whether the game is over
	 */
	public boolean isOver() {
		return piecesOf(topPlayer()).isEmpty() || piecesOf(bottomPlayer()).isEmpty() ||
				numPieces(topPlayer()) <= variant.getMinPieces() || numPieces(bottomPlayer()) <= variant.getMinPieces() ||
				(!canMove(curPlayer) && !moved) || drawFromBoardRepeats() ||
				drawFromKingVKing() || drawFromKingVKingAutomatic();
	}
	
	
	/**
	 * @return The winner of the game if the game is over or null if a draw
	 */
	public Player winner() {
		Player winner = null;
		if (piecesOf(bottomPlayer()).isEmpty() ||
				!canMove(bottomPlayer()) ||
				numPieces(bottomPlayer()) <= variant.getMinPieces()) {
			winner = topPlayer();
		}
		else if (piecesOf(topPlayer()).isEmpty() ||
				!canMove(topPlayer()) ||
				numPieces(topPlayer()) <= variant.getMinPieces()) {
			winner = bottomPlayer();
		}
		return winner;
	}
	
	
	/**
	 * @param playerType The player type
	 * @return The player of the given type
	 */
	private Player player(PlayerType playerType) {
		Player player = null;
		switch (playerType) {
		case BLACK:
			player = blackPlayer;
			break;
		case WHITE:
			player = whitePlayer;
			break;
		}
		return player;
	}
	
	
	/**
	 * @param piece The piece to look up
	 * @return The player who owns the given piece
	 */
	private Player playerOf(Piece piece) {
		Player piecePlayer = null;
		switch (piece.getPlayerType()) {
		case BLACK:
			piecePlayer = blackPlayer;
			break;
		case WHITE:
			piecePlayer = whitePlayer;
			break;
		}
		return piecePlayer;
	}
	
	
	/**
	 * @param player The player to look up
	 * @return The pieces belonging to the given player
	 */
	private List<Piece> piecesOf(Player player) {
		List<Piece> playerPieces = new ArrayList<Piece>();
		for (int i = 0; i < variant.getRows(); i++) {
			for (int j = 0; j < variant.getCols(); j++) {
				Piece curPiece = board.getPiece(i, j);
				if (curPiece == null) continue;
				if ((player.getType() == PlayerType.BLACK && curPiece.isBlack()) ||
					(player.getType() == PlayerType.WHITE && curPiece.isWhite())) {
					playerPieces.add(curPiece);
				}
			}
		}
		return playerPieces;
	}
	
	
	/**
	 * 
	 * @param player The player to investigate
	 * @return The number of pieces the player has
	 */
	public int numPieces(Player player) {
		return piecesOf(player).size();
	}
	
	
	/**
	 * 
	 * @param player The player to investigate
	 * @return The pieces of the player which are men
	 */
	public List<Piece> menOf(Player player) {
		List<Piece> playerMen = new ArrayList<Piece>();
		for (Piece piece : piecesOf(player)) {
			if (piece.isMan()) {
				playerMen.add(piece);
			}
		}
		return playerMen;
	}
	
	
	/**
	 * 
	 * @param player The player to investigate
	 * @return The number of men the player has
	 */
	public int numMen(Player player) {
		return menOf(player).size();
	}
	
	
	/**
	 * 
	 * @param player The player to investigate
	 * @return Whether the player has any men
	 */
	public boolean hasMen(Player player) {
		return !menOf(player).isEmpty();
	}
	
	
	/**
	 * 
	 * @param player The player to investigate
	 * @return The pieces of the player which are kings
	 */
	public List<Piece> kingsOf(Player player) {
		List<Piece> playerKings = new ArrayList<Piece>();
		for (Piece piece : piecesOf(player)) {
			if (piece.isKing()) {
				playerKings.add(piece);
			}
		}
		return playerKings;
	}
	
	
	/**
	 * 
	 * @param player The player to investigate
	 * @return The number of kings the player has
	 */
	public int numKings(Player player) {
		return kingsOf(player).size();
	}
	
	
	/**
	 * 
	 * @param player The player to investigate
	 * @return Whether the player has any kings
	 */
	public boolean hasKings(Player player) {
		return !kingsOf(player).isEmpty();
	}
	
	
	/**
	 * Moves a piece.
	 * @param piece The piece to be moved
	 * @param toRow The row to which the piece is to be moved
	 * @param toCol The column to which the piece is to be moved
	*/
	public void move(Piece piece, int toRow, int toCol) {
		Position pos = board.getPosition(piece);
		int fromRow = pos.getRow();
		int fromCol = pos.getCol();
		move(fromRow, fromCol, toRow, toCol);
	}
	
	
	/**
	 * Moves a piece.
	 * @param fromRow The row from which the piece is to be moved
	 * @param fromCol The column from which the piece is to be moved
	 * @param toRow The row to which the piece is to be moved
	 * @param toCol The column to which the piece is to be moved
	*/
	public void move(int fromRow, int fromCol, int toRow, int toCol) {
		boolean terminateSequence = false;	// Whether to terminate the sequence
		boolean capture = false;			// Whether a capture was made
		
		// Get the piece to move
		Piece piece = board.getPiece(fromRow, fromCol);
		
		// Do nothing if this is not a valid move
		if (!isValidMove(piece, toRow, toCol))
			return;
		
		
		// In free capture, if the player can capture but does not capture, flag
		// missed capture
		if (canCapture(playerOf(piece)) && !isValidCapture(piece, toRow, toCol)) {
			missedCapture = true;
			missedCapturePiece = piece;
		}
		
		
		// Check if the move is a capture
		if (isValidCapture(piece, toRow, toCol)) {
			Piece capturedPiece = capturedPiece(piece, toRow, toCol);
			// If it should be removed immediately, remove a captured piece
			if (!variant.isRemovePiecesImmediately()) {
				capturedPieces.add(capturedPiece);
			}
			// Otherwise, just mark it to be removed
			else {
				pieces.remove(capturedPiece);
				board.remove(capturedPiece);
			}
			
			capture = true;
			captured = true;
			capturingPiece = piece;
			
			// Reset the piece's number moves in a row
			piece.resetConsecutiveMoves();
		}
		
		// Otherwise, terminate the sequence after moving
		else {
			terminateSequence = true;
		}
		
		
		// Record the direction the piece is moving in
		lastDir = board.absoluteDirection(piece, new Position(toRow, toCol));
		
		
		// Move the piece
		board.move(piece, toRow, toCol);
		moved = true;
		
		
		// If the piece captured but cannot make another capture, terminate the
		// sequence
		if (capture && !canCapture(piece)) {
			terminateSequence = true;
		}
		
		
		// If a man has reached the kings row and is in the middle of a
		// sequence, check to see which rule applies and act accordingly
		if (capture && canCapture(piece) && piece.isMan() && board.isInKingsRow(piece)) {
			switch (variant.getKingsRowCapture()) {
			// If stop, terminate sequence and become king
			case STOP:
				terminateSequence = true;
				piece.crown();
				break;
			// If skip, continue but do not become king
			case SKIP:
				break;
			// If adapt, continue and become king
			case ADAPT:
				piece.crown();
				break;
			}
		}
		
		// Otherwise, if the man has reached the kings row, just crown it
		else if (piece.isMan() && board.isInKingsRow(piece)) {
			piece.crown();
		}
		
		
		// If player's sequence has been terminated, clear the captured pieces,
		// if there are any, and move on to the next player
		if (terminateSequence && !variant.isHuffing()) {
			terminateSequence();
			
			// Go to the next player unless the game is over
			if (!isOver()) {
				nextPlayer();
			}
		}
	}
	
	
	/**
	 * Terminate a player's movement sequence
	 */
	private void terminateSequence() {
		for (Piece capturedPiece: capturedPieces) {
			pieces.remove(capturedPiece);
			board.remove(capturedPiece);
		}
		capturingPiece = null;
		capturedPieces.clear();
		lastDir = null;
		
		// --- Special rules ---
		
		// Check king vs king draw rule conditions and act accordingly
		if (variant.hasKingVKingDrawRule()) {
			// If the conditions are right, start the king vs king draw
			// countdown
			if (!kingVKingStarted && hasKingVKingCondition()) {
				kingVKingStarted = true;
				higherKingPlayer = curPlayer;
			}
			// Otherwise, count down turns if king vs king rule has started
			else if (kingVKingStarted && curPlayer == higherKingPlayer) {
				kingVKingTurnsLeft--;
			}
		}
	}
	
	
	/**
	 * Changes the current player and board layout
	 * @param state The state of the game board
	 */
	private void setState(BoardState state) {
		if (state == null) return;
		
		curPlayer = player(state.playerType());
		board = new Board(state, variant.getBoardPattern());
		
		// Add the pieces on the board to the list of pieces
		pieces.clear();
		for (int i = 0; i < variant.getRows(); i++) {
			for (int j = 0; j < variant.getCols(); j++) {
				Piece curPiece = board.getPiece(i, j);
				if (curPiece == null) continue;
				pieces.add(curPiece);
			}
		}
	}
	
	
	/**
	 * Undoes the last move made and restores the board to the state it was in
	 * last turn
	 */
	public void undoMove() {
		setState(history.previous());
	}
	
	
	/**
	 * Undoes all moves and restores the board to the state it was in at the
	 * beginning of the game
	 */
	public void undoAllMoves() {
		setState(history.first());
	}
	
	
	/**
	 * Goes forward a move and restores the board to the state it was in during
	 * that turn
	 */
	public void redoMove() {
		setState(history.next());
	}
	
	
	/**
	 * Goes forward all moves and restores the board to the state it was most
	 * recently in
	 */
	public void redoAllMoves() {
		setState(history.last());
	}
	

	/**
	 * Moves a piece (only to be used by the maximum capture method(s)
	 * @param fromRow The row from which the piece is to be moved
	 * @param fromCol The column from which the piece is to be moved
	 * @param toRow The row to which the piece is to be moved
	 * @param toCol The column to which the piece is to be moved
	*/
	private void maximumCaptureMove(int fromRow, int fromCol, int toRow, int toCol) {
		boolean terminateSequence = false;	// Whether to terminate the sequence
		
		// Get the piece to move
		Piece piece = board.getPiece(fromRow, fromCol);
		
		
		// Capture piece
		Piece thisCapturedPiece = capturedPiece(piece, toRow, toCol);
		// If it should be removed immediately, remove a captured piece
		if (!variant.isRemovePiecesImmediately()) {
			capturedPieces.add(thisCapturedPiece);
		}
		// Otherwise, just mark it to be removed
		else {
			pieces.remove(thisCapturedPiece);
			board.remove(thisCapturedPiece);
		}
		
		
		// Move the piece
		board.move(piece, toRow, toCol);
		
		
		// If a man has reached the kings row and is in the middle of a
		// sequence, check to see which rule applies and act accordingly
		if (piece.isMan() && board.isInKingsRow(piece)) {
			switch (variant.getKingsRowCapture()) {
			// If stop, terminate sequence and become king
			case STOP:
				terminateSequence = true;
				piece.crown();
				break;
			// If skip, continue but do not become king
			case SKIP:
				break;
			// If adapt, continue and become king
			case ADAPT:
				piece.crown();
				break;
			}
		}
		
		// Otherwise, if the man has reached the kings row, just crown it
		else if (piece.isMan() && board.isInKingsRow(piece)) {
			piece.crown();
		}
		
		
		// If player's sequence has been terminated, clear the captured pieces,
		// if there are any, and move on to the next player
		if (terminateSequence) {
			for (Piece capturedPiece: capturedPieces) {
				pieces.remove(capturedPiece);
				board.remove(capturedPiece);
			}
			capturingPiece = null;
			capturedPieces.clear();
			lastDir = null;
			
			nextPlayer();
		}
		
		// Otherwise, record the last piece and direction moved
		else {
			capturingPiece = piece;
			lastDir = board.absoluteDirection(piece, new Position(toRow, toCol));
		}
	}
	
	
	/**
	 * @param piece The piece to check
	 * @return Whether a piece is among the player's piece(s) that can make the
	 * maximum number of captures
	 */
	private boolean isMaximumCapturePiece(Piece piece) {
		List<Piece> maxCapturePieces = maximumCapturePieces(playerOf(piece));
		return maxCapturePieces.contains(piece);
	}
	
	
	/**
	 * Returns a list of pieces a player can use to capture if the maximum
	 * capture rule is on
	 * @param player The player to check
	 * @return The list of pieces that can capture
	 */
	private List<Piece> maximumCapturePieces(Player player) {
		List<Piece> pieces = new ArrayList<Piece>();
		List<Integer> maxCaptureValues = new ArrayList<Integer>();
		int maxMaxCaptureValue = 0;
		
		// Get the maximum capture value of each piece
		for (Piece piece : piecesOf(player)) {
			pieces.add(piece);
			maxCaptureValues.add(maximumCaptureValue(piece));
			maxMaxCaptureValue = Math.max(maxCaptureValues.get(maxCaptureValues.size() - 1), maxMaxCaptureValue);
		}
		
		// Return the pieces with only the maximum maximum capture value
		List<Piece> piecesToReturn = new ArrayList<Piece>();
		for (int i = 0; i < maxCaptureValues.size(); i++) {
			if (maxCaptureValues.get(i) == maxMaxCaptureValue) {
				piecesToReturn.add(pieces.get(i));
			}
		}
		return piecesToReturn;
	}
	
	
	/**
	 * @param piece The piece to check
	 * @param position The position for the piece to move to
	 * @return Whether the given piece moving to the given position would be
	 * considered a maximum capture under maximum capture rules
	 */
	private boolean isMaximumCapture(Piece piece, Position position) {
		List<MaximumCapturePosition> maxCapturePositions = maximumCaptures(piece);
		boolean found = false;
		for (Position maxCapturePosition : maxCapturePositions) {
			if (maxCapturePosition.getRow() == position.getRow() &&
					maxCapturePosition.getCol() == position.getCol()) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	
	/**
	 * Returns the number of captures a piece can make in a row
	 * @param piece The piece to check
	 * @return The number of captures the piece can make
	 */
	private int maximumCaptureValue(Piece piece) {
		List<MaximumCapturePosition> maxCapPositions = maximumCaptures(piece);
		if (maxCapPositions.isEmpty()) {
			return 0;
		}
		return maximumCaptures(piece).get(0).getMaxCaptureValue();
	}
	
	
	/**
	 * Returns a list of captures a piece can make taking into account the
	 * maximum capture rule. Also takes into account the maximum number of kings
	 * quality rule if it is in play
	 * @param piece The piece to check
	 * @return A list of captures of the maximum value
	 */
	private List<MaximumCapturePosition> maximumCaptures(Piece piece) {
		// Maximum capture positions
		List<MaximumCapturePosition> maxCapturePositions = new ArrayList<>();
		// Maximum capture value
		int maxCaptureValue = 0;
		
		// Get all capture values and positions from capturable moves, and add
		// them to the list
		for (CapturePosition pos : capturePositions(piece)) {
			int captureValue = maximumCaptureRecursive(piece, pos, this);
			maxCapturePositions.add(new MaximumCapturePosition(pos.getRow(), pos.getCol(), pos.getCapturedPiece(), captureValue));
			maxCaptureValue = Math.max(captureValue, maxCaptureValue);
		}
		
		// Remove capture positions that do not have the maximum capture value
		Iterator<MaximumCapturePosition> i = maxCapturePositions.iterator();
		while (i.hasNext()) {
			MaximumCapturePosition capturePosition = i.next();
			int captureValue = capturePosition.getMaxCaptureValue();
			if (captureValue < maxCaptureValue) {
				i.remove();
			}
		}
		
		// If the quality rule to capture the maximum number of kings is also in
		// play, remove positions that do not capture the maximum number of
		// kings.
		if (variant.hasQualityRule()) {
			int maxNumKings = 0;
			for (MaximumCapturePosition pos : maxCapturePositions) {
				int numKings = maximumCaptureKingsRecursive(piece, pos, this);
				maxNumKings = Math.max(numKings, maxNumKings);
			}
			i = maxCapturePositions.iterator();
			while (i.hasNext()) {
				MaximumCapturePosition capturePosition = i.next();
				int numKings = maximumCaptureKingsRecursive(piece, capturePosition, this);
				if (numKings < maxNumKings) {
					i.remove();
				}
			}
		}
		
		return maxCapturePositions;
	}
	
	
	/**
	 * Returns the maximum number of captures a piece can make after moving to
	 * the designated position
	 * @param piece The piece to check
	 * @param position The position the piece would move to
	 * @param game The checkers game
	 * @return The maximum number of captures the piece can make
	 */
	private int maximumCaptureRecursive(Piece piece, CapturePosition position, Checkers game) {
		
		// Return 0 if the turn has moved to the next player
		if (game.getCurPlayer() != game.playerOf(piece)) {
			return 0;
		}
		
		int fromRow = game.getBoard().getPosition(piece).getRow();
		int fromCol = game.getBoard().getPosition(piece).getCol();
		int toRow = position.getRow();
		int toCol = position.getCol();
		
		// Create a copy of the game and the piece to the position in it
		Checkers gameCopy = game.clone();
		Piece pieceCopy = gameCopy.getBoard().getPiece(fromRow, fromCol);
		gameCopy.maximumCaptureMove(fromRow, fromCol, toRow, toCol);
		
		// Get and return maximum capture value
		int maxCaptureValue = 0;
		for (CapturePosition pos : gameCopy.capturePositions(pieceCopy)) {
			int captureValue = maximumCaptureRecursive(pieceCopy, pos, gameCopy);
			maxCaptureValue = Math.max(captureValue , maxCaptureValue);
		}
		return maxCaptureValue + (position.getCapturedPiece().isKing() ?
				variant.getQuantityRuleKingValue() :
				variant.getQuantityRuleManValue());
	}
	
	
	/**
	 * Returns the maximum number of kings a piece can capture after moving to
	 * the designated position
	 * @param piece The piece to check
	 * @param position The position the piece would move to
	 * @param game The checkers game
	 * @return The maximum number of kings the piece can capture
	 */
	private int maximumCaptureKingsRecursive(Piece piece, CapturePosition position, Checkers game) {
		
		// Create a copy of the game and the piece to the position in it
		int fromRow = game.getBoard().getPosition(piece).getRow();
		int fromCol = game.getBoard().getPosition(piece).getCol();
		int toRow = position.getRow();
		int toCol = position.getCol();
		Checkers gameCopy = game.clone();
		Piece pieceCopy = gameCopy.getBoard().getPiece(fromRow, fromCol);
		gameCopy.maximumCaptureMove(fromRow, fromCol, toRow, toCol);
		gameCopy.curPlayer = gameCopy.playerOf(pieceCopy);
		
		// Is the captured piece a king? If so, the number of kings starts at 1
		int numKings;
		if (position.getCapturedPiece().isKing()) {
			numKings = 1;
		}
		else {
			numKings = 0;
		}
		
		// Get and return maximum number of kings possible to capture
		int maxNumKings = 0;
		for (CapturePosition pos : gameCopy.capturePositions(pieceCopy)) {
			int curNumKings = maximumCaptureKingsRecursive(pieceCopy, pos, gameCopy);
			maxNumKings = Math.max(curNumKings , maxNumKings);
		}
		return numKings + maxNumKings;
	}
	
	
	
	/**
	 * Returns the piece that would be captured if a capturing move is made
	 * @param piece The piece to be moved
	 * @param toRow The row to move to
	 * @param toCol The column to move to
	 * @return The piece that would be captured or null if not actually a
	 * capturing move
	 */
	private Piece capturedPiece(Piece piece, int toRow, int toCol) {
		Piece capturablePiece = null;
		for (CapturePosition capturePos : capturePositions(piece)) {
			if (capturePos.getRow() == toRow && capturePos.getCol() == toCol) {
				capturablePiece = capturePos.getCapturedPiece();
				break;
			}
		}
		return capturablePiece;
	}
	
	
	
	/**
	 * @return A list of pieces the current player can move
	 */
	public List<Piece> movablePieces() {
		List<Piece> movablePieces = new ArrayList<>();
		for (Piece piece : pieces) {
			if (canMove(piece)) {
				movablePieces.add(piece);
			}
		}
		return movablePieces;
	}
	
	
	
	/**
	 * @return A list of pieces a player can use to capture (Does not account
	 * for rules)
	 */
	private List<Piece> capturingPiecesNoRules(Player player) {
		List<Piece> capturablePieces = new ArrayList<>();
		for (Piece piece : piecesOf(player)) {
			if (!capturePositions(piece).isEmpty()) {
				capturablePieces.add(piece);
			}
		}
		return capturablePieces;
	}
	
	
	
	/**
	 * @param player The player to check
	 * @return Whether a player can move
	 */
	public boolean canMove(Player player) {
		return !validMoves(player).isEmpty();
	}
	
	
	
	/**
	 * @param piece The piece to check
	 * @return Whether a piece can move
	 */
	public boolean canMove(Piece piece) {
		return !validMoves(piece).isEmpty();
	}
	
	
	
	/**
	 * @param piece The piece to check
	 * @return Whether the piece can perform a multi-capture
	 */
	public boolean canMultiCapture(Piece piece) {
		for (CapturePosition capturePos : validCaptures(piece)) {
			Position fromPos = board.getPosition(piece);
			int fromRow = fromPos.getRow();
			int fromCol = fromPos.getCol();
			int toRow = capturePos.getRow();
			int toCol = capturePos.getCol();
			Checkers gameCopy = this.clone();
			gameCopy.move(fromRow, fromCol, toRow, toCol);
			if (gameCopy.canCapture(gameCopy.getBoard().getPiece(toRow, toCol))) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * @param player The player to check
	 * @return Whether a player can capture
	 */
	public boolean canCapture(Player player) {
		return !validCaptures(player).isEmpty();
	}
	
	
	
	/**
	 * @param piece The piece to check
	 * @return Whether a piece can capture
	 */
	public boolean canCapture(Piece piece) {
		return !validCaptures(piece).isEmpty();
	}
	
	
	
	/**
	 * @param piece The piece to move
	 * @param toRow The row to move to
	 * @param toCol The column to move to
	 * @return Whether the given move is a valid move
	 */
	public boolean isValidMove(Piece piece, int toRow, int toCol) {
		for (Position movePos : validMoves(piece)) {
			if (movePos.getRow() == toRow && movePos.getCol() == toCol) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * @param piece The piece to move
	 * @param toRow The row to move to
	 * @param toCol The column to move to
	 * @return Whether the given move is a valid capture
	 */
	public boolean isValidCapture(Piece piece, int toRow, int toCol) {
		for (Position capturePos : validCaptures(piece)) {
			if (capturePos.getRow() == toRow && capturePos.getCol() == toCol) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * Returns all valid moves all pieces of a player can make, taking into
	 * account rules and including both regular movements and captures
	 * @param player The player to check
	 * @return The valid moves
	 */
	public List<Position> validMoves(Player player) {
		List<Position> positions = new ArrayList<>();
		for (Piece piece : piecesOf(player)) {
			positions.addAll(validMoves(piece));
		}
		return positions;
	}
	
	
	
	/**
	 * Returns all valid moves a piece can make, taking into account rules and
	 * including both regular movements and captures
	 * @param piece The piece to check
	 * @return The valid moves
	 */
	public List<Position> validMoves(Piece piece) {
		List<Position> positions = new ArrayList<>();
		if (variant.isHuffing()) {
			if (moved) {
				if (capturingPiece != null && piece == capturingPiece) {
					positions.addAll(validCapturePositions(piece));
				}
			}
			else {
				positions.addAll(validCapturePositions(piece));
				positions.addAll(validMovementPositions(piece));
			}
		}
		else if (canCapture(playerOf(piece))) {
			positions.addAll(validCapturePositions(piece));
		}
		else {
			positions.addAll(validMovementPositions(piece));
		}
		return positions;
	}
	
	
	
	/**
	 * Returns all valid captures all pieces of a player can make, taking into
	 * account rules
	 * @param player The player to check
	 * @return The valid captures
	 */
	private List<CapturePosition> validCaptures(Player player) {
		List<CapturePosition> positions = new ArrayList<>();
		for (Piece piece : piecesOf(player)) {
			positions.addAll(validCaptures(piece));
		}
		return positions;
	}
	
	
	
	/**
	 * Returns all valid captures a piece can make, taking into account rules
	 * @param piece The piece to check
	 * @return The valid captures
	 */
	public List<CapturePosition> validCaptures(Piece piece) {
		List<CapturePosition> positions = new ArrayList<>();
		positions.addAll(validCapturePositions(piece));
		return positions;
	}
	
	
	
	/**
	 * Returns the positions to which a piece can move, accounting for rules but
	 * not accounting for capturing moves.
	 * @param piece
	 * @return
	 */
	private List<Position> validMovementPositions(Piece piece) {
		List<Position> positions = new ArrayList<>();
		
		// Invalid if piece is not of current player's collection
		if (playerOf(piece) != curPlayer) {
			return positions;
		}
		
		// Invalid if the piece is a king and it has made the maximum number of
		// consecutive moves (only if the player has any men)
		if (piece.isKing() && hasMen(playerOf(piece)) &&
				piece.getConsecutiveMoves() >= variant.getKingMaxConsecutiveMoves()) {
			return positions;
		}
		
		positions.addAll(movementPositions(piece));
		return positions;
	}
	
	
	
	/**
	 * Returns the positions to which a piece can move. Does not account for
	 * rules other than flying kings and does not include capture positions.
	 * @param piece The piece to check
	 * @return The positions to which the piece can move
	 */
	private List<Position> movementPositions(Piece piece) {
		List<Position> positions = new ArrayList<>();
		for (AbsoluteDirection direction : absoluteMovementDirections(piece)) {
			// Whether this position is after the movable space
			boolean afterMovableSpace = false;
			int curRow = board.getPosition(piece).getRow();
			int curCol = board.getPosition(piece).getCol();
			do {
				Position position = new Position(curRow, curCol);
				switch (direction) {
				case N:
					position.setRow(--curRow);
					break;
				case S:
					position.setRow(++curRow);
					break;
				case E:
					position.setCol(++curCol);
					break;
				case W:
					position.setCol(--curCol);
					break;
				case NW:
					position.setRow(--curRow);
					position.setCol(--curCol);
					break;
				case NE:
					position.setRow(--curRow);
					position.setCol(++curCol);
					break;
				case SW:
					position.setRow(++curRow);
					position.setCol(--curCol);
					break;
				case SE:
					position.setRow(++curRow);
					position.setCol(++curCol);
					break;
				}
				
				// Stop adding position(s) if the space is not a valid space
				if (!board.isValidSpace(position)) {
					break;
				}
				
				// If linear movement is possible, skip if a man of same color
				// is found
				if (variant.hasLinearMovement() && piece.isMan() &&
						board.getPiece(position) != null && board.getPiece(position).isMan() &&
						board.getPiece(position).getPlayerType() == piece.getPlayerType()) {
					continue;
				}
				
				// Stop adding position(s) if the space is not movable to
				if (!board.isMovableSpace(position)) {
					break;
				}
				
				// If the variant has a move on same color rule, skip different-
				// colored squares
				if (variant.canOnlyMoveOnSameColor() &&
						board.getSquare(position) != board.getSquare(board.getPosition(piece))) {
					continue;
				}
				
				positions.add(position);
				afterMovableSpace = true;
				
			// Continue if the piece is a flying king
			} while (piece.isKing() && (variant.getKingType().isFlying()) ||
					(variant.hasLinearMovement() && !afterMovableSpace));
		}
		return positions;
	}
	
	
	
	/**
	 * Returns the absolute directions in which a piece can move
	 * @param piece The piece
	 * @return The absolute directions
	 */
	private List<AbsoluteDirection> absoluteMovementDirections(Piece piece) {
		Player player = playerOf(piece);
		List<AbsoluteDirection> absDirections = new ArrayList<>();
		for (RelativeDirection relDirection : piece.isMan() ?
				variant.getManMovementDirections() : variant.getKingMovementDirections()) {
			switch (relDirection) {
			case DIAGONAL_FORWARD:
				if (player == bottomPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.NW, AbsoluteDirection.NE);
				else if (player == topPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.SW, AbsoluteDirection.SE);
				break;
			case DIAGONAL_BACKWARD:
				if (player == bottomPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.SW, AbsoluteDirection.SE);
				else if (player == topPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.NW, AbsoluteDirection.NE);
				break;
			case ORTHOGONAL_FORWARD:
				if (player == bottomPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.N);
				else if (player == topPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.S);
				break;
			case ORTHOGONAL_BACKWARD:
				if (player == bottomPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.S);
				else if (player == topPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.N);
				break;
			case ORTHOGONAL_SIDEWAYS:
				Collections.addAll(absDirections,
						AbsoluteDirection.W, AbsoluteDirection.E);
				break;
			}
		}
		return absDirections;
	}
	
	
	
	/**
	 * Returns the positions moving to which results in a capture for all pieces
	 * of a player. Accounts for rules.
	 * @param player The player to check
	 * @return The positions to which the player's pieces can move
	 *//*
	private List<CapturePosition> validCapturePositions(Player player) {
		List<CapturePosition> positions = new ArrayList<>();
		for (Piece piece : piecesOf(player)) {
			positions.addAll(validCapturePositions(piece));
		}
		return positions;
	}*/
	
	
	
	/**
	 * Returns the positions moving to which results in a capture. Accounts for
	 * rules.
	 * @param piece The piece to check
	 * @return The positions to which the piece can move
	 */
	private List<CapturePosition> validCapturePositions(Piece piece) {
		List<CapturePosition> positions = new ArrayList<>();
		
		// Invalid if piece is not of current player's collection
		if (playerOf(piece) != curPlayer) {
			return positions;
		}
		
		positions.addAll(capturePositions(piece));
		// Use iterator so that items can be removed from the list
		Iterator<CapturePosition> i = positions.iterator();
		while(i.hasNext()) {
			CapturePosition position = i.next();
			
			// Invalid if there is a capturing piece and this piece is not the
			// capturing piece
			if (capturingPiece != null && piece != capturingPiece) {
				i.remove();
				continue;
			}
			
			// Invalid if piece is man, is trying to capture king, but men
			// cannot capture kings
			if (piece.isMan() && position.getCapturedPiece().isKing() &&
					!variant.isManCanCaptureKing()) {
				i.remove();
				continue;
			}
			
			// Invalid if maximum capture is required, but this is not a maximum
			// capture
			if (variant.hasQuantityRule() && !(isMaximumCapture(piece, position) && isMaximumCapturePiece(piece))) {
				i.remove();
				continue;
			}
			
			// If this piece is a man and both this piece and a king can capture
			// an equal value, only the king can move, so this piece is not a
			// maximum capture
			if (variant.hasPriorityRule() && piece.isMan() &&
					kingCanMaximumCapture(playerOf(piece))) {
				i.remove();
				continue;
			}
			
			// If the rule specifies, if a player may capture an equal number of
			// pieces (each series containing a king) with a king, he cannot
			// capture in a sequence where the king does not occur first
			if (variant.isCaptureFirstKing() && !isFirstKingCapture(piece, position)) {
				i.remove();
				continue;
			}
			
			// Invalid if king cannot reverse direction during capture, and this
			// is a king that is trying to reverse direction mid-capture
			if (!variant.isKingCanReverseDirection() && piece.isKing() && lastDir != null &&
					board.absoluteDirection(piece, position) == lastDir.opposite()) {
				i.remove();
				continue;
			}
			
			// Invalid if piece is man, is trying to capture king in kings row,
			// but the man must capture a king
			if (variant.isPrioritizeKingCaptureInKingsRow() && piece.isMan() &&
					board.isInKingsRow(piece) && canCaptureKing(piece) && !position.getCapturedPiece().isKing()) {
				i.remove();
				continue;
			}
		}
		
		return positions;
	}
	
	
	
	/**
	 * Returns the positions moving to which results in a capture. Does not
	 * account for special rules other than flying kings
	 * @param piece The piece to check
	 * @return The positions to which the piece can move
	 */
	private List<CapturePosition> capturePositions(Piece piece) {
		List<CapturePosition> positions = new ArrayList<>();
		for (AbsoluteDirection direction : absoluteCaptureDirections(piece)) {
			// Whether a capturable piece has been found
			boolean foundCapturablePiece = false;
			// Whether this position is after the capturable piece
			boolean afterCapturablePiece = false;
			int curRow = board.getPosition(piece).getRow();
			int curCol = board.getPosition(piece).getCol();
			CapturePosition position = new CapturePosition(curRow, curCol);
			do {
				switch (direction) {
				case N:
					position.setRow(--curRow);
					break;
				case S:
					position.setRow(++curRow);
					break;
				case E:
					position.setCol(++curCol);
					break;
				case W:
					position.setCol(--curCol);
					break;
				case NW:
					position.setRow(--curRow);
					position.setCol(--curCol);
					break;
				case NE:
					position.setRow(--curRow);
					position.setCol(++curCol);
					break;
				case SW:
					position.setRow(++curRow);
					position.setCol(--curCol);
					break;
				case SE:
					position.setRow(++curRow);
					position.setCol(++curCol);
					break;
				}
				
				// Stop adding position(s) if the space is invalid
				if (!board.isValidSpace(position)) {
					break;
				}
				
				// If the variant has a move on same color rule, skip different-
				// colored squares
				if (variant.canOnlyMoveOnSameColor() &&
						board.getSquare(position) != board.getSquare(board.getPosition(piece))) {
					continue;
				}
				
				// Stop adding position(s) if the piece at the position is in
				// the list of captured pieces or is a player piece
				if (board.getPiece(position) != null &&
						(capturedPieces.contains(board.getPiece(position)) ||
						board.getPiece(position).getPlayerType() == piece.getPlayerType())) {
					break;
				}
				
				// If this is a capturable piece, indicate so
				if (!foundCapturablePiece &&
						board.getPiece(position) != null &&
						playerOf(board.getPiece(position)) != curPlayer) {
					foundCapturablePiece = true;
					position.setCapturedPiece(board.getPiece(position));
				}
				
				// If another piece has been found, there are no longer any
				// valid movements
				else if (board.getPiece(position) != null) {
					break;
				}
				
				// If this space is an empty space after the capturable piece,
				// indicate so
				else if (foundCapturablePiece && !afterCapturablePiece) {
					afterCapturablePiece = true;
				}
				
				// Stop adding position(s) if a capturable piece has not been
				// found, and the piece is either a man or a short king
				if (!foundCapturablePiece && (piece.isMan() ||
						variant.getKingType() == KingType.SHORT)) {
					break;
				}
				
				// Add position if after capturable piece
				if (afterCapturablePiece) {
					positions.add(position);
					position = new CapturePosition(position);
				}
				
				// Stop adding position(s) if a movable space has already been
				// found, and the piece is either a man or a non-flying king
				if (afterCapturablePiece && (piece.isMan() || variant.getKingType().isShort())) {
					break;
				}
				
			} while (true);
		}
		return positions;
	}
	
	
	
	/**
	 * Returns the absolute directions in which a piece can capture
	 * @param piece The piece
	 * @return The absolute directions
	 */
	private List<AbsoluteDirection> absoluteCaptureDirections(Piece piece) {
		Player player = playerOf(piece);
		List<AbsoluteDirection> absDirections = new ArrayList<>();
		for (RelativeDirection relDirection : piece.isMan() ?
				variant.getManCaptureDirections() : variant.getKingCaptureDirections()) {
			switch (relDirection) {
			case DIAGONAL_FORWARD:
				if (player == bottomPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.NW, AbsoluteDirection.NE);
				else if (player == topPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.SW, AbsoluteDirection.SE);
				break;
			case DIAGONAL_BACKWARD:
				if (player == bottomPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.SW, AbsoluteDirection.SE);
				else if (player == topPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.NW, AbsoluteDirection.NE);
				break;
			case ORTHOGONAL_FORWARD:
				if (player == bottomPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.N);
				else if (player == topPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.S);
				break;
			case ORTHOGONAL_BACKWARD:
				if (player == bottomPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.S);
				else if (player == topPlayer()) Collections.addAll(absDirections,
						AbsoluteDirection.N);
				break;
			case ORTHOGONAL_SIDEWAYS:
				Collections.addAll(absDirections,
						AbsoluteDirection.W, AbsoluteDirection.E);
				break;
			}
		}
		return absDirections;
	}
	

	/**
	 * @param player The player to check
	 * @return Whether the player has a king that can maximum capture
	 */
	private boolean kingCanMaximumCapture(Player player) {
		boolean kingCanMaximumCapture = false;
		for (Piece piece : capturingPiecesNoRules(player)) {
			if (piece.isKing() && isMaximumCapturePiece(piece)) {
				kingCanMaximumCapture = true;
				break;
			}
		}
		return kingCanMaximumCapture;
	}

	
	/**
	 * @param player The player to check
	 * @return Whether the piece can capture a king
	 */
	private boolean canCaptureKing(Piece piece) {
		boolean canCaptureKing = false;
		for (CapturePosition capturePosition : capturePositions(piece)) {
			if (capturePosition.getCapturedPiece().isKing()) {
				canCaptureKing = true;
				break;
			}
		}
		return canCaptureKing;
	}
	
	
	
	/**
	 * @return Whether a draw has occurred due to the number of board state
	 * repeats reaching the amount needed for this variant
	 */
	private boolean drawFromBoardRepeats() {
		if (variant.hasNumBoardRepeatsToDraw() && history.highestCount() >= variant.getNumBoardRepeatsToDraw()) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * @return Whether the king vs king rule has been reached and the countdown
	 * should be started. The starting player must be the player with the
	 * higher number of kings
	 */
	private boolean hasKingVKingCondition() {
		
		int topPlayerNumKings = numKings(topPlayer());
		int bottomPlayerNumKings = numKings(bottomPlayer());
		
		int higherNumKings = variant.getKingVKingDrawRule().getHigherNumKings();
		int lowerNumKings = variant.getKingVKingDrawRule().getLowerNumKings();
		
		if (((topPlayerNumKings >= higherNumKings && bottomPlayerNumKings <= lowerNumKings && !hasMen(bottomPlayer())) ||
				(bottomPlayerNumKings >= higherNumKings && topPlayerNumKings <= lowerNumKings && !hasMen(topPlayer()))) &&
				((curPlayer == topPlayer() && topPlayerNumKings >= bottomPlayerNumKings) ||
				(curPlayer == bottomPlayer() && bottomPlayerNumKings >= topPlayerNumKings))) {
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * @return Whether the king vs king automatic rule has been reached
	 */
	private boolean hasKingVKingAutomaticCondition() {
		
		int topPlayerNumKings = numKings(topPlayer());
		int bottomPlayerNumKings = numKings(bottomPlayer());
		
		int p1NumKings = variant.getKingVKingAutomaticDrawRule().getP1NumKings();
		int p2NumKings = variant.getKingVKingAutomaticDrawRule().getP2NumKings();
		
		if ((!hasMen(topPlayer()) && !hasMen(bottomPlayer())) &&
				((topPlayerNumKings <= p1NumKings && bottomPlayerNumKings <= p2NumKings) ||
				(bottomPlayerNumKings <= p1NumKings && topPlayerNumKings <= p2NumKings))) {
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * @return Whether a draw has occurred due to a player with a certain amount
	 * of kings and the other player with another amount of kings not winning
	 * within a certain number of rounds
	 */
	private boolean drawFromKingVKing() {
		if (!variant.hasKingVKingDrawRule()) {
			return false;
		}
		else if (kingVKingTurnsLeft == 0) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * @return Whether a draw has occurred due to each player having a certain
	 * amount of kings and no other pieces
	 */
	private boolean drawFromKingVKingAutomatic() {
		if (!variant.hasKingVKingDrawRule()) {
			return false;
		}
		else if (hasKingVKingAutomaticCondition()) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * @param piece The piece to check
	 * @param position The position for the piece to move to
	 * @return Whether the given piece moving to the given position would
	 * capture the first occurrence of a king among all capturing sequences.
	 */
	private boolean isFirstKingCapture(Piece piece, CapturePosition position) {
		// declare list of maximum capture positions
		MaximumCapturePosition[] maximumCapturePositions =
				maximumCaptures(piece).toArray(new MaximumCapturePosition[0]);
		// declare parallel array of soonest kings w/ each initialized to max
		Integer[] soonestKings = new Integer[maximumCapturePositions.length];
		
		// for each position in maximum capture positions
		int thisSoonestKing = Integer.MAX_VALUE;
		for (int i = 0; i < maximumCapturePositions.length; i++) {
			// recursively get number of turns until first king
			soonestKings[i] = firstKingCaptureRecursive(piece, position, this, 1);
			if (maximumCapturePositions[i].equals(position)) {
				thisSoonestKing = soonestKings[i];
			}
		}
		
		// initialize first king number
		int firstKingTurn = Collections.min(Arrays.asList(soonestKings));
		
		// if this position's soonest king turn is greater than the overall
		// soonest king turn, return false
		if (thisSoonestKing > firstKingTurn) {
			return false;
		}
		// otherwise return true
		return true;
	}
	
	
	/**
	 * Recursively goes through a capturing sequence and finds the turn at which
	 * there is the first occurrence of a king
	 * @param piece The piece to check
	 * @param position The position for the piece to move to
	 * @param The game that will be copied to check for future moves
	 * @param turn The turn number
	 * @return The number of turns it took or max integer value if not found
	 */
	private int firstKingCaptureRecursive(Piece piece, CapturePosition position, Checkers game, int turn) {
		
		// Move piece on new copy 
		int fromRow = game.getBoard().getPosition(piece).getRow();
		int fromCol = game.getBoard().getPosition(piece).getCol();
		int toRow = position.getRow();
		int toCol = position.getCol();
		Checkers gameCopy = game.clone();
		Piece pieceCopy = gameCopy.getBoard().getPiece(fromRow, fromCol);
		gameCopy.maximumCaptureMove(fromRow, fromCol, toRow, toCol);
		
		// Base case 1 - a king was not found and the capturing seq is over
		if (game.getCurPlayer() != game.playerOf(piece)) {
			return Integer.MAX_VALUE;
		}
		
		// Base case 2 - a king is captured
		if (position.getCapturedPiece().isKing()) {
			return turn;
		}
		
		// Recursive case - return minimum of capture positions
		int earliestKingTurn = Integer.MAX_VALUE;
		for (CapturePosition pos : gameCopy.capturePositions(pieceCopy)) {
			int kingTurn = firstKingCaptureRecursive(pieceCopy, pos, gameCopy, turn + 1);
			earliestKingTurn = Math.min(kingTurn , earliestKingTurn);
		}
		return earliestKingTurn;
	}
	
	
	
	
	
	@Override
	public Checkers clone() {
		Checkers copy = new Checkers(variant);
		
		copy.board = this.board.clone();
		
		copy.blackPlayer = this.blackPlayer;
		copy.whitePlayer = this.whitePlayer;
		copy.curPlayer = this.curPlayer;
		
		copy.pieces = new ArrayList<Piece>();
		copy.capturedPieces = new ArrayList<Piece>();
		
		// Add pieces to pieces and captured pieces
		for (int i = 0; i < variant.getRows(); i++) {
			for (int j = 0; j < variant.getCols(); j++) {
				Piece curPiece = this.board.getPiece(i, j);
				if (this.pieces.contains(curPiece)) {
					copy.pieces.add(copy.board.getPiece(i, j));
				}
				if (this.capturedPieces.contains(curPiece)) {
					copy.capturedPieces.add(copy.board.getPiece(i, j));
				}
			}
		}
		
		copy.lastDir = this.lastDir;
		
		return copy;
	}
}
