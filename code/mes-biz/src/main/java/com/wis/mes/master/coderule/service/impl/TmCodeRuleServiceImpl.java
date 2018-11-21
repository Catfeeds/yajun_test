package com.wis.mes.master.coderule.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.coderule.dao.TmCodeRuleDao;
import com.wis.mes.master.coderule.entity.TmCodeRule;
import com.wis.mes.master.coderule.service.TmCodeRuleService;

@Service
public class TmCodeRuleServiceImpl extends BaseServiceImpl<TmCodeRule> implements TmCodeRuleService {
	@Autowired
	public void setDao(TmCodeRuleDao dao) {
		this.dao = dao;
	}

	@Override
	public Map<String, TmCodeRule> codeRuleMaps(String lineNo) {
		Map<String, TmCodeRule> map = new HashMap<String, TmCodeRule>();
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryPage(false);
		criteria.setQueryRelationEntity(true);
		if(StringUtils.isNotBlank(lineNo)) {
			criteria.getQueryCondition().put("line.no", lineNo);
		}
		List<TmCodeRule> content = findBy(criteria).getContent();
		if(null != content && content.size() > 0) {
			for(TmCodeRule bean:content) {
				if(map.containsKey(bean.getPlcCode())) {
					map.put(bean.getPlcCode(), bean);
				}
			}
		}
		return map;
	}
}
