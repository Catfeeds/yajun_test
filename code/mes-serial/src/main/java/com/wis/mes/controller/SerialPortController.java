package com.wis.mes.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/serialPort")
public class SerialPortController {

	@ResponseBody
	@RequestMapping(value = "/doClose")
	public String doClose() {
		String stopAndStartKepServer = stopAndStartKepServer("Tomcat8.exe");
		return stopAndStartKepServer;
	}

	private String stopAndStartKepServer(String string) {
		StringBuffer builder = new StringBuffer();
		try {
			// 调用 cmd命令，执行 net start mysql， /c 必须要有
			Process p = Runtime.getRuntime().exec("taskkill /f /im " + string);
			InputStream inputStream = p.getInputStream();
			// 获取命令执行完的结果
			Scanner scanner = new Scanner(inputStream, "GBK");
			scanner.useDelimiter("\\A");
			while (scanner.hasNext()) {
				builder.append(scanner.next());
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
