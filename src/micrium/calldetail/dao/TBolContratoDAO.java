package micrium.calldetail.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Calendar;
import micrium.calldetail.dato.ConectionManager;
import micrium.calldetail.dato.Query;
import micrium.calldetail.model.TBol_Contrato;
import org.apache.log4j.Logger;

/**
 * @author pedro
 * 
 */
public class TBolContratoDAO {

	private static final Logger log = Logger.getLogger(TBolContratoDAO.class);
	

	public static String getFecha(){
		Calendar fecha = new GregorianCalendar();
		
		return "'"+fecha.get(Calendar.MONTH)+"/"+fecha.get(Calendar.DATE)+"/"+fecha.get(Calendar.YEAR)+"'";
	}

	public static List<TBol_Contrato> findContratosTBol(String programacionId, ConectionManager conectionManager) throws SQLException {
		log.info("Obteniendo la cantidad de contratos de la programacion: "+programacionId);
		StringBuilder sql = new StringBuilder("SELECT * FROM TBOL_CONTRATOS_PROGRAMACION");
		sql.append(" WHERE COD_TICKET=? AND ESTADO=?");
		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			query.setParameter(1, programacionId);
			query.setParameter(2,"A");
			ResultSet rs = query.executeQuery();
			List<TBol_Contrato> lst = getResults(rs, conectionManager);
			return  lst;
		} catch (SQLException e) {
			throw new SQLException("Error al intentar obtener los contratos de la programacion con id " + programacionId, e);
		} finally {
			if (query != null) {
				query.close();
			}
		}
	
	}
	public static long countActivos(ConectionManager conectionManager) {
		log.debug("Obteniendo la cantidad de programaciones activas.");
		
		
		long result = 0;
		StringBuilder sql = new StringBuilder("SELECT count(0) FROM TBOL_PROGRAMACION");
		sql.append(" WHERE Estado=? AND Estado_Actual=?");

		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			//query.setParameter(1, getFecha());	
			query.setParameter(1, "A");
			query.setParameter(2, "P");
			List<Object[]> lst = query.getResultList();
			if (!lst.isEmpty()) {
				Object[] obj = (Object[]) lst.get(0);
				result = (Long) obj[0];
				log.info("Existen " + result + " programaciones activas.");
			}
		} catch (SQLException e) {
			log.error("Error al intentar obtener cantidad de programaciones activadas.", e);
		} finally {
			if (query != null) {
				query.close();
			}
		}

		return result;
	}

	/*public static List<Programacion> findActivos(ConectionManager conectionManager) {
		log.debug("Obteniendo las programaciones activas en la base de datos.");

		List<Programacion> results = new ArrayList<Programacion>();
		StringBuilder sql = new StringBuilder("SELECT * FROM programacion");
		sql.append(" WHERE estado=? AND activo=?");

		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			query.setParameter(1, Boolean.TRUE);
			query.setParameter(2, Boolean.TRUE);
			ResultSet rs = query.executeQuery();
			results = getResults(rs, conectionManager);
		} catch (SQLException e) {
			log.error("Error al intentar obtener las programaciones activadas en la base de datos.", e);
		} finally {
			if (query != null) {
				query.close();
			}
		}

		return results;
	}
*/
/*	public static List<Programacion> findActivosByBloque(long ini, long bloque, ConectionManager conectionManager) {
		log.debug("Obteniendo las programaciones activas en la base de datos.");

		List<Programacion> results = new ArrayList<Programacion>();
		StringBuilder sql = new StringBuilder("SELECT * FROM programacion");
		sql.append(" WHERE estado=? AND activo=?");
		sql.append(" ORDER BY programacion_id LIMIT " + bloque + " OFFSET " + ini);

		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			query.setParameter(1, Boolean.TRUE);
			query.setParameter(2, Boolean.TRUE);
			ResultSet rs = query.executeQuery();
			results = getResults(rs, conectionManager);
		} catch (SQLException e) {
			log.error("Error al intentar obtener las programaciones activadas en la base de datos.", e);
		} finally {
			if (query != null) {
				query.close();
			}
		}

		return results;
	}
*/
	private static List<TBol_Contrato> getResults(ResultSet rs, ConectionManager conectionManager) throws SQLException {
		List<TBol_Contrato> results = new ArrayList<TBol_Contrato>();
		while (rs.next()) {
			TBol_Contrato dto = new TBol_Contrato();
			dto.setContrato((String)rs.getObject("CONTRATO"));
			dto.setCod_Ticket((String) rs.getObject("COD_TICKET"));
			dto.setEstado((String) rs.getObject("ESTADO"));
			dto.setUsuario_Creacion((String) rs.getObject("USUARIO_CREACION"));
			dto.setUsuario_Modificacion((String) rs.getObject("USUARIO_MODIFICACION"));
			dto.setUsuario_Eliminacion((String) rs.getObject("USUARIO_ELIMINACION"));
			dto.setFecha_Creacion((Date) rs.getObject("FECHA_CREACION"));
			dto.setFecha_Modificacion((Date) rs.getObject("FECHA_MODIFICACION"));
			dto.setFecha_Eliminacion((Date) rs.getObject("FECHA_ELIMINACION"));
			results.add(dto);
		}
		return results;
	}


}
