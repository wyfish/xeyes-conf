package io.xeyes.conf.admin.service;


import io.xeyes.conf.admin.core.model.XEyesConfNode;
import io.xeyes.conf.admin.core.model.XEyesConfUser;
import io.xeyes.conf.admin.core.util.ReturnT;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface IXEyesConfNodeService {

	public boolean ifHasProjectPermission(XEyesConfUser loginUser, String loginEnv, String appName);

	public Map<String,Object> pageList(int offset,
									   int pageSize,
									   String appName,
									   String key,
									   XEyesConfUser loginUser,
									   String loginEnv);

	public ReturnT<String> delete(String key, XEyesConfUser loginUser, String loginEnv);

	public ReturnT<String> add(XEyesConfNode XEyesConfNode, XEyesConfUser loginUser, String loginEnv);

	public ReturnT<String> update(XEyesConfNode XEyesConfNode, XEyesConfUser loginUser, String loginEnv);

    /*ReturnT<String> syncConf(String appname, XxlConfUser loginUser, String loginEnv);*/


    // ---------------------- rest api ----------------------

    public ReturnT<Map<String, String>> find(String accessToken, String env, List<String> keys);

    public DeferredResult<ReturnT<String>> monitor(String accessToken, String env, List<String> keys);

}
