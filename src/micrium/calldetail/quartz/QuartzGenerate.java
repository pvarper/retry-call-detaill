package micrium.calldetail.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;

public class QuartzGenerate implements Job {

	private static final Logger log = Logger.getLogger(QuartzGenerate.class);

	public void execute(org.quartz.JobExecutionContext context) throws org.quartz.JobExecutionException {
		log.info("[execute]: Inicio de tarea programada Quartz");

		Process t = new Process();
		t.procesar();

		log.info("[execute]: Fin de tarea programada Quartz");
	}
}
