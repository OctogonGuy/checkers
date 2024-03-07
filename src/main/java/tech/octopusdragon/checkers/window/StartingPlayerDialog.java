package tech.octopusdragon.checkers.window;

import tech.octopusdragon.checkers.CheckersApplication;
import tech.octopusdragon.checkers.model.PlayerType;

import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * A dialog that asks the player which player should be the starting player
 * @author Alex Gill
 *
 * @param <StartingPlayer>
 */
public class StartingPlayerDialog extends ChoiceDialog<PlayerType> {
	
	/**
	 * Creates the dialog
	 */
	public StartingPlayerDialog() {
		super(PlayerType.values()[0], PlayerType.values());
		this.setTitle("Starting Player Selection");
		this.setHeaderText("Who will go first?");
		this.setContentText("Starting Player:");
		
		this.getDialogPane().getScene().getStylesheets().add(
				getClass().getClassLoader().getResource(
				CheckersApplication.CSS_PATH).toExternalForm());
		
		((Stage)this.getDialogPane().getScene().getWindow()).getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream(
				CheckersApplication.CHECK_IMAGE_PATH)));
	}

}
