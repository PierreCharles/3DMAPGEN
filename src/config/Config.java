package config;

import java.util.Locale;

/**
 * Configuration application class Used to define all configuration variables
 * The size is writen in cm (print size)
 * 
 * @author picharles
 *
 */
public class Config {

	/* -------------------------- Global settings -------------------------- */

	// Define if the application is in DEBUG mode
	public static final boolean DEBUG = true;
	// Define the default application language
	public static final String DEFAULT_LANG = "fr";
	// Variable used for the current selected language
	public static Locale Current_Language = new Locale(DEFAULT_LANG);
	// Map mesh Name of the export file
	public static final String EXPORT_PREFIX_FILE_NAME = "Mesh";
	// Name of the output folder to export obj file
	public static final String OUTPUR_FODLER_NAME = "Mesh";

	/* ------------------- Size map and clip config ------------------------ */

	// Elevation of the surface map
	public static final int MAP_ELEVATION = 5;
	// Tickness of the base map
	public static final int BASE_MAP_TICKNESS = 2;
	// Tickness base map raised
	public static final int BASE_MAP_RAISED_TICKNESS = 4;
	public static final int BASE_MAP_RAISED_SIDE_TICKNESS = 5;
	// Size of ID square under the map (square for displaying the letter)
	public static final int MIDDLE_SQUARE_MAP_SIZE = 40;
	// Clips size
	public static final int INSIDE_WIDTH_CLIP = 10; // Width inside of the clip
	public static final int INSIDE_HEIGHT_CLIP = 20; // Total height of the clip
	public static final int TOTAL_CLIP_HEIGHT = 40; // Total height of the clip
	public static final int OUTSIDE_WIDTH_CLIP = 20; // With of the outside clip

	/* ------------------------- Debug methods --------------------------- */

	/**
	 * Debug function for displaying a message
	 * 
	 * @param message
	 */
	public static void Debug(String message) {
		if (Config.DEBUG) {
			System.out.println(message);
		}
	}
}
