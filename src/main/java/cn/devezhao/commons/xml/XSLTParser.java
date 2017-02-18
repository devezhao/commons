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

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentSource;

/**
 * XSLT解析者
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: XSLTParser.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
 */
public class XSLTParser {

	private Transformer transformer;
	
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
