package com.wis.basis.notification.service;

import com.wis.basis.notification.entity.Notification;
import com.wis.core.service.BaseService;

import net.sf.json.JSONObject;

public interface NotificationService extends BaseService<Notification> {

	/**
	 * 获取未读通知信息
	 * @param userId
	 * @return
	 */
	JSONObject findCountInfoNoReadBy(Integer userId);

	void updateNotifictionReadFlag(Integer[] ids, Integer readFlag);
}
