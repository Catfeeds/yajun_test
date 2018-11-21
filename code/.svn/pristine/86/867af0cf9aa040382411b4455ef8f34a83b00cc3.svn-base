package com.wis.mes.master.badparts.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.line.entity.TmLine;

/**
 *
 * @author yajun_ren
 * @company 上海西信信息科技有限公司
 * @date 2018/08/31
 * @desc 部品不良
 *
 */
@Table(name = "tm_bad_parts")
public class TmBadParts extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 不良编码 */
	private String no;
	/** 不良描述 */
	private String name;
	/** 备注 */
	private String remarks;
	
	private Integer tmLineId;
	
	private TmLine tmLine;
	
	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_BAD_PARTS_ID")
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
	
	public static TmBadParts createNo(String no) {
		TmBadParts bean = new TmBadParts();
		bean.setNo(no);
		return bean;
	}
}
