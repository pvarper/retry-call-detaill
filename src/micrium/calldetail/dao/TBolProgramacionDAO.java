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
import micrium.calldetail.model.TBol_Programacion;
import micrium.calldetail.result.Result;
import micrium.calldetail.test.Test;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

/**
 * @author pedro
 * 
 */
public class TBolProgramacionDAO {

	private static final Logger log = Logger.getLogger(TBolProgramacionDAO.class);
	

	
	public static Date getFecha(){
		Date fe=new Date(System.currentTimeMillis());
		//return "TO_DATE('"+fecha.get(Calendar.YEAR)+"/"+fecha.get(Calendar.MONTH)+"/"+fecha.get(Calendar.DATE)+"','yyyy-mm-dd')";
		return fe;
		
	}
	
	/*public synchronized static boolean update(TBol_Linea linea, ConectionManager conectionManager) {
		log.debug("Actualizando la programacion con id " + linea.getCod_ticket());
		boolean result = Boolean.FALSE;

		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE TBOL_PROGRAMACION");
		sb.append(" SET ESTADO_ACTUAL=?");
		sb.append(" WHERE COD_TICKET=?");
		try {
			Query query = conectionManager.createQuery(sb.toString());
			query.setParameter(1, linea.getEstado());
			query.setParameter(2, linea.getCod_ticket());		
			int afectados = query.executeUpdate();
			if (afectados > 0) {
				log.info("Se actualizo satisfactoriamente " + afectados + " registros de programaciones.");
				result = true;
			}
		} catch (SQLException e) {
			log.error("Error al intentar actualizar la programacion con id " + linea.getCod_ticket(), e);
		}
		return result;
	}*/
public static List<TBol_Programacion> findEPPProgramacionTBol(ConectionManager conectionManager) throws SQLException {
		
		List<TBol_Programacion> results = new ArrayList<TBol_Programacion>();
		StringBuilder sql = new StringBuilder("SELECT * FROM TBOL_PROGRAMACION");
		sql.append(" WHERE ESTADO=? AND ESTADO_ACTUAL=? AND FECHA_EJECUCION=? ORDER BY TICKET_PRIORIDAD");
		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			
			query.setParameter(1,"A");
			query.setParameter(2,SysParameter.getProperty(SysParameter.ESTADO_EPP));
			query.setParameter(3, getFecha());
			ResultSet rs = query.executeQuery();
			
			results = getResults(rs, conectionManager);
			
		} catch (Exception e) {
			log.error("Error al intentar obtener el programaciones con estado EPP", e);
		} finally {
			if (query != null) {
				query.close();
			}
		}

