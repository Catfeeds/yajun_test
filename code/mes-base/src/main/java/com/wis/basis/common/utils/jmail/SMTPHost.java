package com.wis.basis.common.utils.jmail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;

public class SMTPHost {
	
	private Properties props = new Properties();
	private String hostIP;
	private String protocal;
	private int port;
	private String sslClass;
	private String sslFallback;
	private int sslPort;
	private String tlsEnable;
	private String auth;

	public String getHostIP() {
		return hostIP;
	}

	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}

	public String getProtocal() {
		return protocal;
	}

	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public String getSslClass() {
		return sslClass;
	}

	public void setSslClass(String sslClass) {
		this.sslClass = sslClass;
	}

	public String getSslFallback() {
		return sslFallback;
	}

	public void setSslFallback(String sslFallback) {
		this.sslFallback = sslFallback;
	}

	public int getSslPort() {
		return sslPort;
	}

	public void setSslPort(int sslPort) {
		this.sslPort = sslPort;
	}

	public String getTlsEnable() {
		return tlsEnable;
	}

	public void setTlsEnable(String tlsEnable) {
		this.tlsEnable = tlsEnable;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public SMTPHost(String hostIP, String protocal, int port, String auth) {
		this.hostIP = hostIP;
		this.protocal = protocal;
		this.port = port;
		this.auth = auth;
		initProps();
	}

	private void initProps() {
		props.put("mail.smtp.host", this.hostIP);
//		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
//		props.put("mail.smtp.socketFactory.fallback", "false"); 
		props.put("mail.transport.protocol", this.protocal); 
		props.put("mail.smtp.port", this.port); 
//		props.put("mail.smtp.socketFactory.port", this.port); 
		props.put("mail.smtp.auth",this.auth);
//		props.put("mail.smtp.starttls.enable","true");
	}

	public Session getSession(Authenticator authenticator) {
		return Session.getInstance(props, authenticator);
	}

	public Session getSession() {
		return Session.getInstance(props);
	}
}
