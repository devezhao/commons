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
package cn.devezhao.commons.encoder;

/**
 * FROM: <tt>org.bouncycastle.util.encoders.UrlBase64Encoder</tt>
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: UrlBase64Encoder.java 78 2012-01-17 08:05:10Z
 *          zhaofang123@gmail.com $
 */
public class UrlBase64Encoder extends Base64Encoder {

	public UrlBase64Encoder() {
		encodingTable[encodingTable.length - 2] = (byte) '-';
		encodingTable[encodingTable.length - 1] = (byte) '_';
		padding = (byte) '.';
		// we must re-create the decoding table with the new encoded values.
		initialiseDecodingTable();
	}
}
