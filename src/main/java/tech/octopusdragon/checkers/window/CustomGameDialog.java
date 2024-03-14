package tech.octopusdragon.checkers.window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tech.octopusdragon.checkers.CheckersApplication;
import tech.octopusdragon.checkers.model.RelativeDirection;
import tech.octopusdragon.checkers.model.Variant;
import tech.octopusdragon.checkers.model.rules.BoardPattern;
import tech.octopusdragon.checkers.model.rules.KingType;
import tech.octopusdragon.checkers.model.rules.KingsRowCapture;
import tech.octopusdragon.checkers.model.rules.StartingPlayer;
import tech.octopusdragon.checkers.model.rules.StartingPositions;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CustomGameDialog extends Dialog<Variant> {
	
	@FXML private Spinner<Integer> rowsSpinner;
	@FXML private Spinner<Integer> columnsSpinner;
	@FXML private Spinner<Integer> piecesSpinner;
	@FXML private ChoiceBox<StartingPositions> startingPositionsChoiceBox;
	@FXML private ChoiceBox<BoardPattern> boardPatternChoiceBox;
	@FXML private ChoiceBox<StartingPlayer> startingPlayerChoiceBox;
	@FXML private ChoiceBox<KingType> kingTypeChoiceBox;
	@FXML private ChoiceBox<KingsRowCapture> kingsRowCapturePromotionChoiceBox;
	@FXML private CheckBox removePiecesImmediatelyCheckBox;
	@FXML private CheckBox manCanCaptureKingCheckBox;
	@FXML private CheckBox quantityRuleCheckBox;
	@FXML private CheckBox qualityRuleCheckBox;
	@FXML private CheckBox priorityRuleCheckBox;

    @FXML private CheckBox manMoveDiagonalForwardCheckBox;
    @FXML private CheckBox manMoveDiagonalBackwardCheckBox;
    @FXML private CheckBox manMoveOrthogonalForwardCheckBox;
    @FXML private CheckBox manMoveOrthogonalSidewaysCheckBox;
    @FXML private CheckBox manMoveOrthogonalBackwardCheckBox;
    @FXML private CheckBox kingMoveDiagonalForwardCheckBox;
    @FXML private CheckBox kingMoveDiagonalBackwardCheckBox;
    @FXML private CheckBox kingMoveOrthogonalForwardCheckBox;
    @FXML private CheckBox kingMoveOrthogonalSidewaysCheckBox;
    @FXML private CheckBox kingMoveOrthogonalBackwardCheckBox;
    @FXML private CheckBox manCaptureDiagonalForwardCheckBox;
    @FXML private CheckBox manCaptureDiagonalBackwardCheckBox;
    @FXML private CheckBox manCaptureOrthogonalForwardCheckBox;
    @FXML private CheckBox manCaptureOrthogonalSidewaysCheckBox;
    @FXML private CheckBox manCaptureOrthogonalBackwardCheckBox;
    @FXML private CheckBox kingCaptureDiagonalForwardCheckBox;
    @FXML private CheckBox kingCaptureDiagonalBackwardCheckBox;
    @FXML private CheckBox kingCaptureOrthogonalForwardCheckBox;
    @FXML private CheckBox kingCaptureOrthogonalSidewaysCheckBox;
    @FXML private CheckBox kingCaptureOrthogonalBackwardCheckBox;
    
    @FXML private ButtonType playButtonType;
    
	/**
	 * Creates a new dialog that asks for and returns a variant of checkers for
	 * the user to play
	 */
	public CustomGameDialog() {
		
		// Build UI
		buildUI();
		
		// Set result converter
		setResultConverter((dialogButton) -> {
			Variant.setCustomRows(rowsSpinner.getValue());
			Variant.setCustomCols(columnsSpinner.getValue());
			Variant.setCustomNumPieces(piecesSpinner.getValue());
			Variant.setCustomStartingPositions(startingPositionsChoiceBox.getValue());
			Variant.setCustomBoardPattern(boardPatternChoiceBox.getValue());
			Variant.setCustomStartingPlayer(startingPlayerChoiceBox.getValue());
			Variant.setCustomKingType(kingTypeChoiceBox.getValue());
			Variant.setCustomKingsRowCapture(kingsRowCapturePromotionChoiceBox.getValue());
			Variant.setCustomRemovePiecesImmediately(removePiecesImmediatelyCheckBox.isSelected());
			Variant.setCustomManCanCaptureKing(manCanCaptureKingCheckBox.isSelected());
			Variant.setCustomQuantityRule(quantityRuleCheckBox.isSelected());
			Variant.setCustomQualityRule(qualityRuleCheckBox.isSelected());
			Variant.setCustomPriorityRule(priorityRuleCheckBox.isSelected());
			
			List<RelativeDirection> manMoveDirections = new ArrayList<>();
			if (manMoveDiagonalForwardCheckBox.isSelected()) manMoveDirections.add(RelativeDirection.DIAGONAL_FORWARD);
			if (manMoveDiagonalBackwardCheckBox.isSelected()) manMoveDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
			if (manMoveOrthogonalForwardCheckBox.isSelected()) manMoveDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
			if (manMoveOrthogonalBackwardCheckBox.isSelected()) manMoveDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
			if (manMoveOrthogonalSidewaysCheckBox.isSelected()) manMoveDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
			Variant.setCustomManMovementDirections(manMoveDirections.toArray(new RelativeDirection[0]));
			
			List<RelativeDirection> kingMoveDirections = new ArrayList<>();
			if (kingMoveDiagonalForwardCheckBox.isSelected()) kingMoveDirections.add(RelativeDirection.DIAGONAL_FORWARD);
			if (kingMoveDiagonalBackwardCheckBox.isSelected()) kingMoveDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
			if (kingMoveOrthogonalForwardCheckBox.isSelected()) kingMoveDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
			if (kingMoveOrthogonalBackwardCheckBox.isSelected()) kingMoveDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
			if (kingMoveOrthogonalSidewaysCheckBox.isSelected()) kingMoveDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
			Variant.setCustomKingMovementDirections(kingMoveDirections.toArray(new RelativeDirection[0]));
			
			List<RelativeDirection> manCaptureDirections = new ArrayList<>();
			if (manCaptureDiagonalForwardCheckBox.isSelected()) manCaptureDirections.add(RelativeDirection.DIAGONAL_FORWARD);
			if (manCaptureDiagonalBackwardCheckBox.isSelected()) manCaptureDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
			if (manCaptureOrthogonalForwardCheckBox.isSelected()) manCaptureDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
			if (manCaptureOrthogonalBackwardCheckBox.isSelected()) manCaptureDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
			if (manCaptureOrthogonalSidewaysCheckBox.isSelected()) manCaptureDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
			Variant.setCustomManCaptureDirections(manCaptureDirections.toArray(new RelativeDirection[0]));
			
			List<RelativeDirection> kingCaptureDirections = new ArrayList<>();
			if (kingCaptureDiagonalForwardCheckBox.isSelected()) kingCaptureDirections.add(RelativeDirection.DIAGONAL_FORWARD);
			if (kingCaptureDiagonalBackwardCheckBox.isSelected()) kingCaptureDirections.add(RelativeDirection.DIAGONAL_BACKWARD);
			if (kingCaptureOrthogonalForwardCheckBox.isSelected()) kingCaptureDirections.add(RelativeDirection.ORTHOGONAL_FORWARD);
			if (kingCaptureOrthogonalBackwardCheckBox.isSelected()) kingCaptureDirections.add(RelativeDirection.ORTHOGONAL_BACKWARD);
			if (kingCaptureOrthogonalSidewaysCheckBox.isSelected()) kingCaptureDirections.add(RelativeDirection.ORTHOGONAL_SIDEWAYS);
			Variant.setCustomKingCaptureDirections(kingCaptureDirections.toArray(new RelativeDirection[0]));
			
			ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
			return data == ButtonData.OK_DONE ? Variant.CUSTOM : null;
		});
	}
	
	
	/**
	 * Builds the GUI
	 */
	private void buildUI() {
		
		// Load FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomGame.fxml"));
		loader.setController(this);
		DialogPane dialogPane = null;
		try {
			dialogPane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setDialogPane(dialogPane);
		
		// Add choices to choice boxes
		startingPositionsChoiceBox.setItems(FXCollections.observableArrayList(StartingPositions.values()));
		boardPatternChoiceBox.setItems(FXCollections.observableArrayList(BoardPattern.values()));
		startingPlayerChoiceBox.setItems(FXCollections.observableArrayList(StartingPlayer.values()));
		kingTypeChoiceBox.setItems(FXCollections.observableArrayList(KingType.values()));
		kingsRowCapturePromotionChoiceBox.setItems(FXCollections.observableArrayList(KingsRowCapture.values()));
		
		// Disable play button if all parameters are not specified
		dialogPane.lookupButton(playButtonType).disableProperty().bind(
				rowsSpinner.valueProperty().isNull().or(
				columnsSpinner.valueProperty().isNull().or(
				piecesSpinner.valueProperty().isNull().or(
				startingPositionsChoiceBox.valueProperty().isNull().or(
				boardPatternChoiceBox.valueProperty().isNull().or(
				startingPlayerChoiceBox.valueProperty().isNull().or(
				kingTypeChoiceBox.valueProperty().isNull().or(
				kingsRowCapturePromotionChoiceBox.valueProperty().isNull()
		))))))));
		
		// Window icon
		((Stage)dialogPane.getScene().getWindow()).getIcons().add(
				new Image(getClass().getClassLoader().getResourceAsStream(
				CheckersApplication.CHECK_IMAGE_PATH)));
		
		// Set title
		this.setTitle("Custom Game");
		
		// Allow resize
		final Stage stage = ((Stage)dialogPane.getScene().getWindow());
		Platform.runLater(() -> {
			stage.setMinWidth(stage.getWidth());
			stage.setMinHeight(stage.getHeight());
		});
		this.setResizable(true);
	}
}
