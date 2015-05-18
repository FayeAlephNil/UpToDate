package pt.uptodate.api;

import java.util.HashMap;

public interface IUpdateable {
	/**
	 * Returns the mod name
	 * @return Mod name
	 */
	public String getName();

	/**
	 * Gets the remote update info
	 * @return Has to be valid JSON
	 */
	public String getRemote();

	/**
	 * Gets the local version info
	 * @return key is either 'technical' or 'display', value for technical has to be convertible to an int
	 */
	public HashMap<String, String> getLocal();
}