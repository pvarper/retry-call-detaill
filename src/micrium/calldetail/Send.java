package micrium.calldetail;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import micrium.calldetail.bussines.GenerateThreadS;




import micrium.calldetail.bussines.SendBL;
import micrium.calldetail.bussines.SysParameter;
import micrium.calldetail.dao.TBolProgramacionDAO;
import micrium.calldetail.dato.ConectionManager;

import micrium.calldetail.model.TBol_Linea;
import micrium.calldetail.model.TBol_Programacion;


import micrium.calldetail.model.TBol_Historial;

import micrium.calldetail.result.Code;
import micrium.calldetail.result.Result;

import micrium.calldetail.test.Test;
import micrium.calldetail.utils.NumberUtil;


import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
/**
 * @author pedro
 * 
 */
public class Send {

	private static final Logger log = Logger.getLogger(Send.class);
	private static String EMAILENPTY = "EMAILENPTY";

	public static void main(String[] args) {
		DOMConfigurator.configure("etc" + File.separator + "log4j.xml");
		send();
	}

	public static void send() {
		log.info("Iniciando el registro del proceso de envio de correos.");
		ConectionManager conectionManager = ConectionManager.getInstance();

		if (!conectionManager.open()) {
			log.info("No se pudo abrir la conexion a la base de datos.");
			return;
		}

		try {

			// Vamos a iniciar el proceso de envio de detalles por correo.

			Result result = sendCallDetail(conectionManager);
			log.info(result.getDescription());

		} catch (Exception e) {
			log.error("Se ha producido una excepcion al intentar registrar el proceso de envio", e);

		} finally {
			log.info("Termino el proceso de envio de detalles por correo.");
			conectionManager.close();
		}
	}

