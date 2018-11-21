package com.wis.mes.dakin.production.tracing.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.dakin.production.tracing.dao.VdkProductParametersDao;
import com.wis.mes.dakin.production.tracing.entity.VdkProductParameters;
import com.wis.mes.dakin.production.tracing.service.VdkProductParametersService;
import com.wis.mes.dakin.production.tracing.vo.ProductParameterVo;
import com.wis.mes.util.StringUtil;

@Service
public class VdkProductParametersServiceImpl extends BaseServiceImpl<VdkProductParameters>
		implements VdkProductParametersService {

	@Autowired
	public void setDao(VdkProductParametersDao dao) {
		this.dao = dao;
	}

	@Override
	public List<ProductParameterVo> queryProductParameters(Map<String, Object> param) {
		return ((VdkProductParametersDao) dao).queryProductParameters(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportProductTracing(List<ProductParameterVo> list, Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		Sheet sheet = wb.getSheetAt(0);
		Map<String, Object> reportData = getReportData(list);
		Map<String, Map<String, String>> equipmentParamsMap = (Map<String, Map<String, String>>) reportData
				.get("equipmentParamsMap");
		Map<String, ProductParameterVo> voMap = (Map<String, ProductParameterVo>) reportData.get("voMap");
		int rownum = 2;
		for (Map.Entry<String, ProductParameterVo> entry : voMap.entrySet()) {
			ProductParameterVo vo = entry.getValue();
			String key = vo.getBackNumber() + vo.getMachineName();
			Map<String, String> paramsValue = equipmentParamsMap.get(key);
			LoadUtils.getCell(sheet, rownum, 0).setCellValue(vo.getPlantName());
			LoadUtils.getCell(sheet, rownum, 1).setCellValue(vo.getLineNo());
			LoadUtils.getCell(sheet, rownum, 2).setCellValue(vo.getLineName());
			LoadUtils.getCell(sheet, rownum, 3).setCellValue(DateUtils.formatDateTime(vo.getCreateTime()));
			LoadUtils.getCell(sheet, rownum, 4).setCellValue(vo.getShiftNo());
			LoadUtils.getCell(sheet, rownum, 5).setCellValue(vo.getBackNumber());
			LoadUtils.getCell(sheet, rownum, 6).setCellValue(vo.getMachineOfName());
			LoadUtils.getCell(sheet, rownum, 7).setCellValue(vo.getMachineName());
			LoadUtils.getCell(sheet, rownum, 8).setCellValue(vo.getpId());
			LoadUtils.getCell(sheet, rownum, 9).setCellValue(vo.getBeginTime());
			LoadUtils.getCell(sheet, rownum, 10).setCellValue(vo.getFinishTime());
			LoadUtils.getCell(sheet, rownum, 11).setCellValue("");
			// 气密设备 [28] [29][35] [36]
			int cellnum = 12;
			for (int i = 1; i <= 4; i++) {
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue("");// 瞬时压力值
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("PMSHISTORY|" + i + "|PRESSURE"));// 在实测试值
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("PMSHISTORY|" + i + "|PERIOD"));// 在实时间
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("PMSHISTORY|" + i + "|SETUPTIME"));// 设定时间
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("PMSHISTORY|" + i + "|SETUPPRESSURE"));// 设定测试值
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("PMSHISTORY|" + i + "|SETUPRETURNVAL"));// 设定回收值
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("PMSHISTORY|" + i + "|RESULT"));// 气密结果
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("PMSHISTORY|" + i + "|WORKINGTIME"));// 作业时间
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("PMSHISTORY|" + i + "|UPDATETIME"));// 数据保存时间
			}

			// 气密设备 [44]
			LoadUtils.getCell(sheet, rownum, 48).setCellValue("");// 瞬时压力值
			LoadUtils.getCell(sheet, rownum, 49).setCellValue("");// 数据保存时间
			// 气密设备 [45]
			LoadUtils.getCell(sheet, rownum, 50).setCellValue("");// 瞬时压力值
			LoadUtils.getCell(sheet, rownum, 51).setCellValue("");// 数据保存时间
			// 真空干燥[65][66][67][68][69][70][72]
			cellnum = 52;
			for (int i = 1; i <= 7; i++) {
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue("");//瞬时真空值
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|VACUUM1"));// 真空值1
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|VACUUM2"));// 真空值2
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|VACUUM3"));// 真空值3
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|VACUUM4"));// 真空值4
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|VACUUM5"));// 真空值5
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|VACUUM6"));//  真空值6
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|VACUUM7"));//  真空值7
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|ELAPSEDTIME"));//动态真空时间设定值 
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|STABILIZATION"));// 静态真空时间设定值
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|RESULT"));// 真空结果
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|SETDATE"));// 真空记录保存时间
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("VTS_RST|" + i + "|GAPRESULT"));// 设备状态 
			}
			cellnum = 143;
			// 冷媒填充[75][104]
			for (int i = 1; i <= 2; i++) {
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|SBID"));//序号
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|TORUNUM"));//设定充填量
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|ZUJANUM"));//追加充填量
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|TORUCOU"));//实际充填量
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|TORU"));//充填结果
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|ERRORCODE"));//故障代码
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|PRDTIME"));//充填时间
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|PRDYMDSTD"));//开始充填时间
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|PRDYMDEND"));//结束充填时间
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|TYPE"));//冷媒种类
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|BEFORE_CZ"));// 充填前重量
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|BALANCE"));// 充填后重量
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|BALANCE"));// 差异重量
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|CZTEST_FLG"));// 称重结果
				LoadUtils.getCell(sheet, rownum, cellnum++).setCellValue(paramsValue.get("COOLRST_TBL|" + i + "|PORT"));// 充填口
			}

			// 油充填
			LoadUtils.getCell(sheet, rownum, 173).setCellValue(paramsValue.get("OILRST_TBL||TORUNUM"));//设定充填量
			LoadUtils.getCell(sheet, rownum, 174).setCellValue(paramsValue.get("OILRST_TBL||ZUJANUM"));//追加充填量
			LoadUtils.getCell(sheet, rownum, 175).setCellValue(paramsValue.get("OILRST_TBL||TORUCOU"));// 实际充填量
			LoadUtils.getCell(sheet, rownum, 176).setCellValue(paramsValue.get("OILRST_TBL||TORURST"));//充填结果
			LoadUtils.getCell(sheet, rownum, 177).setCellValue(paramsValue.get("OILRST_TBL||ERRORCODE"));//故障代码
			LoadUtils.getCell(sheet, rownum, 178).setCellValue(paramsValue.get("OILRST_TBL||PRDTIME"));//充填时间
			LoadUtils.getCell(sheet, rownum, 179).setCellValue(paramsValue.get("OILRST_TBL||PRDYMDSTD"));//充填开始时间
			LoadUtils.getCell(sheet, rownum, 180).setCellValue(paramsValue.get("OILRST_TBL||PRDYMDEND"));//充填结束时间
			// 绝缘耐压
			LoadUtils.getCell(sheet, rownum, 181).setCellValue(paramsValue.get("RESISTANCETEST||INSULATION1"));//绝缘测试结果
			LoadUtils.getCell(sheet, rownum, 182).setCellValue(paramsValue.get("RESISTANCETEST||RESISTANCE1"));// 耐压测试结果
			LoadUtils.getCell(sheet, rownum, 183).setCellValue(paramsValue.get("RESISTANCETEST||EARTH"));// 接地测试结果
			LoadUtils.getCell(sheet, rownum, 184).setCellValue(paramsValue.get("RESISTANCETEST||RESULT"));// 测试结果
			LoadUtils.getCell(sheet, rownum, 185).setCellValue(paramsValue.get("RESISTANCETEST||TESTTIME"));//测试记录时间
			LoadUtils.getCell(sheet, rownum, 186).setCellValue(paramsValue.get("RESISTANCETEST||RESISTANCETEST"));//耐压测试电压设定值
			LoadUtils.getCell(sheet, rownum, 187).setCellValue(paramsValue.get("RESISTANCETEST||INSULATIONTEST"));// 绝缘测试电压设定值
			LoadUtils.getCell(sheet, rownum, 188).setCellValue(paramsValue.get("RESISTANCETEST||INSULATION2"));//二次绝缘值
			LoadUtils.getCell(sheet, rownum, 189).setCellValue(paramsValue.get("RESISTANCETEST||INSULATION3"));//三次绝缘值
			LoadUtils.getCell(sheet, rownum, 190).setCellValue(paramsValue.get("RESISTANCETEST||RESISTANCE2"));//二次耐压值
			LoadUtils.getCell(sheet, rownum, 191).setCellValue(paramsValue.get("RESISTANCETEST||RESISTANCE3"));//三次耐压值

			// 最终检查
			LoadUtils.getCell(sheet, rownum, 192).setCellValue(paramsValue.get("ZZJC_DATE||TIMEID"));//时间ID
			LoadUtils.getCell(sheet, rownum, 193).setCellValue(paramsValue.get("ZZJC_DATE||PRDYMD"));//生产计划日
			LoadUtils.getCell(sheet, rownum, 194).setCellValue(paramsValue.get("ZZJC_DATE||RFID"));// RFID结果
			LoadUtils.getCell(sheet, rownum, 195).setCellValue(paramsValue.get("ZZJC_DATE||WARRANTY"));//保修证打印时间
			LoadUtils.getCell(sheet, rownum, 196).setCellValue(paramsValue.get("ZZJC_DATE||PQC"));//合格证打印时间
			LoadUtils.getCell(sheet, rownum, 197).setCellValue(paramsValue.get("ZZJC_DATE||MP"));//机械铭牌打印时间
			LoadUtils.getCell(sheet, rownum, 198).setCellValue(paramsValue.get("ZZJC_DATE||RESISTANCE"));//绝缘耐压结果
			LoadUtils.getCell(sheet, rownum, 199).setCellValue(paramsValue.get("ZZJC_DATE||LABELL"));// 捆包标签打印时间
			LoadUtils.getCell(sheet, rownum, 200).setCellValue(paramsValue.get("ZZJC_DATE||COOL1"));//一次冷媒结果
			LoadUtils.getCell(sheet, rownum, 201).setCellValue(paramsValue.get("ZZJC_DATE||COOL2"));//二次冷媒结果
			LoadUtils.getCell(sheet, rownum, 202).setCellValue(paramsValue.get("ZZJC_DATE||OIL"));//油充填结果
			LoadUtils.getCell(sheet, rownum, 203).setCellValue(paramsValue.get("ZZJC_DATE||VTS"));// 真空结果
			LoadUtils.getCell(sheet, rownum, 204).setCellValue(paramsValue.get("ZZJC_DATE||WYZ"));// 外机运转检查结果
			LoadUtils.getCell(sheet, rownum, 205).setCellValue(paramsValue.get("ZZJC_DATE||DYQM"));//低压气密结果
			LoadUtils.getCell(sheet, rownum, 206).setCellValue(paramsValue.get("ZZJC_DATE||CZK"));// 粗真空结果
			LoadUtils.getCell(sheet, rownum, 207).setCellValue(paramsValue.get("ZZJC_DATE||GYQM"));//高压气密结果
			LoadUtils.getCell(sheet, rownum, 208).setCellValue(paramsValue.get("ZZJC_DATE||QMHS"));//气密回收结果
			LoadUtils.getCell(sheet, rownum, 209).setCellValue(paramsValue.get("ZZJC_DATE||PICKING"));// 配料时间
			LoadUtils.getCell(sheet, rownum, 210).setCellValue(paramsValue.get("ZZJC_DATE||MPSB"));// 铭牌识别结果
			LoadUtils.getCell(sheet, rownum, 211).setCellValue(paramsValue.get("ZZJC_DATE||BZP"));//半制品照合结果
			LoadUtils.getCell(sheet, rownum,212).setCellValue(paramsValue.get("ZZJC_DATE||BLCZ"));//不良机处置结果
			LoadUtils.getCell(sheet, rownum, 213).setCellValue(paramsValue.get("ZZJC_DATE||DYKQ"));//低压空气结果
			LoadUtils.getCell(sheet, rownum, 214).setCellValue(paramsValue.get("ZZJC_DATE||DZJP"));//电子角品时间
			LoadUtils.getCell(sheet, rownum, 215).setCellValue(paramsValue.get("ZZJC_DATE||NYZ"));//内机运转检查结果
			LoadUtils.getCell(sheet, rownum, 216).setCellValue(paramsValue.get("ZZJC_DATE||ZZYW"));// 制造业务指示
			LoadUtils.getCell(sheet, rownum,217).setCellValue(paramsValue.get("ZZJC_DATE||PRESULT"));//前工序检查结果
			LoadUtils.getCell(sheet, rownum,218).setCellValue(paramsValue.get("ZZJC_DATE||RESULTT"));//最终检查结果
			LoadUtils.getCell(sheet, rownum,219).setCellValue(paramsValue.get("ZZJC_DATE||TIMEE"));//最终检查时间
			LoadUtils.getCell(sheet, rownum,220).setCellValue(paramsValue.get("ZZJC_DATE||ERR_ID"));//不合格代码
			LoadUtils.getCell(sheet, rownum,221).setCellValue(paramsValue.get("ZZJC_DATE||NXBQ"));//能效标签结果
			
			// CCD
			LoadUtils.getCell(sheet, rownum,222).setCellValue(paramsValue.get("VEC_SCAN_LOG||ID"));//ID
			LoadUtils.getCell(sheet, rownum,223).setCellValue(paramsValue.get("VEC_SCAN_LOG||BARCODE"));// RFID读取数据
			LoadUtils.getCell(sheet, rownum,224).setCellValue(paramsValue.get("VEC_SCAN_LOG||OTHER"));// 条码后缀
			LoadUtils.getCell(sheet, rownum,225).setCellValue(paramsValue.get("VEC_SCAN_LOG||HEIGHT"));//高度
			LoadUtils.getCell(sheet, rownum,226).setCellValue(paramsValue.get("VEC_SCAN_LOG||LOGO"));// 意匠铭牌图号
			LoadUtils.getCell(sheet, rownum,227).setCellValue(paramsValue.get("VEC_SCAN_LOG||MNAME"));// 机种名
			LoadUtils.getCell(sheet, rownum,228).setCellValue(paramsValue.get("VEC_SCAN_LOG||MTYPE"));// 机种种类
			LoadUtils.getCell(sheet, rownum,229).setCellValue(paramsValue.get("VEC_SCAN_LOG||KIBAN_0"));//机号
			LoadUtils.getCell(sheet, rownum,230).setCellValue(paramsValue.get("VEC_SCAN_LOG||RESULT_MSG"));// 识别结果
			LoadUtils.getCell(sheet, rownum,231).setCellValue(paramsValue.get("VEC_SCAN_LOG||ERR_CODE"));//故障代码
			LoadUtils.getCell(sheet, rownum,232).setCellValue(paramsValue.get("VEC_SCAN_LOG||CREATE_TIME"));//CCD识别时间
			LoadUtils.getCell(sheet, rownum,233).setCellValue(paramsValue.get("VEC_SCAN_LOG||NX_HTTP"));//能效标识二维码
			LoadUtils.getCell(sheet, rownum,234).setCellValue(paramsValue.get("VEC_SCAN_LOG||JXMP_HTTP"));//机械铭牌二维码
			LoadUtils.getCell(sheet, rownum,235).setCellValue(paramsValue.get("VEC_SCAN_LOG||NX_ERR_CODE"));//能效标识故障代码
			
			// 捆包标签
			LoadUtils.getCell(sheet, rownum,236).setCellValue(paramsValue.get("LOG||PRINTDATE"));//捆包标签打印时间
			LoadUtils.setCellStyle(sheet, LoadUtils.setCellBorder(cellStyle), 0, 237, rownum);
			rownum++;
		}
	}

	private Map<String, Object> getReportData(List<ProductParameterVo> list) {
		Map<String, Map<String, String>> equipmentParamsMap = new HashMap<String, Map<String, String>>();
		Map<String, ProductParameterVo> voMap = new HashMap<String, ProductParameterVo>();
		for (ProductParameterVo vo : list) {
			String key = vo.getBackNumber() + vo.getMachineName();
			String paramKey = vo.getEquipmentType() + "|"
					+ (StringUtil.isEmpty(vo.getEquipmentNo()) ? "" : vo.getEquipmentNo()) + "|" + vo.getParams();
			if (equipmentParamsMap.containsKey(key)) {
				Map<String, String> paramValueMap = equipmentParamsMap.get(key);
				paramValueMap.put(paramKey, vo.getParamsValue());
				voMap.put(key, vo);
			} else {
				Map<String, String> paramValueMap = new HashMap<String, String>();
				paramValueMap.put(paramKey, vo.getParamsValue());
				equipmentParamsMap.put(key, paramValueMap);
			}
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("voMap", voMap);
		returnMap.put("equipmentParamsMap", equipmentParamsMap);
		return returnMap;
	}
}
