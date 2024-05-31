package tech.octopusdragon.checkers.view.widget;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import tech.octopusdragon.checkers.model.Family;
import tech.octopusdragon.checkers.model.Variant;

/**
 * A node of VariantTree that contains the sublist of variants
 */
public class VariantNode extends List<Variant> {
    public VariantNode(Family family, Skin skin) {
        super(skin);
        // Populate list with variants
        for (Variant variant : Variant.values()) {
            if (variant.getFamily() == family) {
                getItems().add(variant);
            }
        }
    }
}
