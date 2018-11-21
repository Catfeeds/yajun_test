package com.wis.mes.util; 

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**   
 * @author Jixueyuan   
 * @date 2017年6月10日
 * @Description: 校验图片的是否合法
 */
public class CheckImageUtils {
	public static String  checkImage(MultipartFile rawFile,String realPath){
				//图片是否合法FLAG
				boolean typeFlag = false;
				boolean contentFlag = false;
				String msg = "";//图片校验结果
				//允许上传的图片格式
				final String[] IMAGE_TYPE = new String[] { ".jpg", ".jpeg",  ".png",".bmp", ".gif" };
				for(String type : IMAGE_TYPE) {
					if (StringUtils.endsWithIgnoreCase(rawFile.getOriginalFilename(), type)) {
					    typeFlag = true;
					    break;
			            }     
			     }
					File newFile=new File(realPath);
					if (!newFile.isDirectory()) {
					    // 如果目录不存在，则创建目录
					    newFile.mkdirs(); 
					}
					try {
						rawFile.transferTo(newFile);
						//获取图片的长和宽,如果获取不到则图片为非法图片
						 BufferedImage image = ImageIO.read(newFile);
						 if (image != null) {
						      if(StringUtil.isNotBlank(image.getWidth() + "")){
						            contentFlag = true;
						         }
						      if(StringUtil.isNotBlank(image.getHeight() + "")){
						             contentFlag = true;
						         }
						  }
						 if ((!typeFlag)||(!contentFlag)){
			                 newFile.delete(); // 不合法,将文件删除	
			                 msg="请上传合法的图片";
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
					return msg;
	}
}
