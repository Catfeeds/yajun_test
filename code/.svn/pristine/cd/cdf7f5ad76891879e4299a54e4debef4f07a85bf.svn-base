package com.wis.mes.production.tksenergy.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.core.entity.AbstractEntity;

@Table(name = "TKS_ENERGY")
public class TksEnergy extends AbstractEntity {

	/**
	 * 工位队列校验数据记录
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String acEnergy;//空调能耗 
	private String lightEnergy;//照明能耗 
	private String deviceEnergy;//设别能耗 
	private Date createTime;//创建时间 

	@Column(name = "ID")
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_STATION_QUEUE_RECORD_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="AC_ENERGY")
	public String getAcEnergy() {
		return acEnergy;
	}

	public void setAcEnergy(String acEnergy) {
		this.acEnergy = acEnergy;
	}

	@Column(name="LIGHT_ENERGY")
	public String getLightEnergy() {
		return lightEnergy;
	}

	public void setLightEnergy(String lightEnergy) {
		this.lightEnergy = lightEnergy;
	}

	@Column(name="DEVICE_ENERGY")
	public String getDeviceEnergy() {
		return deviceEnergy;
	}

	public void setDeviceEnergy(String deviceEnergy) {
		this.deviceEnergy = deviceEnergy;
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
	
}
