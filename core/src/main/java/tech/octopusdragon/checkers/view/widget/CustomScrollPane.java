package tech.octopusdragon.checkers.view.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A scroll pane that automatically focuses so that you do not have to click it before you can scroll.
 */
public class CustomScrollPane extends ScrollPane {
    public CustomScrollPane(Actor actor, Skin skin) {
        super(actor, skin);
        setScrollbarsVisible(true);
        setFadeScrollBars(false);
        addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                getStage().setScrollFocus(CustomScrollPane.this);
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                getStage().setScrollFocus(null);
            }
        });
    }
}
