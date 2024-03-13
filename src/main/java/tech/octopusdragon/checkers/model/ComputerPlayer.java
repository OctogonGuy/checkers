package tech.octopusdragon.checkers.model;

import java.util.Random;

/**
 * AI algorithms to make moves. Difficulty determines the chance of whether a
 * random move is made or a move according to the minimax algorithm is made as
 * well as the maximum search depth of the minimax algorithm.
 * @author Alex Gill
 *
 */
public class ComputerPlayer {
	
	private static int MIN_DEPTH = 3;	// Minimum search depth for minimax
	private static int MAX_DEPTH = 9;	// Maximum search depth for minimax
	
	/**
	 * Returns the computer player's chosen move
	 * @param game The checkers game to reference
	 * @param difficulty The difficulty from 0.0 (easiest) to 1.0 (hardest)
	 * @return The computer player's chosen move
	 */
	public static Move getMove(Checkers game, double difficulty) {
		Random rand = new Random();
		
		if (rand.nextDouble() < -Math.log10(0.9*difficulty + 0.1)) {
			return randomMove(game);
		}
		
		return minimaxMove(game, (int)Math.ceil(difficulty * MAX_DEPTH));
	}
	
	
	/**
	 * Returns a randomly selected move from all available moves
	 * @param game The checkers game to reference
	 * @return A random move
	 */
	private static Move randomMove(Checkers game) {
		Random rand = new Random();
		Move move = game.validMoves(game.getCurPlayer()).get(rand.nextInt(game.validMoves(game.getCurPlayer()).size()));
		return move;
	}
	
	
	/**
	 * Returns the best move the current player can make according to the
	 * minimax algorithm.
	 * @param game The game
	 * @param startingDepth The depth to start at
	 * @return The best move the player can make
	 */
	private static Move minimaxMove(Checkers game, int startingDepth) {
		int bestValue = Integer.MIN_VALUE;
		Move bestMove = null;
		int value = Integer.MIN_VALUE;
		boolean maximizingPlayer = true;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		
		// TODO Make this part better
		// Artificially lowering starting depth because variants with more
		// rules and/or larger boards take too long to compute
		if (game.getVariant().hasQuantityRule())
			startingDepth--;
		startingDepth -= game.getVariant().getNumPieces();
		startingDepth = Math.max(startingDepth, MIN_DEPTH);
		
		Checkers gameCopy = game.clone();
		Player movedPlayer = gameCopy.getCurPlayer();
		for (Move move : game.validMoves(movedPlayer)) {
			gameCopy.move(move);
			value = minimax(gameCopy, startingDepth - 1, alpha, beta, gameCopy.getCurPlayer() == movedPlayer ? maximizingPlayer : !maximizingPlayer);
			gameCopy.undoMove();
			if (value > bestValue || bestMove == null) {
				bestValue = value;
				bestMove = move;
			}
			alpha = Math.max(alpha, value);
			if (value >= beta)
				break;
		}
		
		return bestMove;
	}
	
	
	/**
	 * If it is the maximizing player's turn to move, returns the maximized
	 * minimum value resulting from the opponent's possible moves. If it is the
	 * minimizing player's turn to move, returns the minimized maximum value
	 * resulting from the opponent's possible moves. Incorporates alpha beta
	 * pruning to increase efficiency.
	 * @param game The hypothetical game
	 * @param depth The current depth
	 * @param maximizingPlayer Whether it is the maximizing player's turn
	 * @return The value
	 */
	private static int minimax(Checkers game, int depth, int alpha, int beta, boolean maximizingPlayer) {
		
		// Base case
		if (game.isOver()) { 			// Reached leaf node
			if (game.winner() != null) 	// Maximizing player won or lost
				return maximizingPlayer ? Integer.MAX_VALUE : Integer.MIN_VALUE;
			else						// Draw
				return 0;
		}
		else if (depth == 0)			// Reached maximum search depth
			return staticEvaluation(game, maximizingPlayer);
		
		// Maximizing player
		if (maximizingPlayer) {
			int value = Integer.MIN_VALUE;
			for (Move move : game.validMoves(game.getCurPlayer())) {
				Player movedPlayer = game.getCurPlayer();
				game.move(move);
				value = Math.max(value, minimax(game, depth - 1, alpha, beta, game.getCurPlayer() == movedPlayer ? maximizingPlayer : !maximizingPlayer));
				game.undoMove();
				alpha = Math.max(alpha, value);
				if (value >= beta)
					break;
			}
			return value;
		}
		
		// Minimizing player
		else {
			int value = Integer.MAX_VALUE;
			for (Move move : game.validMoves(game.getCurPlayer())) {
				Player movedPlayer = game.getCurPlayer();
				game.move(move);
				value = Math.min(value, minimax(game, depth - 1, alpha, beta, game.getCurPlayer() == movedPlayer ? maximizingPlayer : !maximizingPlayer));
				game.undoMove();
				beta = Math.min(beta, value);
				if (value <= alpha)
					break;
			}
			return value;
		}
	}
	
	
	/**
	 * Returns a heuristic value for a non-final game state
	 * @param game The hypothetical game
	 * @param maximizingPlayer Whether it is the maximizing player's turn
	 * @return The value
	 */
	private static int staticEvaluation(Checkers game, boolean maximizingPlayer) {
		Player maximizer, minimizer;
		
		if (game.getCurPlayer() == game.getTopPlayer() && maximizingPlayer) {
			maximizer = game.getTopPlayer();
			minimizer = game.getBottomPlayer();
		}
		else if (game.getCurPlayer() == game.getBottomPlayer() && !maximizingPlayer) {
			maximizer = game.getTopPlayer();
			minimizer = game.getBottomPlayer();
		}
		else if (maximizingPlayer) {
			maximizer = game.getBottomPlayer();
			minimizer = game.getTopPlayer();
		}
		else {
			maximizer = game.getBottomPlayer();
			minimizer = game.getTopPlayer();
		}
		
		return game.numPieces(maximizer) - game.numPieces(minimizer);
	}
}
