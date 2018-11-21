package com.wis.mes.master.path.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.workshop.entity.TmWorkshop;

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
@Table(name = "tm_path")
public class TmPath extends AuditEntity {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 工厂主键 */
	private Integer tmPlantId;
	/** 工厂bean */
	private TmPlant plant;
	/** 车间主键 */
	private Integer tmWorkshopId;
	/** 车间bean */
	private TmWorkshop workshop;
	/** 产线主键 */
	private Integer tmLineId;
	/** 产线bean */
	private TmLine line;
	/** 产品主键 */
	private Integer tmPartId;
	/** 产品bean */
	private TmPart part;
	/** 编码 */
	private String no;
	/** 描述 */
	private String name;
	/** 来源工艺路径主键 */
	private Integer copyPathId;
	/** 来源工艺路径Bean */
	private TmPath copyPath;
	/** 维护状态 */
	private String status;
	/** 启用 */
	private String enabled;
	
	/** 备注*/
	private String remark;
	
	/** 背番号*/
	private Integer backNumber;
	
	/** 机种名*/
	private String machineOfName;
	

	@Override
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_PATH_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(final Integer id) {
		this.id = id;
	}

	@Column(name = "TM_PLANT_ID")
	public Integer getTmPlantId() {
		return tmPlantId;
	}

	public void setTmPlantId(final Integer tmPlantId) {
		this.tmPlantId = tmPlantId;
	}

	@Column(name = "TM_WORKSHOP_ID")
	public Integer getTmWorkshopId() {
		return tmWorkshopId;
	}

	public void setTmWorkshopId(final Integer tmWorkshopId) {
		this.tmWorkshopId = tmWorkshopId;
	}

	@Column(name = "TM_LINE_ID")
	public Integer getTmLineId() {
		return tmLineId;
	}

	public void setTmLineId(final Integer tmLineId) {
		this.tmLineId = tmLineId;
	}

	@Column(name = "TM_PART_ID")
	public Integer getTmPartId() {
		return tmPartId;
	}

	public void setTmPartId(final Integer tmPartId) {
		this.tmPartId = tmPartId;
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

	@Column(name = "COPY_PATH_ID")
	public Integer getCopyPathId() {
		return copyPathId;
	}

	public void setCopyPathId(final Integer copyPathId) {
		this.copyPathId = copyPathId;
	}

	@Column(name = "ENABLED")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(final String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "TM_PLANT_ID")
	public TmPlant getPlant() {
		return plant;
	}

	public void setPlant(final TmPlant plant) {
		this.plant = plant;
	}

	@Column(name = "TM_WORKSHOP_ID")
	public TmWorkshop getWorkshop() {
		return workshop;
	}

	public void setWorkshop(final TmWorkshop workshop) {
		this.workshop = workshop;
	}

	@Column(name = "TM_LINE_ID")
	public TmLine getLine() {
		return line;
	}

	public void setLine(final TmLine line) {
		this.line = line;
	}

	@Column(name = "TM_PART_ID")
	public TmPart getPart() {
		return part;
	}

	public void setPart(final TmPart part) {
		this.part = part;
	}

	@Column(name = "COPY_PATH_ID")
	public TmPath getCopyPath() {
		return copyPath;
	}

	public void setCopyPath(TmPath copyPath) {
		this.copyPath = copyPath;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="BACK_NUMBER")
	public Integer getBackNumber() {
		return backNumber;
	}

	public void setBackNumber(Integer backNumber) {
		this.backNumber = backNumber;
	}

	@Column(name="MACHINE_OF_NAME")
	public String getMachineOfName() {
		return machineOfName;
	}

	public void setMachineOfName(String machineOfName) {
		this.machineOfName = machineOfName;
	}

	
}
