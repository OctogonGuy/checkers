package tech.octopusdragon.checkers.data;

import com.badlogic.gdx.Screen;
import tech.octopusdragon.checkers.CheckersApplication;

import java.util.Stack;

public class SessionData {
    public static CheckersApplication application;
    public static Stack<Class<? extends Screen>> screenHistory = new Stack<>();
}
