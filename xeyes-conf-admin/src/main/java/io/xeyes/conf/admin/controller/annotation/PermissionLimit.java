package io.xeyes.conf.admin.controller.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限限制
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionLimit {
	
	/**
	 * 要求用户登录
	 *
	 * @return
	 */
	boolean limit() default true;

	/**
	 * 要求管理员权限
	 *
	 * @return
	 */
	boolean adminUser() default false;

}