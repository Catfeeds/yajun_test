package com.wis.mes.master.parametermanage.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.equipment.entity.TmEquipmentParam;
import com.wis.mes.master.parametermanage.dao.TmParameterManageDao;
import com.wis.mes.master.parametermanage.entity.TmParameterManage;
import com.wis.mes.master.parametermanage.entity.TmParameterManageDetail;
import com.wis.mes.master.parametermanage.service.TmParameterManageDetailService;
import com.wis.mes.master.parametermanage.service.TmParameterManageService;
import com.wis.mes.master.parametermanage.vo.ParameterManageVo;
import com.wis.mes.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class TmParameterManageServiceImpl extends BaseServiceImpl<TmParameterManage> implements TmParameterManageService {

	@Autowired
	public void setDao(TmParameterManageDao dao) {
		this.dao = dao;
	}
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TmParameterManageDetailService parameterManageDetailService;
	

	@Override
	public List<ParameterManageVo> parameterColumn() {
		return ((TmParameterManageDao) dao).parameterColumn();
	}

	@Override
	public PageResult<Map<String,Object>> queryParameterManage(
			BootstrapTableQueryCriteria criteria) {
		return ((TmParameterManageDao) dao).queryParameterManage(criteria);
	}

	@Override
	public void doSaveParameter(String parameterManage) {
		JSONObject JsonObject = JSONObject.fromObject(parameterManage);
		if(checkProductModel(JsonObject.getString("backNumber"),"")){
			throw new BusinessException("PARAMETER_NAME_REPETITION");
		}
		TmParameterManage bean = new TmParameterManage();
		bean.setBackNumber(JsonObject.getString("backNumber"));
		bean.setMachineOfName(JsonObject.getString("machineOfName"));
		bean = doSave(bean);
		logService.doAddLog("TmParameterManage", bean);
		if(StringUtil.isNotEmpty(JsonObject.get("details"))){
			JSONArray jsonArray = JSONArray.fromObject(JsonObject.get("details"));
			if(jsonArray.size() > 0){
				List<TmParameterManageDetail> detailList = new ArrayList<TmParameterManageDetail>();
				TmParameterManageDetail detailBean = null;
				for(int i=0;i<jsonArray.size();i++){
					JSONObject obj = jsonArray.getJSONObject(i);
					detailBean = new TmParameterManageDetail();
					detailBean.setTmParameterManageId(bean.getId());
					if(obj.containsKey("parameterName") &&StringUtil.isNotEmpty(obj.get("parameterName"))){
						detailBean.setParameterName(obj.getString("parameterName"));
					}
					if(obj.containsKey("parameterId") && StringUtil.isNotEmpty(obj.get("parameterId"))){
						detailBean.setParameterId(Integer.valueOf(obj.getString("parameterId")));
					}
					if(obj.containsKey("parameterExplain") && StringUtil.isNotEmpty(obj.get("parameterExplain"))){
						detailBean.setParameterExplain(obj.getString("parameterExplain"));
					}
					if(obj.containsKey("maxVal") && StringUtil.isNotEmpty(obj.get("maxVal"))){
						detailBean.setMaxVal(obj.getString("maxVal"));
					}
					if(obj.containsKey("minVal") && StringUtil.isNotEmpty(obj.get("minVal"))){
						detailBean.setMinVal(obj.getString("minVal"));
					}
					detailList.add(detailBean);
				}
				if(null != detailList && detailList.size() > 0){
					parameterManageDetailService.doSaveBatch(detailList);
					logService.doAddLog("TmParameterManageDetail", detailList);
				}
			}
		}
	}
	private boolean checkProductModel(String backNumber,String id){
		TmParameterManage eg = new TmParameterManage();
		eg.setBackNumber(backNumber);
		List<TmParameterManage> list = this.findByEg(eg);
		if(null != list && list.size() > 0){
			if(StringUtil.isBlank(id)){
				return true;
			}else{
				for(TmParameterManage bean:list){
					if(StringUtil.isNotBlank(id) && !String.valueOf(bean.getId()).equals(id)){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void doUpdateParameter(String parameterManage) {
		JSONObject JsonObject = JSONObject.fromObject(parameterManage);
		if(StringUtil.isNotEmpty(JsonObject.get("id"))){
			if(checkProductModel(JsonObject.getString("backNumber"),JsonObject.getString("id"))){
				throw new BusinessException("PARAMETER_NAME_REPETITION");
			}
			TmParameterManage before = findById(Integer.valueOf(JsonObject.getString("id")));
			TmParameterManage bean = before;
			bean.setBackNumber(JsonObject.getString("backNumber"));
			bean.setMachineOfName(JsonObject.getString("machineOfName"));
			bean = doUpdate(bean);
			logService.doUpdateLog("TmParameterManage", before, bean);
			if(StringUtil.isNotEmpty(JsonObject.get("details"))){
				JSONArray jsonArray = JSONArray.fromObject(JsonObject.get("details"));
				if(jsonArray.size() > 0){
					TmParameterManageDetail detailBean = null;
					for(int i=0;i<jsonArray.size();i++){
						JSONObject obj = jsonArray.getJSONObject(i);
						detailBean = parameterManageDetailService.findById(Integer.valueOf(obj.getString("id")));
						if(null != detailBean){
							if(obj.containsKey("parameterId") && StringUtil.isNotEmpty(obj.get("parameterId"))){
								detailBean.setParameterId(Integer.valueOf(obj.getString("parameterId")));
							}
							if(obj.containsKey("maxVal")){
								detailBean.setMaxVal(obj.getString("maxVal"));
							}
							if(obj.containsKey("minVal")){
								detailBean.setMinVal(obj.getString("minVal"));
							}
						}
						parameterManageDetailService.doUpdate(detailBean);
					}
				}
			}
		}
	}

	@Override
	public TmParameterManage findByParameterManageId(String parameterManageId) {
		return ((TmParameterManageDao) dao).findByParameterManageId(parameterManageId);
	}

	@Override
	public void doDeleteParameterDetail(String ids) {
		((TmParameterManageDao) dao).doDeleteParameterDetail(ids);
	}

	@Override
	public List<TmEquipmentParam> saveOrUpdateColumn() {
		return ((TmParameterManageDao) dao).saveOrUpdateColumn();
	}

	@Override
	public List<ParameterManageVo> getParameterRange(String backNumber) {
		return ((TmParameterManageDao) dao).getParameterRange(backNumber);
	}
}
