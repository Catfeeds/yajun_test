package com.wis.mes.production.regulate.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.entity.TmUlocSipNc;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.production.plan.porder.entity.ToPorder;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;
import com.wis.mes.production.plan.porder.entity.ToPorderAviBom;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathParameter;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathPart;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathSip;
import com.wis.mes.production.record.entity.TpRecordUloc;
import com.wis.mes.production.regulate.dao.OnlineDao;
import com.wis.mes.production.wip.entity.TpWip;

@Repository
public class OnlineDaoImpl extends SqlSessionDaoSupport implements OnlineDao {

	@Override
	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public List<TmUloc> getOnlineUlocByLineId(Integer tmLineId, String ulocIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tmLineId", tmLineId);
		param.put("tmUlocIds", ulocIds);
		param.put("isOnline", "YES");
		return getSqlSession().selectList("OnlineMapper.getOnlineUlocByLineId", param);
	}

	@Override
	public List<ToPorderAviBom> getAviBom(Integer toPorderAviId, Integer tmUlocId, String seq, String globelConfig) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("toPorderAviId", toPorderAviId);
		param.put("tmUlocId", tmUlocId);
		param.put("seq", seq);
		//查询关键件信息
		List<ToPorderAviPathPart> pathPartList = getSqlSession().selectList("OnlineMapper.getQbomByAviIdUlocAndSeq",
				param);
		//查询BOM信息
		List<ToPorderAviBom> bomList = getSqlSession().selectList("OnlineMapper.getBomByUlocIdAndToPorderAviId", param);
		return getAviBom(pathPartList == null ? new ArrayList<ToPorderAviPathPart>() : pathPartList,
				bomList == null ? new ArrayList<ToPorderAviBom>() : bomList, globelConfig);
	}

	private List<ToPorderAviBom> getAviBom(List<ToPorderAviPathPart> pathPartList, List<ToPorderAviBom> bomList,
			String globelConfig) {
		if (bomList.size() == 0 && pathPartList.size() != 0) {
			for (ToPorderAviPathPart bean : pathPartList) {
				ToPorderAviBom bom = new ToPorderAviBom();
				BeanUtils.copyProperties(bean, bom, "id");
				bomList.add(bom);
			}
			return bomList;
		}
		Map<String, ToPorderAviPathPart> partMap = new HashMap<String, ToPorderAviPathPart>();
		for (ToPorderAviPathPart part : pathPartList) {
			partMap.put(part.getTmPartId().toString(), part);
		}
		for (ToPorderAviBom bom : bomList) {
			if (partMap.containsKey(bom.getTmPartId().toString())) {
				if ("BOM".equals(globelConfig)) {
					continue;
				} else if ("QBOM".equals(globelConfig)) {
					BeanUtils.copyProperties(partMap.get(bom.getTmPartId().toString()), bom, "id");
				} else {
					bom.setQty(partMap.get(bom.getTmPartId().toString()).getQty() + bom.getQty());
				}
				partMap.remove(bom.getTmPartId().toString());
			}
		}
		if (!partMap.isEmpty()) {
			for (Map.Entry<String, ToPorderAviPathPart> entry : partMap.entrySet()) {
				ToPorderAviBom bom = new ToPorderAviBom();
				BeanUtils.copyProperties(entry.getValue(), bom, "id");
				bomList.add(bom);
			}
		}
		return bomList;

	}

	@Override
	public List<ToPorderAviPathParameter> getAviPathParameter(Integer toPorderAviId, Integer tmUlocId, String seq) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("toPorderAviId", toPorderAviId);
		param.put("tmUlocId", tmUlocId);
		param.put("seq", seq);
		return getSqlSession().selectList("OnlineMapper.getBindParameterByAviId", param);
	}

	@Override
	public List<ToPorderAviPathSip> getAviPathSip(Integer toPorderAviId, Integer tmUlocId, String seq) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("toPorderAviId", toPorderAviId);
		param.put("tmUlocId", tmUlocId);
		param.put("seq", seq);
		return getSqlSession().selectList("OnlineMapper.getSipByAviIdUlocAndSeq", param);
	}

	@Override
	public List<ToPorder> getPorderByUlocId(Integer tmUlocId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tmUlocId", tmUlocId);
		return getSqlSession().selectList("OnlineMapper.getPorderByUlocId", param);
	}

	@Override
	public List<ToPorderAvi> getAviByPorderId(String aviStatus, Integer toPorderId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("aviStatus", aviStatus);
		param.put("toPorderId", toPorderId);
		return getSqlSession().selectList("OnlineMapper.getAviByPorderId", param);
	}

	@Override
	public TmWorktime getNowWorkTimeByLineId(Integer tmLineId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tmLineId", tmLineId);
		return getSqlSession().selectOne("OnlineMapper.getNowWorkTimeByLineId", param);
	}

	@Override
	public Integer getUlocCrossQty(Integer tmUlocId, String operationType, String startTime, String endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tmUlocId", tmUlocId);
		param.put("operationType", operationType);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return getSqlSession().selectOne("OnlineMapper.getUlocCrossQty", param);
	}

	@Override
	public List<TpWip> getWaitOnlineSn(String aviNo, String routingSeq, Integer tmUlocId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("routingSeq", routingSeq);
		param.put("tmUlocId", tmUlocId);
		param.put("aviNo", aviNo);
		return getSqlSession().selectList("OnlineMapper.getWaitOnlineSn", param);
	}

	@Override
	public List<TpRecordUloc> getAlreadyOnlineData(Integer tmUlocId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tmUlocId", tmUlocId);
		return getSqlSession().selectList("OnlineMapper.getAlreadyOnlineData", param);
	}

	@Override
	public TmUloc findUloc(Integer id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		return getSqlSession().selectOne("OnlineMapper.findUloc", param);
	}

	@Override
	public List<TmUlocSipNc> findTmUlocSipNcByTmUlocId(Integer tmUlocId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tmUlocId", tmUlocId);
		return getSqlSession().selectList("OnlineMapper.findTmUlocSipNcByTmUlocId", param);
	}

	@Override
	public int getUnTreatedNcBySN(String SN) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SN", SN);
		return getSqlSession().selectOne("OnlineMapper.getUnTreatedNcBySN", param);
	}
}
