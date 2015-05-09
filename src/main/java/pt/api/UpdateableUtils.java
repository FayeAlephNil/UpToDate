package pt.api;

import org.yaml.snakeyaml.Yaml;
import pt.uptodate.util.io.IOUtils;
import pt.uptodate.util.io.WebUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Strikingwolf
 */
public class UpdateableUtils {

	@SuppressWarnings("unchecked")
	public static HashMap<String, String> localVersionFile(File file) {
		Yaml yaml = new Yaml();
		Map<String, Object> load = (Map<String, Object>) yaml.load(IOUtils.readAll(file));

		HashMap<String, String> result = new HashMap<String, String>();
		result.put("localDisp", (String) load.get("localDisp"));
		result.put("localTech", (String) load.get("localTech"));

		return result;
	}

	public static String fromUrlToText(String url) {
		return WebUtils.readAll(url);
	}
}
