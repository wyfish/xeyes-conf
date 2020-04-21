package io.xeyes.conf.core.core;

import io.xeyes.conf.core.exception.XEyesConfException;
import io.xeyes.conf.core.model.XEyesConfParamVO;
import io.xeyes.conf.core.util.BasicHttpUtil;
import io.xeyes.conf.core.util.json.BasicJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * XEyes 配置中心的远端配置
 */
public class XEyesConfRemoteConf {
    private static Logger logger = LoggerFactory.getLogger(XEyesConfRemoteConf.class);

    private static String adminAddress;
    private static String env;
    private static String accessToken;

    private static List<String> adminAddressArr = null;

    public static void init(String adminAddress, String env, String accessToken) {

        // valid
        if (adminAddress==null || adminAddress.trim().length()==0) {
            throw new XEyesConfException("xeyes-conf adminAddress can not be empty");
        }
        if (env==null || env.trim().length()==0) {
            throw new XEyesConfException("xeyes-conf env can not be empty");
        }

        XEyesConfRemoteConf.adminAddress = adminAddress;
        XEyesConfRemoteConf.env = env;
        XEyesConfRemoteConf.accessToken = accessToken;

        // parse
        XEyesConfRemoteConf.adminAddressArr = new ArrayList<>();
        if (adminAddress.contains(",")) {
            XEyesConfRemoteConf.adminAddressArr.add(adminAddress);
        } else {
            XEyesConfRemoteConf.adminAddressArr.addAll(Arrays.asList(adminAddress.split(",")));
        }

    }

    /**
     * get and valid
     *
     * @param url
     * @param requestBody
     * @param timeout
     * @return
     */
    private static Map<String, Object> getAndValid(String url, String requestBody, int timeout){

        // resp json
        String respJson = BasicHttpUtil.postBody(url, requestBody, timeout);
        if (respJson == null) {
            return null;
        }

        // parse obj
        Map<String, Object> respObj = BasicJson.parseMap(respJson);
        int code = Integer.valueOf(String.valueOf(respObj.get("code")));
        if (code != 200) {
            logger.info("request fail, msg={}", (respObj.containsKey("msg")?respObj.get("msg"):respJson) );
            return null;
        }
        return respObj;
    }


    /**
     * 获取配置
     *
     * @param keys
     * @return
     */
    public static Map<String, String> find(Set<String> keys) {
        for (String adminAddressUrl: XEyesConfRemoteConf.adminAddressArr) {

            // url + param
            String url = adminAddressUrl + "/conf/find";

            XEyesConfParamVO paramVO = new XEyesConfParamVO();
            paramVO.setAccessToken(accessToken);
            paramVO.setEnv(env);
            paramVO.setKeys(new ArrayList<String>(keys));

            String paramsJson = BasicJson.toJson(paramVO);

            // get and valid
            Map<String, Object> respObj = getAndValid(url, paramsJson, 5);

            // parse
            if (respObj!=null && respObj.containsKey("data")) {
                Map<String, String> data = (Map<String, String>) respObj.get("data");
                return data;
            }
        }

        return null;
    }

    /**
     * 猎取特定属性的配置
     * @param key
     * @return
     */
    public static String find(String key) {
        Map<String, String> result = find(new HashSet<String>(Arrays.asList(key)));
        if (result!=null) {
            return result.get(key);
        }
        return null;
    }


    /**
     * 监控配置是否发生变化
     *
     * @param keys
     * @return
     */
    public static boolean monitor(Set<String> keys) {

        for (String adminAddressUrl: XEyesConfRemoteConf.adminAddressArr) {

            // url + param
            String url = adminAddressUrl + "/conf/monitor";

            XEyesConfParamVO paramVO = new XEyesConfParamVO();
            paramVO.setAccessToken(accessToken);
            paramVO.setEnv(env);
            paramVO.setKeys(new ArrayList<String>(keys));

            String paramsJson = BasicJson.toJson(paramVO);

            // get and valid
            Map<String, Object> respObj = getAndValid(url, paramsJson, 60);

            return respObj!=null?true:false;
        }
        return false;
    }

}
