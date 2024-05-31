package tech.octopusdragon.checkers.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import tech.octopusdragon.checkers.data.SessionData;
import tech.octopusdragon.checkers.data.UserData;
import tech.octopusdragon.checkers.model.Board;
import tech.octopusdragon.checkers.model.Checkers;
import tech.octopusdragon.checkers.model.PlayerType;
import tech.octopusdragon.checkers.model.Variant;
import tech.octopusdragon.checkers.model.rules.StartingPlayer;
import tech.octopusdragon.checkers.view.style.UIStyle;
import tech.octopusdragon.checkers.view.widget.*;

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
        CustomCheckBox highlightMovesCustomCheckBox = new CustomCheckBox("Highlight moves", skin);
        highlightMovesCustomCheckBox.setChecked(UserData.highlightMoves);
        highlightMovesCustomCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.highlightMoves = highlightMovesCustomCheckBox.isChecked();
            }
        });
        table.add(highlightMovesCustomCheckBox).colspan(3);
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
        CustomCheckBox topHumanCustomCheckBox = new CustomCheckBox("Human", skin);
        topHumanCustomCheckBox.setChecked(!UserData.topPlayerComputer);
        playerSettingsTable.add(topHumanCustomCheckBox).spaceTop(0);
        CustomCheckBox topComputerCustomCheckBox = new CustomCheckBox("Computer", skin);
        topComputerCustomCheckBox.setChecked(UserData.topPlayerComputer);
        playerSettingsTable.add(topComputerCustomCheckBox).spaceTop(0);
        ButtonGroup<CustomCheckBox> topPlayerGroup = new ButtonGroup<>(
            topHumanCustomCheckBox, topComputerCustomCheckBox);
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
        playerSettingsTable.add(bottomHumanCustomCheckBox);
        CustomCheckBox bottomComputerCustomCheckBox = new CustomCheckBox("Computer", skin);
        bottomComputerCustomCheckBox.setChecked(UserData.bottomPlayerComputer);
        playerSettingsTable.add(bottomComputerCustomCheckBox);
        ButtonGroup<CustomCheckBox> bottomPlayerGroup = new ButtonGroup<>(
            bottomHumanCustomCheckBox, bottomComputerCustomCheckBox);
        bottomPlayerGroup.setMaxCheckCount(1);
        table.add(playerSettingsTable);
        table.row();

        // Variant tree
        VariantTree variantTree = new VariantTree(skin);
        variantTree.setSelected(UserData.selectedVariant);
        variantTree.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UserData.selectedVariant = variantTree.getSelected();
                System.out.println(UserData.selectedVariant);
            }
        });
        CustomScrollPane listScrollPane = new CustomScrollPane(variantTree, skin);
        listScrollPane.scrollTo(0, variantTree.getSelectedY(), 0, 0);
        table.add(listScrollPane).expand().fill().maxWidth(UIStyle.MAX_WIDTH);
        table.row();

        // Button bar
        HorizontalGroup buttonBar = new HorizontalGroup().space(UIStyle.BUTTON_BAR_SPACING);
        CustomButton playButton = new CustomButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.topPlayer = topPlayer;
                UserData.topPlayerComputer = topComputerCustomCheckBox.isChecked();
                UserData.bottomPlayerComputer = bottomComputerCustomCheckBox.isChecked();
                if (variantTree.getSelected() == Variant.CUSTOM) {
                    SessionData.application.setScreen(new CustomGameScreen());
                }
                else {
                    UserData.game = new Checkers(variantTree.getSelected());
                    if (UserData.game.getVariant().getStartingPlayer() == StartingPlayer.EITHER)
                        SessionData.application.setScreen(new StartingPlayerScreen());
                    else
                        SessionData.application.setScreen(new GameScreen());
                }
            }
        });
        buttonBar.addActor(playButton);
        CustomButton infoButton = new CustomButton("Info", skin);
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SessionData.application.setScreen(new VariantInfoScreen(variantTree.getSelected()));
            }
        });
        buttonBar.addActor(infoButton);
        if (!SessionData.screenHistory.isEmpty()) {
            CustomButton backButton = new CustomButton("Back", skin);
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
