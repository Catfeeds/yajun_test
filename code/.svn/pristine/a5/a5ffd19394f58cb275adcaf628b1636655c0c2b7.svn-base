package com.wis.basis.notification.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.notification.entity.Notification;
import com.wis.basis.notification.service.MsgContentService;
import com.wis.basis.notification.service.NotificationService;
import com.wis.basis.notification.vo.MsgContentVo;
import com.wis.core.context.WebContextHolder;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/notification")
public class NotificationController extends BaseController {

	@Autowired
	private NotificationService service;
	@Autowired
	private MsgContentService msgContentService;

	@RequestMapping(value = "/myListInput")
	public String myListInput(HttpServletResponse response, ModelMap modelMap) {
		return "/msg/notification/notification_list";
	}

	@RequestMapping(value = "/myList")
	@ResponseBody
	public JsonActionResult myList(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
	}

	@RequestMapping(value = "/managementListInput")
	public String managementListInput(HttpServletResponse response, ModelMap modelMap) {
		return "/msg/notification/all_list";
	}

	@RequestMapping(value = "/managementList")
	@ResponseBody
	public JsonActionResult managementList(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		return ActionUtils.handleListResult(response, msgContentService.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public String addInput() {
		return "/msg/notification/notification_add";
	}

	@RequestMapping(value = "add")
	@ResponseBody
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, MsgContentVo msgContentVo) {
		System.out.println(msgContentVo.getFile().getName());
		msgContentService.doAdd(msgContentVo);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "countInfo")
	@ResponseBody
	public void countInfo(HttpServletRequest request, HttpServletResponse response) {
		ActionUtils.handleResult(response, service.findCountInfoNoReadBy(WebContextHolder.getCurrentUser().getId()));
	}

	@RequestMapping(value = "viewInput")
	@ResponseBody
	public void viewInput(HttpServletResponse response, Integer id) {
		Notification notification = service.findById(id);
		if (null != notification) {
			notification.setReadFlag(1);
			service.doUpdate(notification);
		}
		Integer msgId = null;
		if (null != notification && null != (msgId = notification.getMsgId())) {
			notification.setMsgContent(msgContentService.findById(msgId));
		}
		ActionUtils.handleResult(response, JSONObject.fromObject(notification));
	}

	@RequestMapping(value = "deleteNotification")
	@ResponseBody
	public JsonActionResult deleteNotification(HttpServletResponse response, Integer id) {
		service.doDeleteById(id);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "deleteNotifications")
	@ResponseBody
	public JsonActionResult deleteNotifications(HttpServletResponse response, String setIds) {
		service.doDeleteById(getIds(setIds));
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "changeReadFlag")
	@ResponseBody
	public JsonActionResult changeReadFlag(HttpServletResponse response, String setIds, Integer readFlag) {
		service.updateNotifictionReadFlag(getIds(setIds), readFlag);
		return ActionUtils.handleResult(response);
	}
}
