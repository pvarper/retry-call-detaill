package micrium.calldetail.bussines;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import micrium.calldetail.dao.TBolContratoDAO;
import micrium.calldetail.dao.TBolCorreoDAO;
import micrium.calldetail.dao.TBolHistorialDAO;
import micrium.calldetail.dao.TBolLineaDAO;
import micrium.calldetail.dao.TBolProgramacionDAO;
import micrium.calldetail.dato.ConectionManager;
import micrium.calldetail.mail.Mail;
import micrium.calldetail.mail.MailManager;
import micrium.calldetail.model.TBol_Contrato;
import micrium.calldetail.model.TBol_Correo;
import micrium.calldetail.model.TBol_Historial;
import micrium.calldetail.model.TBol_Linea;
import micrium.calldetail.model.TBol_Programacion;
import micrium.calldetail.result.Code;
import micrium.calldetail.result.Result;
import micrium.calldetail.test.Test;
import micrium.calldetail.utils.BooleanUtil;
import micrium.calldetail.utils.FileUtil;
import micrium.calldetail.utils.NumberUtil;
import micrium.calldetail.utils.StringUtil;
import org.apache.log4j.Logger;
/**
 * @author pedro
 * 
 */
public class GenerateBL {

	private static final String CRLF = "\n";

	private static final Logger log = Logger.getLogger(GenerateBL.class);

	public static Result paramValidate() {
		log.info("Se va validar los parametros del sistema.");

		Result result = new Result();
		String msg = StringUtil.EMPTY;

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.COUNT_THREAD))) {
			msg += "Parametro limit.thread invalido" + CRLF;
		} else {
			if (NumberUtil.toInt(SysParameter.getProperty(SysParameter.COUNT_THREAD)) <= 0) {
				msg += "Parametro limit.thread invalido" + CRLF;
			}
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.EXPRESION_IP))) {
			msg += "Parametro validator.ip invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.WSDL_DETALLE))) {
			msg += "Parametro wsdl.detalle invalido" + CRLF;
		}

		if (!BooleanUtil.isBoolean(SysParameter.getProperty(SysParameter.WS_DETALLE_SSL))) {
			msg += "Parametro ws.detalle.ssl invalido" + CRLF;
		} else {
			if (BooleanUtil.toBoolean(SysParameter.getProperty(SysParameter.WS_DETALLE_SSL))) {

				if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.WS_DETALLE_KEYSTORE))) {
					msg += "Parametro ws.detalle.keysotore Invalido" + CRLF;
				} else {
					if (!FileUtil.exists(SysParameter.getProperty(SysParameter.WS_DETALLE_KEYSTORE))) {
						msg += "El archivo ws.detalle.keysotore no existe en disco" + CRLF;
					}
				}

				if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.WS_DETALLE_IP))) {
					msg += "Parametro ws.detalle.ip invalido" + CRLF;
				}

				if (!NumberUtil.isNumber(SysParameter.getProperty(SysParameter.WS_DETALLE_PUERTO))) {
					msg += "Parametro ws.detalle.puerto invalido" + CRLF;
				}
			}
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
			msg += "Parametro path.detalle-telegroup-pdf invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.PATH_DETALLE_TELEGROUP_XLS))) {
			msg += "Parametro path.detalle-telegroup-xls invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.APP_USER))) {
			msg += "Parametro app.user invalido" + CRLF;
		}

		if (StringUtil.isEmpty(SysParameter.getProperty(SysParameter.APP_PASSWORD))) {
			msg += "Parametro app.password invalido" + CRLF;
		}

		if (!msg.isEmpty()) {
			log.info(msg);
			result.error(msg);
			return result;
		}

		log.info("Los parametros son validos.");
		result.ok("Los parametros son validos.");
		return result;
	}
	
	public static Result countProgramacionesActivosTBOL(ConectionManager conectionManager) throws SQLException {
		log.info("Se va obtener la cantidad de programaciones activas.");
		Result result = new Result();
		log.info("sesiones abiertas: "+Test.ses);
		long count = TBolProgramacionDAO.countActivosTBol(conectionManager);
		log.info("sesiones abiertas: "+Test.ses);
		if (count == 0) {
			log.info("No existen programaciones activas.");
			result.error("No existen programaciones activas.");
			return result;
		}

		log.info("Se encontraron " + count + " programaciones activas.");
		result.ok("Se encontraron " + count + " programaciones activas.");
		result.setData(count);
		return result;
	}
	public static Result findProgramacionesActivosTBol(ConectionManager conectionManager) throws SQLException {
		log.info("Se va obtener la cantidad de programaciones activas.");
		Result result = new Result();
		List<TBol_Programacion> lst = TBolProgramacionDAO.findActivosTBol(conectionManager);
		if (!lst.isEmpty()) {
			result.ok("Programaciones obtenida satisfactoriamente.", lst);
		} else {
			log.info("No existen programaciones activas.");
			result.error("No existen programaciones activas.");
		}
		return result;
	}
	public static Result findProgramacionTBol(String Cod_Ticket,ConectionManager conectionManager) throws SQLException {
		log.info("Se va obtener la programacion"+Cod_Ticket);
		Result result = new Result();
		TBol_Programacion lst = TBolProgramacionDAO.findTBol(Cod_Ticket, conectionManager);
		
		if (lst!=null) {
			result.ok("Programacion obtenida satisfactoriamente con ticket"+Cod_Ticket, lst);
		} else {
			log.info("No existen programacion con ticket:"+Cod_Ticket);
			result.error("No existen programacioncon ticket:"+Cod_Ticket);
		}
		return result;
	}
	public static Result findcontratosProgramacionTBol(String codTicket,ConectionManager conectionManager) throws SQLException {
		log.info("Se va obtener la cantidad de contratos de la programacion: "+codTicket);
		Result result = new Result();
		 List<TBol_Contrato> lst= TBolContratoDAO.findContratosTBol(codTicket, conectionManager);
		if (!lst.isEmpty()) {
			result.setData(lst);
			result.ok("contratos obtenido satisfactoriamente.", lst);
		} else {
			log.info("No existen contratos para esta programacion");
			result.error("No existen contratos para esta programacion");
		}
		return result;
	}
	public static Result findLineasContratos(String contrato,String ticket,ConectionManager conectionManager) throws SQLException {
		log.info("Se va obtener la cantidad de lineas del contrato: "+contrato);	 
		Result result= TBolLineaDAO.findLineasContratos(contrato,ticket, conectionManager);
		if (!result.getCode().equalsIgnoreCase(Code.OK)){
			log.info("no se obtuvieron lineas del contrato"+contrato);
			return result;
		}
		log.info("no se obtuvieron lineas del contrato"+contrato);
		return result;
	}
	/*public static Result CountfindLineasContratos(String contrato,String ticket,ConectionManager conectionManager) throws SQLException {
		log.info("Se va obtener la cantidad de lineas del contrato: "+contrato);
		Result result = new Result();
		result= TBolLineaDAO.findLineasContratos(contrato,ticket, conectionManager);
		if (!result.getCode().equalsIgnoreCase(Code.OK)){			
			return result;
		}
		return result;
	}*/


