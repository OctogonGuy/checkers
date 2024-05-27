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
import tech.octopusdragon.checkers.data.UIStyle;
import tech.octopusdragon.checkers.data.UserData;
import tech.octopusdragon.checkers.model.PlayerType;

public class StartingPlayerScreen implements Screen {
    private final Stage stage;
    private final Skin skin;

    public StartingPlayerScreen() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().center();
        table.pad(UIStyle.V_PADDING, UIStyle.H_PADDING, UIStyle.V_PADDING, UIStyle.H_PADDING);
        table.defaults().padTop(UIStyle.V_SPACING);
        stage.addActor(table);

        // Starting player select
        HorizontalGroup startingPlayerSelect = new HorizontalGroup().space(UIStyle.H_SPACING);
        Label startingPlayerLabel = new Label("Starting player", skin);
        startingPlayerSelect.addActor(startingPlayerLabel);
        SelectBox<PlayerType> startingPlayerSelectBox = new SelectBox<>(skin);
        startingPlayerSelectBox.setItems(PlayerType.values());
        startingPlayerSelect.addActor(startingPlayerSelectBox);
        table.add(startingPlayerSelect).space(0).expandY().fill();
        table.row();

        // Button bar
        HorizontalGroup buttonBar = new HorizontalGroup().space(UIStyle.BUTTON_BAR_SPACING);
        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserData.game.setStartingPlayer(startingPlayerSelectBox.getSelected());
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
