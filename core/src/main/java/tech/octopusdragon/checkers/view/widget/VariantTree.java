package tech.octopusdragon.checkers.view.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import tech.octopusdragon.checkers.model.Family;
import tech.octopusdragon.checkers.model.Variant;
import tech.octopusdragon.checkers.view.style.UIStyle;

/**
 * A widget with a list of variants separated by family
 */
public class VariantTree extends Table {
    public VariantTree(Skin skin) {
        super();
        // Add all family lists
        for (Family family: Family.values()) {
            add(new Label(family.toString(), skin)).expand().fill()
                .padTop(getChildren().size != 1 ? UIStyle.FAMILY_LABEL_MARGIN_TOP : 0)
                .padBottom(UIStyle.FAMILY_LABEL_MARGIN_BOTTOM);
            row();
            VariantNode newVariantNode = new VariantNode(family, skin);
            // Deselect items in all other lists when a list item is selected
            newVariantNode.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (actor instanceof VariantNode eventNode && eventNode.getSelectedIndex() != -1) {
                        for (int i = 0; i < getChildren().size; i++) {
                            Actor node = getChildren().get(i);
                            if (node instanceof VariantNode variantNode && variantNode != eventNode) {
                                variantNode.setSelectedIndex(-1);
                            }
                        }
                    }
                }
            });
            add(newVariantNode).expand().fill();
            row();
        }
    }

    /**
     * @return The selected variant
     */
    public Variant getSelected() {
        for (Actor node : getChildren()) {
            if (node instanceof VariantNode variantNode && variantNode.getSelected() != null)
                return variantNode.getSelected();
        }
        return null;
    }

    /**
     * Sets the selected variant
     * @param variant The variant to select
     */
    public void setSelected(Variant variant) {
        for (int i = 0; i < getChildren().size; i++) {
            Actor node = getChildren().get(i);
            if (node instanceof VariantNode variantNode && variantNode.getItems().contains(variant, true)) {
                variantNode.setSelected(variant);
            }
        }
    }

    /**
     * @return The y position of the selected item
     */
    public float getSelectedY() {
        Variant selVar = getSelected();
        for (int i = 0; i < getChildren().size; i++) {
            Actor node = getChildren().get(i);
            if (node instanceof VariantNode variantNode && variantNode.getItems().contains(selVar, true)) {
                validate();
                variantNode.validate();
                return variantNode.getY()
                    + getPrefHeight() / 2
                    + (variantNode.getPrefHeight()
                        * (variantNode.getItems().size - variantNode.getSelectedIndex() + 1)
                        / variantNode.getItems().size)
                    + variantNode.getPrefHeight() / variantNode.getItems().size / 2;
            }
        }
        return 0;
    }
}
