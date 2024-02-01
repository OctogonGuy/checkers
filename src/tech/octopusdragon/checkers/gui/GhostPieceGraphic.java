package tech.octopusdragon.checkers.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.effect.BoxBlur;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * An imaginary piece that shows where a player can move.
 * @author Alex Gill
 *
 */
public class GhostPieceGraphic extends PieceGraphic {

	// --- Constants ---
	// Piece size / square size ratio
	private final static double PIECE_RATIO = 0.55;
	
	// --- UI components
	private Circle piece;
	private BoxBlur blur;
	
	
	/**
	 * Constructs a ghost piece to show where a player can go.
	 * @param size The height and width of the piece
	 */
	public GhostPieceGraphic(double size) {
		super();
		
		buildPiece();
		
		// Set sizes explicitly
		resizePiece(size, size);
		
		// Listen for parent property to set sizes
		parentProperty().addListener((obs, oldVal, newVal) -> {
			autosizePiece();
		});
	}
	
	
	/**
	 * Constructs a ghost piece to show where a player can go that auto-sizes
	 */
	public GhostPieceGraphic() {
		super();
		
		buildPiece();
		
		// Listen for parent property to set sizes
		parentProperty().addListener((obs, oldVal, newVal) -> {
			autosizePiece();
		});
		Platform.runLater(() -> {
			autosizePiece();
		});
	}
	
	
	/**
	 * Builds the piece
	 */
	private void buildPiece() {
		
		// Add CSS classes
		getStyleClass().add("ghost-piece");
		
		// Set the instance variables
		highlighted = false;
		selected = false;
		
		
		// Add the circle
		piece = new Circle();
		piece.setOpacity(0.8);
		this.getChildren().add(piece);
		
		
		// Add a blur effect
		blur = new BoxBlur();
		blur.setIterations(2);
		this.setEffect(blur);
		
		
		// Create an animation that fades the glow in and out
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(true);
		KeyValue kv = new KeyValue(this.opacityProperty(), 0.0);
		KeyFrame kf = new KeyFrame(Duration.millis(FADE_DURATION), kv);
		timeline.getKeyFrames().add(kf);
		timeline.play();
		
		
		// Listen for CSS
		highlightColorProperty().addListener((obs, oldVal, newVal) -> {
			piece.setFill(highlightColorProperty.getValue());
		});
	}
	
	
	/**
	 * Resizes the node based on parent bounds
	 */
	private void autosizePiece() {
		if (getParent() == null) return;
		Bounds bounds = getParent().getLayoutBounds();
		double width = bounds.getWidth();
		double height = bounds.getHeight();
		resize(width, height);
	}
	
	
	/**
	 * Resizes the node
	 * @param width The new width of the node
	 * @param height The new height of the node
	 */
	private void resizePiece(double width, double height) {
		double squareSize = Math.min(width, height);
		double pieceSize = squareSize * PIECE_RATIO;
		double radius = pieceSize / 2;
		
		piece.setCenterX(radius);
		piece.setCenterY(radius);
		piece.setRadius(radius);

		blur.setWidth((squareSize / 2 - radius) / 3);
		blur.setHeight((squareSize / 2 - radius) / 3);
	}
}