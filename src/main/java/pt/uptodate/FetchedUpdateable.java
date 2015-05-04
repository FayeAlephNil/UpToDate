package pt.uptodate;

import org.yaml.snakeyaml.Yaml;
import pt.api.IUpdateable;
import pt.uptodate.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Strikingwolf
 */
public class FetchedUpdateable {

	public boolean auto;
	public int severity;
	public String displaySeverity;
	public String display;
	public String oldDisp;
	public String url;

	public int old;
	public int version;
	public int diff;

	public IUpdateable mod;

	public boolean chatted = false;

	public FetchedUpdateable(IUpdateable mod) {
		this.mod = mod;

		Yaml yaml = new Yaml();
		Map<String, Object> load = (Map<String, Object>) yaml.load(mod.getRemote());
		auto = (Boolean) load.get("auto");
		url = (String) load.get("url");

		List<Integer> severityL = (List<Integer>) load.get("severity");
		List<String> displayL = (List<String>) load.get("display");
		List<Integer> versionL = (List<Integer>) load.get("technical");

		HashMap<String, String> local = mod.getLocal();
		old = Integer.valueOf(local.get("technical"));
		oldDisp = local.get("display");

		int splitIndex = versionL.indexOf(old);
		version = versionL.get(versionL.size() - 1);
		display = displayL.get(versionL.size() - 1);
		severity = Util.max(Util.after(severityL, splitIndex));

		diff = version - old;

		if (severity < 2) {
			displaySeverity = "Normal";
		} else if (severity < 3) {
			displaySeverity = "Severe!";
		} else {
			displaySeverity = "Critical!!";
		}
	}
}
