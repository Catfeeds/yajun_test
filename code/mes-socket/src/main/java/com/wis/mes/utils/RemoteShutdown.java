package com.wis.mes.utils;

import java.io.IOException;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

@Component("remoteShutdown")
@WebService
public class RemoteShutdown{

	private static final String cmdStart = "cmd.exe /c start ";
	// 执行命令的方法
	public String sendExeCmd(String command) {
		String message = "";
		Runtime ec = Runtime.getRuntime();
		try {
			//shutdown -r -t 60
			ec.exec(cmdStart+command);
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
			}
		} catch (IOException e) {
			message = e.getMessage();
			e.printStackTrace();
		}
		return message;
	}
}