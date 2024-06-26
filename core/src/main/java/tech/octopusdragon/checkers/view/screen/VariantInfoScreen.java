package tech.octopusdragon.checkers.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import tech.octopusdragon.checkers.data.SessionData;
import tech.octopusdragon.checkers.model.RelativeDirection;
import tech.octopusdragon.checkers.model.Variant;
import tech.octopusdragon.checkers.view.style.UIStyle;
import tech.octopusdragon.checkers.view.widget.CustomButton;
import tech.octopusdragon.checkers.view.widget.CustomScrollPane;
import tech.octopusdragon.checkers.view.widget.HeaderLabel;

import java.util.Arrays;

public class VariantInfoScreen implements Screen {
    private final Stage stage;
    private final Skin skin;

    public VariantInfoScreen(Variant variant) {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        StringBuilder sb;

        Table table = new Table();
        table.setFillParent(true);
        table.pad(UIStyle.V_PADDING, UIStyle.H_PADDING, UIStyle.V_PADDING, UIStyle.H_PADDING);
        table.defaults().padTop(UIStyle.V_SPACING);
        stage.addActor(table);

        // Name
        HeaderLabel nameText = new HeaderLabel(variant.getName(), skin);
        nameText.setWrap(true);
        nameText.setAlignment(Align.center);
        table.add(nameText).width(stage.getWidth() - UIStyle.H_PADDING * 2);
        table.row().space(0);

        // Info table
        Table infoTable = new Table();
        infoTable.columnDefaults(0).left();
        infoTable.defaults().expandX().padTop(UIStyle.TABLE_V_SPACING);
        infoTable.pad(UIStyle.V_PADDING, UIStyle.H_PADDING, UIStyle.V_PADDING, UIStyle.H_PADDING);
        infoTable.setBackground(skin.getDrawable("innerColor"));
        CustomScrollPane scrollPane = new CustomScrollPane(infoTable, skin);
        table.add(scrollPane).expand().fill();
        table.row();

        // Button bar
        HorizontalGroup buttonBar = new HorizontalGroup().space(UIStyle.BUTTON_BAR_SPACING);
        CustomButton backButton = new CustomButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               SessionData.application.lastScreen();
            }
        });
        buttonBar.addActor(backButton);
        table.add(buttonBar);

        // Description
        Label descriptionText = new Label(variant.getDescription(), skin);
        descriptionText.setWrap(true);
        infoTable.add(descriptionText).width(stage.getWidth() - UIStyle.H_PADDING*8).colspan(2).padTop(0);
        infoTable.row();

        // Only show name and description if custom
        if (variant == Variant.CUSTOM) return;

        // Rows
        Label rowsLabel = new Label("Rows", skin);
        infoTable.add(rowsLabel);
        Label rowsText = new Label(Integer.toString(variant.getRows()), skin);
        infoTable.add(rowsText).right();
        infoTable.row();

        // Columns
        Label colsLabel = new Label("Columns", skin);
        infoTable.add(colsLabel);
        Label colsText = new Label(Integer.toString(variant.getCols()), skin);
        infoTable.add(colsText).right();
        infoTable.row();

        // Number of pieces
        Label piecesLabel = new Label("Pieces", skin);
        infoTable.add(piecesLabel);
        Label piecesText = new Label(Integer.toString(variant.getNumPieces()), skin);
		infoTable.add(piecesText).right();
        infoTable.row();

        // Board pattern
        Label boardPatternLabel = new Label("Board pattern", skin);
        infoTable.add(boardPatternLabel);
        Label boardPatternText = new Label(variant.getBoardPattern().toString(), skin);
		infoTable.add(boardPatternText).left();
        infoTable.row();

        // Starting positions
        Label startingPositionsLabel = new Label("Starting positions", skin);
        infoTable.add(startingPositionsLabel);
        Label startingPositionsText = new Label(variant.getStartingPositions().toString(), skin);
		infoTable.add(startingPositionsText).left();
        infoTable.row();

        // Starting player
        Label startingPlayerLabel = new Label("Starting player", skin);
        infoTable.add(startingPlayerLabel);
        Label startingPlayerText = new Label(variant.getStartingPlayer().toString(), skin);
		infoTable.add(startingPlayerText).left();
        infoTable.row();

        // Movement
        Label movementLabel = new Label("Movement", skin);
        infoTable.add(movementLabel);
        sb = new StringBuilder();
        switch (variant.getFamily()) {
            case TURKISH:
                sb.append("Orthogonal");
                break;
            case INTERNATIONAL:
            case SPANISH:
            case MISCELLANEOUS:
                sb.append("Diagonal");
                break;
            case GOTHIC:
                sb.append("Orthogonal + Diagonal");
                break;
            case FRISIAN:
                sb.append("Diagonal + Orthogonal");
                break;
            case SPECIAL:
                sb.append("Special");
                break;
        }
        Label movementText = new Label(sb.toString(), skin);
		infoTable.add(movementText).left();
        infoTable.row();

        // Backwards capture
        Label backwardsCaptureLabel = new Label("Backwards capture", skin);
        infoTable.add(backwardsCaptureLabel);
        Label backwardsCaptureText = new Label(
            Arrays.asList(variant.getManCaptureDirections()).contains(
                RelativeDirection.ORTHOGONAL_BACKWARD) ||
            Arrays.asList(variant.getManCaptureDirections()).contains(
                RelativeDirection.DIAGONAL_BACKWARD) ? "O" : "X",
        skin);
        infoTable.add(backwardsCaptureText).center();
        infoTable.row();

        // Flying kings
        Label flyingKingsLabel = new Label("Flying kings", skin);
        infoTable.add(flyingKingsLabel);
        sb = new StringBuilder();
        switch (variant.getKingType()) {
            case SHORT:
                sb.append("X");
                break;
            case FLYING:
                sb.append("O");
                break;
            case SHORT_FLYING:
                sb.append("O (King Halt)");
                break;
        }
        Label flyingKingsText = new Label(sb.toString(), skin);
		infoTable.add(flyingKingsText).center();
        infoTable.row();

        // Kings row capture promotion
        Label kingsRowCapturePromotionLabel = new Label("Kings row capture promotion", skin);
        infoTable.add(kingsRowCapturePromotionLabel);
        Label kingsRowCapturePromotionText = new Label(variant.getKingsRowCapture().toString(), skin);
		infoTable.add(kingsRowCapturePromotionText).left();
        infoTable.row();

        // Remove pieces immediately
        Label removePiecesImmediatelyLabel = new Label("Remove pieces immediately", skin);
        infoTable.add(removePiecesImmediatelyLabel);
        Label removePiecesImmediatelyText = new Label(variant.isRemovePiecesImmediately() ? "O" : "X", skin);
		infoTable.add(removePiecesImmediatelyText).center();
        infoTable.row();

        // Man can capture king
        Label manCanCaptureKingLabel = new Label("Man can capture king", skin);
        infoTable.add(manCanCaptureKingLabel);
        Label manCanCaptureKingText = new Label(variant.isManCanCaptureKing() ? "O" : "X", skin);
		infoTable.add(manCanCaptureKingText).center();
        infoTable.row();

        // Quantity rule
        Label quantityRuleLabel = new Label("Quantity rule", skin);
        infoTable.add(quantityRuleLabel);
        Label quantityRuleText = new Label(variant.hasQuantityRule() ? "O" : "X", skin);
		infoTable.add(quantityRuleText).center();
        infoTable.row();

        // Quality rule
        Label qualityRuleLabel = new Label("Quality rule", skin);
        infoTable.add(qualityRuleLabel);
        Label qualityRuleText = new Label(variant.hasQualityRule() ? "O" : "X", skin);
		infoTable.add(qualityRuleText).center();
        infoTable.row();

        // Priority rule
        Label priorityRuleLabel = new Label("Priority rule", skin);
        infoTable.add(priorityRuleLabel);
        Label priorityRuleText = new Label(variant.hasPriorityRule() ? "O" : "X", skin);
		infoTable.add(priorityRuleText).center();
        infoTable.row();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Color color = skin.getColor("backgroundColor");
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
