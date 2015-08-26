package com.virtualspace.service.common.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.here.framework.core.redis.RedisPool;
import com.here.framework.log.HLoggerFactory;
import com.here.framework.service.BaseService;
import com.here.framework.service.ServiceException;
import com.virtualspace.database.dao.UserMapper;
import com.virtualspace.database.model.User;
import com.virtualspace.service.common.UserService;

import redis.clients.jedis.Jedis;

public class UserServiceImp extends BaseService implements UserService
{
	private final static String REDIS_USER_NAME = "user";
	
	@Override
	public User getUserByPk(String userpk) throws ServiceException {
		UserMapper mapper = this.getMapper(UserMapper.class);
		return mapper.selectByPrimaryKey(userpk);
	}

	@Override
	public Map<String, String> getUserNamesByPks(List<String> userpks) throws ServiceException {
		
		Map<String, String> resultMap = new HashMap<>();
		if (userpks == null) return resultMap;
		
		Jedis jedis = RedisPool.getBusinessJedis();
		try{
			List<String> names = jedis.hmget(REDIS_USER_NAME, userpks.toArray(new String[userpks.size()]));
			if (names != null && names.size() == userpks.size()) 
			{
				for (int i = 0; i < userpks.size(); i++) 
				{
					String pk = userpks.get(i);
					String name = names.get(i);
					
					if (name == null) 
					{
						User user = this.getUserByPk(pk);
						if (user != null) 
						{
							name = user.getNickName();
						}
						jedis.hset(REDIS_USER_NAME, pk, name);
					}
					resultMap.put(userpks.get(i), name);
				}
			}
			else{
				List<User> users = this.getUsersByPks(userpks);
				Map<String,String> cacheData = new HashMap<>();
				for(User user : users)
				{
					cacheData.put(user.getPk(), user.getNickName());
				}
				jedis.hmset(REDIS_USER_NAME, cacheData);
			}
		}catch(Exception exception)
		{
			HLoggerFactory.getLogger(this.getClass()).error(exception);
		}
		finally{
			RedisPool.returnBusinessJedis(jedis);
		}
		return resultMap;
	}
	
	@Override
	public List<User> getUsersByPks(List<String> userpks) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean insert(User user) throws ServiceException 
	{
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			int result = mapper.insert(user);
			return result > 0?true:false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Boolean update(User user) throws ServiceException {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			int result = mapper.updateByPrimaryKey(user);
			Jedis jedis = RedisPool.getBusinessJedis();
			try {
				jedis.hset(REDIS_USER_NAME,user.getPk(), user.getNickName());
			} catch (Exception e) {
				HLoggerFactory.getLogger(this.getClass()).error(e);
			}
			finally {
				RedisPool.returnBusinessJedis(jedis);
			}	
			return result > 0?true:false;
		} catch (Exception e) {
			return false;
		}
		
	}

	@Override
	public Boolean delete(User user) throws ServiceException {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			mapper.deleteByPrimaryKey(user.getPk());
			Jedis jedis = RedisPool.getBusinessJedis();
			try {
				jedis.hdel(REDIS_USER_NAME,user.getPk());
			} catch (Exception e) {
				HLoggerFactory.getLogger(this.getClass()).error(e);
			}
			finally {
				RedisPool.returnBusinessJedis(jedis);
			}	
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public Boolean isUserExistsByTelephone(String telephone) throws ServiceException 
	{
		UserMapper userMapper = this.getMapper(UserMapper.class);
		int result = userMapper.isUserExistsByTelephone(telephone);
		
		if(result >= 1) return true;
		return false;
	}

	@Override
	public Boolean isUserExistsByEmail(String email) throws ServiceException 
	{
		UserMapper userMapper = this.getMapper(UserMapper.class);
		int result = userMapper.isUserExistsByEmail(email);
		
		if(result >= 1) return true;
		
		return false;
	}

}
