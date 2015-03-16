package micrium.calldetail.test;

import java.io.File;


import micrium.calldetail.Retry;

import org.apache.log4j.xml.DOMConfigurator;

public class Test {

	//private static final Logger log = Logger.getLogger(Retry.class);
	public static int ses=0;
	public static void main(String[] args) {
		DOMConfigurator.configure("etc" + File.separator + "log4j.xml");
		Retry.retryTBol();
		
	}
	

}
