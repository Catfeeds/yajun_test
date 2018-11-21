package com.wis.mes.opc.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

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
import com.wis.basis.common.web.model.JsonActionResult;

import net.sf.json.JSONObject;

public class HttpClientUtils extends Thread{
	private static Log logger = LogFactory.getLog(LogConstant.OPC_LOG);
	private final static String mesBaseUrl = OpcPropertiesConfig.getPropertyByKey("opc.mes.baseUrl");

	private static JsonActionResult getJsonActionResult() {
		JsonActionResult result = new JsonActionResult();
		result.setSuccess(true);
		result.setMessage("success");
		return result;
	}

	public static JsonActionResult doGetWithParam1(String url, Map<String, String> param) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JsonActionResult result = getJsonActionResult();
		// 创建一个uri对象
		URIBuilder uriBuilder;
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
				result = (JsonActionResult) JSONObject
						.toBean(JSONObject.fromObject(EntityUtils.toString(entity, "utf-8")), JsonActionResult.class);
			} else {
				result.setSuccess(false);
				result.setMessage("请求失败" + ":statusCode:" + statusCode);
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
}