package com.wis.basis.common.utils;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.core.framework.service.exception.BusinessException;

public class BaseExcelUtil<T> {

	public final static String success = "Excel导出成功";

	public static Map<String, Object> exportData(final HttpServletResponse response,
			final List<Map<String, Object>> list, final String filePath, final String[] header) {
		Map<String, Object> result = new HashMap<String, Object>();
		result = transferExportData(response, list, filePath, header);
		return result;
	}

	private static Map<String, Object> transferExportData(final HttpServletResponse response,
			final List<Map<String, Object>> list, final String filePath, final String[] header) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 1.写入信息
		final String[] strs = filePath.split("\\.");
		final String fileType = strs[strs.length - 1];
		if ("xlsx".equalsIgnoreCase(fileType)) {
			writeXlsxData(response, list, filePath, result, header);
		} else if ("xls".equalsIgnoreCase(fileType)) {
			result = writeXlsData(response, list, filePath, result, header);
		} else {
			result.put("code", -103);
			result.put("message", "文件不是正确的EXCEL格式");
		}
		return result;
	}

	public static Map<String, Object> writeXlsxData(final HttpServletResponse response,
			final List<Map<String, Object>> list, final String filePath, final Map<String, Object> result,
			String[] header) {
		final XSSFWorkbook wb = new XSSFWorkbook();
		final XSSFSheet sheet = wb.createSheet();
		XSSFRow row = null;
		XSSFCellStyle textCellStyle = wb.createCellStyle();
		XSSFDataFormat dataFormat = wb.createDataFormat();
		textCellStyle.setDataFormat(dataFormat.getFormat("@"));
		// 添加excel头
		row = sheet.createRow(0);
		if (header != null) {
			Cell cell = null;
			for (int i = 0; i < header.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(header[i]);
			}
		}
		// 添加excel内容
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			Set<String> set = map.keySet();
			Object[] keys = set.toArray();
			row = sheet.createRow(i + 1);
			Cell cell = null;
			for (int j = 0; j < keys.length; j++) {
				cell = row.createCell(j);
				if (header != null) {
					cell.setCellValue(String.valueOf(map.get(header[j])));
					cell.setCellStyle(textCellStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				}
			}
		}

		try {
			result.put("code", 1);
			result.put("message", "成功导出" + list.size() + "条记录到" + filePath);
			String fileName = new String(filePath.getBytes(), "iso-8859-1");
			response.setContentType("application/ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setCharacterEncoding("UTF-8");
			wb.write(response.getOutputStream());
			response.getOutputStream().close();
		} catch (final Exception e) {
			result.put("code", 0);
			result.put("message", e.getMessage());
		}
		return result;
	}

	public static Map<String, Object> writeXlsData(final HttpServletResponse response,
			final List<Map<String, Object>> list, final String filePath, final Map<String, Object> result,
			String[] header) {
		final HSSFWorkbook wb = new HSSFWorkbook();
		final HSSFSheet sheet = wb.createSheet();

		HSSFRow row = null;
		// 添加excel头
		row = sheet.createRow(0);
		if (header != null && header.length >= list.get(0).keySet().size()) {
			Cell cell = null;
			for (int i = 0; i < header.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(header[i]);
			}
		} else {
			Cell cell = null;
			final Object[] keys = list.get(0).keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(keys[i].toString());
			}
		}
		// 添加excel内容
		for (int i = 0; i < list.size(); i++) {
			final Map<String, Object> map = list.get(i);
			final Set<String> set = map.keySet();
			final Object[] keys = set.toArray();
			row = sheet.createRow(i + 1);
			Cell cell = null;
			for (int j = 0; j < keys.length; j++) {
				cell = row.createCell(j);
				if (header != null && header.length >= list.get(0).keySet().size()) {
					cell.setCellValue(String.valueOf(map.get(header[j])));
				} else {
					cell.setCellValue(String.valueOf(map.get(keys[j].toString())));
				}
			}
		}

		try {
			result.put("code", 1);
			result.put("message", "成功导出" + list.size() + "条记录到" + filePath);

			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition",
					"attachment;filename=\"" + java.net.URLEncoder.encode("xx", "UTF-8") + ".xls" + "\" ");
			wb.write(response.getOutputStream());
			response.getOutputStream().close();
		} catch (final Exception e) {
			result.put("code", -200);
			result.put("message", e.getMessage());
		}
		return result;
	}

	/**
	 * 导出所有数据 @Title:exportDataAll @param @param response @param @param
	 * list @param @param filePath @param @param header @param @return @return
	 * Map<String,Object> @throws
	 */
	public static Map<String, Object> exportDataAll(HttpServletResponse response, List<Map<String, Object>> parentList,
			String[] parentHeader, Map<Integer, List<Map<String, Object>>> childExportMap, String[] childHeader,
			String filePath) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 1.写入信息
		final String[] strs = filePath.split("\\.");
		final String fileType = strs[strs.length - 1];
		if ("xlsx".equalsIgnoreCase(fileType)) {
			result = writeXlsxDataAll(response, parentList, parentHeader, childExportMap, childHeader, filePath);
		} else if ("xls".equalsIgnoreCase(fileType)) {
			result = writeXlsData(response, parentList, filePath, result, parentHeader);
		} else {
			result.put("code", -103);
			result.put("message", "文件不是正确的EXCEL格式");
		}

		return result;
	}

	public static Map<String, Object> writeXlsxDataAll(HttpServletResponse response,
			List<Map<String, Object>> parentList, String[] parentHeader,
			Map<Integer, List<Map<String, Object>>> childExportMap, String[] childHeader, String filePath) {
		final XSSFWorkbook wb = new XSSFWorkbook();
		final XSSFSheet sheet = wb.createSheet();
		Map<String, Object> result = new HashMap<String, Object>();

		XSSFRow row = null;
		row = sheet.createRow(0);
		row.setHeightInPoints(30);
		XSSFCellStyle parentTitleCellStyle = wb.createCellStyle();
		XSSFCellStyle childTitleCellStyle = wb.createCellStyle();
		XSSFCellStyle cellStyle = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setFontName("微软雅黑");
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		//specify a background cell color
		parentTitleCellStyle.setFillForegroundColor((short) 22);
		parentTitleCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		parentTitleCellStyle.setFont(font);
		parentTitleCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		parentTitleCellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
		parentTitleCellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		parentTitleCellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		parentTitleCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		parentTitleCellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);

		childTitleCellStyle.setFillForegroundColor((short) 42);
		childTitleCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		childTitleCellStyle.setFont(font);
		childTitleCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		childTitleCellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
		childTitleCellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		childTitleCellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		childTitleCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		childTitleCellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);

		cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		// 设置内容表格的格式为文本格式
		XSSFCellStyle textCellStyle = wb.createCellStyle();
		XSSFDataFormat dataFormat = wb.createDataFormat();
		textCellStyle.setDataFormat(dataFormat.getFormat("@"));
		cellStyle.setDataFormat(dataFormat.getFormat("@"));
		// 调整尺寸大小
		int adjustSize = 200;
		// 添加excel头
		if (parentHeader != null) {
			Cell cell = null;
			for (int i = 1; i < parentHeader.length + childHeader.length; i++) {
				cell = row.createCell(i - 1);
				if (i < parentHeader.length) {
					cell.setCellValue(parentHeader[i]);
					if (parentHeader[i].length() > 4) {
						sheet.setColumnWidth(i - 1, (parentHeader[i].getBytes().length) * adjustSize);
					}
					cell.setCellStyle(parentTitleCellStyle);
				} else {
					cell.setCellValue(childHeader[i - parentHeader.length]);
					if (childHeader[i - parentHeader.length].length() > 4) {
						sheet.setColumnWidth(i - 1,
								(childHeader[i - parentHeader.length].getBytes().length) * adjustSize);
					}
					cell.setCellStyle(childTitleCellStyle);
				}
			}
			//从第几行开始创建
			int beginRow = 0;
			// 添加excel内容
			for (int i = 0; i < parentList.size(); i++) {
				List<Map<String, Object>> childExportList = new ArrayList<Map<String, Object>>();
				Map<String, Object> map = parentList.get(i);
				Set<String> set = map.keySet();
				Object[] keys = set.toArray();
				//第一次遍历从第一行，其他从关联明细结束行开始遍历
				if (i == 0) {
					beginRow = i + 1;
					row = sheet.createRow(beginRow);
				} else {
					row = sheet.createRow(beginRow);

				}
				//要导出的主数据关联的字表明细
				childExportList = childExportMap.get(map.get(parentHeader[0]));

				Cell contentCell = null;
				String cellValue = "";
				for (int j = 0; j < keys.length - 1; j++) {
					contentCell = row.createCell(j);
					cellValue = String.valueOf(map.get(parentHeader[j + 1]));
					contentCell.setCellValue(cellValue);
					if (sheet.getColumnWidth(j) < (cellValue.getBytes().length) * adjustSize) {
						sheet.setColumnWidth(j, (cellValue.getBytes().length) * adjustSize);
					}
					contentCell.setCellStyle(cellStyle);
					// 主表设置成文本格式
					contentCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				}

				for (int k = keys.length - 1; k < keys.length - 1 + childHeader.length; k++) {
					contentCell = row.createCell(k);
					contentCell.setCellStyle(cellStyle);
				}

				int creatIndex = keys.length;
				int childNum = 0;
				cellValue = "";
				if (null != childExportList) {
					for (int m = 0; m < childExportList.size(); m++) {
						Map<String, Object> childMap = childExportList.get(m);
						Set<String> childSset = childMap.keySet();
						Object[] childKeys = childSset.toArray();
						row = sheet.createRow(m + beginRow + 1);
						for (int n = creatIndex; n < childKeys.length + creatIndex; n++) {
							contentCell = row.createCell(n - 1);
							cellValue = String.valueOf(childMap.get(childHeader[n - creatIndex]));
							// 子表设置成文本格式
							contentCell.setCellValue(cellValue);
							contentCell.setCellStyle(textCellStyle);
							contentCell.setCellType(XSSFCell.CELL_TYPE_STRING);
							if (sheet.getColumnWidth(n - 1) < (cellValue.getBytes().length) * adjustSize) {
								sheet.setColumnWidth(n - 1, (cellValue.getBytes().length) * adjustSize);
							}
						}
					}
					childNum = childExportList.size();
				}
				beginRow = beginRow + 1 + childNum;
			}
		}

		try {
			result.put("code", 1);
			result.put("message", "成功导出" + parentList.size() + "条记录到" + filePath);
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition",
					"attachment;filename=\"" + java.net.URLEncoder.encode(filePath, "UTF-8") + "\" ");
			wb.write(response.getOutputStream());
			response.getOutputStream().close();
		} catch (final Exception e) {
			result.put("code", 0);
			result.put("message", e.getMessage());
		}
		return result;
	}

	public static List<Row> readXls(final Workbook hssfWorkbook) {
		final List<Row> rows = new ArrayList<Row>();
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			final Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				final Row hssfRow = hssfSheet.getRow(rowNum);
				rows.add(hssfRow);
			}
		}
		return rows;
	}

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public static <E> List<Map> readExcel(Workbook workbook, ExcelImportPojo excelImportPojo) {
		Sheet sheet = workbook.getSheetAt(0);
		Row row;
		List<E> parentBeanList = new ArrayList<E>();
		List<E> childBeanList = new ArrayList<E>();
		Map<Integer, Object> parentMap = new HashMap<Integer, Object>();
		Map<Integer, List<E>> childMap = new HashMap<Integer, List<E>>();
		List<Map> resultEntity = new ArrayList<Map>();
		SimpleDateFormat sDateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATE_DEFAULT);
		SimpleDateFormat timeFormat = new SimpleDateFormat(DateUtils.FORMAT_TIME_HH_MM_SS);
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATETIME_DEFAULT);
		int parentKey = 0;
		int rowNum = 0;
		try {
			for (rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				E parentBean = (E) Class.forName(excelImportPojo.getParentClassName()).newInstance();
				E childBean = (E) Class.forName(excelImportPojo.getChildClassName()).newInstance();
				row = sheet.getRow(rowNum); // 获得行
				for (int i = 0; i < excelImportPojo.getParentHeader().length
						+ excelImportPojo.getChildHeader().length; i++) {
					if (i < excelImportPojo.getParentHeader().length
							&& StringUtils.isNotBlank(LoadUtils.getCell(row, 0))) {
						if (excelImportPojo.getParentField()[i].split(":")[0].equals("Date")) {
							Date date = null;
							if (LoadUtils.getCell(row, i).split(":").length > 2
									&& LoadUtils.getCell(row, i).length() > 10) {
								// yyyy-MM-dd HH:mm:ss
								date = dateFormat.parse(LoadUtils.getCell(row, i));
							} else if (LoadUtils.getCell(row, i).split(":").length > 2) {
								// HH:mm:ss
								date = timeFormat.parse(LoadUtils.getCell(row, i));
							} else {
								// yyyy-MM-dd
								date = sDateFormat.parse(LoadUtils.getCell(row, i));
							}
							BeanUtils.setProperty(parentBean, excelImportPojo.getParentField()[i].split(":")[1], date);
						} else if (excelImportPojo.getParentField()[i].split(":")[0].equals("Integer")) {
							// 此处有可能出现解析数字异常
							BeanUtils.setProperty(parentBean, excelImportPojo.getParentField()[i].split(":")[1],
									Integer.valueOf(LoadUtils.getCell(row, i)));
						} else {
							BeanUtils.setProperty(parentBean, excelImportPojo.getParentField()[i],
									LoadUtils.getCell(row, i));
						}
						if (i == excelImportPojo.getParentHeader().length - 1) {
							break;
						}

					} else {
						for (int j = 0; j < excelImportPojo.getChildField().length; j++) {
							i = excelImportPojo.getParentHeader().length + j;
							if (excelImportPojo.getChildField()[j].split(":")[0].equals("Date")) {
								Date date = null;
								if (LoadUtils.getCell(row, i).split(":").length > 2
										&& LoadUtils.getCell(row, i).length() > 10) {
									date = dateFormat.parse(LoadUtils.getCell(row, i));
								} else if (LoadUtils.getCell(row, i).split(":").length > 2) {
									date = timeFormat.parse(LoadUtils.getCell(row, i));
								} else {
									date = sDateFormat.parse(LoadUtils.getCell(row, i));
								}
								BeanUtils.setProperty(childBean, excelImportPojo.getChildField()[j].split(":")[1],
										date);
							} else if (excelImportPojo.getChildField()[j].split(":")[0].equals("Integer")) {
								BeanUtils.setProperty(childBean, excelImportPojo.getChildField()[j].split(":")[1],
										Integer.valueOf(LoadUtils.getCell(row, i)));
							} else {
								BeanUtils.setProperty(childBean, excelImportPojo.getChildField()[j],
										LoadUtils.getCell(row, i));
							}
						}
					}
				}
				if (StringUtils.isBlank(LoadUtils.getCell(row, 0))) {
					childBeanList.add(childBean);
				}
				if (StringUtils.isNotBlank(LoadUtils.getCell(row, 0))) {
					parentKey = rowNum;
					parentMap.put(parentKey, parentBean);
				}
				if (null != childBeanList && childBeanList.size() > 0) {
					if (StringUtils.isNotBlank(LoadUtils.getCell(sheet.getRow(rowNum + 1), 0))
							&& rowNum != sheet.getLastRowNum()) {
						childMap.put(parentKey, childBeanList);
						childBeanList = new ArrayList<E>();
					} else if (rowNum == sheet.getLastRowNum()) {
						childMap.put(parentKey, childBeanList);
					}
				}

			}
			resultEntity.add(parentMap);
			resultEntity.add(childMap);
		} catch (Exception e) {
			throw new BusinessException("IMPORT_ERROR_INFO", +rowNum + "行格式有误，请检查！");
		}
		return resultEntity;
	}

	public static Map<String, Object> readExcelDataAll(Workbook workbook,ExcelImportPojo excelImportPojo) {
		Sheet sheet = workbook.getSheetAt(0);
		Row row;
		// 盛放子表对象的集合
		List<Object> childBeanList = new ArrayList<Object>();
		// 盛放（行标:主表对象）的Map
		Map<Integer, Object> parentMap = new HashMap<Integer, Object>();
		// (行标:子表对象集合)的Map
		Map<Integer, Object> childMap = new HashMap<Integer, Object>();
		// 整行空行的标记

		// 存放(行角标:主表对象)以及(行角标:子表对象集合)的整体集合
		Map<String, Object> resultEntity = new HashMap<String, Object>();
		// 日期转换工具
		SimpleDateFormat sDateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATE_DEFAULT);
		SimpleDateFormat timeFormat = new SimpleDateFormat(DateUtils.FORMAT_TIME_HH_MM_SS);
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.FORMAT_DATETIME_DEFAULT);
		int parentKey = 0;
		int rowNum = 0;
		boolean thisRowParentBlank = false;
		boolean nextRowParentBlank = false;
		try {
			loopMain: for (rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				Object parentBean = Class.forName(excelImportPojo.getParentClassName()).newInstance();
				Object childBean = Class.forName(excelImportPojo.getChildClassName()).newInstance();
				row = sheet.getRow(rowNum); // 获得行
				for (int i = 0; i < excelImportPojo.getParentHeader().length
						+ excelImportPojo.getChildHeader().length; i++) {
					if (i < excelImportPojo.getParentHeader().length
							&& !isAllLineBlank(row, excelImportPojo.getParentHeader().length)) {// 修复bug：StringUtils.isNotBlank(LoadUtils.getCell(row,
																								// 0))
						if (excelImportPojo.getParentField()[i].split(":")[0].equals("Date")) {
							Date date = null;
							if (StringUtils.isBlank(LoadUtils.getCell(row, i))) {
								throw new BusinessException("第" + (rowNum + 1) + "行：["+excelImportPojo.getParentHeader()[i]+":不能为空！]");
							}
							try {
								if (LoadUtils.getCell(row, i).split(":").length > 2
										&& LoadUtils.getCell(row, i).length() > 10) {
									// yyyy-MM-dd HH:mm:ss
									date = dateFormat.parse(LoadUtils.getCell(row, i));
								} else if (LoadUtils.getCell(row, i).split(":").length > 2) {
									// HH:mm:ss
									date = timeFormat.parse(LoadUtils.getCell(row, i));
								} else {
									// yyyy-MM-dd
									date = sDateFormat.parse(LoadUtils.getCell(row, i));
								}
								
							} catch (Exception e) {
								throw new BusinessException("第" + (rowNum + 1) + "行：["+excelImportPojo.getParentHeader()[i]+":格式错误！]");
							}
							
							BeanUtils.setProperty(parentBean, excelImportPojo.getParentField()[i].split(":")[1], date);
						} else if (excelImportPojo.getParentField()[i].split(":")[0].equals("Integer")) {
							// 此处有可能出现解析数字异常
							try {
								if (StringUtils.isNotBlank(LoadUtils.getCell(row, i))) {
									BeanUtils.setProperty(parentBean, excelImportPojo.getParentField()[i].split(":")[1],
											Integer.valueOf(LoadUtils.getCell(row, i)));
								}
							} catch (Exception e) {
								throw new BusinessException("第" + (rowNum + 1) + "行：["+excelImportPojo.getParentHeader()[i]+":格式错误！]");
							}
							
						} else {
							if (StringUtils.isNotBlank(LoadUtils.getCell(row, i))) {
								BeanUtils.setProperty(parentBean, excelImportPojo.getParentField()[i],
										LoadUtils.getCell(row, i));
							}
						}
						if (i == excelImportPojo.getParentHeader().length - 1) {
							break;
						}

					} else {
						// 先判断整行是否为空
						if (isAllLineBlank(row,
								excelImportPojo.getParentHeader().length + excelImportPojo.getChildHeader().length)) {
							resultEntity.put("blankLineInfo",
									"第" + (rowNum + 1) + "行:该行整行为空，至此行导入中断，请检查处理后再行操作！");
							break loopMain;
						}
						for (int j = 0; j < excelImportPojo.getChildField().length; j++) {
							i = excelImportPojo.getParentHeader().length + j;
							if (excelImportPojo.getChildField()[j].split(":")[0].equals("Date")) {
								Date date = null;
								if (StringUtils.isBlank(LoadUtils.getCell(row, i))) {
									throw new BusinessException("第" + (rowNum + 1) + "行：["+excelImportPojo.getChildHeader()[j]+":不能为空！]");
								}
								try {
									
									if (LoadUtils.getCell(row, i).split(":").length > 2
											&& LoadUtils.getCell(row, i).length() > 10) {
										date = dateFormat.parse(LoadUtils.getCell(row, i));
									} else if (LoadUtils.getCell(row, i).split(":").length > 2) {
										date = timeFormat.parse(LoadUtils.getCell(row, i));
									} else {
										date = sDateFormat.parse(LoadUtils.getCell(row, i));
									}
								} catch (Exception e) {
									throw new BusinessException("第" + (rowNum + 1) + "行：["+excelImportPojo.getChildHeader()[j]+":格式错误！]");
								}
								
								BeanUtils.setProperty(childBean, excelImportPojo.getChildField()[j].split(":")[1],
										date);
							} else if (excelImportPojo.getChildField()[j].split(":")[0].equals("Integer")) {
								try {
									if (StringUtils.isNotBlank(LoadUtils.getCell(row, i))) {
										BeanUtils.setProperty(childBean, excelImportPojo.getChildField()[j].split(":")[1],
												Integer.valueOf(LoadUtils.getCell(row, i)));
									}
								} catch (Exception e) {
									throw new BusinessException("第" + (rowNum + 1) + "行：["+excelImportPojo.getChildHeader()[j]+":格式错误！]");
								}
								
							} else {
								BeanUtils.setProperty(childBean, excelImportPojo.getChildField()[j],
										LoadUtils.getCell(row, i));
							}
						}
					}
				}

				// 判断本行和下行，主数据列是否都为空
				thisRowParentBlank = isAllLineBlank(row, excelImportPojo.getParentHeader().length);
				nextRowParentBlank = isAllLineBlank(sheet.getRow(rowNum + 1), excelImportPojo.getParentHeader().length);
				if (thisRowParentBlank) {
					childBeanList.add(childBean);
					// 本行主数据列无数据，下行主数据列无数据，且不是最后一行：正在读取子数据中间的某一条
					if (nextRowParentBlank && rowNum != sheet.getLastRowNum()) {
						childMap.put(parentKey, childBeanList);

					} else {
						// 本行主数据列无数据，下行主数据列有数据，或是最后一行：已读取某个主数据的最后一条子数据
						childMap.put(parentKey, childBeanList);
						childBeanList = new ArrayList<Object>();
					}
				} else {
					parentKey = rowNum;
					parentMap.put(parentKey, parentBean);
				}
			}

			resultEntity.put("parentMap", parentMap);
			resultEntity.put("childrenMap", childMap);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("第" + (rowNum + 1) + "行存在数据格式错误，请检查修改后重新操作！");
		}
		return resultEntity;

	}

	public static Map<String, String> getSplitValMap(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		String[] strs = value.split("-");
		int len = strs.length;
		Map<String, String> map = new HashMap<String, String>();
		if (len == 1) {
			map.put("left", value);
			return map;
		} else if (len == 2) {
			map.put("left", strs[0].trim());
			map.put("right", strs[1].trim());
			return map;
		} else if (len == 4) {
			map.put("left", (strs[0] + "-" + strs[1]).trim());
			map.put("right", (strs[2] + "-" + strs[3]).trim());
			return map;
		} else if (len == 3) {
			if (Pattern.compile("[\u4e00-\u9fa5]").matcher(strs[1]).find()) {
				map.put("left", strs[0].trim());
				map.put("right", (strs[1] + "-" + strs[2]).trim());
			} else {
				if (Pattern.compile("^\\d+$").matcher(strs[2]).find()) {
					map.put("left", strs[0].trim());
					map.put("right", (strs[1] + "-" + strs[2]).trim());
				} else {
					map.put("left", (strs[0] + "-" + strs[1]).trim());
					map.put("right", strs[2].trim());
				}
			}
			//此种方式也可以应付比较正常的起名方式
			/*if(!Pattern.compile("^\\d+$").matcher(strs[1]).find()
			&&!Pattern.compile("[a-zA-Z]+").matcher(strs[1]).find()){
				map.put("left", strs[0].trim());
				map.put("right", (strs[1]+"-"+strs[2]).trim());
			}else{
				map.put("left", (strs[0]+"-"+strs[1]).trim());
				map.put("right", strs[2].trim());
			}*/
			return map;
		}
		return null;
	}
    
	/**
	 * @desc 获取页面提示信息MsgTip(不含导入失败总条数),以及导入时错误信息日志记录
	 * @param repeatOrUpdateFlag
	 *            重复数据是否覆盖（全局配置参数）
	 * @param addCount
	 *            成功新增记录数
	 * @param updateCount
	 *            成功更新记录数
	 * @param repeatLindexes
	 *            重复记录在Excel表格中的行标
	 * @param errorInfos
	 *            错误信息集合
	 * @param req
	 *            HttpServletRequest
	 * @param operateEntityName
	 *            操作对象
	 * @return
	 */
    //获取导入错误信息日志，以及页面提醒的简略信息
    public static Map<String,Object> getLogsAndMsgTip(String repeatOrUpdateFlag,StringBuffer addCount,StringBuffer updateCount,Set<Integer> repeatLindexes,
			List<String> errorInfos,HttpServletRequest req,String operateEntityName ){
	
		//返回信息
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
			if (repeatLindexes != null && (!repeatLindexes.isEmpty())) {
				msgTip.append("因系统存在重复数据，而导入失败的记录如下:<br/>");
				int count = 0;
				for (Integer rowIndex : repeatLindexes) {
					StringBuffer msgLog = new StringBuffer(getMessage(req, "REPEAT_DATA_WHEN_IMPORT"));
					ImportLog log = new ImportLog();
					msgLog.append("第" + rowIndex + "行");
					if (count < ConstantUtils.TIPS_COUNT_FOR_DUPLICATE_ERROR) {
						msgTip.append("第" + rowIndex + "行,");
					}
					if (count == ConstantUtils.TIPS_COUNT_FOR_DUPLICATE_ERROR) {
						msgTip.append(ConstantUtils.SUSPENSION_POINTS);
					}
					log.setErrorDesc(msgLog.toString());
					log.setOperateEntityName(operateEntityName);
					logs.add(log);
					count++;
				}
				msgTip.append("<br/>");
			}
		}

		if (!errorInfos.isEmpty()) {
			msgTip.append("因格式错误，而导入失败的数据如下:<br/>");
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
				log.setOperateEntityName(operateEntityName);
				logs.add(log);
				count++;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msgTip", msgTip.toString());
		map.put("logs", logs);
		return map;
	}

	/**
	 * @desc 获取页面提示信息MsgTip(含导入失败总条数),以及导入时错误信息日志记录
	 * @param repeatOrUpdateFlag
	 *            重复数据是否覆盖（全局配置参数）
	 * @param addCount
	 *            成功新增记录数
	 * @param updateCount
	 *            成功更新记录数
	 * @param repeatLindexes
	 *            重复记录在Excel表格中的行标
	 * @param errorInfos
	 *            错误信息集合
	 * @param req
	 *            HttpServletRequest
	 * @param operateEntityName
	 *            操作对象
	 * @return
	 */
	// 获取导入错误信息日志，以及页面提醒的简略信息
	public static Map<String, Object> getLogsAndMsgTip(String repeatOrUpdateFlag, StringBuffer addCount,
			StringBuffer updateCount, int totalInt, Set<Integer> repeatLindexes, List<String> errorInfos,
			HttpServletRequest req, String operateEntityName) {

		// 返回信息
		int addCountInt = Integer.parseInt(addCount.toString());
		StringBuffer msgTip = new StringBuffer();
		List<ImportLog> logs = new ArrayList<ImportLog>();
		msgTip.append("成功新增:");
		msgTip.append(addCount);
		msgTip.append("条记录;<br/>");
		if ("true".equals(repeatOrUpdateFlag)) {
			int updateCountInt = Integer.parseInt(updateCount.toString());
			msgTip.append("成功更新:");
			msgTip.append(updateCount);
			msgTip.append("条记录;<br/>");
			msgTip.append("已读数据中，导入失败:");
			msgTip.append(totalInt - addCountInt - updateCountInt);
			msgTip.append("条记录;<br/>");
		} else {
			msgTip.append("已读数据中，导入失败:");
			msgTip.append(totalInt - addCountInt);
			msgTip.append("条记录;<br/>");
			if (repeatLindexes != null && (!repeatLindexes.isEmpty())) {
				msgTip.append("因系统存在重复数据，而导入失败的记录如下:<br/>");
				int count = 0;
				for (Integer rowIndex : repeatLindexes) {
					StringBuffer msgLog = new StringBuffer(getMessage(req, "REPEAT_DATA_WHEN_IMPORT"));
					ImportLog log = new ImportLog();
					msgLog.append("第" + rowIndex + "行");
					if (count < ConstantUtils.TIPS_COUNT_FOR_DUPLICATE_ERROR) {
						msgTip.append("第" + rowIndex + "行,");
					}
					if (count == ConstantUtils.TIPS_COUNT_FOR_DUPLICATE_ERROR) {
						msgTip.append(ConstantUtils.SUSPENSION_POINTS);
					}
					log.setErrorDesc(msgLog.toString());
					log.setOperateEntityName(operateEntityName);
					logs.add(log);
					count++;
				}
				msgTip.append("<br/>");
			}
		}

		if (!errorInfos.isEmpty()) {
			msgTip.append("因格式错误，而导入失败的数据如下:<br/>");
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
				log.setOperateEntityName(operateEntityName);
				logs.add(log);
				count++;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msgTip", msgTip.toString());
		map.put("logs", logs);
		return map;
	}

	/**
	 * 判断整行是不是空
	 * @return
	 */
	public static boolean isAllLineBlank(Row row, int judgeSize) {
		boolean boo = true;
		for(int i=0;i<judgeSize;i++){
			if (StringUtils.isNotBlank(LoadUtils.getCell(row, i))) {
				boo = false;
				break;
			}
		}
		return boo;
	}

}
