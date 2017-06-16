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
