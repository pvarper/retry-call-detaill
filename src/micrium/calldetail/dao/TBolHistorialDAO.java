package micrium.calldetail.dao;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import micrium.calldetail.bussines.SysParameter;
import micrium.calldetail.dato.ConectionManager;
import micrium.calldetail.dato.Query;
import micrium.calldetail.model.TBol_Historial;
import micrium.calldetail.model.TBol_Linea;
import micrium.calldetail.result.Result;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

/**
 * @author pedro
 * 
 */
public class TBolHistorialDAO {

	private static final Logger log = Logger.getLogger(TBolHistorialDAO.class);
	

	public static Date getFecha(){
		Date fe=new Date(System.currentTimeMillis());
		//return "TO_DATE('"+fecha.get(Calendar.YEAR)+"/"+fecha.get(Calendar.MONTH)+"/"+fecha.get(Calendar.DATE)+"','yyyy-mm-dd')";
		return fe;
		
	}
	public static List<TBol_Historial> findEPVTBol(ConectionManager conectionManager) {
		log.debug("Obteniendo los hitoriales EN ESTADO EPV en la base de datos.");

		List<TBol_Historial> results = new ArrayList<TBol_Historial>();
		StringBuilder sql = new StringBuilder("SELECT * FROM TBOL_HISTORIAL");
		sql.append(" WHERE ESTADO=? AND FECHA_FIN is null AND NRO_INTENTOS is not null");

		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			//String gg=getFecha();
			query.setParameter(1,SysParameter.getProperty(SysParameter.ESTADO_EPV));
			//query.setParameter(3, gg);
			ResultSet rs = query.executeQuery();
			
			results = getResults(rs, conectionManager);
			
		} catch (Exception e) {
			log.error("Error al intentar obtener los hitoriales EN ESTADO EPV en la base de datos.", e);
		} finally {
			if (query != null) {
				query.close();
			}
		}

