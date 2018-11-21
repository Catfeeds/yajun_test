package com.wis.core.framework.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wis.core.framework.dao.DictTypeDao;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.entity.DictType;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.DictTypeService;
import com.wis.core.service.impl.BaseServiceImpl;

@Service("typeService")
public class DictTypeServiceImpl extends BaseServiceImpl<DictType>implements DictTypeService {

	@Autowired
	public void setTypeDao(DictTypeDao typeDao) {
		this.dao = typeDao;
	}

	@Autowired
	private DictEntryService dictEntryService;

	@Override
	@Cacheable(value = Constants.CACHE_NAME_PREFIX + "dictType", key = "new String('" + Constants.CACHE_NAME_PREFIX + "typeService.findById')+#id")
	public DictType findById(Integer id) {
		return super.findById(id);
	}

	@Override
	@CachePut(value = Constants.CACHE_NAME_PREFIX + "dictType", key = "new String('" + Constants.CACHE_NAME_PREFIX + "typeService.findById')+#result.id")
	public DictType doSave(DictType entity) {
		return super.doSave(entity);
	}

	public Integer check(String typeCode) {
		DictType type = new DictType();
		type.setCode(typeCode);
		List<DictType> list = dao.findByEg(type);
		if (list != null && list.size() > 0) {
			return list.get(0).getId();
		}
		return null;
	}

	@Cacheable(value = Constants.CACHE_NAME_PREFIX + "pageEntry", key = "new String('" + Constants.CACHE_NAME_PREFIX + "pageEntry')")
	public Map<String, Map<String, Map<String, String>>> findPageEntry() {
		List<Map<String, String>> queryList = ((DictTypeDao) dao).getTypeCodeMap();
		Map<String, Map<String, Map<String, String>>> result = new HashMap<String, Map<String, Map<String, String>>>();
		for (Map<String, String> m : queryList) {
			String type = m.get("typeCode");
			Map<String, String> names = new HashMap<String, String>();
			names.put("zh_CN", m.get("name"));
			names.put("en_US", m.get("enName"));
			if (result.containsKey(type)) {
				result.get(type).put(m.get("entryCode"), names);
			} else {
				Map<String, Map<String, String>> temp = new HashMap<String, Map<String, String>>();
				temp.put(m.get("entryCode"), names);
				result.put(type, temp);
			}
		}
		return result;
	}

	@Override
	@CachePut(value = Constants.CACHE_NAME_PREFIX + "dictType", key = "new String('" + Constants.CACHE_NAME_PREFIX + "typeService.findById')+#result.id")
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries" }, allEntries = true)
	public DictType doUpdate(DictType type) {
		DictType savedType = dao.get(type.getId());
		savedType.setCode(type.getCode());
		savedType.setName(type.getName());
		savedType.setEnName(type.getEnName());
		return dao.update(savedType);
	}

	@Override
	public Boolean checkCode(String code) {
		DictType type = new DictType();
		type.setCode(code);
		List<DictType> types = dao.findByEg(type);
		return types.size() > 0;
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries", Constants.CACHE_NAME_PREFIX + "dictType" }, allEntries = true)
	public void doRemoveByBatch(Integer[] ids) {
		if (null == ids || ids.length == 0) {
			return;
		}
		List<Integer> entriesId = new ArrayList<Integer>();
		List<DictEntry> deletedEntries = new ArrayList<DictEntry>();
		for (int i = 0; i < ids.length; i++) {
			DictEntry eg = new DictEntry();
			eg.setTypeId(ids[i]);
			List<DictEntry> entries = dictEntryService.findByEg(eg);
			if (null != entries && entries.size() > 0) {
				for (int j = 0; j < entries.size(); j++) {
					Integer entryId = entries.get(j).getId();
					if (!entriesId.contains(entryId)) {
						entriesId.add(entryId);
						deletedEntries.add(entries.get(j));
					}
				}
			}
		}
		super.doRemoveByBatch(ids);
		if (!entriesId.isEmpty()) {
			Integer[] entryIds = entriesId.toArray(new Integer[] {});
			dictEntryService.doRemoveByBatch(entryIds);
		}
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries" }, allEntries = true)
	public DictType doSaveOrUpdate(DictType entity) {
		return super.doSaveOrUpdate(entity);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries" }, allEntries = true)
	public void doDeleteById(Integer id) {
		super.doDeleteById(id);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries" }, allEntries = true)
	public void doDeleteById(Integer[] ids) {
		super.doDeleteById(ids);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries" }, allEntries = true)
	public void doRemoveById(Integer id) {
		super.doRemoveById(id);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "pageEntry", Constants.CACHE_NAME_PREFIX + "dictEntries" }, allEntries = true)
	public void doUpdateBatch(List<DictType> entities) {
		super.doUpdateBatch(entities);
	}

}
