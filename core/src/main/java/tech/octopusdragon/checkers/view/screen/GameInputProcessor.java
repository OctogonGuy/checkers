package tech.octopusdragon.checkers.view.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import tech.octopusdragon.checkers.CheckersApplication;
import tech.octopusdragon.checkers.data.UserData;
import tech.octopusdragon.checkers.model.Capture;
import tech.octopusdragon.checkers.model.Move;
import tech.octopusdragon.checkers.model.Piece;
import tech.octopusdragon.checkers.model.Position;

public class GameInputProcessor extends InputAdapter {
    private final GameScreen gameScreen;
    private final CheckersApplication application;

    public GameInputProcessor(CheckersApplication application, GameScreen gameScreen) {
        this.application = application;
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (UserData.game.isOver()) return false;
        Position clickedSpace = gameScreen.clickedSpace();
        gameScreen.clickedOnPiece = clickedSpace != null
                && (UserData.game.getBoard().getPiece(clickedSpace) != null
                && (UserData.game.canMove(UserData.game.getBoard().getPiece(clickedSpace)))
                || (UserData.game.getVariant().isHuffing() && UserData.game.hasCaptured()))
                || (gameScreen.selectedSpace != null && clickedSpace != null
                && UserData.game.isValidMove(new Move(gameScreen.selectedSpace, clickedSpace)));
        return button == Input.Buttons.LEFT && pointer <= 0 || gameScreen.animating;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Detect menu button touch
        if (gameScreen.menuButton.getBoundingRectangle().contains(new Vector2(
            screenX, gameScreen.viewport.getScreenHeight() - screenY))) {
            application.setScreen(new OptionsScreen());
            return true;
        }

        if (button != Input.Buttons.LEFT || pointer > 0 || gameScreen.animating || UserData.game.isOver()) return false;

        Position clickedSpace = gameScreen.clickedSpace();

        if (!gameScreen.dragging) {
            // Confirm huffing piece
            if (UserData.game.getVariant().isHuffing() && UserData.game.hasCaptured()) {
                if (UserData.game.missedCapture()) {
                    Position pos = UserData.game.getBoard().getPosition(UserData.game.missedCapturePiece());
                    gameScreen.addSpriteToCaptureSprites(pos);
                    gameScreen.captureAnimating.put(pos, true);
                }
                else {
                    gameScreen.captureAnimating.put(gameScreen.captures.removeFirst(), true);
                }
                gameScreen.timeUntilNextCaptureAnimation = GameScreen.MULTI_CAPTURE_INTERVAL;
                UserData.game.endTurn();
                gameScreen.selectedSpace = null;
            }

            // Select piece
            else if (gameScreen.selectedSpace == null
                && clickedSpace != null
                && UserData.game.getBoard().isOccupied(clickedSpace)
                && UserData.game.canMove(UserData.game.getBoard().getPiece(clickedSpace))) {
                gameScreen.selectedSpace = clickedSpace;
            }

            // Deselect piece
            else if (gameScreen.selectedSpace != null
                && (clickedSpace == null
                || !UserData.game.isValidMove(new Move(gameScreen.selectedSpace, clickedSpace)))) {
                gameScreen.selectedSpace = null;
            }

            // Move piece
            else if (gameScreen.selectedSpace != null) {
                gameScreen.move(new Move(gameScreen.selectedSpace, clickedSpace));
            }
        }
        else {
            // Deselect piece
            if (gameScreen.selectedSpace != null && (clickedSpace == null
                || !UserData.game.isValidMove(new Move(gameScreen.selectedSpace, clickedSpace)))) {
                gameScreen.selectedSpace = null;
            }

            // Move piece
            else if (gameScreen.selectedSpace != null) {
                Move move = new Move(gameScreen.selectedSpace, clickedSpace);

                // Capture animation
                if (UserData.game.isValidCapture(move)) {
                    Piece piece = UserData.game.getBoard().getPiece(move.getFromPos());
                    for (Capture capture : UserData.game.validCaptures(piece)) {
                        Position capturePos = capture.getCapturePos();
                        if (capture.getToPos().getRow() == move.getToPos().getRow()
                            && capture.getToPos().getCol() == move.getToPos().getCol()) {
                            gameScreen.captureAnimating.put(capturePos, false);
                            Sprite sprite = new Sprite(gameScreen.getPieceTexture(capturePos));
                            sprite.setSize(GameScreen.PIECE_PROPORTION, GameScreen.PIECE_PROPORTION);
                            sprite.setCenter(
                                capturePos.getCol() + 0.5f,
                                gameScreen.gameRowToWorldY(capturePos.getRow()) + 0.5f);
                            gameScreen.capturedSprites.put(capturePos, sprite);
                            gameScreen.captures.add(capturePos);
                        }
                        if (!UserData.game.getVariant().isHuffing()
                            && !UserData.game.getVariant().isRemovePiecesImmediately()
                            && !gameScreen.captures.isEmpty()
                            && !UserData.game.canMultiCapture(
                                UserData.game.getBoard().getPiece(gameScreen.selectedSpace))) {
                            gameScreen.captureAnimating.put(gameScreen.captures.removeFirst(), true);
                            gameScreen.timeUntilNextCaptureAnimation = GameScreen.MULTI_CAPTURE_INTERVAL;
                        }
                        else if (!UserData.game.getVariant().isHuffing() && !gameScreen.captures.isEmpty()) {
                            gameScreen.captureAnimating.put(gameScreen.captures.removeFirst(), true);
                        }
                    }
                }

                // Make move
                UserData.game.move(move);
                // Remove huffing piece if missed capture
                if (UserData.game.getVariant().isHuffing() && UserData.game.missedCapture()) {
                    Position pos = UserData.game.getBoard().getPosition(UserData.game.missedCapturePiece());
                    gameScreen.addSpriteToCaptureSprites(pos);
                    gameScreen.captureAnimating.put(pos, true);
                    gameScreen.timeUntilNextCaptureAnimation = GameScreen.MULTI_CAPTURE_INTERVAL;
                }
                UserData.game.endTurn();
                gameScreen.selectedSpace = null;
                if (UserData.game.getVariant().isHuffing() && UserData.game.hasCaptured())
                    gameScreen.selectedSpace = move.getToPos();
            }

            // Finish drag
            gameScreen.dragging = false;
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!gameScreen.clickedOnPiece || UserData.game.isOver()) return false;

        Position clickedSpace = gameScreen.clickedSpace();

        // Select piece
        if (clickedSpace != null && (gameScreen.selectedSpace == null
            && UserData.game.getBoard().isOccupied(clickedSpace)
            && (UserData.game.canMove(UserData.game.getBoard().getPiece(clickedSpace)))
            || clickedSpace.equals(UserData.game.getBoard().getPosition(UserData.game.capturingPiece())))) {
            gameScreen.selectedSpace = clickedSpace;

            // Start drag
            gameScreen.dragging = true;
            gameScreen.movingPiece = new Sprite(gameScreen.getPieceTexture(gameScreen.selectedSpace));
        }

        return true;
    }
}
