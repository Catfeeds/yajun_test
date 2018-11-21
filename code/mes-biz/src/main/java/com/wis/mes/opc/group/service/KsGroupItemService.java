package com.wis.mes.opc.group.service;

import java.util.List;

import com.wis.core.service.BaseService;
import com.wis.mes.opc.group.entity.KsGroupItem;

public interface KsGroupItemService extends BaseService<KsGroupItem> {

	List<KsGroupItem> getItemListByGroupCode(String code);

	List<KsGroupItem> getRedisItemListByGroupCode(String code);
}
