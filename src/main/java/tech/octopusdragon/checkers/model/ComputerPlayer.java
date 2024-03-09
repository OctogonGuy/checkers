package tech.octopusdragon.checkers.model;

import java.util.Random;

/**
 * AI algorithms to make moves
 * @author Alex Gill
 *
 */
public class ComputerPlayer {
	
	/**
	 * Returns the computer player's chosen move
	 * @param game The checkers game to reference
	 * @return The computer player's chosen move
	 */
	public static Move getMove(Checkers game) {
		return randomMove(game);
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
}
