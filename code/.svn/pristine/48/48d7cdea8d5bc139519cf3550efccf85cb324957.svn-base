package com.wis.basis.common.web.download;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

public class CommonDownloadView extends AbstractView {

	@SuppressWarnings("rawtypes")
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String savePath = (String) request.getAttribute(Constants.DEFAULT_COMMON_DOWNLOAD_SAVE_PATH);

		String fileType = (String) request.getAttribute(Constants.DEFAULT_COMMON_DOWNLOAD_FILE_TYPE);

		String aFilePath = savePath + File.separator;

		String aFileName = request.getAttribute(Constants.DEFAULT_COMMON_DOWNLOAD_FILE_NAME) + fileType;

		String showFileName = (String) request.getAttribute(Constants.DEFAULT_COMMON_DOWNLOAD_SHOW_FILE_NAME)
				+ fileType;

		FileInputStream in = null;
		ServletOutputStream out = null;

		try {

			//自己扩展
			if (".xls".equals(fileType) || ".xlsx".equals(fileType)) {
				response.setContentType("application/vnd.ms-excel");
			} else if (".zip".equals(fileType)) {
				response.setContentType("application/zip");
			} else if (".tar".equals(fileType)) {
				response.setContentType("application/x-tar");
			} else if (".txt".equals(fileType)) {
				response.setContentType("application/octet-stream;charset=GBK");
			}

			response.setCharacterEncoding("GBK");

			//展示给用户的文件名
			response.setHeader("Content-disposition",
					"attachment; filename=" + java.net.URLEncoder.encode(showFileName, "UTF-8"));

			in = new FileInputStream(aFilePath + aFileName);
			out = response.getOutputStream();

			int aRead = 0;
			while ((aRead = in.read()) != -1 & in != null) {
				out.write(aRead);
			}
			out.flush();
		} catch (Exception e) {
			logger.error("Exception raised-------->", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {

			}
		}
	}

}
