package tech.octopusdragon.checkers.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import tech.octopusdragon.checkers.model.rules.BoardPattern;
import tech.octopusdragon.checkers.model.rules.KingType;
import tech.octopusdragon.checkers.model.rules.KingVKingAutomaticDrawRule;
import tech.octopusdragon.checkers.model.rules.KingVKingDrawRule;
import tech.octopusdragon.checkers.model.rules.KingsRowCapture;
import tech.octopusdragon.checkers.model.rules.StartingPlayer;
import tech.octopusdragon.checkers.model.rules.StartingPositions;

/**
 * Represents a variant of checkers
 * @author Alex Gill
 *
 */
public enum Variant {
	// TODO - Add ability to make custom variant

	// --- Turkish family ---
	TURKISH,
	GREEK,
	ADIGHA,
	OSSETIAN_KENY,
	// --- International family ---
	INTERNATIONAL,
	IVORIAN,
	CANADIAN,
	SRI_LANKAN,
	SOUTH_AFRICAN,
	BRAZILIAN,
	FILIPINO,
	MOZAMBICAN,
	SWAZI,
	// --- International family 2 ---
	RUSSIAN,
	SPANTSIRETTI,
	POOL,
	NORTH_GERMAN,
	JAMAICAN,
	LAOTIAN,
	GHANAIAN,
	NIGERIAN,
	// --- Turkish + international family ---
	ARMENIAN,
	GOTHIC,
	TURKTHIC,
	CRODA,
	UNIVERSAL,
	// --- Frisian family ---
	FRISIAN,
	EURASIAN,
	SIBERIAN,
	GERMAN_ENGLISCH,
	DANISH,
	SWEDISH,
	// --- Spanish family ---
	SPANISH,
	PORTUGUESE,
	ARGENTINIAN,
	BURMESE,
	INDONESIAN,
	MALAYSIAN,
	// --- Spanish family 2 ---
	CZECH,
	SLOVAK,
	THAI,
	AFRICAN_AMERICAN,
	TANZANIAN,
	GERMAN,
	CENTRAL_SOUTH_GERMAN,
	SINGAPOREAN,
	// --- Miscellaneous family ---
	AMERICAN,
	ITALIAN,
	DAMA_TRIESTINA,
	// TODO - Hawaiian Konane
	// --- Invented family ---
	DAMEO,
	// TODO - Vigman's Draughts
	// TODO - Philosophy Shogi Checkers
	// TODO - Poddavki (Suicide Checkers)
	// TODO - Parachute Checkers
	// TODO - One way Checker
	// TODO - Corner Checkers
	// --- Custom variant
	CUSTOM;



