package micrium.calldetail.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import micrium.calldetail.result.Result;

import org.apache.log4j.Logger;

public class ExcelUtil {

	public static final int KB = 1024;
	public static final int MB = 1048576;

	private static final Logger log = Logger.getLogger(ExcelUtil.class);

	public <E> Result splitExcelByWeight(String nameReportHeader, String nameReportSplit, String desFileName, Map<String, Object> parameters,
			List<E> beanCollection, int splitSize, int unidadMedida) {
		log.info("Iniciando el particionamiento de documento " + desFileName);

		Result result = new Result();
		List<String> lstFilesPath = new ArrayList<String>();

		float file_size = (float) FileUtil.length(desFileName) / unidadMedida;
		if (file_size <= splitSize) {
			lstFilesPath.add(desFileName);
			result.ok("No fue necesario particionar el archivo " + desFileName);
			result.setData(lstFilesPath);
			return result;
		}

		String outFileHeader = StringUtil.EMPTY;
		try {
			outFileHeader = StringUtil.substringBeforeLast(desFileName, ".") + "-header." + StringUtil.substringAfterLast(desFileName, ".");
			PrintUtil printUtils = new PrintUtil();

			if (!printUtils.exportReportToXlsFile(nameReportHeader, outFileHeader, parameters, null)) {
				log.error("El archivo " + desFileName + " no se particiono, la cabecera " + outFileHeader + " no se exporto.");
				result.error("El archivo " + outFileHeader + " no se exporto.");
				return result;
			}

			file_size = (float) FileUtil.length(outFileHeader) / unidadMedida;

			if (file_size > splitSize) {
				FileUtil.delete(outFileHeader);
				log.info("El archivo " + outFileHeader + " no se particiono, la cabecera del reporte excede el tamanio limitado.");
				result.error("El archivo no se particiono, la cabecera del reporte excede el tamanio limitado.");
				return result;
			}

			int splitCount = (int) (file_size / splitSize) + 1;
			boolean particionar = Boolean.TRUE;
			while (particionar) {
				particionar = Boolean.FALSE;

				List<List<E>> lstParticionados = splitList(beanCollection, splitCount);
				for (List<E> list : lstParticionados) {
					String outFile = StringUtil.substringBeforeLast(desFileName, ".") + "-" + lstFilesPath.size() + "."
							+ StringUtil.substringAfterLast(desFileName, ".");

					if (!printUtils.exportReportToXlsFile(nameReportSplit, outFile, parameters, list)) {
						FileUtil.delete(lstFilesPath);
						FileUtil.delete(outFileHeader);
						log.error("El archivo " + desFileName + " no se particiono, el cuerpo del reporte " + outFileHeader + " no se exporto.");
						result.error("El archivo " + outFile + " no se exporto.");
						return result;
					}

					file_size = (float) FileUtil.length(outFile) / unidadMedida;
					if (file_size > splitSize) {
						FileUtil.delete(lstFilesPath);
						lstFilesPath.clear();
						particionar = Boolean.TRUE;
						break;
					}

					lstFilesPath.add(outFile);
				}

				splitCount++;
			}

			lstFilesPath.add(0, outFileHeader);

			result.ok("El particionamiento se realizo satisfactoriamente en los archivos " + lstFilesPath);
			result.setData(lstFilesPath);

		} catch (Exception e) {
			FileUtil.delete(lstFilesPath);
			FileUtil.delete(outFileHeader);
			result.error("Error al particionar el archivo " + desFileName);
			log.error("Error al particionar el archivo " + desFileName, e);
		} finally {
			FileUtil.delete(desFileName);
			log.info("Termino el particionamiento del archivo " + desFileName);
		}

		return result;
	}

	public <E> List<List<E>> splitList(List<E> beanCollection, int splitCount) {
		List<List<E>> lstResult = new ArrayList<List<E>>();
		if (splitCount > beanCollection.size() || splitCount <= 0) {
			return lstResult;
		}

		int fromIndex = 0;
		int bloque = beanCollection.size() / splitCount;
		for (int i = 0; i < splitCount - 1; i++) {
			List<E> lst = beanCollection.subList(fromIndex, fromIndex + bloque);
			fromIndex += bloque;
			lstResult.add(lst);
		}

		List<E> lst = beanCollection.subList(fromIndex, beanCollection.size());
		lstResult.add(lst);

		return lstResult;
	}

}
