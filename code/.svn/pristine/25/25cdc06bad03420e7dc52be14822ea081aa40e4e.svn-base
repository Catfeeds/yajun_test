package com.wis.basis.configuration.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.wis.basis.configuration.dao.TsParameterDao;
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.basis.configuration.service.TsParameterService;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;

@Service
public class TsParameterSerivceImpl extends BaseServiceImpl<TsParameter> implements TsParameterService {
	@Autowired
	public void setDao(TsParameterDao dao) {
		this.dao = dao;
	}

	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private ImportLogService importLogService;

	/**
	 * 获取参数列表
	 * 
	 * @return List<DictEntry>
	 * @param tmParameter
	 * @author 赵宪泉
	 * @Time 2017/5/25
	 */
	@Override
	public List<DictEntry> getDictItem(TsParameter tmParameter) {
		List<TsParameter> tmParameters = (tmParameter == null ? findAll() : findByEg(tmParameter));
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TsParameter e : tmParameters) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			dict.setName(e.getCode() + "-" + e.getName());
			dictList.add(dict);
		}
		return dictList;
	}

	/**
	 * 导出参数数据
	 * 
	 * @return Map<String,Object>
	 * @param response,list,filePath,header
	 * @author 赵宪泉
	 * @Time 2017/5/25
	 */
	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TsParameter> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			TsParameter tmParameter = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(header[0], tmParameter.getCode());
			map.put(header[1], tmParameter.getName());
			map.put(header[2], tmParameter.getRegularExpression());
			map.put(header[3], tmParameter.getVariuableName());
			map.put(header[4], tmParameter.getDefaultValue());
			map.put(header[5], tmParameter.getNote());
			map.put(header[6], tmParameter.getUnit());
			exportDataList.add(map);
		}
		result = BaseExcelUtil.exportData(response, exportDataList, filePath, header);
		return result;
	}

	@Override
	public Workbook getWorkbookTemp(String downName, String templatePath, List<TsParameter> list) {
		try {
			final Workbook wb = WorkbookFactory.create(new File(templatePath));
			return wb;
		} catch (final Exception e) {
			log.error("Error down assembleDefect template.", e);
			throw new BusinessException("ERROR_DOWNLOAD_FILE");
		}
	}

	@Override
	public String doImportExcelData(Workbook book, HttpServletRequest req) {
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);
		// 参数map
		Map<String, TsParameter> parameterMap = new HashMap<String, TsParameter>();
		for (TsParameter e : findAll()) {
			parameterMap.put(e.getCode(), e);
		}

		// 需要插入的list
		List<TsParameter> addParameterList = new ArrayList<TsParameter>();
		// 需要更新的Map
		Map<Integer, TsParameter> updateParameterMap = new HashMap<Integer, TsParameter>();
		// 格式错误的信息容器
		final List<String> errorInfos = new ArrayList<String>();

		// 读取内容
		final Sheet sheet = book.getSheetAt(0);
		// "第"
		final String DI = getMessage(req, "DI");
		String value = null;
		Row row;//获得行使用
		// 循环输出表格内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			int index = 0;//获得列使用
			TsParameter entity = new TsParameter();
			//读取第一列
			value = LoadUtils.getCell(row, index);
			//验证编码
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PARAMETER_IMPORT_CODE_REQUIRED"));
				continue;
			}
			if (value.length() > 36) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PARAMETER_IMPORT_CODE_LENGTH_REQUIRED"));
				continue;
			}
			if ((!Pattern.compile("[\\w-\\s]+$").matcher(value).matches()
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PARAMETER_IMPORT_CODE_FORMAT_REQUIRED"));
				continue;
			}
			if ( null != parameterMap.get(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "PARAMETER_IMPORT_CODE_REPEAT_REQUIRED"));
				continue;
			}
			entity.setCode(value);

			//验证名称
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 36) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PARAMETER_IMPORT_NAME_LENGTH_REQUIRED"));
					continue;
				}
			}
			entity.setName(value);

			//验证表达式
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 1000) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PARAMETER_IMPORT_REGULAR_EXPRESSION_LENGTH_REQUIRED"));
					continue;
				}
			}
			entity.setRegularExpression(value);

			//验证变量名
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PARAMETER_IMPORT_VARIUABLE_NAME_REQUIRED"));
					continue;
				}
			}
			entity.setVariuableName(value);

			//验证默认值
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 36) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PARAMETER_IMPORT_DEFAULT_VALUE_LENGTH_REQUIRED"));
					continue;
				}
			}
			entity.setDefaultValue(value);

			//验证备注
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(value)) {
				if (value.length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PARAMETER_IMPORT_NOTE_LENGTH_REQUIRED"));
					continue;
				}
			}
			entity.setNote(value);
			//验证单位
			index++;
			value = LoadUtils.getCell(row, index);
			if (StringUtils.isBlank(value)) {
				if (value.length() > 100) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "PARAMETER_IMPORT_UNIT_LENGTH_REQUIRED"));
					continue;
				}
			}
			entity.setUnit(value);
			if (parameterMap.get(entity.getCode()) == null) {
				addParameterList.add(entity);
			} else {
				updateParameterMap.put(i + 1, entity);
			}
		}
		List<TsParameter> updateList = needUpdateEntitys(updateParameterMap);
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			batchImport(addParameterList, ConstantUtils.IMPORT_BATCH_COUNT, ConstantUtils.OPERATION_INSERT, addCount);
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
		// 返回信息
		StringBuffer msgTip = new StringBuffer();
		List<ImportLog> logs = new ArrayList<ImportLog>();
		msgTip.append("成功新增:");
		msgTip.append(addCount);
		msgTip.append("条记录;<br/>");
		if ("true".equals(repeatOrUpdateFlag)) {
			msgTip.append("成功更新:");
			msgTip.append(updateCount);
			msgTip.append("条记录;<br/>");
		} else {
			if (!updateParameterMap.keySet().isEmpty()) {
				msgTip.append("因系统存在重复数据，而导入失败的记录:<br/>");
				int count = 0;
				for (Integer rowIndex : updateParameterMap.keySet()) {
					StringBuffer msgLog = new StringBuffer(getMessage(req, "REPEAT_DATA_WHEN_IMPORT"));
					ImportLog log = new ImportLog();
					msgLog.append(DI + rowIndex + "行");
					if (count < ConstantUtils.TIPS_COUNT_FOR_DUPLICATE_ERROR) {
						msgTip.append(DI + rowIndex + "行,");
					}
					if (count == ConstantUtils.TIPS_COUNT_FOR_DUPLICATE_ERROR) {
						msgTip.append(ConstantUtils.SUSPENSION_POINTS);
					}
					log.setErrorDesc(msgLog.toString());
					log.setOperateEntityName(getMessage(req, "PARAMETER_MANAGE"));
					logs.add(log);
					count++;
				}
				msgTip.append("<br/>");
			}
		}
		if (!errorInfos.isEmpty()) {
			msgTip.append("因格式错误，而导入失败的数据:<br/>");
			int count = 0;
			for (String errorInfo : errorInfos) {
				StringBuffer msgLog = new StringBuffer(getMessage(req, "ERROR_DATA_WHEN_IMPORT"));
				ImportLog log = new ImportLog();
				msgLog.append(errorInfo);
				if (count < ConstantUtils.TIPS_COUNT_FOR_PATTERN_ERROR) {
					msgTip.append(errorInfo);
					msgTip.append("<br/>");
				}
				if (count == ConstantUtils.TIPS_COUNT_FOR_PATTERN_ERROR) {
					msgTip.append(ConstantUtils.SUSPENSION_POINTS);
				}
				log.setErrorDesc(msgLog.toString());
				log.setOperateEntityName(getMessage(req, "PARAMETER_MANAGE"));
				logs.add(log);
				count++;
			}
		}
		importLogService.doSaveBatch(logs);
		return msgTip.toString();
	}

	private void batchImport(List<TsParameter> list, int num, String insert,
			StringBuffer buffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TsParameter>> parts = SpiltUtils.averageAssign(list, num);
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
			} catch (Exception e) {
				buffer.append(successCount);
				throw new RuntimeException();
			}
		} else {
			buffer.append(successCount);
		}

	}

	private List<TsParameter> needUpdateEntitys(Map<Integer, TsParameter> updateParameterMap) {
		List<TsParameter> updateList = new ArrayList<TsParameter>();
		for (Integer key : updateParameterMap.keySet()) {
			TsParameter insert = updateParameterMap.get(key);
			TsParameter parameter = new TsParameter();
			parameter.setCode(insert.getCode());
			TsParameter newData = findUniqueByEg(parameter);
			parameter.setName(isNull(insert.getName()));
			parameter.setRegularExpression(isNull(insert.getRegularExpression()));
			parameter.setVariuableName(isNull(insert.getVariuableName()));
			parameter.setDefaultValue(isNull(insert.getDefaultValue()));
			parameter.setNote(isNull(insert.getNote()));
			updateList.add(newData);
		}
		return updateList;
	}

	private String isNull(Object obj){
		if (null == obj || "" == obj ) {
			return "";
		}
		return obj.toString();
	}

	/** 生成正则表达式 */
	@Override
	public StringBuffer  generatorExpression(String max,String min) {
		StringBuffer reg = new StringBuffer("/");
		int minlength = min.length();
		int maxlength = max.length();
		int index;
		if (min.contains(".")) {
		}
		else{
			for (int i = 0;i < minlength; i++) {
				index = 0;
				reg.append("^");
				if (minlength == 1) {
					reg.append("["+min.charAt(0)+"-9]$|");
					break;
				}else{
					for (int j = 0;j < minlength-i-1; j++) {
						reg.append(min.charAt(j));
						index++;
					}
					for (int k = index;k < minlength; k++) {
						if (index == minlength-1) {
							reg.append("[0-"+min.charAt(k)+"]");
						}
						else if (k == index && k != 0){
							reg.append("["+((int)min.charAt(k)-47)+"-9]");
						}
						else{
							reg.append("[0-9]");
						}
					}
				}
				reg.append("$|");
			}
		}
		if (maxlength-minlength>1) {
			reg.append("^[0-9]{"+(minlength+1)+"-"+(maxlength-minlength)+"}$|");
		}
		if (max.contains(".")) {
		}
		else if(max.contains("-")){
		}
		else{
			for (int i = 0;i < maxlength-1; i++) {
				index = 0;
				reg.append("^");
				if (maxlength-i==2) {
					reg.append("[1-"+max.charAt(0)+"]");
				}
				for (int j = 1;j < maxlength-i-1; j++) {
					reg.append(max.charAt(j));
					index++;
				}
				if (index == maxlength-1) {
					reg.append("[0-"+max.charAt(index)+"]");
				}else{
					reg.append("[0-"+((int)max.charAt(index)-47)+"][0-9]{1-"+ i +"}");
				}
				reg.append("$|");
			}
		}
		return reg;
	}
}
