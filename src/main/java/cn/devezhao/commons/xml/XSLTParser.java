package cn.devezhao.commons.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentSource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * XSLT 解析者
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public class XSLTParser {

	private final Transformer transformer;
	
	/**
	 * @param transformer
	 */
	protected XSLTParser(Transformer transformer) {
		this.transformer = transformer;
	}

	/**
	 * @return
	 */
	public Transformer getTransformer() {
		return this.transformer;
	}

	/**
	 * @param element
	 * @return
	 */
	public String parse(Element element) {
		return parse( DocumentHelper.createDocument(element) );
	}
	
	/**
	 * @param document
	 * @return
	 */
	public String parse(Document document) {
		StringWriter sw = new StringWriter();
		StreamResult outputTarget = new StreamResult(sw);
		DocumentSource xmlSource = new DocumentSource(document);

		try {
			synchronized (transformer) {
				transformer.transform(xmlSource, outputTarget);
			}
		} catch (TransformerException e) {
			throw new RuntimeException("Transform document error!", e);
		}
		return sw.toString();
	}
}
