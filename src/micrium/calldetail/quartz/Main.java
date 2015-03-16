package micrium.calldetail.quartz;

import java.io.File;
import micrium.calldetail.bussines.SysParameter;
import micrium.calldetail.dato.ConectionManager;
import micrium.calldetail.result.Code;
import micrium.calldetail.result.Result;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class Main {

	private static final Logger log = Logger.getLogger(Main.class);

	public static void main(String[] args) {

		DOMConfigurator.configure("etc" + File.separator + "log4j.xml");
		Scheduler scheduler;
		ConectionManager conectionManager=null;
		try {
			log.info("Inicializando proceso quartz.");
			JobDetail job = new JobDetail("quartz", Scheduler.DEFAULT_GROUP, QuartzGenerate.class);
			conectionManager = ConectionManager.getInstance();
			
			if (!conectionManager.open()) {
				log.info("No se ha podido establecer conexion con la base de datos");
				return;
			}
			Result result = SysParameter.load(conectionManager);
			if(!result.getCode().equalsIgnoreCase(Code.OK)){
				log.info("error al obtener los parametros de la base de datos");
				return;
			}
			// segundo, minuto, hora, dia, mes, anio
			// 0 0 0 1 * ?
			// 0 0 0 * * ?
			// 0/10 * * * * ? cada 10 segundos
			// String planificacion = "0 0 0 * * ?"; // cada dia a las 00:00 hrs
			String programacionTarea = SysParameter.getProperty(SysParameter.PROGRAMACION_TAREA);
			log.info(programacionTarea);
			CronTrigger trigger = new CronTrigger("quartz", Scheduler.DEFAULT_GROUP, programacionTarea);
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.scheduleJob(job, trigger);
			scheduler.start();

		} catch (Exception e) {
			log.error("Problema al lanzar el quartz", e);
		}finally{
			if(conectionManager!=null){
				conectionManager.close();
			}
			
		}
	}
}
