package tech.octopusdragon.checkers.gui;

import java.io.IOException;
import java.util.Optional;

import tech.octopusdragon.checkers.Checkers;
import tech.octopusdragon.checkers.dialog.NewGameDialog;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * A GUI application that allows the user to play a game of checkers.
 * @author Alex Gill
 *
 */
public class CheckersApplication extends Application {
	
	// --- Constants ---
	public static final String CHECK_IMAGE_PATH = "resources/images/icon.png";
	public static final String CSS_PATH = "resources/stylesheets/main.css";
	
	
	@Override
	public void start(Stage primaryStage) {
		
		// Show the new game dialog
		Optional<Checkers> result = new NewGameDialog().showAndWait();
		if (!result.isPresent()) {
			System.exit(0);
		}
		
		// Set the scene
		FXMLLoader loader = setFXMLRoot("GameRoot.fxml", primaryStage);
		GameRootController controller = loader.getController();
		primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(
						CHECK_IMAGE_PATH)));
		
		// Set the new game
		controller.newGame(result.get());
		
		// Show the scene
		Platform.runLater(() -> {
			primaryStage.show();
		});
	}

	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	/**
	 * Sets the stage's current scene to a new scene with a root loaded with an
	 * FXML file
	 * @param fxmlPath The path of the FXML file
	 * @param stage The stage to set the new scene to
	 * @return The FXMLLoader associated with the new scene
	 */
	private FXMLLoader setFXMLRoot(String fxmlPath, Stage stage) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		try {
			Scene scene = new Scene(loader.load());
			scene.getStylesheets().add(getClass().getClassLoader().getResource(
					CSS_PATH).toExternalForm());
			stage.setScene(scene);
		} catch (IOException e) {
			System.out.println("Error loading FXML file");
			e.printStackTrace();
		}
		return loader;
	}
	
}
