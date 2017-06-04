package config;

import java.util.Locale;

/**
 * Configuration application class
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
	public static final String EXPORT_FILE_NAME = "MeshPart";
	
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
