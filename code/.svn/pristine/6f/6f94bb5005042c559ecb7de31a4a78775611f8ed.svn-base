package com.wis.mes.production.record.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.part.entity.TmPart;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TpRecord
 * 
 * @author liuzejun
 *
 * @date 2017年6月20日 下午2:17:50
 * 
 * @Description: 产品档案表
 */
@Table(name = "tp_record")
public class TpRecord extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** SN唯一码 */
	private String sn;
	/** 工单编号 */
	private String porderNo;
	/** 生产序列编号 */
	private String aviNo;
	/** 物料ID */
	private Integer tmPartId;
	private TmPart part;
	/** 数量 */
	private Integer qty;
	/** 下线时间 */
	private Date offlineTime;
	/** 上线时间 */
	private Date onlineTime;
	/** 原SN号 */
	private String parentSn;
	/** 入库数量 */
	private Integer inWareQty;

	@Override
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "SN")
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "PORDER_NO")
	public String getPorderNo() {
		return porderNo;
	}

	public void setPorderNo(String porderNo) {
		this.porderNo = porderNo;
	}

	@Column(name = "AVI_NO")
	public String getAviNo() {
		return aviNo;
	}

	public void setAviNo(String aviNo) {
		this.aviNo = aviNo;
	}

	@Column(name = "TM_PART_ID")
	public Integer getTmPartId() {
		return tmPartId;
	}

	public void setTmPartId(Integer tmPartId) {
		this.tmPartId = tmPartId;
	}

	@Column(name = "TM_PART_ID")
	public TmPart getPart() {
		return part;
	}

	public void setPart(TmPart part) {
		this.part = part;
	}

	@Column(name = "QTY")
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "OFFLINE_TIME")
	public Date getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "ONLINE_TIME")
	public Date getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}

	@Column(name = "PARENT_SN")
	public String getParentSn() {
		return parentSn;
	}

	public void setParentSn(String parentSn) {
		this.parentSn = parentSn;
	}

	@Column(name = "IN_WARE_QTY")
	public Integer getInWareQty() {
		return inWareQty;
	}

	public void setInWareQty(Integer inWareQty) {
		this.inWareQty = inWareQty;
	}

}
