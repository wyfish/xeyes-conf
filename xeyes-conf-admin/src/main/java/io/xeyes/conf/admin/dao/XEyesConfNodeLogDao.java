package io.xeyes.conf.admin.dao;

import io.xeyes.conf.admin.core.model.XEyesConfNodeLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface XEyesConfNodeLogDao {

	public List<XEyesConfNodeLog> findByKey(@Param("env") String env, @Param("key") String key);

	public void add(XEyesConfNodeLog xxlConfNode);

	public int deleteTimeout(@Param("env") String env,
							 @Param("key") String key,
							 @Param("length") int length);

}
