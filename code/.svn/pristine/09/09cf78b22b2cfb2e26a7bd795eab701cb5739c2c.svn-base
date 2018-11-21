/**
 * 
 */
package com.wis.mes.production.metalplate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.basis.systemadmin.entity.Employee;
import com.wis.core.entity.AbstractEntity;

/**
 * @author caixia
 *
 */
@Table(name = "TB_SCHEDULING_RECOR_LOG")
public class TbSchedulingRecorLog extends AbstractEntity {

	private Integer id;
	/***钣金图号**/
	private String machiningSurface;
	private String status;
	private Date createTime;
	private Integer createUser;
	/**计划需求日期**/
	private String taskTime;
	private Employee employee;
	/**派工数**/
	private Integer dispatchNumber;
	/**成品数**/
	private Integer finishNumber;
	/**生产顺位**/
	private String synPosition;

	/**
	 * @return the id
	 */
	@SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_SCHEDULING_RECOR_LOG_ID")
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the machiningSurface
	 */
	@Column(name = "MACHINING_SURFACE")
	public String getMachiningSurface() {
		return machiningSurface;
	}

	/**
	 * @param machiningSurface the machiningSurface to set
	 */
	public void setMachiningSurface(String machiningSurface) {
		this.machiningSurface = machiningSurface;
	}

	/**
	 * @return the status
	 */
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the createTime
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", updatable = false)
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the createUser
	 */
	@Column(name = "CREATE_USER")
	public Integer getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the taskTime
	 */
	@Column(name = "TASK_TIME")
	public String getTaskTime() {
		return taskTime;
	}

	/**
	 * @param taskTime the taskTime to set
	 */
	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}

	/**
	 * @return the employee
	 */
	@Column(name = "CREATE_USER")
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Column(name = "DISPATCH_NUMBER")
	public Integer getDispatchNumber() {
		return dispatchNumber;
	}

	public void setDispatchNumber(Integer dispatchNumber) {
		this.dispatchNumber = dispatchNumber;
	}

	@Column(name = "FINISH_NUMBER")
	public Integer getFinishNumber() {
		return finishNumber;
	}

	public void setFinishNumber(Integer finishNumber) {
		this.finishNumber = finishNumber;
	}

	@Column(name = "SYN_POSITION")
	public String getSynPosition() {
		return synPosition;
	}

	public void setSynPosition(String synPosition) {
		this.synPosition = synPosition;
	}

}
