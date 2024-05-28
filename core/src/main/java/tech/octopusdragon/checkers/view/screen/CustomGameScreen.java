package tech.octopusdragon.checkers.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import tech.octopusdragon.checkers.data.SessionData;
import tech.octopusdragon.checkers.data.UserData;
import tech.octopusdragon.checkers.model.Checkers;
import tech.octopusdragon.checkers.model.RelativeDirection;
import tech.octopusdragon.checkers.model.Variant;
import tech.octopusdragon.checkers.model.rules.*;
import tech.octopusdragon.checkers.view.style.UIStyle;
import tech.octopusdragon.checkers.view.widget.CustomButton;
import tech.octopusdragon.checkers.view.widget.CustomCheckBox;
import tech.octopusdragon.checkers.view.widget.CustomScrollPane;
import tech.octopusdragon.checkers.view.widget.HeaderLabel;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class CustomGameScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final SelectBox<Integer> rowsSpinner;
    private final SelectBox<Integer> columnsSpinner;
    private final SelectBox<Integer> piecesPerSideSpinner;
    private final SelectBox<BoardPattern> boardPatternSpinner;
    private final SelectBox<StartingPositions> pieceStartingPositionsSpinner;
    private final SelectBox<StartingPlayer> startingPlayerSpinner;
    private final CustomCheckBox manMoveDiagonalForwardCustomCheckBox;
    private final CustomCheckBox manCaptureDiagonalForwardCustomCheckBox;
    private final CustomCheckBox kingMoveDiagonalForwardCustomCheckBox;
    private final CustomCheckBox kingCaptureDiagonalForwardCustomCheckBox;
    private final CustomCheckBox manMoveDiagonalBackwardCustomCheckBox;
    private final CustomCheckBox manCaptureDiagonalBackwardCustomCheckBox;
    private final CustomCheckBox kingMoveDiagonalBackwardCustomCheckBox;
    private final CustomCheckBox kingCaptureDiagonalBackwardCustomCheckBox;
    private final CustomCheckBox manMoveOrthogonalForwardCustomCheckBox;
    private final CustomCheckBox manCaptureOrthogonalForwardCustomCheckBox;
    private final CustomCheckBox kingMoveOrthogonalForwardCustomCheckBox;
    private final CustomCheckBox kingCaptureOrthogonalForwardCustomCheckBox;
    private final CustomCheckBox manMoveOrthogonalSidewaysCustomCheckBox;
    private final CustomCheckBox manCaptureOrthogonalSidewaysCustomCheckBox;
    private final CustomCheckBox kingMoveOrthogonalSidewaysCustomCheckBox;
    private final CustomCheckBox kingCaptureOrthogonalSidewaysCustomCheckBox;
    private final CustomCheckBox manMoveOrthogonalBackwardCustomCheckBox;
    private final CustomCheckBox manCaptureOrthogonalBackwardCustomCheckBox;
    private final CustomCheckBox kingMoveOrthogonalBackwardCustomCheckBox;
    private final CustomCheckBox kingCaptureOrthogonalBackwardCustomCheckBox;
    private final SelectBox<KingType> kingTypeSpinner;
    private final SelectBox<KingsRowCapture> kingsRowCapturePromotionSpinner;
    private final CustomCheckBox removePiecesImmediatelyCustomCheckBox;
    private final CustomCheckBox manCanCaptureKingCustomCheckBox;
    private final CustomCheckBox quantityRuleCustomCheckBox;
    private final CustomCheckBox qualityRuleCustomCheckBox;
    private final CustomCheckBox priorityRuleCustomCheckBox;

    public CustomGameScreen() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.pad(UIStyle.V_PADDING, UIStyle.H_PADDING, UIStyle.V_PADDING, UIStyle.H_PADDING);
        table.defaults().padTop(UIStyle.V_SPACING);
        stage.addActor(table);

        // Header
        HeaderLabel header = new HeaderLabel("Custom Game", skin);
        table.add(header).padTop(0);
        table.row();

        // Options table
        Table optionsTable = new Table();
        optionsTable.columnDefaults(0).left();
        optionsTable.defaults().expandX().padTop(UIStyle.V_SPACING);
        optionsTable.pad(UIStyle.V_PADDING, UIStyle.H_PADDING, UIStyle.V_PADDING, UIStyle.H_PADDING);
        optionsTable.setBackground(skin.getDrawable("innerColor"));
        CustomScrollPane optionsTableScrollPane = new CustomScrollPane(optionsTable, skin);
        table.add(optionsTableScrollPane).expand().fill();
        table.row();

        // Button bar
        HorizontalGroup buttonBar = new HorizontalGroup().space(UIStyle.BUTTON_BAR_SPACING);
        CustomButton playButton = new CustomButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createCustomVariant();
                UserData.game = new Checkers(Variant.CUSTOM);
                if (Variant.CUSTOM.getStartingPlayer() == StartingPlayer.EITHER)
                    SessionData.application.setScreen(new StartingPlayerScreen());
                else
                    SessionData.application.setScreen(new GameScreen());
            }
        });
        buttonBar.addActor(playButton);
        CustomButton backButton = new CustomButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SessionData.application.lastScreen();
            }
        });
        buttonBar.addActor(backButton);
        table.add(buttonBar);

        // Rows
        Label rowsLabel = new Label("Rows", skin);
        optionsTable.add(rowsLabel).padTop(0);
        rowsSpinner = new SelectBox<>(skin);
        rowsSpinner.setItems(IntStream.range(2, 99).boxed().toArray(Integer[]::new));
        optionsTable.add(rowsSpinner).padTop(0);
        optionsTable.row();

        // Columns
        Label columnsLabel = new Label("Columns", skin);
        optionsTable.add(columnsLabel);
        columnsSpinner = new SelectBox<>(skin);
        columnsSpinner.setItems(IntStream.range(1, 99).boxed().toArray(Integer[]::new));
        optionsTable.add(columnsSpinner);
        optionsTable.row();

        // Columns
        Label piecesPerSideLabel = new Label("Pieces per side", skin);
        optionsTable.add(piecesPerSideLabel);
        piecesPerSideSpinner = new SelectBox<>(skin);
        piecesPerSideSpinner.setItems(IntStream.range(1, 99).boxed().toArray(Integer[]::new));
        optionsTable.add(piecesPerSideSpinner);
        optionsTable.row();

        // Board pattern
        Label boardPatternLabel = new Label("Board pattern", skin);
        optionsTable.add(boardPatternLabel);
        boardPatternSpinner = new SelectBox<>(skin);
        boardPatternSpinner.setItems(BoardPattern.values());
        optionsTable.add(boardPatternSpinner);
        optionsTable.row();

        // Piece starting positions
        Label pieceStartingPositionsLabel = new Label("Starting positions", skin);
        optionsTable.add(pieceStartingPositionsLabel);
        pieceStartingPositionsSpinner = new SelectBox<>(skin);
        pieceStartingPositionsSpinner.setItems(StartingPositions.values());
        optionsTable.add(pieceStartingPositionsSpinner);
        optionsTable.row();

        // Starting player
        Label startingPlayerLabel = new Label("Starting player", skin);
        optionsTable.add(startingPlayerLabel);
        startingPlayerSpinner = new SelectBox<>(skin);
        startingPlayerSpinner.setItems(StartingPlayer.values());
        optionsTable.add(startingPlayerSpinner);
        optionsTable.row();

        // Moves
        Label movesLabel = new Label("Moves", skin);
        optionsTable.add(movesLabel);
        Table movesTable = new Table();
        movesTable.defaults().padTop(UIStyle.TABLE_V_SPACING).padLeft(UIStyle.H_SPACING);
        movesTable.columnDefaults(0).padLeft(0);
        movesTable.add().padTop(0);
        movesTable.add(new Label("Man", skin)).colspan(2).padTop(0);
        movesTable.add(new Label("King", skin)).colspan(2).padTop(0);
        movesTable.row();
        movesTable.add();
        movesTable.add(new Label("Move", skin));
        movesTable.add(new Label("Capture", skin));
        movesTable.add(new Label("Move", skin));
        movesTable.add(new Label("Capture", skin));
        movesTable.row();
        movesTable.add(new Label("Diagonal\nforward", skin));
        manMoveDiagonalForwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(manMoveDiagonalForwardCustomCheckBox);
        manCaptureDiagonalForwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(manCaptureDiagonalForwardCustomCheckBox);
        kingMoveDiagonalForwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(kingMoveDiagonalForwardCustomCheckBox);
        kingCaptureDiagonalForwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(kingCaptureDiagonalForwardCustomCheckBox);
        movesTable.row();
        movesTable.add(new Label("Diagonal\nbackward", skin));
        manMoveDiagonalBackwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(manMoveDiagonalBackwardCustomCheckBox);
        manCaptureDiagonalBackwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(manCaptureDiagonalBackwardCustomCheckBox);
        kingMoveDiagonalBackwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(kingMoveDiagonalBackwardCustomCheckBox);
        kingCaptureDiagonalBackwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(kingCaptureDiagonalBackwardCustomCheckBox);
        movesTable.row();
        movesTable.add(new Label("Orthogonal\nforward", skin));
        manMoveOrthogonalForwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(manMoveOrthogonalForwardCustomCheckBox);
        manCaptureOrthogonalForwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(manCaptureOrthogonalForwardCustomCheckBox);
        kingMoveOrthogonalForwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(kingMoveOrthogonalForwardCustomCheckBox);
        kingCaptureOrthogonalForwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(kingCaptureOrthogonalForwardCustomCheckBox);
        movesTable.row();
        movesTable.add(new Label("Orthogonal\nsideways", skin));
        manMoveOrthogonalSidewaysCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(manMoveOrthogonalSidewaysCustomCheckBox);
        manCaptureOrthogonalSidewaysCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(manCaptureOrthogonalSidewaysCustomCheckBox);
        kingMoveOrthogonalSidewaysCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(kingMoveOrthogonalSidewaysCustomCheckBox);
        kingCaptureOrthogonalSidewaysCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(kingCaptureOrthogonalSidewaysCustomCheckBox);
        movesTable.row();
        movesTable.add(new Label("Orthogonal\nbackward", skin));
        manMoveOrthogonalBackwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(manMoveOrthogonalBackwardCustomCheckBox);
        manCaptureOrthogonalBackwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(manCaptureOrthogonalBackwardCustomCheckBox);
        kingMoveOrthogonalBackwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(kingMoveOrthogonalBackwardCustomCheckBox);
        kingCaptureOrthogonalBackwardCustomCheckBox = new CustomCheckBox(null, skin);
        movesTable.add(kingCaptureOrthogonalBackwardCustomCheckBox);
        optionsTable.add(movesTable);
        optionsTable.row();

        // King type
        Label kingTypeLabel = new Label("King type", skin);
        optionsTable.add(kingTypeLabel);
        kingTypeSpinner = new SelectBox<>(skin);
        kingTypeSpinner.setItems(KingType.values());
        optionsTable.add(kingTypeSpinner);
        optionsTable.row();

        // Kings row capture promotion
        Label kingsRowCapturePromotionLabel = new Label("Kings row capture", skin);
        optionsTable.add(kingsRowCapturePromotionLabel);
        kingsRowCapturePromotionSpinner = new SelectBox<>(skin);
        kingsRowCapturePromotionSpinner.setItems(KingsRowCapture.values());
        optionsTable.add(kingsRowCapturePromotionSpinner);
        optionsTable.row();

        // Remove pieces immediately
        Label removePiecesImmediatelyLabel = new Label("Remove immediately", skin);
        optionsTable.add(removePiecesImmediatelyLabel);
        removePiecesImmediatelyCustomCheckBox = new CustomCheckBox(null, skin);
        optionsTable.add(removePiecesImmediatelyCustomCheckBox);
        optionsTable.row();

        // Man can capture king
        Label manCanCaptureKingLabel = new Label("Men capture kings", skin);
        optionsTable.add(manCanCaptureKingLabel);
        manCanCaptureKingCustomCheckBox = new CustomCheckBox(null, skin);
        optionsTable.add(manCanCaptureKingCustomCheckBox);
        optionsTable.row();

        // Quantity rule
        Label quantityRuleLabel = new Label("Quantity rule", skin);
        optionsTable.add(quantityRuleLabel);
        quantityRuleCustomCheckBox = new CustomCheckBox(null, skin);
        optionsTable.add(quantityRuleCustomCheckBox);
        optionsTable.row();

        // Quality rule
        Label qualityRuleLabel = new Label("Quality rule", skin);
        optionsTable.add(qualityRuleLabel);
        qualityRuleCustomCheckBox = new CustomCheckBox(null, skin);
        optionsTable.add(qualityRuleCustomCheckBox);
        optionsTable.row();

        // Priority rule
        Label priorityRuleLabel = new Label("Priority rule", skin);
        optionsTable.add(priorityRuleLabel);
        priorityRuleCustomCheckBox = new CustomCheckBox(null, skin);
        optionsTable.add(priorityRuleCustomCheckBox);
        optionsTable.row();
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

    /**
     * Creates a custom variant based on the user's choices
     */
    private void createCustomVariant() {
        Variant.setCustomRows(rowsSpinner.getSelected());
        Variant.setCustomCols(columnsSpinner.getSelected());
        Variant.setCustomNumPieces(piecesPerSideSpinner.getSelected());
        Variant.setCustomStartingPositions(pieceStartingPositionsSpinner.getSelected());
        Variant.setCustomBoardPattern(boardPatternSpinner.getSelected());
        Variant.setCustomStartingPlayer(startingPlayerSpinner.getSelected());
        Variant.setCustomKingType(kingTypeSpinner.getSelected());
        Variant.setCustomKingsRowCapture(kingsRowCapturePromotionSpinner.getSelected());
        Variant.setCustomRemovePiecesImmediately(removePiecesImmediatelyCustomCheckBox.isChecked());
        Variant.setCustomManCanCaptureKing(manCanCaptureKingCustomCheckBox.isChecked());
        Variant.setCustomQuantityRule(quantityRuleCustomCheckBox.isChecked());
        Variant.setCustomQualityRule(qualityRuleCustomCheckBox.isChecked());
        Variant.setCustomPriorityRule(priorityRuleCustomCheckBox.isChecked());

        ArrayList<RelativeDirection> manMoveDirections = new ArrayList<>();
        if (manMoveDiagonalForwardCustomCheckBox.isChecked())
			manMoveDirections.add(RelativeDirection.DIAGONAL_FORWARD);
        if (manMoveDiagonalBackwardCustomCheckBox.isChecked())
			manMoveDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
        if (manMoveOrthogonalForwardCustomCheckBox.isChecked())
			manMoveDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
        if (manMoveOrthogonalBackwardCustomCheckBox.isChecked())
			manMoveDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
        if (manMoveOrthogonalSidewaysCustomCheckBox.isChecked())
			manMoveDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
        Variant.setCustomManMovementDirections(manMoveDirections.toArray(new RelativeDirection[0]));

        ArrayList<RelativeDirection> kingMoveDirections = new ArrayList<>();
        if (kingMoveDiagonalForwardCustomCheckBox.isChecked())
			kingMoveDirections.add(RelativeDirection.DIAGONAL_FORWARD);
        if (kingMoveDiagonalBackwardCustomCheckBox.isChecked())
			kingMoveDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
        if (kingMoveOrthogonalForwardCustomCheckBox.isChecked())
			kingMoveDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
        if (kingMoveOrthogonalBackwardCustomCheckBox.isChecked())
			kingMoveDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
        if (kingMoveOrthogonalSidewaysCustomCheckBox.isChecked())
			kingMoveDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
        Variant.setCustomKingMovementDirections(kingMoveDirections.toArray(new RelativeDirection[0]));

        ArrayList<RelativeDirection> manCaptureDirections = new ArrayList<>();
        if (manCaptureDiagonalForwardCustomCheckBox.isChecked())
			manCaptureDirections.add(RelativeDirection.DIAGONAL_FORWARD);
        if (manCaptureDiagonalBackwardCustomCheckBox.isChecked())
			manCaptureDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
        if (manCaptureOrthogonalForwardCustomCheckBox.isChecked())
			manCaptureDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
        if (manCaptureOrthogonalBackwardCustomCheckBox.isChecked())
			manCaptureDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
        if (manCaptureOrthogonalSidewaysCustomCheckBox.isChecked())
			manCaptureDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
        Variant.setCustomManCaptureDirections(manCaptureDirections.toArray(new RelativeDirection[0]));

        ArrayList<RelativeDirection> kingCaptureDirections = new ArrayList<>();
        if (kingCaptureDiagonalForwardCustomCheckBox.isChecked())
			kingCaptureDirections.add(RelativeDirection.DIAGONAL_FORWARD);
        if (kingCaptureDiagonalBackwardCustomCheckBox.isChecked())
			kingCaptureDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
        if (kingCaptureOrthogonalForwardCustomCheckBox.isChecked())
			kingCaptureDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
        if (kingCaptureOrthogonalBackwardCustomCheckBox.isChecked())
			kingCaptureDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
        if (kingCaptureOrthogonalSidewaysCustomCheckBox.isChecked())
			kingCaptureDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
        Variant.setCustomKingCaptureDirections(kingCaptureDirections.toArray(new RelativeDirection[0]));
    }
}
