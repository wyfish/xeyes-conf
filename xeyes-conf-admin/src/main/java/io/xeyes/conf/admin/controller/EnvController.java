package io.xeyes.conf.admin.controller;

import io.xeyes.conf.admin.controller.annotation.PermissionLimit;
import io.xeyes.conf.admin.core.model.XEyesConfEnv;
import io.xeyes.conf.admin.core.util.ReturnT;
import io.xeyes.conf.admin.dao.XEyesConfEnvDao;
import io.xeyes.conf.admin.dao.XEyesConfNodeDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 环境管理
 *
 */
@Controller
@RequestMapping("/env")
public class EnvController {
	
	@Resource
	private XEyesConfEnvDao XEyesConfEnvDao;
    @Resource
    private XEyesConfNodeDao XEyesConfNodeDao;


	@RequestMapping
	@PermissionLimit(adminUser = true)
	public String index(Model model) {

		List<XEyesConfEnv> list = XEyesConfEnvDao.findAll();
		model.addAttribute("list", list);

		return "env/env.index";
	}

	@RequestMapping("/save")
	@PermissionLimit(adminUser = true)
	@ResponseBody
	public ReturnT<String> save(XEyesConfEnv XEyesConfEnv){

		// valid
		if (StringUtils.isBlank(XEyesConfEnv.getEnv())) {
			return new ReturnT<String>(500, "Env不可为空");
		}
		if (XEyesConfEnv.getEnv().length()<3 || XEyesConfEnv.getEnv().length()>50) {
			return new ReturnT<String>(500, "Env长度限制为4~50");
		}
		if (StringUtils.isBlank(XEyesConfEnv.getTitle())) {
			return new ReturnT<String>(500, "请输入Env名称");
		}

		// valid repeat
		XEyesConfEnv existEnv = XEyesConfEnvDao.load(XEyesConfEnv.getEnv());
		if (existEnv != null) {
			return new ReturnT<String>(500, "Env已存在，请勿重复添加");
		}

		int ret = XEyesConfEnvDao.save(XEyesConfEnv);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@PermissionLimit(adminUser = true)
	@ResponseBody
	public ReturnT<String> update(XEyesConfEnv XEyesConfEnv){

		// valid
		if (StringUtils.isBlank(XEyesConfEnv.getEnv())) {
			return new ReturnT<String>(500, "Env不可为空");
		}
		if (StringUtils.isBlank(XEyesConfEnv.getTitle())) {
			return new ReturnT<String>(500, "请输入Env名称");
		}

		int ret = XEyesConfEnvDao.update(XEyesConfEnv);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/remove")
	@PermissionLimit(adminUser = true)
	@ResponseBody
	public ReturnT<String> remove(String env){

		if (StringUtils.isBlank(env)) {
			return new ReturnT<String>(500, "参数Env非法");
		}

        // valid
        int list_count = XEyesConfNodeDao.pageListCount(0, 10, env, null, null);
        if (list_count > 0) {
            return new ReturnT<String>(500, "拒绝删除，该Env下存在配置数据");
        }

		// valid can not be empty
		List<XEyesConfEnv> allList = XEyesConfEnvDao.findAll();
		if (allList.size() == 1) {
			return new ReturnT<String>(500, "拒绝删除, 需要至少预留一个Env");
		}

		int ret = XEyesConfEnvDao.delete(env);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
