package micrium.calldetail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import micrium.calldetail.bussines.GenerateBL;
import micrium.calldetail.bussines.GenerateThread;
import micrium.calldetail.bussines.SysParameter;
import micrium.calldetail.dato.ConectionManager;
import micrium.calldetail.model.TBol_Contrato;
import micrium.calldetail.model.TBol_Linea;
import micrium.calldetail.model.TBol_Programacion;
import micrium.calldetail.model.TBol_ProgramacionCompleta;
import micrium.calldetail.result.Code;
import micrium.calldetail.result.Result;
import org.apache.log4j.Logger;


/**
 * @author pedro
 * 
 */
public class Generate {

	private static final Logger log = Logger.getLogger(Generate.class);


	
	public static void generateTBol() {
		log.info("Iniciando el registro del proceso de generacion");
		ConectionManager conectionManager = ConectionManager.getInstance();
		
		if (!conectionManager.open()) {
			log.info("No se pudo abrir la conexion a la base de datos");
			return;
		}
		
		try {
			// obteniendo las programaciones pendientes*/
			Result result = generateCallDetailTBol(conectionManager);
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				log.info("Termino el proceso de generacion de detalle, " + result.getDescription());
				log.info(result.getDescription());
			}

		} catch (Exception e) {
			log.error("Se ha producido una excepcion al intentar registrar el proceso de generacion", e);
		} finally {
			conectionManager.close();
		}

	}
	public static Result generarHilos(List<TBol_ProgramacionCompleta> listaLineas,List<TBol_Linea> lineasContratos,ConectionManager conectionManager){
		
		Result result = new Result();
		result.ok("generando hilos");
		long count =listaLineas.size();
		long countThread=Long.parseLong(SysParameter.getProperty(SysParameter.COUNT_THREAD));
		long bloque = count / countThread;
		long modulo = count % countThread;
		if (bloque == 0) {
			countThread = modulo;
		}
		int ini = 0;
		List<GenerateThread> lstHilos = new ArrayList<GenerateThread>();
		log.info("Se va dividir la tarea de generacion  de " + count + " Lineas entre " + countThread + " hilos");
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
			    int contadorLineas=0;
			    List<TBol_Linea> lstLineas = new ArrayList<TBol_Linea>();
			    	while(contadorLineas<lineasPorHilo){								
						TBol_Linea linea = new TBol_Linea();
						linea.setCod_ticket(listaLineas.get(0).getCod_Ticket());
						linea.setContrato(listaLineas.get(0).getId_Contrato());
						linea.setLinea(listaLineas.get(0).getLinea());
						linea.setId_client(listaLineas.get(0).getId_Client());
						linea.setReintentador(listaLineas.get(0).getEstado());
						linea.setEstado(listaLineas.get(0).getEstado_Actual());
						linea.setFecha_Inicial(listaLineas.get(0).getFecha_inicial());
						linea.setFecha_Final(listaLineas.get(0).getFecha_Final());						
						lstLineas.add(linea);
						listaLineas.remove(0);
						contadorLineas++;
					}
			    	GenerateThread hilo = new GenerateThread(lstLineas);
					lstHilos.add(hilo);
			    	contadorVeces++;
			    	if(contadorVeces==veces){
			    		lineasPorHilo--;
			    		
			    	}
				log.info("ini:" + ini + " | bloque:" + (bloque + add) + " | size:" + lineasContratos.size());
				
			}

			ini += (bloque + add);
		}

		if (lstHilos.isEmpty()) {
			result.error("No hay hilos para lanzar la tarea de generacion de detalles");
			return result;
		}
		log.info("Se va lanzar " + lstHilos.size() + " hilos.");
		for (GenerateThread hilo : lstHilos) {
			hilo.start();
		}

		result.ok("Los " + lstHilos.size() + " hilos se lanzaron satisfactoriamente");
		return result;
		
	}
	public static Result generateCallDetailTBol(ConectionManager conectionManager) {
		Result result = new Result();

		try {
			// Vamos a iniciar la carga de los parametros del sistema
			result = SysParameter.load(conectionManager);
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				return result;
			}
			// Vamos a obtener la cantidad de programaciones activas
			result = GenerateBL.findProgramacionesActivosTBol(conectionManager);
			if (result.getCode().equalsIgnoreCase(Code.ERROR)){
				log.info("No se obtuvo ninguna programacion");
				return result;
			}
			List<TBol_ProgramacionCompleta> listaLineas= new ArrayList<TBol_ProgramacionCompleta>();
			List<TBol_Programacion> programacionActiva = (List<TBol_Programacion>)result.getData();
			//List<TBol_Contrato> contratosProgramacion=new ArrayList<TBol_Contrato>();
			List<TBol_Contrato> contratosProgramacion;
			List<TBol_Linea> lineasContratos= new ArrayList<TBol_Linea>();
			
			
			for (int i = 0; i < programacionActiva.size(); i++) {
				Date fechaF=programacionActiva.get(i).getFecha_Ejecucion();
				Date fechaI=programacionActiva.get(i).getFecha_Ejecucion();
				//obteniendo contratos de cada programacion Activa
				result=GenerateBL.findcontratosProgramacionTBol(programacionActiva.get(i).getCod_Ticket(), conectionManager);
				if (result.getCode().equalsIgnoreCase(Code.ERROR)){
					log.info("No se obtuvo contratos para esta programacion: "+programacionActiva.get(i).getCod_Ticket());
					continue;
				}
				contratosProgramacion= (List<TBol_Contrato>)result.getData();	
				if(!contratosProgramacion.isEmpty()){
					for (int j = 0; j < contratosProgramacion.size(); j++) {
						//obtener lineas de cada contrato
						
						result=GenerateBL.findLineasContratos(contratosProgramacion.get(j).getContrato(),contratosProgramacion.get(j).getCod_Ticket(), conectionManager);
						if (result.getCode().equalsIgnoreCase(Code.ERROR)){
							log.info("No se obtuvo lineas para este contrato: "+contratosProgramacion.get(j).getContrato());
							continue;
						}
						lineasContratos=(List<TBol_Linea>) result.getData();
						if(!lineasContratos.isEmpty()){
							for (TBol_Linea linea : lineasContratos) {
								TBol_ProgramacionCompleta l= new TBol_ProgramacionCompleta();
								l.setCod_Ticket(programacionActiva.get(i).getCod_Ticket());
								l.setId_Client(programacionActiva.get(i).getId_Client());
								l.setId_Contrato(contratosProgramacion.get(j).getContrato());
								l.setLinea(linea.getLinea());
								if(programacionActiva.get(i).getPeriodicidad().equals(SysParameter.getProperty(SysParameter.TIPO_PERIODICIDAD_S))){
									if(programacionActiva.get(i).getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_WEEKLY))){							
										java.util.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
										l.setFecha_Final(feF);
										
										java.util.Date feI= new Date(fechaI.getYear(),fechaI.getMonth(),fechaI.getDate()-9);
										l.setFecha_inicial(feI);
										
									}
									if(programacionActiva.get(i).getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_QUINCENAL))){

										java.util.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
										l.setFecha_Final(feF);
										
										java.util.Date feI= new Date(fechaI.getYear(),fechaI.getMonth(),fechaI.getDate()-17);
										l.setFecha_inicial(feI);
										
									}
									if(programacionActiva.get(i).getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_MONTHLY))){
										java.util.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
										l.setFecha_Final(feF);
										
										java.util.Date feI= new Date(fechaI.getYear(),fechaI.getMonth()-1,fechaI.getDate()-2);
										l.setFecha_inicial(feI);
										
									}
									if(programacionActiva.get(i).getTipo_Solicitud().equals(SysParameter.getProperty(SysParameter.TIPOSOLICITUD_BIMONTHLY))){
										java.util.Date feF= new Date(fechaF.getYear(),fechaF.getMonth(),fechaF.getDate()-2);
										l.setFecha_Final(feF);
										
										java.util.Date feI= new Date(fechaI.getYear(),fechaI.getMonth()-2,fechaI.getDate()-2);
										l.setFecha_inicial(feI);
										
									}
									
								}
								if(programacionActiva.get(i).getPeriodicidad().equals(SysParameter.getProperty(SysParameter.TIPO_PERIODICIDAD_N))){
									java.util.Date feI= new Date(programacionActiva.get(i).getFecha_Inicial().getYear(),programacionActiva.get(i).getFecha_Inicial().getMonth(),programacionActiva.get(i).getFecha_Inicial().getDate());
									java.util.Date feF= new Date(programacionActiva.get(i).getFecha_Final().getYear(),programacionActiva.get(i).getFecha_Final().getMonth(),programacionActiva.get(i).getFecha_Final().getDate());
									
									l.setFecha_inicial(feI);
									l.setFecha_Final(feF);
								}
								l.setEstado_Actual(programacionActiva.get(i).getEstado_Actual());
								listaLineas.add(l);
							}
							
						}
						//return result;
					}
					
				}
			
		
			}
			result=generarHilos(listaLineas, lineasContratos, conectionManager);
			if (result.getCode().equalsIgnoreCase(Code.ERROR)){
				log.info("No se genero hilos");
				return result;
			}
			return result;

		} catch (Exception e) {
			log.error("Se ha producido una excepcion al intentar realizar la generacion de detalle", e);
			result.error("Se ha producido una excepcion con el mensaje {" + e.getMessage() + "}, al intentar realizar la generación de detalle");
			return result;
		}

	}


}
