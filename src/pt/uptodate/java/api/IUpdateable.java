package pt.uptodate.java.api;

import java.util.HashMap;

public interface IUpdateable {

	// Has to be valid YAML or JSON
	public String getRemote();

	// key is either 'technical' or 'display', value for technical has to be convertible to an int
	public HashMap<String, String> getLocal();
}
