package com.wis.mes.master.nc.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.SpiltUtils;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.AuditConfiguration;
import com.wis.core.framework.entity.AuditLog;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditConfigurationService;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.core.utils.SerialNumUtil;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.nc.dao.TmNcDao;
import com.wis.mes.master.nc.entity.TmFaultGrade;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.entity.TmNcGroup;
import com.wis.mes.master.nc.service.TmFaultGradeService;
import com.wis.mes.master.nc.service.TmNcGroupService;
import com.wis.mes.master.nc.service.TmNcService;
import com.wis.mes.master.nc.vo.NGVo;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.util.StringUtil;

@Service
public class TmNcServiceImpl extends BaseServiceImpl<TmNc> implements TmNcService {

    @Autowired
    public void setDao(final TmNcDao dao) {
        this.dao = dao;
    }

    @Autowired
    private TmNcGroupService ncGroupService;
    @Autowired
    private GlobalConfigurationService globalConfigurationService;
    @Autowired
    private AuditLogService logService;
    @Autowired
    private AuditConfigurationService configurationService;
    @Autowired
    private ImportLogService importLogService;
    @Autowired
    private TmUlocService ulocService;
    @Autowired
    private TmFaultGradeService faultGradeService;
    @Autowired
	private TmNcGroupService tmNcGroupService;
	@Autowired
	private TmLineService tmLineService;

    @Override
    public List<TmNc> findByNcGroupId(final Integer ncGroupId) {
        return ((TmNcDao) dao).findByNcGroupId(ncGroupId);
    }

