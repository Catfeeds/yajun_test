package com.wis.basis.systemadmin.controller;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.framework.service.GlobalConfigurationService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/system/upload")
public class UploadController extends BaseController {

	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	/**
	 * 导入
	 * 
	 * @param modelMap
	 * @param importUrl
	 *            导入模块处理方法地址
	 * @param templateUrl
	 *            模版地址
	 * @return
	 * @param callback
	 *            导入文件后返回函数
	 */
	@RequestMapping(value = "/importFileInput")
	public ModelAndView importFileInput(ModelMap modelMap, String importUrl, @RequestParam(defaultValue = "") String templateUrl,
			@RequestParam(defaultValue = "'xls','xlsx'") String fileTypeDesc, @RequestParam(defaultValue = "") String callback) {
		modelMap.addAttribute("importUrl", importUrl);
		if (StringUtils.isNotBlank(templateUrl)) {
			modelMap.addAttribute("templateUrl", templateUrl);
		}
		modelMap.addAttribute("allowedFileExtensions", fileTypeDesc);
		modelMap.addAttribute("callback", callback);
		return new ModelAndView("/upload_template/file_import");
	}
	
	
	@RequestMapping(value = "/importImageInput")
	public ModelAndView importImageInput(ModelMap modelMap, String importUrl, @RequestParam(defaultValue = "") String callback,
			@RequestParam(defaultValue = "'bmp','jpg','jpeg','png','gif'") String fileTypeDesc) {
		modelMap.addAttribute("importUrl", importUrl);
		modelMap.addAttribute("allowedFileExtensions", fileTypeDesc);
		modelMap.addAttribute("callback", callback);
		return new ModelAndView("/upload_template/image_import");
	}

	/**
	 * 上传附件页面
	 * 
	 * @param modelMap
	 * @param response
	 * @param urlId
	 *            存储附件字段名
	 * @param previewId
	 *            预览附件ID
	 * @param deleteId
	 *            删除附件ID
	 * @return
	 */
	@RequestMapping(value = "/importInput")
	public ModelAndView importInput(ModelMap modelMap, HttpServletResponse response, String urlId, String previewId, String deleteId) {
		modelMap.addAttribute("urlId", urlId);
		if (previewId != null && !("").equals(previewId)) {
			modelMap.addAttribute("previewId", previewId);
		} else {
			modelMap.addAttribute("previewId", "preview");
		}
		if (deleteId != null && !("").equals(deleteId)) {
			modelMap.addAttribute("deleteId", deleteId);
		} else {
			modelMap.addAttribute("deleteId", "delete");
		}
		return new ModelAndView("/upload_template/importinput");
	}

	/**
	 * 上传图片页面
	 * 
	 * @param modelMap
	 * @param response
	 * @param urlId
	 *            存储图片字段名
	 * @param previewId
	 *            预览图片ID
	 * @param deleteId
	 *            删除图片ID
	 * @return
	 */
	@RequestMapping(value = "/importImgInput")
	public ModelAndView importImgInput(ModelMap modelMap, HttpServletResponse response, String urlId, String previewId, String deleteId) {
		modelMap.addAttribute("urlId", urlId);
		if (previewId != null && !("").equals(previewId)) {
			modelMap.addAttribute("previewId", previewId);
		} else {
			modelMap.addAttribute("previewId", "preview");
		}
		if (deleteId != null && !("").equals(deleteId)) {
			modelMap.addAttribute("deleteId", deleteId);
		} else {
			modelMap.addAttribute("deleteId", "delete");
		}
		return new ModelAndView("/upload_template/import_img");
	}

	/**
	 * 删除附件
	 * 
	 * @param modelMap
	 * @param response
	 * @param name
	 *            附件地址
	 */
	@RequestMapping(value = "/deleteAttachment")
	public void deleteAttachment(ModelMap modelMap, HttpServletResponse response, String name) {
		JSONObject result = new JSONObject();
		try {
			String ctxPath = globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_ATTACHMENT_BASE_PATH) + File.separator;
			name = name.substring(name.lastIndexOf("/"));
			File file = new File(ctxPath + name);
			if (file.isFile() && file.exists()) {
				file.delete();
			}
			result.put("status", "y");
			result.put("info", "删除成功！");
		} catch (Exception e) {
			result.put("status", "n");
			result.put("info", e.getMessage());
		}
		ActionUtils.handleResult(response, result);
	}

	/**
	 * 删除图片
	 * 
	 * @param modelMap
	 * @param response
	 * @param name
	 *            图片地址
	 */
	@RequestMapping(value = "/deleteImg")
	public void deleteImg(ModelMap modelMap, HttpServletResponse response, String name) {
		JSONObject result = new JSONObject();
		try {
			String ctxPath = globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_IMG_BASE_PATH) + File.separator;
			name = name.substring(name.lastIndexOf("/"));
			File file = new File(ctxPath + name);
			if (file.isFile() && file.exists()) {
				file.delete();
			}
			result.put("status", "y");
			result.put("info", "删除成功！");
		} catch (Exception e) {
			result.put("status", "n");
			result.put("info", e.getMessage());
		}
		ActionUtils.handleResult(response, result);
	}

}
