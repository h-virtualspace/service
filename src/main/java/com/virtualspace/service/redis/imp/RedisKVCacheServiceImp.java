package com.virtualspace.service.redis.imp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

import com.here.framework.core.redis.RedisPool;
import com.here.framework.service.BaseService;
import com.virtualspace.redis.service.KVCacheException;
import com.virtualspace.redis.service.RedisKVCacheException;
import com.virtualspace.redis.service.RedisKVCacheService;

public class RedisKVCacheServiceImp extends BaseService implements RedisKVCacheService
{
	@Override
	public Boolean setKeyValue(String key, String value)throws KVCacheException 
	{
		Jedis jedis = this.getJedis();
		
		if (this.getJedis().set(key, value) != null) 
		{
			this.returnJedis(jedis);
			return true;
		}
		return false;
	}

	@Override
	public String getKey(String key) throws KVCacheException {
		Jedis jedis = this.getJedis();
		String result = jedis.get(key);
		this.returnJedis(jedis);
		return result;
	}

	@Override
	public Boolean expire(String key, int seconds) throws KVCacheException 
	{
		Jedis jedis = this.getJedis();
		long result = jedis.expire(key, seconds);
		this.returnJedis(jedis);
		
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean exists(String key) throws KVCacheException {
		Jedis jedis = this.getJedis();
		Boolean result = jedis.exists(key);
		this.returnJedis(jedis);
		
		return result;
	}

	@Override
	public Boolean deleteKeys(List<String> keys) throws KVCacheException {
		Jedis jedis = this.getJedis();
		long result = jedis.del(keys.toArray(new String[keys.size()]));
		this.returnJedis(jedis);
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean hashOneSet(String key, String field, String value)throws KVCacheException {
		Jedis jedis = this.getJedis();
		long result = jedis.hset(key, field, value);
		this.returnJedis(jedis);
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean hashMultiSet(String key, Map<String, String> map)throws KVCacheException {
		Jedis jedis = this.getJedis();
		String result = jedis.hmset(key, map);
		this.returnJedis(jedis);
		if (result != null) 
		{
			return true;
		}
		return false;
	}

	@Override
	public String hashOneGet(String key, String field) throws KVCacheException 
	{
		Jedis jedis = this.getJedis();
		String value = jedis.hget(key, field);
		this.returnJedis(jedis);
		return value;
	}

	@Override
	public Map<String, String> hashGetMap(String key) throws KVCacheException {
		Jedis jedis = this.getJedis();
		Map<String, String> result = jedis.hgetAll(key);
		this.returnJedis(jedis);
		return result;
	}

	@Override
	public String listLeftPop(String key) throws KVCacheException {
		Jedis jedis = this.getJedis();
		String result = jedis.lpop(key);
		this.returnJedis(jedis);
		return result;
	}

	@Override
	public Boolean listLeftPush(String key, List<String> values) throws KVCacheException {
		Jedis jedis = this.getJedis();
		long result = jedis.lpush(key, values.toArray(new String[values.size()]));
		this.returnJedis(jedis);
		
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public String listRightPop(String key) throws KVCacheException {
		Jedis jedis = this.getJedis();
		String result = jedis.rpop(key);
		this.returnJedis(jedis);
		return result;
	}

	@Override
	public Boolean listRightPush(String key, List<String> values) throws KVCacheException {
		Jedis jedis = this.getJedis();
		long result = jedis.rpush(key, values.toArray(new String[values.size()]));
		this.returnJedis(jedis);
		
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public String listIndex(String key, long index) throws KVCacheException {
		Jedis jedis = this.getJedis();
		String result = jedis.lindex(key, index);
		this.returnJedis(jedis);
		return result;
	}

	@Override
	public Boolean setAdd(String key, List<String> values) throws KVCacheException {
		Jedis jedis = this.getJedis();
		long result = jedis.sadd(key, values.toArray(new String[values.size()]));
		this.returnJedis(jedis);
		
		if(result > 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public Boolean setRemove(String key, List<String> values) throws KVCacheException {
		Jedis jedis = this.getJedis();
		long result = jedis.srem(key, values.toArray(new String[values.size()]));
		this.returnJedis(jedis);
		
		if(result > 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public Set<String> getSet(String key) throws KVCacheException {
		Jedis jedis = this.getJedis();
		Set<String> result = jedis.smembers(key);
		this.returnJedis(jedis);
		
		return result;
	}

	@Override
	public Boolean setIsExist(String key, String member) throws KVCacheException {
		Jedis jedis = this.getJedis();
		Boolean result = jedis.sismember(key,member);
		this.returnJedis(jedis);
		
		return result;
	}

	@Override
	public long setSize(String key) throws KVCacheException {
		Jedis jedis = this.getJedis();
		long result = jedis.scard(key);
		
		return result;
	}

	@Override
	public Jedis getJedis() throws RedisKVCacheException 
	{
		Jedis jedis = RedisPool.getBusinessJedis();
		if(jedis == null)
		{
			throw new RedisKVCacheException("无法从连接池中获取jedis");
		}
		
		return jedis;
	}
	@Override
	public Jedis getJedis(String name) throws RedisKVCacheException 
	{
		Jedis jedis = RedisPool.getJedis(name);
		if(jedis == null)
		{
			throw new RedisKVCacheException("无法从连接池中获取jedis");
		}
		
		return jedis;
	}
	/**
     * 归还一个连接
     * 
     * @param jedis
     */
    public void returnJedis(Jedis jedis) 
    {
    	RedisPool.returnBusinessJedis(jedis);
    }
    /**
     * 归还一个连接
     * 
     * @param jedis
     */
    public void returnJedis(String name,Jedis jedis) 
    {
    	RedisPool.returnJedis(name,jedis);
    }
}
