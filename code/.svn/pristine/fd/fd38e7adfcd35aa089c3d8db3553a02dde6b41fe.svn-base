package com.wis.mes.master.path.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.path.dao.TmPathDao;
import com.wis.mes.master.path.entity.TmPath;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 工艺路径数据持久层接口实现
 *
 */
@Repository
public class TmPathDaoImpl extends BaseDaoImpl<TmPath> implements TmPathDao {

	@Override
	public List<TmPath> getPathByPlantAndParts(Integer tmPlantId, Integer tmPartId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tmPartId", tmPartId);
		paramMap.put("tmPlantId", tmPlantId);
		return getSqlSession().selectList("PathMapper.getPath", paramMap);
	}

	@Override
	public TmPath getByPathUlocId(Integer tmPathUlocId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tmPathUlocId", tmPathUlocId);
		return getSqlSession().selectOne("PathMapper.getByPathUlocId", paramMap);
	}

}
