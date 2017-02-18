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

import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

/**
 * XSLT解析者工厂
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: XSLTParserFactory.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
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
