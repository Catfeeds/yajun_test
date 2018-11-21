package com.wis.mes.production.plan.porder.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.plan.porder.dao.ToPorderDao;
import com.wis.mes.production.plan.porder.entity.ToPorder;

@Repository
public class ToPorderDaoImpl extends BaseDaoImpl<ToPorder> implements ToPorderDao {

	@Override
	public void deleteAviByPorderId(Integer toPorderId) {
		getSqlSession().delete("PorderMapper.deleteAviByPorderId", toPorderId);
	}

	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		Map<String, Object> queryCondition = queryCriteria.getQueryCondition();
		if (queryCondition.containsKey("planStartTimeEnd")) {
			queryCondition.put("planEndTime", queryCondition.get("planStartTimeEnd"));
			queryCondition.remove("planStartTimeEnd");
			String showClose = "";
			if (queryCondition.containsKey("showClose")) {
				showClose = queryCondition.get("showClose").toString();
				queryCondition.remove("showClose");
			}
			String hql = super.getSqlBy(queryCriteria);
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO.equals(showClose)) {
				hql = hql.replace("where 1=1", "where 1=1 and toporder_0.PRODUCT_STATUS !='"
						+ ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_CLOSE + "' ");
			}
			hql = hql.replace("and toporder_0.PLAN_END_TIME", "and toporder_0.PLAN_START_TIME");
			return hql;
		}
		String showClose = "";
		if (queryCondition.containsKey("showClose")) {
			showClose = queryCondition.get("showClose").toString();
			queryCondition.remove("showClose");
		}
		String hql = super.getSqlBy(queryCriteria);
		if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO.equals(showClose)) {
			hql = hql.replace("where 1=1", "where 1=1 and toporder_0.PRODUCT_STATUS !='"
					+ ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_CLOSE + "' ");
		}
		return hql;
	}
}
