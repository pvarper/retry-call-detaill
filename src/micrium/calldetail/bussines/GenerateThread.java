package micrium.calldetail.bussines;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import micrium.calldetail.dato.ConectionManager;
import micrium.calldetail.model.TBol_Correo;
import micrium.calldetail.model.TBol_Linea;
import micrium.calldetail.result.Code;
import micrium.calldetail.result.Result;
import micrium.calldetail.test.Test;
import micrium.calldetail.utils.DateUtil;
import micrium.calldetail.utils.ExcelUtil;
import micrium.calldetail.utils.FileUtil;
import micrium.calldetail.utils.NumberUtil;
import micrium.calldetail.utils.PDFUtil;
import micrium.calldetail.utils.PrintUtil;
import micrium.calldetail.utils.StringUtil;
import micrium.calldetail.ws.DetalleLlamadaWS;
import micrium.ws.Consolidado;

import org.apache.log4j.Logger;
/**
 * @author pedro
 * 
 */
public class GenerateThread extends Thread {

	//private List<Programacion> lstProgramaciones;
	private List<TBol_Linea> lstLineasContratos;
	private static final Logger log = Logger.getLogger(GenerateThread.class);
	private static int count = 0;
	public static String mensaje_error = StringUtil.EMPTY;
	public static String mensaje_ok = StringUtil.EMPTY;
	private static final String DETAILEMPTY = "DETAILEMPTY";
	
	public GenerateThread(List<TBol_Linea> lineasContratos) {
		this.lstLineasContratos = lineasContratos;
	}

