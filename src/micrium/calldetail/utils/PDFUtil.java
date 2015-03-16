package micrium.calldetail.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import micrium.calldetail.result.Result;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PDFUtil {

	public static final int KB = 1024;
	public static final int MB = 1048576;

	private static final Logger log = Logger.getLogger(PDFUtil.class);

	public void pdfCopy(String select_pages, String org_dir, String des_dir) throws Exception {
		PdfReader reader = new PdfReader(org_dir);
		reader.selectPages(select_pages);
		manipulateWithStamper(reader, des_dir);
		reader.close();
	}

	private PdfStamper manipulateWithStamper(PdfReader reader, String outFile) throws Exception {
		return new PdfStamper(reader, new FileOutputStream(outFile));
	}

	public PdfCopy pdfCopy(PdfReader reader, String outFile) throws Exception {
		int n = reader.getNumberOfPages();
		Document document = new Document();
		PdfCopy copy = new PdfCopy(document, new FileOutputStream(outFile));
		document.open();
		for (int i = 0; i < n;) {
			copy.addPage(copy.getImportedPage(reader, ++i));
		}
		document.close();
		return copy;
	}

	public Result splitPDFByWeight(String inFile, int splitSize, int unidadMedida) {
		log.info("Iniciando el particionamiento de documento " + inFile);

		Result result = new Result();
		List<String> lstFilesPath = new ArrayList<String>();
		String pathname_size = StringUtil.EMPTY;

		float file_size = (float) FileUtil.length(inFile) / unidadMedida;
		if (file_size <= splitSize) {
			lstFilesPath.add(inFile);
			result.ok("No fue necesario particionar el archivo " + inFile);
			result.setData(lstFilesPath);
			return result;
		}

		try {
			int split_size_byte = (splitSize * unidadMedida);

			// se le resta 4kb por margen de error
			int error_magin = split_size_byte / MB;

			if (error_magin <= 0) {
				error_magin = 1;
			}

			split_size_byte -= (error_magin * 4) * KB;

			PdfReader reader = new PdfReader(inFile);

			int number_of_pages = reader.getNumberOfPages();

			PdfCopy copy = null;
			Document document = null;
			PdfCopy copy_size = null;
			Document document_size = null;
			pathname_size = StringUtil.substringBeforeLast(inFile, ".") + "-size.pdf";

			int i = 1;
			boolean new_split = true;
			boolean add_page = false;

			while (i <= number_of_pages) {

				if (new_split) {
					// Se crea el archivo pdf split
					String outFile = StringUtil.substringBeforeLast(inFile, ".") + "-" + lstFilesPath.size() + ".pdf";
					document = new Document(reader.getPageSizeWithRotation(1));
					copy = new PdfCopy(document, new FileOutputStream(outFile));
					document.open();
					// System.out.println("Writing " + outFile);

					// Se crea el archivo pdf size
					document_size = new Document(reader.getPageSizeWithRotation(1));
					copy_size = new PdfCopy(document_size, new FileOutputStream(pathname_size));
					document_size.open();

					// System.out.println("Writing " + outFile);
					new_split = false;
					add_page = false;
					// System.out.println(copy.getCurrentDocumentSize());

					lstFilesPath.add(outFile);
				}

				PdfImportedPage page = copy.getImportedPage(reader, i);
				copy_size.addPage(page);
				// float file_size_disk_byte = FileUtils.length(pathname);

				float file_size_disk_byte = copy_size.getCurrentDocumentSize();

				if (file_size_disk_byte <= split_size_byte) {
					copy.addPage(page);
					i++;
					add_page = true;
				}

				if ((file_size_disk_byte > split_size_byte) || (i > number_of_pages)) {
					new_split = true;
					document_size.close();
					copy_size.close();

					if (add_page) {
						document.close();
						copy.close();
					} else {
						FileUtil.delete(lstFilesPath);
						result.error("El archivo exportado " + inFile + " no se particiono, existen paginas que exeden al tamanio limitado.");
						return result;
					}
				}
			}

			result.ok("El particionamiento se realizo satisfactoriamente en los archivos " + lstFilesPath);
			result.setData(lstFilesPath);

		} catch (IOException e) {
			FileUtil.delete(lstFilesPath);
			result.error("Error al particionar el archivo " + inFile);
			log.error("Error al particionar el archivo " + inFile, e);

		} catch (DocumentException e) {
			FileUtil.delete(lstFilesPath);
			result.error("Error al particionar el archivo " + inFile);
			log.error("Error al particionar el archivo " + inFile, e);

		} finally {
			FileUtil.delete(inFile);
			FileUtil.delete(pathname_size);
			log.info("Termino el particionamiento del archivo " + inFile);
		}

		return result;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File miDir = new File(".");
		System.out.println(miDir.getCanonicalPath());
		String curDir = System.getProperty("user.dir");
		System.out.println(curDir);

		// String inFile = "resources/doc.pdf";
		// System.out.println(splitPDFByWeight(inFile, 277, "usuario", PDFUtils.KB));
		// splitPDFByPage();
		// pdfCopy("1-2", inFile, inFile);

	}

}
