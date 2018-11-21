package com.wis.mes.master.path.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.uloc.entity.TmUloc;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 工艺路径bean
 *
 */
@Table(name = "tm_path_uloc")
public class TmPathUloc extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 工艺路径主键 */
	private Integer tmPathId;
	/** 工艺路径实体 */
	private TmPath path;
	/** 站点顺序 */
	private String seq;
	/** 站点主键 */
	private Integer tmUlocId;
	/** 站点bean */
	private TmUloc uloc;
	/** 父站点集 */
	private String parentId;
	/** 操作时间 （毫秒） */
	private Long operateTime;
	/** 间隔通过时间（毫秒） */
	private Long intervalTime;
	/** 是否为质检点 */
	private String isSip;
	/** 是否自动入库 */
	private String isAutoInstorage;
	/** 入库等待时间(毫秒) */
	private Long instorageTime;
	/** 是否自动报工 */
	private String isReported;
	/** 对应外系统编码 */
	private String erpUlocCode;
	/** 备注 */
	private String note;
	/** 是否上线点 */
	private String isOnline;
	/** 是否下线点 */
	private String isOffline;
	/** X轴 */
	private String x;
	/** y轴 */
	private String y;
	/** 宽 */
	private String width;
	/** 高 */
	private String height;
	/** DIV的SEQ */
	private String rectSeq;

	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PATH_ULOC_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(final Integer id) {
		this.id = id;
	}

	@Column(name = "TM_PATH_ID")
	public Integer getTmPathId() {
		return tmPathId;
	}

	public void setTmPathId(final Integer tmPathId) {
		this.tmPathId = tmPathId;
	}

	@Column(name = "TM_PATH_ID")
	public TmPath getPath() {
		return path;
	}

	public void setPath(final TmPath path) {
		this.path = path;
	}

	@Column(name = "SEQ")
	public String getSeq() {
		return seq;
	}

	public void setSeq(final String seq) {
		this.seq = seq;
	}

	@Column(name = "TM_ULOC_ID")
	public Integer getTmUlocId() {
		return tmUlocId;
	}

	public void setTmUlocId(final Integer tmUlocId) {
		this.tmUlocId = tmUlocId;
	}

	@Column(name = "TM_ULOC_ID")
	public TmUloc getUloc() {
		return uloc;
	}

	public void setUloc(final TmUloc uloc) {
		this.uloc = uloc;
	}

	@Column(name = "PARENT_ID")
	public String getParentId() {
		return parentId;
	}

	public void setParentId(final String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "OPERATE_TIME")
	public Long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(final Long operateTime) {
		this.operateTime = operateTime;
	}

	@Column(name = "INTERVAL_TIME")
	public Long getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(final Long intervalTime) {
		this.intervalTime = intervalTime;
	}

	@Column(name = "IS_SIP")
	public String getIsSip() {
		return isSip;
	}

	public void setIsSip(final String isSip) {
		this.isSip = isSip;
	}

	@Column(name = "IS_AUTO_INSTORAGE")
	public String getIsAutoInstorage() {
		return isAutoInstorage;
	}

	public void setIsAutoInstorage(final String isAutoInstorage) {
		this.isAutoInstorage = isAutoInstorage;
	}

	@Column(name = "INSTORAGE_TIME")
	public Long getInstorageTime() {
		return instorageTime;
	}

	public void setInstorageTime(final Long instorageTime) {
		this.instorageTime = instorageTime;
	}

	@Column(name = "IS_REPORTED")
	public String getIsReported() {
		return isReported;
	}

	public void setIsReported(final String isReported) {
		this.isReported = isReported;
	}

	@Column(name = "ERP_ULOC_CODE")
	public String getErpUlocCode() {
		return erpUlocCode;
	}

	public void setErpUlocCode(final String erpUlocCode) {
		this.erpUlocCode = erpUlocCode;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	@Column(name = "IS_ONLINE")
	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	@Column(name = "IS_OFFLINE")
	public String getIsOffline() {
		return isOffline;
	}

	public void setIsOffline(String isOffline) {
		this.isOffline = isOffline;
	}

	@Column(name = "X")
	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	@Column(name = "Y")
	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	@Column(name = "WIDTH")
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@Column(name = "HEIGHT")
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Column(name = "RECT_SEQ")
	public String getRectSeq() {
		return rectSeq;
	}

	public void setRectSeq(String rectSeq) {
		this.rectSeq = rectSeq;
	}

}
