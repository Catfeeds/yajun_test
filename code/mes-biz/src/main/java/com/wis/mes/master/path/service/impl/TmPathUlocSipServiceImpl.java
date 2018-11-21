package com.wis.mes.master.path.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.basis.configuration.service.TsParameterService;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.core.utils.SerialNumUtil;
import com.wis.mes.master.path.dao.TmPathUlocSipDao;
import com.wis.mes.master.path.entity.TmPathUlocSip;
import com.wis.mes.master.path.service.TmPathUlocSipService;

@Service
public class TmPathUlocSipServiceImpl extends BaseServiceImpl<TmPathUlocSip> implements TmPathUlocSipService {

	@Autowired
	public void setDao(TmPathUlocSipDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TsParameterService parameterService;

	/**
	 * <key parameterId,value TmPathUlocSip
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	private Map<String, TmPathUlocSip> getParameterIdMap(Integer tmPathUlocId) {
		Map<String, TmPathUlocSip> map = new HashMap<String, TmPathUlocSip>();
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("tmPathUlocId", tmPathUlocId.toString());
		List<TmPathUlocSip> content = findBy(criteria).getContent();
		for (TmPathUlocSip sip : content) {
			map.put(sip.getTsParameterId().toString(), sip);
		}
		return map;
	}

	@Override
	public List<TmPathUlocSip> doSavePathUlocSip(String[] tsParameterId, String[] note, Integer tmPathUlocId) {
		Map<String, TmPathUlocSip> parameterIdMap = getParameterIdMap(tmPathUlocId);
		List<TmPathUlocSip> sipAddList = new ArrayList<TmPathUlocSip>();
		List<TmPathUlocSip> sipUpdateList = new ArrayList<TmPathUlocSip>();
		//选中一条数据，并且备注没有填写
		if (tsParameterId.length == 1 && note.length == 0) {
			if (parameterIdMap.containsKey(tsParameterId[0])) {//数据库存在
				TmPathUlocSip sip = parameterIdMap.get(tsParameterId[0]);
				sip.setNote("");
				sipUpdateList.add(sip);
			} else {
				TmPathUlocSip sip = new TmPathUlocSip();
				sip.setTmPathUlocId(tmPathUlocId);
				sip.setNote("");
				sip.setTsParameterId(Integer.valueOf(tsParameterId[0]));
				sipAddList.add(sip);
			}

		} else {
			for (int i = 0; i < tsParameterId.length && i < note.length; i++) {
				if (parameterIdMap.containsKey(tsParameterId[i])) {//数据库中不存在
					TmPathUlocSip sip = parameterIdMap.get(tsParameterId[i]);
					sip.setNote(note[i]);
					sipUpdateList.add(sip);
				} else {
					TmPathUlocSip sip = new TmPathUlocSip();
					sip.setNote(note[i]);
					sip.setTmPathUlocId(tmPathUlocId);
					sip.setTsParameterId(Integer.valueOf(tsParameterId[i]));
					sipAddList.add(sip);
				}
			}
		}
		doSaveBatch(sipAddList);
		if (sipUpdateList.size() > 0) {
			doUpdateBatch(sipUpdateList);
		}
		return sipAddList;
	}

	@Override
	public PageResult<TsParameter> getEquipmentAndUlocParameter(QueryCriteria criteria) {
		return ((TmPathUlocSipDao) dao).getEquipmentAndUlocParameter(criteria);
	}

	@Override
	public List<TsParameter> getSipParameterExamineAndNotInSip(Integer tmPathUlocId, Integer tmUlocId,
			String[] tsParameterIds) {
		return ((TmPathUlocSipDao) dao).getSipParameterExamineAndNotInSip(tmPathUlocId, tmUlocId, tsParameterIds);
	}

	@Override
	public List<TsParameter> getSipParameterExamine(Integer tmPathUlocId, Integer tmUlocId) {
		return ((TmPathUlocSipDao) dao).getSipParameterExamine(tmPathUlocId, tmUlocId);
	}

	@Override
	public void exportExcelData(HttpServletResponse response, List<TmPathUlocSip> content, String path,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		for (TmPathUlocSip bean : content) {
			TsParameter parameter = bean.getParameter();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("质检项编号", parameter.getCode());
			map.put("质检项名称", parameter.getName());
			map.put("质检项表达式", parameter.getRegularExpression());
			map.put("质检项变量", parameter.getVariuableName());
			map.put("质检项说明", parameter.getNote() == null ? "" : parameter.getNote());
			map.put("备注", bean.getNote() == null ? "" : bean.getNote());
			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, path, header);

	}

	@Override
	public List<TmPathUlocSip> getPathUloSips(Integer tmPathUlocId) {
		TmPathUlocSip sip = new TmPathUlocSip();
		sip.setTmPathUlocId(tmPathUlocId);
		return findByEg(sip);
	}

	@Override
	public List<TmPathUlocSip> getPathUlocSipData(String[] tsParameterId, String[] note, Integer tmPathUlocId) {
		List<TmPathUlocSip> list = new ArrayList<TmPathUlocSip>();
		//选中一条数据，并且备注没有填写
		if (tsParameterId.length == 1 && note.length == 0) {
			TmPathUlocSip sip = new TmPathUlocSip();
			sip.setId(SerialNumUtil.getInstance().nextInt(ConstantUtils.SERIAL_PATH_ULOC_SIP_ID));
			sip.setTmPathUlocId(tmPathUlocId);
			sip.setNote("");
			sip.setTsParameterId(Integer.valueOf(tsParameterId[0]));
			sip.setParameter(parameterService.findById(sip.getTsParameterId()));
			list.add(sip);
		} else {
			for (int i = 0; i < tsParameterId.length && i < note.length; i++) {
				TmPathUlocSip sip = new TmPathUlocSip();
				sip.setId(SerialNumUtil.getInstance().nextInt(ConstantUtils.SERIAL_PATH_ULOC_SIP_ID));
				sip.setNote(note[i]);
				sip.setTmPathUlocId(tmPathUlocId);
				sip.setTsParameterId(Integer.valueOf(tsParameterId[i]));
				sip.setParameter(parameterService.findById(sip.getTsParameterId()));
				list.add(sip);
			}
		}
		return list;
	}
}
