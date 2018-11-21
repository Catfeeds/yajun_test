package com.wis.core.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wis.core.framework.dao.GlobalConfigurationDao;
import com.wis.core.framework.entity.GlobalConfiguration;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.service.impl.BaseServiceImpl;

@Service(value = "globalConfigurationService")
public class GlobalConfigurationServiceImpl extends BaseServiceImpl<GlobalConfiguration> implements GlobalConfigurationService {

	@Autowired
	public void setDao(GlobalConfigurationDao dao) {
		this.dao = dao;
	}

	/**
	 * 根据Key查询配置
	 */
	//, unless="#result == null"
	@Override
	@Cacheable(value = Constants.CACHE_NAME_PREFIX + "globalConfiguration", key = "new String('" + Constants.CACHE_NAME_PREFIX + "globalConfigurationService.findGlobeConfigurationByKey')+#key")
	public GlobalConfiguration findGlobeConfigurationByKey(String key) {
		GlobalConfiguration globeConfiguration = dao.findUniqueByEg(GlobalConfiguration.createGcByKey(key));
		return globeConfiguration;
	}

	@Override
	@Cacheable(value = Constants.CACHE_NAME_PREFIX + "globalConfiguration", key = "new String('" + Constants.CACHE_NAME_PREFIX + "globalConfigurationService.findById')+#id")
	public GlobalConfiguration findById(Integer id) {
		return super.findById(id);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "globalConfiguration" }, allEntries = true)
	public GlobalConfiguration doSaveOrUpdate(GlobalConfiguration entity) {
		return super.doSaveOrUpdate(entity);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "globalConfiguration" }, allEntries = true)
	public void doDeleteById(Integer id) {
		super.doDeleteById(id);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "globalConfiguration" }, allEntries = true)
	public void doDeleteById(Integer[] ids) {
		super.doDeleteById(ids);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "globalConfiguration" }, allEntries = true)
	public void doRemoveById(Integer id) {
		super.doRemoveById(id);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "globalConfiguration" }, allEntries = true)
	public void doRemoveByBatch(Integer[] ids) {
		super.doRemoveByBatch(ids);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "globalConfiguration" }, allEntries = true)
	public GlobalConfiguration doUpdate(GlobalConfiguration entity) {
		return super.doUpdate(entity);
	}

	@Override
	@CacheEvict(value = { Constants.CACHE_NAME_PREFIX + "globalConfiguration" }, allEntries = true)
	public void doUpdateBatch(List<GlobalConfiguration> entities) {
		super.doUpdateBatch(entities);
	}

	@Override
	public String getValueByKey(String key) {
		GlobalConfiguration configuration = findGlobeConfigurationByKey(key);
		if(null == configuration) {
			return "";
		}
		return configuration.getValue();
	}
}