	public void run() {
		log.info("Se inicio el hilo para la generacion de detalle de " + lstLineasContratos.size() + " Lineas");
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

			// Vamos a conectarnos al servicio web para obtener el detalle
			DetalleLlamadaWS detalleLlamadaWS = new DetalleLlamadaWS();
			if (!detalleLlamadaWS.conectarWs()) {
				result.error("[No se ha podido establecer conexion con el servicio web]");
				return result;
			}

			StringBuilder sb_error = new StringBuilder();
			StringBuilder sb_ok = new StringBuilder();
			
			
			//guardar historial
			//si es reintentador, no actualiza y no guarda historial
			/*for (TBol_Linea lineas : lstLineasContratos) {						
					if(lineas.getReintentador()==null){
						lineas.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPR));
						result=GenerateBL.saveHistorialTBol(lineas, conectionManager);
						if (result.getCode().equalsIgnoreCase(Code.ERROR)){
							log.info("No se guardo el historial");
							return result;
						}					
					}
			}*/
			
			// Ciclo que recorre la lista de programacion y hace el proceso de verificar si les toca generar el detalle.
			for (TBol_Linea lineas : lstLineasContratos) {			
		
				// Vamos a obtener el correo
				result = GenerateBL.findCorreoByIdTBol(lineas.getCod_ticket(), conectionManager);
				log.info(result.getDescription());
				if (!result.getCode().equalsIgnoreCase(Code.OK)) {
					sb_error.append("[" + result.getDescription() + ", nro de prog " + lineas.getContrato() + "]");
					return result;
				}
				TBol_Correo correo = (TBol_Correo) result.getData();
				//generar el detalle de llamadas por linea TBOL			
				log.info("Se va a procesar la generacion de detalle de la linea: " + lineas.getLinea());
				result = procesarLineas(lineas, correo, detalleLlamadaWS, conectionManager);
				if (!result.getCode().equalsIgnoreCase(Code.OK)) {
					log.info(result.getDescription() + ", linea con ticket " + lineas.getCod_ticket());
					sb_error.append("[" + result.getDescription() + ", nro de linea " + lineas.getLinea() + "]");
					continue;
				}
			//log.info("SE ACTUALIZO LA LINEA "+linea.getLinea()+" Y EL CONTRATO "+linea.getContrato()+"**");	
				if (result.getDescription().equalsIgnoreCase(DETAILEMPTY)) {
					sb_ok.append("[No hay detalle para generar, nro de prog " + lineas.getLinea() + "]");
				}

				log.info("El proceso de generacion de detalle de la linea con numero " + lineas.getLinea()
						+ " termino correctamente");
				}
				
			if (!sb_error.toString().isEmpty()) {
				result.error(sb_error.toString());
				return result;
			}

			result.ok(sb_ok.toString());
			return result;

		} catch (Exception e) {
			log.error("Se ha producido una excepcion al intentar generar el detalle", e);
			result.error("Se ha producido una excepcion con el mensaje {" + e.getMessage() + "}, al intentar generar el detalle]");
			return result;

		} finally {
			count--;
			log.info("El hilo termino de generar los detalles programados");
		}
	}
	private Result procesarLineas(TBol_Linea linea, TBol_Correo correo, DetalleLlamadaWS detalleLlamadaWS,
			ConectionManager conectionManager) throws SQLException {
		
		Result result = new Result();

		String filenameReport = StringUtil.EMPTY;
		String reportNameHeaderXls = StringUtil.EMPTY;
		String reportNameBodyXls = StringUtil.EMPTY;
		String pathDetalle = StringUtil.EMPTY;
		Consolidado consolidado = null;
		List lstConsolidadoDetalles = null;
		String titleReport = StringUtil.EMPTY;
		
		Map<String, Object> rptParameters = new HashMap<String, Object>();

		String tipoAdjunto = SysParameter.getProperty(SysParameter.TIPO_DOCUMENTO);
		boolean isTipoAdjuntoXls = tipoAdjunto.equalsIgnoreCase(SysParameter.TIPO_DOCUMENTO);
		String particionTamanio = SysParameter.getProperty(SysParameter.PARTICION_TAMANO);
			log.info("Se va obtener datos para generar el detalle de llamadas sac de la programacion con id " + linea.getCod_ticket());		
			result = detalleLlamadaWS.getDetalleLlamadasConsolidadoSacTBol(SysParameter.getProperty(SysParameter.APP_USER), linea.getLinea(), SysParameter.getProperty(SysParameter.CONSOLIDADO_SAC_TITULAR), SysParameter.getProperty(SysParameter.CONSOLIDADO_SAC_REPRESENTANTELEGAL),Boolean.parseBoolean(SysParameter.getProperty(SysParameter.CONSOLIDADO_SAC_ORDENJUDICIAL)),SysParameter.getProperty(SysParameter.CONSOLIDADO_SAC_DOCUMENTO),SysParameter.getProperty(SysParameter.CONSOLIDADO_SAC_NRODOCUMENTO),Integer.valueOf(SysParameter.getProperty(SysParameter.CONSOLIDADO_SAC_TIPODETALLE)),DateUtil.dateToXMLGregorianCalendar(linea.getFecha_Inicial()),DateUtil.dateToXMLGregorianCalendar(linea.getFecha_Final()),SysParameter.getProperty(SysParameter.CONSOLIDADO_SAC_DETALLETRANSACCION));
			String error=result.getDescription();
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				Consolidado con= (Consolidado) result.getData();
				if(con!=null){
					if(con.getDescripcion().equalsIgnoreCase("No existe la cuenta")){
						result.error("No se ha encontrado detalle de llamadas para la consulta, nrocuenta: "+con.getCuenta());
						log.info("CAMBIANDO A ESTADO EPP motivo: "+result.getDescription());
						linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPR));
						result= GenerateBL.updateHistorialTBol(linea, conectionManager);
						if (result.getCode().equalsIgnoreCase(Code.ERROR)){
							log.info("No se pudo actualizar la tabla historial con estado EPR");
							result.error("no se actualizo el historial de la linea"+linea.getLinea()+" Y EL CONTRATO "+linea.getContrato()+"**");
							return result;
						}
						linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPP));
						result= GenerateBL.saveHistorialTBol2(linea,con.getDescripcion(), conectionManager);
						if (result.getCode().equalsIgnoreCase(Code.ERROR)){
							log.info("No se pudo guardar en la tabla historial con estado EPP");
							result.error("no se guardo el historial con estado EPP de la linea "+linea.getLinea()+" Y EL CONTRATO "+linea.getContrato()+"**");
							return result;
						}

						return result;
					}
				}
				log.info("No se obtuvo los datos para generar los detalles");
				//pongo la linea en estado de EPR
				log.info("CAMBIANDO A ESTADO EPR");
				linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPR));
				result=GenerateBL.updateEstado(linea, conectionManager);
				if (result.getCode().equalsIgnoreCase(Code.ERROR)){
					log.info("No se cambio a estado EPR");
					return result;
				}
				if(!con.getDescripcion().equalsIgnoreCase("Se ha sobrepasado la cantidad de peticiones aceptadas. Por favor consulte con el Administrador.")){
					linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPR));
					log.info("actualizando el historial de la linea: "+linea.getLinea()+" con estado EPR en fallida");
					//result=GenerateBL.updateHistorialIntentosTBol(linea, "No se genero el detalle al consumir el servicio", conectionManager);
					result=GenerateBL.updateHistorialIntentosTBol(linea, error, conectionManager);
					if(!result.getCode().equalsIgnoreCase(Code.OK)){
						log.info("No genero el detalle de llamadas y no actualizo los intentos del historial por "+result.getDescription());
						result.error("No genero el detalle y no actualizo los intentos del historial por "+result.getDescription());
						return result;
					}
					log.info("No genero el detalle de llamadas y actualizo los intentos del historial");
					result.error("No genero detalles y actualizo los intentos del historial");
					return result;
				}
				result.error("No genero detalles debido a: "+error+" por lo tanto no incremento el numero de intentos");
				return result;
			}			
			consolidado = (Consolidado) result.getData();
			if (consolidado == null) {
				result.error("El consolidado servicio web es nulo.");
				return result;
			}
			if(consolidado.getListaDetalle().isEmpty()){
				if(consolidado.getListaDetalle().isEmpty()){
					result.error("No se ha encontrado detalle de llamadas para la consulta, nrocuenta: "+consolidado.getCuenta());
					log.info("CAMBIANDO A ESTADO EPR motivo: "+result.getDescription());
					linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPR));
					result= GenerateBL.updateHistorialTBol(linea, conectionManager);
					if (result.getCode().equalsIgnoreCase(Code.ERROR)){
						log.info("No se pudo actualizar la tabla historial con estado EPR");
						result.error("no se actualizo el historial de la linea"+linea.getLinea()+" Y EL CONTRATO "+linea.getContrato()+"**");
						return result;
					}
					linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPP));
					result= GenerateBL.saveHistorialTBol2(linea,consolidado.getDescripcion(), conectionManager);
					if (result.getCode().equalsIgnoreCase(Code.ERROR)){
						log.info("No se pudo guardar en la tabla historial con estado EPP");
						result.error("no se guardo el historial con estado EPP de la linea "+linea.getLinea()+" Y EL CONTRATO "+linea.getContrato()+"**");
						return result;
					}

					return result;
				}
			}
			
			lstConsolidadoDetalles = consolidado.getListaDetalle();
			if (lstConsolidadoDetalles.isEmpty()) {
				result.ok(DETAILEMPTY);
				return result;
			}

			titleReport = SysParameter.getProperty(SysParameter.RPT_DETALLE_SAC_TITLE);
			filenameReport = "rpt_detalle_sac_xls";
			reportNameBodyXls = "rpt_detalle_sac_body_xls";
			reportNameHeaderXls = "rpt_detalle_sac_header_xls";

			pathDetalle = SysParameter.getProperty(SysParameter.PATH_DETALLE_LLAMADAS);
		


		String pathLogo = FileUtil.getRealPath("reports/logo.png");
		rptParameters.put("logo", pathLogo);
		rptParameters.put("titulo", titleReport);
		rptParameters.put("nro_cuenta", linea.getLinea());
		rptParameters.put("fecha_emision", new Date());
		rptParameters.put("periodo_inicio",linea.getFecha_Inicial());
		rptParameters.put("periodo_fin", linea.getFecha_Final());
		rptParameters.put("solicitante", linea.getId_client());
		
		rptParameters.put("codigo_detalle", consolidado.getCodigoDetalle());
		rptParameters.put("disclaimer", consolidado.getNota());

		String uid = "T"+linea.getCod_ticket()+"C"+linea.getContrato()+"L"+linea.getLinea();
		pathDetalle = pathDetalle.replace(SysParameter.PATH_DETALLE_TAG, uid);

		if (!exportarDetalle(linea,conectionManager ,isTipoAdjuntoXls, lstConsolidadoDetalles,rptParameters, filenameReport, pathDetalle)) {
			log.info("No exporto el detalle de llamadas");
			//pongo la linea en estado de EPR
			log.info("CAMBIANDO A ESTADO EPR");
			linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPR));
			result=GenerateBL.updateEstado(linea, conectionManager);
			if (result.getCode().equalsIgnoreCase(Code.ERROR)){
				log.info("No se cambio a estado EPR");
				return result;
			}
			
			linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPR));
				result=GenerateBL.updateHistorialIntentosTBol(linea, "No se pudo exportar el detalle generado", conectionManager);
				if(!result.getCode().equalsIgnoreCase(Code.OK)){
					log.info("No exporto el detalle de llamadas y no actualizo los intentos del historial por "+result.getDescription());
					result.error("No exporto el detalle y no actualizo los intentos de la tabla historial por "+result.getDescription());
					return result;
				}
				log.info("No genero el detalle de llamadas y actualizo los intentos del historial ");
				result.error("No exporto el detalle y actualizo los intentos de la tabla historial");
				return result;

		}
		linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPR));
		result= GenerateBL.updateHistorialTBol(linea, conectionManager);
		if (result.getCode().equalsIgnoreCase(Code.ERROR)){
			log.info("No se pudo actualizar la tabla historial con estado EPR");
			result.error("no se actualizo el historial de la linea"+linea.getLinea()+" Y EL CONTRATO "+linea.getContrato()+"**");
			return result;
		}
		linea.setEstado(SysParameter.getProperty(SysParameter.ESTADO_EPP));
		result= GenerateBL.saveHistorialTBol(linea, conectionManager);
		if (result.getCode().equalsIgnoreCase(Code.ERROR)){
			log.info("No se pudo guardar en la tabla historial con estado EPP");
			result.error("no se guardo el historial con estado EPP de la linea "+linea.getLinea()+" Y EL CONTRATO "+linea.getContrato()+"**");
			return result;
		}
		result = particionarArchivo(isTipoAdjuntoXls, lstConsolidadoDetalles, rptParameters, pathDetalle, particionTamanio, reportNameHeaderXls,
				reportNameBodyXls);
		if (!result.getCode().equalsIgnoreCase(Code.OK)) {
			result.error("No se ha podido particionar el detalle de llamdas con codigo " + consolidado.getCodigoDetalle());
			return result;
		}
		//List<String> lstPathsFiles = (List<String>) result.getData();
		result.ok("Termino el proceso de generacion del detalle de la programacion con id " /*+ programacion.getProgramacionId()*/);
		
		return result;

	}

	public static boolean exportarDetalle(TBol_Linea linea,ConectionManager conectionManager,boolean isTipoAdjuntoXls, List<?> lstConsolidadoDetalles,
			Map<String, Object> rptParameters, String filenameReport, String pathDetalle) throws SQLException {
		log.info("Se va exportar el detalle al directorio " + pathDetalle);
		boolean result = Boolean.FALSE;
	//	String fileName = FilenameUtil.getName(pathDetalle);

		PrintUtil printUtils = new PrintUtil();
		
			result = printUtils.exportReportToXlsFile(filenameReport, pathDetalle, rptParameters, lstConsolidadoDetalles);
			if (result) {
				log.info("El detalle de llamadas se exporto al directorio " + pathDetalle);	
		
			}
		log.info("Termino la exportacion del detalle al directorio " + pathDetalle);
		return result;
	}

	public Result particionarArchivo(boolean isTipoAdjuntoPdf, List<?> lstConsolidadoDetalles, Map<String, Object> rptParameters, String pathDetalle,
			String particionTamano, String reportNameHeader, String reportNameBody) {
		log.info("Iniciando el particionado del archivo " + pathDetalle + " en tamanios de " + particionTamano);
		log.info("sesiones abiertas: "+Test.ses);
		int split_size = NumberUtil.toInt(particionTamano.substring(0, particionTamano.length() - 2));
		int unidad_medida = particionTamano.endsWith("kb") ? PDFUtil.KB : PDFUtil.MB;

		if (isTipoAdjuntoPdf) {
			PDFUtil pdfUtils = new PDFUtil();
			Result result = pdfUtils.splitPDFByWeight(pathDetalle, split_size, unidad_medida);
			log.info("Termino el particionado del archivo " + pathDetalle + " en tamanios de " + particionTamano);
			return result;
		} else {
			ExcelUtil excelUtils = new ExcelUtil();
			Result result = excelUtils.splitExcelByWeight(reportNameHeader, reportNameBody, pathDetalle, rptParameters, lstConsolidadoDetalles,
					split_size, unidad_medida);
			log.info("Termino el particionado del archivo " + pathDetalle + " en tamanios de " + particionTamano);
			return result;
		}
	}
}
