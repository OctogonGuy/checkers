package tech.octopusdragon.checkers;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;

/**
 * Check box that is bigger than the default
 */
public class CustomCheckBox extends CheckBox {
    public CustomCheckBox(String text, Skin skin) {
        super(text, skin);
        getImage().setScaling(Scaling.fill);
        getImageCell().size(UIStyle.CHECK_BOX_SIZE, UIStyle.CHECK_BOX_SIZE);
    }
}
