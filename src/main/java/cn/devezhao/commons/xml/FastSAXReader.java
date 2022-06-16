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
	
	private final Queue<XMLReader> xmlReadres = new ConcurrentLinkedQueue<>();

	/**
	 * @param initial
	 */
	public FastSAXReader(int initial) {
		this.initial = initial;
		doInitAllCache();
	}

	/**
	 * @param xxe
	 * @return
	 */
	public SAXReader getSAXParser(boolean xxe) {
		XMLReader xmlReader = xmlReadres.poll();
		if (xmlReader == null) {
			LOG.warn("No more cache, create new XMLReader ....");
			xmlReader = doCachingOneReader(null);
		}
		
		SAXReader saxReader = new SAXReader(xmlReader);
		saxReader.setMergeAdjacentText(true);
		if (xxe) {
			try {
				saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			} catch (SAXException ignored) {
				// NOOP
			}
		}
		return saxReader;
	}

	/**
	 * @param reader
	 */
	public void release(SAXReader reader) {
		try {
			xmlReadres.add(reader.getXMLReader());
		} catch (SAXException e) {
			LOG.warn("Release SAXReader to cache fail!", e);
		}
	}
	
	/**
	 * Initialize cache
	 */
	private void doInitAllCache() {
		LOG.debug("Initializing " + initial + " SAXReader in cache");
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
			xmlReadres.add(reader);
			
			return reader;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
