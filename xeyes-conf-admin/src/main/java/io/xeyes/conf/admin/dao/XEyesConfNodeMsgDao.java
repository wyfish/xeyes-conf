package io.xeyes.conf.admin.dao;

import io.xeyes.conf.admin.core.model.XEyesConfNodeMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface XEyesConfNodeMsgDao {

	public void add(XEyesConfNodeMsg xxlConfNode);

	public List<XEyesConfNodeMsg> findMsg(@Param("readedMsgIds") List<Integer> readedMsgIds);

	public int cleanMessage(@Param("messageTimeout") int messageTimeout);

}
