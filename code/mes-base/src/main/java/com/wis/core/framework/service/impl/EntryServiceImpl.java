package com.wis.core.framework.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wis.core.framework.dao.DictEntryDao;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.entity.DictType;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.DictTypeService;
import com.wis.core.service.impl.BaseServiceImpl;

@Service("entryService")
public class EntryServiceImpl extends BaseServiceImpl<DictEntry>implements DictEntryService {

	@Autowired
	public void setDao(DictEntryDao dao) {
		this.dao = dao;
	}

	private DictEntryDao getDao() {
		return (DictEntryDao) dao;
	}

	@Autowired
	private DictTypeService typeService;

	public DictEntry getEntryByCode(String typeCode, String entryCode) {
		return getDao().getByCode(typeCode, entryCode);
	}

	@Cacheable(value = Constants.CACHE_NAME_PREFIX + "dictEntries", key = "new String('" + Constants.CACHE_NAME_PREFIX + "entryService.getEntryByTypeCode')+#id")
	public List<DictEntry> getEntryByTypeId(int typeId) {
		return getDao().getByTypeId(typeId);
	}

	@Cacheable(value = Constants.CACHE_NAME_PREFIX + "dictEntries", key = "new String('" + Constants.CACHE_NAME_PREFIX + "entryService.getEntryByTypeCode')+#typeCode")
	public List<DictEntry> getEntryByTypeCode(String typeCode) {
		return getDao().getByTypeCode(typeCode);
	}

	public Integer check(int typeId, String entryCode) {
		List<DictEntry> list = getDao().check(typeId, entryCode);
		if (list != null && list.size() > 0) {
			return list.get(0).getId();
		}
		return null;
	}

	@CachePut(value = Constants.CACHE_NAME_PREFIX + "dictEntry", key = "new String('" + Constants.CACHE_NAME_PREFIX + "entryService.findById')+#result.id")
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries" }, allEntries = true)
	public DictEntry doUpdate(DictEntry entry) {
		DictEntry updateEntry = dao.get(entry.getId());
		updateEntry.setName(entry.getName());
		updateEntry.setEnName(entry.getEnName());
		updateEntry.setCode(entry.getCode());
		updateEntry.setSortNo(entry.getSortNo());
		if (null == updateEntry.getSortNo()) {
			updateEntry.setSortNo(0);
		}
		updateEntry.setType(entry.getType());
		dao.update(updateEntry);
		return updateEntry;
	}

	@CachePut(value = Constants.CACHE_NAME_PREFIX + "dictEntry", key = "new String('" + Constants.CACHE_NAME_PREFIX + "entryService.findById')+#result.id")
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries" }, allEntries = true)
	public DictEntry doSave(DictEntry entry) {
		if (entry.getTypeId() != null) {
			DictType type = typeService.findById(entry.getTypeId());
			entry.setType(type);
		}
		if (null == entry.getSortNo()) {
			entry.setSortNo(0);
		}
		dao.save(entry);
		return entry;

	}

	@Override
	@Cacheable(value = Constants.CACHE_NAME_PREFIX + "dictEntry", key = "new String('" + Constants.CACHE_NAME_PREFIX + "entryService.findById')+#id")
	public DictEntry findById(Integer id) {
		DictEntry entry = super.findById(id);
		entry.setType(typeService.findById(entry.getTypeId()));
		return entry;
	}

	@Override
	@Cacheable(value = Constants.CACHE_NAME_PREFIX + "dictEntries", key = "new String('" + Constants.CACHE_NAME_PREFIX + "entryService.getMapTypeCode')+#typeCode")
	public Map<String, DictEntry> getMapTypeCode(String typeCode) {
		Map<String, DictEntry> result = new HashMap<String, DictEntry>();
		List<DictEntry> list = getEntryByTypeCode(typeCode);
		for (DictEntry entry : list) {
			result.put(entry.getCode(), entry);
		}
		return result;
	}

	@Override
	@CachePut(value = Constants.CACHE_NAME_PREFIX + "dictEntry", key = "new String('" + Constants.CACHE_NAME_PREFIX + "entryService.findById')+#result.id")
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries" }, allEntries = true)
	public DictEntry doSaveOrUpdate(DictEntry entity) {
		return super.doSaveOrUpdate(entity);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries", Constants.CACHE_NAME_PREFIX + "dictEntry" }, allEntries = true)
	public void doDeleteById(Integer id) {
		super.doDeleteById(id);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries", Constants.CACHE_NAME_PREFIX + "dictEntry" }, allEntries = true)
	public void doDeleteById(Integer[] ids) {
		super.doDeleteById(ids);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries" }, allEntries = true)
	public void doUpdateBatch(List<DictEntry> entities) {
		super.doUpdateBatch(entities);
	}

}
