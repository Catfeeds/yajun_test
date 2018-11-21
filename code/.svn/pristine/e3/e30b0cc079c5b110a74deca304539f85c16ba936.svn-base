package com.wis.mes.production.metalplate.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.metalplate.dao.TbMetalPlateSchedulDao;
import com.wis.mes.production.metalplate.entity.TbMetalPlateSchedul;

@Repository
public class TbMetalPlateSchedulDaoImpl extends BaseDaoImpl<TbMetalPlateSchedul> implements TbMetalPlateSchedulDao {

	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		String sql = super.getSqlBy(queryCriteria);
		sql = sql.replace("where 1=1", "where tbmetalplateschedul_0.status not in (2,9)");
		//sql = sql.replace("ORDER BY tbmetalplateschedul_0.ID","ORDER BY tbmetalplateschedul_0.SORT ASC,tbmetalplateschedul_0.ID");
		sql = sql.replace("ORDER BY tbmetalplateschedul_0.ID","ORDER BY tbmetalplateschedul_0.REGION_MARK ASC, tbmetalplateschedul_0.SORT ASC, tbmetalplateschedul_0.ID ");
		return sql;
	}

	@Override
	public TbMetalPlateSchedul findRelevanceById(Map<String, Object> params) {
		return getSqlSession().selectOne("SheetMetalMaterialMapper.findRelevanceById", params);
	}

	@Override
	public Map<String, Object> findSchedulEgByPress(String press) {
		return getSqlSession().selectOne("SheetMetalMaterialMapper.findSchedulEgByPress", press);
	}

    @Override
    public PageResult<Map<String,Object>> pmcReport(BootstrapTableQueryCriteria criteria) {
        PageResult<Map<String,Object>> pageResult = new PageResult();
        Integer offsetIndex = criteria.getCurrentIndex();
        Integer pageSize = criteria.getRows();
        pageResult.setCurrentIndex(criteria.getCurrentIndex());
        pageResult.setPageSize(criteria.getRows());
        Map<String, Object> params = criteria.getQueryCondition();

        pageResult.setTotalCount(pmcReportCnt(params));
        params.put("firstRow", offsetIndex);
        params.put("pageSize", (pageSize * criteria.getPage()));

        if (0 != pageSize) {
            pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
            pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
        }
        List<Map<String,Object>> content = getSqlSession().selectList("SheetMetalMaterialMapper.pmcReport", params);
        pageResult.setContent(content);
        return pageResult;
    }

    private int pmcReportCnt(Map<String, Object> params) {
        return getSqlSession().selectOne("SheetMetalMaterialMapper.pmcReportCnt", params);
    }

	@Override
	public List<Map<String,Object>> queryPMCBoardProductionInfo() {
		return getSqlSession().selectList("SheetMetalMaterialMapper.queryPMCBoardProductionInfo");
	}

	@Override
	public int queryBeProductionNumberByRegionMark(String regionMark) {
		return getSqlSession().selectOne("SheetMetalMaterialMapper.queryBeProductionNumberByRegionMark", regionMark);
	}

	@Override
	public TbMetalPlateSchedul queryNextSchedulByRegionMark(String regionMark) {
		return getSqlSession().selectOne("SheetMetalMaterialMapper.queryNextSchedulByRegionMark", regionMark);
	}

	@Override
	public int doUnLock(TbMetalPlateSchedul mps) {
		return getSqlSession().update("SheetMetalMaterialMapper.doLock", mps);
	}

	@Override
	public int doLock(TbMetalPlateSchedul mps) {
		return getSqlSession().update("SheetMetalMaterialMapper.doLock", mps);
	}

	@Override
	public int doMandatoryEnd(TbMetalPlateSchedul mps) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void doWorkOrderStick(Map<String, Object> params) {
		getSqlSession().update("SheetMetalMaterialMapper.doWorkOrderStick", params);
	}
}
