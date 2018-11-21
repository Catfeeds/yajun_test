package com.wis.basis.common.utils;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;

public class WorkBookUtils {

	public static String getStringExcelContent(Row row, int colIndex) {
		Cell cell = row.getCell(colIndex);
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return NumberToTextConverter.toText(cell.getNumericCellValue());
			}
		}
		return null;
	}

	public static Double getNumericExcelContent(Row row, int colIndex) {
		Cell cell = row.getCell(colIndex);
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return cell.getNumericCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return Double.parseDouble(cell.getStringCellValue());
			}
		}
		return null;
	}

	public static Boolean getBooleanExcelContent(Row row, int colIndex) {
		Cell cell = row.getCell(colIndex);
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return "1".equals(cell.getStringCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return 1 == cell.getNumericCellValue();
			}
		}
		return null;
	}

	public static String getDateExcelContent(Row row, int colIndex,
			SimpleDateFormat format) {
		Cell cell = row.getCell(colIndex);
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return format.format(cell.getDateCellValue());
			}
		}
		return null;
	}

	/**
	 * 读取路径上的PNG格式图片存到excel表格中
	 * 
	 * @param file
	 *            路径
	 * @param sheet
	 * @param wb
	 * @param anchor
	 */
	
	public static void saveImageToExcel(ByteArrayOutputStream byteArrayOut,
			Sheet sheet, Workbook wb, ClientAnchor anchor,boolean size) {
		try {
			if (size) {
				sheet.createDrawingPatriarch()
				.createPicture(
						anchor,
						wb.addPicture(byteArrayOut.toByteArray(),
								HSSFWorkbook.PICTURE_TYPE_PNG)).resize(1);
			}else{
				sheet.createDrawingPatriarch()
				.createPicture(
						anchor,
						wb.addPicture(byteArrayOut.toByteArray(),
								HSSFWorkbook.PICTURE_TYPE_PNG));
			}
		}finally{
			IOUtils.closeQuietly(byteArrayOut);
		}
	}
}
