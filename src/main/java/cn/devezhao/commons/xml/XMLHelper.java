package cn.devezhao.commons.xml;

import cn.devezhao.commons.web.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

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
	 * @deprecated 未启用 XXE 防护，请改用 {@link #parseText(String, boolean)}，对不可信输入指定 {@code xxe=true}
	 */
	@Deprecated
	public static Document parseText(String text) {
		try {
			return DocumentHelper.parseText(text);
		} catch (DocumentException ex) {
			throw new IllegalArgumentException("Bad XML content: " + text, ex);
		}
	}
	
	/**
	 * 解析 XML 文本
	 *
	 * @param text
	 * @param xxe 是否启用 XXE 防护（禁止 DTD 声明，阻断外部实体注入）
	 * @return
	 */
	public static Document parseText(String text, boolean xxe) {
		if (!xxe) {
			return parseText(text);
		}
		try {
			SAXReader saxReader = new SAXReader();
			saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			return saxReader.read(new StringReader(text));
		} catch (DocumentException ex) {
			throw new IllegalArgumentException("Bad XML content: " + text, ex);
		} catch (SAXException ex) {
			throw new IllegalArgumentException("Couldn't enable XXE protection!", ex);
		}
	}
	
	/**
	 * @param fileName
	 * @return
	 * @deprecated 未启用 XXE 防护，请改用 {@link #createDocument(String, boolean)}，对不可信输入指定 {@code xxe=true}
	 */
	@Deprecated
	public static Document createDocument(String fileName) {
		return createDocument(fileName, false);
	}
	
	/**
	 * 读取 XML 文件
	 *
	 * @param fileName
	 * @param xxe 是否启用 XXE 防护（禁止 DTD 声明，阻断外部实体注入）
	 * @return
	 */
	public static Document createDocument(String fileName, boolean xxe) {
		try {
			return createDocument(new FileInputStream(fileName), xxe);
		} catch (FileNotFoundException ex) {
			throw new RuntimeException("Couldn't find specified file: " + fileName, ex);
		}
	}
	
	/**
	 * @param stream
	 * @return
	 * @deprecated 未启用 XXE 防护，请改用 {@link #createDocument(InputStream, boolean)}，对不可信输入指定 {@code xxe=true}
	 */
	@Deprecated
	public static Document createDocument(InputStream stream) {
		return createDocument(stream, false);
	}
	
	/**
	 * 从输入流读取 XML
	 *
	 * @param stream
	 * @param xxe 是否启用 XXE 防护（禁止 DTD 声明，阻断外部实体注入）
	 * @return
	 */
	public static Document createDocument(InputStream stream, boolean xxe) {
		SAXReader saxReader = FAST_SAX_READER.getSAXParser(xxe);
		try {
			saxReader.setEncoding(WebUtils.ENCODING_DEFAULT);
			return saxReader.read(stream);
		} catch (DocumentException ex) {
			throw new RuntimeException("Couldn't read XML from InputStream!", ex);
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
	 * @deprecated 未启用 XXE 防护，请改用 {@link #createDocument(Reader, String, boolean)}，对不可信输入指定 {@code xxe=true}
	 */
	@Deprecated
	public static Document createDocument(Reader reader) {
		return createDocument(reader, WebUtils.ENCODING_DEFAULT);
	}
	
	/**
	 * @param reader
	 * @param encoding
	 * @return
	 * @deprecated 未启用 XXE 防护，请改用 {@link #createDocument(Reader, String, boolean)}，对不可信输入指定 {@code xxe=true}
	 */
	@Deprecated
	public static Document createDocument(Reader reader, String encoding) {
		return createDocument(reader, encoding, false);
	}
	
	/**
	 * 从字符流读取 XML
	 *
	 * @param reader
	 * @param encoding
	 * @param xxe 是否启用 XXE 防护（禁止 DTD 声明，阻断外部实体注入）
	 * @return
	 */
	public static Document createDocument(Reader reader, String encoding, boolean xxe) {
		SAXReader saxReader = FAST_SAX_READER.getSAXParser(xxe);
		try {
			saxReader.setEncoding(encoding);
			return saxReader.read(reader);
		} catch (DocumentException ex) {
			throw new RuntimeException("Couldn't read XML from Reader!", ex);
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
