package com.wis.mes.master.workcalendar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.workcalendar.dao.TmWorktimeRestDao;
import com.wis.mes.master.workcalendar.entity.TmWorktimeRest;
import com.wis.mes.master.workcalendar.service.TmWorktimeRestService;

import java.util.List;

@Service
public class TmWorktimeRestServiceImpl extends BaseServiceImpl<TmWorktimeRest> implements TmWorktimeRestService {

	@Autowired
	public void setDao(TmWorktimeRestDao dao) {
		this.dao = dao;
	}
	public String queryRestIdsByWorkTimeId(Integer[] workTimeIds) {
	    return ((TmWorktimeRestDao)dao).queryRestIdsByWorkTimeId(workTimeIds);
	}

	@Override
	public List<TmWorktimeRest> findByWorkTimeId(Integer workTimeId){
        TmWorktimeRest eg = new TmWorktimeRest();
        eg.setTmWorktimeId(workTimeId);
        return findByEg(eg);
    }
}
