package com.wis.mes.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.wis.basis.common.utils.LogConstant;
import com.wis.basis.common.utils.SystemConfig;
import com.wis.basis.common.web.model.JsonActionResult;

public class HttpClientUtils {
	private static Log logger = LogFactory.getLog(LogConstant.BIZ_LOG);
	private final static String mesBaseUrl = SystemConfig.getPropertyByKey("opc.mes.baseUrl");

	public static JsonActionResult doGetWithParam1(String url, Map<String, String> param) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 创建一个uri对象
		URIBuilder uriBuilder;
		JsonActionResult result = new JsonActionResult();
		result.setSuccess(false);
		result.setMessage("请求失败!");
		try {
			uriBuilder = new URIBuilder(mesBaseUrl + url);
			for (Map.Entry<String, String> entry : param.entrySet()) {
				uriBuilder.addParameter(entry.getKey(), entry.getValue());
			}
			HttpGet get = new HttpGet(uriBuilder.build());
			// 执行请求
			CloseableHttpResponse response = httpClient.execute(get);
			// 取响应的结果
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				result = (JsonActionResult) JSONObject.toBean(JSONObject.fromObject(EntityUtils.toString(entity, "utf-8")),JsonActionResult.class);
			}
			response.close();
			httpClient.close();
		} catch (URISyntaxException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (ClientProtocolException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;

	}
	
	public static JsonActionResult doGetWithParam1(String url) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 创建一个uri对象
		URIBuilder uriBuilder;
		JsonActionResult result = new JsonActionResult();
		result.setSuccess(false);
		result.setMessage("请求失败!");
		try {
			uriBuilder = new URIBuilder(mesBaseUrl + url);
			HttpGet get = new HttpGet(uriBuilder.build());
			// 执行请求
			CloseableHttpResponse response = httpClient.execute(get);
			// 取响应的结果
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				result = (JsonActionResult) JSONObject.toBean(JSONObject.fromObject(EntityUtils.toString(entity, "utf-8")),JsonActionResult.class);
			}
			response.close();
			httpClient.close();
		} catch (URISyntaxException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (ClientProtocolException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;

	}

	/*public static void main(String args[]) throws IOException {
		for (int i = 0; i < 20; i++) {
			new Thread(new Runnable() {
				public void run() {
					Map<String, String> map = new HashMap<String, String>();
					map.put("str", "strstrstrstrstr");
				    HttpClientUtils.doGetWithParam("test.do", map);
					
				}
			}).start();
		}
	}*/
	
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("str", "strstrstrstrstr");
	    HttpClientUtils.doGetWithParam1("test.do", map);
	}
}