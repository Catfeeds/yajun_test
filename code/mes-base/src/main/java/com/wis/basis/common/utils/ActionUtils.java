package com.wis.basis.common.utils;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.vo.TypeVo;
import com.wis.basis.common.web.model.JqueryActionResult;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.systemadmin.entity.User;
import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;

import net.sf.json.JSONObject;

/**
 * @author vincent
 * 
 */
public class ActionUtils {

	private static Log logger = LogFactory.getLog("systemLogger");

	public static JsonActionResult handleResult(HttpServletResponse response) {
		JsonActionResult result = new JsonActionResult();
		result.setSuccess(true);
		return result;
	}

	public static JsonActionResult handleResult(boolean success, String msg) {
		JsonActionResult result = new JsonActionResult();
		result.setMessage(msg);
		result.setSuccess(success);
		return result;
	}

	public static JsonActionResult handleResult(Object data) {
		JsonActionResult result = new JsonActionResult();
		result.setSuccess(true);
		result.setData(data);
		return result;
	}

	private static void setHeader(HttpServletResponse response) {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
	}

	public static void handleResult(HttpServletResponse response, JSONObject jsonObject) {
		setHeader(response);
		try {
			PrintWriter out = response.getWriter();
			if (!jsonObject.containsKey(ConstantUtils.REQUEST_SUCCESS_KEY)) {
				jsonObject.put(ConstantUtils.REQUEST_SUCCESS_KEY, true);
			}
			out.write(jsonObject.toString());
			out.close();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public static void handleResult(HttpServletResponse response, Object object) {
		setHeader(response);
		try {
			PrintWriter out = response.getWriter();
			out.write(object.toString());
			out.close();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public static JsonActionResult handleListResult(HttpServletResponse response, PageResult<?> pageResult) {
		JqueryActionResult result = new JqueryActionResult();
		result.setSuccess(true);
		result.setRows(pageResult.getContent());
		result.setTotal(pageResult.getTotalCount());
		result.setCurrentPage(pageResult.getCurrentPage());
		result.setTotalPage(pageResult.getTotalPage());
		return result;
	}

	private static ModelAndView generateEntry(ModelMap modelMap, ModelAndView view, TypeVo... typeVos) {
		Map<String, List<DictEntry>> entryMap = WebContextHolder.getUserDetailsImpl().getEntryMap();
		if (entryMap == null) {
			entryMap = new HashMap<String, List<DictEntry>>();
		}
		if (typeVos != null) {
			DictEntryService entryService = WebContextHolder.getWebAppContext().getBean(DictEntryService.class);
			for (TypeVo typeVo : typeVos) {
				if (!entryMap.containsKey(typeVo.typeCode)) {
					entryMap.put(typeVo.typeCode, entryService.getEntryByTypeCode(typeVo.typeCode));
				}
				modelMap.put(typeVo.variableName, entryMap.get(typeVo.typeCode));
			}
		}
		WebContextHolder.getUserDetailsImpl().setEntryMap(entryMap);
		return view;
	}

	public static ModelAndView handleSelectResult(HttpServletRequest request, ModelMap modelMap, ModelAndView view, TypeVo... typeVos) {
		if (request.getParameter(ConstantUtils.REQUEST_CURRENT_PAGE_ID) != null) {
			modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, request.getParameter(ConstantUtils.REQUEST_CURRENT_PAGE_ID));
		}
		if (request.getParameter(ConstantUtils.REQUEST_CUSTOM_PARAMS) != null) {
			modelMap.addAttribute(ConstantUtils.REQUEST_CUSTOM_PARAMS, request.getParameter(ConstantUtils.REQUEST_CUSTOM_PARAMS));
		}
		if (request.getParameter(ConstantUtils.REQUEST_SINGLE_SELECT_KEY) != null) {
			modelMap.addAttribute(ConstantUtils.REQUEST_SINGLE_SELECT_KEY, "true");
		} else {
			modelMap.addAttribute(ConstantUtils.REQUEST_SINGLE_SELECT_KEY, "false");
		}
		return generateEntry(modelMap, view, typeVos);
	}

	public static ModelAndView handleEntryResult(ModelMap modelMap, ModelAndView view, TypeVo... typeVos) {
		return generateEntry(modelMap, view, typeVos);
	}

	/**
	 * <b>Method Function Description:</b><br>
	 * 判断是否为Ajax请求
	 * 
	 * @param request
	 * @return <br>
	 *         <b>Method Logic Description:</b>
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equals("XMLHttpRequest")) {
			return true;
		} else if (StringUtils.equals("XMLHttpRequest", request.getParameter("X-Requested-With"))) {
			return true;
		} else {
			return false;
		}
	}

	public static Object getBean(String name) {
		Object bean = WebContextHolder.getWebAppContext().getBean(name);
		return bean;
	}
	
	public static void addUserDataPermission(QueryCriteria criteria,String parameName) {
		if(null != WebContextHolder.getContext().getAuthentication()) {
			User user = (User) WebContextHolder.getCurrentUser();
			if(!criteria.getQueryCondition().containsKey(parameName)) {
				if(null != user.getTmLineIds()) {
					criteria.getQueryCondition().put(parameName+"IN", user.getTmLineIds());
				}
			}
		}
	}
	
	/***
	 * @author yajun_ren
	 * @date 2018/10/17
	 * @desc 默认添加时间条件
	 * @param arg1 参数名称 , arg2(开始时间提前几个月请输入负数)
	 * **/
	public static void dateConditionDefault(QueryCriteria criteria,String arg1,Integer arg2) {
		if(StringUtils.isNotBlank(arg1) && null != arg2) {
			String timeStart = arg1+"Start";
			String timeEnd = arg1+"End";
			if(!criteria.getQueryCondition().containsKey(timeStart) && !criteria.getQueryCondition().containsKey(timeEnd)) {
				Date date = new Date();
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(date);
				rightNow.add(Calendar.MONTH, arg2);
				criteria.getQueryCondition().put(timeStart,DateUtils.formatDate(rightNow.getTime(),DateUtils.FORMAT_DATETIME_DEFAULT));
				criteria.getQueryCondition().put(timeEnd,DateUtils.formatDate(date,DateUtils.FORMAT_DATETIME_DEFAULT));
			}
		}
	}
	
	public static Map<String,Object>  handleMap(boolean success,Object data,String message) {
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("success", success);
		result.put("data", data);
		result.put("message", message);
		return result;
	}
	
	public static Map<String,Object>  handleMap(Object data) {
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("data", data);
		return result;
	}
}
