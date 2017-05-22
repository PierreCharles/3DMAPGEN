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
	
}
