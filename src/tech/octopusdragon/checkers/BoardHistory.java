/**
 * 
 */
package tech.octopusdragon.checkers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps track of the state of the game board for every turn in the game
 * @author Alex Gill
 * 
 */
public class BoardHistory {
	
	// Keeps track of the game state of game board and player
	private List<BoardState> boardHistory;
	private int index;	// Current index in the history
	// Keeps track of the number of occurrences of each board state
	private Map<BoardState, Integer> boardCounts;
	
	
	/**
	 * Constructor
	 */
	public BoardHistory() {
		boardHistory = new ArrayList<BoardState>();
		boardCounts = new HashMap<BoardState, Integer>();
	}
	
	
	/**
	 * Creates a new board state from the given items and adds it to the
	 * history. If the current index is lower than that of the last element,
	 * then it deletes all elements after this new one
	 * @param board The current board
	 * @param player The current player
	 */
	public void push(Board board, Player player) {
		while (boardHistory.size() > index + 1) {
			
			// Remove from counts map and update count
			Integer count = boardCounts.get(boardHistory.get(index + 1));
			boardCounts.put(boardHistory.get(index + 1), count - 1);
			
			boardHistory.remove(index + 1);
		}
		
		BoardState boardState = new BoardState(board, player);
		boardHistory.add(new BoardState(board, player));
		
		// Add to counts map and update count
		Integer count = boardCounts.get(boardState);
		if (count == null) {
			boardCounts.put(boardState, 1);
		}
		else {
			boardCounts.put(boardState, count + 1);
		}
		
		index = boardHistory.size() - 1;
	}
	
	
	/**
	 * Decrements the history index and returns the board state at that index.
	 * Does nothing if the current index is 0.
	 * @return The board state
	 */
	public BoardState previous() {
		if (index == 0) return null;
		return boardHistory.get(--index);
	}
	
	
	/**
	 * Increments the history index and returns the board state at that index.
	 * Does nothing if the current index is greater than or equal to the size
	 * of the history.
	 * @return The board state
	 */
	public BoardState next() {
		if (index >= boardHistory.size()) return null;
		return boardHistory.get(++index);
	}
	
	
	/**
	 * Sets the history index to 0 and returns the first board state.
	 * Does nothing if the board history is empty
	 * @return The board state
	 */
	public BoardState first() {
		if (boardHistory.isEmpty()) return null;
		return boardHistory.get(index = 0);
	}
	
	
	/**
	 * Sets the history index to the index of the last item and returns the
	 * last board state.
	 * Does nothing if the board history is empty
	 * @return The board state
	 */
	public BoardState last() {
		if (boardHistory.isEmpty()) return null;
		return boardHistory.get(index = boardHistory.size() - 1);
	}
	
	
	/**
	 * @return The highest number of identical board states
	 */
	public int highestCount() {
		return Collections.max(boardCounts.values());
	}

}
