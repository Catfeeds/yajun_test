package com.wis.basis.common.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LogConstant;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.framework.service.exception.BusinessException;

import net.sf.json.JSONObject;

/**
 * 系统异常处理拦截器，捕获并处理所有系统异常及自定义异常
 */
public class ExceptionInterceptor implements HandlerExceptionResolver {

	private Log logger = LogFactory.getLog(LogConstant.SYS_LOG);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.error(ExceptionUtils.getStackTrace(ex));
		if (request.getHeader("X-Requested-With") != null) {
			
			JsonActionResult jsonActionResult=new JsonActionResult();
			jsonActionResult.setSuccess(false);
			RequestContext requestContext=new RequestContext(request);
			if (ex instanceof BusinessException) {
				BusinessException businessException = (BusinessException) ex;
				jsonActionResult.setMessage(requestContext.getMessage(businessException.getMessage(),businessException.getParams()));
			}else {
				jsonActionResult.setMessage(requestContext.getMessage(ConstantUtils.REQUEST_EXCEPTION_SYSTEM));
			}
			ActionUtils.handleResult(response, JSONObject.fromObject(jsonActionResult));
			return null;
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("ex", ex);
		return new ModelAndView("error/commons/exceptionAll", model);
	}

}
