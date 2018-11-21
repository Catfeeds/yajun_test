package com.wis.basis.job.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.mail.MailService;
/**
 * 邮件JOB
 * @author Danny
 *
 */
@Component("mailJob")
public class MailJob  {
	
	@Autowired
	private MailService service;
	
	
	public void execute() {
		service.doSendMailAndUpdateFlag();
	}
}
