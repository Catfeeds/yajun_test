package com.wis.mes.production.plan.porder.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.wis.core.service.BaseService;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;

public interface ToPorderAviService extends BaseService<ToPorderAvi> {

	/**
	 * 保存AVI
	 * 
	 * @param toPorderId
	 *            工单ID
	 * @param everyQty
	 *            单个序列数量
	 * @param tmPathId
	 *            工艺路径
	 * @param tmBomId
	 *            BOM
	 * @param seqRuleNo
	 *            规则的编号
	 */
	public void doSaveAvi(Integer toPorderId, Integer everyQty, Integer tmPathId, Integer tmBomId, String note,
			String no);

	/**
	 * 删除该序列下的BOM信息与AVI
	 * 
	 * @param aviId
	 */
	public void deleteAviBomAndPathByAviId(Integer aviId);

	/**
	 * 查询工单下生产序列信息
	 * 
	 * @param toPorderId
	 * @return
	 */
	public List<ToPorderAvi> getAvis(Integer toPorderId);

	/**
	 * 取消派工
	 * 
	 * @param id
	 */
	public void doCancelTask(Integer id);

	/**
	 * 根据生产序列ID查询工单的工厂ID
	 * 
	 * @param aviId
	 * @return
	 */
	public Integer getPorderPlantIdByAviId(Integer aviId);

	/**
	 * 生产序列数据导出
	 * 
	 * @param response
	 * @param content
	 * @param filePath
	 * @param header
	 */
	public void exportExcelData(HttpServletResponse response, List<ToPorderAvi> content, String filePath,
			String[] header);

	void deleteAviPathDetailByAviId(Integer aviId);

	/**
	 * 根据工艺路径id查询产线id
	 * 
	 * @param tmPathId
	 * @return
	 */
	Integer getLineId(Integer tmPathId);
}
