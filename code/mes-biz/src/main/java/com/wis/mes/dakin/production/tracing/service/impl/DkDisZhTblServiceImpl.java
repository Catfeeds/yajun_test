package com.wis.mes.dakin.production.tracing.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.LoadUtils;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.dakin.production.tracing.dao.DkDisZhTblDao;
import com.wis.mes.dakin.production.tracing.entity.DkDisZhTbl;
import com.wis.mes.dakin.production.tracing.service.DkDisZhTblService;
import com.wis.mes.dakin.production.tracing.vo.ProductPartVo;

@Service
public class DkDisZhTblServiceImpl extends BaseServiceImpl<DkDisZhTbl> implements DkDisZhTblService {

	@Autowired
	public void setDao(DkDisZhTblDao dao) {
		this.dao = dao;
	}

	@Override
	public List<ProductPartVo> queryProductPart(Map<String, Object> param) {
		return ((DkDisZhTblDao) dao).queryProductPart(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportProductPart(List<ProductPartVo> list, Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		Map<String, Object> reportMap = getReportMap(list);
		Map<String, Map<String, ProductPartVo>> partMap = (Map<String, Map<String, ProductPartVo>>) reportMap
				.get("partMap");
		Map<String, ProductPartVo> voMap = (Map<String, ProductPartVo>) reportMap.get("voMap");
		Sheet sheet = wb.getSheetAt(0);
		int rownum = 2;
		for (Map.Entry<String, ProductPartVo> entry : voMap.entrySet()) {
			ProductPartVo vo = entry.getValue();
			LoadUtils.getCell(sheet, rownum, 0).setCellValue(vo.getPlantName());
			LoadUtils.getCell(sheet, rownum, 1).setCellValue(vo.getLineNo());
			LoadUtils.getCell(sheet, rownum, 2).setCellValue(vo.getLineName());
			LoadUtils.getCell(sheet, rownum, 3).setCellValue(vo.getCreateTime());
			LoadUtils.getCell(sheet, rownum, 4).setCellValue(vo.getShiftNo());
			LoadUtils.getCell(sheet, rownum, 5).setCellValue(vo.getBackNumber());
			LoadUtils.getCell(sheet, rownum, 6).setCellValue(vo.getMachineOfName());
			LoadUtils.getCell(sheet, rownum, 7).setCellValue(vo.getMachineName());
			LoadUtils.getCell(sheet, rownum, 8).setCellValue(vo.getpId());
			LoadUtils.getCell(sheet, rownum, 9).setCellValue(vo.getBeginTime());
			LoadUtils.getCell(sheet, rownum, 10).setCellValue(vo.getFinishTime());
			LoadUtils.getCell(sheet, rownum, 11).setCellValue("");

			Map<String, ProductPartVo> map = partMap.get(entry.getKey());
			if (map.containsKey(vo.getPartType())) {
				for (Map.Entry<String, ProductPartVo> pentry : map.entrySet()) {
					ProductPartVo productPartVo = pentry.getValue();
					if ("01".equals(productPartVo.getPartType())) { // 压缩机
						LoadUtils.getCell(sheet, rownum, 12).setCellValue(productPartVo.getPartBackNumber());
						LoadUtils.getCell(sheet, rownum, 13).setCellValue(productPartVo.getPartFigure());
						LoadUtils.getCell(sheet, rownum, 14).setCellValue(productPartVo.getPartMachineNumber());
						LoadUtils.getCell(sheet, rownum, 15).setCellValue(productPartVo.getPartIrradiationTime());
						LoadUtils.getCell(sheet, rownum, 16).setCellValue("OK");
					} else if ("31".equals(productPartVo.getPartType())) {// 热交
						LoadUtils.getCell(sheet, rownum, 17).setCellValue(productPartVo.getPartBackNumber());
						LoadUtils.getCell(sheet, rownum, 18).setCellValue(productPartVo.getPartFigure());
						LoadUtils.getCell(sheet, rownum, 19).setCellValue(productPartVo.getPartMachineNumber());
						LoadUtils.getCell(sheet, rownum, 20).setCellValue(productPartVo.getPartIrradiationTime());
						LoadUtils.getCell(sheet, rownum, 21).setCellValue("OK");
					} else if ("49".equals(productPartVo.getPartType())) {// 配管
						LoadUtils.getCell(sheet, rownum, 22).setCellValue(productPartVo.getPartBackNumber());
						LoadUtils.getCell(sheet, rownum, 23).setCellValue(productPartVo.getPartFigure());
						LoadUtils.getCell(sheet, rownum, 24).setCellValue(productPartVo.getPartMachineNumber());
						LoadUtils.getCell(sheet, rownum, 25).setCellValue(productPartVo.getPartIrradiationTime());
						LoadUtils.getCell(sheet, rownum, 26).setCellValue("OK");
						LoadUtils.getCell(sheet, rownum, 27).setCellValue("");// TODO//
																				// 工位区分
					} else if ("70".equals(productPartVo.getPartType())) {// 电装品
						LoadUtils.getCell(sheet, rownum, 28).setCellValue(productPartVo.getPartBackNumber());
						LoadUtils.getCell(sheet, rownum, 29).setCellValue(productPartVo.getPartFigure());
						LoadUtils.getCell(sheet, rownum, 30).setCellValue(productPartVo.getPartMachineNumber());
						LoadUtils.getCell(sheet, rownum, 31).setCellValue(productPartVo.getPartIrradiationTime());
						LoadUtils.getCell(sheet, rownum, 32).setCellValue("OK");
					} else if ("02".equals(productPartVo.getPartType())) {// 小件
						LoadUtils.getCell(sheet, rownum, 33).setCellValue(productPartVo.getPartBackNumber());
						LoadUtils.getCell(sheet, rownum, 34).setCellValue(productPartVo.getPartFigure());
						LoadUtils.getCell(sheet, rownum, 35).setCellValue(productPartVo.getPartMachineNumber());
						LoadUtils.getCell(sheet, rownum, 36).setCellValue(productPartVo.getPartIrradiationTime());
						LoadUtils.getCell(sheet, rownum, 37).setCellValue("OK");
					}
				}
			}

			LoadUtils.setCellStyle(sheet, LoadUtils.setCellBorder(cellStyle), 0, 38, rownum);
			rownum++;
		}

	}

	private Map<String, Object> getReportMap(List<ProductPartVo> list) {
		Map<String, Map<String, ProductPartVo>> partMap = new HashMap<String, Map<String, ProductPartVo>>();
		Map<String, ProductPartVo> voMap = new HashMap<String, ProductPartVo>();
		for (ProductPartVo vo : list) {
			String key = vo.getBackNumber() + vo.getMachineName();
			if (partMap.containsKey(key)) {
				Map<String, ProductPartVo> map = partMap.get(key);
				map.put(vo.getPartType(), vo);
			} else {
				Map<String, ProductPartVo> map = new HashMap<String, ProductPartVo>();
				map.put(vo.getPartType(), vo);
				partMap.put(key, map);
				voMap.put(key, vo);
			}
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("partMap", partMap);
		returnMap.put("voMap", voMap);
		return returnMap;
	}
}
