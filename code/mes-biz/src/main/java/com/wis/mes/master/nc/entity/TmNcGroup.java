package com.wis.mes.master.nc.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.line.entity.TmLine;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 不合格代码组bean
 *
 */
@Table(name = "tm_nc_group")
public class TmNcGroup extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 不合格组编码 */
	private String no;
	/** 不合格组描述 */
	private String name;
	/** 对应外系统编码 */
	private String extCode;
	/** 备注 */
	private String remarks;
	
	private Integer tmLineId;
	
	private TmLine tmLine;
	
	private String lineNo;

	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_NC_GROUP_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(final Integer id) {
		this.id = id;
	}

	@Column(name = "NO")
	public String getNo() {
		return no;
	}

	public void setNo(final String no) {
		this.no = no;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "EXT_CODE")
	public String getExtCode() {
		return extCode;
	}

	public void setExtCode(final String extCode) {
		this.extCode = extCode;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name="TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name="TM_LINE_ID")
	public TmLine getTmLine() {
		return tmLine;
	}

	public void setTmLine(TmLine tmLine) {
		this.tmLine = tmLine;
	}

	@Transient
	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	
}
