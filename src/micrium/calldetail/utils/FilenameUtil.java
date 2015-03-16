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

import org.apache.commons.io.FilenameUtils;

/**
 * @author mario
 * 
 */
public class FilenameUtil extends FilenameUtils {

	public static void main(String[] args) {
		String path = "D:\\Dasarrollo\\Servidores\\EAP-6.0.0.GA\\jboss-eap-6.0\\standalone\\deployments\\call-detail-web.war\\reports\\documento.pdf";
		System.out.println(getExtension(path));

		System.out.println(getName(path));

		System.out.println(getBaseName(path));
	}
}
