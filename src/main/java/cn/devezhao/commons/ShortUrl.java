package cn.devezhao.commons;

import cn.devezhao.commons.http4.HttpClientEx;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 * 短链处理
 * 
 * @author Zhao Fangfang
 * @version $Id: ShortUrl.java 87 2015-09-28 14:51:01Z zhaofang123@gmail.com $
 */
@Deprecated
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
		Set<String> urls = new HashSet<>();
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
	
	private static final HttpClientEx HTTP_CLIENT_EX = new HttpClientEx(3 * 1000, "utf-8");
	// http://blog.sina.com.cn/s/blog_9e1ea13a01017y3n.html
	private static final String[] APPKEYS = new String[] { "5786724301", "82966982", "405597125", "3822648575", "2849184197", "2702428363", "211160679", "31641035" };
	private static String LAST_APPKEY = null;
	/**
	 * t.cn
	 * 
	 * @param url
	 * @return
	 */
	public static String shortUrl(String url) {
		String appkey = LAST_APPKEY;
		for (int i = 0; i < 5; i++) {
			if (i > 0 || appkey == null) {
				appkey = APPKEYS[RandomUtils.nextInt(APPKEYS.length)];
			}
			
			try {
				String api = String.format(
						"http://api.weibo.com/2/short_url/shorten.json?source=%s&url_long=%s",
						appkey,
						CodecUtils.urlEncode(url));
				String result = HTTP_CLIENT_EX.get(api);
				String[] split = result.split("http://t.cn");
				if (split.length != 2) {
					return url;
				}
				String sUrl = split[1].split("url_long")[0].split(",")[0];
				sUrl = "t.cn" + sUrl.replaceAll("\"", EMPTY).replaceAll(",", EMPTY);
				LAST_APPKEY = appkey;
				return sUrl;
			} catch (Exception ex) {
				LOG.error("短网址失败: " + url + " << " + appkey + " >> " + ex.getLocalizedMessage());
			}
		}
		return url;
	}
	
	/**
	 * 使用百度 dwz.la 缩短网址
	 * 
	 * @param url
	 * @return
	 */
	@Deprecated
	public static String dwzUrl(String url) {
		HttpPost post = new HttpPost("http://dwz.cn/create.php");
		List<NameValuePair> nvp = new ArrayList<>();
		nvp.add(new BasicNameValuePair("url", url));
		try {
			post.setEntity(new UrlEncodedFormEntity(nvp));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		
		String r = HttpClientEx.instance().execMethod(post);
		r = r.replace("\\", EMPTY);
		String[] rSplit = r.split("dwz.cn");
		String sUrl = rSplit[1].split("\"")[0];
		sUrl = "dwz.cn" + sUrl;
		return sUrl;
	}
	
	/**
	 * dwz.la
	 * 
	 * @param url
	 * @return
	 */
	public static String ft12Url(String url) {
		try {
			String url2 = String.format("http://api.ft12.com/api.php?url=%s", CodecUtils.urlEncode(url));
			return HttpClientEx.instance().get(url2);

		} catch (Exception ex) {
			LOG.error("短网址失败: " + url + " >> " + ex.getLocalizedMessage());
		}
		return url;
	}
}
