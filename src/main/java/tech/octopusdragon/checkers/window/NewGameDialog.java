package tech.octopusdragon.checkers.window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tech.octopusdragon.checkers.CheckersApplication;
import tech.octopusdragon.checkers.model.Board;
import tech.octopusdragon.checkers.model.Checkers;
import tech.octopusdragon.checkers.model.Config;
import tech.octopusdragon.checkers.model.Family;
import tech.octopusdragon.checkers.model.PlayerType;
import tech.octopusdragon.checkers.model.Variant;
import tech.octopusdragon.checkers.model.rules.StartingPlayer;

import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Shows a selection to allow the user to select a regional variant of checkers
 * to play. It includes buttons with the name of the variant. Upon clicking
 * these, it starts a new game of that variant. Next to these buttons is a
 * button with a question mark. This button will bring up a new dialog that
 * displays a description of the variant.
 * @author Alex Gill
 *
 */
public class NewGameDialog extends Dialog<Checkers> {
	
	// --- Fields ---
	
	@FXML private ButtonType closeButtonType;
	@FXML private ButtonType playButtonType;
	@FXML private ButtonType infoButtonType;
    @FXML private TreeView<Object> variantList;
    @FXML private CheckBox highlightMovesCheckBox;
    @FXML private RadioButton topPlayerBlackRadioButton;
    @FXML private RadioButton bottomPlayerBlackRadioButton;
    @FXML private RadioButton topPlayerWhiteRadioButton;
    @FXML private RadioButton bottomPlayerWhiteRadioButton;
    @FXML private CheckBox blackComputerPlayerCheckBox;
    @FXML private CheckBox whiteComputerPlayerCheckBox;
    private Checkers game;
	
    
    
    // --- Constructors ---
    
	/**
	 * Creates a new dialog that asks for and returns a variant of checkers for
	 * the user to play
	 */
	public NewGameDialog() {
		
		// Build UI
		buildUI();
		
		// Set result converter
		setResultConverter((dialogButton) -> {
			ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
			return data == ButtonData.OK_DONE ? game : null;
		});
	}
	
	
	/**
	 * Builds the GUI
	 */
	private void buildUI() {
		
		// Load FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NewGame.fxml"));
		loader.setController(this);
		DialogPane dialogPane = null;
		try {
			dialogPane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setDialogPane(dialogPane);
		
		// Window icon
		((Stage)dialogPane.getScene().getWindow()).getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream(
				CheckersApplication.CHECK_IMAGE_PATH)));
		
		// Populate tree view with variants
		Map<Family, List<Variant>> families = new LinkedHashMap<>();
		for (Variant variant : Variant.values()) {
			if (!families.containsKey(variant.getFamily())) {
				families.put(variant.getFamily(), new ArrayList<>());
			}
			families.get(variant.getFamily()).add(variant);
		}
		TreeItem<Object> rootItem = new TreeItem<>("Variants");
		rootItem.setExpanded(true);
		for (Family family : families.keySet()) {
			TreeItem<Object> familyItem = new TreeItem<>(family + " Variants");
			familyItem.setExpanded(true);
			for (Variant variant : families.get(family)) {
				TreeItem<Object> variantItem = new TreeItem<>(variant);
				familyItem.getChildren().add(variantItem);
			}
			rootItem.getChildren().add(familyItem);
		}
		variantList.setRoot(rootItem);
		
		// Bind top/bottom player radio buttons' selected property to inverse
		topPlayerBlackRadioButton.selectedProperty().bindBidirectional(bottomPlayerWhiteRadioButton.selectedProperty());
		bottomPlayerBlackRadioButton.selectedProperty().bindBidirectional(topPlayerWhiteRadioButton.selectedProperty());
		
		// Set initially selected check and radio buttons
		highlightMovesCheckBox.setSelected(Config.isHighlightMoves());
		RadioButton topPlayerRadioButton = null;
		RadioButton bottomPlayerRadioButton = null;
		switch (Config.getTopPlayer()) {
		case BLACK:
			topPlayerRadioButton = topPlayerBlackRadioButton;
			bottomPlayerRadioButton = bottomPlayerWhiteRadioButton;
			break;
		case WHITE:
			topPlayerRadioButton = topPlayerWhiteRadioButton;
			bottomPlayerRadioButton = bottomPlayerBlackRadioButton;
			break;
		}
		topPlayerRadioButton.setSelected(true);
		bottomPlayerRadioButton.setSelected(true);
		blackComputerPlayerCheckBox.setSelected(Config.isBlackComputerPlayer());
		whiteComputerPlayerCheckBox.setSelected(Config.isWhiteComputerPlayer());

		// Disable Play and Info buttons when nothing is selected
		Binding<Boolean> disableButtonBinding = Bindings.createBooleanBinding(() -> {
			if (variantList.getSelectionModel().getSelectedItem() == null ||
					!variantList.getSelectionModel().getSelectedItem().isLeaf()) {
				return true;
			}
			return false;
		}, variantList.getSelectionModel().selectedItemProperty());
		dialogPane.lookupButton(playButtonType).disableProperty().bind(disableButtonBinding);
		dialogPane.lookupButton(infoButtonType).disableProperty().bind(disableButtonBinding);
		
		// Show starting player dialog and set starting player if starting
		// player is either
		dialogPane.lookupButton(playButtonType).addEventFilter(ActionEvent.ACTION, event -> {
			Variant variant = (Variant)variantList.getSelectionModel().getSelectedItem().getValue();
			
			// Instantiate the game
			game = new Checkers(variant);
			
			// Show dialog to choose first player if starting player is either
			if (variant.getStartingPlayer() == StartingPlayer.EITHER) {
				PlayerType startingPlayer;
				Optional<PlayerType> startingPlayerResult = new StartingPlayerDialog().showAndWait();
				if (startingPlayerResult.isPresent()) {
					startingPlayer = startingPlayerResult.get();
					game.setStartingPlayer(startingPlayer);
				}
				else {
					
					// Do nothing if a player was not chosen
					event.consume();
				}
			}
		});
		
		// Show info if info button is clicked
		dialogPane.lookupButton(infoButtonType).addEventFilter(ActionEvent.ACTION, event -> {
			new VariantInfoWindow((Variant)variantList.getSelectionModel().getSelectedItem().getValue()).show();
			event.consume();
		});
		
		// Set title
		this.setTitle("New Game");
		
		// Allow resize
		final Stage stage = ((Stage)dialogPane.getScene().getWindow());
		Platform.runLater(() -> {
			stage.setMinWidth(stage.getWidth());
			stage.setMinHeight(stage.getHeight());
		});
		this.setResizable(true);
	}
	
	
	@FXML
	private void setTopPlayerTypeToBlack() {
		Board.setTopPlayerType(PlayerType.BLACK);
		Config.setTopPlayer(PlayerType.BLACK);
	}
	
	
	@FXML
	private void setTopPlayerTypeToWhite() {
		Board.setTopPlayerType(PlayerType.WHITE);
		Config.setTopPlayer(PlayerType.WHITE);
	}
	
	
	@FXML
	private void toggleBlackPlayerComputer() {
		Config.setBlackComputerPlayer(!Config.isBlackComputerPlayer());
	}
	
	
	@FXML
	private void toggleWhitePlayerComputer() {
		Config.setWhiteComputerPlayer(!Config.isWhiteComputerPlayer());
	}
	
	
	@FXML
	private void toggleHighlightMoves() {
		Config.setHighlightMoves(!Config.isHighlightMoves());
	}
}
