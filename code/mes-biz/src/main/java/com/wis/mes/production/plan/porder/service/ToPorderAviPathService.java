package com.wis.mes.production.plan.porder.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wis.core.service.BaseService;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPath;

public interface ToPorderAviPathService extends BaseService<ToPorderAviPath> {

	/**
	 * 保存生产序列对应的工艺路径
	 * 
	 * @param 生产序列ID
	 * @param tmPathId
	 */
	public void doSavePath(Integer aviId, Integer tmPathId);

	/**
	 * 查询生产序列下不是下线点的数据
	 * 
	 * @param aviId
	 * @return
	 */
	public List<Map<String, Object>> selectNotOfflineDataByAviId(Integer aviId);

	/**
	 * 保存生产序列下的工艺路径
	 * 
	 * @param bean
	 * @param ulocType
	 * @return
	 */
	public ToPorderAviPath doSavePathUloc(ToPorderAviPath bean, String ulocType);

	/**
	 * 查询生产序列下的工艺路径信息，以seq为正序排列
	 * 
	 * @param aviId
	 * @return
	 */
	public List<ToPorderAviPath> getAviPaths(Integer aviId, boolean queryRelationEntity);

	/**
	 * 修改生产序列下的工艺路径
	 * 
	 * @param before
	 * @param bean
	 * @param ulocType
	 */
	public void doUpdatePathUloc(ToPorderAviPath before, ToPorderAviPath bean, String ulocType);

	/**
	 * 工艺路径echarts图形展示
	 * 
	 * @param aviId
	 * @return
	 */
	public Map<String, String> getEchartsData(String aviId);

	/**
	 * 删除并且更新seq
	 * 
	 * @param deleteIds
	 * @param list
	 */
	public void doDeleteIdsAndUpdateSeq(Integer[] deleteIds, List<ToPorderAviPath> list);

	/**
	 * 生产序列下，工艺路径信息导出
	 * 
	 * @param response
	 * @param content
	 * @param filePath
	 * @param header
	 */
	public void exportExcelData(HttpServletResponse response, List<ToPorderAviPath> content, String filePath,
			String[] header);

	/**
	 * 根据生产序列工艺路径id查询工单所在的工厂
	 * 
	 * @param toPorderAviPathId
	 * @return
	 */
	Integer getPorderPlantIdByAviPathId(Integer toPorderAviPathId);
}
