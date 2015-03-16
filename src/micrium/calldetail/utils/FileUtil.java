/*-*
 *
 * FILENAME  :
 *    FileUtils.java
 *
 * STATUS  :	
 *    2013 23:17:02  
 *
 *    
 * Copyright (c) 2012 SystemSoft Ltda. All rights reserved.
 *
 ****************************************************************/

package micrium.calldetail.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author mario
 * 
 */
public class FileUtil extends FileUtils {

	private static final Log log = LogFactory.getLog(FileUtil.class);

	@SuppressWarnings("unchecked")
	public static List<String> loadLinesFile(InputStream inputStream) {
		List<String> result = null;
		try {
			result = IOUtils.readLines(inputStream);
		} catch (Exception e) {
			throw new RuntimeException("Error occurred reading the InputStream " + e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return result;
	}

	public static void saveLinesFile(List<String> values, File file) {
		OutputStream out = null;
		try {
			forceDeleteOnExit(file);
			out = new FileOutputStream(file);
			IOUtils.writeLines(values, null, out);
		} catch (Exception e) {
			throw new RuntimeException("Error occurred reading the InputStream " + e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	@SuppressWarnings("resource")
	public static InputStream getInputStreamResource(String fileName) {

		InputStream inputStream = null;

		try {
			File file = new File(fileName);
			if (file.exists()) {
				inputStream = new FileInputStream(file);
			} else {
				Thread thread = Thread.currentThread();
				if (thread != null) {
					ClassLoader classLoader = thread.getContextClassLoader();
					if (classLoader != null) {
						inputStream = classLoader.getResourceAsStream(fileName);
					}
				}
			}
		} catch (FileNotFoundException e) {
			log.error("Error occurred reading the File [" + fileName + "], " + e.getMessage(), e);
		}
		return inputStream;
	}

	public static boolean delete(String pathname) {
		File file = new File(pathname);
		return file.delete();
	}

	public static boolean delete(List<String> lstFilesPath) {
		for (String pathname : lstFilesPath) {
			if (!delete(pathname)) {
				return false;
			}
		}
		return true;
	}

	public static long length(String pathname) {
		File file = new File(pathname);
		return file.length();
	}

	public static boolean exists(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static String getRealPathProject() {
		return System.getProperty("user.dir");
	}

	public static String getRealPath(String filename) {
		File file = new File(filename);
		return file.getAbsolutePath();
	}

	public static void main(String[] args) {
		// if
		// (fileExists("D:\\Dasarrollo\\Servidores\\EAP-6.0.0.GA\\jboss-eap-6.0\\standalone\\deployments\\call-detail-web.war\\reports\\documento.pdf"))
		// {
		// System.out.println("existe");
		// } else {
		// System.out.println("no existe");
		// }

		System.out.println(getRealPath("etc"));

		System.out.println(System.getProperty("user.dir"));
	}
}
