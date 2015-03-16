package micrium.calldetail.bussines;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import micrium.calldetail.dato.ConectionManager;
import micrium.calldetail.mail.Mail;
import micrium.calldetail.mail.MailManager;
import micrium.calldetail.model.TBol_Correo;
import micrium.calldetail.model.TBol_Historial;
import micrium.calldetail.model.TBol_Linea;
import micrium.calldetail.model.TBol_Programacion;
import micrium.calldetail.result.Code;
import micrium.calldetail.result.Result;
import micrium.calldetail.utils.BooleanUtil;
import micrium.calldetail.utils.DateUtil;
import micrium.calldetail.utils.DigestUtil;
import micrium.calldetail.utils.EmailUtil;
import micrium.calldetail.utils.FileUtil;
import micrium.calldetail.utils.FilenameUtil;
import micrium.calldetail.utils.NumberUtil;
import micrium.calldetail.utils.StringUtil;
import micrium.ws.Consolidado;
import micrium.ws.ConsolidadoDetalle;

import org.apache.log4j.Logger;
import org.quartz.jobs.ee.jms.JmsJobException;
/**
 * @author pedro
 * 
 */
public class GenerateThreadS extends Thread {

	//private List<Programacion> lstProgramaciones;
	private List<TBol_Historial> lstHistorial;
	private static final Logger log = Logger.getLogger(GenerateThreadS.class);
	private static int count = 0;
	private static String mensaje_error = StringUtil.EMPTY;
	private static String mensaje_ok = StringUtil.EMPTY;

	private static final String DETAILEMPTY = "DETAILEMPTY";

	public GenerateThreadS(List<TBol_Historial> lstHist) {
		this.lstHistorial = lstHist;
	}

