package cn.devezhao.commons.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * SAXReader 缓存
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public class FastSAXReader {
	
	private static final Log LOG = LogFactory.getLog(FastSAXReader.class);
	
	private final int initial;
	
	private final Queue<XMLReader> xmlReaders = new ConcurrentLinkedQueue<>();

	/**
	 * @param initial
	 */
	public FastSAXReader(int initial) {
		this.initial = initial;
		doInitAllCache();
	}

	/**
	 * 从缓存池获取 SAXReader
	 *
	 * @param xxe 是否启用 XXE 防护（禁止 DTD 声明，阻断外部实体注入）
	 * @return
	 */
	public SAXReader getSAXParser(boolean xxe) {
		XMLReader xmlReader = xmlReaders.poll();
		if (xmlReader == null) {
			LOG.warn("No more cached XMLReaders, creating a new one");
			xmlReader = doCachingOneReader(null);
		}
		
		SAXReader saxReader = new SAXReader(xmlReader);
		saxReader.setMergeAdjacentText(true);
		try {
			saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", xxe);
		} catch (SAXException ignored) {
			// NOOP
		}
		return saxReader;
	}

	/**
	 * @param reader
	 */
	public void release(SAXReader reader) {
		try {
			xmlReaders.add(reader.getXMLReader());
		} catch (SAXException e) {
			LOG.warn("Failed to release SAXReader to cache!", e);
		}
	}
	
	/**
	 * Initialize cache
	 */
	private void doInitAllCache() {
		LOG.debug("Initializing " + initial + " SAXReaders in cache");
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			
			for (int i = 0; i < initial; i++) {
				doCachingOneReader(factory);
			}
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	private XMLReader doCachingOneReader(SAXParserFactory factory) {
		if (factory == null) {
			factory = SAXParserFactory.newInstance();
		}
		
		try {
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setFeature("http://xml.org/sax/features/validation", false);
			xmlReaders.add(reader);
			
			return reader;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
