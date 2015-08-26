package com.virtualspace.service.register.imp;

import java.util.Date;

import org.springframework.util.StringUtils;

import com.here.framework.dao.UIDGenerator;
import com.here.framework.service.BaseService;
import com.here.framework.service.ServiceException;
import com.virtualspace.database.model.User;
import com.virtualspace.redis.service.RedisKVCacheService;
import com.virtualspace.service.common.UserService;
import com.virtualspace.service.login.LoginService;
import com.virtualspace.service.register.RegisterService;
import com.virtualspace.util.Md5Util;

public class RegisterServiceImp extends BaseService implements RegisterService
{
	@Override
	public User registerUserByTelephone(String telephone, String password, String checkCode) throws ServiceException 
	{
		if(StringUtils.isEmpty(telephone) || StringUtils.isEmpty(password) || StringUtils.isEmpty(checkCode))
		{
			return null;
		}
		User user = new User();
		user.setPk(UIDGenerator.getInstance().generate());
		user.setId(telephone);
		user.setPassword(Md5Util.getMD5Str(password));
		user.setNickName(telephone);
		user.setTelephone(telephone);
		user.setCreateTime(new Date(System.currentTimeMillis()));
		user.setLastModifyTime(new Date(System.currentTimeMillis()));
		
		UserService userService = this.getService(UserService.class);
		if (userService.insert(user)) 
		{
			LoginService loginService = this.getService(LoginService.class);
			User loginUser = loginService.loginByTelephone(telephone, password);
			return loginUser;
		}
		
		return null;
	}

	@Override
	public User registerUserByEmail(String email, String password) throws ServiceException 
	{
		if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password))
		{
			return null;
		}
		
		User user = new User();
		user.setPk(UIDGenerator.getInstance().generate());
		user.setId(email);
		user.setPassword(Md5Util.getMD5Str(password));
		user.setNickName(email);
		user.setCreateTime(new Date(System.currentTimeMillis()));
		user.setLastModifyTime(new Date(System.currentTimeMillis()));
		UserService userService = this.getService(UserService.class);
		if (userService.insert(user)) 
		{
			LoginService loginService = this.getService(LoginService.class);
			User loginUser = loginService.loginByEmail(email, password);
			return loginUser;
		}
		
		return null;
	}

}
