package io.xeyes.conf.admin.dao;

import io.xeyes.conf.admin.core.model.XEyesConfProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface XEyesConfProjectDao {

    public List<XEyesConfProject> findAll();

    public int save(XEyesConfProject XEyesConfProject);

    public int update(XEyesConfProject XEyesConfProject);

    public int delete(@Param("appname") String appname);

    public XEyesConfProject load(@Param("appname") String appname);

}