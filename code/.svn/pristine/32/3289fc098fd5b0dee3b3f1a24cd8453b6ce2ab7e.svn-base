package com.wis.mes.master.boardmanage.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AbstractEntity;

/**
 * @author yajun_ren
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2018年9月5日
 *
 * @desc 区域管理
 */
@Table(name = "tm_board_manage")
public class TmBoardManage extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/** id */
	private Integer id;
	/**设备IDS**/
	private String tmEquipmentIds;
	/**机台**/
	private String tmEquipmentNames;
	/**工程数**/
	private String stepNumber;
	/**区域标识**/
	private String regionMark;
	/**PLC编号**/
	private String plcNo;
	/**备注**/
	private String remarks;
	/**目标可动率*/
	private Integer targetMobility ;
	
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_BOARD_MANAGE_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "TM_EQUIPMENT_IDS")
	public String getTmEquipmentIds() {
		return tmEquipmentIds;
	}

	public void setTmEquipmentIds(String tmEquipmentIds) {
		this.tmEquipmentIds = tmEquipmentIds;
	}
	
	@Column(name = "TM_EQUIPMENT_NAMES")
	public String getTmEquipmentNames() {
		return tmEquipmentNames;
	}

	public void setTmEquipmentNames(String tmEquipmentNames) {
		this.tmEquipmentNames = tmEquipmentNames;
	}

	@Column(name = "STEP_NUMBER")
	public String getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(String stepNumber) {
		this.stepNumber = stepNumber;
	}

	@Column(name = "REGION_MARK")
	public String getRegionMark() {
		return regionMark;
	}
	public void setRegionMark(String regionMark) {
		this.regionMark = regionMark;
	}
	
	@Column(name = "PLC_NO")
	public String getPlcNo() {
		return plcNo;
	}
	public void setPlcNo(String plcNo) {
		this.plcNo = plcNo;
	}

	@Column(name="REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public static TmBoardManage createRegionMark(String regionMark) {
		TmBoardManage bean = new TmBoardManage();
		bean.setRegionMark(regionMark);
		return bean;
	}

	@Column(name="TARGET_MOBILITY")
	public Integer getTargetMobility() {
		return targetMobility;
	}

	public void setTargetMobility(Integer targetMobility) {
		this.targetMobility = targetMobility;
	}
	
}
