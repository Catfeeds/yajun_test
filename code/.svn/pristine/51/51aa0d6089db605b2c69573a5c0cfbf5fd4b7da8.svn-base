package com.wis.mes.master.coderule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.line.entity.TmLine;

@Entity
@Table(name = "TM_CODE_RULE")
public class TmCodeRule extends AuditEntity {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 区域 */
	private Integer tmLineId;
	private TmLine line;
	/** 区分 */
	private String distinguish;
	/** 数据源 */
	private String dataSource;
	/** 编号 */
	private String code;
	/** 描述 */
	private String codeDesc;
	/** PLC code */
	private String plcCode;
	private String note;

	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_CODE_RULE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "TM_LINE_ID")
	public TmLine getLine() {
		return line;
	}

	public void setLine(TmLine line) {
		this.line = line;
	}

	@Column(name = "DISTINGUISH")
	public String getDistinguish() {
		return distinguish;
	}

	public void setDistinguish(String distinguish) {
		this.distinguish = distinguish;
	}

	@Column(name = "DATA_SOURCE")
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "CODE_DESC")
	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}

	@Column(name = "PLC_CODE")
	public String getPlcCode() {
		return plcCode;
	}

	public void setPlcCode(String plcCode) {
		this.plcCode = plcCode;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
