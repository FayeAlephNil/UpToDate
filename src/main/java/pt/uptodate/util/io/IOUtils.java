package pt.uptodate.util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class IOUtils {

	public static FileInputStream newInputStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String readLine(File file) {
		return readLine(newReader(newInputStream(file)));
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

	public static String getLine(BufferedReader reader, String key) {
		while (true) {
			String b = readLine(reader);
			if (b == null) {
				break;
			}
			else if (b.contains(key)) {
				return b;
			}
		}
		return key;
	}

	public static List<String> getLines(File file, String key) {
		List<String> list = Lists.newArrayList();
		for (String string: new FileReader(file)) {
			if (string.contains(key)) {
				list.add(string);
			}
		}
		return list;
	}

	public static FileOutputStream newOutputStream(File file) {
		try {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			return new FileOutputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static BufferedWriter newWriter(OutputStream stream) {
		return new BufferedWriter(new OutputStreamWriter(stream));
	}

	public static BufferedWriter newWriter(File file) {
		return new BufferedWriter(new OutputStreamWriter(newOutputStream(file)));
	}

	public static void writeLines(File file, Iterable<String> lines) {
		if (file == null) {
			throw new IllegalArgumentException();
		}
		BufferedWriter a = newWriter(newOutputStream(file));
		try {
			for (String b: lines) {
				a.write(b);
				a.newLine();
			}
			a.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeLine(File file, String line) {
		if (file == null) {
			throw new IllegalArgumentException();
		}
		BufferedWriter a = newWriter(newOutputStream(file));
		try {
			a.write(line);
			a.newLine();
			a.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeLines(File file, Object... lines) {
		if (file == null || !FileFilters.TEXT_FILES.accept(file)) {
			throw new IllegalArgumentException();
		}
		BufferedWriter a = newWriter(newOutputStream(file));
		try {
			for (Object b: lines) {
				a.write(b.toString());
				a.newLine();
			}
			a.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Set<String> readLines(File file) {
		if (file == null) {
			throw new IllegalArgumentException();
		}
		Set<String> a = Sets.newHashSet();
		for (String b: newReader(file)) {
			a.add(b);
		}
		return a;
	}

	public static List<String> readAllFilesInFolder(File folder) {
		if (folder == null || !folder.isDirectory()) {
			throw new IllegalArgumentException();
		}
		List<String> list = Lists.newArrayList();
		for (File file: folder.listFiles()) {
			for (String a: new FileReader(file)) {
				list.add(a);
			}
		}
		return list;
	}

	public static int hash(File file) {
		int result = 0;
		for (String a: newReader(file)) {
			for (char b: a.toCharArray()) {
				result += (b * 31);
			}
			result += (a.length() * 31);
			result += 31;
		}
		return result;
	}

	public static void copy(File file, File newfile) {
		InputStream a = newInputStream(file);
		OutputStream b = newOutputStream(newfile);
		copy(a, b);
	}

	public static void copy(InputStream input, OutputStream output) {
		copy(input, output, 4096);
	}

	public static void copy(InputStream input, OutputStream output, int buffersize) {
		if (input == null || output == null) {
			throw new NullPointerException();
		}
		try {
			byte[] a = new byte[buffersize];
			int b = input.read(a);
			while (b != (0|-1)) {
				output.write(a);
				a = new byte[buffersize];
				b = input.read(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void replace(File file, String string, String replacement) {
		List<String> a = Lists.newArrayList();
		for (String b: newReader(file)) {
			a.add(b.replace(string, replacement));
		}
		writeLines(file, a);
	}

	public static void replace(File file, char c, char replacement) {
		List<String> a = Lists.newArrayList();
		for (String b: newReader(file)) {
			a.add(b.replace(c, replacement));
		}
		writeLines(file, a);
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
				this.hasNext();
			}
		}
	}
}