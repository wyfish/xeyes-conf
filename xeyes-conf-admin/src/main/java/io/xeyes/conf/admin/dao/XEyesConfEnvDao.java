package io.xeyes.conf.admin.dao;

import io.xeyes.conf.admin.core.model.XEyesConfEnv;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface XEyesConfEnvDao {

    public List<XEyesConfEnv> findAll();

    public int save(XEyesConfEnv XEyesConfEnv);

    public int update(XEyesConfEnv XEyesConfEnv);

    public int delete(@Param("env") String env);

    public XEyesConfEnv load(@Param("env") String env);

}