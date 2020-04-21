package io.xeyes.conf.admin.controller;

import io.xeyes.conf.admin.controller.annotation.PermissionLimit;
import io.xeyes.conf.admin.core.model.XEyesConfNode;
import io.xeyes.conf.admin.core.model.XEyesConfProject;
import io.xeyes.conf.admin.core.model.XEyesConfUser;
import io.xeyes.conf.admin.core.util.JacksonUtil;
import io.xeyes.conf.admin.core.util.ReturnT;
import io.xeyes.conf.admin.dao.XEyesConfProjectDao;
import io.xeyes.conf.admin.service.IXEyesConfNodeService;
import io.xeyes.conf.admin.service.impl.LoginService;
import io.xeyes.conf.admin.controller.interceptor.EnvInterceptor;
import io.xeyes.conf.core.model.XEyesConfParamVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 配置管理
 *
 * @author xuxueli
 */
@Controller
@RequestMapping("/conf")
public class ConfController {

	@Resource
	private XEyesConfProjectDao XEyesConfProjectDao;
	@Resource
	private IXEyesConfNodeService ixEyesConfNodeService;

	@RequestMapping("")
	public String index(HttpServletRequest request, Model model, String appname){

		List<XEyesConfProject> list = XEyesConfProjectDao.findAll();
		if (list==null || list.size()==0) {
			throw new RuntimeException("系统异常，无可用项目");
		}

		XEyesConfProject project = list.get(0);
		for (XEyesConfProject item: list) {
			if (item.getAppname().equals(appname)) {
				project = item;
			}
		}

		boolean ifHasProjectPermission = ixEyesConfNodeService.ifHasProjectPermission(
				(XEyesConfUser) request.getAttribute(LoginService.LOGIN_IDENTITY),
				(String) request.getAttribute(EnvInterceptor.CURRENT_ENV),
				project.getAppname());

		model.addAttribute("ProjectList", list);
		model.addAttribute("project", project);
		model.addAttribute("ifHasProjectPermission", ifHasProjectPermission);

		return "conf/conf.index";
	}

	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(HttpServletRequest request,
										@RequestParam(required = false, defaultValue = "0") int start,
										@RequestParam(required = false, defaultValue = "10") int length,
										String appname,
										String key) {

		XEyesConfUser XEyesConfUser = (XEyesConfUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
		String loginEnv = (String) request.getAttribute(EnvInterceptor.CURRENT_ENV);

		return ixEyesConfNodeService.pageList(start, length, appname, key, XEyesConfUser, loginEnv);
	}

	/**
	 * get
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(HttpServletRequest request, String key){

		XEyesConfUser XEyesConfUser = (XEyesConfUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
		String loginEnv = (String) request.getAttribute(EnvInterceptor.CURRENT_ENV);

		return ixEyesConfNodeService.delete(key, XEyesConfUser, loginEnv);
	}

	/**
	 * create/update
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(HttpServletRequest request, XEyesConfNode XEyesConfNode){

		XEyesConfUser XEyesConfUser = (XEyesConfUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
		String loginEnv = (String) request.getAttribute(EnvInterceptor.CURRENT_ENV);

		// fill env
		XEyesConfNode.setEnv(loginEnv);

		return ixEyesConfNodeService.add(XEyesConfNode, XEyesConfUser, loginEnv);
	}
	
	/**
	 * create/update
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(HttpServletRequest request, XEyesConfNode XEyesConfNode){

		XEyesConfUser XEyesConfUser = (XEyesConfUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
		String loginEnv = (String) request.getAttribute(EnvInterceptor.CURRENT_ENV);

		// fill env
		XEyesConfNode.setEnv(loginEnv);

		return ixEyesConfNodeService.update(XEyesConfNode, XEyesConfUser, loginEnv);
	}

	/*@RequestMapping("/syncConf")
	@ResponseBody
	public ReturnT<String> syncConf(HttpServletRequest request,
										String appname) {

		XxlConfUser xxlConfUser = (XxlConfUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
		String loginEnv = (String) request.getAttribute(CURRENT_ENV);

		return xxlConfNodeService.syncConf(appname, xxlConfUser, loginEnv);
	}*/


	// ---------------------- rest api ----------------------

    @Value("${xxl.conf.access.token}")
    private String accessToken;


	/**
	 * 配置查询 API
	 *
	 * 说明：查询配置数据；
	 *
	 * ------
	 * 地址格式：{配置中心跟地址}/find
	 *
	 * 请求参数说明：
	 *  1、accessToken：请求令牌；
	 *  2、env：环境标识
	 *  3、keys：配置Key列表
	 *
	 * 请求数据格式如下，放置在 RequestBody 中，JSON格式：
	 *
	 *     {
	 *         "accessToken" : "xx",
	 *         "env" : "xx",
	 *         "keys" : [
	 *             "key01",
	 *             "key02"
	 *         ]
	 *     }
	 *
	 * @param data
	 * @return
	 */
	@RequestMapping("/find")
	@ResponseBody
	@PermissionLimit(limit = false)
	public ReturnT<Map<String, String>> find(@RequestBody(required = false) String data){

		// parse data
		XEyesConfParamVO confParamVO = null;
		try {
			confParamVO = (XEyesConfParamVO) JacksonUtil.readValue(data, XEyesConfParamVO.class);
		} catch (Exception e) { }

		// parse param
		String accessToken = null;
		String env = null;
		List<String> keys = null;
		if (confParamVO != null) {
			accessToken = confParamVO.getAccessToken();
			env = confParamVO.getEnv();
			keys = confParamVO.getKeys();
		}

		return ixEyesConfNodeService.find(accessToken, env, keys);
	}

	/**
	 * 配置监控 API
	 *
	 * 说明：long-polling 接口，主动阻塞一段时间（默认30s）；直至阻塞超时或配置信息变动时响应；
	 *
	 * ------
	 * 地址格式：{配置中心跟地址}/monitor
	 *
	 * 请求参数说明：
	 *  1、accessToken：请求令牌；
	 *  2、env：环境标识
	 *  3、keys：配置Key列表
	 *
	 * 请求数据格式如下，放置在 RequestBody 中，JSON格式：
	 *
	 *     {
	 *         "accessToken" : "xx",
	 *         "env" : "xx",
	 *         "keys" : [
	 *             "key01",
	 *             "key02"
	 *         ]
	 *     }
	 *
	 * @param data
	 * @return
	 */
	@RequestMapping("/monitor")
	@ResponseBody
	@PermissionLimit(limit = false)
	public DeferredResult<ReturnT<String>> monitor(@RequestBody(required = false) String data){

		// parse data
		XEyesConfParamVO confParamVO = null;
		try {
			confParamVO = (XEyesConfParamVO) JacksonUtil.readValue(data, XEyesConfParamVO.class);
		} catch (Exception e) { }

		// parse param
		String accessToken = null;
		String env = null;
		List<String> keys = null;
		if (confParamVO != null) {
			accessToken = confParamVO.getAccessToken();
			env = confParamVO.getEnv();
			keys = confParamVO.getKeys();
		}

		return ixEyesConfNodeService.monitor(accessToken, env, keys);
	}


}