/*	public static Result updateProgramacionTBol(TBol_Linea linea, ConectionManager conectionManager) {
		log.info("Se va actualizar la programacion " + linea.getCod_ticket());
		Result result = new Result();
		if (!TBolProgramacionDAO.update(linea, conectionManager)) {
			log.info("No se actualizo la programacion " + linea.getCod_ticket());
			result.error("No se actualizo la programacion con id " + linea.getCod_ticket());
			return result;
		}

		log.info("Se actualizo satisfactoriamente la programacion con id " + linea.getCod_ticket());
		result.ok("Se actualizo satisfactoriamente la programacion con id " + linea.getCod_ticket());
		return result;
	}*/
	public static Result updateHistorialTBol(TBol_Linea linea, ConectionManager conectionManager) {
		log.info("Se va actualizar el historial " + linea.getCod_ticket());
		Result result=TBolHistorialDAO.update(linea, conectionManager);
		if (result.getCode().equalsIgnoreCase(Code.ERROR)) {
			log.info("No se actualizo la historial " + linea.getCod_ticket());
			return result;
		}
		//log.info("Se actualizo satisfactoriamente el historial con id " + linea.getCod_ticket());
		result.ok("Se actualizo satisfactoriamente el historial con id " + linea.getCod_ticket());
		return result;
	}
	public static Result updateHistorialIntentosTBol(TBol_Linea linea,String error, ConectionManager conectionManager) {
		log.info("Se va actualizar el historial " + linea.getCod_ticket()+ " a fallida");
		 Result result=TBolHistorialDAO.updateIntentos(linea, error, conectionManager);
		if(result.getCode().equalsIgnoreCase(Code.ERROR)){	
			log.info(result.getDescription());
			return result;		
		}
		List<String> res=(List<String>)result.getData();
		if(res.get(0).equalsIgnoreCase("OK")){
			if(res.get(1)!=null){
				if(res.get(1).equalsIgnoreCase("S")){
					List<String> lstCorreo=new ArrayList<String>();
					lstCorreo.add(res.get(2));
					Mail mail = MailManager.getMailIntentos("PROBLEMA TICKET: "+linea.getCod_ticket(), "se coloco en estado manual", lstCorreo);
					result=MailManager.sendEmailIntentos(mail);
					if (!result.getCode().equalsIgnoreCase(Code.OK)) {
						log.info("No se envio el correo " + res.get(2));
						result.error("No se envio el correo " + res.get(2));
						return result;
					}		
				}			
			}	
			result.ok("Se actualizo los intentos del historial "+linea.getCod_ticket());
		}
		if(res.get(0).equalsIgnoreCase("ERROR")){
			result.error(res.get(3));
		}
		//log.info("Se actualizo satisfactoriamente el historial con id " + linea.getCod_ticket());
		//result.ok("Se actualizo satisfactoriamente el historial con id " + linea.getCod_ticket());
		return result;
	}
	public static Result updateEstado(TBol_Linea linea,ConectionManager conectionManager) {
		log.info("Se va actualizar el estado del historial " + linea.getCod_ticket());
		 Result result=TBolHistorialDAO.updateEstado(linea,  conectionManager);
		if(result.getCode().equalsIgnoreCase(Code.ERROR)){
			return result;
		}
		return result;
	}
	public static Result saveHistorialTBol(TBol_Linea linea, ConectionManager conectionManager) {
		log.info("Se va registrar el historial " + linea.getCod_ticket());
		Result result=TBolHistorialDAO.save(linea, conectionManager);;
		if (!result.getCode().equalsIgnoreCase(Code.OK)) {
			log.info("No se registro el historial " + linea.getCod_ticket());
			return result;
		}
		result.ok("Se registro satisfactoriamente la historial con id " + linea.getCod_ticket());
		return result;
	}
	public static Result saveHistorialTBol2(TBol_Linea linea,String error, ConectionManager conectionManager) {
		log.info("Se va registrar el historial " + linea.getCod_ticket());
		Result result=TBolHistorialDAO.save2(linea,error, conectionManager);;
		if (!result.getCode().equalsIgnoreCase(Code.OK)) {
			log.info("No se registro el historial " + linea.getCod_ticket());
			return result;
		}
		result.ok("Se registro satisfactoriamente la historial con id " + linea.getCod_ticket());
		return result;
	}

	public static Result findCorreoByIdTBol(String codTicket, ConectionManager conectionManager) {
		log.info("Se va obtener el correo con id " + codTicket);
		Result result = new Result();
		TBol_Correo correo = TBolCorreoDAO.find(codTicket, conectionManager);
		if (correo == null) {
			log.info("No se encontro el correo con id " + codTicket);
			result.error("No se encontro el correo con id " + codTicket);
			return result;
		}

		log.info("Se encontro el correo satisfactoriamente con id " + codTicket);
		result.ok("Se encontro el correo satisfactoriamente con id " + codTicket);
		result.setData(correo);
		return result;
	}
	public static Result findEPRTBol(ConectionManager conectionManager) {
		log.info("Se va obtener historiales con estado EPR ");
		Result result = new Result();
		List<TBol_Historial> historial = TBolHistorialDAO.findEPRTBol(conectionManager);
		if (historial.isEmpty()) {
			log.info("No se encontro historiales con estado EPR");
			result.error("No se encontro historiales con estado EPR ");
			return result;
		}

		log.info("Se encontro historiales con estado EPR");
		result.ok("Se encontro historiales con estado EPR");
		result.setData(historial);
		return result;
	}
	public static Result findEPVTBol(ConectionManager conectionManager) {
		log.info("Se va obtener historiales con estado EPV ");
		Result result = new Result();
		List<TBol_Historial> historial = TBolHistorialDAO.findEPVTBol(conectionManager);
		if (historial.isEmpty()) {
			log.info("No se encontro historiales con estado EPV");
			result.error("No se encontro historiales con estado EPV ");
			return result;
		}

		log.info("Se encontro historiales con estado EPV");
		result.ok("Se encontro historiales con estado EPV");
		result.setData(historial);
		return result;
	}


}
