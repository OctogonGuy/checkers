package tech.octopusdragon.checkers;

import com.badlogic.gdx.Screen;

import java.util.Stack;

public class SessionData {
    public static CheckersApplication application;
    public static Stack<Class<? extends Screen>> screenHistory = new Stack<>();
}
