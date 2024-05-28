package tech.octopusdragon.checkers.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import tech.octopusdragon.checkers.data.SessionData;
import tech.octopusdragon.checkers.data.UserData;
import tech.octopusdragon.checkers.model.Board;
import tech.octopusdragon.checkers.model.Checkers;
import tech.octopusdragon.checkers.model.PlayerType;
import tech.octopusdragon.checkers.view.style.UIStyle;
import tech.octopusdragon.checkers.view.widget.CustomButton;
import tech.octopusdragon.checkers.view.widget.CustomCheckBox;
import tech.octopusdragon.checkers.view.widget.CustomScrollPane;
import tech.octopusdragon.checkers.view.widget.HeaderLabel;

public class OptionsScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final Texture whitePieceTexture;
    private final Texture blackPieceTexture;
    private final Image topPlayerImage;
    private final Image bottomPlayerImage;
    private static final float PLAYER_IMAGE_SIZE = 40f;

    public OptionsScreen() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        whitePieceTexture = new Texture(Gdx.files.internal("images/piece_white_man.png"));
        whitePieceTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        blackPieceTexture = new Texture(Gdx.files.internal("images/piece_black_man.png"));
        blackPieceTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Table table = new Table();
        table.setFillParent(true);
        table.pad(UIStyle.V_PADDING, UIStyle.H_PADDING, UIStyle.V_PADDING, UIStyle.H_PADDING);
        table.defaults().padTop(UIStyle.V_SPACING);
        stage.addActor(table);

        // Header
        HeaderLabel header = new HeaderLabel("Options", skin);
        table.add(header).padTop(0);
        table.row();

        // Settings table
        Table settingsTable = new Table();
        settingsTable.setFillParent(true);
        settingsTable.defaults().padTop(UIStyle.V_SPACING);
        settingsTable.pad(UIStyle.V_PADDING, UIStyle.H_PADDING, UIStyle.V_PADDING, UIStyle.H_PADDING);
        settingsTable.setBackground(skin.getDrawable("innerColor"));
        CustomScrollPane scrollPane = new CustomScrollPane(settingsTable, skin);
        table.add(scrollPane).expand().fill();
        table.row();

        // New game
        HorizontalGroup startButtons = new HorizontalGroup().space(UIStyle.H_SPACING);
        CustomButton newGameButton = new CustomButton("New game", skin);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SessionData.application.setScreen(new NewGameScreen());
            }
        });
        startButtons.addActor(newGameButton);

        // Restart game
        CustomButton restartButton = new CustomButton("Restart", skin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.game = new Checkers(UserData.game.getVariant());
            }
        });
        startButtons.addActor(restartButton);
        settingsTable.add(startButtons).space(0);
        settingsTable.row();

        // Variant info
        HorizontalGroup infoButtons = new HorizontalGroup().space(UIStyle.H_SPACING);
        CustomButton infoButton = new CustomButton("Info", skin);
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SessionData.application.setScreen(new VariantInfoScreen(UserData.game.getVariant()));
            }
        });
        infoButtons.addActor(infoButton);
        settingsTable.add(infoButtons);
        settingsTable.row();

        // Undo
        HorizontalGroup undoButtons = new HorizontalGroup().space(UIStyle.H_SPACING);
        CustomButton undoButton = new CustomButton("Undo", skin);
        undoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.game.undoTurn();
            }
        });
        undoButtons.addActor(undoButton);

        // Undo all
        CustomButton undoAllButton = new CustomButton("Undo all", skin);
        undoAllButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.game.undoAllTurns();
            }
        });
        undoButtons.addActor(undoAllButton);
        settingsTable.add(undoButtons);
        settingsTable.row();

        // Redo
        HorizontalGroup redoButtons = new HorizontalGroup().space(UIStyle.H_SPACING);
        CustomButton redoButton = new CustomButton("Redo", skin);
        redoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.game.redoTurn();
            }
        });
        redoButtons.addActor(redoButton);

        // Redo all
        CustomButton redoAllButton = new CustomButton("Redo all", skin);
        redoAllButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.game.redoAllTurns();
            }
        });
        redoButtons.addActor(redoAllButton);
        settingsTable.add(redoButtons);
        settingsTable.row();

        // Highlight moves check box
        CustomCheckBox highlightMovesCustomCheckBox = new CustomCheckBox("Highlight moves", skin);
        highlightMovesCustomCheckBox.setChecked(UserData.highlightMoves);
        highlightMovesCustomCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.highlightMoves = highlightMovesCustomCheckBox.isChecked();
            }
        });
        settingsTable.add(highlightMovesCustomCheckBox).colspan(3);
        settingsTable.row();

        // Players
        Table playerSettingsTable = new Table();
        playerSettingsTable.defaults().expandX().spaceTop(UIStyle.TABLE_V_SPACING).spaceLeft(UIStyle.TABLE_H_SPACING);
        playerSettingsTable.columnDefaults(0).spaceLeft(0);
        Label topPlayerLabel = new Label("Top player", skin);
        playerSettingsTable.add(topPlayerLabel).spaceTop(0);
        topPlayerImage = new Image();
        topPlayerImage.setSize(PLAYER_IMAGE_SIZE, PLAYER_IMAGE_SIZE);
        topPlayerImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchPlayerColors();
            }
        });
        playerSettingsTable.add(topPlayerImage).size(PLAYER_IMAGE_SIZE, PLAYER_IMAGE_SIZE).spaceTop(0);
        CustomCheckBox topHumanCustomCheckBox = new CustomCheckBox("Human", skin);
        topHumanCustomCheckBox.setChecked(!UserData.topPlayerComputer);
        topHumanCustomCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.topPlayerComputer = !topHumanCustomCheckBox.isChecked();
            }
        });
        playerSettingsTable.add(topHumanCustomCheckBox);
        CustomCheckBox topComputerCustomCheckBox = new CustomCheckBox("Computer", skin);
        topComputerCustomCheckBox.setChecked(UserData.topPlayerComputer);
        topComputerCustomCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.topPlayerComputer = topComputerCustomCheckBox.isChecked();
            }
        });
        playerSettingsTable.add(topComputerCustomCheckBox).spaceTop(0);
        ButtonGroup<CustomCheckBox> topPlayerGroup = new ButtonGroup<>(topHumanCustomCheckBox, topComputerCustomCheckBox);
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
        CustomCheckBox bottomHumanCustomCheckBox = new CustomCheckBox("Human", skin);
        bottomHumanCustomCheckBox.setChecked(!UserData.bottomPlayerComputer);
        bottomHumanCustomCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.bottomPlayerComputer = !bottomHumanCustomCheckBox.isChecked();
            }
        });
        playerSettingsTable.add(bottomHumanCustomCheckBox);
        CustomCheckBox bottomComputerCustomCheckBox = new CustomCheckBox("Computer", skin);
        bottomComputerCustomCheckBox.setChecked(UserData.bottomPlayerComputer);
        bottomComputerCustomCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.bottomPlayerComputer = bottomComputerCustomCheckBox.isChecked();
            }
        });
        playerSettingsTable.add(bottomComputerCustomCheckBox);
        ButtonGroup<CustomCheckBox> bottomPlayerGroup = new ButtonGroup<>(bottomHumanCustomCheckBox, bottomComputerCustomCheckBox);
        bottomPlayerGroup.setMaxCheckCount(1);
        settingsTable.add(playerSettingsTable);
        settingsTable.row();

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
     * Switches the top and bottom players' piece colors
     */
    private void switchPlayerColors() {
        UserData.topPlayer = UserData.topPlayer == PlayerType.BLACK ? PlayerType.WHITE : PlayerType.BLACK;
        UserData.game.getBoard().invert();
        Board.setTopPlayerType(UserData.topPlayer);
    }
}
