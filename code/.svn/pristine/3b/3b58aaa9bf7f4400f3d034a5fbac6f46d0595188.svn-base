package com.wis.basis.mail.impl;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.sun.mail.smtp.SMTPAddressFailedException;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LogConstant;
import com.wis.basis.mail.MailService;
import com.wis.basis.mail.dao.MailDao;
import com.wis.basis.mail.entity.Mail;
import com.wis.basis.mail.entity.MailEntity;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;

import freemarker.template.Template;

@Service("mailService")
public class MailServiceImpl extends BaseServiceImpl<Mail> implements MailService  {
	@Autowired
	public void setDao(MailDao dao) {
		this.dao = dao;
	}
	
	private Logger log = Logger.getLogger(LogConstant.SYS_LOG);

	private JavaMailSenderImpl mailSender;
	private FreeMarkerConfigurer freeMarkerConfigurer = null;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	
	public boolean doSendMail(MailEntity mailEntity, Map<String, ?> messageContentMap, String templateOrContent) {
		try {
			((MailDao)dao).save(getMail(mailEntity, messageContentMap, templateOrContent));
			return true;
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	private Mail getMail(MailEntity mailEntity, Map<String, ?> messageContentMap, String templateOrContent) {
		Mail mail = new Mail();
		mail.setSubject(mailEntity.getSubject());
		if (mailEntity.getTo() != null && mailEntity.getTo().length > 0) {
			String to = Arrays.toString(mailEntity.getTo());
			mail.setTo(to.substring(1, to.length() - 1));
		}
		if (mailEntity.getCc() != null && mailEntity.getCc().length > 0) {
			String cc = Arrays.toString(mailEntity.getCc());
			mail.setCc(cc.substring(1, cc.length() - 1));
		}
		if (mailEntity.getAttachments() != null && mailEntity.getAttachments().length > 0) {
			StringBuffer filePaths = new StringBuffer();
			for (File attachment : mailEntity.getAttachments()) {
				if (attachment.exists()) {
					filePaths.append(attachment.getPath());
					filePaths.append(",");
				}
			}
			if (filePaths.length() > 0) {
				mail.setFiles(filePaths.substring(0, filePaths.length() - 1));
			}
		}
		if (mailEntity.isHtml()) {
			mail.setText(getMailText(templateOrContent, messageContentMap));
		} else {
			mail.setText(templateOrContent);
		}
		return mail;
	}

	private boolean sendMailNow(Mail mail, MimeMessage msg) {
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
			helper.setFrom(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_USER_NAME));
			helper.setSubject(mail.getSubject());
			if (null != mail.getTo()) {
				String[] to = mail.getTo().split(",");
				helper.setTo(to);
			}
			if (null != mail.getCc()) {
				String[] cc = mail.getCc().split(",");
				helper.setCc(cc);
			}
			if (null != mail.getFiles()) {
				String[] filePaths = mail.getFiles().split(",");
				for (String filePath : filePaths) {
					File file = new File(filePath);
					if (file.exists()) {
						helper.addAttachment(file.getName(), file);
					}
				}
			}
			helper.setText(mail.getText(), true);
			log.info("mail sended to :" + mail.getTo());
			mailSender.send(msg);
			return true;
		} catch (SMTPAddressFailedException e) {
			log.error("邮件发送失败因地址错误或者不存在");
			return false;
		} catch (Exception e) {
			log.error("邮件发送失败");
			log.error(ExceptionUtils.getStackTrace(e));
			return false;
		}

	}

	private void sendMail(Mail mail, MimeMessage msg) {
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
			helper.setFrom(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_USER_NAME));
			helper.setSubject(mail.getSubject());

