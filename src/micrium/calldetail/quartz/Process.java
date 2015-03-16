package micrium.calldetail.quartz;


import micrium.calldetail.Retry;

import org.apache.log4j.Logger;

public class Process {

	private static final Logger log = Logger.getLogger(Process.class);

	public void procesar() {
		try {
			Retry.retryTBol();
		} catch (Exception e) {
			log.error("[procesar] :Problema al intentar procesar.", e);
		}
	}

}
