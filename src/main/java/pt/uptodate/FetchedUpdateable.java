package pt.uptodate;

import org.yaml.snakeyaml.Yaml;
import pt.uptodate.api.IUpdateable;
import pt.uptodate.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Strikingwolf
 */

public class FetchedUpdateable {

	public final boolean auto;
	public final int severity;
	public final String displaySeverity;
	public final String display;
	public final String oldDisp;
	public final String url;

	public final int old;
	public final int version;

	public final String name;

	/**
	 * Creates a FetchedUpdateable from an IUpdateable with the getRemote
	 * @param mod IUpdateable
	 */
	public FetchedUpdateable(IUpdateable mod) {
		this.name = mod.getName();

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

		this.displaySeverity = makeDisplayS(severity);
	}

	/**
	 * Makes a custom FetchedUpdateable from an IUpdateable
	 * @param mod IUpdateable
	 * @param severity severity of update
	 * @param display display version
	 * @param oldDisp old display version
	 * @param url url of download
	 * @param old old version
	 * @param version new version
	 */
	public FetchedUpdateable(IUpdateable mod, boolean auto, int severity, String display, String oldDisp, String url, int old, int version) {
		this(mod, auto, severity, makeDisplayS(severity),display, oldDisp, url, old, version);
	}

	/**
	 * Makes a FetchedUpdateable with auto set to false by default
	 * @param mod IUpdateable
	 * @param severity severity of update
	 * @param display display version
	 * @param oldDisp old display version
	 * @param url url of download
	 * @param old old version
	 * @param version new version
	 */
	public FetchedUpdateable(IUpdateable mod, int severity, String display, String oldDisp, String url, int old, int version) {
		this(mod, false, severity, display, oldDisp, url, old, version);
	}

	/**
	 * Makes a FetchedUpdateable with auto set to false by default
	 * @param mod IUpdateable
	 * @param severity severity of update
	 * @param display display version
	 * @param displaySeverity display Severity
	 * @param oldDisp old display version
	 * @param url url of download
	 * @param old old version
	 * @param version new version
	 */
	public FetchedUpdateable(IUpdateable mod, int severity, String displaySeverity, String display, String oldDisp, String url, int old, int version) {
		this(mod, false, severity, displaySeverity, display, oldDisp, url, old, version);
	}

	/**
	 * Makes a FetchedUpdateable from an IUpdateable
	 * @param mod IUpdateable
	 * @param auto will mod auto-download updates
	 * @param severity severity of update
	 * @param display display version
	 * @param displaySeverity display Severity
	 * @param oldDisp old display version
	 * @param url url of download
	 * @param old old version
	 * @param version new version
	 */
	public FetchedUpdateable(IUpdateable mod, boolean auto, int severity, String displaySeverity, String display, String oldDisp, String url, int old, int version) {
		this(auto, severity, displaySeverity, display, oldDisp, url, old, version, mod.getName());
	}

	/**
	 * Makes a FetchedUpdateable without a display Severity
	 * @param auto will mod auto-download updates
	 * @param severity severity of update
	 * @param display display version
	 * @param oldDisp old display version
	 * @param url url of download
	 * @param old old version
	 * @param version new version
	 * @param modName name of the mod
	 */
	public FetchedUpdateable(boolean auto, int severity, String display, String oldDisp, String url, int old, int version, String modName) {
		this(auto, severity, makeDisplayS(severity), display, oldDisp, url, old, version, modName);
	}

	/**
	 * Lowest level constructor. Makes a FetchedUpdateable
	 * @param auto will mod auto-download updates
	 * @param severity severity of update
	 * @param displaySeverity severity to display
	 * @param display display version
	 * @param oldDisp old display version
	 * @param url url of download
	 * @param old old version
	 * @param version new version
	 * @param modName name of the mod
	 */
	public FetchedUpdateable(boolean auto, int severity, String displaySeverity, String display, String oldDisp, String url, int old, int version, String modName) {
		this.auto = auto;
		this.severity = severity;
		this.displaySeverity = displaySeverity;
		this.display = display;
		this.oldDisp = oldDisp;
		this.url = url;
		this.old = old;
		this.version = version;
		this.name = modName;
	}

	/**
	 * Makes a display severity
	 * @param severity integer severity
	 * @return display severity
	 */
	protected static String makeDisplayS(int severity) {
		if (severity < 2) {
			return "Normal";
		} else if (severity < 3) {
			return "Severe!";
		}
		return "Critical!!";
	}
}
