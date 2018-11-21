package com.wis.basis.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.web.download.Constants;

@Controller
@RequestMapping(value="/testDownload")
public class TestDownloadAction extends BaseController {

    @RequestMapping(value="/init")
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("test/test_upload_download");
        return mav;
    }
    
    @RequestMapping(value="/download")
    public ModelAndView download(HttpServletRequest request, HttpServletResponse response) {
        String result = "测试下载";
        
        request.setAttribute(Constants.DEFAULT_COMMON_DOWNLOAD_SAVE_PATH, request.getSession().getServletContext().getRealPath("download"));
        request.setAttribute(Constants.DEFAULT_COMMON_DOWNLOAD_FILE_TYPE, ".xlsx");
        request.setAttribute(Constants.DEFAULT_COMMON_DOWNLOAD_FILE_NAME, "test_download");
        request.setAttribute(Constants.DEFAULT_COMMON_DOWNLOAD_SHOW_FILE_NAME, "测试下载文件");
        ModelAndView mav = new ModelAndView("CommonDownload");
        mav.addObject(result);
        return mav;
    }

}
