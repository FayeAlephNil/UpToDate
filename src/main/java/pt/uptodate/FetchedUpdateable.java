package pt.uptodate;

import com.google.gson.Gson;
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
	@SuppressWarnings("unchecked")
	public FetchedUpdateable(IUpdateable mod) {
		this.name = mod.getName();

		Map<String, Object> load = new Gson().fromJson(mod.getRemote(), Map.class);
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

	/**
	 * A builder for creating FetchedUpdateables. Example:
	 * <pre><code>FetchedUpdateable fetched = new Builder()
	 * .setAuto(true)
	 * .setDisplay("2.0.0")
	 * .setVersion(200)
	 * .setDisplaySeverity("Critical")
	 * .setSeverity(3)
	 * .setName("ExampleMod")
	 * .setOldDisp("1.0.0")
	 * .setOld(100)
	 * .setUrl("example.com")
	 * .build();</code></pre>
	 *
	 * The Builder can be reused, and <code>build()</code> can be called an infinite amount of times.
	 *
	 * @author CoolSquid
	 */
	public static class Builder {

		private boolean auto;
		private int severity;
		private String displaySeverity;
		private String display;
		private String oldDisp;
		private String url;
		private int old;
		private int version;
		private String name;

		/**
		 * Sets the auto.
		 *
		 * @param auto the auto
		 * @return the builder
		 */
		public Builder setAuto(boolean auto) {
			this.auto = auto;
			return this;
		}

		/**
		 * Sets the severity.
		 *
		 * @param severity the severity
		 * @return the builder
		 */
		public Builder setSeverity(int severity) {
			this.severity = severity;
			return this;
		}

		/**
		 * Sets the display severity.
		 *
		 * @param displaySeverity the display severity
		 * @return the builder
		 */
		public Builder setDisplaySeverity(String displaySeverity) {
			this.displaySeverity = displaySeverity;
			return this;
		}

		/**
		 * Sets the display.
		 *
		 * @param display the display
		 * @return the builder
		 */
		public Builder setDisplay(String display) {
			this.display = display;
			return this;
		}

		/**
		 * Sets the old disp.
		 *
		 * @param oldDisp the old disp
		 * @return the builder
		 */
		public Builder setOldDisp(String oldDisp) {
			this.oldDisp = oldDisp;
			return this;
		}

		/**
		 * Sets the url.
		 *
		 * @param url the url
		 * @return the builder
		 */
		public Builder setUrl(String url) {
			this.url = url;
			return this;
		}

		/**
		 * Sets the old.
		 *
		 * @param old the old
		 * @return the builder
		 */
		public Builder setOld(int old) {
			this.old = old;
			return this;
		}

		/**
		 * Sets the version.
		 *
		 * @param version the version
		 * @return the builder
		 */
		public Builder setVersion(int version) {
			this.version = version;
			return this;
		}

		/**
		 * Sets the name.
		 *
		 * @param name the name
		 * @return the builder
		 */
		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Builds the fetched updateable.
		 *
		 * @return the fetched updateable
		 */
		public FetchedUpdateable build() {
			return new FetchedUpdateable(this.auto, this.severity, this.displaySeverity, this.display, this.oldDisp, this.url, this.old, this.version, this.name);
		}
	}
}
