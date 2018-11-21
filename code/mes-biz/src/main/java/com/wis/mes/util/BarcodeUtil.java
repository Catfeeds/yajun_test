package com.wis.mes.util;

import java.io.OutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * @author LIUZEJUN
 *
 *
 * @date 2017年5月13日下午8:21:50
 *
 * @desc 条码打印
 */
public class BarcodeUtil {
	private final static int WIDTH = 110;
	private final static int HIGHT = 50;

	/**
	 * 生成条形码到流
	 *
	 * @param msg
	 * @param ous
	 */
	public static void generateBarToStream(String content, OutputStream ous) {
		int codeWidth = 3 + // start guard
				(7 * 6) + // left bars
				5 + // middle guard
				(7 * 6) + // right bars
				3; // end guard
		codeWidth = Math.max(codeWidth, WIDTH);
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, codeWidth, HIGHT,
					null);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", ous);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
