package com.wis.mes.master.workcalendar.service;

import com.wis.core.service.BaseService;
import com.wis.mes.master.workcalendar.entity.TmWorktimeRest;

import java.util.List;

public interface TmWorktimeRestService extends BaseService<TmWorktimeRest> {
	String queryRestIdsByWorkTimeId(Integer[] workTimeIds);

    List<TmWorktimeRest> findByWorkTimeId(Integer workTimeId);
}
