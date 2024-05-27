package tech.octopusdragon.checkers.view.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import tech.octopusdragon.checkers.view.style.UIStyle;

public class CustomButton extends TextButton {
    public CustomButton(String text, Skin skin) {
        super(text, skin);
        getLabelCell().pad(
            UIStyle.BUTTON_PADDING_V, UIStyle.BUTTON_PADDING_H,
            UIStyle.BUTTON_PADDING_V, UIStyle.BUTTON_PADDING_H);
    }
}