	static {

		// --- Turkish family ---

		TURKISH.setName("Turkish Dama");
		TURKISH.setFamily(Family.TURKISH);
		TURKISH.setRows(8);
		TURKISH.setCols(8);
		TURKISH.setNumPieces(16);
		TURKISH.setBoardPattern(BoardPattern.ALL_WHITE);
		TURKISH.setStartingPositions(StartingPositions.ALL_FROM_SECOND_ROW);
		TURKISH.setStartingPlayer(StartingPlayer.WHITE);
		TURKISH.setManMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS });
		TURKISH.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS });
		TURKISH.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD });
		TURKISH.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD });
		TURKISH.setKingType(KingType.FLYING);
		TURKISH.setKingsRowCapture(KingsRowCapture.SKIP);
		TURKISH.setRemovePiecesImmediately(true);
		TURKISH.setManCanCaptureKing(true);
		TURKISH.setKingCanReverseDirection(false);
		TURKISH.setQuantityRule(true);
		TURKISH.setQualityRule(false);
		TURKISH.setPriorityRule(false);
		TURKISH.setPrioritizeKingCaptureInKingsRow(true);

		GREEK.copyVariant(TURKISH);
		GREEK.setName("Greek Ntama");
		GREEK.setKingType(KingType.SHORT_FLYING);

		ADIGHA.copyVariant(TURKISH);
		ADIGHA.setName("Adigha Phèklèn");
		ADIGHA.setRemovePiecesImmediately(false);

		OSSETIAN_KENY.copyVariant(TURKISH);
		OSSETIAN_KENY.setName("Ossetian Keny");
		OSSETIAN_KENY.setKingCanReverseDirection(true);
		OSSETIAN_KENY.setKingsRowCapture(KingsRowCapture.ADAPT);



		// --- International family ---

		INTERNATIONAL.setName("International Draughts/Polish Warcaby");
		INTERNATIONAL.setFamily(Family.INTERNATIONAL);
		INTERNATIONAL.setRows(10);
		INTERNATIONAL.setCols(10);
		INTERNATIONAL.setNumPieces(20);
		INTERNATIONAL.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_BLACK);
		INTERNATIONAL.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_LEFT);
		INTERNATIONAL.setStartingPlayer(StartingPlayer.WHITE);
		INTERNATIONAL.setManMovementDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD });
		INTERNATIONAL.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		INTERNATIONAL.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		INTERNATIONAL.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		INTERNATIONAL.setKingType(KingType.FLYING);
		INTERNATIONAL.setKingsRowCapture(KingsRowCapture.SKIP);
		INTERNATIONAL.setRemovePiecesImmediately(false);
		INTERNATIONAL.setManCanCaptureKing(true);
		INTERNATIONAL.setKingCanReverseDirection(false);
		INTERNATIONAL.setQuantityRule(true);
		INTERNATIONAL.setQualityRule(false);
		INTERNATIONAL.setPriorityRule(false);
		INTERNATIONAL.setNumBoardRepeatsToDraw(3);

		IVORIAN.copyVariant(INTERNATIONAL);
		IVORIAN.setName("Ivorian Dames/Paraguayan Dames");
		IVORIAN.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_WHITE);
		IVORIAN.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_RIGHT);

		CANADIAN.copyVariant(INTERNATIONAL);
		CANADIAN.setName("Canadian Dames");
		CANADIAN.setRows(12);
		CANADIAN.setCols(12);
		CANADIAN.setNumPieces(30);

		SRI_LANKAN.copyVariant(CANADIAN);
		SRI_LANKAN.setName("Sri Lankan Dam");
		SRI_LANKAN.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_WHITE);
		SRI_LANKAN.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_RIGHT);

		SOUTH_AFRICAN.copyVariant(INTERNATIONAL);
		SOUTH_AFRICAN.setName("South African Dumm");
		SOUTH_AFRICAN.setRows(14);
		SOUTH_AFRICAN.setCols(14);
		SOUTH_AFRICAN.setNumPieces(42);

		BRAZILIAN.copyVariant(INTERNATIONAL);
		BRAZILIAN.setName("Brazilian Damas");
		BRAZILIAN.setRows(8);
		BRAZILIAN.setCols(8);
		BRAZILIAN.setNumPieces(12);

		FILIPINO.copyVariant(BRAZILIAN);
		FILIPINO.setName("Filipino Dama");
		FILIPINO.setBoardPattern(BoardPattern.FILIPINO);
		FILIPINO.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_RIGHT);

		MOZAMBICAN.copyVariant(INTERNATIONAL);
		MOZAMBICAN.setName("Mozambican Damas");
		MOZAMBICAN.setStartingPlayer(StartingPlayer.EITHER);
		MOZAMBICAN.setQuantityRuleKingValue(2);

		SWAZI.copyVariant(INTERNATIONAL);
		SWAZI.setName("Swazi Mswati");
		SWAZI.setStartingPlayer(StartingPlayer.EITHER);
		SWAZI.setKingsRowCapture(KingsRowCapture.ADAPT);
		SWAZI.setRemovePiecesImmediately(false);
		SWAZI.setHuffing(true);



		// --- International family 2 ---

		RUSSIAN.copyVariant(INTERNATIONAL);
		RUSSIAN.setName("Russian Shashki");
		RUSSIAN.setRows(8);
		RUSSIAN.setCols(8);
		RUSSIAN.setNumPieces(12);
		RUSSIAN.setKingsRowCapture(KingsRowCapture.ADAPT);
		RUSSIAN.setQuantityRule(false);
		RUSSIAN.setKingVKingDrawRule(new KingVKingDrawRule(3, 1, 15));

		SPANTSIRETTI.copyVariant(RUSSIAN);
		SPANTSIRETTI.setName("Spantsiretti/Russian 80 Cells");
		SPANTSIRETTI.setCols(10);
		SPANTSIRETTI.setNumPieces(15);

		POOL.copyVariant(RUSSIAN);
		POOL.setName("Pool Checkers");
		POOL.setStartingPlayer(StartingPlayer.BLACK);
		POOL.setKingsRowCapture(KingsRowCapture.SKIP);
		POOL.setKingVKingDrawRule(new KingVKingDrawRule(3, 1, 13));

		NORTH_GERMAN.copyVariant(POOL);
		NORTH_GERMAN.setName("North German Dame");
		NORTH_GERMAN.setStartingPlayer(StartingPlayer.WHITE);
		NORTH_GERMAN.setKingVKingDrawRule(new KingVKingDrawRule());

		JAMAICAN.copyVariant(POOL);
		JAMAICAN.setName("Jamaican Draughts");
		JAMAICAN.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_WHITE);
		JAMAICAN.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_RIGHT);

		LAOTIAN.copyVariant(JAMAICAN);
		LAOTIAN.setName("Laotian Draughts");
		LAOTIAN.setRows(10);
		LAOTIAN.setCols(10);
		LAOTIAN.setNumPieces(20);
		LAOTIAN.setStartingPlayer(StartingPlayer.WHITE);
		LAOTIAN.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_BLACK);
		LAOTIAN.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_LEFT);

		GHANAIAN.copyVariant(LAOTIAN);
		GHANAIAN.setName("Ghanaian Damii");
		GHANAIAN.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_WHITE);
		GHANAIAN.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_RIGHT);
		GHANAIAN.setMinPieces(1);
		GHANAIAN.setHuffing(true);

		NIGERIAN.copyVariant(LAOTIAN);
		NIGERIAN.setName("Nigerian Drafts");
		NIGERIAN.setStartingPlayer(StartingPlayer.EITHER);
		NIGERIAN.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_WHITE);
		NIGERIAN.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_RIGHT);
		NIGERIAN.setHuffing(true);



		// --- Turkish + international family ---

		ARMENIAN.copyVariant(TURKISH);
		ARMENIAN.setFamily(Family.GOTHIC);
		ARMENIAN.setName("Armenian Tama");
		ARMENIAN.setManMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.DIAGONAL_FORWARD });
		ARMENIAN.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });

		GOTHIC.copyVariant(ARMENIAN);
		GOTHIC.setName("Gothic Dame/Old German Dame");
		GOTHIC.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_BLACK);
		GOTHIC.setStartingPositions(StartingPositions.ALL_FROM_FIRST_ROW);
		GOTHIC.setManMovementDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD });
		GOTHIC.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.DIAGONAL_FORWARD });
		GOTHIC.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		GOTHIC.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		GOTHIC.setKingType(KingType.SHORT);
		GOTHIC.setKingsRowCapture(KingsRowCapture.STOP);

		TURKTHIC.copyVariant(ARMENIAN);
		TURKTHIC.setName("Turkthic Dama/Turkish Gothic Dama");
		TURKTHIC.setManMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.DIAGONAL_FORWARD });
		TURKTHIC.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.DIAGONAL_FORWARD });
		TURKTHIC.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		TURKTHIC.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		TURKTHIC.setKingType(KingType.SHORT_FLYING);
		TURKTHIC.setKingsRowCapture(KingsRowCapture.ADAPT);

		CRODA.copyVariant(ARMENIAN);
		CRODA.setName("Croda Croation");
		CRODA.setNumPieces(24);
		CRODA.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_BLACK);
		CRODA.setStartingPositions(StartingPositions.ALL_FROM_FIRST_ROW);
		CRODA.setManMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.DIAGONAL_FORWARD });
		CRODA.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD });
		CRODA.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD });
		CRODA.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD });
		CRODA.setRemovePiecesImmediately(false);

		UNIVERSAL.copyVariant(CRODA);
		UNIVERSAL.setName("Universal Checkers");
		UNIVERSAL.setManMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.DIAGONAL_FORWARD });
		UNIVERSAL.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		UNIVERSAL.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		UNIVERSAL.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });



		// --- Frisian family ---

		FRISIAN.copyVariant(INTERNATIONAL);
		FRISIAN.setName("Frisian Dammen");
		FRISIAN.setFamily(Family.FRISIAN);
		FRISIAN.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		FRISIAN.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		FRISIAN.setQualityRule(true);
		FRISIAN.setPriorityRule(true);
		FRISIAN.setKingVKingDrawRule(new KingVKingDrawRule(2, 1, 7));
		FRISIAN.setQuantityRuleManValue(2);
		FRISIAN.setQuantityRuleKingValue(3);
		FRISIAN.setKingMaxConsecutiveMoves(3);
		FRISIAN.setOnlyMoveOnSameColor(true);

		EURASIAN.copyVariant(INTERNATIONAL);
		EURASIAN.setFamily(FRISIAN.getFamily());
		EURASIAN.setName("Eurasian Dama/Turkish-Russian Dama");
		EURASIAN.setRows(8);
		EURASIAN.setCols(8);
		EURASIAN.setNumPieces(12);
		EURASIAN.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD });
		EURASIAN.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD });
		EURASIAN.setQuantityRule(false);
		EURASIAN.setKingVKingDrawRule(new KingVKingDrawRule(3, 1, 15));
		EURASIAN.setOnlyMoveOnSameColor(true);

		SIBERIAN.copyVariant(EURASIAN);
		SIBERIAN.setName("Siberian Turkutaga");
		SIBERIAN.setNumPieces(8);
		SIBERIAN.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS });
		SIBERIAN.setKingsRowCapture(KingsRowCapture.STOP);
		SIBERIAN.setKingVKingDrawRule(new KingVKingDrawRule());

		GERMAN_ENGLISCH.copyVariant(EURASIAN);
		GERMAN_ENGLISCH.setName("German Englisch/Swedish Engelska");
		GERMAN_ENGLISCH.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		GERMAN_ENGLISCH.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		GERMAN_ENGLISCH.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		GERMAN_ENGLISCH.setKingVKingDrawRule(new KingVKingDrawRule());

		DANISH.copyVariant(GERMAN_ENGLISCH);
		DANISH.setName("Danish Makvar");
		DANISH.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.DIAGONAL_FORWARD });

		SWEDISH.copyVariant(GERMAN_ENGLISCH);
		SWEDISH.setName("Swedish Marquere");
		SWEDISH.setKingsRowCapture(KingsRowCapture.STOP);



		// --- Spanish family ---

		SPANISH.setName("Spanish Damas");
		SPANISH.setFamily(Family.SPANISH);
		SPANISH.setRows(8);
		SPANISH.setCols(8);
		SPANISH.setNumPieces(12);
		SPANISH.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_BLACK);
		SPANISH.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_RIGHT);
		SPANISH.setStartingPlayer(StartingPlayer.WHITE);
		SPANISH.setManMovementDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD });
		SPANISH.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD });
		SPANISH.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		SPANISH.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		SPANISH.setKingType(KingType.FLYING);
		SPANISH.setKingsRowCapture(KingsRowCapture.STOP);
		SPANISH.setRemovePiecesImmediately(false);
		SPANISH.setManCanCaptureKing(true);
		SPANISH.setKingCanReverseDirection(false);
		SPANISH.setQuantityRule(true);
		SPANISH.setQualityRule(true);
		SPANISH.setPriorityRule(false);

		PORTUGUESE.copyVariant(SPANISH);
		PORTUGUESE.setName("Portuguese Damas");
		PORTUGUESE.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_WHITE);
		PORTUGUESE.setKingVKingDrawRule(new KingVKingDrawRule(3, 1, 12));

		ARGENTINIAN.copyVariant(SPANISH);
		ARGENTINIAN.setName("Argentinian Damas");
		ARGENTINIAN.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_WHITE);
		ARGENTINIAN.setKingType(KingType.SHORT_FLYING);

		BURMESE.copyVariant(SPANISH);
		BURMESE.setName("Burmese Kyar/Moroccan Dama/Algerian Dama/Tunisian Dama");
		BURMESE.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_WHITE);
		BURMESE.setQuantityRule(false);

		INDONESIAN.copyVariant(SPANISH);
		INDONESIAN.setName("Indonesian Dam Blas");
		INDONESIAN.setRows(10);
		INDONESIAN.setCols(10);
		INDONESIAN.setNumPieces(20);
		INDONESIAN.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_LEFT);
		INDONESIAN.setHuffing(true);

		MALAYSIAN.copyVariant(SPANISH);
		MALAYSIAN.setName("Malaysian Dam Haji");
		MALAYSIAN.setRows(12);
		MALAYSIAN.setCols(12);
		MALAYSIAN.setNumPieces(30);
		MALAYSIAN.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_LEFT);
		MALAYSIAN.setHuffing(true);



		// --- Spanish family 2 ---

		CZECH.copyVariant(SPANISH);
		CZECH.setName("Czech Dama");
		CZECH.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_LEFT);
		CZECH.setQuantityRule(false);
		CZECH.setQualityRule(false);
		CZECH.setPriorityRule(true);

		SLOVAK.copyVariant(CZECH);
		SLOVAK.setName("Slovak Dama");
		SLOVAK.setNumPieces(8);

		THAI.copyVariant(CZECH);
		THAI.setName("Thai Makhos");
		THAI.setNumPieces(8);
		THAI.setStartingPlayer(StartingPlayer.BLACK);
		THAI.setKingType(KingType.SHORT_FLYING);
		THAI.setRemovePiecesImmediately(true);
		THAI.setKingCanReverseDirection(true);
		THAI.setPriorityRule(false);

		AFRICAN_AMERICAN.copyVariant(SPANISH);
		AFRICAN_AMERICAN.setName("African-American Straight");
		AFRICAN_AMERICAN.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_LEFT);
		AFRICAN_AMERICAN.setStartingPlayer(StartingPlayer.BLACK);
		AFRICAN_AMERICAN.setPriorityRule(false);

		TANZANIAN.copyVariant(AFRICAN_AMERICAN);
		TANZANIAN.setName("Tanzanian Draughts");
		TANZANIAN.setStartingPlayer(StartingPlayer.EITHER);
		TANZANIAN.setKingVKingDrawRule(new KingVKingDrawRule(3, 1, 12));
		TANZANIAN.setKingVKingAutomaticDrawRule(new KingVKingAutomaticDrawRule(2, 2));

		GERMAN.copyVariant(CZECH);
		GERMAN.setName("German Dame/Swedish Dampsel");
		GERMAN.setPriorityRule(false);

		CENTRAL_SOUTH_GERMAN.copyVariant(GERMAN);
		CENTRAL_SOUTH_GERMAN.setName("Central-South German Dame/Danish Damspil/Finnish Tammi");
		CENTRAL_SOUTH_GERMAN.setKingType(KingType.SHORT_FLYING);

		SINGAPOREAN.copyVariant(CZECH);
		SINGAPOREAN.setName("Singaporean Dam Haji");
		SINGAPOREAN.setRows(12);
		SINGAPOREAN.setCols(12);
		SINGAPOREAN.setNumPieces(30);
		SINGAPOREAN.setPriorityRule(true);
		SINGAPOREAN.setHuffing(true);



		// --- Miscellaneous family ---

		AMERICAN.setName("American Checkers/English Draughts/Straight Checkers");
		AMERICAN.setFamily(Family.MISCELLANEOUS);
		AMERICAN.setRows(8);
		AMERICAN.setCols(8);
		AMERICAN.setNumPieces(12);
		AMERICAN.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_BLACK);
		AMERICAN.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_LEFT);
		AMERICAN.setStartingPlayer(StartingPlayer.BLACK);
		AMERICAN.setManMovementDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD });
		AMERICAN.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD });
		AMERICAN.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		AMERICAN.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		AMERICAN.setKingType(KingType.SHORT);
		AMERICAN.setKingsRowCapture(KingsRowCapture.STOP);
		AMERICAN.setRemovePiecesImmediately(true);
		AMERICAN.setManCanCaptureKing(true);
		AMERICAN.setKingCanReverseDirection(false);
		AMERICAN.setQuantityRule(false);
		AMERICAN.setQualityRule(false);
		AMERICAN.setPriorityRule(false);

		ITALIAN.setName("Italian Dama");
		ITALIAN.setFamily(AMERICAN.getFamily());
		ITALIAN.setRows(8);
		ITALIAN.setCols(8);
		ITALIAN.setNumPieces(12);
		ITALIAN.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_WHITE);
		ITALIAN.setStartingPositions(StartingPositions.ALTERNATING_FROM_BOTTOM_RIGHT);
		ITALIAN.setStartingPlayer(StartingPlayer.WHITE);
		ITALIAN.setManMovementDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD });
		ITALIAN.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD });
		ITALIAN.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		ITALIAN.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		ITALIAN.setKingType(KingType.SHORT);
		ITALIAN.setKingsRowCapture(KingsRowCapture.STOP);
		ITALIAN.setRemovePiecesImmediately(false);
		ITALIAN.setManCanCaptureKing(false);
		ITALIAN.setKingCanReverseDirection(false);
		ITALIAN.setQuantityRule(true);
		ITALIAN.setQualityRule(true);
		ITALIAN.setPriorityRule(true);
		ITALIAN.setCaptureFirstKing(true);

		DAMA_TRIESTINA.copyVariant(ITALIAN);
		DAMA_TRIESTINA.setName("Dama Triestina");
		DAMA_TRIESTINA.setRows(10);
		DAMA_TRIESTINA.setCols(10);
		DAMA_TRIESTINA.setNumPieces(20);



		// --- Invented family ---

		DAMEO.setName("Dameo");
		DAMEO.setFamily(Family.SPECIAL);
		DAMEO.setRows(8);
		DAMEO.setCols(8);
		DAMEO.setNumPieces(18);
		DAMEO.setBoardPattern(BoardPattern.BOTTOM_LEFT_SQUARE_BLACK);
		DAMEO.setStartingPositions(StartingPositions.DAMEO);
		DAMEO.setStartingPlayer(StartingPlayer.WHITE);
		DAMEO.setManMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.DIAGONAL_FORWARD });
		DAMEO.setManCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD });
		DAMEO.setKingMovementDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD,
				RelativeDirection.DIAGONAL_FORWARD,
				RelativeDirection.DIAGONAL_BACKWARD });
		DAMEO.setKingCaptureDirections(new RelativeDirection[] {
				RelativeDirection.ORTHOGONAL_FORWARD,
				RelativeDirection.ORTHOGONAL_SIDEWAYS,
				RelativeDirection.ORTHOGONAL_BACKWARD });
		DAMEO.setKingType(KingType.FLYING);
		DAMEO.setKingsRowCapture(KingsRowCapture.SKIP);
		DAMEO.setRemovePiecesImmediately(false);
		DAMEO.setManCanCaptureKing(true);
		DAMEO.setKingCanReverseDirection(false);
		DAMEO.setQuantityRule(true);
		DAMEO.setQualityRule(false);
		DAMEO.setPriorityRule(false);
		DAMEO.setLinearMovement(true);



		// --- Custom variant ---
		CUSTOM.setName("Custom");

	}



	// The name of the variant
	private String name;

	// A description of the variant
	private String description;

	// The general category of the variant
	private Family family;

	// The number of rows on the board
	private int rows;

	// The number of columns on the board
	private int cols;

	// The number of pieces per side
	private int numPieces;

	// The pattern of the board
	private BoardPattern boardPattern;

	// The starting positions of pieces on the board
	private StartingPositions startingPositions;

	// The player who moves first
	private StartingPlayer startingPlayer;

	// Allowed direction(s) of a man's movement
	private RelativeDirection[] manMovementDirections;

	// Allowed direction(s) of a man's capture
	private RelativeDirection[] manCaptureDirections;

	// Allowed direction(s) of a king's movement
	private RelativeDirection[] kingMovementDirections;

	// Allowed direction(s) of a kings' capture
	private RelativeDirection[] kingCaptureDirections;

	// The type of king
	private KingType kingType;

	// The rule regarding what happens when a man lands in the kings row in the
	// middle of a capturing sequence
	private KingsRowCapture kingsRowCapture;

	// Whether a captured piece is removed from the board immediately
	private boolean removePiecesImmediately;

	// Whether a man can capture a king
	private boolean manCanCaptureKing;

	// Whether a king can reverse its direction 180 degrees during a capture
	private boolean kingCanReverseDirection;

	// Whether a capturing sequence must capture the maximum number of pieces
	private boolean quantityRule;

	// If there are two or several moves that capture the same number of men and
	// kings, whether a capture of the maximum number of kings is necessary
	private boolean qualityRule;

	// Whether the king must capture in a situation in which both a man and a
	// king can capture
	private boolean priorityRule;

	// The number of times a board state has to be repeated before a draw is
	// declared, if the rule is relevant to the variant. If not, setting this
	// field to -1 should nullify this rule
	private int numBoardRepeatsToDraw;

	// A rule associated with the number of turns a player must win in if each
	// player has a certain number of kings
	private KingVKingDrawRule kingVKingDrawRule;

	// A rule associated with whether a game is an automatic draw if each player
	// has a certain number of kings
	private KingVKingAutomaticDrawRule kingVKingAutomaticDrawRule;

	// If a man reaches the final row in a capture next to an undefended
	// opposing king, it must then proceed to capture this opponent’s king
	// before it is crowned itself
	private boolean prioritizeKingCaptureInKingsRow;

	// If a player may capture an equal number of pieces (each series containing
	// a king) with a king, whether he must capture wherever the opponent’s king
	// occurs first
	private boolean captureFirstKing;

	// The value of a man in a maximum capture (default = 1)
	private int quantityRuleManValue;

	// The value of a king in a maximum capture (default = 1)
	private int quantityRuleKingValue;

	// The maximum number of consecutive moves a king can make. This can be
	// reset if the king makes a capture or if the player moves another piece.
	// This is ignored if the player has only kings
	private int kingMaxConsecutiveMoves;

	// Whether a piece can only move along same-colored squares
	private boolean onlyMoveOnSameColor;

	// A player will lose if a player gets down to this many pieces
	private int minPieces;

	// Whether huffing is enforced
	private boolean huffing;

	// Whether a man can jump over a line of unbroken men of the same color
	private boolean linearMovement;



	/**
	 * Creates an empty variant
	 */
	private Variant() {
		this.setNumBoardRepeatsToDraw(-1);
		this.setKingVKingDrawRule(new KingVKingDrawRule());
		this.setKingVKingAutomaticDrawRule(new KingVKingAutomaticDrawRule());
		this.setQuantityRuleManValue(1);
		this.setQuantityRuleKingValue(1);
		this.setKingMaxConsecutiveMoves(Integer.MAX_VALUE);

		// Load description from text file
		InputStream inputStream = null;
		try {
			inputStream = Variant.class.getClassLoader().getResourceAsStream(
					"variant_descriptions/" + name().toLowerCase() + ".txt");
			BufferedReader inputFile = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			StringBuilder descriptionSB = new StringBuilder();
			String line;
			while ((line = inputFile.readLine()) != null) {
				descriptionSB.append(line);
			}
			setDescription(descriptionSB.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @return A description of the variant
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description A description of the variant
	 */
	private void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return The general category of the variant
	 */
	public Family getFamily() {
		return family;
	}

	/**
	 * @param family The general category of the variant
	 */
	private void setFamily(Family family) {
		this.family = family;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	private void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @return the cols
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * @param cols the cols to set
	 */
	private void setCols(int cols) {
		this.cols = cols;
	}

	/**
	 * @return the numPieces
	 */
	public int getNumPieces() {
		return numPieces;
	}

	/**
	 * @param numPieces the numPieces to set
	 */
	private void setNumPieces(int numPieces) {
		this.numPieces = numPieces;
	}

	/**
	 * @return the boardPattern
	 */
	public BoardPattern getBoardPattern() {
		return boardPattern;
	}



	/**
	 * @param boardPattern the boardPattern to set
	 */
	private void setBoardPattern(BoardPattern boardPattern) {
		this.boardPattern = boardPattern;
	}

	/**
	 * @return the startingPositions
	 */
	public StartingPositions getStartingPositions() {
		return startingPositions;
	}

	/**
	 * @param startingPositions the startingPositions to set
	 */
	private void setStartingPositions(StartingPositions startingPositions) {
		this.startingPositions = startingPositions;
	}

	/**
	 * @return the startingPlayer
	 */
	public StartingPlayer getStartingPlayer() {
		return startingPlayer;
	}

	/**
	 * @param startingPlayer the startingPlayer to set
	 */
	private void setStartingPlayer(StartingPlayer startingPlayer) {
		this.startingPlayer = startingPlayer;
	}

	/**
	 * @return the manMovementDirections
	 */
	public RelativeDirection[] getManMovementDirections() {
		return manMovementDirections;
	}

	/**
	 * @param manMovementDirections the manMovementDirections to set
	 */
	private void setManMovementDirections(RelativeDirection[] manMovementDirections) {
		this.manMovementDirections = manMovementDirections;
	}

	/**
	 * @return the manCaptureDirections
	 */
	public RelativeDirection[] getManCaptureDirections() {
		return manCaptureDirections;
	}

	/**
	 * @param manCaptureDirections the manCaptureDirections to set
	 */
	private void setManCaptureDirections(RelativeDirection[] manCaptureDirections) {
		this.manCaptureDirections = manCaptureDirections;
	}

	/**
	 * @return the kingMovementDirections
	 */
	public RelativeDirection[] getKingMovementDirections() {
		return kingMovementDirections;
	}

	/**
	 * @param kingMovementDirections the kingMovementDirections to set
	 */
	private void setKingMovementDirections(RelativeDirection[] kingMovementDirections) {
		this.kingMovementDirections = kingMovementDirections;
	}

	/**
	 * @return the kingCaptureDirections
	 */
	public RelativeDirection[] getKingCaptureDirections() {
		return kingCaptureDirections;
	}

	/**
	 * @param kingCaptureDirections the kingCaptureDirections to set
	 */
	private void setKingCaptureDirections(RelativeDirection[] kingCaptureDirections) {
		this.kingCaptureDirections = kingCaptureDirections;
	}

	/**
	 * @return the kingType
	 */
	public KingType getKingType() {
		return kingType;
	}

	/**
	 * @param kingType the kingType to set
	 */
	private void setKingType(KingType kingType) {
		this.kingType = kingType;
	}

	/**
	 * @return the kingsRowCapture
	 */
	public KingsRowCapture getKingsRowCapture() {
		return kingsRowCapture;
	}

	/**
	 * @param kingsRowCapture the kingsRowCapture to set
	 */
	private void setKingsRowCapture(KingsRowCapture kingsRowCapture) {
		this.kingsRowCapture = kingsRowCapture;
	}

	/**
	 * @return the removePiecesImmediately
	 */
	public boolean isRemovePiecesImmediately() {
		return removePiecesImmediately;
	}

	/**
	 * @param removePiecesImmediately the removePiecesImmediately to set
	 */
	private void setRemovePiecesImmediately(boolean removePiecesImmediately) {
		this.removePiecesImmediately = removePiecesImmediately;
	}

	/**
	 * @return the manCanCaptureKing
	 */
	public boolean isManCanCaptureKing() {
		return manCanCaptureKing;
	}

	/**
	 * @param manCanCaptureKing the manCanCaptureKing to set
	 */
	private void setManCanCaptureKing(boolean manCanCaptureKing) {
		this.manCanCaptureKing = manCanCaptureKing;
	}

	/**
	 * @return the kingCanReverseDirection
	 */
	public boolean isKingCanReverseDirection() {
		return kingCanReverseDirection;
	}

	/**
	 * @param kingCanReverseDirection the kingCanReverseDirection to set
	 */
	private void setKingCanReverseDirection(boolean kingCanReverseDirection) {
		this.kingCanReverseDirection = kingCanReverseDirection;
	}

	/**
	 * @return Whether maximum capture sequence is required
	 */
	public boolean hasQuantityRule() {
		return quantityRule;
	}

	/**
	 * @param quantityRule Whether maximum capture sequence is required
	 */
	private void setQuantityRule(boolean quantityRule) {
		this.quantityRule = quantityRule;
	}

	/**
	 * @return Whether a sequence with max kings is required
	 */
	public boolean hasQualityRule() {
		return qualityRule;
	}

	/**
	 * @param qualityRule Whether a sequence with max kings is required
	 */
	private void setQualityRule(boolean qualityRule) {
		this.qualityRule = qualityRule;
	}

	/**
	 * @return Whether a king is preferred over a man for a maximum capture
	 * sequence
	 */
	public boolean hasPriorityRule() {
		return priorityRule;
	}

	/**
	 * @param priorityRule Whether a king is preferred over a man for a maximum
	 * capture sequence
	 */
	private void setPriorityRule(boolean priorityRule) {
		this.priorityRule = priorityRule;
	}

	/**
	 * @return the numBoardRepeatsToDraw
	 */
	public int getNumBoardRepeatsToDraw() {
		return numBoardRepeatsToDraw;
	}

	/**
	 * @param numBoardRepeatsToDraw the numBoardRepeatsToDraw to set
	 */
	private void setNumBoardRepeatsToDraw(int numBoardRepeatsToDraw) {
		this.numBoardRepeatsToDraw = numBoardRepeatsToDraw;
	}

	/**
	 * @return Whether a rule exists to draw the game after a certain number of
	 * board state repeats
	 */
	public boolean hasNumBoardRepeatsToDraw() {
		return numBoardRepeatsToDraw != -1;
	}

	/**
	 * @return the kingVKingDrawRule
	 */
	public KingVKingDrawRule getKingVKingDrawRule() {
		return kingVKingDrawRule;
	}

	/**
	 * @param kingVKingDrawRule the kingVKingDrawRule to set
	 */
	private void setKingVKingDrawRule(KingVKingDrawRule kingVKingDrawRule) {
		this.kingVKingDrawRule = kingVKingDrawRule;
	}

	/**
	 * @return Whether a rule exists to draw the game after a certain number of
	 * turns following the players having a certain number of kings
	 */
	public boolean hasKingVKingDrawRule() {
		return !kingVKingDrawRule.isEmpty();
	}

	/**
	 * @return the kingVKingAutomaticDrawRule
	 */
	public KingVKingAutomaticDrawRule getKingVKingAutomaticDrawRule() {
		return kingVKingAutomaticDrawRule;
	}

	/**
	 * @param kingVKingAutomaticDrawRule the kingVKingAutomaticDrawRule to set
	 */
	private void setKingVKingAutomaticDrawRule(KingVKingAutomaticDrawRule kingVKingAutomaticDrawRule) {
		this.kingVKingAutomaticDrawRule = kingVKingAutomaticDrawRule;
	}

	/**
	 * @return Whether a rule exists to draw the game automatically if each
	 * player has a certain amount of kings
	 */
	public boolean hasKingVKingAutomaticDrawRule() {
		return !kingVKingAutomaticDrawRule.isEmpty();
	}

	/**
	 * @return the prioritizeKingCaptureInKingsRow
	 */
	public boolean isPrioritizeKingCaptureInKingsRow() {
		return prioritizeKingCaptureInKingsRow;
	}

	/**
	 * @param prioritizeKingCaptureInKingsRow the prioritizeKingCaptureInKingsRow to set
	 */
	private void setPrioritizeKingCaptureInKingsRow(boolean prioritizeKingCaptureInKingsRow) {
		this.prioritizeKingCaptureInKingsRow = prioritizeKingCaptureInKingsRow;
	}

	/**
	 * @return the captureFirstKing
	 */
	public boolean isCaptureFirstKing() {
		return captureFirstKing;
	}

	/**
	 * @param captureFirstKing the captureFirstKing to set
	 */
	private void setCaptureFirstKing(boolean captureFirstKing) {
		this.captureFirstKing = captureFirstKing;
	}

	/**
	 * @return the value of a man in a maximum capturing sequence
	 */
	public int getQuantityRuleManValue() {
		return quantityRuleManValue;
	}

	/**
	 * @param quantityRuleManValue the value of a man in a maximum capturing
	 * sequence
	 */
	private void setQuantityRuleManValue(int quantityRuleManValue) {
		this.quantityRuleManValue = quantityRuleManValue;
	}

	/**
	 * @return the value of a king in a maximum capturing sequence
	 */
	public int getQuantityRuleKingValue() {
		return quantityRuleKingValue;
	}

	/**
	 * @param quantityRuleKingValue the value of a king in a maximum capturing
	 * sequence
	 */
	private void setQuantityRuleKingValue(int quantityRuleKingValue) {
		this.quantityRuleKingValue = quantityRuleKingValue;
	}

	/**
	 * @return the kingMaxConsecutiveMoves
	 */
	public int getKingMaxConsecutiveMoves() {
		return kingMaxConsecutiveMoves;
	}

	/**
	 * @param kingMaxConsecutiveMoves the kingMaxConsecutiveMoves to set
	 */
	private void setKingMaxConsecutiveMoves(int kingMaxConsecutiveMoves) {
		this.kingMaxConsecutiveMoves = kingMaxConsecutiveMoves;
	}

	/**
	 * @return the onlyMoveOnSameColor
	 */
	public boolean canOnlyMoveOnSameColor() {
		return onlyMoveOnSameColor;
	}

	/**
	 * @param onlyMoveOnSameColor the onlyMoveOnSameColor to set
	 */
	private void setOnlyMoveOnSameColor(boolean onlyMoveOnSameColor) {
		this.onlyMoveOnSameColor = onlyMoveOnSameColor;
	}

	/**
	 * @return the minPieces
	 */
	public int getMinPieces() {
		return minPieces;
	}

	/**
	 * @param minPieces the minPieces to set
	 */
	private void setMinPieces(int minPieces) {
		this.minPieces = minPieces;
	}

	/**
	 * @return the huffing
	 */
	public boolean isHuffing() {
		return huffing;
	}

	/**
	 * @param huffing the huffing to set
	 */
	private void setHuffing(boolean huffing) {
		this.huffing = huffing;
	}

	/**
	 * @return the linearMovement
	 */
	public boolean hasLinearMovement() {
		return linearMovement;
	}

	/**
	 * @param linearMovement the linearMovement to set
	 */
	private void setLinearMovement(boolean linearMovement) {
		this.linearMovement = linearMovement;
	}

	// --- Custom setters ---

	/**
	 * @param rows the rows to set
	 */
	public static void setCustomRows(int rows) {
		CUSTOM.rows = rows;
	}

	/**
	 * @param cols the cols to set
	 */
	public static void setCustomCols(int cols) {
		CUSTOM.cols = cols;
	}

	/**
	 * @param numPieces the numPieces to set
	 */
	public static void setCustomNumPieces(int numPieces) {
		CUSTOM.numPieces = numPieces;
	}

	/**
	 * @param boardPattern the boardPattern to set
	 */
	public static void setCustomBoardPattern(BoardPattern boardPattern) {
		CUSTOM.boardPattern = boardPattern;
	}

	/**
	 * @param startingPositions the startingPositions to set
	 */
	public static void setCustomStartingPositions(StartingPositions startingPositions) {
		CUSTOM.startingPositions = startingPositions;
	}

	/**
	 * @param startingPlayer the startingPlayer to set
	 */
	public static void setCustomStartingPlayer(StartingPlayer startingPlayer) {
		CUSTOM.startingPlayer = startingPlayer;
	}

	/**
	 * @param manMovementDirections the manMovementDirections to set
	 */
	public static void setCustomManMovementDirections(RelativeDirection[] manMovementDirections) {
		CUSTOM.manMovementDirections = manMovementDirections;
	}

	/**
	 * @param manCaptureDirections the manCaptureDirections to set
	 */
	public static void setCustomManCaptureDirections(RelativeDirection[] manCaptureDirections) {
		CUSTOM.manCaptureDirections = manCaptureDirections;
	}

	/**
	 * @param kingMovementDirections the kingMovementDirections to set
	 */
	public static void setCustomKingMovementDirections(RelativeDirection[] kingMovementDirections) {
		CUSTOM.kingMovementDirections = kingMovementDirections;
	}

	/**
	 * @param kingCaptureDirections the kingCaptureDirections to set
	 */
	public static void setCustomKingCaptureDirections(RelativeDirection[] kingCaptureDirections) {
		CUSTOM.kingCaptureDirections = kingCaptureDirections;
	}

	/**
	 * @param kingType the kingType to set
	 */
	public static void setCustomKingType(KingType kingType) {
		CUSTOM.kingType = kingType;
	}

	/**
	 * @param kingsRowCapture the kingsRowCapture to set
	 */
	public static void setCustomKingsRowCapture(KingsRowCapture kingsRowCapture) {
		CUSTOM.kingsRowCapture = kingsRowCapture;
	}

	/**
	 * @param removePiecesImmediately the removePiecesImmediately to set
	 */
	public static void setCustomRemovePiecesImmediately(boolean removePiecesImmediately) {
		CUSTOM.removePiecesImmediately = removePiecesImmediately;
	}

	/**
	 * @param manCanCaptureKing the manCanCaptureKing to set
	 */
	public static void setCustomManCanCaptureKing(boolean manCanCaptureKing) {
		CUSTOM.manCanCaptureKing = manCanCaptureKing;
	}

	/**
	 * @param kingCanReverseDirection the kingCanReverseDirection to set
	 */
	public static void setCustomKingCanReverseDirection(boolean kingCanReverseDirection) {
		CUSTOM.kingCanReverseDirection = kingCanReverseDirection;
	}

	/**
	 * @param quantityRule Whether maximum capture sequence is required
	 */
	public static void setCustomQuantityRule(boolean quantityRule) {
		CUSTOM.quantityRule = quantityRule;
	}

	/**
	 * @param qualityRule Whether a sequence with max kings is required
	 */
	public static void setCustomQualityRule(boolean qualityRule) {
		CUSTOM.qualityRule = qualityRule;
	}

	/**
	 * @param priorityRule Whether a king is preferred over a man for a maximum
	 * capture sequence
	 */
	public static void setCustomPriorityRule(boolean priorityRule) {
		CUSTOM.priorityRule = priorityRule;
	}

	/**
	 * Sets fields to the same as another variant
	 * @param parentVariant The parent variant to copy
	 */
	private void copyVariant(Variant parentVariant) {
		this.family = parentVariant.family;

		this.rows = parentVariant.rows;
		this.cols = parentVariant.cols;
		this.numPieces = parentVariant.numPieces;
		this.numPieces = parentVariant.numPieces;
		this.boardPattern = parentVariant.boardPattern;
		this.startingPositions = parentVariant.startingPositions;
		this.startingPlayer = parentVariant.startingPlayer;
		this.manMovementDirections = parentVariant.manMovementDirections;
		this.manCaptureDirections = parentVariant.manCaptureDirections;
		this.kingMovementDirections = parentVariant.kingMovementDirections;
		this.kingCaptureDirections = parentVariant.kingCaptureDirections;
		this.kingType = parentVariant.kingType;
		this.kingsRowCapture = parentVariant.kingsRowCapture;
		this.removePiecesImmediately = parentVariant.removePiecesImmediately;
		this.manCanCaptureKing = parentVariant.manCanCaptureKing;
		this.kingCanReverseDirection = parentVariant.kingCanReverseDirection;
		this.quantityRule = parentVariant.quantityRule;
		this.qualityRule = parentVariant.qualityRule;
		this.priorityRule = parentVariant.priorityRule;

		this.numBoardRepeatsToDraw = parentVariant.numBoardRepeatsToDraw;
		this.kingVKingDrawRule = parentVariant.kingVKingDrawRule;
		this.kingVKingAutomaticDrawRule = parentVariant.kingVKingAutomaticDrawRule;
		this.prioritizeKingCaptureInKingsRow = parentVariant.prioritizeKingCaptureInKingsRow;
		this.captureFirstKing = parentVariant.captureFirstKing;
		this.quantityRuleManValue = parentVariant.quantityRuleManValue;
		this.quantityRuleKingValue = parentVariant.quantityRuleKingValue;
		this.kingMaxConsecutiveMoves = parentVariant.kingMaxConsecutiveMoves;
		this.onlyMoveOnSameColor = parentVariant.onlyMoveOnSameColor;
		this.minPieces = parentVariant.minPieces;
		this.huffing = parentVariant.huffing;
		this.linearMovement = parentVariant.linearMovement;
	}

	@Override
	public String toString() {
		return name;
	}
}
