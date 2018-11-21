package com.wis.mes.master.path.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.path.dao.TmPathUlocDao;
import com.wis.mes.master.path.entity.TmPathUloc;
import com.wis.mes.util.StringUtil;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 工艺路径站点数据持久层接口实现
 *
 */
@Repository
public class TmPathUlocDaoImpl extends BaseDaoImpl<TmPathUloc> implements TmPathUlocDao {

	@Override
	public List<Map<String, Object>> queryPathUloc(Integer tmPathId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tmPathId", tmPathId);
		return getSqlSession().selectList("PathMapper.queryPathUloc", paramMap);
	}

	@Override
	public List<Map<String, Object>> findByIds(String ids) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtil.isBlank(ids)) {
			paramMap.put("tmPathUlocIds", "('')");
		} else {
			paramMap.put("tmPathUlocIds", "(" + ids + ")");
		}
		return getSqlSession().selectList("PathMapper.queryPathUloc", paramMap);
	}

	@Override
	public List<Map<String, Object>> queryByPathIdAndUlocType(Integer tmPathId, String ulocType) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tmPathId", tmPathId);
		paramMap.put("isOffline", ulocType);
		return getSqlSession().selectList("PathMapper.queryPathUloc", paramMap);
	}

	@Override
	public void deletePathUlocChilds(String tmPathUlocIds) {
		if (StringUtil.isBlank(tmPathUlocIds)) {
			return;
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("tmPathUlocIds", "(" + tmPathUlocIds + ")");
		getSqlSession().delete("PathMapper.deletePathUlocChilds", parameter);
	}

	@Override
	public void doDeletePathUlocOrParam(String tmPathUlocIds) {
		if (StringUtil.isBlank(tmPathUlocIds)) {
			return;
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("tmPathUlocIds", "(" + tmPathUlocIds + ")");
		getSqlSession().delete("PathMapper.deleteBatchPathUlocId", parameter);
		
	}
	
	@Override
	public List<Map<String, Object>> getTmPathIdsByTmPathUlocIds(String tmPathIds) {
		if (StringUtil.isBlank(tmPathIds)) {
			return null;
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("tmPathIds", "(" + tmPathIds + ")");
		return getSqlSession().selectList("PathMapper.selectTmPathIdsByTmPathUlocIds", parameter);
	}

}
