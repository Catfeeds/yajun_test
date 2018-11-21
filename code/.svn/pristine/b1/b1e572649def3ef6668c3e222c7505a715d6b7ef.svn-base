package com.wis.basis.notification.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.notification.dao.NotificationDao;
import com.wis.basis.notification.entity.Notification;
import com.wis.basis.notification.service.NotificationService;
import com.wis.core.service.impl.BaseServiceImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class NotificationServiceImpl extends BaseServiceImpl<Notification> implements NotificationService {

	@Autowired
	private void setDao(NotificationDao dao) {
		this.dao = dao;
	}

	@Override
	public JSONObject findCountInfoNoReadBy(Integer userId) {
		JSONObject res = new JSONObject();
//		StringBuffer sql = new StringBuffer(
//				"select MSG_TYPE as msgType,count(1) as num from TS_NOTIFICATION where (READ_FLAG=0 and RECEIVER_ID="
//						+ userId + ") or (RECEIVER_ID is null) group by MSG_TYPE");
		StringBuffer sql = new StringBuffer(
				"select MSG_TYPE as msgType,count(1) as num from TS_NOTIFICATION where READ_FLAG=0 group by MSG_TYPE");
		List<Map<String, Object>> notifications = dao.findAllSql(sql);
		int totalCount = 0;
		if (null != notifications && notifications.size() > 0) {
			for (Map<String, Object> map : notifications) {
				totalCount += Integer.valueOf(map.get("num").toString());
			}
			res.put("notifications", JSONArray.fromObject(notifications));
		}
		res.put("totalCount", totalCount);
		return res;
	}

	@Override
	public void updateNotifictionReadFlag(Integer[] ids, Integer readFlag) {
		for (Integer id: ids) {
			Notification notification = null;
			if (null != (notification = findById(id))) {
				notification.setReadFlag(readFlag);
				doUpdate(notification);
			}
		}
	}
}
