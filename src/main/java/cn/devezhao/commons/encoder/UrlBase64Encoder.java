package cn.devezhao.commons.encoder;

/**
 * <tt>org.bouncycastle.util.encoders.UrlBase64Encoder</tt>
 *
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 * @deprecated 历史遗留实现，请使用 JDK 自带的 {@code java.util.Base64.getUrlEncoder()/getUrlDecoder()}（JDK 8+）
 */
@Deprecated
public class UrlBase64Encoder extends Base64Encoder {

	public UrlBase64Encoder() {
		encodingTable[encodingTable.length - 2] = (byte) '-';
		encodingTable[encodingTable.length - 1] = (byte) '_';
		padding = (byte) '.';
		// we must re-create the decoding table with the new encoded values.
		initialiseDecodingTable();
	}
}
