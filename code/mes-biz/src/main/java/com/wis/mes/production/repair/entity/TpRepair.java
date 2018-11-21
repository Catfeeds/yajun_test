package com.wis.mes.production.repair.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpRepair
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午2:32:40
 * 
 * @Description: 返修主表
 */
@Table(name = "tp_repair")
public class TpRepair extends AuditEntity {

	/** 主键 */
	private Integer id;
	/** 返修单号 */
	private String repairNo;
	/** SN编号 */
	private String sn;
	/** 返修工艺路径ID */
	private Integer tpPathId;
	/** 返修开始时间 */
	private String startTime;
	/** 返修结束时间 */
	private String endTime;
	/** 返修状态(NEW 新单、ING返修中、FINISH返修完成) */
	private String status;
	/** 是否返修成功 */
	private String isOk;
	/** 备注 */
	private String note;

	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "REPAIR_NO")
	public String getRepairNo() {
		return repairNo;
	}

	public void setRepairNo(String repairNo) {
		this.repairNo = repairNo;
	}

	@Column(name = "SN")
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "TP_PATH_ID")
	public Integer getTpPathId() {
		return tpPathId;
	}

	public void setTpPathId(Integer tpPathId) {
		this.tpPathId = tpPathId;
	}

	@Column(name = "START_TIME")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "IS_OK")
	public String getIsOk() {
		return isOk;
	}

	public void setIsOk(String isOk) {
		this.isOk = isOk;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
