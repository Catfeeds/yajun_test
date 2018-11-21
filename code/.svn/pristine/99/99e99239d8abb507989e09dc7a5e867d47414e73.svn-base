package com.wis.mes.master.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.wis.core.dao.OrderBy;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.entity.AbstractEntity;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.base.dao.MasterBaseDao;
import com.wis.mes.master.base.service.MasterBaseService;

/**
 * @author Jixueyuan
 * @date 2017年6月14日
 * @Description: 主数据BaseServiceImpl
 */
public class MasterBaseServiceImpl<T extends AbstractEntity> extends BaseServiceImpl<T>
		implements MasterBaseService<T> {

	protected MasterBaseDao<T> dao;

	@Autowired
	public void setMasterBaseDao(MasterBaseDao<T> dao) {
		super.dao = dao;
	}

	@Override
	public T findById(Integer id) {
		return dao.get(id);
	}

	@Override
	public List<T> findAll(OrderBy... orderBies) {
		return dao.findAll(orderBies);
	}

	@Override
	public List<T> findAll(boolean isRequiredPermisson, OrderBy... orderBies) {
		if (isRequiredPermisson) {
			return this.findAll(orderBies);
		} else {
			return super.findAll(orderBies);
		}
	}

	@Override
	public List<T> findByEg(T eg) {
		return dao.findByEg(eg);
	}

	@Override
	public T findUniqueByEg(T eg) {
		return dao.findUniqueByEg(eg);
	}

	@Override
	public List<T> findAllInIds(Integer[] ids) {
		return dao.findAllInIds(ids);
	}

	@Override
	public T findById(Integer id, boolean queryRelationEntity) {
		if (!queryRelationEntity) {
			return findById(id);
		}
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryRelationEntity(true);
		criteria.addQueryCondition("id", id.toString());
		criteria.setRows(1);
		criteria.setQueryPage(false);
		criteria.setExcludeFields(new ArrayList<String>());
		criteria.setIncludesReomved(true);
		criteria.setUseCacheSql(false);
		PageResult<T> result = dao.findBy(criteria);
		if (result.getContent().size() == 1) {
			return result.getContent().get(0);
		}
		return null;
	}

	@Override
	public PageResult<T> findBy(QueryCriteria queryCriteria) {
		return dao.findBy(queryCriteria);
	}

	@Override
	public T findById(boolean isRequiredPermisson, Integer id) {
		if (isRequiredPermisson) {
			return this.findById(id);
		} else {
			return super.findById(id);
		}
	}

	@Override
	public List<T> findAllInIds(Integer[] ids, boolean isRequiredPermisson) {
		if (isRequiredPermisson) {
			return this.findAllInIds(ids);
		} else {
			return super.findAllInIds(ids);
		}
	}

	@Override
	public T findById(Integer id, boolean queryRelationEntity, boolean isRequiredPermisson) {
		if (isRequiredPermisson) {
			return this.findById(id, queryRelationEntity);
		} else {
			return super.findById(id, queryRelationEntity);
		}
	}

}
