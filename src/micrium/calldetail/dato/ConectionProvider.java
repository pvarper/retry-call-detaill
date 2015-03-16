package micrium.calldetail.dato;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import micrium.calldetail.utils.Parameter;

import org.apache.log4j.Logger;

public class ConectionProvider {
	private static final Logger log = Logger.getLogger(ConectionProvider.class);

	/*public static String HOST_POSTGRES = Parameter.HOST_POSTGRES;
	public static String DB_POSTGRES = Parameter.DB_POSTGRES;
	public static String PORT_POSTGRES = Parameter.PORT_POSTGRES;
	public static String USER_POSTGRES = Parameter.USER_POSTGRES;
	public static String PASSWORD_POSTGRES = Parameter.PASSWORD_POSTGRES;
	public static String SCHEMA_POSTGRES = Parameter.SCHEMA_POSTGRES;*/

	private final static String DRIVER_CLASS_NAME_POSTGRES = "org.postgresql.Driver";

	/*public static synchronized Connection getConnection(byte sgbd) {
		return conexionPosgrest();
	}*/

	/*private static Connection conexionPosgrest() {
		log.info("Obteniendo la conexion a la base de datos " + DB_POSTGRES);
		Connection conneccion = null;

		try {
			Class.forName(DRIVER_CLASS_NAME_POSTGRES).newInstance();
			String ulrjdbc = "jdbc:postgresql://" + HOST_POSTGRES + ":" + PORT_POSTGRES + "/" + DB_POSTGRES + "?" + "user=" + USER_POSTGRES
					+ "&password=" + PASSWORD_POSTGRES;

			log.debug("url:" + ulrjdbc + ", schema:" + SCHEMA_POSTGRES);

			PreparedStatement statement = null;
			try {
				conneccion = DriverManager.getConnection(ulrjdbc);
				String sql = "set search_path=" + SCHEMA_POSTGRES;
				statement = conneccion.prepareStatement(sql);
				int result = statement.executeUpdate();

				log.info("El resultado es " + result + " al buscar el esquema " + SCHEMA_POSTGRES);

				log.info("Coonexion obtenido satisfactoriamente a la base de datos " + DB_POSTGRES);
			} catch (SQLException e) {
				log.error("Error al intentar obtener la conexion a la base de datos " + DB_POSTGRES, e);
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						log.error("Error al intentar cerrar el PreparedStatement", e);
					}
				}
			}

		} catch (InstantiationException e) {
			log.error("Error al intentar obtener la conexion a la base de datos " + DB_POSTGRES, e);
		} catch (IllegalAccessException e) {
			log.error("Error al intentar obtener la conexion a la base de datos " + DB_POSTGRES, e);
		} catch (ClassNotFoundException e) {
			log.error("Error al intentar obtener la conexion a la base de datos " + DB_POSTGRES, e);
		}

		return conneccion;
	}*/

	public static void main(String[] args) {
		// DOMConfigurator.configure("etc" + File.separator + "log4j.xml");
		// conexionPosgrest();
	}

}
