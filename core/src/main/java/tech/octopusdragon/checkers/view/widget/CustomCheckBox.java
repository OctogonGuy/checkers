package tech.octopusdragon.checkers.view.widget;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import tech.octopusdragon.checkers.data.UIStyle;

/**
 * Check box that is bigger than the default
 */
public class CustomCheckBox extends CheckBox {
    public CustomCheckBox(String text, Skin skin) {
        super(text, skin);
        getImage().setScaling(Scaling.fill);
        getImageCell().size(UIStyle.CHECK_BOX_SIZE, UIStyle.CHECK_BOX_SIZE);
        getImageCell().padRight(UIStyle.CHECK_BOX_SPACING);
    }
}