		return results;

	}
	public static TBol_Programacion findTBol(String cod_ticket, ConectionManager conectionManager) throws SQLException {
		StringBuilder sql = new StringBuilder("SELECT * FROM TBOL_PROGRAMACION");
		sql.append(" WHERE COD_TICKET=?");
		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			query.setParameter(1, cod_ticket);
			ResultSet rs = query.executeQuery();
			List<TBol_Programacion> lst = getResults(rs, conectionManager);
			return !lst.isEmpty() ? lst.get(0) : null;
		} catch (SQLException e) {
			throw new SQLException("Error al intentar obtener la programacion con id " + cod_ticket, e);
		} finally {
			if (query != null) {
				query.close();
			}
		}
	}

	public static long countActivosTBol(ConectionManager conectionManager) {
		log.debug("Obteniendo la cantidad de programaciones activas.");
		long result = 0;
		StringBuilder sql = new StringBuilder("SELECT count(0) FROM TBOL_PROGRAMACION");
		sql.append(" WHERE Estado=? AND Estado_Actual=? AND FECHA_EJECUCION=?");
		log.info("sesiones abiertas: "+Test.ses);
		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			//	
			query.setParameter(1, "A");
			query.setParameter(2, "P");
			query.setParameter(3, getFecha());
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
	}
	public static List<TBol_Programacion> findActivosTBol(ConectionManager conectionManager) {
		log.debug("Obteniendo las programaciones activas en la base de datos.");

		List<TBol_Programacion> results = new ArrayList<TBol_Programacion>();
		StringBuilder sql = new StringBuilder("SELECT * FROM TBOL_PROGRAMACION");
		sql.append(" WHERE ESTADO=? AND ESTADO_ACTUAL=? AND FECHA_EJECUCION=? ORDER BY TICKET_PRIORIDAD");

		Query query = null;
		try {
			query = conectionManager.createQuery(sql.toString());
			
			query.setParameter(1,"A");
			query.setParameter(2,SysParameter.getProperty(SysParameter.ESTADO_PEN));
			query.setParameter(3, getFecha());
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
	}
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
	private static List<TBol_Programacion> getResults(ResultSet rs, ConectionManager conectionManager) throws SQLException {
		List<TBol_Programacion> results = new ArrayList<TBol_Programacion>();
		while (rs.next()) {
			TBol_Programacion dto = new TBol_Programacion();
			dto.setCod_Ticket((String)rs.getObject("COD_TICKET"));
			dto.setId_Client((String)rs.getObject("IDCLIENT"));
			dto.setOperation((String)rs.getObject("OPERATION"));
			dto.setTicket_Prioridad((String)rs.getObject("TICKET_PRIORIDAD"));
			dto.setTicket_Descripcion((String)rs.getObject("TICKET_DESCRIPCION"));
			dto.setEmail_Cliente((String)rs.getObject("EMAIL_CLIENTE"));
			dto.setTelefono_Cliente((String)rs.getObject("TELEFONO_CLIENTE"));
			dto.setPeriodicidad((String)rs.getObject("PERIODICIDAD"));
			dto.setTipo_Solicitud((String)rs.getObject("TIPO_SOLICITUD"));
			dto.setFecha_Inicial((Date)rs.getObject("FECHA_INICIAL"));
			dto.setFecha_Final((Date)rs.getObject("FECHA_FINAL"));
			dto.setFecha_Registro((Date)rs.getObject("FECHA_REGISTRO"));
			dto.setFecha_Ejecucion((Date)rs.getObject("FECHA_EJECUCION"));
			dto.setFecha_Fin((Date)rs.getObject("FECHA_FIN"));
			dto.setEstado((String)rs.getObject("ESTADO"));
			dto.setEstado_Actual((String) rs.getObject("ESTADO_ACTUAL"));
			dto.setUsuario_Creacion((String) rs.getObject("USUARIO_CREACION"));
			dto.setUsuario_Modificacion((String) rs.getObject("USUARIO_MODIFICAION"));
			dto.setUsuario_Eliminacion((String) rs.getObject("USUARIO_ELIMINACION"));
			dto.setFecha_Creacion((Date) rs.getObject("FECHA_CREACION"));
			dto.setFecha_Modificacion((Date) rs.getObject("FECHA_MODIFICACION"));
			dto.setFecha_Eliminacion((Date) rs.getObject("FECHA_ELIMINACION"));
			results.add(dto);
		}
		return results;
	}
	public synchronized static Result savePrueba(TBol_Programacion linea, ConectionManager conectionManager) {
		//log.debug("Registrando el historial de ticket " + linea.getCod_Ticket()+" linea "+linea.getLinea()+" contrato "+linea.getContrato());
		Result result= new Result();
		List<String> lista= new ArrayList<String>();
		String sql = "{call PGK_TBOL_DETALLETELEFONIA.P_INGRESABANDEJA(?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement callableStatement = null;
		try {
			callableStatement =conectionManager.PrepareCall(sql);
			callableStatement.setString(1, linea.getOperation());
			callableStatement.setString(2, linea.getId_Client());
			callableStatement.setString(3, linea.getTicket_Prioridad());
			callableStatement.setString(4, linea.getTicket_Descripcion());
			callableStatement.setString(5, linea.getEmail_Cliente());
			callableStatement.setString(6, linea.getTelefono_Cliente());
			callableStatement.setString(7, linea.getTipo_Solicitud());
			callableStatement.registerOutParameter(8,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(9,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(10,OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(11,OracleTypes.VARCHAR);
			int afectados=callableStatement.executeUpdate();
			if (afectados > 0) {
				String rs = (String)callableStatement.getObject(8);
				String codigo = (String)callableStatement.getObject(9);	
				String mensaje = (String)callableStatement.getObject(10);	
				String ticket = (String)callableStatement.getObject(11);	
				if(rs.equalsIgnoreCase("OK")){
					log.info("Se registro satisfactoriamente " + afectados + " registros de historial.");
					result.ok("Se registro satisfactoriamente " + afectados + " registros de historial.");
					lista.add(rs);
					lista.add(codigo);
					lista.add(mensaje);
					lista.add(ticket);
					result.setData(lista);
					return result;
				}
				lista.add(rs);
				lista.add(codigo);
				lista.add(mensaje);
				lista.add(ticket);
				result.setData(lista);
				result.error((String)callableStatement.getObject(10));
				return result;
			}
			result.error("No se registro el historial");
			return result;
		} catch (SQLException e) {
				log.info("Error al intentar guardar el historial con id " + linea.getCod_Ticket()+", " +e);
				result.error("Error al intentar guardar el historial con id " + linea.getCod_Ticket()+", " +e);
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

}
