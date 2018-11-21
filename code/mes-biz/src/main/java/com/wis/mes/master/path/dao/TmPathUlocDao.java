package com.wis.mes.master.path.dao;

import java.util.List;
import java.util.Map;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.path.entity.TmPathUloc;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 工艺路径站点数据持久层接口
 *
 */
public interface TmPathUlocDao extends BaseDao<TmPathUloc> {

	public List<Map<String, Object>> queryPathUloc(Integer tmPathId);

	/***
	 * 根据ppathUlocId查询
	 * 
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> findByIds(String ids);

	/**
	 * 根据工艺路径ID和序状态查询
	 * 
	 * @param tmPathId
	 * @param ulocType
	 *            in () 或者 = '' 或者 ！=‘’
	 * @return
	 */
	public List<Map<String, Object>> queryByPathIdAndUlocType(Integer tmPathId, String ulocType);

	/**
	 * 删除站点下的所有子信息
	 * 
	 * @param tmPathUlocIds
	 *            以逗号隔开的id
	 */
	void deletePathUlocChilds(String tmPathUlocIds);
	
	/**
	 * 删除站点及站点下的参数
	 * 
	 * @param tmPathUlocIds
	 *            以逗号隔开的id
	 */
	void doDeletePathUlocOrParam(String tmPathUlocIds);
	
	/**
	 * 根据工艺路径ids查询对应所以有的站点ids
	 * @param tmPathIds
	 * @return
	 */
	List<Map<String, Object>> getTmPathIdsByTmPathUlocIds(String tmPathIds);

}
