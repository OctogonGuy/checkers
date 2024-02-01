package tech.octopusdragon.checkers.gui;

import java.io.IOException;
import java.util.List;

import tech.octopusdragon.checkers.Board;
import tech.octopusdragon.checkers.Piece;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * A graphic of a piece in the game of checkers
 * @author Alex Gill
 *
 */
public class PieceGraphic extends Group {
	
	// --- Constants ---
	// Piece size / square size ratio
	private static final double PIECE_RATIO = 0.82;
	// The edge width as a percent
	private static final double EDGE_WIDTH = 1;
	// The concavity width as a percent
	private static final double CONCAVITY_WIDTH = 0.75;
	// The fade duration in ms
	protected static final double FADE_DURATION = 1500.0;
	// Image of crown to use for decal
	private static final String CROWN_IMAGE = "resources/images/crown.png";

	
	// --- Instance variables ---
	private Piece piece;			// The referenced piece
	protected boolean highlighted;	// Whether the piece is highlighted
	protected boolean selected;		// Whether the piece is selected
	private double radius;			// The radius of the piece
	
	
	// --- UI components ---
	@FXML private Circle outerCircle;
	@FXML private Circle innerCircle;
	@FXML private Circle concavity;
	private InnerShadow concavityShadow;
	private ImageView decal;
	@FXML private Circle decalMask;
	private DropShadow highlight;
	private DropShadow select;
	
	
	// --- Stylable Properties ---
	private static final StyleablePropertyFactory<PieceGraphic> FACTORY =
			new StyleablePropertyFactory<>(Group.getClassCssMetaData());
	// The paint of the graphic
	private StyleableProperty<Color> colorProperty;
	private static final CssMetaData<PieceGraphic, Color> COLOR =
			FACTORY.createColorCssMetaData("-color", s -> s.colorProperty);
	// The color of the highlight effect
	protected StyleableProperty<Color> highlightColorProperty;
	private static final CssMetaData<PieceGraphic, Color> HIGHLIGHT_COLOR =
			FACTORY.createColorCssMetaData("-highlight-color", s -> s.highlightColorProperty);
	// The color of the select effect
	private StyleableProperty<Color> selectColorProperty;
	private static final CssMetaData<PieceGraphic, Color> SELECT_COLOR =
			FACTORY.createColorCssMetaData("-select-color", s -> s.selectColorProperty);
	
	
	/**
	 * Constructs a blank slated checker piece
	 */
	protected PieceGraphic() {
		super();
		
		// Initialize properties
		colorProperty = new SimpleStyleableObjectProperty<Color>(COLOR, Color.GRAY);
		highlightColorProperty = new SimpleStyleableObjectProperty<Color>(HIGHLIGHT_COLOR, Color.GRAY);
		selectColorProperty = new SimpleStyleableObjectProperty<Color>(SELECT_COLOR, Color.GRAY);

		// Load FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Piece.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Constructs a checker piece.
	 * @param piece The piece this is based on
	 * @param size The height and width of the piece
	 */
	public PieceGraphic(Piece piece, double size) {
		this();
		
		buildPiece(piece);
		
		// Set sizes explicitly
		resizePiece(size, size);
		
		// Wait for parent to set sizes
		Platform.runLater(() -> {
			autosizePiece();
		});
	}
	
	
	/**
	 * Constructs a checker piece that will auto-size to parent
	 * @param piece The piece this is based on
	 */
	public PieceGraphic(Piece piece) {
		this();
		
		buildPiece(piece);
		
		// Wait for parent to set sizes
		Platform.runLater(() -> {
			autosizePiece();
		});
	}
	
	
	/**
	 * Builds the piece
	 */
	private void buildPiece(Piece piece) {
		
		// Add CSS classes depending on the piece
		if (piece.isBlack()) {
			getStyleClass().add("black");
		}
		if (piece.isWhite()) {
			getStyleClass().add("white");
		}
		if (piece.isMan()) {
			getStyleClass().add("man");
		}
		if (piece.isKing()) {
			getStyleClass().add("king");
		}
		
		// Set the instance variables
		this.piece = piece;
		highlighted = false;
		selected = false;
		
		
		// Initialize the king decal
		decal = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(CROWN_IMAGE)));
		decal.setPreserveRatio(true);
		// Rotate the decal to be upside-down for top player
		if (piece.getPlayerType() == Board.getTopPlayerType()) {
			decal.setRotate(180);
		}
		decalMask.setClip(decal);
		
