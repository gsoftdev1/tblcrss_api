package com.tablecross.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class IoUtils {
	public static DecimalFormat df = new DecimalFormat("###,###");

	public static String formatCurrency(long number) {
		return df.format(number);
	}

	public static void closeFile(FileInputStream fis) {
		if (fis != null)
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public static boolean copy(String fromFileName, String toFileName,
			boolean overWrite) throws IOException {
		boolean copyOK = false;
		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);

		if (!fromFile.exists())
			throw new IOException("FileCopy: " + "no such source file: "
					+ fromFileName);
		if (!fromFile.isFile())
			throw new IOException("FileCopy: " + "can't copy directory: "
					+ fromFileName);
		if (!fromFile.canRead())
			throw new IOException("FileCopy: " + "source file is unreadable: "
					+ fromFileName);

		if (toFile.isDirectory())
			toFile = new File(toFile, fromFile.getName());

		if (toFile.exists()) {
			if (!toFile.canWrite())
				throw new IOException("FileCopy: "
						+ "destination file is unwriteable: " + toFileName);
			if (!overWrite)
				throw new IOException("FileCopy: "
						+ "existing file was not overwritten.");
		} else {
			String parent = toFile.getParent();
			if (parent == null)
				parent = System.getProperty("user.dir");
			File dir = new File(parent);
			if (!dir.exists())
				throw new IOException("FileCopy: "
						+ "destination directory doesn't exist: " + parent);
			if (dir.isFile())
				throw new IOException("FileCopy: "
						+ "destination is not a directory: " + parent);
			if (!dir.canWrite())
				throw new IOException("FileCopy: "
						+ "destination directory is unwriteable: " + parent);
		}

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1)
				to.write(buffer, 0, bytesRead); // write
			copyOK = true;
		} finally {
			if (from != null) {
				try {
					from.close();
				} catch (IOException e) {
					copyOK = false;
					;
				}
			}
			if (to != null) {
				try {
					to.close();
				} catch (IOException e) {
					copyOK = false;
					;
				}
			}

		}
		return copyOK;
	}
}
