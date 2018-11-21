/**
 * 2013-5-17 下午2:16:14
 */
package com.wis.basis.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.HttpRequestDeviceUtils;
import com.wis.basis.common.utils.SystemConfig;
import com.wis.basis.systemadmin.entity.User;
import com.wis.basis.systemadmin.service.UserService;
import com.wis.core.context.WebContextHolder;
import com.wis.core.framework.entity.Menu;
import com.wis.core.framework.service.DictTypeService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.MenuService;

//			         _ooOoo_ 
//                  o8888888o 
//                  88" . "88 
//                  (| -_- |) 
//                   O\ = /O 
//               ____/`---*\____ 
//               . * \\| |// `. 
//              / \\||| : |||// \ 
//            / _||||| -:- |||||- \ 
//              | | \\\ - /// | | 
//            | \_| **\---/** | | 
//             \ .-\__ `-` ___/-. / 
//          ___`. .* /--.--\ `. . __ 
//       ."" *< `.___\_<|>_/___.* >*"". 
//      | | : `- \`.;`\ _ /`;.`/ - ` : | | 
//        \ \ `-. \_ __\ /__ _/ .-` / / 
//======`-.____`-.___\_____/___.-`____.-*====== 
// 				     `=---=* 
//............................................. 
//                 佛祖保佑   永无BUG 
//		                        佛曰: 
//		     写字楼里写字间，写字间里程序员； 
//        程序人员写程序，又拿程序换酒钱。 
//        酒醒只在网上坐，酒醉还来网下眠； 
//        酒醉酒醒日复日，网上网下年复年。 
//        但愿老死电脑间，不愿鞠躬老板前； 
//        奔驰宝马贵者趣，公交自行程序员。 
//        别人笑我忒疯癫，我笑自己命太贱； 
//        不见满街漂亮妹，哪个归得程序员？
@Controller
public class MainController extends BaseController {

	@Autowired
	private MenuService menuService;
	@Autowired
	private DictTypeService typeService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/main")
	public ModelAndView mainInput(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)
			throws ParseException {

		modelMap.addAttribute("userName", WebContextHolder.getCurrentUser().getName());
		modelMap.addAttribute("id", WebContextHolder.getCurrentUser().getId());
		modelMap.addAttribute("menus", menuService.findAllBy(WebContextHolder.getCurrentUser().getId()));
		modelMap.addAttribute("version", SystemConfig.getVersion());
		modelMap.addAttribute("welcomeInfo", getWelcomeInfo());
		modelMap.addAttribute("gender", ((User) WebContextHolder.getCurrentUser()).getGender());
		modelMap.addAttribute("systemName", SystemConfig.getSystemName());
		modelMap.addAttribute("entrys", JSONObject.fromObject(typeService.findPageEntry()).toString());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd E HH:mm");
		Date lastLoginTime = WebContextHolder.getCurrentUser().getLastLoginDate();
		if (lastLoginTime != null) {
			modelMap.addAttribute("lastLoginDate", format.format(lastLoginTime));
		}
		changeLang(request, response);

		if ("true".equalsIgnoreCase(
				globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_PWD_VALIDITY_PERIOD_VERIFY))
				&& verifyPwdOvertime(modelMap)) {
			modelMap.addAttribute("pwdRegExp",
					globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_PWD_REG_EXP));
			return new ModelAndView("main/update_password");
		} else if (ConstantUtils.REQUEST_LANG_EN_US
				.equals(request.getSession().getAttribute(ConstantUtils.REQUEST_LANG_KEY))) {// TODO
																								// Test
																								// Only
			return new ModelAndView("modernMain");
		}
		if (HttpRequestDeviceUtils.isMobileDevice(request)) {
			return new ModelAndView("phone_main/main");
		} else {
			return new ModelAndView("main/main");
		}
	}

	@RequestMapping(value = "/menu")
	public ModelAndView mainInput(HttpServletRequest request, Integer id, ModelMap modelMap) {
		if (id != null) {
			Menu menu = menuService.findMenuById(id);
			request.setAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, id);
			return new ModelAndView("forward:" + menu.getUrl());
		}
		Object lang = request.getSession().getAttribute(ConstantUtils.REQUEST_LANG_KEY);

		return new ModelAndView("main/default_" + lang);
	}

	private String getWelcomeInfo() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.HOUR_OF_DAY) < 12) {
			return "MORNING";
		} else if (calendar.get(Calendar.HOUR_OF_DAY) < 18) {
			return "AFTERNOON";
		}
		return "EVENING";
	}

	private void changeLang(HttpServletRequest request, HttpServletResponse response) {
		Object lang = request.getSession().getAttribute(ConstantUtils.REQUEST_LANG_KEY);
		try {
			if (lang != null) {
				LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
				LocaleEditor localeEditor = new LocaleEditor();
				if (StringUtils.isNotBlank(lang.toString())) {
					localeEditor.setAsText(lang.toString());
					localeResolver.setLocale(request, response, (Locale) localeEditor.getValue());
				}
			} else {
				LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
				LocaleEditor localeEditor = new LocaleEditor();
				localeEditor.setAsText("zh_CN");
				localeResolver.setLocale(request, response, (Locale) localeEditor.getValue());
				request.getSession().setAttribute(ConstantUtils.REQUEST_LANG_KEY, "zh_CN");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * 验证密码超期 , 用户密码有效期为90天（可配置），到达有效期后还未更改的，则登陆后强制修改密码， 并重新登录
	 * 
	 * @throws ParseException
	 */
	public boolean verifyPwdOvertime(ModelMap modelMap) throws ParseException {
		String pwdValidityPeriod = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_PWD_VALIDITY_PERIOD);

		User user = userService.findById(WebContextHolder.getCurrentUser().getId());

		if (StringUtils.isNotEmpty(pwdValidityPeriod)) {
			int validityPeriod = Integer.parseInt(pwdValidityPeriod);
			Date nowTime = new Date();
			Date updateTime = null;
			if (null != user.getPwdUpdateTime()) {
				updateTime = user.getPwdUpdateTime();
			} else {
				updateTime = user.getCreateTime();
			}
			long differDay = daysBetween(updateTime, nowTime);
			if (differDay >= validityPeriod) {
				modelMap.addAttribute("validityDay", validityPeriod + "天");
				return true;
			}
		}
		return false;
	}

	public static long daysBetween(Date smdate, Date bdate) throws ParseException {
		if (null == smdate || null == bdate) {
			return 0;
		}

		return DateUtils.dateDiff(smdate, bdate);
	}
}
