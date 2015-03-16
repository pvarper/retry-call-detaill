package micrium.calldetail.dato;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class ConectionManager {

	private static final Logger log = Logger.getLogger(ConectionManager.class);

	private Connection conexion = null;
	private byte sgbd;

	public static ConectionManager getInstance() {
		return new ConectionManager();
	}

	public void setTipoSGBD(byte sgbd) {
		this.sgbd = sgbd;
	}

	public boolean open() {
		//conexion = ConectionProvider.getConnection(sgbd); para postgre
		
			conexion = OfflineCallDBConexion.getConnection();//para oracle
		return conexion != null;
	}

	public boolean openTransaction() {
		boolean result = false;
		if (!open()) {
			log.info("No se pudo abrir la conexion a la base de datos.");
			
			return result;
		}
		try {
			conexion.setAutoCommit(false);
			result = true;
		} catch (SQLException e) {
			log.error("error al desactivar el autocommit de la conexxion.", e);
		}
		return result;
	}

	public void closeTransaction() {
		if (conexion != null) {
			try {
				conexion.commit();
				conexion.setAutoCommit(true);
			} catch (Exception e) {
				log.error("Error al intentar cerrar la conexion a la base de datos.", e);
			}
		}
	}

	public void rollBack() {
		if (conexion != null) {
			try {
				conexion.rollback();
				conexion.setAutoCommit(true);
			} catch (SQLException e) {
				log.error("Error al intentar realizar el rollBack.", e);
			}
		}
	}

	public void close() {
		if (conexion != null) {
			try {
				conexion.close();
			} catch (SQLException e) {
				log.error("Error al intentar cerrar la conexion a la base de datos.", e);
			}
		}
	}

	public Query createQuery(String sql) throws SQLException {
		return new Query(conexion, sql);
	}
	public CallableStatement PrepareCall(String sql) throws SQLException {
		return conexion.prepareCall(sql);
		//return new Query(conexion, sql,null);
	}
	public Query createQuery(String sql, int generateKey) throws SQLException {
		return new Query(conexion, sql, generateKey);
	}

	public byte getSgbd() {
		return sgbd;
	}

	public void setSgbd(byte sgbd) {
		this.sgbd = sgbd;
	}
	
}
