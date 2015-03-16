package micrium.calldetail.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import micrium.calldetail.dato.ConectionManager;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.quartz.jobs.ee.jms.JmsJobException;

public class Parameter {

	protected final static String configFile = "etc" + File.separator + "retry-call-detail.properties";
	protected static final Properties prop = new Properties();

	private static final Logger log = Logger.getLogger(ConectionManager.class);

	static {
		init();
	}

	// Parametros de Base de Datos de Postgres.
	/*public static String HOST_POSTGRES = prop.getProperty("bd.postgres.host");
	public static String DB_POSTGRES = prop.getProperty("bd.postgres.database");
	public static String PORT_POSTGRES = prop.getProperty("bd.postgres.port");
	public static String USER_POSTGRES = prop.getProperty("bd.postgres.user");
	public static String PASSWORD_POSTGRES = prop.getProperty("bd.postgres.password");
	public static String SCHEMA_POSTGRES = prop.getProperty("bd.postgres.schema");
	//public static String SYSTEM_TIMEZONE = prop.getProperty("system.timezone");
	//public static String PROGRAMACION_TAREA = prop.getProperty("quartz.programacion.tarea");
	*/
	//parametros de Base de Datos de Oracle
	//public static String HOST_ORACLE = prop.getProperty("bd.postgres.host");
	public static int CALL_OFFLINE_BD_TIMEOUT= Integer.parseInt(prop.getProperty("bd.oracle.call"));
	public static String CALL_OFFLINE_BD_DRIVER= prop.getProperty("bd.oracle.driver");
	public static String CALL_OFFLINE_BD_URL=prop.getProperty("bd.oracle.url");
	public static String DB_ORACLE = prop.getProperty("bd.oracle.database");
	//public static String PORT_ORACLE = prop.getProperty("bd.oracle.port");
	public static String USER_ORACLE = prop.getProperty("bd.oracle.user");
	public static String PASSWORD_ORACLE = prop.getProperty("bd.oracle.password");
	public static String CALL_OFFLINE_BD_VALIDATION_QUERY = prop.getProperty("bd.oracle.validation_query");
	public static int CALL_OFFLINE_BD_MIN_EVICTABLE_IDLE_TIMEMILLIS = Integer.parseInt(prop.getProperty("bd.oracle.min_evictable_Idle_timemillis"));
	public static int CALL_OFFLINE_BD_MAX_ACTIVE = Integer.parseInt(prop.getProperty("bd.oracle.max_active"));
	public static int CALL_OFFLINE_BD_MIN_IDLE =Integer.parseInt(prop.getProperty("bd.oracle.min_Idle"));
	public static int CALL_OFFLINE_BD_MAX_IDLE = Integer.parseInt(prop.getProperty("bd.oracle.max_Idle"));
	public static int CALL_OFFLINE_BD_MAX_WAIT =Integer.parseInt( prop.getProperty("bd.oracle.max_wait"));
	//public static String SCHEMA_ORACLE = prop.getProperty("bd.oracle.schema");
	public static String SYSTEM_TIMEZONE = prop.getProperty("system.timezone");
	//public static String PROGRAMACION_TAREA = prop.getProperty("quartz.programacion.tarea");

	/** flujo de entrada relacionado con el archivo de configuracion */
	private static InputStream configFileStream;

	public static void init() {
		try { // Ubicar el archivo en el classPath actual
			log.info("se va cargar los parametros de la BD");
			configFileStream = new FileInputStream(configFile);
			prop.load(configFileStream);
		
		}catch(FileNotFoundException e){
			log.error("Error al obtener los parametros de la aplicacion."+e);
		} catch (Exception e) {
			log.error("Error al obtener los parametros de la aplicacion."+e);}
		finally {
			IOUtils.closeQuietly(configFileStream);
		}
	}
}
