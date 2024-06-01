package tech.octopusdragon.checkers.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import tech.octopusdragon.checkers.data.SessionData;
import tech.octopusdragon.checkers.data.UserData;
import tech.octopusdragon.checkers.model.*;
import tech.octopusdragon.checkers.model.rules.BoardPattern;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameScreen implements Screen {
    protected final SpriteBatch batch;
    protected final GameInputProcessor input;
    protected final Viewport viewport;
    protected final Viewport boardViewport;
    protected final Viewport outerViewport;
    protected final Viewport topViewport;
    protected final Viewport bottomViewport;
    protected final Viewport backgroundViewport;
    protected final Texture whiteManPiece;
    protected final Texture whiteKingPiece;
    protected final Texture blackManPiece;
    protected final Texture blackKingPiece;
    protected final Texture highlight;
    protected final Texture select;
    protected final Texture huffingConfirm;
    protected final Texture whiteAlternatingSpace;
    protected final Texture whiteAllSpace;
    protected final Texture blackAlternatingSpace;
    protected final Texture blackAllSpace;
    protected final Texture filipinoEvenCornerSpace;
    protected final Texture filipinoOddCornerSpace;
    protected final Texture filipinoEvenEdgeSpace;
    protected final Texture filipinoOddEdgeSpace;
    protected final Texture filipinoEvenCenterSpace;
    protected final Texture filipinoOddCenterSpace;
    protected final Texture menuButtonTexture;
    protected final Texture background;
    protected final BitmapFont font;
    protected final GlyphLayout topGlyphLayout;
    protected final GlyphLayout bottomGlyphLayout;
    protected final Map<PlayerType, GlyphLayout> messageLabels;
    protected final ShapeRenderer boardBorder;
    protected final ShapeRenderer hoverBox;
    protected Sprite movingPiece;
    protected Map<Position, Sprite> capturedSprites;
    protected Sprite menuButton;
    protected float highlightOpacity;   // Highlight opacity oscillates between 0.0 and 1.0
    protected boolean highlightOpacityIncreasing;   // Whether highlight opacity is increasing
    protected Position selectedSpace;   // Space that has been selected, if any
    protected boolean dragging; // Whether a piece is being dragged
    protected boolean clickedOnPiece;   // Whether a piece has been clicked on
    protected float moveAnimationTimeLeft;  // Seconds left until piece move animation is over
    protected boolean animating;    // Whether a piece is undergoing a move animation
    protected Map<Position, Boolean> captureAnimating;  // Whether a piece is undergoing a capture animation
    protected List<Position> captures;  // Current captures in order
    protected float timeUntilNextCaptureAnimation;  // Time until next capture animation
    protected Move curMove; // The move being animated
    protected String messageToRender;   // The current custom message to render, if there is one
    protected boolean computerMoving;   // Whether the computer is trying to make a move
    protected Move computerMove;     // The computer's move if any
    protected static final float BORDER_WIDTH = 0.2f;   // Width of board border
    protected static final float VERT_GUTTER = 0.12f; // Height of top and bottom gutters
    protected static final float HORIZ_GUTTER = 0.05f;  // Width of left and right gutters
    protected static final float HIGHLIGHT_INTERVAL = 1.0f; // Oscillation duration in seconds
    protected static final float MOVE_ANIMATION_DURATION = 1.0f;    // Duration of move animation
    protected static final float CAPTURE_ANIMATION_DURATION = 0.5f; // Duration of capture animation
    protected static final float CAPTURE_SCALE = 2.0f;  // The scale to which checkers will grow when captured
    protected static final float PIECE_PROPORTION = 0.9f;   // Proportion of square to which piece will extend
    protected static final float PIECE_HIGHLIGHT_PROPORTION = 1.2f;   // Proportion of square to which glow will extend
    protected static final float SPACE_HIGHLIGHT_PROPORTION = 0.8f;   // Proportion of square to which glow will extend
    protected static final float MULTI_CAPTURE_INTERVAL = 0.2f; // Time between multiple capture animations
    protected static final float MENU_BUTTON_PADDING = 5;   // Padding around menu button
    protected static final int CHANGING_MESSAGE_TIMEOUT = 1000; // Time for computer thinking message (ms)
    protected final float FONT_LINE_HEIGHT;
    protected final float FONT_HEIGHT;

    public GameScreen() {
        this.batch = SessionData.application.batch;

        input = new GameInputProcessor(SessionData.application, this);
        Gdx.input.setInputProcessor(input);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        viewport = new ScreenViewport();
        boardViewport = new FitViewport(UserData.game.getBoard().getCols(), UserData.game.getBoard().getRows());
        outerViewport = new FitViewport(1, 1);
        topViewport = new ScreenViewport();
        topViewport.getCamera().rotate(180, 0, 0, 1);
        bottomViewport = new ScreenViewport();
        backgroundViewport = new FillViewport(800, 800);

        menuButtonTexture = new Texture(Gdx.files.internal("images/menu_button.png"));
        menuButton = new Sprite(menuButtonTexture);
        menuButton.setOrigin(0, 0);

        whiteManPiece = new Texture(Gdx.files.internal("images/piece_white_man.png"));
        whiteManPiece.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        whiteKingPiece = new Texture(Gdx.files.internal("images/piece_white_king.png"));
        whiteKingPiece.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        blackManPiece = new Texture(Gdx.files.internal("images/piece_black_man.png"));
        blackManPiece.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        blackKingPiece = new Texture(Gdx.files.internal("images/piece_black_king.png"));
        blackKingPiece.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        highlight = new Texture(Gdx.files.internal("images/highlight.png"));
        highlight.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        select = new Texture(Gdx.files.internal("images/select.png"));
        select.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        huffingConfirm = new Texture(Gdx.files.internal("images/huffing_confirm.png"));
        huffingConfirm.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        whiteAlternatingSpace = new Texture(Gdx.files.internal("images/square_light_alternating.jpg"));
        whiteAlternatingSpace.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        whiteAllSpace = new Texture(Gdx.files.internal("images/square_light_all.jpg"));
        whiteAllSpace.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        blackAlternatingSpace = new Texture(Gdx.files.internal("images/square_dark_alternating.jpg"));
        blackAlternatingSpace.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        blackAllSpace = new Texture(Gdx.files.internal("images/square_dark_all.jpg"));
        blackAllSpace.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        filipinoEvenCornerSpace = new Texture(Gdx.files.internal("images/square_filipino_even_corner_tl.jpg"));
        filipinoEvenCornerSpace.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        filipinoOddCornerSpace = new Texture(Gdx.files.internal("images/square_filipino_odd_corner_tl.jpg"));
        filipinoOddCornerSpace.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        filipinoEvenEdgeSpace = new Texture(Gdx.files.internal("images/square_filipino_even_edge_t.jpg"));
        filipinoEvenEdgeSpace.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        filipinoOddEdgeSpace = new Texture(Gdx.files.internal("images/square_filipino_odd_edge_t.jpg"));
        filipinoOddEdgeSpace.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        filipinoEvenCenterSpace = new Texture(Gdx.files.internal("images/square_filipino_even_center.jpg"));
        filipinoEvenCenterSpace.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        filipinoOddCenterSpace = new Texture(Gdx.files.internal("images/square_filipino_odd_center.jpg"));
        filipinoOddCenterSpace.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        background = new Texture(Gdx.files.internal("images/table.jpg"));
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("fonts/Lora_game.fnt"));
        font.setColor(Color.BLACK);
        topGlyphLayout = new GlyphLayout(font, "");
        bottomGlyphLayout = new GlyphLayout(font, "");
        boardBorder = new ShapeRenderer();
        hoverBox = new ShapeRenderer();

        FONT_LINE_HEIGHT = font.getLineHeight();
        FONT_HEIGHT = font.getCapHeight();

        messageLabels = new HashMap<>();
        messageLabels.put(Board.getTopPlayerType(), topGlyphLayout);
        messageLabels.put(Board.bottomPlayerType(), bottomGlyphLayout);

        highlightOpacity = 1.0f;
        highlightOpacityIncreasing = false;

        captureAnimating = new HashMap<>();
        capturedSprites = new HashMap<>();
        captures = new LinkedList<>();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {

        oscillateHighlight(deltaTime);
        countDownMoveAnimationTimeLeft(deltaTime);
        countDownMultiCaptureAnimation(deltaTime);
        determineComputerMove();
        makeComputerMove();

        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.getCamera().update();

        renderBackground();
        renderBoardBorder();
        renderMessages();
        renderMenuButton();
        renderBoard();
        renderHighlight();
        renderHuffingConfirm();
        renderSelect();
        renderHoverBox();
        renderPieces();
        renderCaptureAnimationFrame(deltaTime);
        renderMoveAnimationFrame(deltaTime);
        renderPieceAtCursorLocation();
    }

    @Override
    public void resize(int width, int height) {
        // Font size
        try {
            font.getData().setScale(
                2*(Math.min(height, width) + FONT_LINE_HEIGHT + FONT_HEIGHT) / FONT_LINE_HEIGHT / FONT_HEIGHT);
        } catch (IllegalArgumentException ignored) { }
        // Menu button size
        menuButton.setScale(font.getScaleX());
        // Viewport sizes
        viewport.update(width, height, true);
        viewport.setScreenPosition(0, 0);
        boardViewport.update(
            (int)(width * (1 - HORIZ_GUTTER * 2)),
            (int)(height * (1 - VERT_GUTTER * 2)),
            true);
        boardViewport.setScreenPosition(
            (width - boardViewport.getScreenWidth()) / 2,
            (height - boardViewport.getScreenHeight()) / 2);
        Vector2 borderCorner = boardViewport.project(
            new Vector2(
                UserData.game.getBoard().getCols() + BORDER_WIDTH,
                UserData.game.getBoard().getRows() + BORDER_WIDTH));
        Vector2 borderPos = boardViewport.project(
            new Vector2(-BORDER_WIDTH, -BORDER_WIDTH));
        outerViewport.update((int)(borderCorner.x - borderPos.x), (int)(borderCorner.y - borderPos.y), true);
        outerViewport.setScreenPosition((int)borderPos.x, (int)borderPos.y);
        topViewport.update(width, outerViewport.getTopGutterHeight(), true);
        topViewport.setScreenPosition(0, outerViewport.getTopGutterY());
        bottomViewport.update(width, outerViewport.getBottomGutterHeight(), true);
        bottomViewport.setScreenPosition(0, 0);
        backgroundViewport.update(width, height, true);
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
        font.dispose();
        whiteManPiece.dispose();
        whiteKingPiece.dispose();
        blackManPiece.dispose();
        blackKingPiece.dispose();
        highlight.dispose();
        select.dispose();
        whiteAlternatingSpace.dispose();
        whiteAllSpace.dispose();
        blackAlternatingSpace.dispose();
        blackAllSpace.dispose();
        filipinoEvenCornerSpace.dispose();
        filipinoOddCornerSpace.dispose();
        filipinoEvenEdgeSpace.dispose();
        filipinoOddEdgeSpace.dispose();
        filipinoEvenCenterSpace.dispose();
        filipinoOddCenterSpace.dispose();
        menuButtonTexture.dispose();
    }

    // --- Render methods ---

    /**
     * Draws the border around the board
     */
    protected void renderBoardBorder() {
        outerViewport.apply();
        boardBorder.setProjectionMatrix(outerViewport.getCamera().combined);
        boardBorder.begin(ShapeRenderer.ShapeType.Filled);
        boardBorder.setColor(new Color(0x664734ff));
        boardBorder.rect(0, 0, 1, 1);
        boardBorder.end();
    }

    /**
     * Draws the background
     */
    protected void renderBackground() {
        backgroundViewport.apply();
        batch.setProjectionMatrix(backgroundViewport.getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();
    }

    /**
     * Shows relevant turn-based information to the top and bottom players
     */
    protected void renderMessages() {
        // Render message if there is no custom message
        if (messageToRender == null) {
            changeMessage();
        }
        // Render new custom message if there is one
        else {
            changeMessage(messageToRender);
        }

        // Draw the piece graphics
        Texture topGraphic, bottomGraphic;
        PlayerType topPlayerType = UserData.game.getTopPlayer().getType();
        PlayerType bottomPlayerType = UserData.game.getBottomPlayer().getType();
        PlayerType curPlayerType = UserData.game.getCurPlayer().getType();;
        // If game is over, draw each player's piece
        if (UserData.game.isOver()) {
            topGraphic = topPlayerType == PlayerType.BLACK ? blackManPiece : whiteManPiece;
            bottomGraphic = bottomPlayerType == PlayerType.BLACK ? blackManPiece : whiteManPiece;
        }
        // Otherwise, draw the current player's piece
        else {
            topGraphic = bottomGraphic = curPlayerType == PlayerType.BLACK ? blackManPiece : whiteManPiece;
        }
        // Draw the piece graphics
        topViewport.apply();
        batch.setProjectionMatrix(topViewport.getCamera().combined);
        batch.begin();
        batch.enableBlending();
        font.draw(batch, topGlyphLayout,
            topViewport.getWorldWidth() - topGlyphLayout.width - topGlyphLayout.height * 4,
            topViewport.getWorldHeight() / 2 + topGlyphLayout.height / 2);
        batch.draw(topGraphic,
            topViewport.getWorldWidth() - topGlyphLayout.height * 3,
            topViewport.getWorldHeight() / 2 - topGlyphLayout.height,
            topGlyphLayout.height * 2,
            topGlyphLayout.height * 2);
        batch.disableBlending();
        batch.end();
        bottomViewport.apply();
        batch.setProjectionMatrix(bottomViewport.getCamera().combined);
        batch.begin();
        batch.enableBlending();
        font.draw(batch, bottomGlyphLayout,
            bottomViewport.getWorldWidth() - bottomGlyphLayout.width - bottomGlyphLayout.height * 4,
            bottomViewport.getWorldHeight() / 2 + bottomGlyphLayout.height / 2);
        batch.draw(bottomGraphic,
            topViewport.getWorldWidth() - bottomGlyphLayout.height * 3,
            topViewport.getWorldHeight() / 2 - bottomGlyphLayout.height,
            bottomGlyphLayout.height * 2,
            bottomGlyphLayout.height * 2);
        batch.disableBlending();
        batch.end();
    }

    /**
     * Draws the board squares
     */
    protected void renderBoard() {
        boardViewport.apply();
        batch.setProjectionMatrix(boardViewport.getCamera().combined);
        batch.begin();
        for (int i = UserData.game.getBoard().getRows() - 1; i >= 0; i--) {
            for (int j = 0; j < UserData.game.getBoard().getCols(); j++) {
                BoardPattern pattern = UserData.game.getBoard().getPattern();
                Texture texture = null;
                int row = gameRowToWorldY(i);
                switch (pattern) {
                    case ALL_BLACK:
                        texture = blackAllSpace;
                        break;
                    case ALL_WHITE:
                        texture = whiteAllSpace;
                        break;
                    case BOTTOM_LEFT_SQUARE_BLACK:
                        if ((i + j) % 2 == 0)
                            texture = blackAlternatingSpace;
                        else
                            texture = whiteAlternatingSpace;
                        break;
                    case BOTTOM_LEFT_SQUARE_WHITE:
                        if ((i + j) % 2 == 0)
                            texture = whiteAlternatingSpace;
                        else
                            texture = blackAlternatingSpace;
                        break;
                    case FILIPINO:
                        if ((row == 0 || row == UserData.game.getBoard().getRows() - 1)
                            && (j == 0 || j == UserData.game.getBoard().getCols() - 1)) {
                            if ((row + j) % 2 == 0)
                                texture = filipinoEvenCornerSpace;
                            else
                                texture = filipinoOddCornerSpace;
                        }
                        else if (row == 0 || j == 0
                            || row == UserData.game.getBoard().getRows() - 1
                            || j == UserData.game.getBoard().getCols() - 1) {
                            if ((row + j) % 2 == 0)
                                texture = filipinoEvenEdgeSpace;
                            else
                                texture = filipinoOddEdgeSpace;
                        }
                        else {
                            if ((row + j) % 2 == 0)
                                texture = filipinoEvenCenterSpace;
                            else
                                texture = filipinoOddCenterSpace;
                        }

                }
                Sprite sprite = new Sprite(texture);
                sprite.setPosition(j, i);
                sprite.setSize(1, 1);
                sprite.setOrigin(0.5f, 0.5f);
                if (pattern == BoardPattern.FILIPINO) {
                    if (j == UserData.game.getBoard().getCols() - 1 && row != UserData.game.getBoard().getRows() - 1)
                        sprite.rotate(270);
                    else if (row == UserData.game.getBoard().getRows() - 1 && j != 0)
                        sprite.rotate(180);
                    else if (j == 0 && row != 0)
                        sprite.rotate(90);
                }
                sprite.draw(batch);
            }
        }
        batch.end();
    }

    /**
     * Draws a highlight graphic for movable pieces or spaces
     */
    protected void renderHighlight() {
        if (UserData.highlightMoves && !animating && !computerMoving && !UserData.game.isOver()) {
            boardViewport.apply();
            batch.setProjectionMatrix(boardViewport.getCamera().combined);
            batch.begin();
            batch.enableBlending();
            Color color = batch.getColor();
            color.a = highlightOpacity;
            batch.setColor(color);
            if (selectedSpace == null) {
                for (Piece piece : UserData.game.movablePieces()) {
                    Position space = UserData.game.getBoard().getPosition(piece);
                    batch.draw(highlight,
                        space.getCol() + (1.0f - PIECE_HIGHLIGHT_PROPORTION) / 2,
                        gameRowToWorldY(space.getRow()) + (1.0f - PIECE_HIGHLIGHT_PROPORTION) / 2,
                        PIECE_HIGHLIGHT_PROPORTION, PIECE_HIGHLIGHT_PROPORTION);
                }
            }
            else {
                for (Move move : UserData.game.validMoves(UserData.game.getBoard().getPiece(selectedSpace))) {
                    Position space = move.getToPos();
                    batch.draw(highlight,
                        space.getCol() + (1.0f - SPACE_HIGHLIGHT_PROPORTION) / 2,
                        gameRowToWorldY(space.getRow()) + (1.0f - SPACE_HIGHLIGHT_PROPORTION) / 2,
                        SPACE_HIGHLIGHT_PROPORTION, SPACE_HIGHLIGHT_PROPORTION);
                }
            }
            color.a = 1;
            batch.setColor(color);
            batch.disableBlending();
            batch.end();
        }
    }

    /**
     * Draws a select graphic for the selected piece if there is one
     */
    protected void renderSelect() {
        if (selectedSpace != null && !animating && !dragging && !computerMoving && !UserData.game.isOver()
            && (!UserData.game.hasCaptured() || !UserData.game.getVariant().isHuffing())) {
            boardViewport.apply();
            batch.setProjectionMatrix(boardViewport.getCamera().combined);
            batch.begin();
            batch.enableBlending();
            batch.draw(select,
                selectedSpace.getCol() + (1.0f - PIECE_HIGHLIGHT_PROPORTION) / 2,
                gameRowToWorldY(selectedSpace.getRow()) + (1.0f - PIECE_HIGHLIGHT_PROPORTION) / 2,
                PIECE_HIGHLIGHT_PROPORTION, PIECE_HIGHLIGHT_PROPORTION);
            batch.disableBlending();
            batch.end();
        }
    }

    /**
     * Draws a confirmation graphic for a piece that has captured if huffing rules apply
     */
    protected void renderHuffingConfirm() {
        if (selectedSpace != null && !animating && !dragging && !computerMoving && !UserData.game.isOver()
            && UserData.game.hasCaptured() && UserData.game.getVariant().isHuffing()) {
            boardViewport.apply();
            batch.setProjectionMatrix(boardViewport.getCamera().combined);
            batch.begin();
            batch.enableBlending();
            batch.draw(huffingConfirm,
                selectedSpace.getCol() + (1.0f - PIECE_HIGHLIGHT_PROPORTION) / 2,
                gameRowToWorldY(selectedSpace.getRow()) + (1.0f - PIECE_HIGHLIGHT_PROPORTION) / 2,
                PIECE_HIGHLIGHT_PROPORTION, PIECE_HIGHLIGHT_PROPORTION);
            batch.disableBlending();
            batch.end();
        }
    }

    /**
     * Draws a box around the hovered over square if dragging a piece to a movable space
     */
    protected void renderHoverBox() {
        if (dragging) {
            Position clickedSpace = clickedSpace();
            if (clickedSpace != null && UserData.game.isValidMove(new Move(selectedSpace, clickedSpace))) {
                hoverBox.setProjectionMatrix(boardViewport.getCamera().combined);
                hoverBox.begin(ShapeRenderer.ShapeType.Filled);
                hoverBox.setColor(new Color(0xffff00ff));
                hoverBox.rect(
                    clickedSpace.getCol(), gameRowToWorldY(clickedSpace.getRow()),
                    0.1f, 1);
                hoverBox.rect(
                    clickedSpace.getCol(), gameRowToWorldY(clickedSpace.getRow()) + 0.9f,
                    1, 0.1f);
                hoverBox.rect(
                    clickedSpace.getCol() + 0.9f, gameRowToWorldY(clickedSpace.getRow()),
                    0.1f, 1);
                hoverBox.rect(
                    clickedSpace.getCol(), gameRowToWorldY(clickedSpace.getRow()),
                    1, 0.1f);
                hoverBox.end();
            }
        }
    }

    /**
     * Draws the pieces on the board
     */
    protected void renderPieces() {
        boardViewport.apply();
        batch.setProjectionMatrix(boardViewport.getCamera().combined);
        batch.begin();
        batch.enableBlending();
        for (int i = 0; i < UserData.game.getBoard().getRows(); i++) {
            for (int j = 0; j < UserData.game.getBoard().getCols(); j++) {
                if (UserData.game.getBoard().getPiece(i, j) == null
                    || ((dragging || animating) && selectedSpace != null
                        && selectedSpace.getRow() == i && selectedSpace.getCol() == j)
                    || (captureAnimating.containsKey(new Position(i, j))))
                    continue;
                float rotation = UserData.game.getBoard().getPiece(i, j).getPlayerType()
                    == UserData.game.topPlayer().getType() ? 180 : 0;
                batch.draw(new TextureRegion(getPieceTexture(i, j)),
                    j + (1.0f - PIECE_PROPORTION) / 2, gameRowToWorldY(i) + (1.0f - PIECE_PROPORTION) / 2,
                    PIECE_PROPORTION / 2, PIECE_PROPORTION / 2,
                    PIECE_PROPORTION, PIECE_PROPORTION,
                    1, 1,
                    rotation);
            }
        }
        batch.disableBlending();
        batch.end();
    }

    /**
     * Draws a piece at the cursor's location if dragging a piece
     */
    protected void renderPieceAtCursorLocation() {
        if (dragging && selectedSpace != null) {
            Vector2 coords = boardViewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            boardViewport.apply();
            batch.setProjectionMatrix(boardViewport.getCamera().combined);
            batch.begin();
            batch.enableBlending();
            movingPiece.setCenter(coords.x, coords.y);
            movingPiece.setSize(PIECE_PROPORTION, PIECE_PROPORTION);
            movingPiece.setOrigin(PIECE_PROPORTION / 2, PIECE_PROPORTION / 2);
            if (UserData.game.getBoard().getPiece(selectedSpace).getPlayerType()
                == UserData.game.topPlayer().getType())
                movingPiece.rotate(180);
            movingPiece.draw(batch);
            batch.disableBlending();
            batch.end();
        }
    }

    /**
     * Draws a frame of a piece's movement animation if a piece is currently moving
     * @param deltaTime Amount of time passed since last frame
     */
    protected void renderMoveAnimationFrame(float deltaTime) {
        if (animating && !computerMoving) {
            batch.setProjectionMatrix(boardViewport.getCamera().combined);
            batch.begin();
            batch.enableBlending();
            int deltaX = moveAnimationTimeLeft > 0 ?
                curMove.getToPos().getCol() - curMove.getFromPos().getCol() : 0;
            int deltaY = moveAnimationTimeLeft > 0 ?
                gameRowToWorldY(curMove.getToPos().getRow()) - gameRowToWorldY(curMove.getFromPos().getRow()) : 0;
            movingPiece.setX(movingPiece.getX() + deltaX * (deltaTime / MOVE_ANIMATION_DURATION));
            movingPiece.setY(movingPiece.getY() + deltaY * (deltaTime / MOVE_ANIMATION_DURATION));
            for (Position capturedPosition : capturedSprites.keySet()) {
                Sprite capturedSprite = capturedSprites.get(capturedPosition);
                if ((Math.signum((movingPiece.getX() + 0.5) - (capturedSprite.getX() + 0.5))
                        != Math.signum((movingPiece.getX() + 0.5 - deltaX) - (capturedSprite.getX() + 0.5))
                    || Math.signum((movingPiece.getY() + 0.5) - (capturedSprite.getY() + 0.5))
                        != Math.signum((movingPiece.getY() + 0.5 - deltaY) - (capturedSprite.getY() + 0.5)))
                    && UserData.game.getVariant().isRemovePiecesImmediately()) {
                    captureAnimating.put(capturedPosition, true);
                }
            }
            // Make the actual move in the game
            if (moveAnimationTimeLeft <= 0) {
                if (!captures.isEmpty() &&  !UserData.game.getVariant().isHuffing()
                    && (UserData.game.getVariant().isRemovePiecesImmediately()
                        || !UserData.game.canMultiCapture(UserData.game.getBoard().getPiece(curMove.getFromPos())))) {
                        captureAnimating.put(captures.removeFirst(), true);
                        timeUntilNextCaptureAnimation = MULTI_CAPTURE_INTERVAL;
                }
                animating = false;
                UserData.game.move(curMove);
                // Remove huffing piece if missed capture
                if (UserData.game.getVariant().isHuffing() && UserData.game.missedCapture()) {
                    Position pos = UserData.game.getBoard().getPosition(UserData.game.missedCapturePiece());
                    addSpriteToCaptureSprites(pos);
                    captureAnimating.put(pos, true);
                    timeUntilNextCaptureAnimation = MULTI_CAPTURE_INTERVAL;
                    UserData.game.endTurn();
                }
                else if (!UserData.game.hasCaptured() && UserData.game.getVariant().isHuffing())
                    UserData.game.endTurn();
                selectedSpace = null;
                if (!UserData.game.getVariant().isHuffing()
                    && (UserData.game.canCapture(UserData.game.getBoard().getPiece(curMove.getToPos()))
                    || (UserData.game.getVariant().isHuffing() && UserData.game.hasCaptured())))
                    selectedSpace = curMove.getToPos();
                if (UserData.game.getVariant().isHuffing() && UserData.game.hasCaptured())
                    selectedSpace = curMove.getToPos();
                curMove = null;
            }
            movingPiece.draw(batch);
            batch.disableBlending();
            batch.end();
        }
    }

    /**
     * Draws a frame of a piece's capture animation if a piece is/has been captured
     * @param deltaTime Amount of time passed since last frame
     */
    protected void renderCaptureAnimationFrame(float deltaTime) {
        batch.setProjectionMatrix(boardViewport.getCamera().combined);
        batch.begin();
        batch.enableBlending();
        if (timeUntilNextCaptureAnimation <= 0 && !captures.isEmpty() && captures.size() != captureAnimating.size()) {
            captureAnimating.put(captures.removeFirst(), true);
            timeUntilNextCaptureAnimation = MULTI_CAPTURE_INTERVAL;
        }
        for (Position capturedPosition : captureAnimating.keySet()) {
            if (!capturedSprites.containsKey(capturedPosition)) continue;
            Sprite sprite = capturedSprites.get(capturedPosition);
            if (captureAnimating.get(capturedPosition)) {
                float newSize = sprite.getWidth() + CAPTURE_SCALE * deltaTime / CAPTURE_ANIMATION_DURATION;
                sprite.setSize(newSize, newSize);
                sprite.setAlpha(sprite.getColor().a - deltaTime / CAPTURE_ANIMATION_DURATION);
                sprite.setCenter(
                    capturedPosition.getCol() + 0.5f, gameRowToWorldY(capturedPosition.getRow()) + 0.5f);
                sprite.setOrigin(
                    newSize / 2, newSize / 2);
            }
            if (sprite.getColor().a <= 0) {
                captureAnimating.put(capturedPosition, false);
                capturedSprites.remove(capturedPosition);
            }
            else {
                sprite.draw(batch);
            }
        }
        if (!animating && capturedSprites.isEmpty()) {
            captureAnimating.clear();
        }
        batch.disableBlending();
        batch.end();
    }



    // --- Helper methods ---

    /**
     * Updates the highlight opacity value to show an in and out glowing animation
     */
    protected void oscillateHighlight(float deltaTime) {
        highlightOpacity += (highlightOpacityIncreasing ? 1.0f : -1.0f) * deltaTime / HIGHLIGHT_INTERVAL;
        if (highlightOpacity >= 1.0f) {
            highlightOpacity = 2.0f - highlightOpacity;
            highlightOpacityIncreasing = false;
        }
        else if (highlightOpacity <= 0.0f) {
            highlightOpacity = Math.abs(highlightOpacity);
            highlightOpacityIncreasing = true;
        }
    }

    /**
     * Decrements the time left for the move animation
     * @param deltaTime Seconds passed since last frame
     */
    void countDownMoveAnimationTimeLeft(float deltaTime) {
        if (moveAnimationTimeLeft > 0)
            moveAnimationTimeLeft -= deltaTime;
    }

    /**
     * Decrements the time left until the next capture animation starts
     * @param deltaTime Seconds passed since last frame
     */
    void countDownMultiCaptureAnimation(float deltaTime) {
        if (timeUntilNextCaptureAnimation > 0)
            timeUntilNextCaptureAnimation -= deltaTime;
    }

    /**
     * Displays appropriate messages regarding the current player or state of the game
     */
    protected void changeMessage() {
        for (PlayerType playerType : PlayerType.values()) {
            GlyphLayout messageLabel = messageLabels.get(playerType);
            if (UserData.game.isOver()) {
                Player winner = UserData.game.winner();
                if (winner == null) {
                    messageLabel.setText(font, "It is a draw.");
                }

                else if (playerType == winner.getType()) {
                    messageLabel.setText(font, "You win!");
                }

                else {
                    messageLabel.setText(font, "You lose.");
                }
            }
            else if (playerType == UserData.game.getCurPlayer().getType()) {
                messageLabel.setText(font, "Your turn");
            }
            else {
                messageLabel.setText(font, "Opponent's turn");
            }
        }
    }

    /**
     * Changes the message to the given string
     * @param str The string
     */
    private void changeMessage(String str) {
        for (PlayerType playerType : PlayerType.values()) {
            GlyphLayout messageLabel = messageLabels.get(playerType);
            messageLabel.setText(font, str);
        }
    }

    /**
     * Starts the move animation and eventually causes the actual move to be played.
     * @param move The move to play
     */
    protected void move(Move move) {
        Piece piece = UserData.game.getBoard().getPiece(move.getFromPos());
        movingPiece = new Sprite(getPieceTexture(move.getFromPos()));
        movingPiece.setPosition(
            selectedSpace.getCol() + (1.0f - PIECE_PROPORTION) / 2,
            gameRowToWorldY(selectedSpace.getRow()) + (1.0f - PIECE_PROPORTION) / 2);
        movingPiece.setSize(PIECE_PROPORTION, PIECE_PROPORTION);
        movingPiece.setOrigin(PIECE_PROPORTION / 2, PIECE_PROPORTION / 2);
        if (UserData.game.getBoard().getPiece(move.getFromPos()).getPlayerType()
            == UserData.game.topPlayer().getType())
            movingPiece.rotate(180);
        moveAnimationTimeLeft = MOVE_ANIMATION_DURATION;
        animating = true;
        curMove = move;
        // Add captured positions if there is a capture
        if (UserData.game.isValidCapture(move)) {
            for (Capture capture : UserData.game.validCaptures(piece)) {
                Position capturePos = capture.getCapturePos();
                if (capture.getToPos().getRow() == move.getToPos().getRow()
                        && capture.getToPos().getCol() == move.getToPos().getCol()) {
                    captureAnimating.put(capturePos, false);
                    Sprite sprite = new Sprite(getPieceTexture(capturePos));
                    sprite.setSize(PIECE_PROPORTION, PIECE_PROPORTION);
                    sprite.setCenter(capturePos.getCol() + 0.5f, gameRowToWorldY(capturePos.getRow()) + 0.5f);
                    sprite.setOrigin(PIECE_PROPORTION / 2, PIECE_PROPORTION / 2);
                    if (UserData.game.getBoard().getPiece(capturePos).getPlayerType()
                        == UserData.game.topPlayer().getType())
                        sprite.rotate(180);
                    capturedSprites.put(capturePos, sprite);
                    captures.add(capturePos);
                    timeUntilNextCaptureAnimation = 0;
                    break;
                }
            }
        }
    }

    /**
     * Converts a row to the corresponding y-coordinate in world units
     * @param row The row
     * @return The y-coordinate in world units
     */
    protected int gameRowToWorldY(int row) {
        return UserData.game.getBoard().getRows() - 1 - row;
    }

    /**
     * Returns the corresponding texture of the piece at the given position
     * @param position The position of the piece
     * @return The texture
     */
    protected Texture getPieceTexture(Position position) {
        return getPieceTexture(position.getRow(), position.getCol());
    }

    /**
     * Returns the corresponding texture of the piece at the given position
     * @param row The row of the piece on the board
     * @param column The column of the piece on the board
     * @return The texture
     */
    protected Texture getPieceTexture(int row, int column) {
        switch (UserData.game.getBoard().getPiece(row, column).getType()) {
            case BLACK_MAN:
                return blackManPiece;
            case WHITE_MAN:
                return whiteManPiece;
            case BLACK_KING:
                return blackKingPiece;
            case WHITE_KING:
                return whiteKingPiece;
            default:
                return null;
        }
    }

    /**
     * @return The clicked space or null if invalid
     */
    protected Position clickedSpace() {
        Vector2 coords = boardViewport.unproject(new Vector2().set(Gdx.input.getX(), Gdx.input.getY()));
        Position pos = new Position((int)(UserData.game.getBoard().getRows() - coords.y), (int)coords.x);
        try {
            UserData.game.getBoard().isOccupied(pos);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return pos;
    }

    /**
     * Adds a sprite to captured sprites
     * @param pos The position of the piece to add
     */
    protected void addSpriteToCaptureSprites(Position pos) {
        Sprite sprite = new Sprite(getPieceTexture(pos));
        sprite.setSize(PIECE_PROPORTION, PIECE_PROPORTION);
        sprite.setCenter(pos.getCol() + 0.5f, gameRowToWorldY(pos.getRow()) + 0.5f);
        sprite.setOrigin(PIECE_PROPORTION / 2, PIECE_PROPORTION / 2);
        if (UserData.game.getBoard().getPiece(pos).getPlayerType() == UserData.game.topPlayer().getType())
            sprite.rotate(180);
        capturedSprites.put(pos, sprite);
    }

    /**
     * Draws the menu button
     */
    protected void renderMenuButton() {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.enableBlending();
        menuButton.setPosition(
            viewport.getScreenWidth() - menuButton.getWidth() * menuButton.getScaleX() - MENU_BUTTON_PADDING,
            viewport.getScreenHeight() - menuButton.getHeight() * menuButton.getScaleX() - MENU_BUTTON_PADDING);
        menuButton.draw(batch);
        batch.disableBlending();
        batch.end();
    }


    /**
     * Makes computer player play move
     */
    private void determineComputerMove() {
        if (animating || computerMoving || UserData.game.isOver()
            || !(UserData.game.getCurPlayer() == UserData.game.topPlayer() && UserData.topPlayerComputer ||
            UserData.game.getCurPlayer() == UserData.game.bottomPlayer() && UserData.bottomPlayerComputer)) return;

        computerMoving = true;

        Thread messageThread = new Thread(() -> {
            int periods = 0;
            boolean running = true;
            while (running) {
                String suffix = ".".repeat(Math.max(0, periods));
                suffix += " ".repeat(Math.max(0, 3 - periods));
                periods++;
                if (periods > 3) periods = 0;
                messageToRender = "Computer player thinking" + suffix;
                try {
                    Thread.sleep(CHANGING_MESSAGE_TIMEOUT);
                } catch (InterruptedException ignored) {
                    running = false;
                }
            }
        });
        messageThread.setDaemon(true);
        messageThread.start();

        Thread thread = new Thread(() -> {
            try {
                computerMove = ComputerPlayer.getMove(UserData.game, 0.5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            messageThread.interrupt();
        });
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Makes and animates the computer move if there is one
     */
    private void makeComputerMove() {
        if (computerMove != null && !UserData.game.isOver()) {
            selectedSpace = computerMove.getFromPos();
            move(computerMove);
            computerMove = null;
            computerMoving = false;
            messageToRender = null;
        }
    }
}
