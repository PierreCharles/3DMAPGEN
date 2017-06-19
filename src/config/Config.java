package config;

import java.util.Locale;

/**
 * Configuration application class
 * Used to define all configuration variables
 * 
 * @author picharles
 *
 */
public class Config {

	// Define if the application is in DEBUG mode
	public static final boolean DEBUG = true;
	
	// Define the default application language
	public static final String DEFAULT_LANG = "fr";
	
	// Variable used for the current selected language
	public static Locale Current_Language = new Locale(DEFAULT_LANG);
	
	// Name of the export file
	public static final String EXPORT_PREFIX_FILE_NAME = "MeshPart";
	
	// Name of the output folder to export obj file
	public static final String OUTPUR_FODLER_NAME = "Mesh";
	
	// Letter deep square size (under the map)
	public static final int LETTER_SIZE_SQUARE = 0;
	
	//Base map size
	public static final int BASE_MAP_SIZE = 25;
	
	// -------- Clips size ------ //
	public static final int WIDTH_CLIP_INSIDE = 1; // Width inside of the clip
	public static final int CLIP_HEIGHT_INSIDE = 2; // Total height of the clip
	public static final int CLIP_HEIGHT = 4; // Total height of the clip
	public static final int WIDTH_CLIP_OUTSIDE = 2; // The with of the clip outside
	
	/**
	 * Debug function for displaying a message
	 * @param message
	 */
	public static void Debug(String message){
		if(Config.DEBUG){
			System.out.println(message);
		}
	}
	
}
