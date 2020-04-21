package io.xeyes.conf.admin.service.impl;

import io.xeyes.conf.admin.core.model.XEyesConfUser;
import io.xeyes.conf.admin.core.util.CookieUtil;
import io.xeyes.conf.admin.core.util.JacksonUtil;
import io.xeyes.conf.admin.core.util.ReturnT;
import io.xeyes.conf.admin.dao.XEyesConfUserDao;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * Login Service
 *
 */
@Configuration
public class LoginService {

    public static final String LOGIN_IDENTITY = "XEYES_CONF_LOGIN_IDENTITY";

    @Resource
    private XEyesConfUserDao XEyesConfUserDao;

    private String makeToken(XEyesConfUser XEyesConfUser){
        String tokenJson = JacksonUtil.writeValueAsString(XEyesConfUser);
        String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        return tokenHex;
    }
    private XEyesConfUser parseToken(String tokenHex){
        XEyesConfUser XEyesConfUser = null;
        if (tokenHex != null) {
            String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray());      // username_password(md5)
            XEyesConfUser = JacksonUtil.readValue(tokenJson, XEyesConfUser.class);
        }
        return XEyesConfUser;
    }

    /**
     * login
     *
     * @param response
     * @param usernameParam
     * @param passwordParam
     * @param ifRemember
     * @return
     */
    public ReturnT<String> login(HttpServletResponse response, String usernameParam, String passwordParam, boolean ifRemember){

        XEyesConfUser XEyesConfUser = XEyesConfUserDao.load(usernameParam);
        if (XEyesConfUser == null) {
            return new ReturnT<String>(500, "账号或密码错误");
        }

        String passwordParamMd5 = DigestUtils.md5DigestAsHex(passwordParam.getBytes());
        if (!XEyesConfUser.getPassword().equals(passwordParamMd5)) {
            return new ReturnT<String>(500, "账号或密码错误");
        }

        String loginToken = makeToken(XEyesConfUser);

        // do login
        CookieUtil.set(response, LOGIN_IDENTITY, loginToken, ifRemember);
        return ReturnT.SUCCESS;
    }

    /**
     * logout
     *
     * @param request
     * @param response
     */
    public void logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.remove(request, response, LOGIN_IDENTITY);
    }

    /**
     * logout
     *
     * @param request
     * @return
     */
    public XEyesConfUser ifLogin(HttpServletRequest request){
        String cookieToken = CookieUtil.getValue(request, LOGIN_IDENTITY);
        if (cookieToken != null) {
            XEyesConfUser cookieUser = parseToken(cookieToken);
            if (cookieUser != null) {
                XEyesConfUser dbUser = XEyesConfUserDao.load(cookieUser.getUsername());
                if (dbUser != null) {
                    if (cookieUser.getPassword().equals(dbUser.getPassword())) {
                        return dbUser;
                    }
                }
            }
        }
        return null;
    }

}
