package com.wis.mes.master.nc.entity;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wis.core.entity.AuditEntity;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 报废代码bean
 *
 */
@Table(name = "tm_scrap")
public class TmScrap extends AuditEntity {
    private static final long serialVersionUID = 1L;
    /** 主键 */
    private Integer id;
    /** 报废编码 */
    private String no;
    /** 报废描述 */
    private String note;
    /** 对应外系统编码 */
    private String extCode;

    @Override
    @SequenceGenerator(name = "generator", allocationSize = 1, sequenceName = "SEQ_SCRAP_ID")
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

    @Column(name = "NOTE")
    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    @Column(name = "EXT_CODE")
    public String getExtCode() {
        return extCode;
    }

    public void setExtCode(final String extCode) {
        this.extCode = extCode;
    }

}
