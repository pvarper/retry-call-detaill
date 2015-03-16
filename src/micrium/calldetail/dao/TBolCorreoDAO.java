package micrium.calldetail.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import micrium.calldetail.dato.ConectionManager;
import micrium.calldetail.dato.Query;
import micrium.calldetail.model.TBol_Correo;

import org.apache.log4j.Logger;

/**
 * @author pedro
 * 
 */
public class TBolCorreoDAO {

	private static final Logger log = Logger.getLogger(TBolCorreoDAO.class);

	public synchronized static boolean save(TBol_Correo correo, ConectionManager conectionManager) {
		log.debug("Guardando el correo con Nro de Ticket: " + correo.getCod_Ticket());
		boolean result = Boolean.FALSE;

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO TBol_Correos(Cod_Programacion,Correo,Tipo_Correo,Estado,Usuario_Creacion,Usuario_Modificacion,Usuario_Eliminacion,Fecha_Creacion,Fecha_Modificacion,Fecha_Eliminacion");
		sb.append("VALUES(?,?,?,?,?,?,?,?,?,?)");
		try {
			Query query = conectionManager.createQuery(sb.toString(), Query.RETURN_GENERATED_KEYS);
			query.setParameter(1, correo.getCod_Ticket());
			query.setParameter(2, correo.getCorreo());
			query.setParameter(4, correo.getEstado());
			query.setParameter(5, correo.getUsuario_Creacion());
			query.setParameter(6, correo.getUsuario_Modificacion());
			query.setParameter(7, correo.getUsuario_Eliminacion());
			query.setParameter(8, correo.getFecha_Creacion());
			query.setParameter(9, correo.getFecha_Modificacion());
			query.setParameter(10, correo.getFecha_Eliminacion());
			int correoId = query.executeUpdate();

			if (correoId != -1) {
				log.info("El correo con Nro de Ticket: " + correo.getCod_Ticket());
				result = true;
				//correo.setCorreoId(correoId);
			}
		} catch (SQLException e) {
			log.error("Error al intentar guardar el correo con Nro de Ticket: " + correo.getCod_Ticket(), e);
		}
		return result;
	}

	public static List<TBol_Correo> findTBol(String correoId, ConectionManager conectionManager) {
		log.info("Obteniendo el correo de ticket " + correoId);
		StringBuilder sql = new StringBuilder("SELECT * FROM TBOL_CORREOS_PROGRAMACION");
		sql.append(" WHERE COD_TICKET=? AND ESTADO=?");
		List<TBol_Correo> lst;
		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			query.setParameter(1, correoId);
			query.setParameter(2, "A");
			ResultSet rs = query.executeQuery();
			lst = getResults(rs, conectionManager);
			return lst;
		} catch (SQLException e) {
			log.error("Error al intentar obtener  el correo de ticket " + correoId, e);
			lst=new ArrayList<TBol_Correo>();
			return lst;
		} finally {
			if (query != null) {
				query.close();
			}
		}
		
	}
	public static List<TBol_Correo> findPendientes(ConectionManager conectionManager) throws SQLException {
		StringBuilder sql = new StringBuilder("SELECT * FROM correo");
		sql.append(" WHERE pendiente=? and enviado=? and programado=?");

		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			query.setParameter(1, Boolean.TRUE);
			query.setParameter(2, Boolean.FALSE);
			query.setParameter(3, Boolean.FALSE);
			ResultSet rs = query.executeQuery();
			return getResults(rs, conectionManager);
		} catch (SQLException e) {
			throw new SQLException("Error al intentar obtener el correos pendientes de envio.", e);
		} finally {
			if (query != null) {
				query.close();
			}
		}

	}
	public synchronized static int update(TBol_Correo correo, ConectionManager conectionManager) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE TBol_Correos");
		sb.append(" SET Cod_Programacion=?,Correo=?,Tipo_Correo=?,Estado=?,Usuario_Creacion=?,Usuario_Modificacion=?,Usuario_Eliminacion=?,Fecha_Creacion=?,Fecha_Modificacion=?,Fecha_Eliminacion=?");
		sb.append(" WHERE correo_id=?");
		try {
			Query query = conectionManager.createQuery(sb.toString());
			query.setParameter(1, correo.getCod_Ticket());
			query.setParameter(2, correo.getCorreo());
			query.setParameter(4, correo.getEstado());
			query.setParameter(5, correo.getUsuario_Creacion());
			query.setParameter(6, correo.getUsuario_Modificacion());
			query.setParameter(7, correo.getUsuario_Eliminacion());
			query.setParameter(8, correo.getFecha_Creacion());
			query.setParameter(9, correo.getFecha_Modificacion());
			query.setParameter(10, correo.getFecha_Eliminacion());
			return query.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException("Error al intentar actualizar el correocon Nro de Ticket: " + correo.getCod_Ticket(), e);
		}
	}

	public static TBol_Correo find(String correoId, ConectionManager conectionManager) {
		log.info("Obteniendo el correo con id " + correoId);

		StringBuilder sql = new StringBuilder("SELECT * FROM TBOL_CORREOS_PROGRAMACION");
		sql.append(" WHERE COD_TICKET=? AND ESTADO=?");

		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			query.setParameter(1, correoId);
			query.setParameter(2, "A");
			ResultSet rs = query.executeQuery();
			List<TBol_Correo> lst = getResults(rs, conectionManager);
			return !lst.isEmpty() ? lst.get(0) : null;
		} catch (SQLException e) {
			log.error("Error al intentar obtener  el correo con id " + correoId, e);
		} finally {
			if (query != null) {
				query.close();
			}
		}
		return null;
	}



	private static List<TBol_Correo> getResults(ResultSet rs, ConectionManager conectionManager) throws SQLException {
		List<TBol_Correo> results = new ArrayList<TBol_Correo>();
		while (rs.next()) {
			TBol_Correo dto = new TBol_Correo();
			dto.setCod_Ticket((String) rs.getObject("COD_TICKET"));
			dto.setCorreo((String) rs.getObject("CORREO"));
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
