package pt.uptodate.util;


import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebUtils {

	public static URL newURL(String url) {
		if (!url.contains("://")) {
			url = "http://" + url;
		}
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void openBrowser(String url) {
		openBrowser(newURL(url));
	}

	public static void openBrowser(URL url) {
		openBrowser(newURI(url));
	}

	public static void openBrowser(URI uri) {
		try {
			Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static URI newURI(URL url) {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}
