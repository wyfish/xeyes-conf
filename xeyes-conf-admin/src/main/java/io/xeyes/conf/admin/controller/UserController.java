package io.xeyes.conf.admin.controller;

import io.xeyes.conf.admin.controller.annotation.PermissionLimit;
import io.xeyes.conf.admin.core.model.XEyesConfEnv;
import io.xeyes.conf.admin.core.model.XEyesConfProject;
import io.xeyes.conf.admin.core.model.XEyesConfUser;
import io.xeyes.conf.admin.core.util.ReturnT;
import io.xeyes.conf.admin.dao.XEyesConfEnvDao;
import io.xeyes.conf.admin.dao.XEyesConfProjectDao;
import io.xeyes.conf.admin.dao.XEyesConfUserDao;
import io.xeyes.conf.admin.service.impl.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private XEyesConfUserDao XEyesConfUserDao;
    @Resource
    private XEyesConfProjectDao XEyesConfProjectDao;
    @Resource
    private XEyesConfEnvDao XEyesConfEnvDao;

    @RequestMapping("")
    @PermissionLimit(adminUser = true)
    public String index(Model model){

        List<XEyesConfProject> projectList = XEyesConfProjectDao.findAll();
        model.addAttribute("projectList", projectList);

        List<XEyesConfEnv> envList = XEyesConfEnvDao.findAll();
        model.addAttribute("envList", envList);

        return "user/user.index";
    }

    @RequestMapping("/pageList")
    @PermissionLimit(adminUser = true)
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String username,
                                        int permission) {

        // xxlConfNode in mysql
        List<XEyesConfUser> data = XEyesConfUserDao.pageList(start, length, username, permission);
        int list_count = XEyesConfUserDao.pageListCount(start, length, username, permission);

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("data", data);
        maps.put("recordsTotal", list_count);		// 总记录数
        maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
        return maps;
    }

    /**
     * add
     *
     * @return
     */
    @RequestMapping("/add")
    @PermissionLimit(adminUser = true)
    @ResponseBody
    public ReturnT<String> add(XEyesConfUser XEyesConfUser){

        // valid
        if (StringUtils.isBlank(XEyesConfUser.getUsername())){
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "用户名不可为空");
        }
        if (StringUtils.isBlank(XEyesConfUser.getPassword())){
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码不可为空");
        }
        if (!(XEyesConfUser.getPassword().length()>=4 && XEyesConfUser.getPassword().length()<=100)) {
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码长度限制为4~50");
        }

        // passowrd md5
        String md5Password = DigestUtils.md5DigestAsHex(XEyesConfUser.getPassword().getBytes());
        XEyesConfUser.setPassword(md5Password);

        int ret = XEyesConfUserDao.add(XEyesConfUser);
        return ret>0? ReturnT.SUCCESS: ReturnT.FAIL;
    }

    /**
     * delete
     *
     * @return
     */
    @RequestMapping("/delete")
    @PermissionLimit(adminUser = true)
    @ResponseBody
    public ReturnT<String> delete(HttpServletRequest request, String username){

        XEyesConfUser loginUser = (XEyesConfUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
        if (loginUser.getUsername().equals(username)) {
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "禁止操作当前登录账号");
        }

        /*List<XxlConfUser> adminList = xxlConfUserDao.pageList(0, 1 , null, 1);
        if (adminList.size()<2) {

        }*/

        XEyesConfUserDao.delete(username);
        return ReturnT.SUCCESS;
    }

    /**
     * update
     *
     * @return
     */
    @RequestMapping("/update")
    @PermissionLimit(adminUser = true)
    @ResponseBody
    public ReturnT<String> update(HttpServletRequest request, XEyesConfUser XEyesConfUser){

        XEyesConfUser loginUser = (XEyesConfUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
        if (loginUser.getUsername().equals(XEyesConfUser.getUsername())) {
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "禁止操作当前登录账号");
        }

        // valid
        if (StringUtils.isBlank(XEyesConfUser.getUsername())){
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "用户名不可为空");
        }

        XEyesConfUser existUser = XEyesConfUserDao.load(XEyesConfUser.getUsername());
        if (existUser == null) {
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "用户名非法");
        }

        if (StringUtils.isNotBlank(XEyesConfUser.getPassword())) {
            if (!(XEyesConfUser.getPassword().length()>=4 && XEyesConfUser.getPassword().length()<=50)) {
                return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码长度限制为4~50");
            }
            // passowrd md5
            String md5Password = DigestUtils.md5DigestAsHex(XEyesConfUser.getPassword().getBytes());
            existUser.setPassword(md5Password);
        }
        existUser.setPermission(XEyesConfUser.getPermission());

        int ret = XEyesConfUserDao.update(existUser);
        return ret>0? ReturnT.SUCCESS: ReturnT.FAIL;
    }

    @RequestMapping("/updatePermissionData")
    @PermissionLimit(adminUser = true)
    @ResponseBody
    public ReturnT<String> updatePermissionData(HttpServletRequest request,
                                                    String username,
                                                    @RequestParam(required = false) String[] permissionData){

        XEyesConfUser existUser = XEyesConfUserDao.load(username);
        if (existUser == null) {
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "参数非法");
        }

        String permissionDataArrStr = permissionData!=null?StringUtils.join(permissionData, ","):"";
        existUser.setPermissionData(permissionDataArrStr);
        XEyesConfUserDao.update(existUser);

        return ReturnT.SUCCESS;
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    public ReturnT<String> updatePwd(HttpServletRequest request, String password){

        // new password(md5)
        if (StringUtils.isBlank(password)){
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码不可为空");
        }
        if (!(password.length()>=4 && password.length()<=100)) {
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码长度限制为4~50");
        }
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

        // update pwd
        XEyesConfUser loginUser = (XEyesConfUser) request.getAttribute(LoginService.LOGIN_IDENTITY);

        XEyesConfUser existUser = XEyesConfUserDao.load(loginUser.getUsername());
        existUser.setPassword(md5Password);
        XEyesConfUserDao.update(existUser);

        return ReturnT.SUCCESS;
    }

}
