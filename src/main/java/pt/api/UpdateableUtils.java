package pt.api;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import pt.uptodate.util.io.IOUtils;
import pt.uptodate.util.io.WebUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Strikingwolf
 */
public class UpdateableUtils {
	public static String fromUrlPlain(String url) {
		List<String> read = WebUtils.readWebsite(url);
		if (read == null) {
			return null;
		} else {
			return StringUtils.join(read, "");
		}
	}

	public static HashMap<String, String> localVersionFile(File file) {
		Set<String> read = IOUtils.readLines(file);
		Yaml yaml = new Yaml();
		Map<String, Object> load = (Map<String, Object>) yaml.load(StringUtils.join(read));

		HashMap<String, String> result = new HashMap<String, String>();
		result.put("localDisp", (String) load.get("localDisp"));
		result.put("localTech", (String) load.get("localTech"));

		return result;
	}
}
