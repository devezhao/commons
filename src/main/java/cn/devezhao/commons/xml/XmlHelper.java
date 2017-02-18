/*
 Copyright (C) 2010 QDSS.org
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.devezhao.commons.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import cn.devezhao.commons.web.WebUtils;

/**
 * XML helper
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: XmlHelper.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
 */
public class XmlHelper {
	
	private static final FastSAXReader fastSAXReader = new FastSAXReader(100);
	
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
		SAXReader saxReader = fastSAXReader.getSAXParser();
		
		try {
			saxReader.setEncoding(WebUtils.ENCODING_DEFAULT);
			return saxReader.read(stream);
		} catch (DocumentException ex) {
			throw new RuntimeException("Could't read XML from InputStream!", ex);
		} finally {
			fastSAXReader.release(saxReader);
			
			try {
				stream.close();
			} catch (IOException ioex) {}
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
		SAXReader saxReader = fastSAXReader.getSAXParser();
		
		try {
			saxReader.setEncoding(encoding);
			return saxReader.read(reader);
		} catch (DocumentException ex) {
			throw new RuntimeException("Could't read XML from InputStream!", ex);
		} finally {
			fastSAXReader.release(saxReader);
			
			try {
				reader.close();
			} catch (IOException ioex) {}
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
