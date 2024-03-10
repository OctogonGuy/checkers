package tech.octopusdragon.checkers.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

public class Config {
	
	// --- Static members ---
	
	private static final String USERDATA_DIRECTORY = "userdata/";
	private static final String USERDATA_FILENAME = "checkers_config.json";
	
	private static Config config;
	private static Gson gson;
	
	static {
		// Instantiate using GsonBuilder to include newlines, whitespace, etc.
		gson = new GsonBuilder().setPrettyPrinting().create();
		
		// Read config data from JSON
		try {
			config = gson.fromJson(new JsonReader(new InputStreamReader(new FileInputStream(
					USERDATA_DIRECTORY + USERDATA_FILENAME))), Config.class);
		} catch (JsonParseException | IOException e) {
			config = new Config();
			save();
		}
	}
	
	/**
	 * Writes data to JSON
	 */
	public static void save() {
		
		// Make sure userdata folder exists
		File folder = new File(USERDATA_DIRECTORY);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(
					USERDATA_DIRECTORY + USERDATA_FILENAME));
			gson.toJson(config, writer);
			writer.flush();
			writer.close();
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	// --- Static getters and setters ---
	
	/**
	 * @return the topPlayer
	 */
	public static PlayerType getTopPlayer() {
		return config.topPlayer;
	}

	/**
	 * Sets and writes topPlayer to config file
	 * @param topPlayer the topPlayer to set
	 */
	public static void setTopPlayer(PlayerType topPlayer) {
		config.topPlayer = topPlayer;
		save();
	}

	/**
	 * @return the highlightMoves
	 */
	public static boolean isHighlightMoves() {
		return config.highlightMoves;
	}

	/**
	 * Sets and writes highlightMoves to config file
	 * @param highlightMoves the highlightMoves to set
	 */
	public static void setHighlightMoves(boolean highlightMoves) {
		config.highlightMoves = highlightMoves;
		save();
	}

	/**
	 * @return the isBlackComputerPlayer
	 */
	public static boolean isBlackComputerPlayer() {
		return config.isBlackComputerPlayer;
	}

	/**
	 * Sets and writes isBlackComputerPlayer to config file
	 * @param isBlackComputerPlayer the isBlackComputerPlayer to set
	 */
	public static void setBlackComputerPlayer(boolean isBlackComputerPlayer) {
		config.isBlackComputerPlayer = isBlackComputerPlayer;
		save();
	}

	/**
	 * @return the isWhiteComputerPlayer
	 */
	public static boolean isWhiteComputerPlayer() {
		return config.isWhiteComputerPlayer;
	}

	/**
	 * Sets and writes isWhiteComputerPlayer to config file
	 * @param isWhiteComputerPlayer the isWhiteComputerPlayer to set
	 */
	public static void setWhiteComputerPlayer(boolean isWhiteComputerPlayer) {
		config.isWhiteComputerPlayer = isWhiteComputerPlayer;
		save();
	}
	
	
	
	// --- Instance members ---

	private PlayerType topPlayer;
	private boolean highlightMoves;
	private boolean isBlackComputerPlayer;
	private boolean isWhiteComputerPlayer;
	
	/**
	 * Initialize with default values
	 */
	private Config() {
		topPlayer = PlayerType.BLACK;
		highlightMoves = true;
		isBlackComputerPlayer = false;
		isWhiteComputerPlayer = false;
	}

}
