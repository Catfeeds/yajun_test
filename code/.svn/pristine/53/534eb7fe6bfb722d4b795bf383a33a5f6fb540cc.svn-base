package com.wis.mes.master.path.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.equipment.entity.TmEquipmentParam;

/**
 * 工艺路径站点的参数
 * 
 * @author liuzejun
 *
 */
@Table(name = "tm_path_uloc_parameter")
public class TmPathUlocParameter extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 工艺路径站点ID */
	private Integer tmPathUlocId;
	/**old 参数ID */
	//private Integer tsParameterId;
	/**new 2018/3/21 zhuzw 修改为设备参数关系表ID   （TM_EQUIPMENT_PARAM）*/
	private Integer tmEquipmentParamId;
	/**old 参数Bean */
	/**new 2018/3/21 zhuzw 修改为设备参数关系bean （TM_EQUIPMENT_PARAM） */
	//private TsParameter parameter;
	private TmEquipmentParam parameter;
	/** 备注 */
	private String note;
	/** 启用 */
	private String enabled;
	/** 是否必须 */
	private String isRequired;
	/** 节点的ID */
	private String rectSeq;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PATH_ULOC_PARAMETER_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_PATH_ULOC_ID")
	public Integer getTmPathUlocId() {
		return tmPathUlocId;
	}

	public void setTmPathUlocId(Integer tmPathUlocId) {
		this.tmPathUlocId = tmPathUlocId;
	}

//	@Column(name = "TS_PARAMETER_ID")
//	public Integer getTsParameterId() {
//		return tsParameterId;
//	}
//
//	public void setTsParameterId(Integer tsParameterId) {
//		this.tsParameterId = tsParameterId;
//	}
	
	@Column(name = "TM_EQUIPMENT_PARAM_ID")
	public Integer getTmEquipmentParamId() {
		return tmEquipmentParamId;
	}

	public void setTmEquipmentParamId(Integer tmEquipmentParamId) {
		this.tmEquipmentParamId = tmEquipmentParamId;
	}


	@Column(name = "TM_EQUIPMENT_PARAM_ID")
	public TmEquipmentParam getParameter() {
		return parameter;
	}

	
	public void setParameter(TmEquipmentParam parameter) {
		this.parameter = parameter;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "IS_REQUIRED")
	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	@Transient
	public String getRectSeq() {
		return rectSeq;
	}

	public void setRectSeq(String rectSeq) {
		this.rectSeq = rectSeq;
	}

}
