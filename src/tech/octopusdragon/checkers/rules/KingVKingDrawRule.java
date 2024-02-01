package tech.octopusdragon.checkers.rules;

/**
 * A rule that works as such: If there is a player with X kings or more is
 * playing against a player with Y kings, and no men and the player with X kings
 * is unable to win within Z moves, then the game is a draw.
 * @author Alex Gill
 *
 */
public class KingVKingDrawRule {
	
	private int higherNumKings;	// The number of kings the higher player has
	private int lowerNumKings;	// The number of kings the lower player has
	private int numTurns;	// The number of turns before a draw occurs
	private boolean empty;	// Whether the rule doesn't exist
	
	
	/**
	 * Creates an empty king vs. king draw rule. This means the variant does not
	 * have this as a rule
	 */
	public KingVKingDrawRule() {
		empty = true;
	}
	
	
	/**
	 * Creates a king vs. king draw rule
	 * @param higherKings The number of kings the higher player has
	 * @param lowerKings The number of kings the lower player has
	 * @param turns The number of turns before a draw occurs
	 */
	public KingVKingDrawRule(int higherKings, int lowerKings, int turns) {
		higherNumKings = higherKings;
		lowerNumKings = lowerKings;
		numTurns = turns;
		empty = false;
	}
	
	
	/**
	 * @return The number of kings the higher player has
	 */
	public int getHigherNumKings() {
		return higherNumKings;
	}
	
	
	/**
	 * @return The number of kings the lower player has
	 */
	public int getLowerNumKings() {
		return lowerNumKings;
	}
	
	
	/**
	 * @return The number of turns to win before a draw occurs
	 */
	public int getNumTurns() {
		return numTurns;
	}
	
	
	/**
	 * @return Whether the rule doesn't exist and the owning variant does not
	 * have this rule
	 */
	public boolean isEmpty() {
		return empty;
	}
}
