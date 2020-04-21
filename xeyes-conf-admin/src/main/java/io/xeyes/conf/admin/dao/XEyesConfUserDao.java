package io.xeyes.conf.admin.dao;

import io.xeyes.conf.admin.core.model.XEyesConfUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface XEyesConfUserDao {

    public List<XEyesConfUser> pageList(@Param("offset") int offset,
                                        @Param("pagesize") int pagesize,
                                        @Param("username") String username,
                                        @Param("permission") int permission);
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("username") String username,
                             @Param("permission") int permission);

    public int add(XEyesConfUser XEyesConfUser);

    public int update(XEyesConfUser XEyesConfUser);

    public int delete(@Param("username") String username);

    public XEyesConfUser load(@Param("username") String username);

}
