package micrium.calldetail.dao;

import java.sql.CallableStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Calendar;
import micrium.calldetail.dato.ConectionManager;
import micrium.calldetail.model.TBol_Linea;
import micrium.calldetail.result.Result;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;

/**
 * @author pedro
 * 
 */
public class TBolLineaDAO {

	private static final Logger log = Logger.getLogger(TBolLineaDAO.class);
	

	public static String getFecha(){
		Calendar fecha = new GregorianCalendar();
		
		return "'"+fecha.get(Calendar.MONTH)+"/"+fecha.get(Calendar.DATE)+"/"+fecha.get(Calendar.YEAR)+"'";
	}
	public static Result findLineasContratos(String contrato,String ticket, ConectionManager conectionManager) {
		log.info("Obteniendo la cantidad de lineas del contrato: "+contrato);
		String sql = "{call PGK_TBOL_DETALLETELEFONIA.ObtieneLineasContrato(?,?,?,?,?)}";
		Boolean afectado=Boolean.TRUE;
		CallableStatement callableStatement = null;
		Result result= new Result();
		try {
			//query = conectionManager.createQuery(sql.toString());
			//query.setParameter(1, contrato);
			callableStatement = conectionManager.PrepareCall(sql);
			callableStatement.setString(1, contrato);
			callableStatement.setString(2, ticket);
			callableStatement.registerOutParameter(3, OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(4,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			afectado=callableStatement.execute();
			if (!afectado){
				String respuesta=callableStatement.getString(3);
				if(respuesta.equals("OK")){
					ResultSet rs = (ResultSet)callableStatement.getObject(5);
					List<TBol_Linea> lst = getResults(rs, conectionManager);
					result.ok("Se obtuvo las lineas del contrato: "+contrato+" ticket: "+ticket);
					result.setData(lst);
					return  result;
				}
				result.error(callableStatement.getString(4));
				return result;
			}
			result.error("No Se obtuvo las lineas del contrato: "+contrato+" ticket: "+ticket);
			return result;	
			
		} catch (SQLException e) {
			result.error("Error al intentar obtener las lineas de contrato: " + contrato+", "+e);
			return result;
			
		} finally {
			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/*public static long countActivos(ConectionManager conectionManager) {
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
	}*/

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
	private static List<TBol_Linea> getResults(ResultSet rs, ConectionManager conectionManager) throws SQLException {
		List<TBol_Linea> results = new ArrayList<TBol_Linea>();
		while (rs.next()) {
			TBol_Linea dto = new TBol_Linea();
			dto.setContrato((String)rs.getObject("contrato"));
			dto.setLinea((String)rs.getObject("linea"));
			
			
			results.add(dto);
		}
		return results;
	}



}
