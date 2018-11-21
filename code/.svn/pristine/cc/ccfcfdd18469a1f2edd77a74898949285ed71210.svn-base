package com.wis.basis.common.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LogConstant;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.entity.Removable;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.utils.ConstantUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BaseController {

	protected Log logger = LogFactory.getLog(LogConstant.BIZ_LOG);

	protected Integer[] getIds(String ids) {
		if (StringUtils.isEmpty(ids)) {
			return new Integer[0];
		}
		JSONArray array = JSONArray.fromObject(ids);
		Integer[] result = new Integer[array.size()];
		for (int i = 0; i < array.size(); i++) {
			result[i] = array.getInt(i);
		}
		return result;
	}

	protected String[] getStringIds(String ids) {
		if (StringUtils.isEmpty(ids)) {
			return new String[0];
		}
		JSONArray array = JSONArray.fromObject(ids);
		String[] result = new String[array.size()];
		for (int i = 0; i < array.size(); i++) {
			result[i] = array.getString(i);
		}
		return result;
	}

	protected Object copy(Object source, Object target, String... ignoreProperties) {
		if (ignoreProperties == null || ignoreProperties.length < 1) {
			ignoreProperties = getDefaultIgnore().toArray(new String[0]);
		}
		BeanUtils.copyProperties(source, target, ignoreProperties);
		return target;
	}

	protected List<String> getDefaultIgnore() {
		List<String> result = new ArrayList<String>();
		result.addAll(Arrays.asList(new String[] { Removable.REMOVE_FIELD_NAME, "createUser", "createTime", "updateUser", "updateTime", "optCounter" }));
		return result;
	}

	protected void handelException(Exception ex, HttpServletRequest request, HttpServletResponse response) {

		JsonActionResult jsonActionResult = new JsonActionResult();
		jsonActionResult.setSuccess(false);
		RequestContext requestContext = new RequestContext(request);
		if (ex instanceof BusinessException) {
			BusinessException businessException = (BusinessException) ex;
			jsonActionResult.setMessage(requestContext.getMessage(businessException.getMessage(), businessException.getParams()));
		} else {
			jsonActionResult.setMessage(requestContext.getMessage(ConstantUtils.REQUEST_EXCEPTION_SYSTEM));
		}
		ActionUtils.handleResult(response, JSONObject.fromObject(jsonActionResult));

	}

	protected Integer[] getRptIds(String ids) {
		if (StringUtils.isEmpty(ids)) {
			return null;
		}
		JSONArray array = JSONArray.fromObject(ids);
		Integer[] result = new Integer[array.size()];
		for (int i = 0; i < array.size(); i++) {
			result[i] = array.getInt(i);
		}
		return result;
	}

	protected String[] getRptStringIds(String ids) {
		if (StringUtils.isEmpty(ids)) {
			return null;
		}
		JSONArray array = JSONArray.fromObject(ids);
		String[] result = new String[array.size()];
		for (int i = 0; i < array.size(); i++) {
			result[i] = array.getString(i);
		}
		return result;
	}

	protected Object getObj(Object object) {
		if (StringUtils.isEmpty(object)) {
			return null;
		}
		return object;
	}

	protected String getString(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return str;
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param request
	 * @param key
	 *            CODE
	 * @param args
	 *            参数{0}，{1}
	 * @return
	 */
	protected String getMessage(HttpServletRequest request, String key, Object... args) {
		WebApplicationContext ac = RequestContextUtils.getWebApplicationContext(request);

		Locale currentLocale = Locale.SIMPLIFIED_CHINESE;

		String lang = (String) request.getSession().getAttribute(ConstantUtil.REQUEST_LANG_KEY);

		if (ConstantUtils.REQUEST_LANG_EN_US.equals(lang)) {
			currentLocale = Locale.US;
		}

		return ac.getMessage(key, args, currentLocale);
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param request
	 * @param keys
	 *            CODES
	 * @param args
	 *            参数{0}，{1}
	 * @return
	 */
	protected List<String> getMessage(HttpServletRequest request, String[] keys, Object... args) {
		List<String> list = new ArrayList<String>();

		for (String key : keys) {

			WebApplicationContext ac = RequestContextUtils.getWebApplicationContext(request);

			Locale currentLocale = Locale.SIMPLIFIED_CHINESE;

			String lang = (String) request.getSession().getAttribute(ConstantUtil.REQUEST_LANG_KEY);

			if (ConstantUtils.REQUEST_LANG_EN_US.equals(lang)) {
				currentLocale = Locale.US;
			}

			list.add(ac.getMessage(key, args, currentLocale));
		}

		return list;
	}

	// /**
	// *
	// * @param request
	// * @param key
	// * CODE
	// * @param defaultMsg
	// * 默認消息
	// * @param args
	// * 参数{0}，{1}
	// * @return
	// */
	// protected String getMessage(HttpServletRequest request, String key,
	// String defaultMsg, Object... args) {
	// WebApplicationContext ac =
	// RequestContextUtils.getWebApplicationContext(request);
	//
	// Locale currentLocale = getLocale(request);
	//
	// return ac.getMessage(key, args, defaultMsg, currentLocale);
	// }

	/**
	 * 
	 * @param request
	 * @return the base url of the application
	 */
	protected String getBaseLocation(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		return url.substring(0, url.indexOf(request.getServletPath()));
	}

	/**
	 * 
	 * @param request
	 * @return the base deploy folder of the web project
	 */
	protected String getBasePath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("");
	}

	protected Locale getLocale(HttpServletRequest request) {
		Locale currentLocale = Locale.SIMPLIFIED_CHINESE;
		String lang = (String) request.getSession().getAttribute(ConstantUtil.REQUEST_LANG_KEY);
		if (ConstantUtils.REQUEST_LANG_EN_US.equals(lang)) {
			currentLocale = Locale.US;
		}
		return currentLocale;
	}
}
