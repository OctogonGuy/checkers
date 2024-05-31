package tech.octopusdragon.checkers.data;

import tech.octopusdragon.checkers.model.Board;
import tech.octopusdragon.checkers.model.Checkers;
import tech.octopusdragon.checkers.model.PlayerType;
import tech.octopusdragon.checkers.model.Variant;

public class UserData {
    public static Checkers game;
    public static boolean highlightMoves = true;
    public static PlayerType topPlayer = Board.getTopPlayerType();
    public static boolean topPlayerComputer = false;
    public static boolean bottomPlayerComputer = false;
    public static Variant selectedVariant = Variant.AMERICAN;
}
