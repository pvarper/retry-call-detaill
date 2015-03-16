package micrium.calldetail.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * @author mario
 * 
 */
public class PrintUtil implements Serializable {

	private static final long serialVersionUID = 2463128673554160121L;

	private static Logger log = Logger.getLogger(PrintUtil.class);

	public static final String PATH_REPORT = "reports/";

	public static final String EXTENSION_REPORT = ".jasper";

	public static final String EXTENSION_REPORT_IMAGE = ".jpg";// jpg, png

	public <E> void printPdf(String nameReport, Map<String, Object> parameters, List<E> beanCollection) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			JasperPrint jasperPrint = createJasperPrint(nameReport, parameters, beanCollection);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();
		} catch (JRException ex) {
			log.error("JRException ", ex);
		} catch (IOException e) {
			log.error("IOException ", e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

	public <E> void printExcel(String nameReport, Map<String, Object> parameters, List<E> beanCollection) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			JasperPrint jasperPrint = createJasperPrint(nameReport, parameters, beanCollection);
			JRXlsxExporter exportador = new JRXlsxExporter();
			exportador.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exportador.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, outputStream);
			exportador.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exportador.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exportador.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exportador.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
			exportador.exportReport();

		} catch (JRException ex) {
			log.error("JRException ", ex);
			throw new Exception("Error print excel", ex);
		} catch (IOException e) {
			log.error("IOException ", e);
			throw new Exception("Error print excel", e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

	public <E> boolean exportReportToPdfFile(String nameReport, String desFileName, Map<String, Object> parameters, List<E> beanCollection) {
		log.info("Exportando el reporte " + nameReport + " a pdf al directorio " + desFileName);
		boolean result = Boolean.FALSE;

		try {
			JasperPrint jasperPrint = createJasperPrint(nameReport, parameters, beanCollection);
			JasperExportManager.exportReportToPdfFile(jasperPrint, desFileName);

			if (FileUtil.exists(desFileName)) {
				log.info("El reporte " + nameReport + " se eporto a pdf satisfactoriamente al directorio " + desFileName);
				result = true;
			}

		} catch (JRException ex) {
			log.error("Error al intentar exportar el reporte " + nameReport + " a pdf al directorio " + desFileName, ex);
		} catch (IOException e) {
			log.error("Error al intentar exportar el reporte " + nameReport + " a pdf al directorio " + desFileName, e);
		}

		return result;
	}

	public <E> boolean exportReportToXlsFile(String nameReport, String desFileName, Map<String, Object> parameters, List<E> beanCollection) {
		log.info("Exportando el reporte " + nameReport + " a xls al directorio " + desFileName);
		boolean result = Boolean.FALSE;

		File destFile = new File(desFileName);
		try {
			JasperPrint jasperPrint = createJasperPrint(nameReport, parameters, beanCollection);
			JRXlsxExporter exportador = new JRXlsxExporter();
			exportador.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exportador.setParameter(JRXlsExporterParameter.OUTPUT_FILE, destFile);
			exportador.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exportador.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exportador.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exportador.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
			//exportador.setParameter(JRXlsExporterParameter.PASSWORD, "aaa");
			exportador.exportReport();

			if (FileUtil.exists(desFileName)) {
				log.info("El reporte " + nameReport + " se eporto a xls satisfactoriamente al directorio " + desFileName);
				result = true;
			}

		} catch (JRException ex) {
			log.error("Error al intentar exportar el reporte " + nameReport + " a xls al directorio " + desFileName, ex);
		} catch (IOException e) {
			log.error("Error al intentar exportar el reporte " + nameReport + " a xls al directorio " + desFileName, e);
		}catch (Exception e){
			log.error("Error al intentar exportar el reporte " + nameReport + " a xls al directorio " + desFileName, e);
		}

		return result;
	}

	public <E> String runReportDirect(String nameReport, Map<String, Object> parameters, List<E> beanCollection) {
		String result = null;
		try {
			JasperPrint jasperPrint = createJasperPrint(nameReport, parameters, beanCollection);
			JasperPrintManager.printReport(jasperPrint, false);
		} catch (JRException ex) {
			log.error("JRException ", ex);
		} catch (IOException e) {
			log.error("IOException ", e);
		}
		return result;
	}

	private <E> JasperPrint createJasperPrint(String nameReport, Map<String, Object> parameters, List<E> beanCollection) throws JRException,
			IOException {

		JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(PATH_REPORT + nameReport + EXTENSION_REPORT);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(beanCollection));

		return jasperPrint;
	}


}
