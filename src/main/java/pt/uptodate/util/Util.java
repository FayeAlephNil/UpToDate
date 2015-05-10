package pt.uptodate.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

	public static boolean anyNulls(Object... objects) {
		return anyNulls(objects);
	}

	public static <T> boolean anyNulls(Iterable<T> possibleNulls) {
		for (T item : possibleNulls) {
			if (item == null) {
				return true;
			}
		}
		return false;
	}
}
