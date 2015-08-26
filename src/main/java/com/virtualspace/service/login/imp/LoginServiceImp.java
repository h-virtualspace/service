package com.virtualspace.service.login.imp;

import com.here.framework.service.BaseService;
import com.here.framework.service.ServiceException;
import com.virtualspace.database.dao.UserMapper;
import com.virtualspace.database.model.User;
import com.virtualspace.service.login.LoginService;
import com.virtualspace.util.Md5Util;

public class LoginServiceImp extends BaseService implements LoginService
{
	@Override
	public User loginByTelephone(String telephone, String password) throws ServiceException 
	{
		UserMapper userMapper = this.getMapper(UserMapper.class);
		User result = userMapper.loginByTelephone(telephone, Md5Util.getMD5Str(password));
		if (result != null && result.getTelephone().equals(telephone)) {
			return result;
		}
		return null;
	}

	@Override
	public User loginByEmail(String email, String password) throws ServiceException 
	{
		UserMapper userMapper = this.getMapper(UserMapper.class);
		User result = userMapper.loginByTelephone(email, Md5Util.getMD5Str(password));
		if (result.getEmail().equals(email)) {
			return result;
		}
		return null;
	}

}
