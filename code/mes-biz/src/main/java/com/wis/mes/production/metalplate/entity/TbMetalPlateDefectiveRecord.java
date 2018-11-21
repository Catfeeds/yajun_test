package com.wis.mes.production.metalplate.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.core.entity.Removable;

/**
 * @author CFY
 * @company 上海西信信息科技有限公司
 * @date 2018年7月9日
 * @desc 钣金生产管理，生产缺陷记录信息bean
 */
@Table(name = "TB_MP_DEFECTIVE_RECORD")
public class TbMetalPlateDefectiveRecord extends AuditEntity implements
		Removable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 关联生产记录ID */
	private Integer productionRecordId;
	private TbMetalPlateProductionRecord productionRecord;
	/** 缺陷项目 */
	private String defectiveProject;
	/** 缺陷数量 */
	private Integer defectiveNumber;

	/**部品不良IDS**/
	private String tmBadpartsIds;
	
	private Integer removed;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_MP_DEFECTIVE_RECORD_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "PRODUCTION_RECORD_ID")
	public Integer getProductionRecordId() {
		return productionRecordId;
	}

	public void setProductionRecordId(Integer productionRecordId) {
		this.productionRecordId = productionRecordId;
	}

	@Column(name = "PRODUCTION_RECORD_ID")
	public TbMetalPlateProductionRecord getProductionRecord() {
		return productionRecord;
	}

	public void setProductionRecord(
			TbMetalPlateProductionRecord productionRecord) {
		this.productionRecord = productionRecord;
	}

	@Column(name = "DEFECTIVE_PROJECT")
	public String getDefectiveProject() {
		return defectiveProject;
	}

	public void setDefectiveProject(String defectiveProject) {
		this.defectiveProject = defectiveProject;
	}

	@Column(name = "DEFECTIVE_NUMBER")
	public Integer getDefectiveNumber() {
		return defectiveNumber;
	}

	public void setDefectiveNumber(Integer defectiveNumber) {
		this.defectiveNumber = defectiveNumber;
	}

	@Column(name = "MARK_FOR_DELETE")
	public Integer getRemoved() {
		return removed;
	}

	@Override
	public boolean isEntityRemoved() {
		return (null == removed || removed == 0) ? false : true;
	}

	@Override
	public void setRemoved(Integer removed) {
		this.removed = removed;
	}

	@Column(name = "TM_BAD_PARTS_IDS")
	public String getTmBadpartsIds() {
		return tmBadpartsIds;
	}

	public void setTmBadpartsIds(String tmBadpartsIds) {
		this.tmBadpartsIds = tmBadpartsIds;
	}
	
	
}
