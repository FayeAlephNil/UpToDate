package main.uptodate.java;

import org.yaml.snakeyaml.Yaml;
import api.java.IUpdateable;
import main.uptodate.java.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Strikingwolf
 */
public class FetchedUpdateable {

	public boolean auto;
	public int severity;
	public String display;
	public String oldDisp;
	public int old;
	public int version;
	public int diff;

	public IUpdateable mod;

	public FetchedUpdateable(IUpdateable mod) {
		Yaml yaml = new Yaml();
		Map<String, Object> load = (Map<String, Object>) yaml.load(mod.getRemote());
		Logger.info(load.toString());

		auto = (Boolean) load.get("auto");
		severity = (Integer) load.get("severity");
		display = (String) load.get("display");
		version = (Integer) load.get("technical");

		HashMap<String, String> local = mod.getLocal();
		old = Integer.valueOf(local.get("technical"));
		oldDisp = local.get("display");

		diff = version - old;
	}
}
