package com.wis.mes.util;
 
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
 
public class ImageDraw {
	
	private static String templetImagePath="D:\\image\\1\\aa.jpg";
	
	private static String leaveWithPath="D:\\image\\2\\aa2.jpg";
	
	private static String qrCodePath="D:\\image\\2\\qrcode.png";
	
	public static void main(String[] args) {
		draw(templetImagePath,leaveWithPath,"702194778F002475331111");
	}
	
	/***
	 * @author yajun_ren
	 * @date 2018/10/12
	 * @desc 图片绘画
	 * @param imagePath,path,content,
	 * 
	 * **/
	public static void draw(String imagePath,String path,String content){
		//读取图片文件，得到BufferedImage对象
		BufferedImage bimg;
		try {
			bimg = ImageIO.read(new FileInputStream(imagePath));
			CreateQRCode(300,300,content,"PNG",qrCodePath);
			File srcImgFile = new File(qrCodePath);
            Image srcImg = ImageIO.read(srcImgFile);
			
			//得到Graphics2D 对象
			Graphics2D g2d=(Graphics2D)bimg.getGraphics();
			//设置背景颜色
			g2d.setBackground(Color.WHITE);
			//设置颜色和画笔粗细
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(3));
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			//绘制图案或文字
			g2d.drawString("RQCZQ7BAVN", 75, 43);
			g2d.drawString("A078", 75, 23);
			//插入图片
			g2d.drawImage(srcImg, 400, 20, 70, 70,null);
			
			//保存新图片
			ImageIO.write(bimg, "PNG",new FileOutputStream(path));
			} catch (Exception e) {
				e.printStackTrace();
			}
	   }
	
	/***
	 * @author yajun_ren
	 * @date 2018/10/12
	 * @desc 生成二维码
	 * @param width,height,content,format(生成的图片类型),pathName(文件存放的路径)
	 * 
	 * **/
	public static void CreateQRCode(int width,int height,String content,String format,String pathName) {
		 //  width = 300; height = 300; format = "png";content = "www.shuai.com";
		   
	       //定义二维码的参数
	       HashMap map = new HashMap();
	       //设置编码
	       map.put(EncodeHintType.CHARACTER_SET, "utf-8");
	       //设置纠错等级
	       map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
	       map.put(EncodeHintType.MARGIN, 4);

	       try {
	           //生成二维码
	           BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height);
	           Path file = new File(pathName).toPath();
	           MatrixToImageWriter.writeToPath(bitMatrix, format, file);
	       } catch (WriterException e) {
	           e.printStackTrace();
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	}
	
}
