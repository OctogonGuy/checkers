package tech.octopusdragon.checkers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import tech.octopusdragon.checkers.model.Board;
import tech.octopusdragon.checkers.model.Checkers;
import tech.octopusdragon.checkers.model.PlayerType;

public class SettingsScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final Texture whitePieceTexture;
    private final Texture blackPieceTexture;
    private final Image topPlayerImage;
    private final Image bottomPlayerImage;
    private static final float PLAYER_IMAGE_SIZE = 40f;

    public SettingsScreen() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        whitePieceTexture = new Texture(Gdx.files.internal("images/piece_white_man.png"));
        whitePieceTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        blackPieceTexture = new Texture(Gdx.files.internal("images/piece_black_man.png"));
        blackPieceTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // New game
        TextButton newGameButton = new TextButton("New game", skin);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SessionData.application.setScreen(new NewGameScreen());
            }
        });
        table.add(newGameButton);
        table.row();

        // Restart game
        TextButton restartButton = new TextButton("Restart", skin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.game = new Checkers(UserData.game.getVariant());
            }
        });
        table.add(restartButton);
        table.row();

        // Variant info
        TextButton infoButton = new TextButton("Info", skin);
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SessionData.application.setScreen(new VariantInfoScreen(UserData.game.getVariant()));
            }
        });
        table.add(infoButton);
        table.row();

        // Undo
        TextButton undoButton = new TextButton("Undo", skin);
        undoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.game.undoTurn();
            }
        });
        table.add(undoButton);
        table.row();

        // Undo all
        TextButton undoAllButton = new TextButton("Undo all", skin);
        undoAllButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.game.undoAllTurns();
            }
        });
        table.add(undoAllButton);
        table.row();

        // Redo
        TextButton redoButton = new TextButton("Redo", skin);
        redoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.game.redoTurn();
            }
        });
        table.add(redoButton);
        table.row();

        // Redo all
        TextButton redoAllButton = new TextButton("Redo all", skin);
        redoAllButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.game.redoAllTurns();
            }
        });
        table.add(redoAllButton);
        table.row();

        // Highlight moves check box
        CheckBox highlightMovesCheckBox = new CheckBox("Highlight moves", skin);
        highlightMovesCheckBox.setChecked(UserData.highlightMoves);
        highlightMovesCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.highlightMoves = highlightMovesCheckBox.isChecked();
            }
        });
        table.add(highlightMovesCheckBox).colspan(3);
        table.row();

        // Players
        Table playerSettingsTable = new Table();
        playerSettingsTable.defaults().expand();
        Label topPlayerLabel = new Label("Top player", skin);
        playerSettingsTable.add(topPlayerLabel);
        topPlayerImage = new Image();
        topPlayerImage.setSize(PLAYER_IMAGE_SIZE, PLAYER_IMAGE_SIZE);
        topPlayerImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchPlayerColors();
            }
        });
        playerSettingsTable.add(topPlayerImage).size(PLAYER_IMAGE_SIZE, PLAYER_IMAGE_SIZE);
        CheckBox topHumanCheckBox = new CheckBox("Human", skin);
        topHumanCheckBox.setChecked(!UserData.topPlayerComputer);
        topHumanCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.topPlayerComputer = !topHumanCheckBox.isChecked();
            }
        });
        playerSettingsTable.add(topHumanCheckBox);
        CheckBox topComputerCheckBox = new CheckBox("Computer", skin);
        topComputerCheckBox.setChecked(UserData.topPlayerComputer);
        topComputerCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.topPlayerComputer = topComputerCheckBox.isChecked();
            }
        });
        playerSettingsTable.add(topComputerCheckBox);
        ButtonGroup<CheckBox> topPlayerGroup = new ButtonGroup<>(topHumanCheckBox, topComputerCheckBox);
        topPlayerGroup.setMaxCheckCount(1);
        playerSettingsTable.row();
        Label bottomPlayerLabel = new Label("Bottom player", skin);
        playerSettingsTable.add(bottomPlayerLabel);
        bottomPlayerImage = new Image();
        bottomPlayerImage.setSize(PLAYER_IMAGE_SIZE, PLAYER_IMAGE_SIZE);
        bottomPlayerImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchPlayerColors();
            }
        });
        playerSettingsTable.add(bottomPlayerImage).size(PLAYER_IMAGE_SIZE, PLAYER_IMAGE_SIZE);
        CheckBox bottomHumanCheckBox = new CheckBox("Human", skin);
        bottomHumanCheckBox.setChecked(!UserData.bottomPlayerComputer);
        bottomHumanCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.bottomPlayerComputer = !bottomHumanCheckBox.isChecked();
            }
        });
        playerSettingsTable.add(bottomHumanCheckBox);
        CheckBox bottomComputerCheckBox = new CheckBox("Computer", skin);
        bottomComputerCheckBox.setChecked(UserData.bottomPlayerComputer);
        bottomComputerCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.bottomPlayerComputer = bottomComputerCheckBox.isChecked();
            }
        });
        playerSettingsTable.add(bottomComputerCheckBox);
        ButtonGroup<CheckBox> bottomPlayerGroup = new ButtonGroup<>(bottomHumanCheckBox, bottomComputerCheckBox);
        bottomPlayerGroup.setMaxCheckCount(1);
        table.add(playerSettingsTable);
        table.row();

        // Button bar
        HorizontalGroup buttonBar = new HorizontalGroup();
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SessionData.application.lastScreen();
            }
        });
        buttonBar.addActor(backButton);
        table.add(buttonBar);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        topPlayerImage.setDrawable(new SpriteDrawable(new Sprite(
            UserData.topPlayer == PlayerType.BLACK ? blackPieceTexture : whitePieceTexture)));
        bottomPlayerImage.setDrawable(new SpriteDrawable(new Sprite(
            UserData.topPlayer == PlayerType.BLACK ? whitePieceTexture : blackPieceTexture)));

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
     * Switches the top and bottom players' piece colors
     */
    private void switchPlayerColors() {
        UserData.topPlayer = UserData.topPlayer == PlayerType.BLACK ? PlayerType.WHITE : PlayerType.BLACK;
        UserData.game.getBoard().invert();
        Board.setTopPlayerType(UserData.topPlayer);
    }
}