	public void run() {
		log.info("Se inicio el hilo para el envio de detalle de " + lstHistorial.size() + " historiales");
		count++;
		ConectionManager conectionManager = ConectionManager.getInstance();

		if (!conectionManager.open()) {
			log.info("No se ha podido establecer conexion con la base de datos");
			return;
		}

		try {
			Result result = SysParameter.load(conectionManager);
			 result = generateTBol(conectionManager);
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				mensaje_error += result.getDescription();
			} else {
				mensaje_ok += result.getDescription();
			}

			if (count <= 0) {
				log.info("Terminaron todos los hilos de generar los detalles");			
				log.info(result.getDescription());		
			}
				
		} finally {
			conectionManager.close();
		}
	}

	/**
	 * Metodo que genera el detalle de todas la lista de programaciones que les toca ser enviados por correo.
	 * 
	 * @param conectionManager clase que gestiona la conexion a la base de datos
	 * @return retorna una instancia de Result tiene condigo Ok si la generacion es correcta y codigo error si la generacion fallo.
	 */
	/**
	 * @param conectionManager
	 * @return
	 */
	public Result generateTBol(ConectionManager conectionManager) {
		Result result = new Result();
		try {
			StringBuilder sb_error = new StringBuilder();
			StringBuilder sb_ok = new StringBuilder();
			// RECORRER LA LISTA DE HISTORIAL PARA PROCESAR ENVIOS
			for (TBol_Historial historial : lstHistorial) {
				
				// Vamos a obtener el correo
				result = SendBL.findCorreoByIdTBolString(historial.getCod_Ticket(), conectionManager);
				if (!result.getCode().equalsIgnoreCase(Code.OK)) {
					sb_error.append("[" + result.getDescription() + ", nro de prog " + historial.getContrato() + "]");
					continue;
				}
				List<String> correo = (List<String>) result.getData();

				//generar el detalle de llamadas por linea TBOL			
				log.info("Se va a procesar el envio de correo de la linea: " + historial.getLinea());	
					result = procesarCorreos(historial, correo, conectionManager);
				if (!result.getCode().equalsIgnoreCase(Code.OK)) {
					log.info(result.getDescription() + ", linea con ticket " + historial.getCod_Ticket());
					sb_error.append("[" + result.getDescription() + ", nro de linea " + historial.getLinea() + "]");
					continue;
				}
			
				if (result.getDescription().equalsIgnoreCase(DETAILEMPTY)) {
					sb_ok.append("[No hay detalle para generar, nro de prog " + historial.getLinea() + "]");
				}

				log.info("El proceso de generacion de detalle de la linea con numero " + historial.getLinea()
						+ " termino correctamente");
			}

			if (!sb_error.toString().isEmpty()) {
				result.error(sb_error.toString());
				return result;
			}

			result.ok(sb_ok.toString());
			return result;

		} catch (Exception e) {
			log.error("2Se ha producido una excepcion al intentar generar el detalle", e);
			result.error("[2Se ha producido una excepcion con el mensaje {" + e.getMessage() + "}, al intentar generar el detalle]");
			return result;

		} finally {
			count--;
			log.info("El hilo termino de generar los detalles programados");
		}
	}
	private static Result procesarCorreos(TBol_Historial historial,List<String> correo,ConectionManager conectionManager) {
		log.info("Vamos a procesar el envio al correo de la linea" +historial.getLinea()  );

		Result result = new Result();

		StringBuilder sb_error = new StringBuilder();
			
			// Vamos a procesar el envio por correo programado.
			result=procesarEnvio(historial,correo,conectionManager);
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				log.info("No se envio el correo pendiente " + correo);
				sb_error.append("[No se envio el correo pendiente de linea " + historial.getLinea() + "]");
					log.info("No envio el detalle de llamadas");
					historial.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPV));
					result=SendBL.updateHistorialIntentosTBol(historial, "No envio el detalle al correo", conectionManager);
					if(!result.getCode().equalsIgnoreCase(Code.OK)){
						log.info("No envio el detalle de llamadas y no actualizo los intentos del historial por "+result.getDescription());
						result.error("No envio el detalle y no actualizo los intentos del historial por "+result.getDescription());
						return result;
					}
					log.info("No envio el detalle de llamadas y actualizo los intentos del historial");
					result.error("No envio detalles y actualizo los intentos del historial");
					return result;

			}
				historial.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPV));	
				historial.setHash((String)result.getData());
				result=SendBL.updateHistorialTBol(historial, conectionManager);
				if (result.getCode().equalsIgnoreCase(Code.ERROR)){
					log.info("No cerro el historial con estado epv");
					result.error("No cerro el historial con estado epv");
					return result;
				}
				historial.setEstado(SysParameter.getProperty(SysParameter.ESTADO_ENV));
				result=SendBL.saveHistorialTBol(historial, conectionManager);
				if (result.getCode().equalsIgnoreCase(Code.ERROR)){
					log.info("No se guardo el historial a env ");
					result.error("No se guardo el historial a env ");
					return result;
				}
				historial.setEstado(SysParameter.getProperty(SysParameter.ESTADO_ENV));
				result=SendBL.updateHistorialTBol(historial, conectionManager);
				if (result.getCode().equalsIgnoreCase(Code.ERROR)){
					log.info("No cerro el historial con estado env");
					result.error("No cerro el historial con estado env");
					return result;
				}

		if (!sb_error.toString().isEmpty()) {
			log.info("Se encontraron errores al intentar enviar los correos.");
			result.error(sb_error.toString());
			return result;
		}

		log.info("Los correos se enviaron satisfactoriamente.");

		result.ok("Los correos se enviaron satisfactoriamente.");
		return result;
	}

	/**
	 * Metodo que realiza el proceso de envio de un correo dependiendo el tipo de detalle.
	 * 
	 * @param correo
	 * @param detalle
	 * @param conectionManager
	 * @return devuleve true si el envio es satisfacoriamente y false caso contrario.
	 */
	private static Result procesarEnvio(TBol_Historial historial,List<String> correo, ConectionManager conectionManager) {
		log.info("Vamos a procesar a enviar al correo el detalle de llamadas de la linea "+historial.getLinea());
		Result result= new Result();
		try {
		String pathDetalle = StringUtil.EMPTY;
		pathDetalle = SysParameter.getProperty(SysParameter.PATH_DETALLE_LLAMADAS);	
		pathDetalle = FilenameUtil.getFullPath(pathDetalle) + "detalleT"+historial.getCod_Ticket()+"C"+historial.getContrato()+"L"+historial.getLinea()+".xls";
			if (!FileUtil.exists(pathDetalle)) {
				result=sendEmail2(historial,correo,conectionManager);
				if(!result.getCode().equalsIgnoreCase(Code.OK)){
					log.info("No se envio el correo con 0 detalle de llamadas");
					result.error("No se envio el correo con 0 detalle de llamadas");
					return result;
				}
				log.info("Se envio el correo");
				result.ok("Se envio el correo");
				result.setData(result.getData());
				return result;
			}
			result=sendEmail(historial,correo, pathDetalle, conectionManager);
			if(!result.getCode().equalsIgnoreCase(Code.OK)){
				log.info("No se envio el correo");
				result.error("No se envio el correo");
				return result;
			}
				log.info("Se envio el correo");
				result.ok("Se envio el correo");
				result.setData(result.getData());
				return result;
		} catch (Exception e) {
			// TODO: handle exception
			result.error("No se Proceso el envio");
			return result; 
		}
		
	}

	private static Result sendEmail2(TBol_Historial historial,List<String> correo, ConectionManager conectionManager) {
		log.info("Se va enviar al correo "+correo );
		Result result= new Result();
		try {		
			
			String mensaje=SysParameter.getProperty(SysParameter.TEMPLATE_ENVIO);
			if(historial.getPeriodicidad().equalsIgnoreCase(SysParameter.getProperty(SysParameter.TIPO_PERIODICIDAD_N))){
				mensaje=mensaje.replace("{LINEA}",historial.getLinea());
				mensaje=mensaje.replace("{CONTRATO}",historial.getContrato());
				mensaje=mensaje.replace("{TS}","No es periodico");
				mensaje=mensaje.replace("{FI}",historial.getFecha_Inicial().toString());
				mensaje=mensaje.replace("{FF}",historial.getFecha_Final().toString());
				mensaje=mensaje.replace("{TICKET}",historial.getCod_Ticket());
				mensaje=mensaje.replace("{HASH}","No se encontraron registros de detalles de trafico para la informacion solicitada");
			}else{
				if(historial.getPeriodicidad().equalsIgnoreCase(SysParameter.getProperty(SysParameter.TIPO_PERIODICIDAD_S))){
					mensaje=mensaje.replace("{LINEA}",historial.getLinea());
					mensaje=mensaje.replace("{CONTRATO}",historial.getContrato());
					mensaje=mensaje.replace("{TS}",historial.getTipo_solicitud());
					mensaje=mensaje.replace("{FI}",historial.getFecha_Inicial().toString());
					mensaje=mensaje.replace("{FF}",historial.getFecha_Final().toString());
					mensaje=mensaje.replace("{TICKET}",historial.getCod_Ticket());
					mensaje=mensaje.replace("{HASH}","No se encontraron registros de detalles de trafico para la informacion solicitada");
				}
			}
						
			//Mail mail = MailManager.getMail2("Detalle de llamadas de la linea "+historial.getLinea(),"No se encontraron registros de detalles de llamadas para la informacion solicitada", correo);
			Mail mail = MailManager.getMail2("Detalle de llamadas de la linea "+historial.getLinea(),mensaje, correo);
			result=MailManager.sendEmailDetalle2(mail);
			
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				log.info("No se envio el correo " + correo);
				result.error("No se envio el correo " + correo);
				return result;
			}
			log.info("se envio el correo " + correo);
			result.ok("se envio el correo " + correo);
			return result;
			
			
		} catch (Exception e) {
			// TODO: handle exception
			log.info("No se envio el correo " + correo);
			result.error("No se envio el correo " + correo);
			return result;
		}

	}
	
	
	/**
	 * Metodo que envia el correo con el adjunto del archivo del detalle.
	 * 
	 * @param correo
	 * @param pathDetalle
	 * @param conectionManager
	 * @return retorna true si se envio satisfactoriamente y false en caso contrario.
	 */
	
	private static Result sendEmail(TBol_Historial historial,List<String> correo, String pathDetalle, ConectionManager conectionManager) {
		log.info("Se va enviar al correo "+correo );
		String mensaje,hash="";
		
	//	try {
			hash=DigestUtil.generarHash(pathDetalle);
			mensaje=SysParameter.getProperty(SysParameter.TEMPLATE_ENVIO);

			if(historial.getPeriodicidad().equalsIgnoreCase(SysParameter.getProperty(SysParameter.TIPO_PERIODICIDAD_N))){
				mensaje=mensaje.replace("{LINEA}",historial.getLinea());
				mensaje=mensaje.replace("{CONTRATO}",historial.getContrato());
				mensaje=mensaje.replace("{TS}","No es periodico");
				mensaje=mensaje.replace("{FI}",historial.getFecha_Inicial().toString());
				mensaje=mensaje.replace("{FF}",historial.getFecha_Final().toString());
				mensaje=mensaje.replace("{TICKET}",historial.getCod_Ticket());
				mensaje=mensaje.replace("{HASH}",hash);
			}else{
				if(historial.getPeriodicidad().equalsIgnoreCase(SysParameter.getProperty(SysParameter.TIPO_PERIODICIDAD_S))){
					mensaje=mensaje.replace("{LINEA}",historial.getLinea());
					mensaje=mensaje.replace("{CONTRATO}",historial.getContrato());
					mensaje=mensaje.replace("{TS}",historial.getTipo_solicitud());
					mensaje=mensaje.replace("{FI}",historial.getFecha_Inicial().toString());
					mensaje=mensaje.replace("{FF}",historial.getFecha_Final().toString());
					mensaje=mensaje.replace("{TICKET}",historial.getCod_Ticket());
					mensaje=mensaje.replace("{HASH}",hash);
				}
			}

			Mail mail = MailManager.getMail(pathDetalle,"Detalle de llamadas de la linea "+historial.getLinea(),mensaje, correo);
			Result result=MailManager.sendEmailDetalle(mail);
			
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				log.info("No se envio el correo " + correo);
				result.error("No se envio el correo " + correo);
				return result;
			}
			log.info("se envio el correo " + correo);
			result.ok("se envio el correo " + correo);
			result.setData(hash);
			return result;
			
			
	/*	} catch (JmsJobException e) {
			// TODO: handle exception
			log.info("No se envio el correo " + correo);
			result.error("No se envio el correo " + correo);
			return result;
		}
*/
	}


	
}
