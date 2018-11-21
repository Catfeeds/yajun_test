package com.wis.mes.master.parametermanage.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.equipment.entity.TmEquipmentParam;
import com.wis.mes.master.parametermanage.dao.TmParameterManageDao;
import com.wis.mes.master.parametermanage.entity.TmParameterManage;
import com.wis.mes.master.parametermanage.vo.ParameterManageVo;
import com.wis.mes.util.StringUtil;

@Repository
public class TmParameterManageDaoImpl extends BaseDaoImpl<TmParameterManage> implements TmParameterManageDao {

	@Override
	public List<ParameterManageVo> parameterColumn() {
		return getSqlSession().selectList("ParameterManageMapper.parameterColumn");
	}
	@Override
	public List<TmEquipmentParam> saveOrUpdateColumn() {
		return getSqlSession().selectList("ParameterManageMapper.saveOrUpdateColumn");
	}
	
	
	@Override
	public PageResult<Map<String,Object>> queryParameterManage(BootstrapTableQueryCriteria criteria) {
		PageResult<Map<String,Object>> pageResult = new PageResult<Map<String,Object>>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows() * criteria.getPage();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getUserCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		if(StringUtil.isNotEmpty(criteria.getQueryCondition().get("productModel"))){
			params.put("productModel",criteria.getQueryCondition().get("productModel"));
		}
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<TmParameterManage> content = getSqlSession().selectList("ParameterManageMapper.queryParameterManage", params);
		pageResult.setContent(parameterDataHandling(content));
		return pageResult;
	}
	
	private int getUserCount(Map<String, Object> params) {
		return getSqlSession().selectOne("ParameterManageMapper.queryParameterManageCount", params);
	}
	
	private List<Map<String,Object>> parameterDataHandling (List<TmParameterManage> parameterManages){
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		if(null != parameterManages && parameterManages.size() > 0){
			for(TmParameterManage parameterManage:parameterManages){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("backNumber", parameterManage.getBackNumber());
				map.put("machineOfName", parameterManage.getMachineOfName());
				map.put("id", parameterManage.getId());
				String src = parameterManage.getDetails();
				if(StringUtil.isNotBlank(src)){
					String[] details = src.split(",");
					for(String detail:details){
						String[] rows = detail.split(":");
						for(int i=0;i< rows.length;i++){
							map.put(rows[0], rows.length>1?rows[1]:"");
						}
					}
				}
				listMap.add(map);
			}
		}
		return listMap;
	}


	@Override
	public TmParameterManage findByParameterManageId(String parameterManageId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("parameterManageId", parameterManageId);
		return getSqlSession().selectOne("ParameterManageMapper.findByParameterManageId",map);
	}


	@Override
	public void doDeleteParameterDetail(String ids) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ids", ids);
		getSqlSession().delete("ParameterManageMapper.doDeleteParameterDetail",map);
		
	}
	@Override
	public List<ParameterManageVo> getParameterRange(String backNumber) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("backNumber", backNumber);
		return getSqlSession().selectList("ParameterManageMapper.getParameterRange",map);
	}
	
}
