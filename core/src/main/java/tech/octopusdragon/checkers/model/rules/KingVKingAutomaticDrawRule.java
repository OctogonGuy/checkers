package tech.octopusdragon.checkers.model.rules;

/**
 * A rule that works as such: If a player has X kings or less and no other
 * pieces, and the other player has Y kings or less and no other pieces, the
 * game is a draw
 * @author Alex Gill
 *
 */
public class KingVKingAutomaticDrawRule {

	private int p1NumKings;	// The number of kings one player has
	private int p2NumKings;	// The number of kings another player has
	private boolean empty;	// Whether the rule doesn't exist


	/**
	 * Creates an empty king vs. king automatic draw rule. This means the
	 * variant does not have this as a rule
	 */
	public KingVKingAutomaticDrawRule() {
		empty = true;
	}


	/**
	 * Creates a king vs. king automatic draw rule
	 * @param p1Kings The number of kings one player has
	 * @param p2Kings The number of kings another player has
	 */
	public KingVKingAutomaticDrawRule(int p1Kings, int p2Kings) {
		p1NumKings = p1Kings;
		p2NumKings = p2Kings;
		empty = false;
	}


	/**
	 * @return The number of kings player one has
	 */
	public int getP1NumKings() {
		return p1NumKings;
	}


	/**
	 * @return The number of kings player two has
	 */
	public int getP2NumKings() {
		return p2NumKings;
	}


	/**
	 * @return Whether the rule doesn't exist and the owning variant does not
	 * have this rule
	 */
	public boolean isEmpty() {
		return empty;
	}
}
