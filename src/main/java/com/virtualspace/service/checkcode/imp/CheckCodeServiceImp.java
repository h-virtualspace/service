package com.virtualspace.service.checkcode.imp;

import com.here.framework.service.BaseService;
import com.here.framework.service.ServiceException;
import com.virtualspace.service.checkcode.CheckCodeService;

public class CheckCodeServiceImp extends BaseService implements CheckCodeService 
{
	@Override
	public int sendCheckCodeByTelephone(String telephone, int number) throws ServiceException {
		System.out.println(number);
		return number;
	}

	@Override
	public int sendCheckCodeByEmail(String telephone, int number) throws ServiceException {
		System.out.println(number);
		return number;
	}

}
