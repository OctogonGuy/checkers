package tech.octopusdragon.checkers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import tech.octopusdragon.checkers.model.Checkers;
import tech.octopusdragon.checkers.model.RelativeDirection;
import tech.octopusdragon.checkers.model.Variant;
import tech.octopusdragon.checkers.model.rules.*;

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
    private final CheckBox manMoveDiagonalForwardCheckBox;
    private final CheckBox manCaptureDiagonalForwardCheckBox;
    private final CheckBox kingMoveDiagonalForwardCheckBox;
    private final CheckBox kingCaptureDiagonalForwardCheckBox;
    private final CheckBox manMoveDiagonalBackwardCheckBox;
    private final CheckBox manCaptureDiagonalBackwardCheckBox;
    private final CheckBox kingMoveDiagonalBackwardCheckBox;
    private final CheckBox kingCaptureDiagonalBackwardCheckBox;
    private final CheckBox manMoveOrthogonalForwardCheckBox;
    private final CheckBox manCaptureOrthogonalForwardCheckBox;
    private final CheckBox kingMoveOrthogonalForwardCheckBox;
    private final CheckBox kingCaptureOrthogonalForwardCheckBox;
    private final CheckBox manMoveOrthogonalSidewaysCheckBox;
    private final CheckBox manCaptureOrthogonalSidewaysCheckBox;
    private final CheckBox kingMoveOrthogonalSidewaysCheckBox;
    private final CheckBox kingCaptureOrthogonalSidewaysCheckBox;
    private final CheckBox manMoveOrthogonalBackwardCheckBox;
    private final CheckBox manCaptureOrthogonalBackwardCheckBox;
    private final CheckBox kingMoveOrthogonalBackwardCheckBox;
    private final CheckBox kingCaptureOrthogonalBackwardCheckBox;
    private final SelectBox<KingType> kingTypeSpinner;
    private final SelectBox<KingsRowCapture> kingsRowCapturePromotionSpinner;
    private final CheckBox removePiecesImmediatelyCheckBox;
    private final CheckBox manCanCaptureKingCheckBox;
    private final CheckBox quantityRuleCheckBox;
    private final CheckBox qualityRuleCheckBox;
    private final CheckBox priorityRuleCheckBox;

    public CustomGameScreen() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Table optionsTable = new Table();
        ScrollPane optionsTableScrollPane = new ScrollPane(optionsTable);
        table.add(optionsTableScrollPane);
        table.row();

        // Button bar
        HorizontalGroup buttonBar = new HorizontalGroup();
        TextButton playButton = new TextButton("Play", skin);
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
        TextButton backButton = new TextButton("Back", skin);
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
        optionsTable.add(rowsLabel);
        rowsSpinner = new SelectBox<>(skin);
        rowsSpinner.setItems(IntStream.range(2, 99).boxed().toArray(Integer[]::new));
        optionsTable.add(rowsSpinner);
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
        Label pieceStartingPositionsLabel = new Label("Piece starting positions", skin);
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
        movesTable.add();
        movesTable.add(new Label("Man", skin)).colspan(2);
        movesTable.add(new Label("King", skin)).colspan(2);
        movesTable.row();
        movesTable.add();
        movesTable.add(new Label("Move", skin));
        movesTable.add(new Label("Capture", skin));
        movesTable.add(new Label("Move", skin));
        movesTable.add(new Label("Capture", skin));
        movesTable.row();
        movesTable.add(new Label("Diagonal forward", skin));
        manMoveDiagonalForwardCheckBox = new CheckBox(null, skin);
        movesTable.add(manMoveDiagonalForwardCheckBox);
        manCaptureDiagonalForwardCheckBox = new CheckBox(null, skin);
        movesTable.add(manCaptureDiagonalForwardCheckBox);
        kingMoveDiagonalForwardCheckBox = new CheckBox(null, skin);
        movesTable.add(kingMoveDiagonalForwardCheckBox);
        kingCaptureDiagonalForwardCheckBox = new CheckBox(null, skin);
        movesTable.add(kingCaptureDiagonalForwardCheckBox);
        movesTable.row();
        movesTable.add(new Label("Diagonal backward", skin));
        manMoveDiagonalBackwardCheckBox = new CheckBox(null, skin);
        movesTable.add(manMoveDiagonalBackwardCheckBox);
        manCaptureDiagonalBackwardCheckBox = new CheckBox(null, skin);
        movesTable.add(manCaptureDiagonalBackwardCheckBox);
        kingMoveDiagonalBackwardCheckBox = new CheckBox(null, skin);
        movesTable.add(kingMoveDiagonalBackwardCheckBox);
        kingCaptureDiagonalBackwardCheckBox = new CheckBox(null, skin);
        movesTable.add(kingCaptureDiagonalBackwardCheckBox);
        movesTable.row();
        movesTable.add(new Label("Orthogonal forward", skin));
        manMoveOrthogonalForwardCheckBox = new CheckBox(null, skin);
        movesTable.add(manMoveOrthogonalForwardCheckBox);
        manCaptureOrthogonalForwardCheckBox = new CheckBox(null, skin);
        movesTable.add(manCaptureOrthogonalForwardCheckBox);
        kingMoveOrthogonalForwardCheckBox = new CheckBox(null, skin);
        movesTable.add(kingMoveOrthogonalForwardCheckBox);
        kingCaptureOrthogonalForwardCheckBox = new CheckBox(null, skin);
        movesTable.add(kingCaptureOrthogonalForwardCheckBox);
        movesTable.row();
        movesTable.add(new Label("Orthogonal sideways", skin));
        manMoveOrthogonalSidewaysCheckBox = new CheckBox(null, skin);
        movesTable.add(manMoveOrthogonalSidewaysCheckBox);
        manCaptureOrthogonalSidewaysCheckBox = new CheckBox(null, skin);
        movesTable.add(manCaptureOrthogonalSidewaysCheckBox);
        kingMoveOrthogonalSidewaysCheckBox = new CheckBox(null, skin);
        movesTable.add(kingMoveOrthogonalSidewaysCheckBox);
        kingCaptureOrthogonalSidewaysCheckBox = new CheckBox(null, skin);
        movesTable.add(kingCaptureOrthogonalSidewaysCheckBox);
        movesTable.row();
        movesTable.add(new Label("Orthogonal backward", skin));
        manMoveOrthogonalBackwardCheckBox = new CheckBox(null, skin);
        movesTable.add(manMoveOrthogonalBackwardCheckBox);
        manCaptureOrthogonalBackwardCheckBox = new CheckBox(null, skin);
        movesTable.add(manCaptureOrthogonalBackwardCheckBox);
        kingMoveOrthogonalBackwardCheckBox = new CheckBox(null, skin);
        movesTable.add(kingMoveOrthogonalBackwardCheckBox);
        kingCaptureOrthogonalBackwardCheckBox = new CheckBox(null, skin);
        movesTable.add(kingCaptureOrthogonalBackwardCheckBox);
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
        Label kingsRowCapturePromotionLabel = new Label("Kings row capture promotion", skin);
        optionsTable.add(kingsRowCapturePromotionLabel);
        kingsRowCapturePromotionSpinner = new SelectBox<>(skin);
        kingsRowCapturePromotionSpinner.setItems(KingsRowCapture.values());
        optionsTable.add(kingsRowCapturePromotionSpinner);
        optionsTable.row();

        // Remove pieces immediately
        Label removePiecesImmediatelyLabel = new Label("Remove pieces immediately", skin);
        optionsTable.add(removePiecesImmediatelyLabel);
        removePiecesImmediatelyCheckBox = new CheckBox(null, skin);
        optionsTable.add(removePiecesImmediatelyCheckBox);
        optionsTable.row();

        // Man can capture king
        Label manCanCaptureKingLabel = new Label("Man can capture king", skin);
        optionsTable.add(manCanCaptureKingLabel);
        manCanCaptureKingCheckBox = new CheckBox(null, skin);
        optionsTable.add(manCanCaptureKingCheckBox);
        optionsTable.row();

        // Quantity rule
        Label quantityRuleLabel = new Label("Quantity rule", skin);
        optionsTable.add(quantityRuleLabel);
        quantityRuleCheckBox = new CheckBox(null, skin);
        optionsTable.add(quantityRuleCheckBox);
        optionsTable.row();

        // Quality rule
        Label qualityRuleLabel = new Label("Quality rule", skin);
        optionsTable.add(qualityRuleLabel);
        qualityRuleCheckBox = new CheckBox(null, skin);
        optionsTable.add(qualityRuleCheckBox);
        optionsTable.row();

        // Priority rule
        Label priorityRuleLabel = new Label("Priority rule", skin);
        optionsTable.add(priorityRuleLabel);
        priorityRuleCheckBox = new CheckBox(null, skin);
        optionsTable.add(priorityRuleCheckBox);
        optionsTable.row();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
        Variant.setCustomRemovePiecesImmediately(removePiecesImmediatelyCheckBox.isChecked());
        Variant.setCustomManCanCaptureKing(manCanCaptureKingCheckBox.isChecked());
        Variant.setCustomQuantityRule(quantityRuleCheckBox.isChecked());
        Variant.setCustomQualityRule(qualityRuleCheckBox.isChecked());
        Variant.setCustomPriorityRule(priorityRuleCheckBox.isChecked());

        ArrayList<RelativeDirection> manMoveDirections = new ArrayList<>();
        if (manMoveDiagonalForwardCheckBox.isChecked())
			manMoveDirections.add(RelativeDirection.DIAGONAL_FORWARD);
        if (manMoveDiagonalBackwardCheckBox.isChecked())
			manMoveDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
        if (manMoveOrthogonalForwardCheckBox.isChecked())
			manMoveDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
        if (manMoveOrthogonalBackwardCheckBox.isChecked())
			manMoveDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
        if (manMoveOrthogonalSidewaysCheckBox.isChecked())
			manMoveDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
        Variant.setCustomManMovementDirections(manMoveDirections.toArray(new RelativeDirection[0]));

        ArrayList<RelativeDirection> kingMoveDirections = new ArrayList<>();
        if (kingMoveDiagonalForwardCheckBox.isChecked())
			kingMoveDirections.add(RelativeDirection.DIAGONAL_FORWARD);
        if (kingMoveDiagonalBackwardCheckBox.isChecked())
			kingMoveDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
        if (kingMoveOrthogonalForwardCheckBox.isChecked())
			kingMoveDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
        if (kingMoveOrthogonalBackwardCheckBox.isChecked())
			kingMoveDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
        if (kingMoveOrthogonalSidewaysCheckBox.isChecked())
			kingMoveDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
        Variant.setCustomKingMovementDirections(kingMoveDirections.toArray(new RelativeDirection[0]));

        ArrayList<RelativeDirection> manCaptureDirections = new ArrayList<>();
        if (manCaptureDiagonalForwardCheckBox.isChecked())
			manCaptureDirections.add(RelativeDirection.DIAGONAL_FORWARD);
        if (manCaptureDiagonalBackwardCheckBox.isChecked())
			manCaptureDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
        if (manCaptureOrthogonalForwardCheckBox.isChecked())
			manCaptureDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
        if (manCaptureOrthogonalBackwardCheckBox.isChecked())
			manCaptureDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
        if (manCaptureOrthogonalSidewaysCheckBox.isChecked())
			manCaptureDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
        Variant.setCustomManCaptureDirections(manCaptureDirections.toArray(new RelativeDirection[0]));

        ArrayList<RelativeDirection> kingCaptureDirections = new ArrayList<>();
        if (kingCaptureDiagonalForwardCheckBox.isChecked())
			kingCaptureDirections.add(RelativeDirection.DIAGONAL_FORWARD);
        if (kingCaptureDiagonalBackwardCheckBox.isChecked())
			kingCaptureDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
        if (kingCaptureOrthogonalForwardCheckBox.isChecked())
			kingCaptureDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
        if (kingCaptureOrthogonalBackwardCheckBox.isChecked())
			kingCaptureDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
        if (kingCaptureOrthogonalSidewaysCheckBox.isChecked())
			kingCaptureDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
        Variant.setCustomKingCaptureDirections(kingCaptureDirections.toArray(new RelativeDirection[0]));
    }
}
