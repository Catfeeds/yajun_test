package com.wis.mes.phone.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.security.MD5Utils;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.systemadmin.dao.UserDao;
import com.wis.basis.systemadmin.entity.User;
import com.wis.basis.systemadmin.service.UserService;
import com.wis.core.framework.service.DictTypeService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.mes.phone.service.PhoneMenuService;

@Controller
@RequestMapping(value="/phoneMain")
public class PhoneMainController extends BaseController{
	
	@Autowired
	private UserService userService;
	@Autowired
	private DictTypeService typeService;
	@Autowired
	private PhoneMenuService phoneMenuService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private UserDao userDao;
	
	
	private static final String CUSTOMER_STATUS = "NORMAL";
	
	
	@ResponseBody
	@RequestMapping(value="/phoneLogin")
	public JsonActionResult phoneLogin(String j_username,String j_password){
		JsonActionResult result = new JsonActionResult();
		String message = "";
		boolean verificationState  = false;
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User eg = new User();
		eg.setAccount(j_username);
		eg.setPassword(MD5Utils.passwordEncoder(j_password, j_username));
		eg.setStatus(CUSTOMER_STATUS);
		User bean= userService.findUniqueByEg(eg);
		if (null != bean) {
			verificationState = true;
			resultMap.put("user", bean);
			resultMap.put("entrys",typeService.findPageEntry());
			resultMap.put("menus",phoneMenuService.findPhoneMenuBy(bean.getId()));
			resultMap.put("phoneButton", phoneMenuService.findPermissionBy(bean.getId()));
			resultMap.put("timeOut",globalConfigurationService.getValueByKey(ConstantUtils.APP_TIME_OUT));
		}else {
			message = "用户名或密码错误";
		}
		result.setSuccess(verificationState);
		result.setMessage(message);
		result.setData(resultMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/resetPwd")
	public JsonActionResult resetPwd(HttpServletResponse response, String oldPassword, String newPassword,Integer userId) {
		User user =userService.findById(userId)  ;
		String password = MD5Utils.passwordEncoder(oldPassword, user.getAccount());
		if (!user.getPassword().equals(password)) {
			return ActionUtils.handleResult(false,"原密码错误");
		}
		// 历史密码累计，用逗号隔开
		String[] historyPwds = StringUtils.split(user.getHistoryPwd(), ",");
		if (StringUtils.isBlank(user.getHistoryPwd())) {
			historyPwds = new String[0];
		}
		List<String> historyPwdsList = new ArrayList<String>(Arrays.asList(historyPwds));
		String delPvpCount = globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_DEL_PVP_COUNT);
		if (StringUtils.isNotBlank(delPvpCount)) {
			int count = Integer.parseInt(delPvpCount) - 1;
			if (historyPwds.length >= count) {
				historyPwdsList.remove(0);
			}
			historyPwdsList.add(password);
		}

		user.setHistoryPwd(StringUtils.join(historyPwdsList, ","));
		user.setPwdUpdateTime(new Date());
		user.setPassword(MD5Utils.passwordEncoder(newPassword, user.getAccount()));
		userDao.update(user);
		return ActionUtils.handleResult(response);
	}

}
