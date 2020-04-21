package io.xeyes.conf.admin.controller.interceptor;

import io.xeyes.conf.admin.core.model.XEyesConfEnv;
import io.xeyes.conf.admin.core.util.CookieUtil;
import io.xeyes.conf.admin.dao.XEyesConfEnvDao;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * push cookies to model as cookieMap
 */
@Component
public class EnvInterceptor extends HandlerInterceptorAdapter {

	public static final String CURRENT_ENV = "XEYES_CONF_CURRENT_ENV";

	@Resource
	private XEyesConfEnvDao xEyesConfEnvDao;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// env list
		List<XEyesConfEnv> envList = xEyesConfEnvDao.findAll();
		if (envList==null || envList.size()==0) {
			throw new RuntimeException("系统异常，获取Env数据失败");
		}

		// current env
		String currentEnv = envList.get(0).getEnv();
		String currentEnvCookie = CookieUtil.getValue(request, CURRENT_ENV);
		if (currentEnvCookie!=null && currentEnvCookie.trim().length()>0) {
			for (XEyesConfEnv envItem: envList) {
				if (currentEnvCookie.equals(envItem.getEnv())) {
					currentEnv = envItem.getEnv();
				}
			}
		}

		request.setAttribute("envList", envList);
		request.setAttribute(CURRENT_ENV, currentEnv);

		return super.preHandle(request, response, handler);
	}
	
}
