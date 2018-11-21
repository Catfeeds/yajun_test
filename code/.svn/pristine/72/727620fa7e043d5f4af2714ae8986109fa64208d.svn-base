package com.wis.mes.opc.group.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.opc.group.dao.KsGroupItemDao;
import com.wis.mes.opc.group.entity.KsGroupItem;
import com.wis.mes.opc.group.service.KsGroupItemService;

@Service
public class KsGroupItemServiceImpl extends BaseServiceImpl<KsGroupItem> implements KsGroupItemService {
	private final static String REDIS_KS_GROUP = "WIS_KS_GROUP_KEY";

	@Autowired
	public void setDao(KsGroupItemDao dao) {
		this.dao = dao;
	}

	@Autowired
	private RedisTemplate<String, List<KsGroupItem>> template;

	@Override
	public List<KsGroupItem> getRedisItemListByGroupCode(String code) {
		HashOperations<String, String, List<KsGroupItem>> opsForHash = template.opsForHash();
		List<KsGroupItem> list = opsForHash.get(REDIS_KS_GROUP, code);
		if (list == null) {
			list = getItemListByGroupCode(code);
			opsForHash.put(REDIS_KS_GROUP, code, list);
		}
		return list;
	}

	@Override
	public List<KsGroupItem> getItemListByGroupCode(String code) {
		QueryCriteria citeria = new QueryCriteria();
		citeria.setQueryRelationEntity(true);
		citeria.setQueryPage(false);
		citeria.getQueryCondition().put("group.groupCode", code);
		citeria.setOrderField("orderNumber");
		citeria.setOrderDirection(OrderEnum.ASC);
		List<KsGroupItem> content = this.findBy(citeria).getContent();
		for (KsGroupItem ksGroupItem : content) {
			if (StringUtils.isNoneEmpty(ksGroupItem.getGroup().getGroupChannel())) {
				ksGroupItem.setItemCode(ksGroupItem.getGroup().getGroupChannel() + "." + ksGroupItem.getItemCode());
			}
		}
		return content;
	}
}
