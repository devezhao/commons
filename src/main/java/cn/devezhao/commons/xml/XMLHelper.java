package cn.devezhao.commons.xml;

import cn.devezhao.commons.web.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;

/**
 * XML helper
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public class XMLHelper {
	
	private static final FastSAXReader FAST_SAX_READER = new FastSAXReader(100);
	
	/**
	 * @param text
	 * @return
	 */
	public static Document parseText(String text) {
		try {
			return DocumentHelper.parseText(text);
		} catch (DocumentException ex) {
			throw new IllegalArgumentException("Bad XML content: " + text, ex);
		}
	}
	
	/**
	 * @param fileName
	 * @return
	 */
	public static Document createDocument(String fileName) {
		try {
			return createDocument(new FileInputStream(fileName));
		} catch (FileNotFoundException ex) {
			throw new RuntimeException("Could't found specify file: " + fileName, ex);
		}
	}
	
	/**
	 * @param stream
	 * @return
	 */
	public static Document createDocument(InputStream stream) {
		SAXReader saxReader = FAST_SAX_READER.getSAXParser(false);
		try {
			saxReader.setEncoding(WebUtils.ENCODING_DEFAULT);
			return saxReader.read(stream);
		} catch (DocumentException ex) {
			throw new RuntimeException("Could't read XML from InputStream!", ex);
		} finally {
			FAST_SAX_READER.release(saxReader);
			try {
				stream.close();
			} catch (IOException ignore) { }
		}
	}
	
	/**
	 * @param reader
	 * @return
	 */
	public static Document createDocument(Reader reader) {
		return createDocument(reader, WebUtils.ENCODING_DEFAULT);
	}

	/**
	 * @param reader
	 * @param encoding
	 * @return
	 */
	public static Document createDocument(Reader reader, String encoding) {
		return createDocument(reader, encoding, false);
	}
	
	/**
	 * @param reader
	 * @param encoding
	 * @param xxe
	 * @return
	 */
	public static Document createDocument(Reader reader, String encoding, boolean xxe) {
		SAXReader saxReader = FAST_SAX_READER.getSAXParser(xxe);
		try {
			saxReader.setEncoding(encoding);
			return saxReader.read(reader);
		} catch (DocumentException ex) {
			throw new RuntimeException("Could't read XML from InputStream!", ex);
		} finally {
			FAST_SAX_READER.release(saxReader);
			try {
				reader.close();
			} catch (IOException ignore) { }
		}
	}
	
	/**
	 * 获取元素内容
	 * 
	 * @param node
	 * @param xpath
	 * @return
	 */
	public static String getText(Node node, String xpath) {
		if (node == null) {
			return StringUtils.EMPTY;
		}
		if (!StringUtils.isEmpty(xpath)) {
			node = node.selectSingleNode(xpath);
		}
		return node == null ? StringUtils.EMPTY : node.getText();
	}
	
	/**
	 * 获取属性值
	 * 
	 * @param node
	 * @param xpath
	 * @param attrName
	 * @return
	 */
	public static String getAttribute(Node node, String xpath, String attrName) {
		if (node == null) {
			return StringUtils.EMPTY;
		}
		if (!StringUtils.isEmpty(xpath)) {
			node = node.selectSingleNode(xpath);
		}
		return node == null ? StringUtils.EMPTY 
				: StringUtils.defaultIfEmpty(node.valueOf("@" + attrName), StringUtils.EMPTY);
	}
}
