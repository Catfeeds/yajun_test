package com.wis.basis.systemadmin.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUploadResponse extends HashMap<String, Object> {

	public void setAttachmentId(Integer attachmentId){
		this.put("attachmentId", attachmentId);
	}
	
	public void setError(String error) {
		this.put("error", error);
	}

	public void setErrorkeys(Integer[] errorkeys) {
		this.put("errorkeys", errorkeys);
	}

	public void addInitialPreviewConfig(InitialPreviewConfig initialPreviewConfig) {
		if(this.get("initialPreviewConfig") == null) {
			this.put("initialPreviewConfig", new ArrayList());
		}
		((List)this.get("initialPreviewConfig")).add(initialPreviewConfig);
	}

	public void addInitialPreview(String fileName) {
		if (this.get("initialPreview") == null) {
			this.put("initialPreview", new ArrayList());
		}
		((List) this.get("initialPreview")).add("<div class='file-preview-text'>" + "<h2><i class='glyphicon glyphicon-file'></i></h2>" + fileName + "</div>");
	}

	public static class InitialPreviewConfig extends HashMap<String, Object> {

		public void setUrl(String url) {
			this.put("url", url);
		}

		public void setKey(long key) {
			this.put("key", key);
		}

		public void addExtra(String key, Object value) {
			if (this.get("extra") == null) {
				this.put("extra", new HashMap<String, Object>());
			}
			((Map) this.get("extra")).put(key, value);
		}
	}
}
