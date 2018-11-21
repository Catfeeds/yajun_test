package com.wis.basis.common.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ConstantUtils;

/**
 * @author yuxg
 *
 */
@Controller
@RequestMapping(value = "/locale")
public class LocaleAction extends BaseController {

    @RequestMapping(value = "/change")
    public void doChange(HttpServletRequest request, HttpServletResponse response) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver == null) {
            throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
        }
        String lang = request.getParameter(ConstantUtils.REQUEST_LANG_KEY);
        if (StringUtils.isNotBlank(lang)) {
            if (lang.equals(ConstantUtils.REQUEST_LANG_ZH_CN)){
                localeResolver.setLocale(request, response, Locale.SIMPLIFIED_CHINESE);
            } else{
                localeResolver.setLocale(request, response, Locale.US);
            }
        	request.getSession().setAttribute(ConstantUtils.REQUEST_LANG_KEY, lang);
        }
    }
}
