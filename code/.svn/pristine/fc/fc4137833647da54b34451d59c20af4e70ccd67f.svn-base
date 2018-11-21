package com.wis.basis.notification.service.impl;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.notification.dao.MsgContentDao;
import com.wis.basis.notification.entity.MsgContent;
import com.wis.basis.notification.service.MsgContentService;
import com.wis.basis.notification.vo.MsgContentVo;
import com.wis.core.framework.entity.Attachment;
import com.wis.core.framework.service.AttachmentService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.service.impl.BaseServiceImpl;

@Service
public class MsgContentServiceImpl extends BaseServiceImpl<MsgContent> implements MsgContentService {

	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@Autowired
	private void setDao(MsgContentDao dao) {
		this.dao = dao;
	}

	@Override
	public MsgContent findById(Integer id) {
		MsgContent result = super.findById(id);
		List<Attachment> attachments = attachmentService.getByReferenceId("TR_MSG_ATTACHMENT", "MSG_ID", result.getId());
		result.setAttachments(new HashSet<Attachment>(attachments));
		return result;
	}

	@Override
	public void doAdd(MsgContentVo msgContentVo) {
		MsgContent msgContent = new MsgContent();
		BeanUtils.copyProperties(msgContentVo, msgContent);
		msgContent = doSave(msgContent);
		List<Attachment> attachments = attachmentService.doSave(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_ATTACHMENT_BASE_PATH), msgContentVo.getFile());
		for (Attachment attachment : attachments) {
			attachmentService.doSaveRelation("TR_MSG_ATTACHMENT", "ATTACHMENT_ID", attachment.getId(), "MSG_ID", msgContent.getId());
		}
	}
}
