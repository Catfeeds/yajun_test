package com.wis.mes.production.stationqueuerecord.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AbstractEntity;

@Table(name = "tb_station_queue_record")
public class TbStationQueueRecord extends AbstractEntity {

	/**
	 * 工位队列校验数据记录
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String plcSn;
	private String readTagSn;
	private String ulocNo;
	private Date createTime;
	private String fruit;
	private String rfidReadFruit;

	@Column(name = "ID")
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_STATION_QUEUE_RECORD_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "PLC_SN")
	public String getPlcSn() {
		return plcSn;
	}

	public void setPlcSn(String plcSn) {
		this.plcSn = plcSn;
	}

	@Column(name = "READ_TAG_SN")
	public String getReadTagSn() {
		return readTagSn;
	}

	public void setReadTagSn(String readTagSn) {
		this.readTagSn = readTagSn;
	}

	@Column(name = "ULOC_NO")
	public String getUlocNo() {
		return ulocNo;
	}

	public void setUlocNo(String ulocNo) {
		this.ulocNo = ulocNo;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", updatable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="FRUIT")
	public String getFruit() {
		return fruit;
	}

	public void setFruit(String fruit) {
		this.fruit = fruit;
	}

	@Column(name="RFID_READ_FRUIT")
	public String getRfidReadFruit() {
		return rfidReadFruit;
	}

	public void setRfidReadFruit(String rfidReadFruit) {
		this.rfidReadFruit = rfidReadFruit;
	}
	
}
