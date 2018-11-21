package com.wis.mes.master.boardmanage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.boardmanage.dao.TmBoardManageDao;
import com.wis.mes.master.boardmanage.entity.TmBoardManage;
import com.wis.mes.master.boardmanage.service.TmBoardManageService;
import com.wis.mes.opc.util.metalplate.OpcMetalPlateHelper;

@Service
public class TmBoardManageServiceImpl extends BaseServiceImpl<TmBoardManage> implements TmBoardManageService {

	@Autowired
	public void setDao(TmBoardManageDao dao) {
		this.dao = dao;
	}

	@Override
	public TmBoardManage findBoardManageInfo(String regionMark) {
		TmBoardManage bean = new TmBoardManage();
		if(StringUtils.isNotBlank(regionMark)) {
			bean = findByEg(TmBoardManage.createRegionMark(regionMark)).get(0);
		}
		return bean;
	}
	
	@Override
	public List<DictEntry> queryDictItem(QueryCriteria criteria) {
		criteria.setQueryPage(false);
		List<TmBoardManage> beans = findBy(criteria).getContent();
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmBoardManage e : beans) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			dict.setName(e.getRegionMark());
			dictList.add(dict);
		}
		return dictList;
	}
	
	@Override
	public void sendParameterToOPC(TmBoardManage bean) {
		String tagName = "Banjin2.1756-L72_1.Set.Given_Run_Rate_{0}";
		String itemName = "Given_Run_Rate_{0}";
		if(StringUtils.isNotBlank(bean.getRegionMark())) {
			tagName = tagName.replace("{0}", bean.getRegionMark());
			itemName = itemName.replace("{0}", bean.getRegionMark());
			String[] items = new String[] {tagName};
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(itemName, bean.getTargetMobility());// 目标可动率
			OpcMetalPlateHelper.sendDataToOpc(items, map);
		}
	}
}
