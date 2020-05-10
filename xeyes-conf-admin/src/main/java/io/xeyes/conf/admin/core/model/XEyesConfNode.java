package io.xeyes.conf.admin.core.model;

import java.util.List;

/**
 * 配置节点
 * @author Netfish
 */
public class XEyesConfNode {

	private String env;
	// 配置Key
	private String key;
	// 所属项目AppName
	private String appname;
	// 配置描述
	private String title;
	// 配置Value
	private String value;

	// 配置变更Log
	private List<XEyesConfNodeLog> logList;

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<XEyesConfNodeLog> getLogList() {
		return logList;
	}

	public void setLogList(List<XEyesConfNodeLog> logList) {
		this.logList = logList;
	}
}
