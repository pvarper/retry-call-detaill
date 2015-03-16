package micrium.calldetail.utils;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class DigestUtil extends DigestUtils {

	private static final Logger log = Logger.getLogger(DigestUtil.class);

	public static String generarHash(String pathname) {
		String result = StringUtil.EMPTY;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(pathname));
			result = DigestUtil.md5Hex(fis);
		} catch (Exception e) {
			log.error("Error al generar el hash del archivo " + pathname, e);
		} finally {
			IOUtils.closeQuietly(fis);
		}
		return result;
	}

	/**
	 * @soaparam args
	 */
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 149eac80b09a62fd2a8b3d0390a012ae
		// 149eac80b09a62fd2a8b3d0390a012ae
	}*/
}
