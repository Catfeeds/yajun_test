package com.wis.mes.production.metalplate.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.metalplate.dao.TbMetalPlateSourceDataDao;
import com.wis.mes.production.metalplate.entity.TbMetalPlateSourceData;

@Repository
public class TbMetalPlateSourceDataDaoImpl extends BaseDaoImpl<TbMetalPlateSourceData>
		implements TbMetalPlateSourceDataDao {

	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		String sql = super.getSqlBy(queryCriteria);
		sql = sql.replace("where 1=1", "where 1=1 and tbmetalplatesourcedata_0.status < 2 ");
		return sql;
	}

	@Override
	public List<TbMetalPlateSourceData> findByCollectEg(TbMetalPlateSourceData eg) {
		return getSqlSession().selectList("SheetMetalMaterialMapper.findSourceDataByCollectEg", eg);
	}

	@Override
	public Integer findBySumPlannedProductionByModel(TbMetalPlateSourceData eg) {
		return getSqlSession().selectOne("SheetMetalMaterialMapper.findBySumPlannedProductionByModel", eg);
	}

	@Override
	public TbMetalPlateSourceData findSourceDataByRecordId(Integer recordId) {
		return getSqlSession().selectOne("SheetMetalMaterialMapper.findSourceDataByRecordId", recordId);
	}

	@Override
	public Integer findConsumeApcTotalBySourceDataId(Integer sourceDataId) {
		return getSqlSession().selectOne("SheetMetalMaterialMapper.findConsumeApcTotalBySourceDataId", sourceDataId);
	}

	@Override
	public Integer findInProductionNumBySourceDataId(Integer sourceDataId) {
		return getSqlSession().selectOne("SheetMetalMaterialMapper.findInProductionNumBySourceDataId", sourceDataId);
	}

	@Override
	public Integer findInventoryNumberByDate(Map<String, String> query) {
		return getSqlSession().selectOne("SheetMetalMaterialMapper.findInventoryNumberByDate", query);
	}
}
