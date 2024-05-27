package tech.octopusdragon.checkers;

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
import tech.octopusdragon.checkers.model.Board;
import tech.octopusdragon.checkers.model.Checkers;
import tech.octopusdragon.checkers.model.PlayerType;
import tech.octopusdragon.checkers.model.Variant;
import tech.octopusdragon.checkers.model.rules.StartingPlayer;

public class NewGameScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final Texture whitePieceTexture;
    private final Texture blackPieceTexture;
    private final Image topPlayerImage;
    private final Image bottomPlayerImage;
    private final Texture logoTexture;
    private static final float PLAYER_IMAGE_SIZE = 40f;
    private PlayerType topPlayer;

    public NewGameScreen() {
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

        // Logo
        logoTexture = new Texture(Gdx.files.internal("images/logo.png"));
        Image logo = new Image(logoTexture);
        table.add(logo).size(
            stage.getWidth() / 2,
            logoTexture.getHeight() * (stage.getWidth() / logoTexture.getWidth()) / 2);
        table.row().padTop(0);

        // Subtitle
        Label subtitleLabel = new Label("Select a variant to play.", skin);
        table.add(subtitleLabel);
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
        topPlayer = UserData.topPlayer;
        Table playerSettingsTable = new Table();
        playerSettingsTable.defaults().expandX().spaceTop(UIStyle.TABLE_V_SPACING).spaceLeft(UIStyle.TABLE_H_SPACING);
        playerSettingsTable.columnDefaults(0).spaceLeft(0);
        Label topPlayerLabel = new Label("Top player", skin);
        playerSettingsTable.add(topPlayerLabel).padTop(0);
        topPlayerImage = new Image();
        topPlayerImage.setSize(PLAYER_IMAGE_SIZE, PLAYER_IMAGE_SIZE);
        topPlayerImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchPlayerColors();
            }
        });
        playerSettingsTable.add(topPlayerImage).size(PLAYER_IMAGE_SIZE, PLAYER_IMAGE_SIZE).spaceTop(0);
        CheckBox topHumanCheckBox = new CheckBox("Human", skin);
        topHumanCheckBox.setChecked(!UserData.topPlayerComputer);
        playerSettingsTable.add(topHumanCheckBox).spaceTop(0);
        CheckBox topComputerCheckBox = new CheckBox("Computer", skin);
        topComputerCheckBox.setChecked(UserData.topPlayerComputer);
        playerSettingsTable.add(topComputerCheckBox).spaceTop(0);
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
        playerSettingsTable.add(bottomHumanCheckBox);
        CheckBox bottomComputerCheckBox = new CheckBox("Computer", skin);
        bottomComputerCheckBox.setChecked(UserData.bottomPlayerComputer);
        playerSettingsTable.add(bottomComputerCheckBox);
        ButtonGroup<CheckBox> bottomPlayerGroup = new ButtonGroup<>(bottomHumanCheckBox, bottomComputerCheckBox);
        bottomPlayerGroup.setMaxCheckCount(1);
        table.add(playerSettingsTable);
        table.row();

        // Variant list
        List<Variant> list = new List<>(skin);
        list.setItems(Variant.values());
        ScrollPane listScrollPane = new ScrollPane(list);
        table.add(listScrollPane).expand().fill().maxWidth(UIStyle.MAX_WIDTH);
        table.row();

        // Button bar
        HorizontalGroup buttonBar = new HorizontalGroup().space(UIStyle.BUTTON_BAR_SPACING);
        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.topPlayer = topPlayer;
                UserData.topPlayerComputer = topComputerCheckBox.isChecked();
                UserData.bottomPlayerComputer = bottomComputerCheckBox.isChecked();
                if (list.getSelected() == Variant.CUSTOM) {
                    SessionData.application.setScreen(new CustomGameScreen());
                }
                else {
                    UserData.game = new Checkers(list.getSelected());
                    if (UserData.game.getVariant().getStartingPlayer() == StartingPlayer.EITHER)
                        SessionData.application.setScreen(new StartingPlayerScreen());
                    else
                        SessionData.application.setScreen(new GameScreen());
                }
            }
        });
        buttonBar.addActor(playButton);
        TextButton infoButton = new TextButton("Info", skin);
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SessionData.application.setScreen(new VariantInfoScreen(list.getSelected()));
            }
        });
        buttonBar.addActor(infoButton);
        if (!SessionData.screenHistory.isEmpty()) {
            TextButton backButton = new TextButton("Back", skin);
            backButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    SessionData.application.lastScreen();
                }
            });
            buttonBar.addActor(backButton);
        }
        table.add(buttonBar);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        topPlayerImage.setDrawable(new SpriteDrawable(new Sprite(
            topPlayer == PlayerType.BLACK ? blackPieceTexture : whitePieceTexture)));
        bottomPlayerImage.setDrawable(new SpriteDrawable(new Sprite(
            topPlayer == PlayerType.BLACK ? whitePieceTexture : blackPieceTexture)));

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
        whitePieceTexture.dispose();
        blackPieceTexture.dispose();
        logoTexture.dispose();
    }

    /**
     * Switches the top and bottom players' piece colors
     */
    private void switchPlayerColors() {
        topPlayer = topPlayer == PlayerType.BLACK ? PlayerType.WHITE : PlayerType.BLACK;
        UserData.topPlayer = topPlayer;
        Board.setTopPlayerType(UserData.topPlayer);
    }
}