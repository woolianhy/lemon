package com.lemon.base.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

/**
 * 
 * <p>
 * httpClient封装类
 * </p>
 * 
 * 
 *
 * @author Chenwanli 2015年7月16日
 */
@Component
public class MyHttpClient {

	private CloseableHttpClient httpclient;
	private static final int CONNECTION_MAX_TOTAL = 200;
	private static final int CONNECTION_MAX_PER_ROUTE = 20;

	private String charset = "UTF-8";

	/**
	 * 构造方法
	 */
	public MyHttpClient() {
		PlainConnectionSocketFactory plainsf = PlainConnectionSocketFactory
				.getSocketFactory();
		SSLContext sslContext = SSLContexts.createSystemDefault();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext, NoopHostnameVerifier.INSTANCE);

		Registry<ConnectionSocketFactory> r = RegistryBuilder
				.<ConnectionSocketFactory> create().register("http", plainsf)
				.register("https", sslsf).build();

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
				r);
		cm.setMaxTotal(CONNECTION_MAX_TOTAL);
		cm.setDefaultMaxPerRoute(CONNECTION_MAX_PER_ROUTE);
		httpclient = HttpClients.custom().setConnectionManager(cm).build();

	}

	private HttpGet setHttpGetHeader(HttpGet httpGet,
			Map<String, Object> headers) {
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue().toString());
			}
		}
		return httpGet;
	}

	public String get(HttpGet httpget) throws IOException {
		CloseableHttpResponse response = httpclient.execute(httpget);
		return this.getResponseBodyAsString(httpget, response);
	}

	public String get(Map<String, Object> headers, String url)
			throws IOException {
		HttpGet httpget = new HttpGet(url);
		httpget = this.setHttpGetHeader(httpget, headers);
		return this.get(httpget);
	}

	private String get(String url, String headerAccept) throws IOException {
		Map<String, Object> headers = this.getCommonHeader();
		return this.get(headers, url);
	}

	@SuppressWarnings("unused")
	public void getJpg(String url, HttpServletResponse rps) {
		Map<String, Object> jpgHeader = getJpgHeader();
		HttpGet httpget = new HttpGet(url);
		httpget = this.setHttpGetHeader(httpget, jpgHeader);
		InputStream instream = null;
		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			ServletOutputStream output = rps.getOutputStream();
			if (entity != null) {
				instream = entity.getContent();
				byte b[] = new byte[1024];
				int j = 0;
				while ((j = instream.read(b)) != -1) {
					output.write(b, 0, j);
				}
				output.flush();
				output.close();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Closing the input stream will trigger connection release
			if (instream != null) {
				try {
					instream.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	public String get(String url) throws IOException {
		return this.get(url, null);
	}

	public String getJson(String url) throws IOException {
		return this.get(url, null);
	}

	private HttpPost setHttpPostHeaderAndParams(HttpPost httpPost,
			Map<String, Object> headers, Map<String, String> params)
			throws UnsupportedEncodingException {
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue().toString());
			}
		}

		//
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				if(entry!=null&&entry.getValue()!=null){
					nvps.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue().toString()));
				}
			}
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, getCharset()));
		return httpPost;
	}

	public String post(HttpPost httppost) throws IOException {
		CloseableHttpResponse response = httpclient.execute(httppost);
		return this.getResponseBodyAsString(httppost, response);
	}

	public String post(Map<String, Object> headers, String url,
			Map<String, String> params) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		this.setHttpPostHeaderAndParams(httpPost, headers, params);
		return this.post(httpPost);
	}

	public String post(String url, Map<String, String> params)
			throws IOException {
		Map<String, Object> headers = this.getCommonHeader();
		// headers.put("Accept", HeaderConstant.WEB_ACCEPT_XML);
		return this.post(headers, url, params);
	}

	public String postJson(String url, Map<String, String> params)
			throws IOException {
		Map<String, Object> headers = this.getCommonHeader();
		// headers.put("Accept", HeaderConstant.WEB_ACCEPT_JSON);
		return this.post(headers, url, params);
	}

	public String post(HttpPost httppost, String str) throws IOException {
		StringEntity reqEntity = new StringEntity(str, getCharset());
		httppost.setEntity(reqEntity);
		CloseableHttpResponse response = httpclient.execute(httppost);
		return this.getResponseBodyAsString(httppost, response);
	}

	public String post(Map<String, Object> headers, String url, String str)
			throws IOException {
		HttpPost httpPost = new HttpPost(url);
		this.setHttpPostHeaderAndParams(httpPost, headers, null);
		return this.post(httpPost, str);
	}

	public String post(String url, String str) throws IOException {
		Map<String, Object> headers = this.getCommonHeader();
		return this.post(headers, url, str);
	}

	public String postJson(String url, String str) throws IOException {
		Map<String, Object> headers = this.getCommonHeader();
		headers.put("Content-Type", HeaderConstant.WEB_CONTENT_TYPE_JSON);
		headers.put("encoding", "utf-8");  
		return this.post(headers, url, str);
	}

	public HttpClient getHttpclient() {
		return httpclient;
	}

	private String getResponseBodyAsString(HttpRequestBase httpRequestBase,
			CloseableHttpResponse response) throws IOException {
		String html = null;
		html = EntityUtils.toString(response.getEntity(), getCharset());
		response.close();
		httpRequestBase.releaseConnection();
		return html;
	}

	private Map<String, Object> getCommonHeader() {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("User-Agent", HeaderConstant.WEB_USER_AGENT);
		headers.put("Accept-Language", HeaderConstant.WEB_ACCEPT_LANGUAGE);
		headers.put("Accept", HeaderConstant.WEB_ACCEPT);
		headers.put("Accept-Encoding", HeaderConstant.WEB_ACCEPT_ENCODING);
		return headers;
	}

	private Map<String, Object> getJpgHeader() {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("Content-Type", "image/jpg");
		return headers;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}