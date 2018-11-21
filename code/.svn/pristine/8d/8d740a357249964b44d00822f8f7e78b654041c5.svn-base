package com.wis.basis.systemadmin.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.RequestContext;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.LogConstant;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.systemadmin.model.FileUploadResponse;
import com.wis.core.framework.entity.Attachment;
import com.wis.core.framework.service.AttachmentService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;

@Controller
@RequestMapping(value = "/attachment")
public class AttachmentController extends BaseController {

	protected final Log logger = LogFactory.getLog(LogConstant.SYS_LOG);

	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@RequestMapping(value = "/upload")
	@ResponseBody
	public FileUploadResponse upload(HttpServletRequest request, HttpServletResponse response) {
		FileUploadResponse fileUploadResponse = new FileUploadResponse();
		RequestContext context = new RequestContext(request);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, List<MultipartFile>> fileMap = multipartRequest.getMultiFileMap();
		File targetFile = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		File attachmentFolder = new File(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_ATTACHMENT_BASE_PATH) + File.separator + sdf.format(new Date()));
		if (!attachmentFolder.exists()) {
			attachmentFolder.mkdirs();
		}
		String fileName = null;
		String fileExt = null;
		String newFileName = null;
		SimpleDateFormat df = new SimpleDateFormat("HHmmssSSS");
		// String baseUrl = getBaseLocation(request);
		List<Integer> errorKeys = new ArrayList<Integer>();
		int index = 0;
		for (Entry<String, List<MultipartFile>> entity : fileMap.entrySet()) {
			List<MultipartFile> mfs = entity.getValue();
			for (MultipartFile mf : mfs) {
				fileName = mf.getOriginalFilename();
				try {
					// if (index % 2 == 0) {
					// throw new Exception("d");
					// }
					fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + df.format(new Date()) + "." + fileExt;
					targetFile = new File(attachmentFolder, newFileName);
					mf.transferTo(targetFile);
					Attachment attachment = new Attachment();
					attachment.setName(mf.getOriginalFilename());
					attachment.setUrl(targetFile.getAbsolutePath());
					attachment = attachmentService.doSave(attachment);
					fileUploadResponse.setAttachmentId(attachment.getId());
					// InitialPreviewConfig initialPreviewConfig = new
					// InitialPreviewConfig();
					// initialPreviewConfig.setUrl(baseUrl +
					// "/attachment/delete.do");
					// initialPreviewConfig.setKey(index);
					// initialPreviewConfig.addExtra("id", attachment.getId());
					// fileUploadResponse.addInitialPreviewConfig(initialPreviewConfig);
				} catch (Exception e) {
					errorKeys.add(index);
				}
				// fileUploadResponse.addInitialPreview(fileName);
				index++;
			}
		}
		if (!errorKeys.isEmpty()) {
			Object[] params = new Object[1];
			params[0] = errorKeys.size();
			fileUploadResponse.setError(context.getMessage("ERROR_FILE_UPLOAD", params));
			fileUploadResponse.setErrorkeys(errorKeys.toArray(new Integer[errorKeys.size()]));
		}
		return fileUploadResponse;
	}

	@RequestMapping(value = "/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) {

	}

	@ResponseBody
	@RequestMapping(value = "/down")
	public JsonActionResult down(HttpServletRequest request, HttpServletResponse response, Integer id) {
		if (id != null) {
			Attachment attachment = attachmentService.findById(id);
			try {
				LoadUtils.setContent(request, response, new File(attachment.getUrl()), attachment.getName());
				return ActionUtils.handleResult(response);
			} catch (IOException e) {
				logger.error("Error when download attachment, id: " + id, e);
			}
		}
		logger.error("Error download attachment, id is null");
		throw new BusinessException("ERROR_DOWNLOAD_FILE");
	}

	@ResponseBody
	@RequestMapping(value = "/view")
	public JsonActionResult view(HttpServletRequest request, HttpServletResponse response, Integer id) {
		if (id != null) {
			Attachment attachment = attachmentService.findById(id);
			try {
				LoadUtils.viewContent(request, response, new File(attachment.getUrl()), attachment.getName());
				return ActionUtils.handleResult(response);
			} catch (IOException e) {
				logger.error("Error when download attachment, id: " + id, e);
			}
		}
		logger.error("Error download attachment, id is null");
		throw new BusinessException("ERROR_DOWNLOAD_FILE");
	}
}