    @Override
    public Map<String, Object> exportExcelData(final HttpServletResponse response, final List<TmNc> list,
            final String filePath, final String[] header) {
        final List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
        final Map<Integer,TmUloc>  ulocMap = ulocService.ulocMap(null);
        if (list != null && list.size() > ConstantUtils.MATH_ZERO) {
            // 不合格组Map
            final Map<Integer, String> ncGroupMap = new HashMap<Integer, String>();
            for (final TmNcGroup bean : ncGroupService.findAll()) {
                ncGroupMap.put(bean.getId(), bean.getNo() + ConstantUtils.STRING_ROD + bean.getName());
            }
            // 将每列数据值与标题对应
            for (final TmNc bean : list) {
                final Map<String, Object> map = new HashMap<String, Object>();
                String ngLevel = "";
                StringBuffer ngEntranceBuf = new StringBuffer();
                if(null != bean.getFaultGrade()){
                	if(StringUtils.isNotBlank(bean.getFaultGrade().getNgLevel())){
                		ngLevel = bean.getFaultGrade().getNgLevel();
                	}
                	if(StringUtils.isNotBlank(bean.getFaultGrade().getNgEntrance())){
                		String[] ngEntrances = bean.getFaultGrade().getNgEntrance().split(",");
                		for(String ngEntrance:ngEntrances){
                			if(ulocMap.containsKey(Integer.valueOf(ngEntrance)) && null != ulocMap.get(Integer.valueOf(ngEntrance))){
                				ngEntranceBuf.append(ulocMap.get(Integer.valueOf(ngEntrance)).getNo()+",");
                			}
                		}
                		if(ngEntranceBuf.length()>0){
                			ngEntranceBuf = ngEntranceBuf.deleteCharAt(ngEntranceBuf.length()-1);
                		}
                	}
                }
                map.put("故障编号", StringUtil.getString(bean.getNo()));
                map.put("故障描述", StringUtil.getString(bean.getName()));
                map.put("故障等级", StringUtil.getString(ngLevel));
                map.put("NG出口", StringUtil.getString(ngEntranceBuf));
                map.put("备注", StringUtil.getString(bean.getRemarks()));
                exportDataList.add(map);
            }
            return BaseExcelUtil.exportData(response, exportDataList, filePath, header);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
	public String doImportExcelData(final Workbook workbook, final HttpServletRequest req, final String tmNcGroupId) {
        // 覆盖或更新标识
        final String repeatOrUpdateFlag = globalConfigurationService
                .getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
        // 回滚标识
        final String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);

        // 获取不合格代码Map
        final Map<String, TmNc> ncMap = new HashMap<String, TmNc>();
        final TmNc eg = new TmNc();
        eg.setTmNcGroupId(Integer.valueOf(tmNcGroupId));
        for (final TmNc nc : findByEg(eg)) {
            ncMap.put(nc.getTmNcGroupId()+"-"+nc.getNo(), nc);
        }
        final Map<String,TmFaultGrade> faultGradeMap = new HashMap<String,TmFaultGrade>();
        List<TmFaultGrade> faultGrades = faultGradeService.findAll();
        for(TmFaultGrade faultGrade:faultGrades){
        	faultGradeMap.put(faultGrade.getNgLevel(), faultGrade);
        }
        // 产线Map
 		final Map<Integer, String> lineMap = new HashMap<Integer, String>();
 		for (final TmLine e : tmLineService.findAll()) {
 			lineMap.put(e.getId(),e.getNo());
 		}
 		// 故障组
 		final Map<Integer, TmNcGroup> ncGroupMap = new HashMap<Integer, TmNcGroup>();
 		for (final TmNcGroup e : ncGroupService.findAll()) {
 			ncGroupMap.put(e.getId(),e);
 		}
        // 需要插入的对象List容器
        final List<TmNc> addList = new ArrayList<TmNc>();
        // 需要更新的对象Map容器 (Excel的行数:对象)
        final Map<Integer, TmNc> updateMap = new HashMap<Integer, TmNc>();
        // 格式错误的信息容器
        final List<String> errorInfos = new ArrayList<String>();
        final String DI = getMessage(req, "DI");
        // 读取第一章表格内容
        final Sheet sheet = workbook.getSheetAt(0);
        String value = null;
        Row row;
		int judgeSize = 3;// 数据表格的列数（字段数）
		int totalInt = 0;
        // 循环输出表格中的内容
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i); // 获得行
            int index = 0;
            final TmNc entity = new TmNc();
            // 添加tmNcGroupId
            entity.setTmNcGroupId(Integer.valueOf(tmNcGroupId));
            // 不合格代码
            value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {// 第一格为空
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			if(ncGroupMap.containsKey(Integer.valueOf(tmNcGroupId))) {
				TmNcGroup ncGroup = ncGroupMap.get(Integer.valueOf(tmNcGroupId));
				if(null != ncGroup.getTmLineId()) {
					if(lineMap.containsKey(ncGroup.getTmLineId())) {
						entity.setNo(createTemplateNo(lineMap.get(ncGroup.getTmLineId())+ncGroup.getNo()));
					}
				}else {
					errorInfos.add(DI + (i + 1) + getMessage(req, "ERROR_KEY","故障机组："+ncGroup.getName()+"没有维护产线。"));
	                continue;
				}
			}
//            // 判断是否为空
//            if (StringUtils.isBlank(value)) {
//                errorInfos.add(DI + (i + 1) + getMessage(req, "NC_IMPORT_ERROR_TWO"));
//                continue;
//            }
//            // 判断长度
//            if (value.length() > 100) {
//                errorInfos.add(DI + (i + 1) + getMessage(req, "NC_IMPORT_ERROR_NINE"));
//                continue;
//            }
//            // 判断不合格组编码是否符合规则 编号只允许输入英文、数字、中划线-、下划线_和反斜杠/
//            if ((!Pattern.compile("[\\w\\d-_\\\\\\/]+").matcher(value).find())
//                    || Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find()) {
//                errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_DATA_VALID_ERROR_NO"));
//                continue;
//            }
//            entity.setNo(value);

            // 不合格名称
          //  index++;
          //value = LoadUtils.getCell(row, index);
            // 判断信息是否为空
            if (StringUtils.isBlank(value)) {
                errorInfos.add(DI + (i + 1) + getMessage(req, "NC_IMPORT_ERROR_FOUR"));
                continue;
            }
            // 判断长度
            if (value.length() > 150) {
                errorInfos.add(DI + (i + 1) + getMessage(req, "NC_IMPORT_ERROR_TEN"));
                continue;
            }
            entity.setName(value);
            
            // 故障等级
            index++;
            value = LoadUtils.getCell(row, index);
            // 判断如果不为空
            if (!StringUtils.isBlank(value)) {
            		if(faultGradeMap.containsKey(value)){
            			TmFaultGrade tmfaultGrade = faultGradeMap.get(value);
            			entity.setTmFaultGradeId(tmfaultGrade.getId());
            		}else{
            			errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_DATA_VALID_ERROR_PUBLIC","故障等级不存在。"));
            			 continue;
            		}
            }else{
            	errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_DATA_VALID_ERROR_PUBLIC","故障等级不能为空。"));
            	continue;
            }
            // 备注
            index++;
            value = LoadUtils.getCell(row, index);
            // 判断如果不为空
            if (!StringUtils.isBlank(value)) {
                // 判断长度
                if (value.length() > 150) {
                    errorInfos.add(DI + (i + 1) + getMessage(req, "NC_IMPORT_ERROR_ELEVEN","备注","150"));
                    continue;
                }
            }
            entity.setRemarks(StringUtil.getString(value));
            
            // =========新增还是更新，放入各自集合中===========
            if (ncMap.get(entity.getTmNcGroupId()+"-"+entity.getNo()) == null) {
                addList.add(entity);
            } else {
                updateMap.put(i + 1, entity);
            }
        }
        final List<TmNc> updateList = needUpdateEntitys(updateMap);
        final StringBuffer addCount = new StringBuffer();
        final StringBuffer updateCount = new StringBuffer();
        try {
            batchImport(addList, ConstantUtils.IMPORT_BATCH_COUNT, ConstantUtils.OPERATION_INSERT, addCount);
            // 修改数据
            if ("true".equals(repeatOrUpdateFlag)) {
                batchImport(updateList, ConstantUtils.IMPORT_BATCH_COUNT, null, updateCount);
            }
        } catch (final Exception e) {
            if ("true".equals(isRollBack)) {
                throw new BusinessException("IMPORT_DATA_VALID_ERROR_INFO",
                        getMessage(req, "IMPORT_UNKNOWN_EXCEPTION"));
            } else {
                return getMessage(req, "IMPORT_UNKNOWN_EXCEPTION");
            }
        }
        final Set<Integer> repeatLindexes = updateMap.keySet();

		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "NC_MANAGEMENT"));
        importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));

        return (String) logsAndMsgTip.get("msgTip");
    }
    
    private void batchImport(final List<TmNc> list, final int num, final String insert, final StringBuffer buffer) {
        int successCount = 0;
        if (list.size() > 0) {
            final List<List<TmNc>> parts = SpiltUtils.averageAssign(list, num);
            try {
                for (int i = 0; i < parts.size(); i++) {
                    if ("insert".equals(insert)) {
                        doSaveBatch(parts.get(i));
                        successCount += parts.get(i).size();
                    } else {
                        doUpdateBatch(parts.get(i));
                        successCount += parts.get(i).size();
                    }
                }
                buffer.append(successCount);
            } catch (final Exception e) {
                buffer.append(successCount);
                throw new RuntimeException();
            }
        } else {
            buffer.append(successCount);
        }

    }

    private List<TmNc> needUpdateEntitys(final Map<Integer, TmNc> updateMap) {
        final List<TmNc> updateList = new ArrayList<TmNc>();
        for (final Integer key : updateMap.keySet()) {
            final TmNc insert = updateMap.get(key);
            final TmNc ncGroup = new TmNc();
            ncGroup.setNo(insert.getNo());
            final TmNc newData = findUniqueByEg(ncGroup);
            newData.setName(insert.getName());
            newData.setExtCode(StringUtil.getString(insert.getExtCode()));
            newData.setType(StringUtil.getString(insert.getType()));
            updateList.add(newData);
        }
        return updateList;
    }

    @Override
    public void doLog(final String logType, final String operationType, final String before, final String after) {
        final AuditConfiguration configuration = configurationService.selectByPrimaryKey(logType, operationType);
        if (configuration != null) {
            final AuditLog bean = new AuditLog();
            bean.setOperatorId(WebContextHolder.getCurrentUser().getId());
            bean.setOperatorName(WebContextHolder.getCurrentUser().getName());
            bean.setOperateDate(new Date());
            bean.setBeforeOperateObj(before);
            bean.setAfterOperateObj(after);
            bean.setAuditConfigurationId(configuration.getId());
            logService.doSave(bean);
        }
    }

    @Override
    public Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmNc> list) {
        try {
            final Workbook wb = WorkbookFactory.create(new File(templatePath));
            return wb;
        } catch (final Exception e) {
            log.error("Error down assembleDefect template.", e);
            throw new BusinessException("ERROR_DOWNLOAD_FILE");
        }
    }

    @Override
    public List<NGVo> getDictItem(final TmNc tmNc) {
        final List<TmNc> tmNcs = (tmNc == null ? findAll() : findByEg(tmNc));
        final List<NGVo> dictList = new ArrayList<NGVo>();
        for (final TmNc e : tmNcs) {
            final NGVo dict = new NGVo();
            dict.setCode(e.getId().toString());
            dict.setName(e.getNo() + "-" + e.getName());
            dict.setNgLevel(null != e.getTmFaultGradeId()?e.getTmFaultGradeId().toString():"");
            dictList.add(dict);
        }
        return dictList;
    }

	@Override
	public String createTemplateNo(String src) {
		String nextValue =String.valueOf(SerialNumUtil.getInstance().nextInt(ConstantUtils.NC_TEMPLATE_KEY));
		switch (nextValue.length()) {
		case 1:
			nextValue = "000"+nextValue;
			break;
		case 2:
			nextValue = "00"+nextValue;
			break;
		case 3:
			nextValue = "0"+nextValue;
			break;
		default:
			break;
		}
		return src+nextValue;
	}
	
	@Override
	public List<DictEntry> queryDictItem(QueryCriteria criteria) {
		criteria.setQueryPage(false);
		List<TmNc> beans = findBy(criteria).getContent();
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmNc e : beans) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			dict.setName(e.getNo() + "-" + e.getName());
			dictList.add(dict);
		}
		return dictList;
	}

}
