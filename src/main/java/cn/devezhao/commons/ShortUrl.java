package cn.devezhao.commons;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import cn.devezhao.commons.http4.HttpClientEx;

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
	 * 将内容中的 URL 全部换为短链
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
			if (url.startsWith("http://t.cn") || url.startsWith("http://url.cn")
					|| url.startsWith("http://tb.cn") || url.startsWith("http://dwz.cn")) {
				continue;
			}
			
			String sUrl = shortUrl(url);
			content = content.replace(url, sUrl);
		}
		return content;
	}
	
	private static final Random RND = new Random();
	/**
	 * 新浪短链
	 * 
	 * @param url
	 * @return
	 */
	public static String shortUrl(String url) {
		try {
		    // iPhone 新浪微博客户端 APPKEY: 5786724301
			// Weoco.iPhone APPKEY: 82966982
			String to = String.format(
					"http://api.weibo.com/2/short_url/shorten.json?source=%d&url_long=%s",
					RND.nextBoolean() ? 82966982L : 5786724301L,
					CodecUtils.urlEncode(url));
			String result = HttpClientEx.instance().get(to);
			String split[] = result.split("http://t.cn");
			if (split.length != 2) {
				return url;
			}
			String sUrl = split[1].split("url_long")[0].split(",")[0];
			sUrl = "t.cn" + sUrl.replaceAll("\"", EMPTY).replaceAll(",", EMPTY);
			return sUrl;
		} catch (Exception ex) {
			LOG.error("Shortting URL fail: " + url, ex);
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
		HttpPost post = new HttpPost("http://dwz.cn/create.php");
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("url", url));
		try {
			post.setEntity(new UrlEncodedFormEntity(nvp));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		
		String r = HttpClientEx.instance().execMethod(post);
		r = r.replace("\\", EMPTY);
		String[] r_split = r.split("dwz.cn");
		String sUrl = r_split[1].split("\"")[0];
		sUrl = "dwz.cn" + sUrl;
		return sUrl;
	}
}