			if (null != mail.getTo()) {
				String[] to = mail.getTo().split(",");
				helper.setTo(to);
			}
			if (null != mail.getCc()) {
				String[] cc = mail.getCc().split(",");
				helper.setCc(cc);
			}
			if (null != mail.getFiles()) {
				String[] filePaths = mail.getFiles().split(",");
				for (String filePath : filePaths) {
					File file = new File(filePath);
					if (file.exists()) {
						helper.addAttachment(file.getName(), file);
					}
				}
			}
			helper.setText(mail.getText(), true);
			log.info("mail sended to :" + mail.getTo());
			mailSender.send(msg);
		} catch (SMTPAddressFailedException e) {
			log.error("邮件发送失败因地址错误或者不存在");
		} catch (Exception e) {
			log.error("邮件发送失败");
			log.error(ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public boolean doSendMailNow(MailEntity mailEntity, Map<String, ?> messageContentMap, String templateOrContent) {
		try {
			Mail mail = getMail(mailEntity, messageContentMap, templateOrContent);
			mailSender.setHost(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_HOST));
			mailSender.setUsername(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_USER_NAME));
			mailSender.setPassword(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_PASSWORD));
			boolean sendSuccess = sendMailNow(mail, mailSender.createMimeMessage());
			if (!sendSuccess) {
				((MailDao)dao).save(mail);
			}
			return true;
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	public void doSendMail() {
		List<Mail> mails = ((MailDao)dao).findAll();
		mailSender.setHost(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_HOST));
		mailSender.setUsername(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_USER_NAME));
		mailSender.setPassword(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_PASSWORD));
		MimeMessage msg = mailSender.createMimeMessage();
		for (Mail mail : mails) {
			sendMail(mail, msg);
			((MailDao)dao).delete(mail);
		}
	}

	/**
	 * 将值设定入邮件模版中
	 * 
	 * @param mailTemplate
	 * @param map
	 * @return 邮件内容
	 */
	private String getMailText(String mailTemplate, Map<String, ?> map) {
		String htmlText = "";
		try {
			Template tpl = freeMarkerConfigurer.getConfiguration().getTemplate(mailTemplate);
			htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, map);
		} catch (Exception e) {
			String errorMsg = "turn mail text failure";
			log.error(errorMsg, e);
			throw new BusinessException(errorMsg);
		}
		return htmlText;
	}

	public JavaMailSenderImpl getMailSender() {
		mailSender.setHost(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_HOST));
		mailSender.setUsername(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_USER_NAME));
		mailSender.setPassword(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_PASSWORD));
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public FreeMarkerConfigurer getFreeMarkerConfigurer() {
		return freeMarkerConfigurer;
	}

	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.freeMarkerConfigurer = freeMarkerConfigurer;
	}

	@Override
	public boolean doSendMail(String[] notice, String mailsub, String subject, String definitionName, String taskName, String taskDesc) {
		if (notice != null && notice.length > 0) {
			MailEntity mailEntity = new MailEntity();
			mailEntity.setSubject(mailsub);
			mailEntity.setTo(notice);
			Map<String, Object> messageContentMap = new HashMap<String, Object>();
			messageContentMap.put("subject", subject);
			messageContentMap.put("definitionName", definitionName);
			messageContentMap.put("taskName", taskName);
			messageContentMap.put("taskDesc", taskDesc);
			return doSendMailNow(mailEntity, messageContentMap, "/mail_template/formProcess.html");
		}
		return false;
	}
	
	//定时发送邮件,如果发送失败则将FLAG更新为发送状态(1为已发送状态，0为未发送状态)
	@Override
	public void doSendMailAndUpdateFlag() {
		Mail mailFlag = new Mail();
		mailFlag.setFlag("0");
		List<Mail> mails = ((MailDao)dao).findByEg(mailFlag);
		mailSender.setHost(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_HOST));
		mailSender.setUsername(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_USER_NAME));
		mailSender.setPassword(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_PASSWORD));
		MimeMessage msg = mailSender.createMimeMessage();
		for (Mail mail : mails) {
			try {
				MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
				helper.setFrom(globalConfigurationService.getValueByKey(ConstantUtils.SYS_CONFIG_SMTP_USER_NAME));
				helper.setSubject(mail.getSubject());

				if (null != mail.getTo()) {
					String[] to = mail.getTo().split(",");
					helper.setTo(to);
				}
				if (null != mail.getCc()) {
					String[] cc = mail.getCc().split(",");
					helper.setCc(cc);
				}
				if (null != mail.getFiles()) {
					String[] filePaths = mail.getFiles().split(",");
					for (String filePath : filePaths) {
						File file = new File(filePath);
						if (file.exists()) {
							helper.addAttachment(file.getName(), file);
						}
					}
				}
				helper.setText(mail.getText(), true);
				log.info("mail sended to :" + mail.getTo());
				mailSender.send(msg);
				mail.setFlag("1");//发送成功标记为FLAG为"1"(已发送状态)
				((MailDao)dao).update(mail);
			} catch (SMTPAddressFailedException e) {
				log.error("邮件发送失败因地址错误或者不存在");
			}catch (Exception e) {
				log.error("邮件发送失败");
				log.error(ExceptionUtils.getStackTrace(e));
			}

		}
	}

}
