package tool.jaxb.toxsd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;

import demo.sample.entity.Twitter;

/**
 * <pre>
 * generateXSD method will create XSD from POJO.
 * </pre>
 * 
 * @author aider
 * 
 */
public class XsdGenerate {

	private static final String PACKAGE_FOR_XSD = "demo.sample.entity";

	@Test
	public void generateXSD() throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(PACKAGE_FOR_XSD);
		try {
			new XsdGenerate().pojoToXSD(context, new Twitter(), System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void pojoToXSD(JAXBContext context, Object pojo, OutputStream out) throws IOException, TransformerException {
		final List<DOMResult> results = new ArrayList<DOMResult>();

		context.generateSchema(new SchemaOutputResolver() {

			@Override
			public Result createOutput(String ns, String file) throws IOException {
				DOMResult result = new DOMResult();
				result.setSystemId(file);
				results.add(result);
				return result;
			}
		});

		DOMResult domResult = results.get(0);
		@SuppressWarnings("restriction")
		com.sun.org.apache.xerces.internal.dom.DocumentImpl doc = (com.sun.org.apache.xerces.internal.dom.DocumentImpl) domResult
				.getNode();

		// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(out);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(source, result);
	}
}
