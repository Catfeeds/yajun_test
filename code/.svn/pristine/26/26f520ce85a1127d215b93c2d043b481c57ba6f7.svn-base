package com.wis.mes.master.nc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.nc.dao.TmFaultGradeDao;
import com.wis.mes.master.nc.entity.TmFaultGrade;
import com.wis.mes.master.nc.service.TmFaultGradeService;
import com.wis.mes.master.nc.vo.NGVo;

@Service
public class TmFaultGradeServiceImpl extends BaseServiceImpl<TmFaultGrade> implements TmFaultGradeService {

	@Autowired
	public void setDao(TmFaultGradeDao dao) {
		this.dao = dao;
	}
	/**
	 * 获取故障等级列表
	 * 
	 * @return List<DictEntry>
	 */
	public List<NGVo> getDictItem(TmFaultGrade tmFaultGrade) {
		List<TmFaultGrade> TmFaultGrades = (tmFaultGrade == null ? findAll() : findByEg(tmFaultGrade));
		final List<NGVo> dictList = new ArrayList<NGVo>();
		for (final TmFaultGrade e : TmFaultGrades) {
			final NGVo dict = new NGVo();
			dict.setCode(e.getId().toString());
			dict.setName(e.getNgLevel());
			dict.setRelevantParameter(e.getNgEntrance());
			dictList.add(dict);
		}
		return dictList;
	}
}
