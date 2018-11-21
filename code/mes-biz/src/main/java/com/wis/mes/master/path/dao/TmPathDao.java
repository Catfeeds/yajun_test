package com.wis.mes.master.path.dao;

import java.util.List;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.path.entity.TmPath;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 工艺路径数据持久层接口
 *
 */
public interface TmPathDao extends BaseDao<TmPath> {

	public List<TmPath> getPathByPlantAndParts(Integer tmPlantId, Integer tmPartId);

	/**
	 * 根据工艺路径站点ID查询 工艺路径信息
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	public TmPath getByPathUlocId(Integer tmPathUlocId);
}
