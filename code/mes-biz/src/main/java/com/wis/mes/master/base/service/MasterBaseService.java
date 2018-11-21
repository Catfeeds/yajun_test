package com.wis.mes.master.base.service;

import java.util.List;

import com.wis.core.dao.OrderBy;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.entity.AbstractEntity;
import com.wis.core.service.BaseService;

/**
 * @author Jixueyuan
 * @date 2017年6月14日
 * @Description: 主数据BaseService
 */
public interface MasterBaseService<T extends AbstractEntity> extends BaseService<T> {

	@Override
	public T findById(Integer id);

	public T findById(boolean isRequiredPermisson, Integer id);

	@Override
	public List<T> findAll(OrderBy... orderBies);

	public List<T> findAll(boolean isRequiredPermisson, OrderBy... orderBies);

	@Override
	public List<T> findByEg(T eg);

	@Override
	public T findUniqueByEg(T eg);

	@Override
	public List<T> findAllInIds(Integer[] ids);

	public List<T> findAllInIds(Integer[] ids, boolean isRequiredPermisson);

	@Override
	public T findById(Integer id, boolean queryRelationEntity);

	public T findById(Integer id, boolean queryRelationEntity, boolean isRequiredPermisson);

	@Override
	public PageResult<T> findBy(QueryCriteria queryCriteria);
}
