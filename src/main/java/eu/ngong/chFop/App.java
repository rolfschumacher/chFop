package eu.ngong.chFop;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

/**
 * comment the purpose of the class!
 */
public class App {

	public App() {
	}

	public static void main(String[] args) {

		try {
			// Setup directories
			File baseDir = new File(".");
			File outDir = new File(baseDir, "out");
			outDir.mkdirs();

			// Setup input and output files
			File xslFOFile = new File(baseDir, "simple.fo");
			File pdfFile = new File(outDir, "output.pdf");

			// Setup FOP factory
			FopFactory fopFactory = FopFactory.newInstance(baseDir.toURI());

			// Setup output stream
			try (OutputStream out = Files.newOutputStream(pdfFile.toPath())) {
				// Setup FOP
				Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

				// Setup Transformer
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer();

				// Setup input for XSLT transformation
				Source src = new StreamSource(xslFOFile);

				// Resulting SAX events (the generated FO) must be piped through to FOP
				Result res = new SAXResult(fop.getDefaultHandler());

				// Start XSLT transformation and FOP processing
				transformer.transform(src, res);
			}

			System.out.println("PDF file generated successfully: " + pdfFile.getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}