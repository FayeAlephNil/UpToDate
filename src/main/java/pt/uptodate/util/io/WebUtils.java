package pt.uptodate.util.io;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

/**
 * @author CoolSquid
 */

public class WebUtils {

	public static URL newURL(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static URLConnection newConnection(String url) {
		return newConnection(newURL(url));
	}

	public static URLConnection newConnection(URL url) {
		try {
			return url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static InputStream getStream(String url) {
		return getStream(newURL(url));
	}

	public static InputStream getStream(URL url) {
		try {
			return url.openStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static URLReader newReader(String url) {
		return new URLReader(newURL(url));
	}

	public static URLReader newReader(URL url) {
		return new URLReader(url);
	}

	public static void openBrowser(String url) {
		openBrowser(newURI(url));
	}

	public static void openBrowser(URI uri) {
		try {
			Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class URLReader implements Iterable<String> {

		private final URL url;

		public URLReader(String url) {
			this.url = WebUtils.newURL(url);
		}

		public URLReader(URL url) {
			this.url = url;
		}

		@Override
		public Iterator<String> iterator() {
			return new SiteIterator();
		}

		private class SiteIterator implements Iterator<String> {

			private final BufferedReader a = IOUtils.newReader(WebUtils.getStream(URLReader.this.url));
			private String line;

			@Override
			public boolean hasNext() {
				this.line = IOUtils.readLine(this.a);
				return this.line != null;
			}

			@Override
			public String next() {
				return this.line;
			}

			@Override
			public void remove() {
				this.line = IOUtils.readLine(this.a);
			}
		}
	}

	public static URI newURI(String url) {
		try {
			return new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String readAll(String url) {
		StringBuilder b = new StringBuilder();
		for (String line: newReader(url)) {
			b.append(line);
		}
		return b.toString();
	}
}