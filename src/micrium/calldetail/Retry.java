package micrium.calldetail;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import micrium.calldetail.bussines.GenerateBL;
import micrium.calldetail.bussines.SysParameter;
import micrium.calldetail.dato.ConectionManager;
import micrium.calldetail.model.TBol_Historial;
import micrium.calldetail.model.TBol_Linea;
import micrium.calldetail.model.TBol_Programacion;
import micrium.calldetail.model.TBol_ProgramacionCompleta;
import micrium.calldetail.result.Code;
import micrium.calldetail.result.Result;
import micrium.calldetail.utils.NumberUtil;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
/**
 * @author pedro
 * 
 */
public class Retry {

	private static final Logger log = Logger.getLogger(Retry.class);
	//public 

	public static void main(String[] args) {
		DOMConfigurator.configure("etc" + File.separator + "log4j.xml");
	//	generate();
	}
	public static void retryTBol() {
		log.info("Iniciando el reintentador de generacion de detalle y envio de correo");
		ConectionManager conectionManager = ConectionManager.getInstance();

		if (!conectionManager.open()) {
			log.info("No se pudo abrir la conexion a la base de datos");
			return;
		}
		log.info("Se abrio la conexion a la base de datos");
		try {
			Result result = retryCallDetailTBol(conectionManager);
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				log.info("Termino el proceso de reintentos, " + result.getDescription());
				log.info(result.getDescription());
			}

		} catch (Exception e) {
			log.error("Se ha producido una excepcion al reintentar", e);
		} finally {
			conectionManager.close();
		}

	}
	/**
	 * Metodo obtiene la programaciones pendientes
	 */
	public static Result retryCallDetailTBol(ConectionManager conectionManager) {
		Result result = new Result();

		try {
			// Vamos a iniciar la carga de los parametros del sistema
			result = SysParameter.load(conectionManager);
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				return result;
			}


			// Vamos a obtener la cantidad de programaciones DEL HISTORIAL Q ESTAN EN EPR
			result = GenerateBL.findEPRTBol(conectionManager);
			if (!result.getCode().equalsIgnoreCase(Code.ERROR)){
				List<TBol_Historial> lstHistorialEPR = (List<TBol_Historial>)result.getData();	
				List<TBol_ProgramacionCompleta> listaLineas= new ArrayList<TBol_ProgramacionCompleta>();
				//vamos cada una de la programacion de los historiales EPR
				for (TBol_Historial tBol_Historial : lstHistorialEPR) {
					TBol_Programacion programacion= new TBol_Programacion();
					TBol_ProgramacionCompleta programacionCompleta= new TBol_ProgramacionCompleta();
					result = GenerateBL.findProgramacionTBol(tBol_Historial.getCod_Ticket(), conectionManager);
					if (result.getCode().equalsIgnoreCase(Code.ERROR)){
						log.info("No se obtuvo ninguna programacion");
						return result;
					}
					programacion=(TBol_Programacion)result.getData();
					programacionCompleta.setCod_Ticket(tBol_Historial.getCod_Ticket());
					programacionCompleta.setId_Client(programacion.getId_Client());
					programacionCompleta.setId_Client(tBol_Historial.getContrato());
					programacionCompleta.setId_Contrato(tBol_Historial.getContrato());
					programacionCompleta.setLinea(tBol_Historial.getLinea());
					programacionCompleta.setEstado_Actual(tBol_Historial.getEstado());
					programacionCompleta.setEstado("R");
					programacionCompleta.setPeriodicidad(programacion.getPeriodicidad());
					programacionCompleta.setTipo_solicitud(programacion.getTipo_Solicitud());
					programacionCompleta.setFecha_inicial(programacion.getFecha_Inicial());
					programacionCompleta.setFecha_Final(programacion.getFecha_Final());
					programacionCompleta.setFecha_ejecucion(programacion.getFecha_Ejecucion());
					listaLineas.add(programacionCompleta);
				}
				for (TBol_ProgramacionCompleta tBol_ProgramacionCompleta : listaLineas) {
					java.util.Date fechaF=tBol_ProgramacionCompleta.getFecha_ejecucion();
					java.util.Date fechaI=tBol_ProgramacionCompleta.getFecha_ejecucion();
					
					if(tBol_ProgramacionCompleta.getPeriodicidad().equals(SysParameter.getProperty(SysParameter.TIPO_PERIODICIDAD_S))){
						if(tBol_ProgramacionCompleta.getTipo_solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_WEEKLY))){							
							java.util.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
							tBol_ProgramacionCompleta.setFecha_Final(feF);
							
							java.util.Date feI= new Date(fechaI.getYear(),fechaI.getMonth(),fechaI.getDate()-9);
							tBol_ProgramacionCompleta.setFecha_inicial(feI);
							
						}
						if(tBol_ProgramacionCompleta.getTipo_solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_QUINCENAL))){

							java.util.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
							tBol_ProgramacionCompleta.setFecha_Final(feF);
							
							java.util.Date feI= new Date(fechaI.getYear(),fechaI.getMonth(),fechaI.getDate()-17);
							tBol_ProgramacionCompleta.setFecha_inicial(feI);
							
						}
						if(tBol_ProgramacionCompleta.getTipo_solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_MONTHLY))){
							java.util.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
							tBol_ProgramacionCompleta.setFecha_Final(feF);
							
							java.util.Date feI= new Date(fechaI.getYear(),fechaI.getMonth()-1,fechaI.getDate()-2);
							tBol_ProgramacionCompleta.setFecha_inicial(feI);
							
						}
						if(tBol_ProgramacionCompleta.getTipo_solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_BIMONTHLY))){
							java.util.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
							tBol_ProgramacionCompleta.setFecha_Final(feF);
							
							java.util.Date feI= new Date(fechaI.getYear(),fechaI.getMonth()-2,fechaI.getDate()-2);
							tBol_ProgramacionCompleta.setFecha_inicial(feI);
							
						}
						
					}
					if(tBol_ProgramacionCompleta.getPeriodicidad().equals(SysParameter.getProperty(SysParameter.TIPO_PERIODICIDAD_N))){
						java.util.Date feI= new Date(tBol_ProgramacionCompleta.getFecha_inicial().getYear(),tBol_ProgramacionCompleta.getFecha_inicial().getMonth(),tBol_ProgramacionCompleta.getFecha_inicial().getDate());
						java.util.Date feF= new Date(tBol_ProgramacionCompleta.getFecha_Final().getYear(),tBol_ProgramacionCompleta.getFecha_Final().getMonth(),tBol_ProgramacionCompleta.getFecha_Final().getDate());
						tBol_ProgramacionCompleta.setFecha_inicial(feI);
						tBol_ProgramacionCompleta.setFecha_Final(feF);
					}
				}
				List<TBol_Linea> nul= new ArrayList<TBol_Linea>();

				for (TBol_ProgramacionCompleta programacion : listaLineas) {
					//pongo la linea en estado de reintento 
					log.info("CAMBIANDO A ESTADO REN");
					TBol_Linea linea= new TBol_Linea();
					linea.setCod_ticket(programacion.getCod_Ticket());
					linea.setContrato(programacion.getId_Contrato());
					linea.setLinea(programacion.getLinea());
					linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_REN));
					result=GenerateBL.updateEstado(linea, conectionManager);
					if (result.getCode().equalsIgnoreCase(Code.ERROR)){
						log.info("No se cambio a estado REN");
						return result;
					}
				}		
				result=Generate.generarHilos(listaLineas,nul, conectionManager);
				if (result.getCode().equalsIgnoreCase(Code.ERROR)){
					log.info("No se pudo reintentar los historiales con estado EPR");
					return result;
				}	
			}
			long sleep = NumberUtil.toLong(SysParameter.getProperty(SysParameter.CONSOLIDADO_SAC_SLEEP));
			// Vamos a obtener la cantidad de programaciones DEL HISTORIAL Q ESTAN EN EPV
				result = GenerateBL.findEPVTBol(conectionManager);			
				if (!result.getCode().equalsIgnoreCase(Code.ERROR)){
					//List<TBol_Historial> lstHistorialEPV = (List<TBol_Historial>)result.getData();	
					List<TBol_Historial>lstHitorial = (List<TBol_Historial>)result.getData();	
					for (TBol_Historial tBol_Historial : lstHitorial) {
						//pongo la linea en estado de reintento 
						log.info("CAMBIANDO A ESTADO REN");
						TBol_Linea linea= new TBol_Linea();
						linea.setCod_ticket(tBol_Historial.getCod_Ticket());
						linea.setContrato(tBol_Historial.getContrato());
						linea.setLinea(tBol_Historial.getLinea());
						linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_REN));
						result=GenerateBL.updateEstado(linea, conectionManager);
						if (result.getCode().equalsIgnoreCase(Code.ERROR)){
							log.info("No se cambio a estado REN");
							return result;
						}
						//tBol_Historial.setCodigo_Detalle("R");
					}
					//saco las fechas de la tabla progrmacion
					for (TBol_Historial tBol_Historial : lstHitorial) {
						TBol_Programacion programacion= new TBol_Programacion();
						result=GenerateBL.findProgramacionTBol(tBol_Historial.getCod_Ticket(), conectionManager);
						if (result.getCode().equalsIgnoreCase(Code.ERROR)){
							log.info("No se cambio a estado REN");
							return result;
						}
						programacion=(TBol_Programacion)result.getData();
						
						java.util.Date fechaF=programacion.getFecha_Ejecucion();
						java.util.Date fechaI=programacion.getFecha_Ejecucion();
						
						if(programacion.getPeriodicidad().equals(SysParameter.getProperty(SysParameter.TIPO_PERIODICIDAD_S))){
							if(programacion.getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_WEEKLY))){							
								java.sql.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
								programacion.setFecha_Final(feF);
								
								java.sql.Date feI= new Date(fechaI.getYear(),fechaI.getMonth(),fechaI.getDate()-9);
								programacion.setFecha_Inicial(feI);
								
							}
							if(programacion.getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_QUINCENAL))){

								java.sql.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
								programacion.setFecha_Final(feF);
								
								java.sql.Date feI= new Date(fechaI.getYear(),fechaI.getMonth(),fechaI.getDate()-17);
								programacion.setFecha_Inicial(feI);
								
							}
							if(programacion.getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_MONTHLY))){
								java.sql.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
								programacion.setFecha_Final(feF);
								
								java.sql.Date feI= new Date(fechaI.getYear(),fechaI.getMonth()-1,fechaI.getDate()-2);
								programacion.setFecha_Inicial(feI);
								
							}
							if(programacion.getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_BIMONTHLY))){
								java.sql.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
								programacion.setFecha_Final(feF);
								
								java.sql.Date feI= new Date(fechaI.getYear(),fechaI.getMonth()-2,fechaI.getDate()-2);
								programacion.setFecha_Inicial(feI);
								
							}
							
						}
						
						
						tBol_Historial.setFecha_Inicial(programacion.getFecha_Inicial());
						tBol_Historial.setFecha_Final(programacion.getFecha_Final());
						tBol_Historial.setTipo_solicitud(programacion.getTipo_Solicitud());
						tBol_Historial.setPeriodicidad(programacion.getPeriodicidad());
						tBol_Historial.setFecha_Ejecucion(programacion.getFecha_Ejecucion());
						tBol_Historial.setFecha_Inicial(programacion.getFecha_Inicial());
						tBol_Historial.setFecha_Final(programacion.getFecha_Final());
					}
					
					result=Send.generarHilos(lstHitorial, conectionManager);
					if (result.getCode().equalsIgnoreCase(Code.ERROR)){
						log.info("No se pudo reintentar los historiales con estado EPV");
						return result;
					}				
				}		
			
			return result;

		} catch (Exception e) {
			log.error("Se ha producido una excepcion al reintentar obtener los historiales pendientes", e);
			result.error("Se ha producido una excepcion con el mensaje {" + e.getMessage() + "}, al reintentar obtener los historiales pendientes");
			return result;
		}

	}


}
