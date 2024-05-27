package tech.octopusdragon.checkers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import tech.octopusdragon.checkers.data.SessionData;
import tech.octopusdragon.checkers.view.screen.NewGameScreen;

import java.lang.reflect.InvocationTargetException;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class CheckersApplication extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        SessionData.application = this;
        batch = new SpriteBatch();
        this.setScreen(new NewGameScreen());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void setScreen(Screen screen) {
        if (getScreen() != null)
            SessionData.screenHistory.push(getScreen().getClass());
        super.setScreen(screen);
    }

    /**
     * Switches to the last screen
     */
    public void lastScreen() {
        try {
            // Call super to prevent screen being saved to history
            super.setScreen(SessionData.screenHistory.pop().getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
