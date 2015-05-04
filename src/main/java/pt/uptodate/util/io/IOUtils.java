/*******************************************************************************
 * Copyright (c) 2015 CoolSquid.
 *
 *******************************************************************************/
package pt.uptodate.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class IOUtils {

	public static FileInputStream newInputStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String readLine(BufferedReader reader) {
		try {
			return reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static BufferedReader newReader(InputStream stream) {
		return new BufferedReader(new InputStreamReader(stream));
	}

	public static FileReader newReader(File file) {
		return new FileReader(file);
	}

	public static class FileReader implements Iterable<String> {

		private final File file;

		public FileReader(File file) {
			if (file == null) {
				throw new IllegalArgumentException();
			}
			if (!file.exists()) {
				try {
					if (file.getParentFile() != null && !file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					file.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.file = file;
		}

		@Override
		public Iterator<String> iterator() {
			return new FileIterator();
		}

		private class FileIterator implements Iterator<String> {

			private final BufferedReader a = IOUtils.newReader(IOUtils.newInputStream(FileReader.this.file));
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
				IOUtils.readLine(this.a);
			}
		}
	}

	public static String readAll(File file) {
		StringBuilder b = new StringBuilder();
		for (String line: newReader(file)) {
			b.append(line);
		}
		return b.toString();
	}
}