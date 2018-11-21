package com.wis.mes.master.path.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TmPathPaths
 * 
 * @author liuzejun
 *
 * @date 2017年6月7日 下午6:03:28
 * 
 * @Description:工艺路径连线信息
 */
@Table(name = "tm_path_ligature")
public class TmPathLigature extends AuditEntity {

	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Integer id;
	/** 工艺路径主键 */
	private Integer tmPathId;
	/** 编号 */
	private String no;
	/** 开始位置 */
	private String fromDiv;
	/** 指向位置 */
	private String toDiv;
	/** 点 */
	private String dots;
	/** 线名称 */
	private String text;
	/** 线位置 */
	private String textPos;
	
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PATH_LIGATURE_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TM_PATH_ID")
	public Integer getTmPathId() {
		return tmPathId;
	}

	public void setTmPathId(Integer tmPathId) {
		this.tmPathId = tmPathId;
	}

	@Column(name = "NO")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Column(name = "FROM_DIV")
	public String getFromDiv() {
		return fromDiv;
	}

	public void setFromDiv(String fromDiv) {
		this.fromDiv = fromDiv;
	}

	@Column(name = "TO_DIV")
	public String getToDiv() {
		return toDiv;
	}

	public void setToDiv(String toDiv) {
		this.toDiv = toDiv;
	}

	@Column(name = "DOTS")
	public String getDots() {
		return dots;
	}

	public void setDots(String dots) {
		this.dots = dots;
	}

	@Column(name = "TEXT")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "TEXT_POS")
	public String getTextPos() {
		return textPos;
	}

	public void setTextPos(String textPos) {
		this.textPos = textPos;
	}

}
