package tech.octopusdragon.checkers.view.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class HeaderLabel extends Label {
    public HeaderLabel(String text, Skin skin) {
        super(text, skin);
        setStyle(new LabelStyle(new BitmapFont(Gdx.files.internal("fonts/Lora_header.fnt")), Color.BLACK));
    }
}
