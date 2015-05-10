package pt.api;

import org.yaml.snakeyaml.Yaml;
import pt.uptodate.FetchedUpdateable;
import pt.uptodate.UpToDate;
import pt.uptodate.util.io.IOUtils;
import pt.uptodate.util.io.WebUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Strikingwolf
 */
public class UpdateableUtils {

	/**
	 * Gets the local versions from a file, for use in getLocal
	 * @param file The file to read from
	 * @return The hashmap to return from getLocal
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> localVersionFile(File file) {
		Yaml yaml = new Yaml();
		Map<String, Object> load = (Map<String, Object>) yaml.load(IOUtils.readAll(file));

		HashMap<String, String> result = new HashMap<String, String>();
		result.put("technical", (String) load.get("localDisp"));
		result.put("display", (String) load.get("localTech"));

		return result;
	}

	/**
	 * Turns the url into text, for use in getRemote. URL should be raw text and YAML
	 * @param url the url to use
	 * @return The string to return from getRemote
	 */
	public static String fromUrlToText(String url) {
		return WebUtils.readAll(url);
	}

	/**
	 * Registers an updateable to uptodate's registries
	 * @param updateable the IUpdateable to register
	 */
	public static void registerUpdateable(IUpdateable updateable) {
		UpToDate.registerUpdateable(updateable);
	}

	/**
	 * Registers a FetchedUpdateable to uptodate's registries
	 * @param fetched the FetchedUpdateable to register
	 */
	public static void registerFetched(FetchedUpdateable fetched) {
		UpToDate.registerFetched(fetched);
	}
}
