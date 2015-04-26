package pt.uptodate.util.io;

import java.io.File;
import java.io.FileFilter;

public class FileFilters {

	public static final FileExtensionFilter JAR_FILES = new FileExtensionFilter("jar");
	public static final FileExtensionFilter ZIP_FILES = new FileExtensionFilter("zip");
	public static final FileExtensionFilter TEXT_FILES = new FileExtensionFilter("txt", "cfg", "gradle", "md", "json", "markdown", "log", "info", "asc", "text", "bat", "properties");
	public static final DirectoryFilter FOLDERS = new DirectoryFilter();

	public static class FileExtensionFilter implements FileFilter {

		private final String[] extensions;

		public FileExtensionFilter(String... extensions) {
			this.extensions = extensions;
		}

		@Override
		public boolean accept(File file) {
			for (String a: this.extensions) {
				if (file.getName().endsWith("." + a)) {
					return true;
				}
			}
			return false;
		}
	}

	private static class DirectoryFilter implements FileFilter {

		@Override
		public boolean accept(File file) {
			return file.isDirectory();
		}
	}
}