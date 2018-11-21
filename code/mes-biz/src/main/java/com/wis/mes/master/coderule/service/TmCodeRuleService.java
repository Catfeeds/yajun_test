package com.wis.mes.master.coderule.service;

import java.util.Map;

import com.wis.core.service.BaseService;
import com.wis.mes.master.coderule.entity.TmCodeRule;

public interface TmCodeRuleService extends BaseService<TmCodeRule>{

	Map<String,TmCodeRule> codeRuleMaps(String lineNo);
}
