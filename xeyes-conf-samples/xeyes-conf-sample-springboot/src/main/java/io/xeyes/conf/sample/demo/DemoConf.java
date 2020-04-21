package io.xeyes.conf.sample.demo;

import io.xeyes.conf.core.annotation.XEyesConf;
import org.springframework.stereotype.Component;

/**
 *  测试示例（可删除）
 *
 */
@Component
public class DemoConf {

	@XEyesConf("default.key02")
	public String paramByAnno;

	public String paramByXml = "XML方式配置，请前往 (xeyes-conf-sample-spring) 项目参考查看，springboot项目不推荐采用该方式";

	public void setParamByXml(String paramByXml) {
		this.paramByXml = paramByXml;
	}

}