	/**
	 * Metodo que realiza el envios de mail programados.
	 * @throws InterruptedException 
	 */
	public static Result generarHilos(List<TBol_Historial> lstHitorial,ConectionManager conectionManager) throws InterruptedException{
		
		Result result= new Result();
		result.ok("generando hilos");
		long sleep = NumberUtil.toLong(SysParameter.getProperty(SysParameter.CONSOLIDADO_SAC_SLEEP));
		int count = lstHitorial.size();
		long countThread=Long.parseLong(SysParameter.getProperty(SysParameter.COUNT_THREAD));
		long bloque = count / countThread;
		long modulo = count % countThread;
		if (bloque == 0) {
			countThread = modulo;
		}
		int ini = 0;
		List<GenerateThreadS> lstHilos = new ArrayList<GenerateThreadS>();
		log.info("Se va dividir la tarea de envio  de " + count + " historiales entre " + countThread + " hilos");
		long lineasPorHilo=1;
		long veces=0;
		if(count>countThread){
			lineasPorHilo=(count/countThread)+1;
			veces=(count%countThread);
			if(veces==0){
				veces=countThread;
				lineasPorHilo--;
			}
		}
		int contadorVeces=0;
		for (int k = 0; k < countThread; k++) {
			int add = (k < modulo) ? 1 : 0;
			if (result.getCode().equalsIgnoreCase(Code.OK)) {
				//Asignando las listas a cada hilo
				 int contadorLineas=0;
				 List<TBol_Historial> lstLineas = new ArrayList<TBol_Historial>();
				while(contadorLineas<lineasPorHilo){						
					TBol_Historial linea = new TBol_Historial();
					linea.setCod_Ticket(lstHitorial.get(0).getCod_Ticket());
					linea.setContrato(lstHitorial.get(0).getContrato());
					linea.setLinea(lstHitorial.get(0).getLinea());
					linea.setEstado(lstHitorial.get(0).getEstado());
					linea.setCodigo_Detalle(lstHitorial.get(0).getCodigo_Detalle());
					linea.setFecha_Inicial(lstHitorial.get(0).getFecha_Inicial());
					linea.setFecha_Final(lstHitorial.get(0).getFecha_Final());
					linea.setTipo_solicitud(lstHitorial.get(0).getTipo_solicitud());
					linea.setFecha_Ejecucion(lstHitorial.get(0).getFecha_Ejecucion());
					linea.setPeriodicidad(lstHitorial.get(0).getPeriodicidad());
					lstLineas.add(linea);
					lstHitorial.remove(0);
					contadorLineas++;			
				}
				GenerateThreadS hilo = new GenerateThreadS(lstLineas);
				lstHilos.add(hilo);
				contadorVeces++;
		    	if(contadorVeces==veces){
		    		lineasPorHilo--;
		    		
		    	}
				log.info("ini:" + ini + " | bloque:" + (bloque + add) + " | size:" + lstHitorial.size());
				
			}

			ini += (bloque + add);
		}

		if (lstHilos.isEmpty()) {
			result.error("No hay hilos para lanzar la tarea de generacion de detalles");
			return result;
		}

		log.info("Se va lanzar " + lstHilos.size() + " hilos.");
		for (GenerateThreadS hilo : lstHilos) {
			hilo.start();
		}


			
			log.info("Se va esperar un tiempo de " + sleep + " minutos enviar los correos");
			Thread.sleep(sleep);

		result.ok("El proceso finalizo correctamente");
		return result;
		
	}
	private static Result sendCallDetail(ConectionManager conectionManager) {
		log.info("Se va iniciar el proceso de envio de detalles por correo");

		Result result = new Result();
		try {

			// Vamos a iniciar la carga de los parametros del sistema.
			result = SysParameter.load(conectionManager);
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				return result;
			}
			log.info(result.getDescription());
			
			//obteniendo programacion con estado epp
			result= SendBL.findEPPProgramacionTBol(conectionManager);
			if (result.getCode().equalsIgnoreCase(Code.ERROR)){
				log.info("No se obtuvo ninguna Programacion con estado EPP");
				return result;
			}
			List<TBol_Programacion> lstProgramacionesEPP = (List<TBol_Programacion>)result.getData();
			
			List<TBol_Historial> lstHitorial= new ArrayList<TBol_Historial>();
			for (TBol_Programacion tBol_Programacion : lstProgramacionesEPP) {	
				Date fechaF=tBol_Programacion.getFecha_Ejecucion();
				Date fechaI=tBol_Programacion.getFecha_Ejecucion();
				if(tBol_Programacion.getPeriodicidad().equals(SysParameter.getProperty(SysParameter.TIPO_PERIODICIDAD_S))){
					if(tBol_Programacion.getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_WEEKLY))){							
						java.sql.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
						tBol_Programacion.setFecha_Final(feF);
						
						java.sql.Date feI= new Date(fechaI.getYear(),fechaI.getMonth(),fechaI.getDate()-9);
						tBol_Programacion.setFecha_Inicial(feI);
						
					}
					if(tBol_Programacion.getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_QUINCENAL))){

						java.sql.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
						tBol_Programacion.setFecha_Final(feF);
						
						java.sql.Date feI= new Date(fechaI.getYear(),fechaI.getMonth(),fechaI.getDate()-17);
						tBol_Programacion.setFecha_Inicial(feI);
						
					}
					if(tBol_Programacion.getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_MONTHLY))){
						java.sql.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
						tBol_Programacion.setFecha_Final(feF);
						
						java.sql.Date feI= new Date(fechaI.getYear(),fechaI.getMonth()-1,fechaI.getDate()-2);
						tBol_Programacion.setFecha_Inicial(feI);
						
					}
					if(tBol_Programacion.getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_BIMONTHLY))){
						java.sql.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
						tBol_Programacion.setFecha_Final(feF);
						
						java.sql.Date feI= new Date(fechaI.getYear(),fechaI.getMonth()-2,fechaI.getDate()-2);
						tBol_Programacion.setFecha_Inicial(feI);
						
					}
					
				}
				
				result=SendBL.findEPPHistorialTBol(tBol_Programacion.getCod_Ticket(), conectionManager);
				if (result.getCode().equalsIgnoreCase(Code.ERROR)){
					log.info("No se obtuvo ninguna historial con estado EPP");
					return result;
				}
				List<TBol_Historial>lstHistAux=(List<TBol_Historial>) result.getData();		
				//finaliza historial con estado epp y crea historial con estado epv
				for (TBol_Historial tBol_Historial : lstHistAux) {				
						//si es reintentador no actualiza y no guarda historial
						tBol_Historial.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPP));
						result=SendBL.updateHistorialTBol(tBol_Historial, conectionManager);
						if (result.getCode().equalsIgnoreCase(Code.ERROR)){
							log.info("No se guardo el historial");
							return result;
						}	
						tBol_Historial.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPV));
						result=SendBL.saveHistorialTBol(tBol_Historial, conectionManager);
						if (result.getCode().equalsIgnoreCase(Code.ERROR)){
							log.info("No se guardo el historial");
							return result;
						}	
						tBol_Historial.setTipo_solicitud(tBol_Programacion.getTipo_Solicitud());
						tBol_Historial.setPeriodicidad(tBol_Programacion.getPeriodicidad());
						tBol_Historial.setFecha_Ejecucion(tBol_Programacion.getFecha_Ejecucion());
						tBol_Historial.setFecha_Inicial(tBol_Programacion.getFecha_Inicial());
						tBol_Historial.setFecha_Final(tBol_Programacion.getFecha_Final());
						//tBol_Historial.set(tBol_Programacion.getPeriodicidad());
						/*if(tBol_Programacion.getPeriodicidad().equalsIgnoreCase("N")){
							tBol_Historial.setFecha_Inicial(tBol_Programacion.getFecha_Inicial());
							tBol_Historial.setFecha_Final(tBol_Programacion.getFecha_Final());
						}*/	
						lstHitorial.add(tBol_Historial);
				}		
			}
			result=generarHilos(lstHitorial, conectionManager);
			if (result.getCode().equalsIgnoreCase(Code.ERROR)){
				log.info("No se genero hilos");
				return result;
			}
			return result;

		} catch (Exception e) {
			log.error("Se ha producido una excepción al intentar realizar el envio de correos pendientes", e);
			result.error("[Se ha producido una excepción con el mensaje {" + e.getMessage()
					+ "}, al intentar realizar el envio de correos pendientes");
			return result;
		}

	}


	/**
	 * Metodo que realiza el proceso de enviar todos los correos pasados por parametros lstCorreos, ademas en lstDetallesEnviados adiciona todos los
	 * detalles de los correos enviados, esto para generar un log por detalle y no por cada correo, no olvidar que pueden haber detalles con varios
	 * correos y en lstCorreosFallidos adiciona todos los correos que no pudieron ser enviados, esto para no volver a intentar a enviar el mismo
	 * correo podria causar un ciclo infinito si persistiera el error.
	 * 
	 * @param lstCorreos lista de correos que seran enviados.
	 * @param lstDetallesEnviados lista de detalles que ya se enviaron al menos un correo.
	 * @param lstCorreosFallidos lista de correos que fallaron en el envio.
	 * @param conectionManager manejador de conexion para acceder a la base de datos.
	 * @return Retorna un string de confirmacion o de errores de correos no enviados.
	 */
	

}
