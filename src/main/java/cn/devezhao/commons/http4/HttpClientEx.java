package cn.devezhao.commons.http4;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 05/24/2017
 * @deprecated Use okhttp
 */
@Deprecated
public class HttpClientEx {
	
	private HttpClient httpClient;
	private String encoding;

	public HttpClientEx() {
		this(30 * 1000, "utf-8");
	}
	
	/**
	 * @param timeout
	 * @param encoding
	 */
	public HttpClientEx(int timeout, String encoding) {
		this.encoding = encoding == null ? "utf-8" : encoding;
		
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(100);
		RequestConfig rc = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();
		this.httpClient = HttpClients.custom()
				.setConnectionManager(cm)
				.setDefaultRequestConfig(rc)
				.build();
	}
	
	/**
	 * @return
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	/**
	 * @param url
	 * @return
	 */
	public String get(String url) {
		HttpGet httpGet = new HttpGet(url);
		return execMethod(httpGet);
	}
	
	/**
	 * @param url
	 * @param data
	 * @return
	 */
	public String post(String url, String data) {
		HttpPost httpPost = new HttpPost(url);
		if (data != null) {
			httpPost.setEntity(new StringEntity(data, encoding));
		}
		return execMethod(httpPost);
	}
	
	/**
	 * @param url
	 * @param dataMap
	 * @return
	 */
	public String post(String url, Map<String, Object> dataMap) {
		HttpPost httpPost = new HttpPost(url);
		if (dataMap != null && !dataMap.isEmpty()) {
			List<NameValuePair> params = new ArrayList<>();
			for (Map.Entry<String, Object> e : dataMap.entrySet()) {
				if (e.getValue() != null) {
					params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
				}
			}
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params, encoding));
			} catch (UnsupportedEncodingException e) {
				throw new ExecuteHttpMethodException("设置 POST 参数失败", e);
			}
		}
		return execMethod(httpPost);
	}
	
	/**
	 * @param request
	 * @return
	 */
	public String execMethod(HttpUriRequest request) {
		try {
			HttpResponse resp = httpClient.execute(request);
			String r = null;
			if (resp.getEntity() != null) {
				r = EntityUtils.toString(resp.getEntity(), encoding);
			}
			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new ExecuteHttpMethodException("无效 HTTP 状态: " + resp.getStatusLine() + (r == null ? "" : (":" + r)));
			}
			return r;
		} catch (Exception e) {
			throw new ExecuteHttpMethodException(e);
		}
	}
	
	/**
	 * @param uri
	 * @param timeout
	 * @return
	 */
	public byte[] readBinary(String uri, int timeout) {
		HttpGet get = new HttpGet(uri);
		get.setConfig(RequestConfig.custom().setSocketTimeout(timeout).build());
		
		try {
			HttpResponse resp = httpClient.execute(get);
			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new ExecuteHttpMethodException("无效 HTTP 状态: " + resp.getStatusLine());
			}
			
			return EntityUtils.toByteArray(resp.getEntity());
		} catch (Exception e) {
			throw new ExecuteHttpMethodException(e);
		}
	}
	
	// ---------
	
	private static final HttpClientEx HTTP_CLIENT_EX = new HttpClientEx();
	/**
	 * @return
	 */
	public static HttpClientEx instance() {
		return HTTP_CLIENT_EX;
	}
}
