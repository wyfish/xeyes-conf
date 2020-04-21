package io.xeyes.conf.admin.controller;

import io.xeyes.conf.admin.controller.annotation.PermissionLimit;
import io.xeyes.conf.admin.core.model.XEyesConfProject;
import io.xeyes.conf.admin.core.util.ReturnT;
import io.xeyes.conf.admin.dao.XEyesConfNodeDao;
import io.xeyes.conf.admin.dao.XEyesConfProjectDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目管理
 *
 */
@Controller
@RequestMapping("/project")
public class ProjectController {
	
	@Resource
	private XEyesConfProjectDao XEyesConfProjectDao;
	@Resource
	private XEyesConfNodeDao xxlConfNodeDao;

	@RequestMapping
	@PermissionLimit(adminUser = true)
	public String index(Model model) {

		List<XEyesConfProject> list = XEyesConfProjectDao.findAll();
		model.addAttribute("list", list);

		return "project/project.index";
	}

	@RequestMapping("/save")
	@PermissionLimit(adminUser = true)
	@ResponseBody
	public ReturnT<String> save(XEyesConfProject XEyesConfProject){

		// valid
		if (StringUtils.isBlank(XEyesConfProject.getAppname())) {
			return new ReturnT<String>(500, "AppName不可为空");
		}
		if (XEyesConfProject.getAppname().length()<4 || XEyesConfProject.getAppname().length()>100) {
			return new ReturnT<String>(500, "Appname长度限制为4~100");
		}
		if (StringUtils.isBlank(XEyesConfProject.getTitle())) {
			return new ReturnT<String>(500, "请输入项目名称");
		}

		// valid repeat
		XEyesConfProject existProject = XEyesConfProjectDao.load(XEyesConfProject.getAppname());
		if (existProject != null) {
			return new ReturnT<String>(500, "Appname已存在，请勿重复添加");
		}

		int ret = XEyesConfProjectDao.save(XEyesConfProject);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@PermissionLimit(adminUser = true)
	@ResponseBody
	public ReturnT<String> update(XEyesConfProject XEyesConfProject){

		// valid
		if (StringUtils.isBlank(XEyesConfProject.getAppname())) {
			return new ReturnT<String>(500, "AppName不可为空");
		}
		if (StringUtils.isBlank(XEyesConfProject.getTitle())) {
			return new ReturnT<String>(500, "请输入项目名称");
		}

		int ret = XEyesConfProjectDao.update(XEyesConfProject);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/remove")
	@PermissionLimit(adminUser = true)
	@ResponseBody
	public ReturnT<String> remove(String appname){

		if (StringUtils.isBlank(appname)) {
			return new ReturnT<String>(500, "参数AppName非法");
		}

		// valid
		int list_count = xxlConfNodeDao.pageListCount(0, 10, null, appname, null);
		if (list_count > 0) {
			return new ReturnT<String>(500, "拒绝删除，该项目下存在配置数据");
		}

		List<XEyesConfProject> allList = XEyesConfProjectDao.findAll();
		if (allList.size() == 1) {
			return new ReturnT<String>(500, "拒绝删除, 需要至少预留一个项目");
		}

		int ret = XEyesConfProjectDao.delete(appname);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
