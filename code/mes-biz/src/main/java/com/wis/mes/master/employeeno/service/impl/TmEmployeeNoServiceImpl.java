package com.wis.mes.master.employeeno.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.employeeno.dao.TmEmployeeNoDao;
import com.wis.mes.master.employeeno.entity.TmEmployeeNo;
import com.wis.mes.master.employeeno.service.TmEmployeeNoService;
import com.wis.mes.master.employeeno.vo.TmEmployeeNoVo;

@Service
public class TmEmployeeNoServiceImpl extends BaseServiceImpl<TmEmployeeNo> implements TmEmployeeNoService {
	@Autowired
	public void setDao(TmEmployeeNoDao dao) {
		this.dao = dao;
	}
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private ImportLogService importLogService;
	
	@SuppressWarnings("unchecked")
	@Override
	public String doImportExcelData(Workbook workbook, HttpServletRequest req) {
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);
		// 作业员工号Map
		final Map<String, TmEmployeeNo> tmEmployeeNoMap = new HashMap<String, TmEmployeeNo>();
		for (final TmEmployeeNo e : findAll()) {
			tmEmployeeNoMap.put(e.getNo(), e);
		}
		final Map<String,DictEntry> genders =  entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_USER_SEX);
		final Map<String,String> genderMap = new HashMap<String,String>();
		for(String key:genders.keySet()){
			genderMap.put(genders.get(key).getName(), key);
		}
		// 需要插入的对象List容器
		final List<TmEmployeeNo> addList = new ArrayList<TmEmployeeNo>();
		// 需要修改的对象List容器
		List<TmEmployeeNo> updateList = new ArrayList<TmEmployeeNo>();
		// 需要修改的对象Map
		final Map<Integer, TmEmployeeNo> updateMap = new HashMap<Integer, TmEmployeeNo>();
		// 格式错误的信息容器
		final List<String> errorInfos = new ArrayList<String>();
		// 读取第一个sheet页
		final Sheet sheet = workbook.getSheetAt(0);
		final String DI = getMessage(req, "DI");
		Row row;
		int judgeSize = 3;// 数据表格的列数（字段数）
		int totalInt = 0;
		String value = null;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;
			TmEmployeeNo entity = new TmEmployeeNo();
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			// 判断作业员工号是否为空
			if (StringUtils.isEmpty(value)) {
				errorInfos.add(DI + (i + 1) + "行导入失败,错误信息->[作业员工号为必填项]");
				continue;
			}
			// 判断作业员工号的长度是否超过20位
			if (value.trim().length() > 20) {
				errorInfos.add(DI + (i + 1) + "行导入失败,错误信息->[作业员工号长度不能超出20位]");
				continue;
			}
			// 判断作业员工号是否符合规则 编号只允许输入英文、数字、中划线-、下划线_和反斜杠/
			if (StringUtils.isNotBlank(value) && (!Pattern.compile("[\\w-\\s]+$").matcher(value).matches()
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
				errorInfos.add(DI + (i + 1) + "行导入失败,错误信息->[作业员工号只允许输入英文、数字、中划线-、下划线_和反斜杠]");
				continue;
			}
			entity.setNo(value);
			index++;
			value = LoadUtils.getCell(row, index);
			// 判断作业员名称是否为空
			if (StringUtils.isEmpty(value)) {
				errorInfos.add(DI + (i + 1) + "行导入失败,错误信息->[作业员名称为必填项]");
				continue;
			}
			// 判断作业员工号的长度是否超过50位
			if (value.trim().length() > 20) {
				errorInfos.add(DI + (i + 1) + "行导入失败,错误信息->[作业员名称长度不能超出50位]");
				continue;
			}
			entity.setName(value);
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotEmpty(value)) {
				entity.setSeniority(Integer.valueOf(value));
			}
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotEmpty(value) && genderMap.containsKey(value)) {
				entity.setSex(genderMap.get(value));
			}
			// =========新增还是更新，放入各自集合中===========
			if (tmEmployeeNoMap.get(entity.getNo()) == null) {
				addList.add(entity);
			} else {
				TmEmployeeNo updateBean= tmEmployeeNoMap.get(entity.getNo());
				updateBean.setNo(entity.getNo());
				updateBean.setName(entity.getName());
				updateBean.setSeniority(entity.getSeniority());
				updateBean.setSex(entity.getSex());
				updateList.add(updateBean);
				updateMap.put(i + 1, updateBean);
			}

		}
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			batchImport(addList, ConstantUtils.IMPORT_BATCH_COUNT, ConstantUtils.OPERATION_INSERT, addCount);
			// 修改数据
			if ("true".equals(repeatOrUpdateFlag)) {
				batchImport(updateList, ConstantUtils.IMPORT_BATCH_COUNT, null, updateCount);
			}
		} catch (Exception e) {
			if ("true".equals(isRollBack)) {
				throw new BusinessException("IMPORT_DATA_VALID_ERROR_INFO",
						getMessage(req, "IMPORT_UNKNOWN_EXCEPTION"));
			} else {
				return getMessage(req, "IMPORT_UNKNOWN_EXCEPTION");
			}
		}
		Set<Integer> repeatLindexes = updateMap.keySet();
		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag,addCount,
				updateCount,totalInt, repeatLindexes, errorInfos,req, "作业员工号");
		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");
	}
	
	private void batchImport(List<TmEmployeeNo> list, int num, String insert, StringBuffer countBuffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmEmployeeNo>> employeeNos = SpiltUtils.averageAssign(list, num);
			try {
				for (int i = 0; i < employeeNos.size(); i++) {
					if ("insert".equals(insert)) {
						super.doSaveBatch(employeeNos.get(i));
						successCount += employeeNos.get(i).size();
					} else {
						List<TmEmployeeNo> employeeNoList = employeeNos.get(i);
						for(TmEmployeeNo  bean:employeeNoList) {
							super.doUpdate(bean);
						}
						successCount += employeeNos.get(i).size();
					}
				}
				countBuffer.append(successCount);
			} catch (Exception e) {
				countBuffer.append(successCount);
				throw new RuntimeException();
			}
		} else {
			countBuffer.append(successCount);
		}
	}
	
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmEmployeeNo> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<String,DictEntry> genders =  entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_USER_SEX);
		Map<String, Object> result = new HashMap<String, Object>();
		for(TmEmployeeNo tmEmployeeNo:list){
			Map<String, Object> map = new HashMap<String, Object>();
			String sex = "";
			if(StringUtils.isNotBlank(tmEmployeeNo.getSex())){
				sex = genders.get(tmEmployeeNo.getSex()).getName();
			}
			map.put(header[0], tmEmployeeNo.getNo());
			map.put(header[1], tmEmployeeNo.getName());
			map.put(header[2], null != tmEmployeeNo.getSeniority()? tmEmployeeNo.getSeniority():"");
			map.put(header[3], sex);
			exportDataList.add(map);
		}
		result = BaseExcelUtil.exportData(response, exportDataList, filePath, header);
		return result;
	}
	
	@Override
	public Workbook getWorkbookTemp(final String downName, final String templatePath) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}


	@Override
	public List<TmEmployeeNoVo> getDictItem(TmEmployeeNo arg0) {
		List<TmEmployeeNo> tmEmployeeNos = (arg0 == null ? findAll() : findByEg(arg0));
		final List<TmEmployeeNoVo> arrays = new ArrayList<TmEmployeeNoVo>();
		for (final TmEmployeeNo e : tmEmployeeNos) {
			final TmEmployeeNoVo bean = new TmEmployeeNoVo();
			bean.setCode(e.getId().toString());
			bean.setName(e.getNo()+"-"+e.getName());
			bean.setNo(e.getNo());
			bean.setNameCn(e.getName());
			bean.setSex(e.getSex());
			bean.setSeniority(e.getSeniority());
			arrays.add(bean);
		}
		return arrays;
	}
}
