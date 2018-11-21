package com.wis.mes.production.producttracing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.production.producttracing.dao.TbProductStationDao;
import com.wis.mes.production.producttracing.entity.TbProductStation;
import com.wis.mes.production.producttracing.service.TbProductStationService;
import com.wis.mes.production.producttracing.vo.ProductStationVo;

@Service
public class TbProductStationServiceImpl extends BaseServiceImpl<TbProductStation> implements TbProductStationService {

	@Autowired
	public void setDao(TbProductStationDao dao) {
		this.dao = dao;
	}

	@Autowired
	private GlobalConfigurationService configurationService;

	@Autowired
	private TmUlocService ulocService;

	@Override
	public ProductStationVo getProductStationVo(String ulocNo, String sn) {
		ProductStationVo vo = new ProductStationVo();
		String lineNo = configurationService.getValueByKey(ConstantUtils.R5_LINE_NO);
		TmUloc uloc = ulocService.getRedisUloc(lineNo, ulocNo);
		if (uloc != null) {
			vo.setTmUlocId(uloc.getId());
			vo.setTmLineId(uloc.getTmLineId());
			vo.setTmPlantId(uloc.getTmPlantId());
			TmWorktime workTime = ((TbProductStationDao) dao).getCurrentWorkTime(uloc.getTmLineId());
			if (workTime != null && workTime.getTmClassManagerId() != null) {
				Integer staffId = ((TbProductStationDao) dao).getStaffIdBy(vo.getTmUlocId(), uloc.getTmLineId(),
						workTime.getTmClassManagerId());
				vo.setStaffId(staffId);
				vo.setShiftNo(workTime.getShiftno());
				vo.setClassManagerId(workTime.getTmClassManagerId());
				vo.setTmWorktimeId(workTime.getId());
			}
		}
		vo.setTmLineNo(lineNo);
		vo.setProductTracingId(((TbProductStationDao) dao).getProductIdBySn(sn));
		return vo;
	}
}
