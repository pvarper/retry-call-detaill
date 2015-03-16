package micrium.calldetail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import micrium.calldetail.dato.ConectionManager;
import micrium.calldetail.dato.Query;
import micrium.calldetail.utils.StringUtil;

import org.apache.log4j.Logger;

/**
 * @author pedro
 * 
 */
public class TBolParametroDAO {
	private static final Logger log = Logger.getLogger(TBolParametroDAO.class);

	public static Properties findAll(ConectionManager conectionManager) {
		log.debug("Obteniendo los parametros del sistema en la base de datos.");
		Properties properties = new Properties();

		StringBuilder sql = new StringBuilder("select DOMINIO, CODIGO, DESCRIPCION, VALOR_CARACTER from TBOL_DOMINIOS");
		sql.append(" where DOMINIO=? or DOMINIO= ? or DOMINIO=? or DOMINIO=? or DOMINIO=?");
		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			query.setParameter(1, "DETALLE LLAMADAS");
			query.setParameter(2, "ENVIO CORREO");
			query.setParameter(3, "ESTADO DETALLE");
			query.setParameter(4, "TIPO SOLICITUD JAVA");
			query.setParameter(5, "TYPE TICKET");
			ResultSet rs = query.executeQuery();
			properties = getResults(rs);
		} catch (SQLException e) {
			log.error("Error al intentar obtener los parametros del sistema en la base de datos.", e);
		} finally {
			if (query != null) {
				query.close();
			}
		}
		return properties;
	}
	
	private static Properties getResults(ResultSet rs) throws SQLException {
		Properties results = new Properties();
		while (rs.next()) {
			results.put(String.valueOf(rs.getString("DOMINIO"))+String.valueOf(rs.getString("CODIGO")), StringUtil.defaultString(rs.getString("VALOR_CARACTER")).trim());
		}
		return results;
	}

}