		return results;
	}
	public static List<TBol_Historial> findEPPHistorialTBol(String Cod_Ticket,ConectionManager conectionManager) throws SQLException {
		StringBuilder sql = new StringBuilder("SELECT * FROM TBOL_HISTORIAL");
		sql.append(" WHERE ESTADO=?  AND COD_TICKET=? AND FECHA_FIN is null");

		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			
			query.setParameter(1,SysParameter.getProperty(SysParameter.ESTADO_EPP) );
			query.setParameter(2,Cod_Ticket);
			ResultSet rs = query.executeQuery();
			return getResults(rs, conectionManager);
		} catch (SQLException e) {
			throw new SQLException("Error al intentar obtener historial con estado EPP", e);
		} finally {
			if (query != null) {
				query.close();
			}
		}

	}
	
	public static List<TBol_Historial> findEPRTBol(ConectionManager conectionManager) {
		log.debug("Obteniendo las programaciones activas en la base de datos.");

		List<TBol_Historial> results = new ArrayList<TBol_Historial>();
		StringBuilder sql = new StringBuilder("SELECT * FROM TBOL_HISTORIAL");
		sql.append(" WHERE ESTADO=? AND FECHA_FIN is null AND NRO_INTENTOS is not null");

		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			//String gg=getFecha();
			query.setParameter(1,SysParameter.getProperty(SysParameter.ESTADO_EPR));
			//query.setParameter(3, gg);
			ResultSet rs = query.executeQuery();
			
			results = getResults(rs, conectionManager);
			
		} catch (Exception e) {
			log.error("Error al intentar obtener LOS HISTORIALES EN ESTADO EPR en la base de datos.", e);
		} finally {
			if (query != null) {
				query.close();
			}
		}

		return results;
	}
	public static List<TBol_Historial> findEppTBol(ConectionManager conectionManager) throws SQLException {
		StringBuilder sql = new StringBuilder("SELECT * FROM TBOL_HISTORIAL");
		sql.append(" WHERE ESTADO=?  AND FECHA_FIN is null");

		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			//Object p=null;
			query.setParameter(1,SysParameter.getProperty(SysParameter.ESTADO_EPP) );
			//query.setParameter(2,p);
			ResultSet rs = query.executeQuery();
			return getResults(rs, conectionManager);
		} catch (SQLException e) {
			throw new SQLException("Error al intentar obtener historial con estado EPP", e);
		} finally {
			if (query != null) {
				query.close();
			}
		}

	}
	public synchronized static Result save2(TBol_Linea linea,String error, ConectionManager conectionManager) {
		log.debug("Registrando el historial de ticket " + linea.getCod_ticket()+" linea "+linea.getLinea()+" contrato "+linea.getContrato());
		Result result= new Result();
		String sql = "{call PGK_TBOL_DETALLETELEFONIA.SetInsertHistorial(?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement callableStatement = null;
		try {
			callableStatement =conectionManager.PrepareCall(sql);
			callableStatement.setString(1, linea.getCod_ticket());
			callableStatement.setString(2, linea.getEstado());
			callableStatement.setString(3, linea.getContrato());
			callableStatement.setString(4, linea.getLinea());
			callableStatement.setString(5,"");
			callableStatement.setString(6, "ERROR");
			callableStatement.setString(7, error);
			callableStatement.setString(8,SysParameter.getProperty(SysParameter.APP_USER));
			callableStatement.registerOutParameter(9,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(10,OracleTypes.VARCHAR);
			int afectados=callableStatement.executeUpdate();
			if (afectados > 0) {
				String rs = (String)callableStatement.getObject(9);	
				if(rs.equalsIgnoreCase("OK")){
					log.info("Se registro satisfactoriamente " + afectados + " registros de historial.");
					result.ok("Se registro satisfactoriamente " + afectados + " registros de historial.");
					return result;
				}
				result.error((String)callableStatement.getObject(10));
				return result;
			}
			result.error("No se registro el historial");
			return result;
		} catch (SQLException e) {
				log.info("Error al intentar guardar el historial con id " + linea.getContrato()+", " +e);
				result.error("Error al intentar guardar el historial con id " + linea.getContrato()+", " +e);
				return result;
		} finally {
			if (callableStatement != null) {			
				try {
					callableStatement.close();
					
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		}
	}
	public synchronized static Result save(TBol_Linea linea, ConectionManager conectionManager) {
		log.debug("Registrando el historial de ticket " + linea.getCod_ticket()+" linea "+linea.getLinea()+" contrato "+linea.getContrato());
		Result result= new Result();
		String sql = "{call PGK_TBOL_DETALLETELEFONIA.SetInsertHistorial(?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement callableStatement = null;
		try {
			callableStatement =conectionManager.PrepareCall(sql);
			callableStatement.setString(1, linea.getCod_ticket());
			callableStatement.setString(2, linea.getEstado());
			callableStatement.setString(3, linea.getContrato());
			callableStatement.setString(4, linea.getLinea());
			callableStatement.setString(5, "");
			callableStatement.setString(6, "");
			callableStatement.setString(7, "");
			callableStatement.setString(8,SysParameter.getProperty(SysParameter.APP_USER));
			callableStatement.registerOutParameter(9,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(10,OracleTypes.VARCHAR);
			int afectados=callableStatement.executeUpdate();
			if (afectados > 0) {
				String rs = (String)callableStatement.getObject(9);	
				if(rs.equalsIgnoreCase("OK")){
					log.info("Se registro satisfactoriamente " + afectados + " registros de historial.");
					result.ok("Se registro satisfactoriamente " + afectados + " registros de historial.");
					return result;
				}
				result.error((String)callableStatement.getObject(10));
				return result;
			}
			result.error("No se registro el historial");
			return result;
		} catch (SQLException e) {
				log.info("Error al intentar guardar el historial con id " + linea.getContrato()+", " +e);
				result.error("Error al intentar guardar el historial con id " + linea.getContrato()+", " +e);
				return result;
		} finally {
			if (callableStatement != null) {			
				try {
					callableStatement.close();
					
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		}
	}
	public synchronized static Result updateIntentos(TBol_Linea linea,String error, ConectionManager conectionManager) {
		log.debug("Actualizando intentos en el historial con id " + linea.getCod_ticket());
		Result result = new Result();
		List<String> lista= new ArrayList<String>();
		String sql = "{call PGK_TBOL_DETALLETELEFONIA.SetUpdateIntentos(?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement callableStatement = null;
		try {
			//query = conectionManager.createQuery(sql.toString());
			//query.setParameter(1, contrato);
			callableStatement =conectionManager.PrepareCall(sql);
			callableStatement.setString(1, linea.getCod_ticket());
			callableStatement.setString(2, linea.getContrato());
			callableStatement.setString(3, linea.getLinea());
			callableStatement.setString(4,SysParameter.getProperty(SysParameter.APP_USER));
			callableStatement.setString(5, error);
			callableStatement.setString(6, "ERROR");
			callableStatement.setString(7, linea.getEstado());
			callableStatement.registerOutParameter(8,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(9,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(10,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(11,OracleTypes.VARCHAR);
			int afectados=callableStatement.executeUpdate();	
			if (afectados > 0) {
				String estadoRespuesta=callableStatement.getString(10);
				String esCorreo=callableStatement.getString(8);
				String correo=callableStatement.getString(9);
				String errorRespuesta=callableStatement.getString(11);
				lista.add(estadoRespuesta);
				lista.add(esCorreo);
				lista.add(correo);
				lista.add(errorRespuesta);
				if (estadoRespuesta.equalsIgnoreCase("OK")){
					log.info("Se ejecuto el procedimiento almacenado de update intentos");
					result.ok("Se ejecuto el procedimiento almacenado de update intentos");
					result.setData(lista);
					return result;
				}
				result.error(errorRespuesta);
				return result;
			}
			result.error("No se actualizo intentos");
			return result;
		} catch (SQLException e) {
				result.error("Error al intentar actualizar los intentos en el historial con id " + linea.getContrato()+", "+ e);
				return result;
		} finally {
			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		}

	}
	public synchronized static Result updateEstado(TBol_Linea linea, ConectionManager conectionManager) {
		log.debug("Actualizando el estado en el historial con id " + linea.getCod_ticket());
		Result result= new Result();
		String sql = "{call PGK_TBOL_DETALLETELEFONIA.SetUpdateEstado(?,?,?,?,?,?,?)}";
		CallableStatement callableStatement = null;
		try {
			//query = conectionManager.createQuery(sql.toString());
			//query.setParameter(1, contrato);
			callableStatement =conectionManager.PrepareCall(sql);
			callableStatement.setString(1, linea.getCod_ticket());
			callableStatement.setString(2, linea.getContrato());
			callableStatement.setString(3, linea.getLinea());
			callableStatement.setString(4, SysParameter.getProperty(SysParameter.APP_USER));
			callableStatement.setString(5, linea.getEstado());
			callableStatement.registerOutParameter(6,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(7,OracleTypes.VARCHAR);
			int afectados=callableStatement.executeUpdate();	
			if (afectados > 0) {
				String estadoRespuesta=callableStatement.getString(6);
				String errorRespuesta=callableStatement.getString(7);
				if(estadoRespuesta.equalsIgnoreCase("OK")){
					log.info("Se ejecuto el procedimiento almacenado de update intentos");
					result.ok("Se ejecuto el procedimiento almacenado de update intentos");
					return result;
					
				}
				result.error(errorRespuesta);
				return result;
			}
			result.error("No se Actualizo el estado");
			return result;
		} catch (SQLException e) {
				result.error("Error al intentar actualizar el estado en el historial con id " + linea.getContrato()+", " +e);
				return result;

		} finally {
			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		}
	}
	public synchronized static Result updateIntentos(TBol_Historial historial,String error, ConectionManager conectionManager) {
		log.debug("Actualizando intentos en el historial con id " + historial.getCod_Ticket());
		Result result= new Result();
		List<String> lista = new ArrayList<String>();
		String sql = "{call PGK_TBOL_DETALLETELEFONIA.SetUpdateIntentos(?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement callableStatement = null;
		try {
			callableStatement =conectionManager.PrepareCall(sql);
			callableStatement.setString(1, historial.getCod_Ticket());
			callableStatement.setString(2, historial.getContrato());
			callableStatement.setString(3, historial.getLinea());
			callableStatement.setString(4, SysParameter.getProperty(SysParameter.APP_USER));
			callableStatement.setString(5, error);
			callableStatement.setString(6, "ERROR");
			callableStatement.setString(7, historial.getEstado());
			callableStatement.registerOutParameter(8,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(9,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(10,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(11,OracleTypes.VARCHAR);
			int afectados=callableStatement.executeUpdate();	
			if (afectados > 0) {
				String estadoRespuesta=callableStatement.getString(10);
				String esCorreo=callableStatement.getString(8);
				String correo=callableStatement.getString(9);
				String errorRespuesta=callableStatement.getString(11);
				if(estadoRespuesta.equalsIgnoreCase("OK")){
					lista.add(estadoRespuesta);
					lista.add(esCorreo);
					lista.add(correo);
					lista.add(errorRespuesta);
					result.ok("Se actualizo intentos");
					result.setData(lista);
					return result;
				}			
				log.info(errorRespuesta);
				result.error(errorRespuesta);
				return result;
			}	
			result.error("No se actualizo los intentos");
			return result;
		} catch (SQLException e) {
				result.error("Error al intentar actualizar los intentos en el historial con id " + historial.getContrato()+", "+ e);
				return result;
		} finally {
			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		}
	}
	
	public synchronized static Result update(TBol_Historial linea, ConectionManager conectionManager) {
		log.debug("Actualizando EL HISTORIAL con id " + linea.getCod_Ticket());
		Result result = new Result();
		String sql = "{call PGK_TBOL_DETALLETELEFONIA.SetUpdateHistorial(?,?,?,?,?,?,?)}";
		CallableStatement callableStatement = null;
		try {
			callableStatement =conectionManager.PrepareCall(sql);
			callableStatement.setString(1, linea.getCod_Ticket());
			callableStatement.setString(2, linea.getContrato());
			callableStatement.setString(3, linea.getLinea());
			callableStatement.setString(4,SysParameter.getProperty(SysParameter.APP_USER));
			callableStatement.setString(5, linea.getEstado());
			callableStatement.registerOutParameter(6,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(7,OracleTypes.VARCHAR);
			int afectados=callableStatement.executeUpdate();
			if (afectados > 0) {
				String estadoRespuesta=callableStatement.getString(6);
				String errorRespuesta=callableStatement.getString(7);
				if(estadoRespuesta.equalsIgnoreCase("OK")){
					log.info("Se actualizo satisfactoriamente " + afectados + " registros de historial.");
					result.ok("Se actualizo satisfactoriamente " + afectados + " registros de historial.");
					return result;
				}
				result.error(errorRespuesta);
				return result;
			}	
			result.error("No se actualizo el historial");
			return result;
		} catch (SQLException e) {
				result.error("Error al intentar obtener el historial con id " + linea.getContrato()+", " +e);
				return result;
		} finally {
			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
	}
	public synchronized static Result save(TBol_Historial linea, ConectionManager conectionManager) {
		log.debug("Registrando el historial de ticket " + linea.getCod_Ticket());
		
		Result result = new Result();
		String sql = "{call PGK_TBOL_DETALLETELEFONIA.SetInsertHistorial(?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement callableStatement = null;
		try {
			callableStatement =conectionManager.PrepareCall(sql);
			callableStatement.setString(1, linea.getCod_Ticket());
			callableStatement.setString(2, linea.getEstado());
			callableStatement.setString(3, linea.getContrato());
			callableStatement.setString(4, linea.getLinea());
			callableStatement.setString(5, linea.getHash());
			callableStatement.setString(6, "");
			callableStatement.setString(7, "");
			callableStatement.setString(8, SysParameter.getProperty(SysParameter.APP_USER));
			callableStatement.registerOutParameter(9,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(10,OracleTypes.VARCHAR);
			int afectados=callableStatement.executeUpdate();	
			if (afectados > 0) {
				String rs = (String)callableStatement.getObject(9);
				String errorRespuesta=(String)callableStatement.getObject(10);
				if(rs.equalsIgnoreCase("OK")){
					log.info("Se registro satisfactoriamente " + afectados + " registros de historial.");
					result.ok("Se registro satisfactoriamente " + afectados + " registros de historial.");
					return result;
				}
				log.info("No Se registro satisfactoriamente " + afectados + " registros de historial.");
				result.error(errorRespuesta);
				return result;
			}		
			result.error("No se inserto historial");
			return result;
		} catch (SQLException e) {
				result.error("Error al intentar guardar el historial con id " + linea.getContrato()+", "+ e);
				return result;
		} finally {
			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		}
	}
	public synchronized static Result update(TBol_Linea linea, ConectionManager conectionManager) {
		log.debug("Actualizando EL HISTORIAL con id " + linea.getCod_ticket());
		Result result = new Result();
		String sql = "{call PGK_TBOL_DETALLETELEFONIA.SetUpdateHistorial(?,?,?,?,?,?,?)}";
		CallableStatement callableStatement = null;
		try {
			callableStatement =conectionManager.PrepareCall(sql);
			callableStatement.setString(1, linea.getCod_ticket());
			callableStatement.setString(2, linea.getContrato());
			callableStatement.setString(3, linea.getLinea());
			callableStatement.setString(4,SysParameter.getProperty(SysParameter.APP_USER));
			callableStatement.setString(5, linea.getEstado());
			callableStatement.registerOutParameter(6,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(7,OracleTypes.VARCHAR);
			int afectados=callableStatement.executeUpdate();
			if (afectados > 0) {
				String estadoRespuesta=(String)callableStatement.getObject(6);
				String errorRespuesta=(String)callableStatement.getObject(7);
				if(estadoRespuesta.equalsIgnoreCase("OK")){
					log.info("Se actualizo satisfactoriamente " + afectados + " registros de historial.");
					result.ok("Se actualizo satisfactoriamente " + afectados + " registros de historial.");
					return result;
					
				}
				result.error(errorRespuesta);
				return result;
			}
			result.error("No se actualizo el historial");
			return result;
		} catch (SQLException e) {
				result.error("Error al intentar actualizar el historial con id " + linea.getContrato()+", "+ e);
				return result;
		} finally {
			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		}
	}
	
/*	public static Programacion find(Integer programacionId, ConectionManager conectionManager) throws SQLException {
		StringBuilder sql = new StringBuilder("SELECT * FROM programacion");
		sql.append(" WHERE programacion_id=?");
		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			query.setParameter(1, programacionId);
			ResultSet rs = query.executeQuery();
			List<Programacion> lst = getResults(rs, conectionManager);
			return !lst.isEmpty() ? lst.get(0) : null;
		} catch (SQLException e) {
			throw new SQLException("Error al intentar obtener la programacion con id " + programacionId, e);
		} finally {
			if (query != null) {
				query.close();
			}
		}
	}
*/
	/*public static long countActivosTBol(ConectionManager conectionManager) {
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
				try {
					result = (Long) obj[0];
				} catch (Exception e) {
					// TODO: handle exception
					System.out.print("error");
				}
				
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
/*	public static List<TBol_Programacion> findActivosTBol(ConectionManager conectionManager) {
		log.debug("Obteniendo las programaciones activas en la base de datos.");

		List<TBol_Programacion> results = new ArrayList<TBol_Programacion>();
		StringBuilder sql = new StringBuilder("SELECT * FROM TBOL_PROGRAMACION");
		sql.append(" WHERE ESTADO=? AND ESTADO_ACTUAL=?");

		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			//String gg=getFecha();
			query.setParameter(1,"A");
			query.setParameter(2,"P");
			//query.setParameter(3, gg);
			ResultSet rs = query.executeQuery();
			
			results = getResults(rs, conectionManager);
			
		} catch (Exception e) {
			log.error("Error al intentar obtener las programaciones activadas en la base de datos.", e);
		} finally {
			if (query != null) {
				query.close();
			}
		}

		return results;
	}*/
	/*
	public static List<Programacion> findActivos(ConectionManager conectionManager) {
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
	/*public static List<Programacion> findActivosByBloque(long ini, long bloque, ConectionManager conectionManager) {
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
	private static List<TBol_Historial> getResults(ResultSet rs, ConectionManager conectionManager) throws SQLException {
		List<TBol_Historial> results = new ArrayList<TBol_Historial>();
		while (rs.next()) {
			TBol_Historial dto = new TBol_Historial();
			dto.setCod_Ticket((String)rs.getObject("COD_TICKET"));
			dto.setEstado((String)rs.getObject("ESTADO"));
			dto.setContrato((String)rs.getObject("CONTRATO"));
			dto.setLinea((String)rs.getObject("LINEA"));
		
			results.add(dto);
		}
		return results;
	}



}
