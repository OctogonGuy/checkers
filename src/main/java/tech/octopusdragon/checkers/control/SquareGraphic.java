package tech.octopusdragon.checkers.control;

import java.io.IOException;

import tech.octopusdragon.checkers.model.Square;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;

/**
 * A graphic of a square on a checkers board
 * @author Alex Gill
 *
 */
public class SquareGraphic extends Region {
	
	// --- Instance variables ---
	protected double size;	// The width and height of the square

	
	/**
	 * Constructs a blank square.
	 */
	private SquareGraphic() {
		super();
		
		// Load FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Square.fxml"));
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
	 * Constructs a square.
	 * @param square The type of square
	 * @param size The width and height of the square
	 */
	public SquareGraphic(Square square, double size) {
		this();
		
		// Add a style class based on the square type
		StringBuilder styleClass = new StringBuilder(square.name());
		// Convert to lowercase
		styleClass.replace(0, styleClass.length(), styleClass.toString().toLowerCase());
		// Replace underscores with hyphens
		for (int i = 0; i < styleClass.length(); i++) {
			if (styleClass.charAt(i) == '_') {
				styleClass.replace(i, i + 1, "-");
			}
		}
		// Set as style class
		this.getStyleClass().add(styleClass.toString());
		
		
		// Set the instance variables
		this.size = size;
		
		
		// Initialize the background
		this.setMinSize(size, size);
		this.setPrefSize(size, size);
		this.setMaxSize(size, size);
	}
}
