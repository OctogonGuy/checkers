package tech.octopusdragon.checkers.view.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * A scroll pane that automatically focuses so that you do not have to click it before you can scroll.
 */
public class CustomScrollPane extends ScrollPane {
    public CustomScrollPane(Table table, Skin skin) {
        super(table, skin);
        setScrollbarsVisible(true);
        setFadeScrollBars(false);
        addListener(new InputListener() {
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                getStage().setScrollFocus(CustomScrollPane.this);
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (x < 0 || x > getWidth() || y < 0 || y > getHeight()) {
                    getStage().setScrollFocus(null);
                }
            }
        });
    }
}
