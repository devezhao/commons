package cn.devezhao.commons;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.devezhao.commons.http.HttpClientExec;

/**
 * 短链处理
 * 
 * @author Zhao Fangfang
 * @version $Id: ShortUrl.java 87 2015-09-28 14:51:01Z zhaoff@wisecrm.com $
 */
public class ShortUrl {
	
	private static final Log LOG = LogFactory.getLog(ShortUrl.class);

	final static Pattern URL_PATTERN = Pattern.compile("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=#,]*)?");
	/**
	 * 将内容中的URL全部换为短链
	 * 
	 * @param content
	 * @return
	 */
	public static String shortUrlWithContent(String content) {
		if (content == null) {
			return null;
		}
		
		Matcher matcher = URL_PATTERN.matcher(content);
		Set<String> urls = new HashSet<String>();
		while (matcher.find()) {
			urls.add(matcher.group());
		}
		if (urls.isEmpty()) {
			return content;
		}
		
		for (String url : urls) {
			if (url.startsWith("http://t.cn") || url.startsWith("http://url.cn")) {
				continue;
			}
			
			String sUrl = shortUrl(url);
			content = content.replace(url, sUrl);
		}
		return content;
	}
	
	/**
	 * 短链
	 * 
	 * @param url
	 * @return
	 */
	public static String shortUrl(String url) {
		try {
		    // iPhone 新浪微博客户端 APPKEY: 5786724301
			// Weoco.iPhone 版 APPKEY:82966982
			String to = String.format(
					"http://api.weibo.com/2/short_url/shorten.json?source=%d&url_long=%s",
					new Random().nextBoolean() ? 82966982L : 5786724301L,
					CodecUtils.urlEncode(url));
			String result = HttpClientExec.getInstance().executeGet(to);
			String split[] = result.split("http://t.cn");
			if (split.length != 2) {
				return url;
			}
			
			String sUrl = split[1].split("url_long")[0].split(",")[0];
			sUrl = "http://t.cn" + sUrl.replaceAll("\"", EMPTY).replaceAll(",", EMPTY);
			return sUrl;
		} catch (Exception ex) {
			LOG.error("short URL fail!", ex);
			return url;
		}
	}
	
	/**
	 * 使用百度 dwz.cn 缩短网址
	 * 
	 * @param url
	 * @return
	 */
	public static String dwzUrl(String url) {
		PostMethod method = new PostMethod("http://dwz.cn/create.php");
		method.addParameter("url", url);
		String r = HttpClientExec.getInstance().executeMethod(method);
		r = r.replace("\\", "");
		
		String[] r_split = r.split("dwz.cn");
		String sUrl = r_split[1].split("\"")[0];
		sUrl = "dwz.cn" + sUrl;
		return sUrl;
	}
}
