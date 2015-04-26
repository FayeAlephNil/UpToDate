package pt.uptodate.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Strikingwolf
 */
public class Util {
	public static boolean netIsAvailable() {
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			return false;
		}
	}

	public static String joinAnd(Iterable<String> strings) {
		String result = null;
		Iterator<String> iter = strings.iterator();
		for (int i = 0; iter.hasNext(); i++) {
			result += ", ";
			String next = iter.next();

			if (!iter.hasNext()) {
				result += " and ";
			}

			result += next;
		}
		return result;
	}

	public static Integer max(Iterable<Integer> nums) {
		Integer result = null;
		for (Integer num : nums) {
			if (result == null || num > result) {
				result = num;
			}
		}
		return result;
	}

	public static <T> List<T> after(List<T> list, int index) {
		for (int i = 0; i < index; i++) {
			list.remove(i);
		}
		return list;
	}
}
