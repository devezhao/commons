package cn.devezhao.commons.xml;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

/**
 * XSLT 解析者工厂
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public class XSLTParserFactory {

	private static TransformerFactory transformerFactory;
	static {
		new XSLTParserFactory();
	}
	
	private XSLTParserFactory() {
		transformerFactory = TransformerFactory.newInstance();
	}

	/**
	 * @param stream
	 * @return
	 */
	public static XSLTParser newParser(InputStream stream) {
		StreamSource source = new StreamSource(stream);
		Transformer t = newTransformer(source);
		return new XSLTParser(t);
	}

	/**
	 * @param source
	 * @return
	 */
	public static Transformer newTransformer(StreamSource source) {
		try {
			return transformerFactory.newTransformer(source);
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException("Could't create xslt transformer!", e);
		}
	}
}
