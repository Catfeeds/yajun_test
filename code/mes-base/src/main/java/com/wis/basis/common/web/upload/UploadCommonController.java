package com.wis.basis.common.web.upload;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.wis.core.framework.service.exception.BusinessException;

@Controller
@RequestMapping(value="/uploadCommon")
public class UploadCommonController extends MultiActionController{
    
    private Log logger = LogFactory.getLog("businessLogger");
	
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    private String savePath = "/upload/";
    
    @RequestMapping(value="/upload")
    public String upload(HttpServletRequest request, HttpServletResponse response) throws BusinessException{  
                  
        String responseStr="";  
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();    
        String ctxPath=request.getSession().getServletContext().getRealPath(savePath) + File.separator;   
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
        String ymd = sdf.format(new Date());  
        ctxPath += ymd + File.separator;  
        //创建文件夹  
        File file = new File(ctxPath);    
        if (!file.exists()) {    
            file.mkdirs();    
        }
        String fileName = null; 
        String fileExt = null;
        String newFileName = null;
        File uploadFile = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Random random = new Random();
        StringBuffer succRes = new StringBuffer();
        StringBuffer erreRes = new StringBuffer();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            fileName = mf.getOriginalFilename();
            fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            newFileName = df.format(new Date()) + "_" + random.nextInt(1000) + "." + fileExt;
            uploadFile = new File(ctxPath + newFileName);
            try {
                FileCopyUtils.copy(mf.getBytes(), uploadFile);
                succRes.append(fileName).append("|");
                responseStr = "文件"+succRes+"上传成功！";//getMessageSourceAccessor().getMessage("", request.getLocale());
                logger.debug(responseStr);
            } catch (IOException e) {
                erreRes.append(fileName).append("|");
                responseStr = "文件"+erreRes+"上传失败！";
                logger.error(responseStr,e);
            }
        }
        return null;
    }
}
