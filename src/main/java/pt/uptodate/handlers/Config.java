package pt.uptodate.handlers;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import pt.uptodate.UpToDate;

import java.io.File;


public class Config {
	public static Configuration configuration;

	public static boolean chat = false;
	public static boolean severe = true;
	public static boolean colorblind = false;

	/**
	 * Creates the Config file
	 * @param configFile file to use
	 */
	public static void init(File configFile) {

		//create configuration object from the given file
		if (configuration == null)
		{
			configuration = new Configuration(configFile);
			loadConfiguration();
		}
	}

	/**
	 * Loads the config file when it is changed
	 * @param event event fired on change
	 */
	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.modID.equalsIgnoreCase(UpToDate.MOD_ID))
		{
			loadConfiguration();
		}
	}

	/**
	 * Loads the config
	 */
	private static void loadConfiguration()
	{
		chat = configuration.getBoolean("chat", Configuration.CATEGORY_GENERAL, false, "Setting this to true will turn on chat update notifications and disable the click-to-advance screen");
		severe = configuration.getBoolean("severe", Configuration.CATEGORY_GENERAL, true, "Setting this to false will turn off severe and critical updates stopping loading");
		colorblind = configuration.getBoolean("colorblind", Configuration.CATEGORY_GENERAL, false, "Setting this to true will disable colors and add in markers");

		if (configuration.hasChanged())
		{
			configuration.save();
		}
	}
}
