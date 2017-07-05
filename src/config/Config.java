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
	// Name of the output folder to export obj file
	public static final String OUTPUR_FODLER_NAME = "Mesh";

	/* ------------------- Size map and clip config ------------------------ */

	// Elevation of the surface map
	public static final int MAP_ELEVATION = 5;
	// Elevation (tickness) of the base map
	public static final int BASE_MAP_TICKNESS = 2;
	
	// Elevation (Tickness) of the base map raised
	public static final int BASE_MAP_RAISED_TICKNESS = 4;
	// Elevation (Tickness) (for the middle side face)
	public static final int BASE_MAP_RAISED_SIDE_TICKNESS = 5;
	// Size of ID square under the map (square for displaying the letter)
	public static final int MIDDLE_SQUARE_MAP_SIZE = 40;
	// Clips size
	public static final int INSIDE_WIDTH_CLIP = 10; // Width inside of the clip
	public static final int INSIDE_HEIGHT_CLIP = 20; // Total height of the clip
	public static final int TOTAL_CLIP_HEIGHT = 40; // Total height of the clip
	public static final int OUTSIDE_WIDTH_CLIP = 20; // With of the outside clip
	
	// Minimum marge between clip and middle square
	public static final int INNER_BASE_MINIMUM_MAP_MARGE = 10; 
	
	// Minimum base map size
	public static final int MINIMUM_BASE_MAP_SIZE = 
			TOTAL_CLIP_HEIGHT + MIDDLE_SQUARE_MAP_SIZE + INNER_BASE_MINIMUM_MAP_MARGE;


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
