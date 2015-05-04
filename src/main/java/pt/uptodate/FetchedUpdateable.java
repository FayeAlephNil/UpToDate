package pt.uptodate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import pt.api.IUpdateable;
import pt.uptodate.util.Util;

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

	@SuppressWarnings("unchecked")
	public FetchedUpdateable(IUpdateable mod) {
		this.mod = mod;

		Yaml yaml = new Yaml();
		Map<String, Object> load = (Map<String, Object>) yaml.load(mod.getRemote());
		this.auto = (Boolean) load.get("auto");
		this.url = (String) load.get("url");

		List<Integer> severityL = (List<Integer>) load.get("severity");
		List<String> displayL = (List<String>) load.get("display");
		List<Integer> versionL = (List<Integer>) load.get("technical");

		HashMap<String, String> local = mod.getLocal();
		this.old = Integer.valueOf(local.get("technical"));
		this.oldDisp = local.get("display");

		int splitIndex = versionL.indexOf(this.old);
		this.version = versionL.get(versionL.size() - 1);
		this.display = displayL.get(versionL.size() - 1);
		this.severity = Util.max(Util.after(severityL, splitIndex));

		this.diff = this.version - this.old;

		if (this.severity < 2) {
			this.displaySeverity = "Normal";
		} else if (this.severity < 3) {
			this.displaySeverity = "Severe!";
		} else {
			this.displaySeverity = "Critical!!";
		}
	}
}
