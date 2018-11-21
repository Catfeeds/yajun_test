package com.wis.mes.master.nc.service;

import java.util.List;

import com.wis.core.service.BaseService;
import com.wis.mes.master.nc.entity.TmFaultGrade;
import com.wis.mes.master.nc.vo.NGVo;

public interface TmFaultGradeService extends BaseService<TmFaultGrade> {
	List<NGVo> getDictItem(TmFaultGrade tmFaultGrade);
}
