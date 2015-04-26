package pt.uptodate.util;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebUtil {
	public static void openBrowser(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
