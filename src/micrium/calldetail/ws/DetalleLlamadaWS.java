package micrium.calldetail.ws;

import java.net.URL;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;
import micrium.calldetail.bussines.SysParameter;
import micrium.calldetail.result.Result;
import micrium.calldetail.utils.BooleanUtil;
import micrium.calldetail.utils.DateUtil;
import micrium.calldetail.utils.NumberUtil;
import micrium.calldetail.utils.RedUtil;
import micrium.ws.Consolidado;
import micrium.ws.DetalleTransaccionWS;
import micrium.ws.DetalleTransaccionWS_Service;
import org.apache.log4j.Logger;

/**
 * @author mario
 * 
 */
public class DetalleLlamadaWS {

	private static Logger log = Logger.getLogger(DetalleLlamadaWS.class);

	static {
		javax.net.ssl.HttpsURLConnection
				.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
					public boolean verify(String hostname,
							javax.net.ssl.SSLSession sslSession) {
						return true;
					}
				});
	}

	private static final String ESTADO_FINALIZADO = "F";
	public static final String CODIGO_RESPUESTA_OK = "0";
	private static final String ESTADO_PROCESANDO = "P";

	private DetalleTransaccionWS detalleTransaccionWS;

	public DetalleLlamadaWS() {
		detalleTransaccionWS = null;
	}

	public boolean conectarWs() {
		boolean result = false;

		String wsdl = SysParameter.getProperty(SysParameter.WSDL_DETALLE);
		Boolean ssl = BooleanUtil.toBoolean(SysParameter
				.getProperty(SysParameter.WS_DETALLE_SSL));
		String keyStore = SysParameter
				.getProperty(SysParameter.WS_DETALLE_KEYSTORE);
		String ip = SysParameter.getProperty(SysParameter.WS_DETALLE_IP);
		int port = Integer.parseInt(SysParameter
				.getProperty(SysParameter.WS_DETALLE_PUERTO));

		if (ssl) {
			log.info("Estableciendo conexion segura al servicio web con wsdl "
					+ wsdl + ", keystore " + keyStore + ", ip " + ip
					+ ", port " + port);
			System.setProperty("javax.net.ssl.trustStore", keyStore);
			System.setProperty("java.protocol.handler.pkgs", ip + ":" + port);
		} else {
			log.info("Estableciendo conexion al servicio web con wsdl " + wsdl);
		}

		URL url;
		try {
			url = new URL(wsdl);
			DetalleTransaccionWS_Service service = new DetalleTransaccionWS_Service(
					url);
			detalleTransaccionWS = service.getDetalleTransaccionWSPort();
			if (detalleTransaccionWS != null) {
				String versionId = detalleTransaccionWS.versionID();
				log.info("Conexion establecido al servicio web " + wsdl
						+ " con versionId " + versionId);
				result = true;
			} else {
				log.info("El servicio con wsdl "
						+ wsdl
						+ " no se conecto debido a que el service.getDetalleTransaccionWSPort() devolvio nulo.");
				result = false;
			}
		} catch (Exception e) {
			log.error("Error al intantar establecer conexion al servicio web "
					+ wsdl, e);
			result = false;
			detalleTransaccionWS = null;
		}
		return result;

	}

	public Result getDetalleLlamadasConsolidadoSacTBol(String usuarioWindow,
			String nroCuentas, String titular, String representanteLegal,
			boolean ordenJudicial, String documento, String nroDocumento,
			int tipoDetalle, XMLGregorianCalendar fechaInicio,
			XMLGregorianCalendar fechaFin, String detalleTransaccion) {

		Result result = new Result();

		//String ip = RedUtil.getLocalHost();
		String ip = SysParameter.getProperty(SysParameter.IP_DETALLE_LLAMADAS);
		String usuario = SysParameter.getProperty(SysParameter.APP_USER);
		String password = SysParameter.getProperty(SysParameter.APP_PASSWORD);

		log.info("(ip="
				+ ip
				+ ", usuario="
				+ usuario
				+ ", usuariowindows="
				+ usuarioWindow
				+ "): Se va obteniendo el detalle consolidado de sac con la cuenta "
				+ nroCuentas);

		Consolidado consolidado = null;
		
		if (detalleTransaccionWS == null) {
			if (!conectarWs()) {
				result.error("Existe problema al intentar establecer conexión con el servicio web.");
				log.info(result.getDescription());
				result.setData(consolidado);
				return result;
			}
		}

		
		String estado = ESTADO_PROCESANDO;
		String codigo = CODIGO_RESPUESTA_OK;
		String secuencia = "0";

		Date time_inicio = DateUtil.getDate();
		long time_transcurrido = 0;
		long sleep = NumberUtil.toLong(SysParameter
				.getProperty(SysParameter.CONSOLIDADO_SAC_SLEEP));
		long timeout = DateUtil.segundoToMilisegundo(NumberUtil
				.toLong(SysParameter
						.getProperty(SysParameter.CONSOLIDADO_SAC_TIMEOUT)));
		int registros = NumberUtil
				.toInt(SysParameter
						.getProperty(SysParameter.CONSOLIDADO_SAC_CANTIDAD_DETALLE_PAGINA));
		int pagina = 1;

		try {

			log.info("Se va realizar varias interaciones para consultar el detalle del web services para verificar su finalizacion.");
			while (codigo.equalsIgnoreCase(CODIGO_RESPUESTA_OK)
					&& estado.equalsIgnoreCase(ESTADO_PROCESANDO)
					&& (time_transcurrido < timeout)) {

				log.info("Se va intentar obtener el detalle de la primera pagina, resgistros "
						+ registros);
				consolidado = detalleTransaccionWS
						.detalleLlamadasConsolidadoSac(ip, usuario, password,
								usuarioWindow, nroCuentas, secuencia, pagina,
								registros, titular, representanteLegal,
								ordenJudicial, documento, nroDocumento,
								tipoDetalle, fechaInicio, fechaFin,
								detalleTransaccion);
				
				

				secuencia = consolidado.getNroSecuencia();
				estado = consolidado.getEstado();
				codigo = consolidado.getCodigo();

				time_transcurrido = DateUtil.getDate().getTime()
						- time_inicio.getTime();

				Thread.sleep(sleep);

				log.info("Consulta realizado al metodo consolidado con codigo="
						+ codigo + ", secuencia=" + secuencia + ", estado="
						+ estado);
			}

			if (consolidado == null) {
				result.error("No se ha podido obtener el detalle de llamadas el objeto consolidado es null");
				log.info(result.getDescription());
				result.setData(consolidado);
				return result;
			}

			if (!consolidado.getCodigo().equalsIgnoreCase(CODIGO_RESPUESTA_OK)
					|| !consolidado.getEstado().equalsIgnoreCase(
							ESTADO_FINALIZADO)) {

				if (time_transcurrido >= timeout) {
					result.error("Existe problema de timeout al consultar el detalle al servicio web.");
					log.info(result.getDescription());
					result.setData(consolidado);
					return result;
				}

				result.error("No se ha podido obtener el detalle de llamadas, "
						+ consolidado.getDescripcion());
				log.info(result.getDescription());
				result.setData(consolidado);
				return result;
			}

			if (consolidado.getListaDetalle() == null) {
				result.error("No se ha podido obtener el detalle de llamadas, "
						+ consolidado.getDescripcion());
				log.info(result.getDescription());
				result.setData(consolidado);
				return result;
			}

			pagina = 2;
			int total_paginas = consolidado.getTotalPaginas();
			int total_registros = consolidado.getTotalRegistros();

			log.info("Se va obtener por pagina el detalle del web services de un total de paginas "
					+ total_paginas + ", total de registros " + total_registros);
			while (codigo.equalsIgnoreCase(CODIGO_RESPUESTA_OK)
					&& estado.equalsIgnoreCase(ESTADO_FINALIZADO)
					&& pagina <= total_paginas) {

				log.info("Se va obtener el detalle de la pagina " + pagina
						+ ", resgistros " + registros);
				Consolidado consolidado_pag = detalleTransaccionWS
						.detalleLlamadasConsolidadoSac(ip, usuario, password,
								usuarioWindow, nroCuentas, secuencia, pagina,
								registros, titular, representanteLegal,
								ordenJudicial, documento, nroDocumento,
								tipoDetalle, fechaInicio, fechaFin,
								detalleTransaccion);
				

				secuencia = consolidado_pag.getNroSecuencia();
				estado = consolidado_pag.getEstado();
				codigo = consolidado_pag.getCodigo();

				if (!codigo.equalsIgnoreCase(CODIGO_RESPUESTA_OK)
						|| !estado.equalsIgnoreCase(ESTADO_FINALIZADO)) {
					result.error("No se ha podido obtener el detalle de llamadas. Causado por {"
							+ consolidado.getDescripcion() + "}");
					result.setData(consolidado_pag);
					log.info(result.getDescription());
					return result;
				}

				if (consolidado_pag.getListaDetalle() == null
						|| consolidado_pag.getListaDetalle().isEmpty()) {
					log.info("No se ha obtenido detalle de llamadas con la cuenta "
							+ nroCuentas + " en la pagina " + pagina);
					break;
				}

				consolidado.getListaDetalle().addAll(
						consolidado_pag.getListaDetalle());

				log.info("Se ha obtenido "
						+ consolidado_pag.getListaDetalle().size()
						+ " detalles de la pagina " + pagina + ", resgistros "
						+ registros + ", estado " + estado + ", codigo "
						+ codigo);

				pagina++;
			}

			log.info("Se ha obtenido " + consolidado.getListaDetalle().size()
					+ " registros de detalles de la cuenta " + nroCuentas
					+ ", de un total de " + consolidado.getTotalRegistros());

			result.ok("Se ha obtenido " + consolidado.getListaDetalle().size()
					+ " registros de detalles de llamadas.");

			result.setData(consolidado);
			return result;

		} catch (Exception e) {
			log.error(
					"Se ha producido una excepción al intentar consultar el detalle del servicio web",
					e);
			result.error("Se ha producido una excepción con el mensaje {"
					+ e.getMessage() + "}, al intentar consultar el detalle");
			result.setData(consolidado);
			return result;
		}

	}

	
	

	/*public static void main(String[] args) throws MalformedURLException {
		DOMConfigurator.configure("etc" + File.separator + "log4j.xml");
		log.info("Iniciando el proceso de generacion de detalle de llamadas.");
		ConectionManager conectionManager = ConectionManager.getInstance();

		if (!conectionManager.open()) {
			log.info("No se pudo abrir la conexion a la base de datos.");
			return;
		}

		try {
			Result result = SysParameter.load(conectionManager);
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				log.info("No existen parametros del sistema definidos en la base de datos.");
				return;
			}
			DetalleLlamadaWS detalle = new DetalleLlamadaWS();
			// String WS_URL =
			// "http://localhost:8086/DetalleTransaccionWS/DetalleTransaccionWS?wsdl";
			// detalle.consumeWebService(WS_URL, true,
			// "D:/Dasarrollo/Proyectos/code/programming-mail-process/etc/my.keystore",
			// "127.0.0.1", 8443);
			result = detalle.getDetalleLlamadasConsolidadoSacTBol("pedro",
					"75050000", "titular", "representante legal", true, "CI",
					"34234234", 1, DateUtil.dateToXMLGregorianCalendar(DateUtil
							.restarDias(DateUtil.getDate(), 50)), DateUtil
							.dateToXMLGregorianCalendar(DateUtil.restarDias(
									DateUtil.getDate(), 2)),
					"Prueba Desarrollo.");

			Consolidado consolidado = (Consolidado) result.getData();

			for (ConsolidadoDetalle consolidadoDetalle : consolidado
					.getListaDetalle()) {
				System.out.println(consolidadoDetalle.getUnidad());
			}
		} catch (Exception e) {
			log.error(
					"Error al intentar realizar la generacion de detalle de llamadas.",
					e);
		} finally {
			conectionManager.close();
		}
	}*/
}
