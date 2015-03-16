package micrium.calldetail.bussines;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import micrium.calldetail.dao.TBolCorreoDAO;
import micrium.calldetail.dao.TBolHistorialDAO;
import micrium.calldetail.dao.TBolProgramacionDAO;
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
import micrium.calldetail.utils.NumberUtil;
import micrium.calldetail.utils.StringUtil;

import org.apache.log4j.Logger;
/**
 * @author pedro
 * 
 */
public class SendBL {

	private static final String CRLF = "\n";

	private static final Logger log = Logger.getLogger(SendBL.class);

	public static Result paramValidate() {
		log.info("Vamos a validar los parametros del sistema.");

		Result result = new Result();
		String msg = StringUtil.EMPTY;

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.MAIL_SMTP_HOST))) {
			msg += "Parametro mail.smtp.host Invalido" + CRLF;
		}

		if (!NumberUtil.isNumber(SysParameter.getProperty(SysParameter.MAIL_SMTP_PORT))) {
			msg += "Parametro mail.smtp.port Invalido" + CRLF;
		}

		if (!BooleanUtil.isBoolean(SysParameter.getProperty(SysParameter.MAIL_SMTP_AUTH))) {
			msg += "Parametro mail.smtp.auth Invalido" + CRLF;
		}

		if (!BooleanUtil.isBoolean(SysParameter.getProperty(SysParameter.MAIL_SMTP_STARTTLS_ENABLE))) {
			msg += "Parametro mail.smtp.starttls.enable Invalido" + CRLF;
		}

		if (!BooleanUtil.isBoolean(SysParameter.getProperty(SysParameter.MAIL_SMTP_SSL_ENABLE))) {
			msg += "Parametro mail.smtp.ssl.enable Invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.MAIL_SMTP_USER))) {
			msg += "Parametro mail.smtp.user Invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.MAIL_SMTP_PASSWORD))) {
			msg += "Parametro mail.smtp.password Invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.MAIL_SEPARATOR))) {
			msg += "Parametro mail.separador Invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.EXPRESION_EMAIL))) {
			msg += "Parametro expresion.email Invalido" + CRLF;
		}

	/*	if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.PATH_DETALLE_SAC_PDF))) {
			msg += "Parametro path.detalle-sac-pdf invalido" + CRLF;
		}*/

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.PATH_DETALLE_SAC_XLS))) {
			msg += "Parametro path.detalle-sac-xls invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.PATH_DETALLE_LEGAL_PDF))) {
			msg += "Parametro path.detalle-legal-pdf invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.PATH_DETALLE_LEGAL_XLS))) {
			msg += "Parametro path.detalle-legal-xls invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.PATH_DETALLE_TELEGROUP_PDF))) {
			msg += "Parametro path.detalle-telegroup-total-pdf invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.PATH_DETALLE_TELEGROUP_XLS))) {
			msg += "Parametro path.detalle-telegroup-total-xls invalido" + CRLF;
		}

		if (!msg.isEmpty()) {
			log.info(msg);
			result.error(msg);
			return result;
		}

		log.info("Los parametros son validos.");
		result.ok("Los parametros son correctos.");
		return result;
	}



	
	/*public static Result findCorreoByIdTBol(String codTicket, ConectionManager conectionManager) {
		log.info("Se va obtener el correo de ticket" + codTicket);
		Result result = new Result();
		List<TBol_Correo> correo = TBolCorreoDAO.findTBol(codTicket, conectionManager);
		if (correo.isEmpty()) {
			log.info("No se encontro el correo de ticket " + codTicket);
			result.error("No se encontro el correo de ticket " + codTicket);
			return result;
		}

		log.info("Se encontro el correo satisfactoriamente con id " + codTicket);
		result.ok("Se encontro el correo satisfactoriamente con id " + codTicket);
		result.setData(correo);
		return result;
	}*/
	public static Result findCorreoByIdTBolString(String codTicket, ConectionManager conectionManager) {
		log.info("Se va obtener el correo de ticket" + codTicket);
		Result result = new Result();
		List<String> lista=new ArrayList<String>();
		List<TBol_Correo> correo = TBolCorreoDAO.findTBol(codTicket, conectionManager);	
		if (correo.isEmpty()) {
			log.info("No se encontro el correo de ticket " + codTicket);
			result.error("No se encontro el correo de ticket " + codTicket);
			return result;
		}
		for (TBol_Correo tBol_Correo : correo) {
			lista.add(tBol_Correo.getCorreo());
		}
		log.info("Se encontro el correo satisfactoriamente con id " + codTicket);
		result.ok("Se encontro el correo satisfactoriamente con id " + codTicket);
		result.setData(lista);
		return result;
	}
	public static Result findEPPHistorialTBol(String Cod_Ticket,ConectionManager conectionManager) throws SQLException {
		log.info("Se va obtener las lineas del historial con estado EPP de la programacion "+Cod_Ticket );
		Result result = new Result();
		List<TBol_Historial> historial = TBolHistorialDAO.findEPPHistorialTBol(Cod_Ticket,conectionManager);
		if (historial.isEmpty()) {
			log.info("No se encontro lineas del historial con estado EPP de la programacion  "+Cod_Ticket);
			result.error("No se encontro lineas del historial con estado EPP de la programacion "+Cod_Ticket);
			return result;
		}

		log.info("Se encontro lineas del historial con estado EPP de la programacion   "+Cod_Ticket);
		result.ok("Se encontro lineas del historial con estado EPP de la programacion  "+Cod_Ticket);
		result.setData(historial);
		return result;
	}
	public static Result findEPPProgramacionTBol(ConectionManager conectionManager) throws SQLException {
		log.info("Se va obtener programaciones con estado EPP " );
		Result result = new Result();
		List<TBol_Programacion> programacion = TBolProgramacionDAO.findEPPProgramacionTBol(conectionManager);
		if (programacion.isEmpty()) {
			log.info("No se encontro programaciones con estado EPP");
			result.error("No se encontro programaciones con estado EPP");
			return result;
		}
		log.info("Se encontro historiales con estado EPP  ");
		result.ok("Se encontro historiales con estado EPP");
		result.setData(programacion);
		return result;
	}
	public static Result updateEstado(TBol_Linea linea,ConectionManager conectionManager) {
		log.info("Se va actualizar el estado del historial " + linea.getCod_ticket());
		
		Result result=TBolHistorialDAO.updateEstado(linea,  conectionManager);
		if(result.getCode().equalsIgnoreCase(Code.ERROR)){
			log.info(result.getDescription());
			return result;
		}
		return result;
	}
	public static Result updateHistorialTBol(TBol_Historial linea, ConectionManager conectionManager) {
		log.info("Se va actualizar el historial " + linea.getCod_Ticket());
		
		Result result=TBolHistorialDAO.update(linea, conectionManager);
		if (result.getCode().equalsIgnoreCase(Code.ERROR)) {
			log.info("No se actualizo la historial " + linea.getCod_Ticket());
			return result;
		}
		//log.info("Se actualizo satisfactoriamente el historial con id " + linea.getCod_ticket());
		result.ok("Se actualizo satisfactoriamente el historial con id " + linea.getCod_Ticket());
		return result;
	}
	public static Result updateHistorialIntentosTBol(TBol_Historial historial,String error, ConectionManager conectionManager) {
		log.info("Se va actualizar el historial " + historial.getCod_Ticket()+ " a fallida");
		
		Result result=TBolHistorialDAO.updateIntentos(historial, error, conectionManager);
		if(!result.getCode().equalsIgnoreCase(Code.OK)){
			log.info(result.getDescription());
			return result;
		}
		List<String> res=(List<String>)result.getData();
		if(res.get(0).equalsIgnoreCase("OK")){
			if(res.get(1)!=null){
				if(res.get(1).equalsIgnoreCase("S")){
					List<String> lstCorreo=new ArrayList<String>();
					lstCorreo.add(res.get(2));
					Mail mail = MailManager.getMailIntentos("PROBLEMA TICKET: "+historial.getCod_Ticket(), "", lstCorreo);
					result=MailManager.sendEmailIntentos(mail);
					if (!result.getCode().equalsIgnoreCase(Code.OK)) {
						log.info("No se envio el correo " + res.get(2));
						result.error("No se envio el correo " + res.get(2));
						return result;
					}		
				}
			}	
			result.ok("Se actualizo los intentos del historial "+historial.getCod_Ticket());
		}
		if(res.get(0).equalsIgnoreCase("ERROR")){
			result.error(res.get(3));
		}
		return result;
	}
	public static Result saveHistorialTBol(TBol_Historial linea, ConectionManager conectionManager) {
		log.info("Se va guardar el historial " + linea.getCod_Ticket());
		 
		Result result=TBolHistorialDAO.save(linea, conectionManager);
		if (!result.getCode().equalsIgnoreCase(Code.OK)) {
			log.info(result.getDescription());
			return result;
		}
		log.info("Se guardo satisfactoriamente el historial con id " + linea.getCod_Ticket());
		result.ok("Se guardo satisfactoriamente la historial con id " + linea.getCod_Ticket());
		return result;
	}

	
}
