package io.xeyes.conf.admin.service;


import io.xeyes.conf.admin.core.model.XEyesConfNode;
import io.xeyes.conf.admin.core.model.XEyesConfUser;
import io.xeyes.conf.admin.core.util.ReturnT;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Netfish
 */
public interface IXEyesConfNodeService {

	/**
	 *
	 * @param loginUser
	 * @param loginEnv
	 * @param appName
	 * @return
	 */
	public boolean ifHasProjectPermission(XEyesConfUser loginUser, String loginEnv, String appName);

	/**
	 *
	 * @param offset
	 * @param pageSize
	 * @param appName
	 * @param key
	 * @param loginUser
	 * @param loginEnv
	 * @return
	 */
	public Map<String,Object> pageList(int offset,
									   int pageSize,
									   String appName,
									   String key,
									   XEyesConfUser loginUser,
									   String loginEnv);

	/**
	 *
	 * @param key
	 * @param loginUser
	 * @param loginEnv
	 * @return
	 */
	public ReturnT<String> delete(String key, XEyesConfUser loginUser, String loginEnv);

	/**
	 *
	 * @param xEyesConfNode
	 * @param loginUser
	 * @param loginEnv
	 * @return
	 */
	public ReturnT<String> add(XEyesConfNode xEyesConfNode, XEyesConfUser loginUser, String loginEnv);

	/**
	 *
	 * @param xEyesConfNode
	 * @param loginUser
	 * @param loginEnv
	 * @return
	 */
	public ReturnT<String> update(XEyesConfNode xEyesConfNode, XEyesConfUser loginUser, String loginEnv);

	/**
	 *
	 * @param accessToken
	 * @param env
	 * @param keys
	 * @return
	 */
    public ReturnT<Map<String, String>> find(String accessToken, String env, List<String> keys);

	/**
	 *
	 * @param accessToken
	 * @param env
	 * @param keys
	 * @return
	 */
    public DeferredResult<ReturnT<String>> monitor(String accessToken, String env, List<String> keys);

}
