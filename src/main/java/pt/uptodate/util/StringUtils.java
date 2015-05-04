/*******************************************************************************
 * Copyright (c) 2015 CoolSquid.
 * All rights reserved.
 *******************************************************************************/
package pt.uptodate.util;

public class StringUtils {

	public static StringBuilder builder() {
		return new StringBuilder();
	}

	public static String newStringWithSpaces(Object... objects) {
		return newStringWithSpaces2(objects);
	}

	public static String newStringWithSpaces2(Object[] objects) {
		if (objects == null || objects.length <= 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (Object object: objects) {
			if (object != null) {
				builder.append(object.toString());
				builder.append(" ");
			}
		}
		return builder.substring(0, builder.length() - 1);
	}

	public static String repeat(String string, int length) {
		StringBuilder a = builder();
		for (int b = 0; b < length; b++) {
			a.append(string);
		}
		return a.toString();
	}

	public static String repeat(char c, int length) {
		StringBuilder a = builder();
		for (int b = 0; b < length; b++) {
			a.append(c);
		}
		return a.toString();
	}

	public static String getString(char... b) {
		StringBuilder c = builder();
		boolean e = true;
		for (int a = 0; a < b.length; a++) {
			if (e) {
				c.append(b[a + 1]);
				e = false;
			}
			else {
				c.append(b[a - 1]);
				e = true;
			}
		}
		return c.toString();
	}

	public static String ensureNotNull(String string) {
		return string != null ? string : "";
	}

	public static String newString(Object... objects) {
		return newString2(objects);
	}

	public static String newString2(Object[] objects) {
		if (objects == null || objects.length <= 0) {
			return "";
		}
		StringBuilder builder = builder();
		for (Object object: objects) {
			if (object != null) {
				builder.append(object.toString());
			}
		}
		return builder.toString();
	}
}