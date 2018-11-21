package com.wis.basis.mail;

import java.util.Map;

import com.wis.basis.mail.entity.Mail;
import com.wis.basis.mail.entity.MailEntity;
import com.wis.core.service.BaseService;

public interface MailService extends BaseService<Mail>{

	public boolean doSendMail(MailEntity mailEntity, Map<String, ?> messageContentMap, String templateOrContent);

	public boolean doSendMailNow(MailEntity mailEntity, Map<String, ?> messageContentMap, String templateOrContent);

	public void doSendMail();

	public boolean doSendMail(String[] notice, String mailsub ,String subject,String definitionName,String taskName,String taskDesc);

	public void doSendMailAndUpdateFlag();
}
