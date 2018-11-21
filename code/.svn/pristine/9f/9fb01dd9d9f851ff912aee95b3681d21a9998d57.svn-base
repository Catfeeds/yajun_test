package com.wis.basis.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.context.WebContextHolder;
import com.wis.core.framework.entity.GlobalConfiguration;
import com.wis.core.framework.service.GlobalConfigurationService;

/**
 * @author vincent
 *
 */
public class ActionParameterInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		GlobalConfiguration imageBaseUrlConfig = globalConfigurationService.findGlobeConfigurationByKey(ConstantUtils.SYS_CONFIG_IMG_BASE_URL);
		if (null != imageBaseUrlConfig) {
			request.setAttribute("imagesBaseUrl", imageBaseUrlConfig.getValue());
		}
		request.setAttribute("baseUrl", getBaseLocation(request));
		setFormPermission(request);
		return true;
	}

	private String getBaseLocation(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		return url.substring(0, url.indexOf(request.getServletPath()));
	}

	private void setFormPermission(HttpServletRequest request) {
		try {
			request.setAttribute("formPermissionMap", WebContextHolder.getUserDetailsImpl().getFormPermissionMap());
		} catch (NullPointerException e) {
			// 不带用户的action请求-
		}
	}
}
