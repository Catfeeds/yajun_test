package com.wis.mes.master.classmanage.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.classmanage.dao.TmClassManagerDao;
import com.wis.mes.master.classmanage.entity.TmClassManager;
import com.wis.mes.master.classmanage.service.TmClassManagerService;

@Service
public class TmClassManagerServiceImpl extends BaseServiceImpl<TmClassManager> implements TmClassManagerService{
	
	@Autowired
	public void setDao(TmClassManagerDao dao) {
		this.dao = dao;
	}
	@Autowired
	private DictEntryService entryService;
	
	@Override
	public List<DictEntry> getDictItem(TmClassManager eg) {
		//Map<String,DictEntry> classGroupMap = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_CLASS_GROUP);
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmClassManager bean : (eg == null ? findAll() : findByEg(eg))) {
			final DictEntry dict = new DictEntry();
			dict.setCode(bean.getId().toString());
			dict.setName(bean.getClassGroup());
			dictList.add(dict);
		}
		return dictList;
	}
	
	@Override
	public List<DictEntry> getDictItemEntry(TmClassManager eg) {
		Map<String,DictEntry> classGroupMap = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_CLASS_GROUP);
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmClassManager bean : (eg == null ? findAll() : findByEg(eg))) {
			final DictEntry dict = new DictEntry();
			dict.setCode(bean.getId().toString());
			if(StringUtils.isNotBlank(bean.getClassGroup()) && classGroupMap.containsKey(bean.getClassGroup())){
				dict.setName(classGroupMap.get(bean.getClassGroup()).getName());
			}else{
				dict.setName(bean.getClassGroup());
			}
			dictList.add(dict);
		}
		return dictList;
	}

	@Override
	public List<TmClassManager> plantTolineToClassManager(Integer plantId, Integer lineId) {
		TmClassManager eg = new TmClassManager();
		eg.setTmPlantId(plantId);
		eg.setTmLineId(lineId);
		return super.findByEg(eg);
	}

}
