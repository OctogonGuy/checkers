package tech.octopusdragon.checkers.dialog;

import java.io.IOException;
import java.util.Arrays;

import tech.octopusdragon.checkers.RelativeDirection;
import tech.octopusdragon.checkers.Variant;
import tech.octopusdragon.checkers.gui.CheckersApplication;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Shows information regarding a checkers variant
 * @author Alex Gill
 *
 */
public class VariantInfoWindow extends Stage {

	@FXML private Label nameLabel;
	@FXML private Label descriptionLabel;
	@FXML private Label rowsLabel;
	@FXML private Label colsLabel;
	@FXML private Label piecesLabel;
	@FXML private Label boardPatternLabel;
	@FXML private Label startingPositionsLabel;
	@FXML private Label startingPlayerLabel;
	@FXML private Label movementLabel;
	@FXML private Label backwardsCaptureLabel;
	@FXML private Label flyingKingsLabel;
	@FXML private Label kingsRowCapturePromotionLabel;
	@FXML private Label removePiecesImmediatelyLabel;
	@FXML private Label manCanCaptureKingLabel;
	@FXML private Label quantityRuleLabel;
	@FXML private Label qualityRuleLabel;
	@FXML private Label priorityRuleLabel;

	/**
	 * Constructs the window
	 * @param variant The variant to show information about
	 */
	public VariantInfoWindow(Variant variant) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("VariantInfo.fxml"));
		loader.setController(this);
		try {
			Scene scene = new Scene(loader.load());
			Platform.runLater(() -> {
				((Stage)scene.getWindow()).getIcons().add(
						new Image(getClass().getClassLoader().getResourceAsStream(
						CheckersApplication.CHECK_IMAGE_PATH)));
			});
			this.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Update variant-related text
		updateValues(variant);
		
		// Set title to indicate variant
		this.setTitle(variant.getName() + " Info");
		
		// Block parent window until this is closed
		this.initModality(Modality.APPLICATION_MODAL);
	}
	
	/**
	 * Sets the text of the labels showing the information of the given variant
	 * @param variant The variant to show information of
	 */
	private void updateValues(Variant variant) {
		StringBuilder sb;
		
		// Name and description
		nameLabel.setText(variant.getName());
		descriptionLabel.setText(variant.getDescription());
		
		// Rows
		rowsLabel.setText(Integer.toString(variant.getRows()));
		
		// Columns
		colsLabel.setText(Integer.toString(variant.getCols()));
		
		// Number of pieces per side
		piecesLabel.setText(Integer.toString(variant.getNumPieces()));
		
		// Board pattern
		boardPatternLabel.setText(variant.getBoardPattern().toString());
		
		// Piece starting Positions
		startingPositionsLabel.setText(variant.getStartingPositions().toString());
		
		// Starting player
		startingPlayerLabel.setText(variant.getStartingPlayer().toString());
		
		// Movement
		sb = new StringBuilder();
		switch (variant.getFamily()) {
		case TURKISH:
			sb.append("Orthogonal");
			break;
		case INTERNATIONAL:
		case SPANISH:
		case MISCELLANEOUS:
			sb.append("Diagonal");
			break;
		case GOTHIC:
			sb.append("Orthogonal + Diagonal");
			break;
		case FRISIAN:
			sb.append("Diagonal + Orthogonal");
			break;
		case SPECIAL:
			sb.append("Special");
			break;
		}
		movementLabel.setText(sb.toString());
		
		// Backwards capture
		backwardsCaptureLabel.setText(
				Arrays.asList(variant.getManCaptureDirections()).contains(
						RelativeDirection.ORTHOGONAL_BACKWARD) ||
				Arrays.asList(variant.getManCaptureDirections()).contains(
						RelativeDirection.DIAGONAL_BACKWARD) ? "◯" : "✕");
		
		// Flying kings
		sb = new StringBuilder();
		switch (variant.getKingType()) {
		case SHORT:
			sb.append("✕");
			break;
		case FLYING:
			sb.append("◯");
			break;
		case SHORT_FLYING:
			sb.append("◯ (King Halt)");
			break;
		}
		flyingKingsLabel.setText(sb.toString());
		
		// Kings row capture promotion rule
		kingsRowCapturePromotionLabel.setText(variant.getKingsRowCapture().toString());
		
		// Remove pieces immediately
		removePiecesImmediatelyLabel.setText(variant.isRemovePiecesImmediately() ? "◯" : "✕");
		
		// Man can capture king
		manCanCaptureKingLabel.setText(variant.isManCanCaptureKing() ? "◯" : "✕");
		
		// Quantity rule
		quantityRuleLabel.setText(variant.hasQuantityRule() ? "◯" : "✕");
		
		// Quality rule
		qualityRuleLabel.setText(variant.hasQualityRule() ? "◯" : "✕");
		
		// Priority rule
		priorityRuleLabel.setText(variant.hasPriorityRule() ? "◯" : "✕");
	}
	
	@FXML
	private void close(ActionEvent event) {
		this.close();
	}
}
