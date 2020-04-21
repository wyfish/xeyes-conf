package io.xeyes.conf.admin.dao;

import io.xeyes.conf.admin.core.model.XEyesConfNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface XEyesConfNodeDao {

	public List<XEyesConfNode> pageList(@Param("offset") int offset,
										@Param("pagesize") int pagesize,
										@Param("env") String env,
										@Param("appname") String appname,
										@Param("key") String key);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("env") String env,
							 @Param("appname") String appname,
							 @Param("key") String key);

	public int delete(@Param("env") String env, @Param("key") String key);

	public void insert(XEyesConfNode XEyesConfNode);

	public XEyesConfNode load(@Param("env") String env, @Param("key") String key);

	public int update(XEyesConfNode XEyesConfNode);
	
}
