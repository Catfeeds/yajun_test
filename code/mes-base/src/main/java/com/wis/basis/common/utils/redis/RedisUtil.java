package com.wis.basis.common.utils.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedisPool;

@Service("redisUtil")
public class RedisUtil {
	protected final Log log = LogFactory.getLog(RedisUtil.class);
	@Autowired
	private ShardedJedisPool shardedJedisPool;
	
	
}
