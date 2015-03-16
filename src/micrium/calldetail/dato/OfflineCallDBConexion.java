package micrium.calldetail.dato;
import java.io.Serializable;
import java.sql.Connection;

import micrium.calldetail.test.Test;
import micrium.calldetail.utils.Parameter;
import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * 
 * @author marioae
 */
public class OfflineCallDBConexion implements Serializable{

	private static final long serialVersionUID = 1L;
	public static String prefijoLog = "[OfflineCallDBConexion]";
	private static final Logger log = Logger.getLogger(ConectionProvider.class);

	private int timeount = Parameter.CALL_OFFLINE_BD_TIMEOUT;

	public static DataSource datasource = setupDataSource();

	/**
	 * Retorna una conexion del pool de conexiones.
	 * 
	 * @return Conexion activa a la base de datos, retorna null en caso de no establecerce la conexion.
	 */
	public static synchronized Connection getConnection() {
		try {
			if (datasource == null) {
				setupDataSource();
			}
			Test.ses=Test.ses+1;
			return datasource.getConnection();
			
		} catch (Exception e) {
			log.error(prefijoLog + "Error al intentar obtener la conexion.", e);
			return null;
		}
	}

	public int getTimeount() {
		return timeount;
	}

	public void setTimeount(int timeount) {
		this.timeount = timeount;
	}

	private static DataSource setupDataSource() {
		PoolProperties p = new PoolProperties();
		
		p.setDriverClassName(Parameter.CALL_OFFLINE_BD_DRIVER);
		p.setUrl(Parameter.CALL_OFFLINE_BD_URL);
		p.setUsername(Parameter.USER_ORACLE);
		p.setPassword(Parameter.PASSWORD_ORACLE);

		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setTestOnReturn(false);
		p.setValidationQuery(Parameter.CALL_OFFLINE_BD_VALIDATION_QUERY);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMinEvictableIdleTimeMillis(Parameter.CALL_OFFLINE_BD_MIN_EVICTABLE_IDLE_TIMEMILLIS);
		p.setMaxActive(Parameter.CALL_OFFLINE_BD_MAX_ACTIVE);
		p.setMinIdle(Parameter.CALL_OFFLINE_BD_MIN_IDLE);
		p.setMaxIdle(Parameter.CALL_OFFLINE_BD_MAX_IDLE);
		p.setMaxWait(Parameter.CALL_OFFLINE_BD_MAX_WAIT);
		p.setRemoveAbandonedTimeout(600);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(false);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		datasource = new org.apache.tomcat.jdbc.pool.DataSource();
		datasource.setPoolProperties(p);
		return datasource;
	}
}