		// Only show the decal right now if the piece is a king
		if (!piece.isKing()) {
			decalMask.setVisible(false);
		}
		
		
		// Create the concavity shadow effect
		concavityShadow = new InnerShadow();
		concavity.setEffect(concavityShadow);
		
		
		// Create highlight effect
		highlight = new DropShadow();
		highlight.setBlurType(BlurType.THREE_PASS_BOX);
		highlight.setColor(highlightColorProperty.getValue());
		highlight.setSpread(0.8);
		
		
		// Create select effect
		select = new DropShadow();
		select.setBlurType(BlurType.THREE_PASS_BOX);
		select.setSpread(0.9);
		
		
		// Listen for CSS
		colorProperty().addListener((obs, oldVal, newVal) -> {
			double hue = newVal.getHue();
			double saturation = newVal.getSaturation();
			double brightness = newVal.getBrightness();
			Color color = newVal;
			Color edgeColor = Color.hsb(
					hue,
					saturation,
					brightness * 0.5);
			Color innerShadowColor = Color.hsb(
					hue,
					saturation,
					brightness > 0.1 ? brightness - 0.1: 0.0);
			Color decalColor = Color.hsb(
					hue,
					saturation,
					brightness >= 0.5 ? brightness - 0.25: brightness + 0.25);
			
			outerCircle.setFill(edgeColor);
			innerCircle.setFill(color);
			concavity.setFill(color);
			concavityShadow.setColor(innerShadowColor);
			decalMask.setFill(decalColor);
		});
		highlightColorProperty().addListener((obs, oldVal, newVal) -> {
			highlight.setColor(highlightColorProperty.getValue());
			if (highlighted) {
				deactivate();
				highlight();
			}
		});
		selectColorProperty().addListener((obs, oldVal, newVal) -> {
			select.setColor(selectColorProperty.getValue());
			if (selected) {
				deactivate();
				select();
			}
		});
	}
	
	
	/**
	 * @return The referenced piece
	 */
	public Piece getPiece() {
		return piece;
	}
	
	
	/**
	 * @return The radius of the piece
	 */
	public double getRadius() {
		return radius;
	}


	/**
	 * Returns whether the piece is highlighted or selected.
	 * @return
	 */
	public boolean isActive() {
		return selected || highlighted;
	}
	
	
	/**
	 * Returns whether the piece is highlighted.
	 * @return Whether the piece is highlighted.
	 */
	public boolean isHighlighted() {
		return highlighted;
	}
	
	
	/**
	 * Returns whether the piece is selected.
	 * @return Whether the piece is selected.
	 */
	public boolean isSelected() {
		return selected;
	}
	
	
	/**
	 * Visually shows that this piece has can be moved.
	 */
	public void highlight() {
		if (highlighted) return;
		
		// Set this piece as highlighted
		selected = false;
		getStyleClass().remove("selected");
		highlighted = true;
		getStyleClass().add("highlighted");
		
		// Add highlight effect
		this.setEffect(highlight);
		
		// Reset color
		highlight.setColor(highlightColorProperty().getValue());
		
		// Create an animation that fades the highlight in and out
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(true);
		KeyValue kv = new KeyValue(highlight.colorProperty(),
				Color.web(highlight.getColor().toString(), 0));
		KeyFrame kf = new KeyFrame(Duration.millis(FADE_DURATION), kv);
		timeline.getKeyFrames().add(kf);
		timeline.play();
	}
	
	
	/**
	 * Visually shows that this piece has been selected.
	 */
	public void select() {
		if (selected) return;
		
		// Set this piece as selected
		highlighted = false;
		getStyleClass().remove("highlighted");
		selected = true;
		getStyleClass().add("selected");
		
		// Add select effect
		this.setEffect(select);
	}
	
	
	/**
	 * Visually shows that this piece is not selected or highlighted
	 */
	public void deactivate() {
		
		// Set this piece as neither selected or highlighted
		selected = false;
		getStyleClass().remove("selected");
		highlighted = false;
		getStyleClass().remove("highlighted");
		
		// Remove the effect
		this.setEffect(null);
	}
	
	
	/**
	 * Adds a crown decal for a newly promoted king piece
	 */
	public void crown() {
		decalMask.setVisible(true);
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
		 radius = (pieceSize / 2) - (EDGE_WIDTH / 2);
		
		outerCircle.setCenterX(radius);
		outerCircle.setCenterY(radius);
		outerCircle.setRadius(radius);
		
		innerCircle.setCenterX(radius);
		innerCircle.setCenterY(radius);
		innerCircle.setRadius(radius - EDGE_WIDTH);
		
		concavity.setCenterX(radius);
		concavity.setCenterY(radius);
		concavity.setRadius(radius * CONCAVITY_WIDTH);
		concavityShadow.setRadius(radius);
		
		decal.setFitWidth(Math.sqrt(2) * radius);
		decal.setX(radius + radius * Math.sin(-45.0 * (Math.PI / 180.0)));
		decal.setY(radius + radius * Math.cos(-135.0 * (Math.PI / 180.0)));
		decalMask.setCenterX(radius);
		decalMask.setCenterY(radius);
		decalMask.setRadius(radius);

		highlight.setRadius((squareSize - pieceSize) / 2);
		select.setRadius((squareSize - pieceSize) / 2);
	}
	

	/**
	 * @return the color property
	 */
	@SuppressWarnings("unchecked")
	public ObservableValue<Color> colorProperty() {
		return (ObservableValue<Color>)colorProperty;
	}
	

	/**
	 * @return the highlight color property
	 */
	@SuppressWarnings("unchecked")
	public ObservableValue<Color> highlightColorProperty() {
		return (ObservableValue<Color>)highlightColorProperty;
	}
	

	/**
	 * @return the select color property
	 */
	@SuppressWarnings("unchecked")
	public ObservableValue<Color> selectColorProperty() {
		return (ObservableValue<Color>)selectColorProperty;
	}
	
	
	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
	    return FACTORY.getCssMetaData();
	}
}
