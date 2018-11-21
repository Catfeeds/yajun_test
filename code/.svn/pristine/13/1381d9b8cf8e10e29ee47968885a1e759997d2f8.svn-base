package com.wis.basis.common.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.wis.core.utils.ConstantUtil;

public class GetPropertiesMessageUtils {
	public static String getMessage(HttpServletRequest request, String key, Object... args) {
		@SuppressWarnings("deprecation")
		WebApplicationContext ac = RequestContextUtils.getWebApplicationContext(request);

		Locale currentLocale = Locale.SIMPLIFIED_CHINESE;

		String lang = (String) request.getSession().getAttribute(ConstantUtil.REQUEST_LANG_KEY);

		if (ConstantUtils.REQUEST_LANG_EN_US.equals(lang)) {
			currentLocale = Locale.US;
		}

		return ac.getMessage(key, args, currentLocale);
	}
}
